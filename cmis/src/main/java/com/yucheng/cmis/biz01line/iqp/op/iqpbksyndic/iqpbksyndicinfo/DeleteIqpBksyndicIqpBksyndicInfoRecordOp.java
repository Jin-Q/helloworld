package com.yucheng.cmis.biz01line.iqp.op.iqpbksyndic.iqpbksyndicinfo;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteIqpBksyndicIqpBksyndicInfoRecordOp extends CMISOperation {
	
	private final String modelId = "IqpBksyndicInfo";
	private final String mainModel = "IqpBksyndic";
	private final String pk1_name = "pk1";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk1_value, connection);
			String serno = (String)kColl.getDataValue("serno");
			int count=dao.deleteByPk(modelId, pk1_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Record Count: " + count);
			}
			
			/** 插入主表银团信息内容 */  
			double totalAmt = 0;
			/** 通过币种获取币种汇率，将金额折算成人民币金额存入银团信息主表中 */
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			/** 判断是否存在该条业务记录，存在则统计，不存在则新增 */
			IndexedCollection mainIColl = dao.queryList(modelId, " where serno = '"+serno+"'", connection);
			for(int i=0;i<mainIColl.size();i++){
				KeyedCollection subKColl = (KeyedCollection)mainIColl.get(i);
				String prtcpt_curr = (String)subKColl.getDataValue("prtcpt_curr");
				String prtcpt_amt = (String)subKColl.getDataValue("prtcpt_amt");
				KeyedCollection kc = cmisComponent.getHLByCurrType(prtcpt_curr);
				String flag = (String)kc.getDataValue("flag");
				if(flag.equals("success")){
					BigDecimal sld = (BigDecimal)kc.getDataValue("sld");
					String hl = sld.toString();
					double relAmt = Double.parseDouble(hl)*Double.parseDouble(prtcpt_amt);
					totalAmt += relAmt;
				}else {
					throw new Exception("获取币种汇率出错，请检查系统是否配置了所选币种对应汇率信息！");
				}
			}
			KeyedCollection mainKColl = new KeyedCollection();
			mainKColl.addDataField("serno", serno);
			mainKColl.addDataField("bank_syndic_amt", totalAmt);
			mainKColl.setName(mainModel);     
			
			dao.update(mainKColl, connection);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}

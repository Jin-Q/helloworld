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
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpBksyndicIqpBksyndicInfoRecordOp extends CMISOperation {
	 
	private final String modelId = "IqpBksyndicInfo";
	private final String mainModel = "IqpBksyndic";

	/**
	 * 新增银团信息子表信息，新增操作时，同时更新主表信息。
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			String serno = (String)kColl.getDataValue("serno");
			String prtcpt_role = (String)kColl.getDataValue("prtcpt_role");
			String curr = (String)kColl.getDataValue("prtcpt_curr");
			KeyedCollection kcrRes = cmisComponent.getHLByCurrType(curr);
			String flg = (String)kcrRes.getDataValue("flag");
			if("success".equals(flg)){
				/** 从表新增需要效验参与角色，一个银团只能有一个主办行 */
				if(prtcpt_role.equals("01")){
					IndexedCollection checkIColl = dao.queryList(modelId, " where serno = '"+serno+"'", connection);
					if(checkIColl != null && checkIColl.size() > 0){
						for(int i=0;i<checkIColl.size();i++){
							KeyedCollection checkKColl = (KeyedCollection)checkIColl.get(i);
							String role = (String)checkKColl.getDataValue("prtcpt_role");
							if(role.equals("01")){
								throw new EMPException("该银团已经录入了主办行，不能再次录入主办行！");
							}
						}
					}
				}
				
				String pk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				kColl.setDataValue("pk1", pk);
				dao.insert(kColl, connection);
				
				/** 插入主表银团信息内容 */
				double totalAmt = 0;
				/** 通过币种获取币种汇率，将金额折算成人民币金额存入银团信息主表中 */
				
				/** 判断是否存在该条业务记录，存在则统计，不存在则新增 */
				IndexedCollection mainIColl = dao.queryList(modelId, " where serno = '"+serno+"'", connection);
				for(int i=0;i<mainIColl.size();i++){
					KeyedCollection subKColl = (KeyedCollection)mainIColl.get(i);
					String prtcpt_curr = (String)subKColl.getDataValue("prtcpt_curr");
					String prtcpt_amt = (String)subKColl.getDataValue("prtcpt_amt");
					KeyedCollection kc = cmisComponent.getHLByCurrType(prtcpt_curr);
					String flag = (String)kc.getDataValue("flag");
					if("success".equals(flag)){
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
				KeyedCollection isKColl = dao.queryDetail(mainModel, serno, connection);
				String sernoHelp = (String)isKColl.getDataValue("serno");
				if(sernoHelp != null && sernoHelp.trim().length() > 0){
					dao.update(mainKColl, connection);
				}else {
					dao.insert(mainKColl, connection);
				}
				context.addDataField("flag", "success");
				context.addDataField("message", "");
			}else{
				context.addDataField("flag", "error");
				context.addDataField("message", "获取币种汇率出错，请检查系统是否配置了所选币种对应汇率信息！");
			}
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

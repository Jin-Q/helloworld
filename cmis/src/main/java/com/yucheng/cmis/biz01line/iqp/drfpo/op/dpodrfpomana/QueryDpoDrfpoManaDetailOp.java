package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryDpoDrfpoManaDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpDrfpoMana";
	

	private final String drfpo_no_name = "drfpo_no";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
		//	if(this.updateCheck){
			
		//		RecordRestrict recordRestrict = this.getRecordRestrict(context);
		//		recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
		//	}
			
			
			String drfpo_no_value = null;
			try {
				drfpo_no_value = (String)context.getDataValue(drfpo_no_name);
			} catch (Exception e) {}
			if(drfpo_no_value == null || drfpo_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+drfpo_no_name+"] cannot be null!");

			//构建票据池组件类
			DpoDrfpoComponent dpoComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			/**查询票据池中处于在池状态的票据票面金额价值总额*/
		//	Double count = dpoComponent.getDrftAmtByDrfpoNo(drfpo_no_value,"01");
			//待入池票面价值总额
			Double totalCo = dpoComponent.getDrftAmtByDrfpoNo(drfpo_no_value,"00");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, drfpo_no_value, connection);
		//	kColl.addDataField("bill_amt", count);
			kColl.addDataField("to_bill_amt", totalCo);
			//用户名翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id "};
			String[] fieldName=new String[]{"cus_name"};
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id"});
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			
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

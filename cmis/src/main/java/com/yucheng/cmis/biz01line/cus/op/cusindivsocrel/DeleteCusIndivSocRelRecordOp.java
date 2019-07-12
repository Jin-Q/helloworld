package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivSocRelComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteCusIndivSocRelRecordOp extends CMISOperation {

//	private final String modelId = "CusIndivSocRel";
	

	private final String cus_id_name = "cus_id";
	private final String cus_id_rel_name = "cus_id_rel";
	private final String indiv_cus_rel_name = "indiv_cus_rel";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			//个人客户编号
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			//关联客户编号
			String cus_id_rel_value = null;
			try{
				cus_id_rel_value = (String)context.getDataValue(cus_id_rel_name);
			}catch(Exception e){}
			if(cus_id_rel_value == null || cus_id_rel_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_rel_name+"] cannot be null!");
			//关联客户类型
			String indiv_cus_rel_value = null;
			try{
				indiv_cus_rel_value = (String)context.getDataValue(indiv_cus_rel_name);
			}catch(Exception e){}
			if(indiv_cus_rel_value == null || indiv_cus_rel_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+indiv_cus_rel_name+"] cannot be null!");
			
//			TableModelDAO dao = this.getTableModelDAO(context);
//			Map<String,String> pkMap = new HashMap<String,String>();
//			pkMap.put("cus_id",cus_id_value);
//			pkMap.put("cus_id_rel",cus_id_rel_value);
//			pkMap.put("indiv_cus_rel",indiv_cus_rel_value);
//			int count=dao.deleteByPks(modelId, pkMap, connection);
//			if(count!=1){
//				throw new EMPException("Remove Failed! Records :"+count);
//			}
			CusIndivSocRelComponent cusIndivSocrelComponent = (CusIndivSocRelComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVSOCREL, context, connection);
			cusIndivSocrelComponent.deleteCusIndivSocRel(cus_id_value, cus_id_rel_value, indiv_cus_rel_value);
			
//			CusComRelComponent cususComRelComponent = (CusComRelComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMREL,context,connection);
//			cususComRelComponent.deleteCusComRel(cus_id_value,indiv_rel_cert_typ_value,indiv_rl_cert_code_value,input_br_id,modelId);
			flag = "删除成功";
		}catch (EMPException ee) {
			flag = "删除失败";
			throw ee;
		} catch(Exception e){
			flag = "删除失败";
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}

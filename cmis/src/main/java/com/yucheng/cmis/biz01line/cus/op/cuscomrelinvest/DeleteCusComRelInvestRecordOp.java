package com.yucheng.cmis.biz01line.cus.op.cuscomrelinvest;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteCusComRelInvestRecordOp extends CMISOperation {
	
	private final String modelId = "CusComRelInvest";
	
	private final String cus_id_name = "cus_id";
	private final String cus_id_rel_name = "cus_id_rel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);

			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
				
			String cus_id_rel_value = null;
			try {
				cus_id_rel_value = (String)context.getDataValue(cus_id_rel_name);
			} catch (Exception e) {}
			if(cus_id_rel_value == null || cus_id_rel_value.length() == 0)
				throw new EMPJDBCException("The value of["+cus_id_rel_name+"] cannot be null!");
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("cus_id_rel",cus_id_rel_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("删除失败，影响了"+1+"条记录");
			}
			
//			CusComRelComponent cususComRelComponent = (CusComRelComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMREL,context,connection);
//			cususComRelComponent.deleteCusComRel(cus_id_value,"20",com_inv_inst_code_value,input_br_id_value,modelId);
			
			String modelIdRel="CusComRelApital";
			String cusId="";
			String cusIdRel="";
//			String cert_typ="";
//			String cert_code="";
//			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			cusId=(String) context.getDataValue("cus_id_rel");
			cusIdRel=(String) context.getDataValue("cus_id");
//			CusBase cusBase=cusBaseComponent.getCusBase(cusIdRel);
//			if(cusBase==null||cusBase.getCusId()==null){
//				throw new EMPException("被投资客户码对应的客户信息不存在！");
//			}else{
//				cert_typ=cusBase.getCertType();
//				cert_code=cusBase.getCertCode();
//			}
			
			Map<String,String> pkMapRel = new HashMap<String,String>();
			pkMapRel.put("cus_id",cusId);
			pkMapRel.put("cus_id_rel",cusIdRel);
			count=dao.deleteByPks(modelIdRel, pkMapRel, connection);
			 
			flag = "删除成功";
		}catch (EMPException ee) {
			flag = "删除失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}

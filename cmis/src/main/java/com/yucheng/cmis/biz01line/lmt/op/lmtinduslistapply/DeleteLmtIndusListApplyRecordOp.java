package com.yucheng.cmis.biz01line.lmt.op.lmtinduslistapply;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtIndusListApplyRecordOp extends CMISOperation {

	private final String modelId = "LmtIndusListApply";
	

	private final String serno_name = "serno";
	private final String cus_id_name = "cus_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String serno_value = null;
			String[] cus_id_value = null;
			String records = PUBConstant.SUCCESS;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				cus_id_value = (context.getDataValue(cus_id_name).toString()).split(",");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			if(cus_id_value.length == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			
			LmtPubComponent lmtComponent = (LmtPubComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context, connection);
			for(int i = 0 ;i < cus_id_value.length;i++){
				String nums = lmtComponent.getAgrno("checkDeleteIndusList",cus_id_value[i]);
				if(!nums.equals("0")){
					records = PUBConstant.FAIL;
				}
			}
			
			if(records.equals("success")){
				Map<String,String> pkMap = new HashMap<String,String>();
				pkMap.put("serno",serno_value);
				for(int i = 0 ;i < cus_id_value.length;i++){
					pkMap.put("cus_id",cus_id_value[i]);
					dao.deleteByPks(modelId, pkMap, connection);
				}
			}
			
			context.addDataField("flag", records);
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
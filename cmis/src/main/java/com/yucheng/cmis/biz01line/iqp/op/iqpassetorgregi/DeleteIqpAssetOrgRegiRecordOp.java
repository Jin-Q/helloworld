package com.yucheng.cmis.biz01line.iqp.op.iqpassetorgregi;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAssetOrgRegiRecordOp extends CMISOperation {

	private final String modelId = "IqpAssetOrgRegi";
	

	private final String serno_name = "serno";
	private final String asset_org_id_name = "asset_org_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			String asset_org_id_value = null;
			try {
				asset_org_id_value = (String)context.getDataValue(asset_org_id_name);
			} catch (Exception e) {}
			if(asset_org_id_value == null || asset_org_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+asset_org_id_name+"] cannot be null!");


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("asset_org_id",asset_org_id_value);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			context.addDataField("flag", "success");
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

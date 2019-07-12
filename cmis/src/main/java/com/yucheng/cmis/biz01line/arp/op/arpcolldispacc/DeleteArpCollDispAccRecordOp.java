package com.yucheng.cmis.biz01line.arp.op.arpcolldispacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteArpCollDispAccRecordOp extends CMISOperation {

	private final String modelId = "ArpCollDispAcc";
	

	private final String asset_disp_no_name = "asset_disp_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String asset_disp_no_value = null;
			try {
				asset_disp_no_value = (String)context.getDataValue(asset_disp_no_name);
			} catch (Exception e) {}
			if(asset_disp_no_value == null || asset_disp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+asset_disp_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, asset_disp_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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

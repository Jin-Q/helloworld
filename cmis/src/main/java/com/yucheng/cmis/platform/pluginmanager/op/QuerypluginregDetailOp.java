package com.yucheng.cmis.platform.pluginmanager.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QuerypluginregDetailOp  extends CMISOperation {
	
	private final String modelId = "pluginreg";
	

	private final String plugin_modual_name_id = "plugin_modual_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String plugin_modual_name_value = null;
			try {
				plugin_modual_name_value = (String)context.getDataValue(plugin_modual_name_id);
			} catch (Exception e) {}
			if(plugin_modual_name_value == null || plugin_modual_name_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+plugin_modual_name_id+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, plugin_modual_name_value, connection);
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

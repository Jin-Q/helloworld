package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpAssertRelOp  extends CMISOperation {
	private final String modelId="IqpAsset";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String asset_no="";
		try{
			connection = this.getConnection(context);
		    try{
		    	asset_no =(String)context.getDataValue("asset_no");
		    } catch (Exception e) {}
		      if(asset_no == null || asset_no=="")
		    	  throw new EMPJDBCException("The values cannot be empty!");
		          
		          TableModelDAO dao = this.getTableModelDAO(context);
		      	  KeyedCollection kColl = dao.queryDetail(modelId, asset_no, connection);
		      	  String takeover_type = (String)kColl.getDataValue("takeover_type");
				  context.setDataValue("asset_no", asset_no); 
				  context.addDataField("takeover_type", takeover_type); 
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

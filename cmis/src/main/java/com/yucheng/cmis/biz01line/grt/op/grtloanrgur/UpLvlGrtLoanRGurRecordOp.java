package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpLvlGrtLoanRGurRecordOp extends CMISOperation {
	

	private final String modelId = "GrtLoanRGur";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_id = "";
			String pk_up = "";
			try {
				pk_id = (String)context.getDataValue("pk_id");
				pk_up = (String)context.getDataValue("pk_up");
			} catch (Exception e) {}
			if((pk_id == null || pk_id == "")&&(pk_up == null || pk_up == "") )
				throw new EMPJDBCException("The values to "+pk_id+" or "+pk_up+" cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
  /**担保等级互换**/
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id, connection);
			KeyedCollection upKColl = dao.queryDetail(modelId, pk_up, connection);
			String guar_lvl = (String)kColl.getDataValue("guar_lvl");
			String up_guar_lvl = (String)upKColl.getDataValue("guar_lvl");
			kColl.setDataValue("guar_lvl", up_guar_lvl);
			upKColl.setDataValue("guar_lvl", guar_lvl);
  /**保存互换后的数据**/			
			int count=dao.update(kColl, connection);
			int upCount=dao.update(upKColl, connection);
			if(count!=1 && upCount!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
             context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error"); 
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

package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckRoleOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpBksyndicInfo";
	
	/**
	 * bussiness logic operation  
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno = null;
			String prtcpt_role = null;
			try {
				serno = (String)context.getDataValue("serno");
				prtcpt_role = (String)context.getDataValue("prtcpt_role");
			} catch (Exception e) {}
			if(serno == null || "".equals(serno) || prtcpt_role == null || "".equals(prtcpt_role)){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String condition = "where serno='"+serno+"' and prtcpt_role='01'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0 && iColl != null){
				context.addDataField("flag", "error");
			}else{
				context.addDataField("flag", "success");
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

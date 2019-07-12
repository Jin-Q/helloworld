package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckCancelForZGEOp extends CMISOperation {
	
	private final String modelId = "RLmtAppGuarCont";       
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no = null;
			String serno = null;
			try { 
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(guar_cont_no == null || "".equals(guar_cont_no) || serno == null || "".equals(serno))
				throw new EMPJDBCException("The values pk_id or corre_rel cannot be empty!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where guar_cont_no = '"+guar_cont_no+"' and serno = '"+serno+"' and corre_rel != '3'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				context.addDataField("flag", "error");
			}else{
				context.addDataField("flag", "success");
			}
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

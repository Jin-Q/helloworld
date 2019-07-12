package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckRLmtGuarContOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "RLmtAppGuarCont"; 
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no ="";
			//String limit_code = "";
			try {
				 guar_cont_no = (String)context.getDataValue("guar_cont_no");
				// limit_code = (String)context.getDataValue("limit_code");
			} catch (Exception e) {}
			if(guar_cont_no == null || guar_cont_no=="")
				throw new EMPJDBCException("The values to ["+guar_cont_no+"] cannot be empty!");
			
			String condition = "where guar_cont_no='"+guar_cont_no+"'";
			TableModelDAO dao = this.getTableModelDAO(context); 
			/**查询此笔业务下是否已引入此担保合同*/
			IndexedCollection iCollCont =  dao.queryList(modelId, condition, connection);
			if(iCollCont.size()>0){ 
				context.addDataField("flag","error");     
			}else{
				context.addDataField("flag","success");
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

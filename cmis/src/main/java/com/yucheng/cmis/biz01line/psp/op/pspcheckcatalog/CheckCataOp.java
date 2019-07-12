package com.yucheng.cmis.biz01line.psp.op.pspcheckcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckCataOp extends CMISOperation {
	

	private final String modelId = "PspSchCatRel";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String catalog_id = null;
			String scheme_id = null;
			try {
				catalog_id = (String)context.getDataValue("catalog_id");
				scheme_id = (String)context.getDataValue("scheme_id");
			} catch (Exception e) {}
			if(catalog_id == null || catalog_id.length() == 0)
				throw new EMPJDBCException("The values to catalog_id cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where catalog_id='"+catalog_id+"' and scheme_id='"+scheme_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl!=null&&iColl.size()>0){   
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					scheme_id = (String)kColl.getDataValue("scheme_id");
					if(scheme_id!=null || !scheme_id.equals("")){
						context.addDataField("flag", "error"); 
					}else{
						context.addDataField("flag", "success"); 
					}
				} 
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

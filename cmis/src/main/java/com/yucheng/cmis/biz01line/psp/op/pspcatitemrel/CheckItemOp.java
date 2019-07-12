package com.yucheng.cmis.biz01line.psp.op.pspcatitemrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckItemOp extends CMISOperation {
	

	private final String modelId = "PspCatItemRel";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String item_id = null;
			String catalog_id = null;
			try {
				item_id = (String)context.getDataValue("item_id");
				catalog_id = (String)context.getDataValue("catalog_id");
			} catch (Exception e) {}
			if(item_id == null || item_id.length() == 0)
				throw new EMPJDBCException("The values to item_id cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where item_id='"+item_id+"' and catalog_id='"+catalog_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl!=null&&iColl.size()>0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					catalog_id = (String)kColl.getDataValue("catalog_id");
					if(catalog_id!=null || !catalog_id.equals("")){
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

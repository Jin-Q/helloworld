package com.yucheng.cmis.biz01line.lmt.op.lmtintbnkapp.lmtintbnkappdetails;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryLmtIntbnkAppLmtIntbnkAppDetailsListOp extends CMISOperation {
	
	private final String modelId = "LmtIntbnkAppDetails";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = (String)context.getDataValue("LmtIntbnkApp.serno");
			
			if(serno_value==null){
				throw new EMPException("parent primary key not found!");
			}
			
			
			String conditionStr = "where serno = '" + serno_value+"' order by serno desc,crd_item_id desc";
			
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
		iColl.setName(iColl.getName()+"List");
		this.putDataElement2Context(iColl, context);	
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

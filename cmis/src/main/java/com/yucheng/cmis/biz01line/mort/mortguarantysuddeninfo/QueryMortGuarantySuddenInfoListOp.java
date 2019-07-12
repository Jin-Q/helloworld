package com.yucheng.cmis.biz01line.mort.mortguarantysuddeninfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantySuddenInfoListOp extends CMISOperation {


	private final String modelId = "MortGuarantySuddenInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guaranty_no = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("subMenuId")){
				context.setDataValue("subMenuId", "ywqk");
			}
			guaranty_no = (String) context.getDataValue("guaranty_no");
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"";
			
		
		//	RecordRestrict recordRestrict = this.getRecordRestrict(context);
		//	conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			conditionStr +="where guaranty_no = '"+guaranty_no+"'";
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

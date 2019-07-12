package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizareasupmk;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppBizAreaSupmkListOp extends CMISOperation {


	private final String modelId = "LmtAppBizAreaSupmk";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String serno = null;
			KeyedCollection queryData = null;
			String canWr = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			try {
				serno = (String) context.getDataValue("serno");
				context.setDataValue("serno", serno);
			} catch (Exception e) {}
			
			try {
				canWr = (String) context.getDataValue("canWr");
				context.setDataValue("canWr", canWr);
			} catch (Exception e) {}
			
			int size = 10;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(serno != null)
				if(conditionStr.toUpperCase().contains("WHERE")){
					conditionStr += " and serno ='" + serno + "'" + "order by serno desc";
				}else{
					conditionStr += " where serno ='" + serno + "'" + "order by serno desc";
				}
			else
				if(conditionStr.toUpperCase().contains("WHERE")){
					conditionStr += " and 1 =1 order by serno desc";
				}else{
					conditionStr += " where 1=1 order by serno desc";
				}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
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

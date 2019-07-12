package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtFrozenDetailsListOp extends CMISOperation {

	private final String modelIdApp = "LmtAppDetails";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelIdApp);
			} catch (Exception e) {}
			String serno = (String)context.getDataValue("serno");
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String conditionStrApp = TableModelUtil.getQueryCondition(this.modelIdApp, queryData, context, false, false, false)+"";
			if(conditionStrApp==null || "".endsWith(conditionStrApp)){
				conditionStrApp = "where 1=1 and serno ='"+ serno+"'";
			}else{
				conditionStrApp = conditionStrApp + "and serno =" + serno;
			}
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("sub_type");
			list.add("limit_code");
			list.add("limit_type");
			list.add("guar_type");
			list.add("crd_amt");
			list.add("term_type");
			list.add("term");
			list.add("org_limit_code");
			list.add("update_flag");
			list.add("froze_amt");
			list.add("unfroze_amt");
			IndexedCollection iColl = dao.queryList(modelIdApp,list ,conditionStrApp,connection);
			
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

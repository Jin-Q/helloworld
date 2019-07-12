package com.yucheng.cmis.biz01line.homepage.workflow;

import java.sql.Connection;
import java.util.ArrayList;

import javax.sql.DataSource;

 
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dao.SqlOperator;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

/**
 * homePage小工具类，生成待办列表相关数据
 * @author JackYu
 *
 */
public class QueryHomePageWorkFlowListToDoOp extends CMISOperation{

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = this.getConnection(context);
		//TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
		DataSource dataSource = this.getDataSource(context);
		
		
		KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME); 
		
		IndexedCollection typeIColl = (IndexedCollection)dictColl.getDataElement("ZB_BIZ_CATE");
		String key="";
		for(java.util.Iterator it = typeIColl.iterator();it.hasNext();){
			KeyedCollection kc = (KeyedCollection) it.next();
			String ename=(String) kc.getDataValue("enname");
			key=key+"'"+ename+"',";
		}
	 
		if(key !=null && key.length()>1){
			key = key.substring(0,key.length()-1);
		}
 
		/** 当前登录用户 */
		String CurrentUserID = (String) context.get("currentuserid");
		
		StringBuffer sb = new StringBuffer();
		sb.append("select wfi.appl_type as apply_type,  count(wfi.appl_type) as count")
		  .append("	from wfi_worklist_todo wfi ")
		  .append("where wfi.WFStatus != '2'")
		  .append("	and wfi.WFStatus != '3'")
		  .append("	and wfi.appsign != '2'")
		  .append("	and wfi.appsign != '3'")
		  .append("	and wfi.prenodeid != 'WFBEGIN'")
		  .append("	and wfi.CurrentNodeUser like '%"+CurrentUserID+"%' ")
		  .append("	and wfi.APPL_TYPE in ("+key+") ")
		  .append(" group by wfi.APPL_TYPE ");
		
	 
		SqlOperator sqlOp = SqlOperator.createSqlOperator(sb.toString(), dataSource);
		KeyedCollection kColl = sqlOp.executeSQL(new ArrayList());
		IndexedCollection iColl = (IndexedCollection) kColl.get("resultSet");
		iColl.setName("WorkFlowToDoList");
		
		
		//查询数据放回CONTEXT
		this.putDataElement2Context(iColl, context);
		
		
		return "0";
	}

}

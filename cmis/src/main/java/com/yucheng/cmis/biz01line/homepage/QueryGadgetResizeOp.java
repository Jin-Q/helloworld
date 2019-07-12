package com.yucheng.cmis.biz01line.homepage;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dao.SqlOperator;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * 首页Gadget最大化数据处理OP
 * @author JackYu
 *
 */
public class QueryGadgetResizeOp extends CMISOperation{

	@Override
	public String doExecute(Context context) throws EMPException {
		
		//通过首页的快捷菜单访问Gadget，解决Gadget的title中文传递
		if(context.containsKey("gadgetId")){
			String gadgetId = (String)context.getDataValue("gadgetId");
			
			DataSource dataSource = this.getDataSource(context);
			StringBuffer sb = new StringBuffer();
			sb.append("select ")
			  .append("		 gadget_title as title,")
			  .append("		 gadget_url_resize as url ")
			  .append("from homepage_gadget ")
			  .append("where gadget_id ='"+gadgetId+"'");
			
			SqlOperator sqlOp = SqlOperator.createSqlOperator(sb.toString(), dataSource);
			KeyedCollection kColl = sqlOp.executeSQL(new ArrayList());
			//得到指定月的消费金额
			IndexedCollection iColl = (IndexedCollection) kColl.get("resultSet");
			
			if(context.containsKey("gadgetTitle"))
				context.setDataValue("gadgetTitle", (String)((KeyedCollection)iColl.get(0)).getDataValue("title"));
			else
				context.addDataField("gadgetTitle", (String)((KeyedCollection)iColl.get(0)).getDataValue("title"));
			
			if(context.containsKey("gadgetUrl"))
				context.setDataValue("gadgetUrl", (String)((KeyedCollection)iColl.get(0)).getDataValue("url"));
			else
				context.addDataField("gadgetUrl", (String)((KeyedCollection)iColl.get(0)).getDataValue("url"));
			
		}
		
		return "0";
	}

}

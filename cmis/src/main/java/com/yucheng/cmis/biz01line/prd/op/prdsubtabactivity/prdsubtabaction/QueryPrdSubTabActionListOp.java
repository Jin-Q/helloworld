package com.yucheng.cmis.biz01line.prd.op.prdsubtabactivity.prdsubtabaction;

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

public class QueryPrdSubTabActionListOp extends CMISOperation {
	private final String modelId = "PrdSubTabAction";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			/** 取得主、从资源ID，置于context中 */
			String mainid = "";
			String subid = "";
			if(context.containsKey("mainid")){
				mainid = (String)context.getDataValue("mainid");
			}
			if(context.containsKey("subid")){
				subid = (String)context.getDataValue("subid");
			}
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
								+"";
			if("".equals(conditionStr)){
				conditionStr = " where main_id = '"+mainid+"' and sub_id = '"+subid+"' ";
			}else {
				conditionStr += "and main_id = '"+mainid+"' and sub_id = '"+subid+"' ";
			}
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List list = new ArrayList();
			list.add("pkid");
			list.add("main_act_id");
			list.add("sub_act_id");
			list.add("memo");
			list.add("input_id");
			list.add("input_date");
			list.add("input_br_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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

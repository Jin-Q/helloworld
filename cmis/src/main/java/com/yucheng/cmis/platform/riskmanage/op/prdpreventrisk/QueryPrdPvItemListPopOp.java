package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPvItemListPopOp extends CMISOperation {
	private final String modelId = "PrdPvRiskItem";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			String preventId = (String)context.getDataValue("preventId");
			if(preventId == null){
				throw new EMPException("获取拦截方案ID失败，请检查！");
			}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection relIColl = dao.queryList("PrdPvRiskItemRel", " where prevent_id='"+preventId+"'", connection);
			String sqlHelp = "";
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)relIColl.get(i);
					String itemId = (String)kc.getDataValue("item_id");
					if(itemId != null && itemId != ""){
						sqlHelp += "'"+itemId+"',";
					}
				}
				if(sqlHelp.length()>0){
					sqlHelp = "("+sqlHelp.substring(0, sqlHelp.length()-1)+")";
				}
				
				if(conditionStr != null && conditionStr.trim().length() > 0){
					if(sqlHelp.length() > 0){
						conditionStr = conditionStr +" and item_id not in "+sqlHelp;
					}
				}else {
					if(sqlHelp.length() > 0){
						conditionStr = " where item_id not in "+sqlHelp;
					}
				}
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			List<String> list = new ArrayList<String>();
			list.add("item_id");
			list.add("item_name");
			list.add("used_ind");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName("PrdPvRiskItemList");
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

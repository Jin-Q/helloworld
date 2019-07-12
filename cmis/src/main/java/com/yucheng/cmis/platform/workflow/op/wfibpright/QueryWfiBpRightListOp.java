package com.yucheng.cmis.platform.workflow.op.wfibpright;

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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryWfiBpRightListOp extends CMISOperation {

	private final String modelId = "WfiBpRight";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			Boolean app_flag = false;
			String app_type = null;
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			if(queryData!=null && queryData.containsKey("app_type")){
				app_type = (String)queryData.getDataValue("app_type");
				if(app_type!=null && !"".equals(app_type)){
					app_flag = true;
				}
				queryData.remove("app_type");
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if(app_flag){//将app_type拼入where条件中
				if(conditionStr==null || "".equals(conditionStr)){
					conditionStr = " where app_type='"+app_type+"'";
				}else{
					conditionStr += " and app_type='"+app_type+"'";
				}
			}
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("approve_type");
			list.add("approve_org");
			list.add("approve_duty");
			list.add("approver");
			list.add("cus_type");
			list.add("app_type");
			list.add("biz_type");
			list.add("sig_amt");
			list.add("open_amt");
			list.add("single_amt");
			list.add("pk1");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[] { "approve_org" });
			SInfoUtils.addUSerName(iColl, new String[] { "approver" });
			SInfoUtils.addDutyName(iColl, new String[] { "approve_duty" });
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

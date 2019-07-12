package com.yucheng.cmis.biz01line.ccr.op.ccrappinbank;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCcrAppInBankListOp extends CMISOperation {


	private final String modelId = "CcrAppInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusSameOrg"};
			String[] modelForeign=new String[] { "cus_id" };
			String[] fieldName=new String[]{"same_org_cnname"};

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String temp=SystemTransUtils.dealQueryName(queryData, args, context, modelIds,modelForeign, fieldName);
			if(temp!=null&&temp.length()>0){
				temp=" and "+temp;
			}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
			//conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
			if(conditionStr==null||"".equals(conditionStr.trim())){
				conditionStr += " where flag = '3' "+temp+" order by serno desc";
				
			}else{
				conditionStr += " and flag = '3' "+temp+" order by serno desc";
			}
			conditionStr=recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id"});
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			

			
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

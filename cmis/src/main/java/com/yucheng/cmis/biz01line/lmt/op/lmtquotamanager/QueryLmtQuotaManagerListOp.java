package com.yucheng.cmis.biz01line.lmt.op.lmtquotamanager;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtQuotaManagerListOp extends CMISOperation {


	private final String modelId = "LmtQuotaManager";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false)
									+"order by serno desc";
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("code_id");
			list.add("prd_id");
			list.add("single_amt_quota");
			list.add("sig_amt_quota");
			list.add("sig_loan_quota");
			list.add("sig_use_quota");
			list.add("manager_br_id");
			list.add("manager_id");
			list.add("start_date");
			list.add("end_date");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			
			String menuId = context.getDataValue("menuId").toString();
			if(menuId.equals("OrgLmtQuota")){
				SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" ,"code_id"});
				SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
			}else{
				SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
				SInfoUtils.addUSerName(iColl, new String[] { "manager_id","code_id" });
			}
			
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

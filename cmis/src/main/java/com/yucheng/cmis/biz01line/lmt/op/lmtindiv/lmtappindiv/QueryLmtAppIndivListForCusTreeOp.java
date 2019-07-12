package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppIndivListForCusTreeOp extends CMISOperation {

	private final String modelId = "LmtAppIndiv";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			int size = 15;
		
			if(null != conditionStr && !"".equals(conditionStr)){
				conditionStr += " AND APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}else{
				conditionStr += " WHERE APP_TYPE NOT IN('03','04') ";  //过滤冻结、解冻申请
			}
			
			if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){
				conditionStr +=" AND CUS_ID='"+context.getDataValue("cus_id").toString()+"' ";
			}else{
				throw new EMPException("查询客户授信否决历史出错，传入客户码[cus_id]不能为空。");
			}
			conditionStr +=" AND APPROVE_STATUS='998' ";
			
			conditionStr += " order by serno desc";
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("lrisk_type");
			list.add("crd_totl_amt");
			list.add("app_type");
			list.add("is_open_pos");
			list.add("input_id");
			list.add("input_br_id");
			list.add("approve_status");
			list.add("manager_id");
			list.add("manager_br_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id" });
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			ee.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "查询失败:"+ee.getMessage(), null);
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnStr;
	}

}

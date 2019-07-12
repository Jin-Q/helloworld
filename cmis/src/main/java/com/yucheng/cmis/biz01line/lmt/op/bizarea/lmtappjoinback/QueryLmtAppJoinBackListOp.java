package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappjoinback;

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
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppJoinBackListOp extends CMISOperation {


	private final String modelId = "LmtAppJoinBack";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
//			String process = null;
//			try {
//				process = (String)context.getDataValue("process"); //是否为查询列表
//				context.setDataValue("process", process);
//			} catch (Exception e) {}
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)+" order by serno desc";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
//			if( "yes".equals(process) ){
//				//过滤掉待发起 b  查询
//				if(conditionStr.toUpperCase().contains("WHERE")){
//					conditionStr += " and APPROVE_STATUS != '000' order by serno desc";
//				}else{
//					conditionStr += " where APPROVE_STATUS != '000' order by serno desc";
//				}
//			}else if ("no".equals(process)){
//				//只看到 待发起  和  退回  申请list
//				if(conditionStr.toUpperCase().contains("WHERE")){
//					conditionStr += " and APPROVE_STATUS in ('000','992') order by serno desc";
//				}else{
//					conditionStr += " where APPROVE_STATUS in ('000','992') order by serno desc";
//				}
//			}
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo, connection);
			
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] { "agr_no" };
			String[] modelIds=new String[]{"LmtAgrBizArea"};
			String[] modelForeign=new String[]{"agr_no"};
			String[] fieldName=new String[]{"biz_area_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
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

package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtagrindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrIndivListOp extends CMISOperation {


	private final String modelId = "LmtAgrIndiv";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			/** 授信协议循环总金额、一次性总金额、授信总额 从台账中实时获取   2013-12-17  唐顺岩  */
			String select_sql ="SELECT SERNO, AGR.AGR_NO, CUS_ID, TOTL_START_DATE, TOTL_END_DATE, GUAR_TYPE, MANAGER_BR_ID, MANAGER_ID, SELF_AMT, DET.CRD_TOTL_AMT FROM LMT_AGR_INDIV AGR,";
			select_sql += " (SELECT AGR_NO,SUM(CIR_AMT) CRD_CIR_AMT,SUM(ONE_AMT) CRD_ONE_AMT,SUM(CIR_AMT + ONE_AMT) CRD_TOTL_AMT";
			select_sql += " FROM (SELECT AGR_NO, SUM(DECODE(LIMIT_TYPE, '01', NVL(CRD_AMT,0), 0)) CIR_AMT, SUM(DECODE(LIMIT_TYPE,'02', NVL(CRD_AMT,0), 0)) ONE_AMT FROM LMT_AGR_DETAILS";
			select_sql += " GROUP BY AGR_NO ,LIMIT_TYPE) GROUP BY AGR_NO ) DET ";
			//如果查询条件包含协议号 将协议号查询条件放到协议表取数
			if(conditionStr.indexOf("agr_no =")>0 || conditionStr.indexOf("agr_no=")>0){
				conditionStr = conditionStr.replace("agr_no =", "AGR.AGR_NO=").replace("agr_no=", "AGR.AGR_NO=");
			}
			
			select_sql += conditionStr;
			select_sql += " AND DET.AGR_NO = AGR.AGR_NO ";
			select_sql += " ORDER BY TOTL_START_DATE,SERNO,AGR_NO DESC";
			
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo,this.getDataSource(context), select_sql);
			
//			List<String> list = new ArrayList<String>();
//			list.add("agr_no");
//			list.add("serno");
//			list.add("cus_id");
//			list.add("crd_totl_amt");
//			list.add("totl_start_date");
//			list.add("totl_end_date");
//			list.add("guar_type");
//			list.add("self_amt");
//			list.add("manager_br_id");
//			list.add("manager_id");
//			
//			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
//			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			/** END */
			
			iColl.setName("LmtAgrIndivList");
			
			SInfoUtils.addSOrgName(iColl, new String[] {"manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] {"manager_id"});
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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

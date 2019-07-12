package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

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

public class QueryLmtFrozenApplyListOp extends CMISOperation {

	private final String modelId = "LmtApply";

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
			
//			if(conditionStr==null || "".equals(conditionStr)){
//				conditionStr = " where app_type in ('03','04') order by serno desc "; 
//			}else {
//				conditionStr = conditionStr  + " and app_type in ('03','04') order by serno desc ";
//			}
//			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
//			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
//			List<String> list = new ArrayList<String>();
//			list.add("serno");
//			list.add("cus_id");
//			list.add("app_type");
//			list.add("crd_totl_amt");
//			list.add("crd_cir_amt");
//			list.add("crd_one_amt");
//			list.add("app_date");
//			list.add("input_id");
//			list.add("input_br_id");
//			list.add("approve_status");
//			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
//			iColl.setName(iColl.getName()+"List");
//			
//			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
//			SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
//			
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
//			//详细信息翻译时调用			
//			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
//			for(int i=0;i<iColl.size();i++){
//				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
//				String appType = (String)kColl.getDataValue("app_type");
//				String serno = (String)kColl.getDataValue("serno");
//				Map<String,String> modelMap=new HashMap<String,String>();
//				modelMap.put("IN_申请流水号", serno);
//				Map<String,String> outMap=new HashMap<String,String>();
//				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//				ShuffleServiceInterface shuffleService = null;
//				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
//				
//				if("03".equals(appType)){
//					outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETFROZENAMTAPPDET", modelMap);
//					float frozeAmt = Float.parseFloat(outMap.get("OUT_冻结金额").toString()) ;
//					kColl.addDataField("frozen_amt", frozeAmt);
//				}else if("04".equals(appType)){
//					outMap=shuffleService.fireTargetRule("LMTFROZEN", "GETUNFROZENAMTAPPDET", modelMap);
//					float unfrozenAmt = Float.parseFloat(outMap.get("OUT_解冻金额").toString()) ;
//					kColl.addDataField("unfroze_amt", unfrozenAmt);
//				}
//			}
			
			/** 授信协议循环总金额、一次性总金额、授信总额 从台账中实时获取   2013-12-17  唐顺岩  */
			String select_sql ="SELECT SERNO, AGR.AGR_NO, CUS_ID, APP_TYPE, APP_DATE, APPROVE_STATUS, MANAGER_BR_ID, MANAGER_ID, DET.CRD_CIR_AMT, DET.CRD_ONE_AMT, DET.CRD_TOTL_AMT FROM LMT_APPLY AGR,";
			select_sql += " (SELECT AGR_NO AS AGR_NO1,SUM(CIR_AMT) CRD_CIR_AMT,SUM(ONE_AMT) CRD_ONE_AMT,SUM(CIR_AMT + ONE_AMT) CRD_TOTL_AMT";
			select_sql += " FROM (SELECT AGR_NO, SUM(DECODE(LIMIT_TYPE, '01', NVL(CRD_AMT,0), 0)) CIR_AMT, SUM(DECODE(LIMIT_TYPE,'02', NVL(CRD_AMT,0), 0)) ONE_AMT FROM LMT_AGR_DETAILS";
			if(((String)context.getDataValue("menuId")).indexOf("grp")>=0){  //集团授信时只需统计非低风险额度
				select_sql += " WHERE LRISK_TYPE='20'";
			}
			select_sql += " GROUP BY AGR_NO ,LIMIT_TYPE) GROUP BY AGR_NO ) DET ";
			
			select_sql += conditionStr;
			select_sql += " AND AGR.APP_TYPE IN ('03','04') ";
			select_sql += " AND DET.AGR_NO1 = AGR.AGR_NO ";
			select_sql += " ORDER BY SERNO DESC";
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo,this.getDataSource(context), select_sql);
			
			/** END */
			
			SInfoUtils.addSOrgName(iColl, new String[] {"manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] {"manager_id"});
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iColl.setName("LmtApplyList");
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

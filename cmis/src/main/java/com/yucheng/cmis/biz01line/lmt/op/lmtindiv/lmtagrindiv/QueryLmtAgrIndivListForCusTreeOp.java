package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtagrindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrIndivListForCusTreeOp extends CMISOperation {


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
		
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			String cus_id = "";
			if(context.containsKey("cus_id")&&!"".equals(context.getDataValue("cus_id"))){
				cus_id = (String) context.getDataValue("cus_id");
			}
			
			/** 授信协议循环总金额、一次性总金额、授信总额 从台账中实时获取   2013-12-17  唐顺岩  */
			/** 应审批官郑启雄要求，个人客户信息里面的存量授信，延长6个月后未到期的全部显示  2015-08-25 Edited by FCL**/
			String select_sql ="SELECT SERNO, AGR.AGR_NO, CUS_ID, TOTL_START_DATE, TOTL_END_DATE, GUAR_TYPE, MANAGER_BR_ID, MANAGER_ID, SELF_AMT, DET.CRD_TOTL_AMT FROM LMT_AGR_INDIV AGR,";
			select_sql += " (SELECT AGR_NO,SUM(CIR_AMT) CRD_CIR_AMT,SUM(ONE_AMT) CRD_ONE_AMT,SUM(CIR_AMT + ONE_AMT) CRD_TOTL_AMT";
			select_sql += " FROM (SELECT AGR_NO, SUM(DECODE(LIMIT_TYPE, '01', NVL(CRD_AMT,0), 0)) CIR_AMT, SUM(DECODE(LIMIT_TYPE,'02', NVL(CRD_AMT,0), 0)) ONE_AMT FROM LMT_AGR_DETAILS WHERE LMT_STATUS!='30' AND TO_CHAR(ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-MM-dd'), 6),'yyyy-MM-dd')>=(SELECT OPENDAY FROM PUB_SYS_INFO) ";
			select_sql += " GROUP BY AGR_NO ,LIMIT_TYPE) GROUP BY AGR_NO ) DET ";
			//如果查询条件包含协议号 将协议号查询条件放到协议表取数
			if(conditionStr.indexOf("agr_no =")>0 || conditionStr.indexOf("agr_no=")>0){
				conditionStr = conditionStr.replace("agr_no =", "AGR.AGR_NO=").replace("agr_no=", "AGR.AGR_NO=");
			}
			
			if(conditionStr == null || "".equals(conditionStr)){
				conditionStr = "where cus_id ='"+cus_id+"' ";
			}else{
				conditionStr += "and cus_id ='"+cus_id+"' ";
			}
			
			select_sql += conditionStr;
			select_sql += " AND DET.AGR_NO = AGR.AGR_NO ";
			select_sql += " ORDER BY AGR_NO DESC";
			
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
			ee.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "查询失败:"+ee.getMessage(), null);
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

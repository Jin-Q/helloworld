package com.yucheng.cmis.biz01line.lmt.op.lmtagrinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrInfoListOp extends CMISOperation {

	private final String modelId = "LmtAgrInfo";
	private final String guarModelId = "LmtAgrFinGuar";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			//判断是否担保公司客户，担保公司客户需取融资性担保公司授信协议  start（参考QueryLmtAgrFinGuarListOp.java）
			String cus_id = "";
			String cus_type = "";
			if(context.containsKey("cus_id")&&!"".equals(context.getDataValue("cus_id"))){
				cus_id = (String) context.getDataValue("cus_id");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口	
				CusBase cusBase = service.getCusBaseByCusId(cus_id, context, connection);
				cus_type = cusBase.getCusType();
			}
			
			if(null != cus_type && "A2".equals(cus_type)){
				KeyedCollection queryData = null;
				try {
					queryData = (KeyedCollection)context.getDataElement(guarModelId);
				} catch (Exception e) {}
				String conditionStr = TableModelUtil.getQueryCondition( guarModelId, queryData, context, false, false, false) ;
				
				if(conditionStr == null || "".equals(conditionStr)){
					//conditionStr = "where cus_id ='"+cus_id+"' order by TOTL_START_DATE,SERNO,agr_no desc";
					conditionStr = "where cus_id ='"+cus_id+"' order by LMT_START_DATE,SERNO,agr_no desc";
				}else{
					conditionStr += "and cus_id ='"+cus_id+"' order by LMT_START_DATE,SERNO,agr_no desc";
					//conditionStr += "and cus_id ='"+cus_id+"' order by TOTL_START_DATE,SERNO,agr_no desc";
				}

				IndexedCollection iColl = dao.queryList(guarModelId, null, conditionStr, pageInfo, connection);
				iColl.setName(iColl.getName() + "List");
				this.putDataElement2Context(iColl, context);

				SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
				SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });

				String[] args = new String[] { "cus_id" };
				String[] modelIds = new String[] { "CusBase" };
				String[] modelForeign = new String[] { "cus_id" };
				String[] fieldName = new String[] { "cus_name" };
				// 详细信息翻译时调用
				SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);

				TableModelUtil.parsePageInfo(context, pageInfo);
				
				//控制"授信维护"按钮
				context.put("guar", "guar");
				context.put("menuId", "Agr_Fin_Guar");
				
				return "guar";
			}
			//判断是否担保公司客户，担保公司客户需取融资性担保公司授信协议  end

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			//1.判断subConndition中是否有值，有值则表明来源于集团协议下查看成员公司协议
			if(context.containsKey("subConndition") && !"".equals(context.getDataValue("subConndition"))){
				if(null == conditionStr || "".equals(conditionStr)){
					conditionStr = " WHERE 1=1 ";
				}
				conditionStr = conditionStr + " AND "+ context.getDataValue("subConndition");
			}
			//2.否则是单一法人授信查看授信协议，则使用记录集权限进行过滤
			else{
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
				
//				if(null == conditionStr || "".equals(conditionStr)){
//					conditionStr += " WHERE GRP_AGR_NO IS NULL";
//				}else{
//					conditionStr += " AND GRP_AGR_NO IS NULL";
//				}
			}
			String openDay = (String)context.getDataValue("OPENDAY");
			//客户详情中存量授信中传入cus_id过滤
			if(context.containsKey("cus_id") && !"".equals(context.getDataValue("cus_id"))){  //客户码
				conditionStr += " AND CUS_ID='"+ context.getDataValue("cus_id")+"'";
			}
			
			/** 授信协议循环总金额、一次性总金额、授信总额 从台账中实时获取   2013-12-17  唐顺岩  */
			String select_sql ="SELECT SERNO, AGR.AGR_NO, CUS_ID, START_DATE, END_DATE, CUR_TYPE, MANAGER_BR_ID, MANAGER_ID, DET.CRD_CIR_AMT, DET.CRD_ONE_AMT, DET.CRD_TOTL_AMT FROM LMT_AGR_INFO AGR,";
			select_sql += " (SELECT AGR_NO AS AGR_NO1,SUM(CIR_AMT) CRD_CIR_AMT,SUM(ONE_AMT) CRD_ONE_AMT,SUM(CIR_AMT + ONE_AMT) CRD_TOTL_AMT";
			select_sql += " FROM (SELECT AGR_NO, SUM(DECODE(LIMIT_TYPE, '01', NVL(CRD_AMT,0), 0)) CIR_AMT, SUM(DECODE(LIMIT_TYPE,'02', NVL(CRD_AMT,0), 0)) ONE_AMT FROM LMT_AGR_DETAILS";
			if(((String)context.getDataValue("menuId")).indexOf("grp")>=0){  //集团授信时只需统计非低风险额度
				select_sql += " WHERE LRISK_TYPE='20' AND";
			}else{
				select_sql += " WHERE";
			}
			select_sql += " LMT_STATUS<>'30' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),12),'yyyy-mm-dd')>='"+openDay+"' AND START_DATE<='"+openDay+"' GROUP BY AGR_NO ,LIMIT_TYPE) GROUP BY AGR_NO ) DET ";
			
			select_sql += conditionStr;
			select_sql += " AND DET.AGR_NO1 = AGR.AGR_NO ";
			select_sql += " ORDER BY AGR_NO DESC";
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo,this.getDataSource(context), select_sql);
			
//			List<String> list = new ArrayList<String>();
//			list.add("serno");
//			list.add("agr_no");
//			list.add("cus_id");
//			list.add("crd_totl_amt");
//			list.add("crd_cir_amt");
//			list.add("crd_one_amt");
//			list.add("start_date");
//			list.add("end_date");
//			list.add("cur_type");
//			list.add("manager_br_id");
//			list.add("manager_id");
//			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			/** END */
			
			SInfoUtils.addSOrgName(iColl, new String[] {"manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] {"manager_id"});
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			iColl.setName("LmtAgrInfoList");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		}catch (EMPException ee) {
			throw new EMPException("查询授信协议失败，失败原因："+ee.getMessage());
		} catch(Exception e){
			throw new EMPException("查询授信协议失败，失败原因："+e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}

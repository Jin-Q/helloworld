package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;


import java.math.BigDecimal;
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
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrDetailsListOp extends CMISOperation {

	private final String modelId = "LmtAgrDetails";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String queryConditionStr = " AND 1=1 ";
			KeyedCollection queryData = null;
			
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			if(queryData !=null && queryData.containsKey("forze_date")&&queryData.getDataValue("forze_date")!=null&&!"".equals(queryData.getDataValue("forze_date"))){
				queryConditionStr = " AND LIMIT_CODE IN (SELECT LIMIT_CODE FROM LMT_APP_FROZE_UNFROZE WHERE OVER_DATE = '"+queryData.getDataValue("forze_date")+"' AND APP_TYPE = '07') ";
				queryData.removeDataElement("forze_date");
			}
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			//1.判断传入参数subConndition中是否有值，有值则代表集团授信协议、联保授信协议、行业授信协议下查看台账信息；则只能查询当前
			//授信协议下的分项额度台账
			if(context.containsKey("subConndition") && !"".equals(context.getDataValue("subConndition"))){
				if(null == conditionStr || "".equals(conditionStr)){
					conditionStr = " WHERE 1=1 ";
				}
				conditionStr = conditionStr + " AND "+ context.getDataValue("subConndition");
			}			
			
			//2.否则为授信台账列表，则使用记录集权限进行过滤，加载所有有权限查看的授信台账
			else{
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			}
			conditionStr += queryConditionStr;
			conditionStr += " ORDER BY AGR_NO DESC, LIMIT_CODE DESC";
//			
//			if(conditionStr.indexOf("WHERE") != -1){
//				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
//			}else {
//				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
//			}
//			
//			if(context.containsKey("subConndition") && !"".equals(context.getDataValue("subConndition"))){
//				condition = " AND "+ context.getDataValue("subConndition");
//			}
//			
//			if(context.containsKey("serno")){
//				condition = " and agr_no = (select agr_no from LMT_indus_AGR where serno = '"
//					+(String) context.getDataValue("serno")+"')";
//			}else if(context.containsKey("agr_no")){
//				condition = " and agr_no = '"	+(String) context.getDataValue("agr_no")+"' ";
//			}
//			condition+=" AND SUB_TYPE IN('01','05') AND AGR_NO IN(SELECT AGR_NO FROM LMT_AGR_INFO WHERE GRP_AGR_NO IS NULL UNION ALL SELECT AGR_NO FROM LMT_AGR_INDIV)";
//			
//			conditionStr += condition+" order by limit_code";
			
			
			int size = 10;
		   
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		    /**modified by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 begin**/
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			list.add("cus_id");
			list.add("sub_type");
			list.add("limit_name");
			list.add("term");
			list.add("crd_amt");
			list.add("guar_type");
			list.add("limit_code");
			list.add("start_date");
			list.add("end_date");
			list.add("limit_type");  //额度类型
			list.add("cur_type");
			list.add("lmt_status");
			list.add("lrisk_type");  //低风险业务类型
			list.add("core_corp_cus_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			//added by yangzy 2015/05/08 需求：XD141222087，法人透支需求展示额度剩余额度 start
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				String limit_code = "";
				BigDecimal bal_amt = new BigDecimal("0");
				if(kColl.containsKey("limit_code")&&kColl.getDataValue("limit_code")!=null&&!"".equals(kColl.getDataValue("limit_code"))){
					limit_code = (String) kColl.getDataValue("limit_code");
					BigDecimal crd_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("crd_amt"));
					
					KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(limit_code, "01", connection, context);
					BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollTemp.getDataValue("lmt_amt"));
					bal_amt = crd_amt.subtract(lmt_amt).setScale(2,BigDecimal.ROUND_UP);
				}
				kColl.put("bal_amt", bal_amt);
			}
			//added by yangzy 2015/05/08 需求：XD141222087，法人透支需求展示额度剩余额度 end
			
			String[] args=new String[] { "cus_id","core_corp_cus_id" };
			String[] modelIds=new String[]{"CusBase","CusBase"};
			String[] modelForeign=new String[]{"cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			 /**modified by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 end**/
			/**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
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

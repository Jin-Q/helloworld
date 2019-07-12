package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

public class SubLmtQuoAdjustWFOp extends CMISOperation {


	private final String modelId = "LmtQuotaAdjustApp";
	
	private final String serno_name = "fin_agr_no";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String fin_agr_no_value = null;
			IndexedCollection iCollTemp = null;
			IndexedCollection iCollTemp1 = null;
			IndexedCollection iCollTemp2 = null;
			try {
				fin_agr_no_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(fin_agr_no_value == null || fin_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			String openday = context.getDataValue("OPENDAY").toString();
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String conditionStr = "where fin_agr_no= '"+fin_agr_no_value+"' AND approve_status in ('000','111','992','993') AND STATUS = '2' order by end_date desc";
			String conditionStr1 = "where fin_agr_no= '"+fin_agr_no_value+"' AND approve_status = '000' and fin_serno is null order by end_date desc";
			/* modified by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 start */
			String conditionStr2 = "where fin_agr_no= '"+fin_agr_no_value+"' AND exists (select 1 from lmt_app_fin_guar where lmt_app_fin_guar.serno = lmt_quota_adjust_app.fin_serno and lmt_app_fin_guar.approve_status = '111') order by end_date desc";
			/* modified by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 end */
			iCollTemp = dao.queryList(modelId, null, conditionStr, connection);	
			iCollTemp1 = dao.queryList(modelId, null, conditionStr1, connection);
			iCollTemp2 = dao.queryList(modelId, null, conditionStr2, connection);
			
			KeyedCollection kColl4Agr = dao.queryDetail("LmtAgrFinGuar", fin_agr_no_value, connection);
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(kColl4Agr, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			String cus_id = "";
			String cus_id_displayname = "";
			String fin_totl_limit = "";
			if(kColl4Agr!=null&&kColl4Agr.getDataValue("agr_no")!=null&&!"".equals(kColl4Agr.getDataValue("agr_no"))){
				cus_id = (String) kColl4Agr.getDataValue("cus_id");
				cus_id_displayname = (String) kColl4Agr.getDataValue("cus_id_displayname");
				fin_totl_limit = (String) kColl4Agr.getDataValue("fin_totl_limit");
			}
			
			if(iCollTemp!=null&&iCollTemp.size()>0){
				context.put("flag", "存在【未生效】状态的调整明细，请启用待生效！");
				context.put("serno","");
				context.put("cus_id","");
				context.put("cus_id_displayname","");
				context.put("fin_totl_limit","");
			}else if(iCollTemp2!=null&&iCollTemp2.size()>0){
				context.put("flag", "已存在【审批在途】的调整明细，不能重复发起提交！");
				context.put("serno","");
				context.put("cus_id","");
				context.put("cus_id_displayname","");
				context.put("fin_totl_limit","");
			}else if(iCollTemp1!=null&&iCollTemp1.size()>0){
				KeyedCollection kColl = dao.queryDetail("LmtAgrFinGuar", fin_agr_no_value, connection);
				String serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", (String) kColl.getDataValue("manager_br_id"), connection, context);
				kColl.put("serno", serno);
				kColl.put("app_date", openday);
				kColl.put("approve_status", "000");
				kColl.put("fin_cur_type", "CNY");
				kColl.put("app_type", "01");
				kColl.put("input_id", context.getDataValue("currentUserId"));
				kColl.put("input_br_id", context.getDataValue("organNo"));
				
				kColl.setName("LmtAppFinGuar");
				dao.insert(kColl, connection);
				context.put("flag","success");
				context.put("serno",serno);
				context.put("cus_id",cus_id);
				context.put("cus_id_displayname",cus_id_displayname);
				context.put("fin_totl_limit",fin_totl_limit);
				for(Object obj:iCollTemp1){
					KeyedCollection kColl1 = (KeyedCollection)obj;
					kColl1.put("fin_serno", serno);
					dao.update(kColl1, connection);
				}
			}else{
				KeyedCollection kColl = dao.queryFirst(modelId,null, " where fin_agr_no= '"+fin_agr_no_value+"' AND approve_status in ('000','111','992','993') and fin_serno is not null ", connection);
				
				if(kColl!=null&&kColl.getDataValue("fin_serno")!=null&&!"".equals(kColl.getDataValue("fin_serno"))){
					context.put("serno", kColl.getDataValue("fin_serno"));
					context.put("flag","success");
					context.put("cus_id",cus_id);
					context.put("cus_id_displayname",cus_id_displayname);
					context.put("fin_totl_limit",fin_totl_limit);
				}else{
					context.put("serno", "");
					context.put("cus_id","");
					context.put("cus_id_displayname","");
					context.put("fin_totl_limit","");
					context.put("flag","获取申请流水号失败，请联系管理人员！");
				}
				
			}
			
		}catch (EMPException ee) {
			context.put("flag","业务提交失败，请联系管理人员！");
			context.put("serno","");
			throw ee;
		} catch(Exception e){
			context.put("flag","业务提交失败，请联系管理人员！");
			context.put("serno","");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}

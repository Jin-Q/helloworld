package com.yucheng.cmis.biz01line.lmt.riskmanage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class LmtCheckGuarContDataValidity implements RiskManageInterface {
	/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 begin*/
	private final String modelId = "RLmtAppGuarCont";//授信申请和担保合同关系表
	private final String ladModelId = "LmtAppDetails";//授信申请表
	private final String checkSqlId = "checkExistGuaranty";//检查额度授信分项为抵押或质押是否存在相应的担保合同及担保物
	/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 end*/
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		//获取规则服务接口
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
		Map<String,String> modelMap=new HashMap<String,String>();
		Map<String,String> outMap_sign=new HashMap<String,String>();
		
		/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 begin*/
		String returnFlag="";
		String returnInfo="";
//		//modified by yangzy 2015/06/12 XD150407025_2015 分支机构授权配置,单一法人授信担保校验 start
// 		IndexedCollection ladIColl = dao.queryList(ladModelId," where serno = '" + serno+ "' and  guar_type in ('100','200') and nvl(crd_amt,0)>0 ", connection);
//		if (ladIColl != null && ladIColl.size() > 0) {
//			for (int i = 0; i < ladIColl.size(); i++) {
//				KeyedCollection kColl = (KeyedCollection) ladIColl.get(i);
//				String limit_code = (String) kColl.getDataValue("org_limit_code");// 额度编号
//				String guar_type = (String) kColl.getDataValue("guar_type");//
//				String condition = "";
//				if ("100".equals(guar_type)) {
//					condition = " where limit_code = '" + limit_code+ "' and serno = '" + serno + "' and corre_rel not in ('3','5') and exists (select 1 from grt_guar_cont where grt_guar_cont.guar_cont_no = r_lmt_app_guar_cont.guar_cont_no and grt_guar_cont.guar_way = '00' and grt_guar_cont.guar_cont_state in ('00','01')) ";
//				} else {
//					condition = " where limit_code = '" + limit_code+ "' and serno = '" + serno + "' and corre_rel not in ('3','5') and exists (select 1 from grt_guar_cont where grt_guar_cont.guar_cont_no = r_lmt_app_guar_cont.guar_cont_no and grt_guar_cont.guar_way = '01' and grt_guar_cont.guar_cont_state in ('00','01')) ";
//				}
//				IndexedCollection rlagcIColl = dao.queryList(modelId,condition, connection);// 授信申请和担保合同信息
//		//modified by yangzy 2015/06/12 XD150407025_2015 分支机构授权配置,单一法人授信担保校验 end
//				if (rlagcIColl != null && rlagcIColl.size() > 0) {
//					KeyedCollection paramKcoll = new KeyedCollection(); // Sql参数
//					String showStr = "";
//					if ("100".equals(guar_type)) {
//						paramKcoll.put("guaranty_cls", "1");
//						showStr = "抵押物";
//					} else {
//						paramKcoll.put("guaranty_cls", "2");
//						showStr = "质押物";
//					}
//					for (int j = 0; j < rlagcIColl.size(); j++) {
//						KeyedCollection rlagcKColl = (KeyedCollection) rlagcIColl.get(j);
//						String guar_cont_no = (String) rlagcKColl.getDataValue("guar_cont_no");
//						paramKcoll.put("guar_cont_no", guar_cont_no);
//						KeyedCollection checkKColl = (KeyedCollection) SqlClient.queryFirst(checkSqlId, paramKcoll, null,connection);
//						BigDecimal count =new BigDecimal(0);
//						if (checkKColl != null && checkKColl.containsKey("cnt")) {
//							count =BigDecimalUtil.replaceNull(checkKColl.getDataValue("cnt"));
//						}
//						if (count.compareTo(new BigDecimal(1))<0) {
//							returnFlag = "不通过";
//							returnInfo = "该授信申请中授信分项[" + limit_code+ "]的担保合同["+guar_cont_no+"]不存在押品类别为" + showStr+ "的【担保品】,请检查！";
//							break;
//						}
//					}
//				} else {
//					returnFlag = "不通过";
//					returnInfo = "该授信申请中授信分项[" + limit_code+ "]不存在相应的【担保合同信息】,请检查！";
//				}
//				if(!"".equals(returnFlag)) break;
//				
//			}
//		}
//		
//		if(!"".equals(returnFlag)){
//			Map<String,String> outMap0=new HashMap<String,String>();
//			outMap0.put("OUT_是否通过", returnFlag);
//			outMap0.put("OUT_提示信息",returnInfo);
//			return outMap0;
//		}
		/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 end*/
		String message = "";
		String message_amt = "";
		//查询授信申请下所有的分项记录，分别校验授信下担保合同是否足额
		//需关联流水号并且担保合同与授信关系不为解除   2013-12-03  唐顺岩
		IndexedCollection icoll = dao.queryList("RLmtAppGuarCont", " WHERE LIMIT_CODE IN(SELECT ORG_LIMIT_CODE FROM LMT_APP_DETAILS WHERE GUAR_TYPE = '300' and SERNO='"+serno+"') AND SERNO='"+serno+"' AND CORRE_REL<>'3'", connection);
		for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
			KeyedCollection kcoll = (KeyedCollection) iterator.next();
			String guar_cont_no = (String)kcoll.getDataValue("guar_cont_no");
			/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 begin*/
			String limit_code = (String)kcoll.getDataValue("limit_code");
			IndexedCollection iColl = dao.queryList(ladModelId, " where org_limit_code = '" + limit_code+ "' and serno = '" + serno + "'", connection);
			KeyedCollection kColl=(KeyedCollection) iColl.get(0);
			String is_pre_crd= (String) kColl.getDataValue("is_pre_crd");
			if("2".equals(is_pre_crd)){//是否预授信 为否时
				modelMap.put("IN_SERNO", guar_cont_no);
				outMap_sign=shuffleService.fireTargetRule("GUARCONT", "ISAVAILGUARCONTLMT", modelMap);
				if("不通过".equals(outMap_sign.get("OUT_是否通过").toString())){
					message += "["+guar_cont_no+"]";
				}
				outMap_sign=shuffleService.fireTargetRule("GUARCONT", "ISAVAILGUARAMT", modelMap);
				if("不通过".equals(outMap_sign.get("OUT_是否通过").toString())){
					message_amt += "["+guar_cont_no+"]";
				}
			}
			/* add by wangj 2015-6-11 需求编号：XD150407025_2015 分支机构授权配置 end*/
		}
		
		Map<String,String> outMap=new HashMap<String,String>();
		if(!"".equals(message) || !"".equals(message_amt)){
			if(!"".equals(message)){
				message = "担保合同"+message+"对应担保品信息有误；";
			}
			if(!"".equals(message_amt)){
				message_amt = "担保合同"+message_amt+"对应担保品金额不能覆盖担保合同金额，或联保担保合同金额不等于保证人担保金额；";
			}
			
			message = "授信申请下关联" + message + message_amt+"请检查！";
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", message);
		}else{
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "授信下担保合同信息合规");
		}
		
		return outMap;
	}

}

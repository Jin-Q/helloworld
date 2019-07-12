package com.yucheng.cmis.biz01line.lmt.riskmanage;

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

public class LmtGrpCheckGuarCont implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		//获取规则服务接口
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
		Map<String,String> modelMap=new HashMap<String,String>();
		Map<String,String> outMap_sign=new HashMap<String,String>();
		
		String message = "";
		String conditionApp = "WHERE GRP_SERNO='"+serno+"'";
		IndexedCollection iCollApp = dao.queryList("LmtApply", conditionApp, connection);
		for(int i=0;i<iCollApp.size();i++){
			KeyedCollection kCollApp = (KeyedCollection)iCollApp.get(i);
			String app_serno = (String)kCollApp.getDataValue("serno");
			//查询授信申请下所有的分项记录，分别校验授信下担保合同是否足额
			IndexedCollection icoll = dao.queryList("LmtAppDetails", " WHERE SERNO='"+app_serno+"' AND LMT_TYPE=(SELECT LMT_TYPE FROM LMT_APPLY WHERE SERNO='"+serno+"') ", connection);
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				if("2".equals(kcoll.getDataValue("is_pre_crd")) && !"400".equals(kcoll.getDataValue("guar_type"))){  //不为预授信情况下\并且担保方式不为【信用】
					String limit_code = (String)kcoll.getDataValue("org_limit_code");
					modelMap.put("IN_LIMIT_CODE", limit_code);
					modelMap.put("IN_SERNO", serno);  //需关联流水号   2013-12-03  唐顺岩
					outMap_sign=shuffleService.fireTargetRule("LMT_CHECK", "LMTAPPCHECKGUARAMT", modelMap);
					if("不通过".equals(outMap_sign.get("OUT_是否通过").toString())){
						String ori_limit_code = (String)kcoll.getDataValue("org_limit_code");//提示时取
						message += "["+ori_limit_code+"]";
					}
				}
			}
		}
		
		Map<String,String> outMap=new HashMap<String,String>();
		if(!"".equals(message)){
			message = "非预授信分项"+message+"对应担保合同金额不能覆盖授信金额或联保担保合同金额不等于保证人担保金额，请检查！";
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", message);
		}else{
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "授信下担保合同金额校验通过");
		}
		
		return outMap;
	}

}

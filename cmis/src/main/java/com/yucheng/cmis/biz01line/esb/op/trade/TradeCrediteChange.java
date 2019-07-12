package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.pub.dao.SqlClient;

public class TradeCrediteChange extends ESBTranService{
	private static final Logger logger = Logger.getLogger(TradeCrediteChange.class);
	
	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection conn) throws Exception {
		logger.info("**************************************************:进入授信申请变更接口,输入报文："+kColl);
		//返回报文
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
		String tchPvdrCrdtNo = "";//授信合同编号
		String orgnlCrdAmt = "";//原授信金额
		String newCrdAmt = "";//新授信金额
		String orgnlCrdtExprtnDt = "";//原授信日期
		String newCrdtExprtnDt = "";//新授信日期
		if(reqBody.containsKey("TchPvdrCrdtNo")){
			tchPvdrCrdtNo = (String) reqBody.getDataValue("TchPvdrCrdtNo");
			logger.info("**************************************************:网贷授信合同编号："+tchPvdrCrdtNo);
		}else{
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "请求报文中授信合同编号为空！");
			logger.info("****************************************************返回报文信息："+retKColl);
			return retKColl;
		}
		
		if(reqBody.containsKey("NewCrdAmt")){
			newCrdAmt = (String) reqBody.getDataValue("NewCrdAmt");
			logger.info("**************************************************:授信变更金额："+newCrdAmt);
		}
		if(reqBody.containsKey("NewCrdtExprtnDt")){
			newCrdtExprtnDt = (String) reqBody.getDataValue("NewCrdtExprtnDt");
			logger.info("**************************************************:授信变更日期："+newCrdtExprtnDt);
		}
		
		if(StringUtils.isNotEmpty(newCrdAmt)||StringUtils.isNotEmpty(newCrdtExprtnDt)){
			Map<String,Object> lmtParam = new HashMap<String,Object>();
			lmtParam.put("app_no", tchPvdrCrdtNo);
			//查询授信协议
			Map<String,Object> lmtAgrDetailsKcoll = (Map<String, Object>) SqlClient.queryFirst("queryLmtAgrDetailsByAppNo", lmtParam, null, conn);
			
			//查询授信台账
			Map<String,Object> lmtAgrIndivKcoll = (Map<String, Object>) SqlClient.queryFirst("queryLmtAgrIndivByAppNo", lmtParam, null, conn);
			
			if(lmtAgrDetailsKcoll==null&&lmtAgrIndivKcoll==null){
				retKColl.put("RetCd", "999999");
				retKColl.put("RetInf", "通过网贷授信协议编号"+tchPvdrCrdtNo+"查询授信协议、授信台账为空！");
				logger.info("****************************************************返回报文信息："+retKColl);
				return retKColl;
			}
			
			EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
			Context context = factory.getContextNamed(factory.getRootContextName());
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);

			logger.info("*******************************更新授信台账**********************************");
			KeyedCollection kColl4LAD=new KeyedCollection();
			kColl4LAD.put("limit_code", lmtAgrDetailsKcoll.get("limitCode"));
			if(StringUtils.isNotEmpty(newCrdAmt)){
				kColl4LAD.put("crd_amt", newCrdAmt);
			}
			if(StringUtils.isNotEmpty(newCrdtExprtnDt)){
				kColl4LAD.put("end_date", newCrdtExprtnDt);

			}
			kColl4LAD.setName("LmtAgrDetails");
			dao.update(kColl4LAD,conn);

			logger.info("*******************************更新授信协议**********************************");
			KeyedCollection kColl4SXXY=new KeyedCollection();
			kColl4SXXY.put("agr_no", lmtAgrIndivKcoll.get("agrNo"));
			if(StringUtils.isNotEmpty(newCrdAmt)){
				kColl4SXXY.put("crd_totl_amt", newCrdAmt);
			}
			if(StringUtils.isNotEmpty(newCrdtExprtnDt)){
				kColl4SXXY.put("totl_end_date", newCrdtExprtnDt);

			}
			kColl4SXXY.setName("LmtAgrIndiv");
			dao.update(kColl4SXXY, conn);
			retKColl.put("RetCd", "000000");
			logger.info("****************************************************返回报文信息："+retKColl);
			return retKColl;
		}else{
			retKColl.put("RetCd", "999999");
			retKColl.put("RetInf", "变更金额与变更日期不能同时为空");
			logger.info("****************************************************返回报文信息："+retKColl);
			return retKColl;
		}
	}
}

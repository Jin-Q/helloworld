package com.yucheng.cmis.biz01line.lmt.msi.msiimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;

public class LmtServiceInterfaceImple extends CMISModualService implements LmtServiceInterface {
	private static final Logger logger = Logger.getLogger(LmtServiceInterfaceImple.class);
	/**
	 * 查询客户是否存在有效授信
	 * @param cus_id 客户码
	 * @param cus_type 客户类型（1-单一法人  2-同业客户）
	 * @return res_value 返回是否存在有效授信（1-是  2-否）
	 */
	public String searchIsExistLmt(String cusId, String cusType)throws EMPException {
		logger.info("---------------查询客户是否存在有效授信   开始---------------");
		
		if(null==cusType || (!"1".equals(cusType) && !"2".equals(cusType))){
			logger.error("查询客户是否存在有效授信，参数客户类型[cus_type]传入值："+cusType+" 错误，正确取值：1-单一法人  2-同业客户！");
			throw new EMPException("查询客户是否存在有效授信，参数客户类型[cus_type]传入值："+cusType+" 错误，正确取值：1-单一法人  2-同业客户！");
		}
		if(null==cusId || "".equals(cusId)){
			logger.error("查询客户是否存在有效授信，参数客户码[cus_id]不能为空！");
			throw new EMPException("查询客户是否存在有效授信，参数客户码[cus_id]不能为空！");
		}
		
		String is_exist = "N";
		try{
			// 调用规则管理模块对外提供的服务
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
				throw new EMPException("调用规则管理模块对外提供的服务错误，错误原因："+e.getMessage());
			}
			//取当前机器时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String openDate = sdf.format(date);
			
			Map<String,String> modelMap=new HashMap<String,String>();
			modelMap.put("IN_CUS_ID", cusId);
			modelMap.put("IN_CUS_TYPE", cusType);
			modelMap.put("IN_CUR_DATE", openDate);
			Map<String,String> outMap=new HashMap<String,String>();
			try {
				outMap=shuffleService.fireTargetRule("LMT_CHECK", "ISEXISTCRD", modelMap);
			} catch (Exception e1) {
				cusType = "1".equals(cusType)?"单一法人":"同业客户";
				logger.error("调用规则自动判断是否存在有效授信出错，客户类型："+cusType+"，客户码："+cusId+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD\n错误原因："+e1);
				throw new EMPException(CMISMessage.QUERYERROR,"调用规则自动判断是否存在有效授信出错，客户类型："+cusType+"，客户码："+cusId+"，规则集：LMT_CHECK，规则名称：ISEXISTCRD\n错误原因："+e1);
			}
			
			is_exist =(String) outMap.get("OUT_EXIST_CRD");
		}catch(Exception e){
			logger.error("查询客户是否存在有效授信错误，错误原因："+e.getMessage());
			throw new EMPException("查询客户是否存在有效授信错误，错误原因："+e.getMessage());
		}
		logger.info("---------------查询客户是否存在有效授信   结束---------------");
		is_exist = "Y".equals(is_exist)?"1":"2";  //如果存在（Y）返回1否则返回0
		return is_exist;
	}

	/**
	 * 更改客户额度为不可用，主要用于客户条线变更及集团客户变更。
	 * @param opt_type 操作类型（1-条线变更  2-集团变更）
	 * @param cus_id_str 客户码/集团编号
	 * @param adj_cusIds_bef 变更前名单（操作类型为集团变更时传入）
	 * @param adj_cusIds_aft 变更后名单（操作类型为集团变更时传入）
	 * @return res_value 返回成功(Y)或失败(N)
	 */
	public String updateLmtUnUse(String opt_type,String cus_id_str,String adj_cusIds_bef,String adj_cusIds_aft,Connection connection)throws EMPException {
		String res_value ="N";
		logger.info("---------------调用授信模块接口更改额度状态   开始---------------");
		if(null==opt_type || (!"1".equals(opt_type) && !"2".equals(opt_type))){ //操作类型为空时
			logger.error("调用授信模块接口更改额度状态错误，操作类型[opt_type]传入值："+opt_type+" 错误，正确取值：1-条线变更  2-集团变更！");
			throw new EMPException("调用授信模块接口更改额度状态错误，操作类型[opt_type]传入值："+opt_type+" 错误，正确取值：1-条线变更  2-集团变更！");
		}
		if(null == cus_id_str || "".equals(cus_id_str)){
			logger.error("调用授信模块接口更改额度状态错误，客户码/集团编号[cus_id_str]不能为空！");
			throw new EMPException("调用授信模块接口更改额度状态错误，客户码/集团编号[cus_id_str]不能为空！");
		}
		if("2".equals(opt_type) && (null == adj_cusIds_bef || "".equals(adj_cusIds_bef)) && (null == adj_cusIds_aft || "".equals(adj_cusIds_aft))){
			logger.error("调用授信模块接口更改额度状态错误，操作类型[opt_type]为[集团变更]时变更前名单[adj_cusIds_bef]、变更后名单[adj_cusIds_aft]不能同时为空！");
			throw new EMPException("调用授信模块接口更改额度状态错误，操作类型[opt_type]为[集团变更]时变更前名单[adj_cusIds_bef]、变更后名单[adj_cusIds_aft]不能同时为空！");
		}
		
		try {
			if("1".equals(opt_type)){  //单一法人条线变更
				//先解除担保合同与授信关系
				SqlClient.executeUpd("unchainRLmtGuarCont", cus_id_str, null, null, connection);
				//更新额度状态
				SqlClient.executeUpd("updateLmtAgrDetailsStatus", cus_id_str, "30", null, connection);
			}else{   //集团变更
				//将调整前名单对应的额度设置为单一法人授信额度
				if(null!=adj_cusIds_bef && !"".equals(adj_cusIds_bef)){
					SqlClient.executeUpd("updateLmtAgrInfo4Single", adj_cusIds_aft, null, null, connection);
				}
				
				//将调整后的名单对应的额度设置为挂起
				if(null!=adj_cusIds_aft && !"".equals(adj_cusIds_aft)){   //变更后名单设置额度类型为挂起
					SqlClient.executeUpd("updateLmtAgrDetailsStatusNL", adj_cusIds_aft, "40", null, connection);
				}
			}
			res_value = "Y";
		}catch (Exception e) {
			logger.error("更改客户额度状态错误，错误描述："+e.getMessage());
			throw new EMPException("更改客户额度状态错误，错误描述："+e.getMessage());
		}
		logger.info("---------------调用授信模块接口更改额度状态   结束---------------");
		return res_value;
	}

	/**
	 * 根据查询查询类型（1-存量授信 2-否决历史）查询客户存量授信或否决历史
	 * @param cus_id 客户码
	 * @param select_type 查询类型（1-存量授信 2-否决历史）
	 * @param connection 数据库连接
	 * @return iColl IndexedCollection数据集
	 */
	public IndexedCollection searchLmtAgrInfoList(String cusId,String selectType, Connection connection) throws EMPException {
		IndexedCollection iColl = new IndexedCollection();
		logger.info("---------------根据查询查询类型（1-存量授信 2-否决历史）查询客户存量授信或否决历史   开始---------------");
		if(null==selectType || (!"1".equals(selectType) && !"2".equals(selectType))){ //操作类型为空时
			logger.error("调用授信模块接口查询存量授信或否决历史错误，操作类型[select_type]传入值："+selectType+" 错误，正确取值：1-存量授信 2-否决历史！");
			throw new EMPException("调用授信模块接口查询存量授信或否决历史错误，操作类型[select_type]传入值："+selectType+" 错误，正确取值：1-存量授信 2-否决历史！");
		}
		if(null == cusId || "".equals(cusId)){
			logger.error("调用授信模块接口查询存量授信或否决历史错误，客户码[cus_id]不能为空！");
			throw new EMPException("调用授信模块接口查询存量授信或否决历史错误，客户码[cus_id]不能为空！");
		}
		
		String type_str = "1".equals(selectType)?"存量授信":"否决历史";
		
		try{
			if("1".equals(selectType)){  //存量授信
				iColl = SqlClient.queryList4IColl("searchLmtAgrInfoList", cusId, connection);
			}else if("2".equals(selectType)){   //否决历史
				iColl = SqlClient.queryList4IColl("searchLmtOverruleList", cusId, connection);
			}
		}catch(Exception e){
			logger.error("调用授信模块接口查询"+type_str+"错误，错误描述："+e.getMessage());
			throw new EMPException("调用授信模块接口查询"+type_str+"错误，错误描述："+e.getMessage());
		}
		
		logger.info("---------------根据查询查询类型（1-存量授信 2-否决历史）查询客户存量授信或否决历史  结束---------------");
		return iColl;
	}

	/**
	 * 根据客户码、分项类型（01-一般授信05-供应链授信）查询客户授信协议信息
	 * @param cusId 客户码
	 * @param subType 分项类型（01-一般授信05-供应链授信）
	 * @param connection 数据库连接
	 * @return kColl KeyedCollection数据集
	 */
	public KeyedCollection getLmtAgrInfoMsg(String cusId,String subType, Connection connection) throws EMPException {
		KeyedCollection kColl = new KeyedCollection();
		logger.info("---------------根据客户码、分项类型（01-一般授信05-供应链授信）查询客户授信协议信息   开始---------------");
		if(null==subType || "".equals(subType)){ //操作类型为空时
			logger.error("调用授信模块接口查询客户授信协议信息错误，操作类型[subType]传入值："+subType+" 错误，正确取值：01-一般授信05-供应链授信！");
			throw new EMPException("调用授信模块接口查询客户授信协议信息错误，操作类型[subType]传入值："+subType+" 错误，正确取值：01-一般授信05-供应链授信！");
		}
		if(null == cusId || "".equals(cusId)){
			logger.error("调用授信模块接口查询客户授信协议信息错误，客户码[cusId]不能为空！");
			throw new EMPException("调用授信模块接口查询客户授信协议信息错误，客户码[cusId]不能为空！");
		}
		String agr_no1 = "";
		String agr_no2 = "";
		double crd_cir_amt = 0;
		double crd_one_amt = 0;
		KeyedCollection kColl4Query = new KeyedCollection();
		kColl4Query.addDataField("cus_id", cusId);
		kColl4Query.addDataField("sub_type", subType);
		kColl4Query.addDataField("limit_type", "01");//循环额度
		try{
			IndexedCollection iCollCir = SqlClient.queryList4IColl("queryLmtAgrInfoMsg", kColl4Query, connection);
			kColl4Query.setDataValue("limit_type", "02");//一次性额度
			IndexedCollection iCollOne = SqlClient.queryList4IColl("queryLmtAgrInfoMsg", kColl4Query, connection);
			
			if(iCollCir.size()>0){
				KeyedCollection kCollCir = (KeyedCollection)iCollCir.get(0);
				agr_no1 = (String)kCollCir.getDataValue("agr_no");
				crd_cir_amt = ((BigDecimal)kCollCir.getDataValue("crd_amt")).doubleValue();//循环额度
			}
			if(iCollOne.size()>0){
				KeyedCollection kCollOne = (KeyedCollection)iCollOne.get(0);
				agr_no2 = (String)kCollOne.getDataValue("agr_no");
				crd_one_amt = ((BigDecimal)kCollOne.getDataValue("crd_amt")).doubleValue();//非循环额度
			}
			if(agr_no1!=null&&!"".equals(agr_no1)&&agr_no2!=null&&!"".equals(agr_no2)&&agr_no1.equals(agr_no2)){
				kColl.addDataField("agr_no", agr_no1);
				kColl.addDataField("crd_totl_amt", crd_cir_amt+crd_one_amt);
				kColl.addDataField("crd_cir_amt", crd_cir_amt);
				kColl.addDataField("crd_one_amt", crd_one_amt);
			}else if(agr_no1!=null&&!"".equals(agr_no1)&&agr_no2!=null&&!"".equals(agr_no2)&&!agr_no1.equals(agr_no2)){
				logger.error("调用授信模块接口查询授信协议错误，错误描述：客户["+cusId+"]存在多笔协议["+agr_no1+"]["+agr_no2+"]");
				throw new EMPException("调用授信模块接口查询授信协议错误，错误描述：客户["+cusId+"]存在多笔协议["+agr_no1+"]["+agr_no2+"]");
			}else if(agr_no1!=null&&!"".equals(agr_no1)&&(agr_no2==null||"".equals(agr_no2))){
				kColl.addDataField("agr_no", agr_no1);
				kColl.addDataField("crd_totl_amt", crd_cir_amt);
				kColl.addDataField("crd_cir_amt", crd_cir_amt);
				kColl.addDataField("crd_one_amt", 0);
			}else if(agr_no2!=null&&!"".equals(agr_no2)&&(agr_no1==null||"".equals(agr_no1))){
				kColl.addDataField("agr_no", agr_no2);
				kColl.addDataField("crd_totl_amt", crd_one_amt);
				kColl.addDataField("crd_cir_amt", 0);
				kColl.addDataField("crd_one_amt", crd_one_amt);
			}
		}catch(Exception e){
			logger.error("调用授信模块接口查询错误，错误描述："+e.getMessage());
			throw new EMPException("调用授信模块接口查询错误，错误描述："+e.getMessage());
		}
		
		logger.info("---------------根据客户码、分项类型（01-一般授信05-供应链授信）查询客户授信协议信息  结束---------------");
		return kColl;
	}
	

	/**
	 * 根据客户码、分项类型（01-一般授信05-供应链授信）、核心企业客户码（05-供应链授信 时为必传参数）查询授信台账记录
	 * @param cusId 客户码
	 * @param subType 分项类型（01-一般授信05-供应链授信）
	 * @param coreCorpCusId 核心企业客户码（05-供应链授信 时为必传参数）
	 * @param connection 数据库连接
	 * @return iColl IndexedCollection数据集
	 */
	public IndexedCollection searchLmtAgrDetailsList(String cusId,String subType,String coreCorpCusId,Connection connection)throws EMPException{
		IndexedCollection iCollRes = new IndexedCollection();
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据客户码、分项类型（01-一般授信05-供应链授信）、核心企业客户码（05-供应链授信 时为必传参数）查询授信台账记录   开始---------------", null);
		if(null==subType || (!"01".equals(subType) && !"05".equals(subType))){ //操作类型为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据客户码、分项类型、核心企业客户码查询授信台账记录错误，分项类型[subType]传入值："+subType+" 错误，正确取值：01-一般授信05-供应链授信！", null);
			throw new EMPException("根据客户码、分项类型、核心企业客户码查询授信台账记录错误，分项类型[subType]传入值："+subType+" 错误，正确取值：01-一般授信05-供应链授信！");
		}
		if(null == cusId || "".equals(cusId)){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据客户码、分项类型、核心企业客户码查询授信台账记录错误，客户码[cus_id]不能为空！", null);
			throw new EMPException("根据客户码、分项类型、核心企业客户码查询授信台账记录错误，客户码[cus_id]不能为空！");
		}
		
		if("05".equals(subType) && (null == coreCorpCusId || "".equals(coreCorpCusId))){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据客户码、分项类型、核心企业客户码查询授信台账记录错误，核心企业客户码[core_corp_cus_id]不能为空！", null);
			throw new EMPException("根据客户码、分项类型、核心企业客户码查询授信台账记录错误，客户码[core_corp_cus_id]不能为空！");
		}
		
		
		try{
			KeyedCollection kColl4Query = new KeyedCollection();
			
			if("05".equals(subType)){
				kColl4Query.addDataField("cus_id", cusId);
				kColl4Query.addDataField("sub_type", subType);
				kColl4Query.addDataField("core_corp_cus_id", coreCorpCusId);
				iCollRes = SqlClient.queryList4IColl("searchLmtAgrDetailsList2", kColl4Query, connection);
			}else{
				kColl4Query.addDataField("cus_id", cusId);
				kColl4Query.addDataField("sub_type", subType);
				iCollRes = SqlClient.queryList4IColl("searchLmtAgrDetailsList", kColl4Query, connection);
			}	
			
		}catch(Exception e){
			logger.error("查询客户授信台账记录错误，错误描述："+e.getMessage());
			throw new EMPException("查询客户授信台账记录错误，错误描述："+e.getMessage());
		}
		
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据客户码、分项类型（01-一般授信05-供应链授信）、核心企业客户码（05-供应链授信 时为必传参数）查询授信台账记录   结束---------------", null);
		return iCollRes;
	}
	
	
	/**
	 * 授信项下担保合同签订时实时更新授信启用金额
	 * @param limit_code	授信额度编码
	 * @param guar_amt		担保金额
	 * @param type		分项类别（01--其他，02--联保）
	 * @param connection	数据库连接
	 */
	public void updateLmtEnableamt(String limit_code,BigDecimal guar_amt,String type, Connection connection)throws EMPException{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------授信项下担保合同签订时实时更新授信启用金额  开始---------------", null);
		if(null==limit_code || "".equals(limit_code)){ //操作类型为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同签订时实时更新授信启用金额错误，授信额度编码不能为空！", null);
			throw new EMPException("授信项下担保合同签订时实时更新授信启用金额错误，授信额度编码不能为空！");
		}
		if(guar_amt.compareTo(new BigDecimal("0"))<=0){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同签订时实时更新授信启用金额错误，担保金额不能为空！", null);
			throw new EMPException("授信项下担保合同签订时实时更新授信启用金额错误，担保金额不能为空！");
		}
		if(null==type || "".equals(type)){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同签订时实时更新授信启用金额错误，分项类别不能为空！", null);
			throw new EMPException("授信项下担保合同签订时实时更新授信启用金额错误，分项类别不能为空！");
		}
		KeyedCollection kColl4Query = new KeyedCollection();
		kColl4Query.addDataField("guar_amt", guar_amt);
		kColl4Query.addDataField("guar_amt2", guar_amt);
		try {
			if("01".equals(type)){
				SqlClient.executeUpd("updateLmtEnableamt", limit_code, kColl4Query, null, connection);
			}else if("02".equals(type)){
				SqlClient.executeUpd("updateLmtEnableamtAgr", limit_code, kColl4Query, null, connection);
			}
			
		}catch(Exception e){
			logger.error("授信项下担保合同签订时实时更新授信启用金额错误，错误描述："+e.getMessage());
			throw new EMPException("授信项下担保合同签订时实时更新授信启用金额错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------授信项下担保合同签订时实时更新授信启用金额  结束---------------", null);
	}
	
	/**
	 * 授信项下担保合同注销时实时更新授信启用金额
	 * @param limit_code	授信额度编码
	 * @param guar_amt		担保金额
	 * @param type		分项类别（01--其他，02--联保）
	 * @param connection	数据库连接
	 */
	public void updateLmtEnableamtOff(String limit_code,BigDecimal guar_amt,String type, Connection connection)throws EMPException{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------授信项下担保合同注销时实时更新授信启用金额  开始---------------", null);
		if(null==limit_code || "".equals(limit_code)){ //操作类型为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同注销时实时更新授信启用金额错误，授信额度编码不能为空！", null);
			throw new EMPException("授信项下担保合同注销时实时更新授信启用金额错误，授信额度编码不能为空！");
		}
		if(guar_amt.compareTo(new BigDecimal("0"))<=0){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同注销时实时更新授信启用金额错误，担保金额不能为空！", null);
			throw new EMPException("授信项下担保合同注销时实时更新授信启用金额错误，担保金额不能为空！");
		}
		if(null==type || "".equals(type)){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "授信项下担保合同注销时实时更新授信启用金额错误，分项类别不能为空！", null);
			throw new EMPException("授信项下担保合同注销时实时更新授信启用金额错误，分项类别不能为空！");
		}
		KeyedCollection kColl4Query = new KeyedCollection();
		kColl4Query.addDataField("guar_amt", guar_amt);
		kColl4Query.addDataField("guar_amt2", guar_amt);
		try {
			if("01".equals(type)){
				SqlClient.executeUpd("updateLmtEnableamtOff", limit_code, kColl4Query, null, connection);
			}else if("02".equals(type)){
				SqlClient.executeUpd("updateLmtEnableamtOffAgr", limit_code, kColl4Query, null, connection);
			}
			
		}catch(Exception e){
			logger.error("授信项下担保合同注销时实时更新授信启用金额错误，错误描述："+e.getMessage());
			throw new EMPException("授信项下担保合同注销时实时更新授信启用金额错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------授信项下担保合同注销时实时更新授信启用金额  结束---------------", null);
	}

	/**
	 * 根据授信额度编号查询关联担保合同
	 * @param limit_code	授信额度编码
	 * @param connection	数据库连接
	 */
	public IndexedCollection searchGuarContByLimitCode(String limit_code_str, PageInfo pageInfo,DataSource dataSource)throws EMPException{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据授信额度编号查询关联担保合同 开始---------------", null);
		if(null==limit_code_str || "".equals(limit_code_str)){ //授信额度编码为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据授信额度编号查询关联担保合同错误，授信额度编码不能为空！", null);
			throw new EMPException("根据授信额度编号查询关联担保合同错误，授信额度编码不能为空！");
		}
		String guar_cont_no_str = "";
		IndexedCollection iColl = new IndexedCollection();
		if(!limit_code_str.startsWith("'")){
			limit_code_str = "'"+limit_code_str+"'";
		}
		try {
			
			KeyedCollection kc = new KeyedCollection(); 
			kc.addDataField("limitCodeStr",limit_code_str);
			String sql_select = SqlClient.joinQuerySql("searchGuarContByLimitCode", kc, null);
			iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		}catch(Exception e){
			logger.error("根据授信额度编号查询关联担保合同错误，错误描述："+e.getMessage());
			throw new EMPException("根据授信额度编号查询关联担保合同错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据授信额度编号查询关联担保合同 结束---------------", null);
		return iColl;
	}
	
	/**
	 * 根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额
	 * @param agrNo	授信协议号（单一法人为授信额度编码）
	 * @param subType	授信类型（01-一般授信 02-同业授信 03-第三方授信）
	 * @param connection	数据库连接
	 * @return String	返回授信总金额
	 */
	public String searchLmtAgrAmtByAgrNO(String agrNo,String lmtType, Connection connection)throws EMPException{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额  开始---------------", null);
		if(null==lmtType || (!"01".equals(lmtType) && !"02".equals(lmtType) && !"03".equals(lmtType))){ //操作类型为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，分项类型[lmtType]传入值："+lmtType+" 错误，正确取值：01-一般授信 02-同业授信 03-第三方授信！", null);
			throw new EMPException("根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，分项类型[lmtType]传入值："+lmtType+" 错误，正确取值：01-一般授信 02-同业授信 03-第三方授信！");
		}
		if(null == agrNo || "".equals(agrNo)){
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，授信协议号[agrNo]不能为空！", null);
			throw new EMPException("根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，授信协议号[agrNo]不能为空！");
		}
		String res = "";
		try{
			if("01".equals(lmtType)){
				res = SqlClient.queryFirst("searchSingleLmtAmt", agrNo, null, connection)+"";
			}else if("02".equals(lmtType)){
				res = SqlClient.queryFirst("searchIntbankLmtAmt", agrNo, null, connection)+"";
			}else if("03".equals(lmtType)){
				res = SqlClient.queryFirst("searchThirdPartyLmtAmt", agrNo, null, connection)+"";
			}
		}catch(Exception e){
			logger.error("根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，错误描述："+e.getMessage());
			throw new EMPException("根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额错误，错误描述："+e.getMessage());
		}
		
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "------------根据授信协议号（单一法人为授信额度编码）、授信类型（01-一般授信 02-同业授信 03-第三方授信）查询授信总金额   开始---------------", null);
		return res;
	}
	
	/**
	 * 新增担保合同后保存授信担保合同关系表
	 * @param kColl	授信担保合同关系集合  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 是否保存成功
	 * @throws EMPException
	 */
	public int addRLmtAppGuarCont(KeyedCollection kColl,String guarType, Context context,Connection connection) throws Exception {
		String modelId = "";
		if(kColl.containsKey("type")){   //包含TYPE时说明为授信已签订完成的
			modelId = "RLmtGuarCont";
		}else{
			modelId = "RLmtAppGuarCont";
		}
		kColl.setId(modelId);
		int count = 0; 
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);  
		DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
		/**判断是否存在此授信协议关系，存在则修改，不存在在新增*/
		String limit_code = "";
		if(kColl.containsKey("limit_code")){//新增时limit_code不为空
			limit_code = (String)kColl.getDataValue("limit_code");
		}
		String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
		String isCreditChange = "";
		if(kColl.containsKey("isCreditChange")){
			isCreditChange = (String)kColl.getDataValue("isCreditChange");
		}
		
//		String conditionStr = "where limit_code='"+limit_code+"' and guar_cont_no='"+guar_cont_no+"'";
		String conditionStr = "where guar_cont_no='"+guar_cont_no+"'";
		if(limit_code!=null&&!"".equals(limit_code)){
			conditionStr = "where limit_code='"+limit_code+"' and guar_cont_no='"+guar_cont_no+"'";
		}
		IndexedCollection selectIColl = dao.queryList(modelId, conditionStr, connection); 
		if(selectIColl.size()>0){
			//KeyedCollection modelKcoll = (KeyedCollection)selectIColl.get(0);
			
//			KeyedCollection parameter_kcoll = new KeyedCollection();
//			parameter_kcoll.addDataField("LIMIT_CODE", limit_code);
//			parameter_kcoll.addDataField("GUAR_CONT_NO", guar_cont_no);
			
//			KeyedCollection value_kcoll = new KeyedCollection();
//			value_kcoll.addDataField("GUAR_AMT", kColl.getDataValue("guar_amt"));
//			value_kcoll.addDataField("IS_PER_GUR", kColl.getDataValue("is_per_gur"));
//			value_kcoll.addDataField("IS_ADD_GUAR", kColl.getDataValue("is_add_guar"));
			if(kColl.containsKey("type")){   //包含TYPE时说明为授信已签订完成的
				count = SqlClient.executeUpd("updateRLmtGuarCont", guar_cont_no, (String)kColl.getDataValue("guar_amt"), null, connection);
			}else{
				count = SqlClient.executeUpd("updateRLmtAppGuarCont", guar_cont_no, (String)kColl.getDataValue("guar_amt"), null, connection);
			}
			
			//count = dao.update(modelKcoll, connection);
		}else{
			/**如果为信用证修改，新增的一般担保合同关联关系为新增*/
			if("sx".equals(isCreditChange)){
				kColl.addDataField("corre_rel","2"); //关联关系(新增)   
			}else{
				kColl.addDataField("corre_rel","1"); //关联关系默认值(正常)   
			}
			/**担保等级赋值，大小为已有等级+1   --Start--*/
			if(limit_code!=null && !"".equals(limit_code)){ 
				String condition  = "where limit_code='"+limit_code+"'";
				IndexedCollection iColl = (IndexedCollection)dao.queryList(modelId,condition, connection);  
				String guar_cont_no_str =""; 
				IndexedCollection ContiColl =null;
				int m = 1;
				int size = 1000;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
				//把担保合同编号拼装成一个String 
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
					String guarContNo = (String)kCollGrt.getDataValue("guar_cont_no");
					guar_cont_no_str += "'"+guarContNo+"',"; 
				}
				if(guar_cont_no_str.length()>1){
					guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
					/**调用担保模块接口*/
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
					ContiColl = service.getGuarContInfoList(guar_cont_no_str, pageInfo, datasource); 		
				}
				
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kCollGrt = (KeyedCollection)iColl.get(i);
					String guar_cont_no_Rel = (String)kCollGrt.getDataValue("guar_cont_no");
					for(int j=0;j<ContiColl.size();j++){
						KeyedCollection GuarContkColl = (KeyedCollection)ContiColl.get(j);
						String guarContNo = (String)GuarContkColl.getDataValue("guar_cont_no");
						if(guar_cont_no_Rel.equals(guarContNo)){ 
							String guar_cont_type = (String)GuarContkColl.getDataValue("guar_cont_type");
							//判断担保合同类型 
							if(guar_cont_type.equals(guarType)){
								m += 1; //如果是最高额担保，则记录
							} 
						}
					}
				}
				kColl.addDataField("guar_lvl", m);
			}
			/**---------------担保等级默认赋值--end-------------------*/
			
			/**查询授信协议，如果是担保后签，需要将授信协议也写进去关系表   2013-11-22 唐顺岩 */
			KeyedCollection kcoll_details = dao.queryDetail("LmtAgrDetails", limit_code, connection);
			if(null != kcoll_details && kcoll_details.containsKey("agr_no")){
				kColl.addDataField("agr_no", kcoll_details.getDataValue("agr_no"));
			}
			/**END*/
			count = dao.insert(kColl, connection);
		}
		return count;
	}
	
	/**
	 * 修改删除担保合同查询授信担保合同关系表此担保合同条数
	 * @param guarContNo	担保合同编号  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 业务担保合同kColl
	 * @throws EMPException
	 */
	public int checkRLmtAppGuarContNum(String guarContNo, Context context,Connection connection) throws Exception {
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------修改删除担保合同查询授信担保合同关系表此担保合同条数  开始---------------", null);
		IndexedCollection iColl = new IndexedCollection();
		int num = 0;
		try {
			iColl = SqlClient.queryList4IColl("SelectLmtGuarNum", guarContNo, connection);
			KeyedCollection kColl = (KeyedCollection)iColl.get(0);
			num =Integer.parseInt(kColl.getDataValue("num")+"") ;
		}catch(Exception e){
			logger.error("根据修改删除担保合同查询授信担保合同关系表此担保合同条数错误，错误描述："+e.getMessage());
			throw new EMPException("根据修改删除担保合同查询授信担保合同关系表此担保合同条数错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------修改删除担保合同查询授信担保合同关系表此担保合同条数  结束---------------", null);
		return num;
	}
	
	/**
	 * 修改查看担保合同查询授信担保合同关系表
	 * @param limitCode   授信担保合同关系表 联合主键
	 * @param guarContNo 授信担保合同关系表 联合主键  
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 授信担保合同kColl
	 * @throws EMPException
	 */
	public KeyedCollection selectRLmtAppGuarCont(String limitCode,String guarContNo, Context context, Connection connection) throws Exception {
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		Map<String, String> map = new HashMap<String,String>();
		map.put("limit_code", limitCode);
		map.put("guar_cont_no", guarContNo);
		KeyedCollection kColl = dao.queryDetail("RLmtAppGuarCont", map, connection); 
		return kColl;  
	}
	
	/**
	 * 根据联保授信协议查询联保小组成员信息
	 * @param joint_agr_no	联保协议编号
	 * @param connection 数据库连接
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	public IndexedCollection searchLmtJointNameList(String joint_agr_no,Connection connection)throws EMPException{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据联保授信协议查询联保小组成员信息  开始---------------", null);
		if(null==joint_agr_no || "".equals(joint_agr_no)){ //联保协议编号为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "根据联保授信协议查询联保小组成员信息错误，联保协议编号不能为空！", null);
			throw new EMPException("根据联保授信协议查询联保小组成员信息错误，联保协议编号不能为空！");
		}
		IndexedCollection iColl = new IndexedCollection();
		try {
			iColl = SqlClient.queryList4IColl("searchLmtJointNameList", joint_agr_no, connection);
		}catch(Exception e){
			logger.error("根据授信额度编号查询关联担保合同错误，错误描述："+e.getMessage());
			throw new EMPException("根据授信额度编号查询关联担保合同错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------根据联保授信协议查询联保小组成员信息  结束---------------", null);
		return iColl;
	}
	
	/**
	 * 生成联保授信协议与担保合同关系
	 * @param guar_cont_no	担保合同编号  
	 * @param joint_agr_no	联保协议编号
	 * @param amt			担保合同金额
	 * @param is_per_gur	是否阶段性担保
	 * @param is_add_guar	是否追加
	 * @param context 		上下文
	 * @param connection 	数据库连接
	 * @return boolean
	 * @throws EMPException
	 */
	public boolean createRLmtGuarContByJoint(String guar_cont_no, String joint_agr_no, String amt,String is_per_gur,String is_add_guar, Context context,Connection connection)throws Exception{
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------生成联保授信协议与担保合同关系  开始---------------", null);
		if(null==guar_cont_no || "".equals(guar_cont_no)){ //担保合同编号为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "生成联保授信协议与担保合同关系错误，担保合同编号不能为空！", null);
			throw new EMPException("生成联保授信协议与担保合同关系错误，担保合同编号不能为空！");
		}
		if(null==joint_agr_no || "".equals(joint_agr_no)){ //联保协议编号为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "生成联保授信协议与担保合同关系错误，联保协议编号不能为空！", null);
			throw new EMPException("生成联保授信协议与担保合同关系错误，联保协议编号不能为空！");
		}
		if(null==amt || "".equals(amt)){ //担保合同金额为空时
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "生成联保授信协议与担保合同关系错误，担保合同金额不能为空！", null);
			throw new EMPException("生成联保授信协议与担保合同关系错误，担保合同金额不能为空！");
		}
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//获取联保授信下所有台账
			String condition = " WHERE AGR_NO='"+joint_agr_no+"' AND SUB_TYPE='03' ";
			IndexedCollection guarntr_list = dao.queryList("LmtAgrDetails", condition, connection);
			//获取关联表中记录
			String conditionStr = "where AGR_NO='"+joint_agr_no+"'and guar_cont_no='"+guar_cont_no+"'";
			IndexedCollection rLmtGuarContIcoll = dao.queryList("RLmtGuarCont", conditionStr, connection);
			List<KeyedCollection> list = new ArrayList<KeyedCollection>();
			List<KeyedCollection> listPara = new ArrayList<KeyedCollection>();
			int guar_lvl = 1 ;
			for (Iterator iterator = guarntr_list.iterator(); iterator.hasNext();) {
				KeyedCollection obj = (KeyedCollection) iterator.next();
				
				KeyedCollection r_kcoll = new KeyedCollection();
				KeyedCollection p_kcoll = new KeyedCollection();
				r_kcoll.addDataField("GUAR_CONT_NO", guar_cont_no);	//担保合同编号
				r_kcoll.addDataField("AGR_NO", joint_agr_no);		//联保协议编号
				r_kcoll.addDataField("LIMIT_CODE", obj.getDataValue("limit_code"));	//授信台账编号
				r_kcoll.addDataField("GUAR_AMT", amt);    			//担保金额
				r_kcoll.addDataField("CORRE_REL", "1");    //关联关系
				r_kcoll.addDataField("IS_PER_GUR", is_per_gur);   //是否阶段性担保
				r_kcoll.addDataField("IS_ADD_GUAR", is_add_guar);  //是否追加
				r_kcoll.addDataField("GUAR_LVL", guar_lvl++);   //担保等级
				p_kcoll.addDataField("GUAR_CONT_NO", guar_cont_no);	//担保合同编号
				p_kcoll.addDataField("LIMIT_CODE", obj.getDataValue("limit_code"));	//授信台账编号
				if(rLmtGuarContIcoll.size()!=0){//若存在，则修改
					r_kcoll.remove("GUAR_CONT_NO");
					r_kcoll.remove("LIMIT_CODE");
					SqlClient.executeUpd("updateRLmtGuarContByJoint", p_kcoll, r_kcoll, null, connection);
				}
				list.add(r_kcoll);
				listPara.add(p_kcoll);
			}
			if(0==rLmtGuarContIcoll.size()){//若不存在，则新增
				SqlClient.executeBatch("createRLmtGuarContByJoint", list, connection);
			}
			
		}catch(Exception e){
			logger.error("生成联保授信协议与担保合同关系错误，错误描述："+e.getMessage());
			throw new EMPException("生成联保授信协议与担保合同关系错误，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------------生成联保授信协议与担保合同关系  结束---------------", null);
		return false;
	}
	
	/**
	 * 根据授信额度编码串查询授信额度详细信息
	 * @param limitCodeStr	授信额度编码串  
	 * @param pageInfo	数据分页对象
	 * @param dataSource 	数据连接池对象
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	public IndexedCollection queryLmtAgrDetailsByLimitCodeStr(String limitCodeStr,PageInfo pageInfo,DataSource dataSource)throws Exception{
		IndexedCollection res_value = null;
		KeyedCollection kc = new KeyedCollection();
		logger.info("---------------根据授信额度编码串查询授信额度详细信息 开始---------------");
			try {
			kc.addDataField("limitCodeStr",limitCodeStr);
			String sql_select = SqlClient.joinQuerySql("queryLmtAgrDetailsByLimitCodeStr", kc, null);
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
			
			logger.info("---------------根据授信额度编码串查询授信额度详细信息 结束---------------");
		}catch (Exception e) {
			logger.error("根据授信额度编码串查询授信额度详细信息失败，错误描述："+e.getMessage());
			throw new EMPException("根据授信额度编码串查询授信额度详细信息失败，错误描述："+e.getMessage());
		}
		return res_value;
	}
	
	/**
	 * 根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加
	 * @param guar_cont_no	担保合同编号  
	 * @param type	入口，是授信模块进入还是担保管理(不为空时为担保管理进入)
	 * @return KeyedCollection
	 * @throws EMPException
	 */
	public KeyedCollection queryRLmtGuarContInfo(String guar_cont_no,String type,Connection connection)throws Exception{
		KeyedCollection res_value = new KeyedCollection();
		logger.info("---------------根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加 开始---------------");
		try {
			if(!"".equals(type)){  //授信台账
				res_value = (KeyedCollection)SqlClient.queryFirst("searchGuarContByGuarCont", guar_cont_no, null, connection);
			}else{   //授信分项
				res_value = (KeyedCollection)SqlClient.queryFirst("searchAppGuarContByGuarCont", guar_cont_no, null, connection);
		}
		}catch (Exception e) {
			logger.error("根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号查询担保合同与授信关系表是否阶段担保及是否追加 结束---------------");
		return res_value;
	}
	
	/**
	 * 根据担保合同编号更新授信和担保合同关系表状态(结果表)
	 * @param guar_cont_no	担保合同编号  
	 * @param connection	connection  
	 * @return Int
	 * @throws EMPException
	 */
	public int updateLmtGuarStatus(String guar_cont_no,Connection connection)throws Exception{
		logger.info("---------------根据担保合同编号更新授信和担保合同关系表状态(结果表) 开始---------------");
		int count = 0;
		try {
			count =SqlClient.executeUpd("updateLmtGuarStatus",guar_cont_no,null,null,connection);
		}catch (Exception e) {
			logger.error("根据担保合同编号更新授信和担保合同关系表状态(结果表)失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号更新授信和担保合同关系表状态(结果表)失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号更新授信和担保合同关系表状态(结果表) 结束---------------");
		return count;
	}
	/**
	 * 根据授信品种编号获取授信下的担保编号列表
	 * @param limitCodeStr 授信品种编号列，表字符串类型为：'1212','21121'
	 * @param pageInfo 翻页信息
	 * @param dataSource 数据库连接池
	 * @return 担保合同列表,表字符串类型为：'1212','21121'
	 * @throws Exception
	 */
	public String queryGuarNoListByLimiCodeList(String limitCodeStr, PageInfo pageInfo,DataSource dataSource) throws Exception {
		String retStr = "";
		IndexedCollection res_value = null;
		try {
			String sql_select = SqlClient.joinQuerySql("queryGuarNoListByLimiCodeList", limitCodeStr, null);
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
			if(res_value != null && res_value.size() > 0){
				for(int i=0;i<res_value.size();i++){
					KeyedCollection kc = (KeyedCollection)res_value.get(i);
					String guar_cont_no = (String)kc.getDataValue("guar_cont_no");
					retStr += ("'"+guar_cont_no+"',");
				}
				retStr = retStr.substring(0, retStr.length()-1);
			}
			if(retStr == null || retStr.trim().length() == 0){
				retStr = "''";
			}
		} catch (Exception e) {
			throw new EMPException("");
		}
		return retStr;
	}
	
	
	/**
	 * 提供业务授信审批意见的serno(单一法人)
	 * @param cus_line
	 * @param limit_acc_no
	 * @return serno
	 * @throws Exception
	 */
	public String getSernoForIqpAcc(String cus_line,String limitAccNo,Connection connection) throws Exception {
		logger.info("---------------提供业务授信审批意见的serno开始(单一法人)---------------");
		String serno = null;
		try {
			//循环额度或者一次性额度
				if("BL100".equals(cus_line)||"BL200".equals(cus_line)){//对公客户
					IndexedCollection selectIColl =SqlClient.queryList4IColl("getSernoForIqpCom", limitAccNo,connection);
					if(selectIColl.size()>0){
						KeyedCollection selectKColl = (KeyedCollection)selectIColl.get(0);
						serno = (String)selectKColl.getDataValue("serno");
						//如果serno为空则查联保
						if(serno == null || "".equals(serno)){
							IndexedCollection iColl =SqlClient.queryList4IColl("getSernoForJoint", limitAccNo,connection);
							if(iColl.size()>0){
								KeyedCollection kColl = (KeyedCollection)selectIColl.get(0);
								serno = (String)kColl.getDataValue("serno");
							}
						}
					}
				}else if("BL300".equals(cus_line)){//个人客户
					IndexedCollection selectIColl =SqlClient.queryList4IColl("getSernoForIqpPer", limitAccNo,connection);
					if(selectIColl.size()>0){
						KeyedCollection select = (KeyedCollection)selectIColl.get(0);
						serno = (String)select.getDataValue("serno");
						//如果serno为空则查联保
						if(serno == null || "".equals(serno)){
							IndexedCollection iColl =SqlClient.queryList4IColl("getSernoForJoint", limitAccNo,connection);
							if(iColl.size()>0){
								KeyedCollection kColl = (KeyedCollection)selectIColl.get(0);
								serno = (String)kColl.getDataValue("serno");
							}
						}
					}
				}
		}catch (Exception e){
			logger.error("查询业务授信审批意见的serno失败(单一法人)，错误描述："+e.getMessage());
			throw new EMPException("查询业务授信审批意见的serno失败(单一法人)，错误描述："+e.getMessage());
		}
		logger.info("---------------提供业务授信审批意见的serno结束(单一法人)---------------");
		return serno;
	}

	/**
	 * 提供业务授信审批意见的serno(合作方授信)
	 * @param limit_credit_no
	 * @return serno
	 * @throws Exception
	 */
	public String getSernoForIqpCredit( String limitCreditNo, Connection connection)throws Exception {
		logger.info("---------------提供业务授信审批意见的serno开始(合作方授信)---------------");
		String serno = null;
		try {
			//合作方
			IndexedCollection selectIColl =SqlClient.queryList4IColl("getSernoForIqp", limitCreditNo,connection);
			if(selectIColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)selectIColl.get(0);
				serno = (String)kColl.getDataValue("serno");
			}
		}catch (Exception e){
			logger.error("查询业务授信审批意见的serno失败(合作方授信)，错误描述："+e.getMessage());
			throw new EMPException("查询业务授信审批意见的serno失败(合作方授信)，错误描述："+e.getMessage());
		}
		logger.info("---------------提供业务授信审批意见的serno结束(合作方授信)---------------");
		return serno;
	}
	
	/**
	 * 根据圈商协议编号获取圈商信息(圈商)
	 * @param agr_no
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getLmtAgrBizArea( String agr_no, Connection connection)throws Exception {
		logger.info("---------------根据圈商协议编号获取圈商信息开始(圈商)---------------");
		KeyedCollection kColl = new KeyedCollection("LmtAgrBizArea");
		try {
			IndexedCollection selectIColl =SqlClient.queryList4IColl("getLmtAgrBizArea", agr_no,connection);
			if(selectIColl.size()>0){
				kColl = (KeyedCollection)selectIColl.get(0);
			}
		}catch (Exception e){
			logger.error("根据圈商协议编号获取圈商信息失败(圈商)，错误描述："+e.getMessage());
			throw new EMPException("根据圈商协议编号获取圈商信息失败(圈商)，错误描述："+e.getMessage());
		}
		logger.info("---------------根据圈商协议编号获取圈商信息结束(圈商)---------------");
		return kColl;
	}
	
	/**
	 * 根据圈商协议编号获取圈商下有效成员(圈商)
	 * @param agr_no
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public IndexedCollection getLmtAgrBizAreaMember( String agr_no, Connection connection)throws Exception {
		logger.info("---------------根据圈商协议编号获取圈商成员信息开始(圈商)---------------");
		IndexedCollection iColl = null;
		try {
			if(agr_no!=null&&!"".equals(agr_no)){
				iColl =SqlClient.queryList4IColl("getLmtAgrBizAreaMember", agr_no,connection);
			}
		}catch (Exception e){
			logger.error("根据圈商协议编号获取圈商成员信息失败(圈商)，错误描述："+e.getMessage());
			throw new EMPException("根据圈商协议编号获取圈商成员信息失败(圈商)，错误描述："+e.getMessage());
		}
		logger.info("---------------根据圈商协议编号获取圈商成员信息结束(圈商)---------------");
		return iColl;
	}
	
	/**
	 * 根据合作方客户码获取合作方信息
	 * @param cus_id
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getAgrCoopInfo( String cus_id, Connection connection)throws Exception {
		logger.info("---------------根据合作方客户码获取合作方信息开始---------------");
		KeyedCollection kColl = new KeyedCollection("LmtAgrBizArea");
		try {
			IndexedCollection selectIColl =SqlClient.queryList4IColl("getAgrCoopInfo", cus_id,connection);
			if(selectIColl.size()>0){
				kColl = (KeyedCollection)selectIColl.get(0);
			}
		}catch (Exception e){
			logger.error("根据合作方客户码获取合作方信息失败，错误描述："+e.getMessage());
			throw new EMPException("根据合作方客户码获取合作方信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据合作方客户码获取合作方信息结束---------------");
		return kColl;
	}
	/**
	 * 根据担保合同编号获取其与授信关联信息
	 * @param guar_cont_no	担保合同编号
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	public IndexedCollection getLmtGuarReByGuarContNo(String guar_cont_no,Connection connection) throws Exception {
		logger.info("---------------根据担保合同编号获取其与授信关联信息开始---------------");
		IndexedCollection iColl = null;
		try {
			iColl =SqlClient.queryList4IColl("getLmtGuarReByGuarContNo",guar_cont_no,connection);
		}catch (Exception e){
			logger.error("根据担保合同编号获取其与授信关联信息失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号获取其与授信关联信息失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号获取其与授信关联信息结束---------------");
		return iColl;
	}
	/**
	 * 根据担保合同编号获取其与授信关联信息（授信与担保合同关系表[申请表]）
	 * @param guar_cont_no	担保合同编号
	 * @return IndexedCollection
	 * @throws EMPException
	 */
	public IndexedCollection getLmtAppGuarReByGuarContNo(String guar_cont_no,Connection connection) throws Exception {
		logger.info("---------------根据担保合同编号获取其与授信关联信息（申请表）开始---------------");
		IndexedCollection iColl = null;
		try {
			iColl =SqlClient.queryList4IColl("getLmtAppGuarReByGuarContNo",guar_cont_no,connection);
		}catch (Exception e){
			logger.error("根据担保合同编号获取其与授信关联信息（申请表）失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号获取其与授信关联信息（申请表）失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号获取其与授信关联信息（申请表）结束---------------");
		return iColl;
	}
	/**
	 * 根据担保合同编号删除担保合同与授信关联关系表记录（结果表）
	 * @param guar_cont_no 担保合同编号
	 * @return int 所删除的记录条数
	 * @throws Exception
	 */
	public int deleteRLmtGuarCont(String guar_cont_no, Connection connection)throws Exception {
		logger.info("---------------根据担保合同编号删除担保合同与授信关联关系表记录（结果表）开始---------------");
		int count = 0;
		try {
			count =SqlClient.executeUpd("deleteRLmtGuarCont",guar_cont_no,null,null,connection);
		}catch (Exception e){
			logger.error("根据担保合同编号删除担保合同与授信关联关系表记录（结果表）失败，错误描述："+e.getMessage());
			throw new EMPException("根据担保合同编号删除担保合同与授信关联关系表记录（结果表）失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据担保合同编号删除担保合同与授信关联关系表记录（结果表）结束---------------");
		return count;
	}
	
	/**
	 * 根据授信额度编号查询授信与担保关系表
	 * @param limit_code	授信额度编码
	 * @param Context	
	 * @param connection	
	 */
	public IndexedCollection getRLmtGuarContByLimitCode(String limit_code,Context context, Connection connection)throws Exception {
		logger.info("---------------根据授信额度编号查询授信与担保关系表开始---------------");
		IndexedCollection res_value = new IndexedCollection();
		try {
			String sql_select = "select * from R_Lmt_Guar_Cont where limit_code='"+limit_code+"' and is_add_guar='1'";
			DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			res_value = TableModelUtil.buildPageData(null, datasource, sql_select);
		}catch (Exception e){
			logger.error("根据授信额度编号查询授信与担保关系表失败，错误描述："+e.getMessage());
			throw new EMPException("根据授信额度编号查询授信与担保关系表失败，错误描述："+e.getMessage());
		}
		logger.info("---------------根据授信额度编号查询授信与担保关系表结束---------------");
		return res_value;
	}
	
}

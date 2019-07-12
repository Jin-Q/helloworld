package com.yucheng.cmis.biz01line.ccr.component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.agent.CcrAgent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppAll;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrMGroupScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrModelScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrRatDirect;
import com.yucheng.cmis.biz01line.cus.interfaces.CustomIface;
import com.yucheng.cmis.biz01line.fncinterface.Fnc4FnaInterface;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.domain.IndGroupDomain;
import com.yucheng.cmis.biz01line.ind.domain.IndLibDomain;
import com.yucheng.cmis.biz01line.ind.interfaces.IndCalScoreIface;
import com.yucheng.cmis.biz01line.ind.interfaces.IndModGrpIndIface;
import com.yucheng.cmis.biz01line.ind.interfaces.IndResultValIface;
import com.yucheng.cmis.biz01line.ind.msi.IndServiceInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.CusPubConstant;
import com.yucheng.cmis.pub.FNAPubConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CcrComponent extends CMISComponent {

	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	/**
	 * <h1>新增信用评级申请信息</h1>
	 * <p>
	 * 通过ind模块的取数方法将所需要通过类取数的指标的
     * 指标值抽取入中间表(IND_RESULT_VAL)
     * <br>
	 * 产生业务编号并初始化以下表
	 * <ul>
	 * 	<li>信用评级申请主表(CCR_APP_INFO)</li>
	 * 	<li>信用评级申请明细(CCR_APP_DETAIL)</li>
	 * 	<li>模型得分表(CCR_MODEL_SCORE)</li>
	 * 	<li>模型组得分(CCR_M_GROUP_SCORE)</li>
	 * 	<li>组别指标得分表(CCR_G_IND_SCORE)</li>
	 * </ul>
	 * </p>
	 * @param addInf Domain类型输入参数，包含下列信息
	 * <ul>
	 * 	<li>模型编码(modelNo)</li>
	 * 	<li>模型名称(modelName)</li>
	 * 	<li>业务编号(serno)(返回用)</li>
	 * 	<li>评分人编号(scoringManager)</li>
	 * 	<li>开始日期(startDate)</li>
	 * 	<li>到期日期(expiringDate)</li>
	 * 	<li>申请日期(appBeginDate)</li>
	 * 	<li>客户码(cusId)</li>
	 * 	<li>客户名称(cusName)</li>
	 * 	<li>评分人(actorno)</li>
	 * </ul> 
	 * @return 信息编码flag默认CMISMessage.DEFEAT失败,为CMISMessage.SUCCESS的时候表示成功 
	 * @throws EMPException 
	 */
	public String addAppRat(CcrAppInfo addInf,CcrAppDetail ccrAppDetail ) throws EMPException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
		/**
		 * 创建业务代理类
		 */
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
       /**
        * 新增评级申请主表信息
        */
        
        flagInfo = ccrAgent.addCcrAppInfo(addInf);
        
        if (flagInfo.equals(CMISMessage.DEFEAT)){
        	return CMISMessage.DEFEAT;
        }
       
        /**
         * 新增评级申请附表信息
         */
         
         flagInfo = ccrAgent.addCcrAppDetail(ccrAppDetail);
         
         if (flagInfo.equals(CMISMessage.DEFEAT)){
         	return CMISMessage.DEFEAT;
         }
                 
        return CMISMessage.SUCCESS;
	};
	
	/**
	 * <h1>新增信用评级申请信息</h1>
	 * <p>
	 * 通过ind模块的取数方法将所需要通过类取数的指标的
     * 指标值抽取入中间表(IND_RESULT_VAL)
     * <br>
	 * 产生业务编号并初始化以下表
	 * <ul>
	 * 	<li>信用评级申请主表(CCR_APP_INFO)</li>
	 * 	<li>信用评级申请明细(CCR_APP_DETAIL)</li>
	 * 	<li>模型得分表(CCR_MODEL_SCORE)</li>
	 * 	<li>模型组得分(CCR_M_GROUP_SCORE)</li>
	 * 	<li>组别指标得分表(CCR_G_IND_SCORE)</li>
	 * </ul>
	 * </p>
	 * @param addInf Domain类型输入参数，包含下列信息
	 * <ul>
	 * 	<li>模型编码(modelNo)</li>
	 * 	<li>模型名称(modelName)</li>
	 * 	<li>业务编号(serno)(返回用)</li>
	 * 	<li>评分人编号(scoringManager)</li>
	 * 	<li>开始日期(startDate)</li>
	 * 	<li>到期日期(expiringDate)</li>
	 * 	<li>申请日期(appBeginDate)</li>
	 * 	<li>客户码(cusId)</li>
	 * 	<li>客户名称(cusName)</li>
	 * 	<li>评分人(actorno)</li>
	 * </ul> 
	 * @return 信息编码flag默认CMISMessage.DEFEAT失败,为CMISMessage.SUCCESS的时候表示成功 
	 * @throws EMPException 
	 */
	public String addApp(CcrAppInfo addInf,CcrAppDetail ccrAppDetail ) throws EMPException {
		String flagInfo = CMISMessage.DEFEAT;//信息编码
        /**
         * 从addInf中获得模型编号
         */
		String modelNo = ccrAppDetail.getModelNo();//模型编码
		//String modelName = addInf.getModelName();//模型名称
		String serno= addInf.getSerno();//业务编号
		String scoringManager = addInf.getInputId();//评分人编号
		String cusId=addInf.getCusId();//客户码
		String appBeginDate = addInf.getAppBeginDate();//申请日期
		String fncYear="";//会计年份
		String fncMonth="";//会计月份
		String statPrdStyle="";//报表周期类型
		String isFnc="";//判断是否存在财报
		String message="";
		ArrayList groupList=null;//指标分组列表
		String dates[]=appBeginDate.split("-");
		
		 // 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
				.getInstance();
		ShuffleServiceInterface shuffleService = null;
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices",
							"shuffle");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0,
					"getModualServiceById error!", e);
			throw new EMPException(e);
		}
		
		/**
		 * 调用模型适用规则CCR_CusCCRMJudge取得模型ID以及会计年份、会计月份
		 */
		Map modelMap=new HashMap();
		modelMap.put("cus_id", cusId);
		modelMap.put("model_no", modelNo);
		Map outMap=new HashMap();
		try {
			outMap=shuffleService.fireTargetRule("CCR", "CusCCRMJudge", modelMap);
		} catch (Exception e1) {
			logger.error("获取模型信息失败\n"+e1);
			throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
		}
		modelNo=(String) outMap.get("OUT_模型编号");
		fncYear=(String) outMap.get("OUT_会计年份");
		fncMonth=(String) outMap.get("OUT_会计月份");
		statPrdStyle=(String) outMap.get("OUT_周期类型");
		if(modelNo!=null){
			ccrAppDetail.setModelNo(modelNo);
			ccrAppDetail.setFncYear(fncYear);
			ccrAppDetail.setFncMonth(fncMonth);
			ccrAppDetail.setStatPrdStyle(statPrdStyle);
		}else{
			logger.error("获取模型信息失败\n");
			throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
		}
		
		
		/**
		 * 创建业务代理类
		 */
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
       /**
        * 新增评级申请主表信息
        */
        
        flagInfo = ccrAgent.addCcrAppInfo(addInf);
        
        if (flagInfo.equals(CMISMessage.DEFEAT)){
        	return CMISMessage.DEFEAT;
        }
       
        /**
         * 新增评级申请附表信息
         */
         
         flagInfo = ccrAgent.addCcrAppDetail(ccrAppDetail);
         
         if (flagInfo.equals(CMISMessage.DEFEAT)){
         	return CMISMessage.DEFEAT;
         }
        /**
         * 初始化模型得分信息
         */
        logger.info("=================INIT Model Score===================");
        flagInfo = ccrAgent.initModelScore(serno,cusId, modelNo, scoringManager);
        
        if (flagInfo.equals(CMISMessage.DEFEAT)){
        	return CMISMessage.DEFEAT;
        }
        /**
         * 调用指标组件的接口获得指标和组信息.
         */
        IndModGrpIndIface indModGrpIndIface = (IndModGrpIndIface)this.getComponentInterface(IndPubConstant.IND_MOD_GRP_INDEX_IFACE_ID);
        
        try {
			groupList=indModGrpIndIface.queryIndGroupDomain(modelNo);
		} catch (EMPException e) {
			logger.error("获取指标组失败\n"+e);
			throw new ComponentException(CMISMessage.QUERYERROR,"获取指标组失败"+e);
		}
        
        /**
         * 初始化组得分信息。
         */
        flagInfo = ccrAgent.initGroupScore(groupList, modelNo, serno,cusId, scoringManager);
        Iterator indListIter =null;
        if (flagInfo.equals(CMISMessage.DEFEAT)){
        	return CMISMessage.DEFEAT;
        }
        /**
         * 为每个指标组生成其下指标的得分信息。
         */
        if (groupList==null){
        	logger.error("获取模型组信息失败\n");
			throw new ComponentException(CMISMessage.QUERYERROR,"获取模型组信息失败");
        }
  
     // 调用指标管理模块对外提供的服务：根据指标组获得指标初始得分信息
		IndServiceInterface indService=null;
		try {
			indService = (IndServiceInterface) serviceJndi .getModualServiceById("indServices", "ind");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0, "getModualServiceById error!", e);
			throw new EMPException(e);
		}
        //初始化该客户的所有选项数据.
        for(int i =0;i<groupList.size();i++){
        	IndGroupDomain groupinfo=(IndGroupDomain)groupList.get(i);
        	String groupNo = groupinfo.getGroupNo();
        	String groupName = groupinfo.getGroupName();
        	HashMap map=new HashMap();
			map.put("group_no", groupNo);
			map.put("group_name", groupName);
			map.put("trans_id", groupinfo.getTransId());
			map.put("pk_value", serno);
			map.put("PK_VALUE", serno);
			map.put("IN_业务编号", serno);
			map.put("cusId", cusId);
			map.put("fnc_year", fncYear);
			map.put("fnc_month", fncMonth);
			map.put("stat_prd_style", statPrdStyle);
			ArrayList<HashMap> indList = null;
			try {
				indList = indService.queryGroupIndexesWithShuffle(map);
			} catch (Exception e) {
				logger.error("获取指标信息失败\n"+e);
				throw new ComponentException(e);
			}
        	
            if (indList!=null){
            	indListIter = indList.iterator();
            }else{
            	logger.error("获取指标信息失败\n");
    			throw new ComponentException(CMISMessage.QUERYERROR,"获取指标信息失败");
            }
          
            flagInfo = ccrAgent.initIndScoreWithShuffle(indList, serno,cusId, groupNo, groupName, modelNo, scoringManager);
        	if (flagInfo.equals(CMISMessage.DEFEAT)){
            	return CMISMessage.DEFEAT;
            }
        }

        return CMISMessage.SUCCESS;
	};
	
	
	/**
	 * 增加信用评级批量申请主表(CCR_BATCH_APP_INFO)
	 * @param ccrAppDetail
	 * @return success或者fail
	 * @throws ComponentException
	 */
	public String addCcrAppDetail(CcrAppDetail ccrAppDetail)throws ComponentException{
		String flagInfo=CMISMessage.DEFEAT;
		//创建业务代理类
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
       //新增评级申请主表信息
        
        flagInfo = ccrAgent.addCcrAppDetail(ccrAppDetail);		
		
        return flagInfo; 
	}
	/**
	 * <H1>新增批量任务</H1>
	 * <p>增加批量评级任务,操作的表有
	 * <ul>
	 * 	<li>信用评级批量申请主表(CCR_BATCH_APP_INFO)</li>
	 * 	<li>信用评级申请明细(CCR_APP_DETAIL)</li>
	 * </ul>
	 * 其中明细表中的模型编号统一使用CcrPubConstant.BATCH_MODEL_NO(M20090300035)
	 * </p>
	 * 
	 * @param ccrBatchAppInfo
	 * @return success或者fail
	 * @throws ComponentException
	 */
/*	public String addBatchNDetail(CcrBatchAppInfo ccrBatchAppInfo )throws ComponentException{
		int flag =0;//如果在后边这个flag被置为1,则说明生成了明细信息,否则没有生成任何信息,需要抛出异常,停止插入信息.
		try{
		this.addBatchApp(ccrBatchAppInfo);
		String serno = ccrBatchAppInfo.getSerno();
		String areaNo = ccrBatchAppInfo.getAreaNo();
		String custMgr = ccrBatchAppInfo.getCustMgr();
		ArrayList cusList = null;
		StringBuffer skipCusList = new StringBuffer();//由于客户已经在评级表中存在，所以需要过滤得列表。
		StringBuffer noFinCusList = new StringBuffer();//由于客户状态不等于10，没录完所以需要过滤得列表。
		StringBuffer noFamCusList = new StringBuffer();//由于通过客户接口得到的补录信息为空,没录入农户评级补充信息所以需要过滤得列表。
		//通过客户模块interface 传入areaNo和custMgr获取制定地区的客户列表。
		/**
		 * @todo
		 * 通过客户模块interface 传入areaNo和custMgr获取制定地区的客户列表。
		 * 等客户模块完成之后补全
		
		CustomIface customIface = (CustomIface)this.getComponentInterface(CusPubConstant.CUS_IFACE);
		
		
//		cusList = (ArrayList) customIface.findAgriList(areaNo, custMgr, null);
		
		//检查是否为空
		if (cusList==null||cusList.size()==0){
			String errInfo="获取地区编码为["+areaNo+"]，客户经理为["+custMgr+"]的客户信息列表失败\n可能该客户经理在所选地区内没有客户\n请选择正确的地区";
			logger.error(errInfo);
			throw new ComponentException(errInfo);
		}
	//	Iterator cusListIter= cusList.iterator();
	//	while (cusListIter.hasNext()){
		//	CusIndiv cusIndiv = (CusIndiv)cusListIter.next();
	//		String cusId = cusIndiv.getInnerCusId();
	//		
	//		String cusName = cusIndiv.getCusName();
			/**
			 * 检验此客户是否已经有已经发起的评级任务,如果有,则不重复生成任务
			 * 并产生提醒.
			 */
	//		if(this.checkAppExist(cusId)){
	//			skipCusList.append(""+cusName+"\t");
		//		continue;
		//	}
		//	if(!CusPubConstant.CUSSTATUSFORMAL.equals(cusIndiv.getCusStatus())){
				//如果客户状态不等于CusPubConstant.CUSSTATUSFORMAL，代表客户信息未录全。
		//		noFinCusList.append(""+cusName+"\t");
		//		continue;
		//	}
    		/**
    		 * 验证农户是否录入补录信息
    		 */
			/*
			CusFarCcrBatIface cfcbi=(CusFarCcrBatIface)this.getComponentInterface(CusPubConstant.CUS_FAR_CCR_BAT_IFACE);
			CusFarCcrBat cfcb=cfcbi.findCusFarCcrBatByCusId(cusId);
			if ( ! this.checkFarCcrBat(cfcb) ){
				//农户补录信息为空
				noFamCusList.append(""+cusName+"\t");
				continue;
			}

	//		String lastGrade = cusIndiv.getCrdGrade();
	//		String lastRatingDate = cusIndiv.getCrdDate();
	//		if(lastRatingDate==null||"".equals(lastRatingDate)){
	//			lastRatingDate = CcrPubConstant.DEFAULT_DATE;
	//		}
	//		String inputBrId = cusIndiv.getInputBrId();
	//		String finaBrId = cusIndiv.getMainBrId();//主管机构和财务机构为同一个
		//	String modelNo = CcrPubConstant.BATCH_MODEL_NO;
		//	String modelName = "一般农户评级";
			
		//	CcrAppDetail ccrAppDetail = new CcrAppDetail();
			
		//	ccrAppDetail.setSerno(serno);
		//	ccrAppDetail.setCusId(cusId);
		//  ccrAppDetail.setCusName(cusName);
		//	ccrAppDetail.setModelName(modelName);
		//	ccrAppDetail.setLastGrade(lastGrade);
		//	ccrAppDetail.setLastRatingDate(lastRatingDate);
		//	ccrAppDetail.setInputBrId(inputBrId);
		//	ccrAppDetail.setFinaBrId(finaBrId);
	//		ccrAppDetail.setModelNo(modelNo);
		//	ccrAppDetail.setCusType(cusIndiv.getCusType());
		//  setEndStatus(CcrPubConstant.STD_ZB_END_STA_NOT_END);
	//		this.addCcrAppDetail(ccrAppDetail);
	//		flag=1;
	//	}
	//	String result =skipCusList.toString(); 
	//	if(noFinCusList.length()>0){
	//		result = result+"<br/>未完成\"基本信息\"录入的客户列表："+noFinCusList.toString();
	//	}
	//	if(noFamCusList.length()>0){
//			result = result+"<br/>未完成\"一般农户评级信息\"录入的客户列表："+noFamCusList.toString();
	//	}
		
	//	if(flag==0){
			//抛出异常,因为没有插入任何明细数据.
	//		String errMsg="新增批量评级失败：由于没有能够进行批量评级的用户,所以终止生成此批量评级申请\n<br/>已经开始评级的客户："+result;
	//		logger.error(errMsg);
	//		throw new ComponentException(errMsg);
	//	}
		
		
	//	if("".equals(result.trim())){
	//		return CMISMessage.DEFEAT;
	//	}else{
	//		return result;
	//	}
	//	}catch (ComponentException e){
	//		logger.error(e.toString());
	//		throw e;
	//	}
	}
*/

	/**
	 * <H1>删除评级申请</H1>
	 * 
	 * <p>删除指定编号的评级申请信息，所操作的表有
	 * <ul>
	 * 	<li>信用评级申请主表(CCR_APP_INFO)</li>
	 * 	<li>信用评级申请明细(CCR_APP_DETAIL)</li>
	 * 	<li>模型得分表(CCR_MODEL_SCORE)</li>
	 * 	<li>模型组得分(CCR_M_GROUP_SCORE)</li>
	 * 	<li>组别指标得分表(CCR_G_IND_SCORE)</li>
	 * </ul>
	 * </p>
	 * @param 业务编号 serno 
	 * @return
	 * @throws ComponentException 
	 */
	
	public String deleteApp(String serno) throws ComponentException{
		//创建业务代理类
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        /**
         * 删除本模块中的相关表信息
         */
		ccrAgent.deleteCcrAppInfo(serno);
		ccrAgent.deleteCcrAppDetail(serno);
		ccrAgent.deleteCcrAppScore(serno);
		return null;
	}
	/**
	 * <h1>删除批量任务</h1>
	 * <p>通过业务编号删除掉批量任务信息,包括
	 * <ul>
	 * 	<li>信用评级批量申请主表(ccr_batch_app_info)</li>
	 * 	<li>信用评级申请明细(CCR_APP_DETAIL)</li>
	 * </ul> 
	 * </p>
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public String deleteBatchApp(String serno)throws ComponentException{
		//创建业务代理类
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        ccrAgent.delAllAppBatch(serno);
       return CMISMessage.SUCCESS;
	}
	
	public String deleteBatchAppDetail(String serno,String cusId) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		ccrAgent.deleteCcrAppDetail(serno,cusId);
		return CMISMessage.SUCCESS;
	}
	public String deleteBatchAppScore(String serno,String cusId) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		ccrAgent.deleteCcrAppScore(serno,cusId);
		return CMISMessage.SUCCESS;
	}
	/**
	 * <h1>流程审批通过调用方法(单笔)</h1>
	 * <p>修改申请的流程状态，并将申请信息，得分过度到历史表</p>
	 * @param serno
	 * @param cusId
	 * @return
	 * @throws ComponentException 
	 */
/*	public String approvalApp(String serno,String cusId,String finalGrade,String adjustScore) throws ComponentException{

        CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        String comCrdDt=this.getOpenDay();
		/**
		 * 查询出该客户的评级明细信息
		 
        CcrAppDetail ccrAppDetail = ccrAgent.queryCcrAppDetail(serno, cusId);
        CcrAppInfo ccrAppInfo = ccrAgent.queryCcrAppInfo(serno);

   //     ccrAppDetail.setFinalGrade(finalGrade);
   //     ccrAppDetail.setEndStatus(CcrPubConstant.STD_ZB_END_STA_END);
   //     ccrAppDetail.setLastRatingDate(comCrdDt);//由于审批结束后上次评级日期已经修改为当前日期.

		
        //ccrAppDetail.setFinalScore(finalScore);
        /**
         * 将新得分和最终评级结果保存回 ccr_app_detail表
       
        ccrAgent.updateCcrAppDetail(ccrAppDetail);
        //修改申请信息中的最终审批结果
		/**
		 * 修改申请表申请状态，为"997"通过(通过未签约)
		
		String comCrdLevel = "997";
		String crdEndDt = ccrAppInfo.getExpiringDate();
		ccrAgent.changeAppStatue(serno, comCrdLevel,comCrdDt);
		/**
		 * @todo 调用客户接口，修改客户评级信息
		 * 这里使用了context。如果以后不再用EMP需要注意修改。
		 
		CustomIface  cusIface=(CustomIface)this.getComponentInterface(CusPubConstant.CUS_IFACE);
		try {
			cusIface.modifyComCrdGrade(comCrdDt, crdEndDt, finalGrade,cusId,adjustScore);
		} catch (EMPException e) {
			logger.error(e.getMessage(), e);
			throw new ComponentException("调用客户模块接口出错:"+e.getMessage());
		}

        /**
         * 调用信息模块接口,生成提醒信息
       
//        Remin4OutInterface remin4OutInterface = (Remin4OutInterface)this.getComponentInterface(PUBConstant.REMINDINTERFACE);
//		remin4OutInterface.remindUsers("0500", serno, this.getOpenDay());
		
		/**
		 * 将单笔申请表，申请明细表，模型得分表，组得分表，指标得分表过渡到历史表中
		
		ccrAgent.app2His(serno);
		/**
		 * 删除原申请表,申请明细表,模型得分表,组得分表,指标得分表中的内容
		 
		ccrAgent.delAllApp(serno);
		return comCrdLevel;
	}

*/
	/**
	 * <h1>流程审批通过调用方法</h1>
	 * <p>修改申请的流程状态，并将申请信息，得分过度到历史表</p>
	 * @param serno
	 * @param cusId
	 * @return
	 * @throws ComponentException 
	 */
	public String approvalBatchApp(String serno,ArrayList appDetailList) throws ComponentException{
        CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);	
        String cusId = "";
        String comCrdDt=this.getOpenDay();
//        CcrBatchAppInfo ccrBatchAppInfo = ccrAgent.queryCcrBatchAppInfo(serno);
//        String crdEndDt = ccrBatchAppInfo.getExpiringDate();
		/**
		 * 修改申请表申请状态，为"997"通过(通过未签约)
		 */
		String comCrdLevel = "997";
		ccrAgent.changeBatchAppStatue(serno, comCrdLevel,comCrdDt);
		/**
		 * @todo 调用客户接口，修改客户评级信息
		 * 这里使用了context。如果以后不再用EMP需要注意修改。
		 */
		Context context = this.getContext(); 
		
		Iterator appdetail = appDetailList.iterator();
		while(appdetail.hasNext()){
			CcrAppDetail ccrAppDetail = (CcrAppDetail) appdetail.next();
	//		cusId=ccrAppDetail.getCusId();
	//		ccrAppDetail.setEndStatus(CcrPubConstant.STD_ZB_END_STA_END);
	//		ccrAppDetail.setLastRatingDate(comCrdDt);//由于审批结束后上次评级日期已经修改为当前日期.
			CustomIface  cusIface=(CustomIface)this.getComponentInterface(CusPubConstant.CUS_IFACE);
			try{
	//			cusIface.modifyComCrdGrade(comCrdDt, crdEndDt, ccrAppDetail.getFinalGrade(), cusId);
			}catch(Exception e)
			{
				logger.error("通过客户码:["+cusId+"]修改最终得分失败"+e);
				throw new ComponentException(e);
			}
			//修改之后顺便保存结果
			ccrAgent.updateCcrAppDetail(ccrAppDetail);
	        /**
	         * 调用信息模块接口,生成提醒信息
	         */			
//			Remin4OutInterface remin4OutInterface = (Remin4OutInterface)this.getComponentInterface(PUBConstant.REMINDINTERFACE);
//			remin4OutInterface.remindUsers("0500", serno+"#"+cusId, this.getOpenDay());
			
		}
		
		/**
		 * 将单笔申请表，申请明细表，模型得分表，组得分表，指标得分表过渡到历史表中
		 */
		
		ccrAgent.app2HisBatch(serno);
		/**
		 * 删除原申请表,申请明细表,模型得分表,组得分表,指标得分表中的内容
		 */
		ccrAgent.delAllAppBatch(serno);		
		return null;
	}
	/**
	 * <H1>根据业务编号和客户经理选择查询评级得分(单笔)</H1>
	 * 查询ccr_app_info中的内容
	 * @param serno
	 * @return
	 * @throws Exception 
	 */
	public CcrAppAll loadScore(String serno,String cusId) throws Exception{
		CcrAppDetail ccrAppDetail = this.loadCcrAppDetail(serno);
		
		String modelNo=ccrAppDetail.getModelNo();
		
		CcrModelScore ccrModelScore = this.loadModelScore(serno, modelNo);
		ArrayList ccrMGroupScoreList = this.loadCcrMGroupScore(serno,modelNo,cusId);
		ArrayList ccrGIndScoreList = this.loadCcrGIndScore(serno,cusId,modelNo);
		
		CcrAppAll ccrAppAll = new CcrAppAll();
		//ccrAppAll.setCcrAppInfo(ccrAppInfo);
		ccrAppAll.setCcrAppDetail(ccrAppDetail);
		ccrAppAll.setCcrModelScore(ccrModelScore);
		ccrAppAll.setCcrMGroupScoreList(ccrMGroupScoreList);
		ccrAppAll.setCcrGIndScoreList(ccrGIndScoreList);
		
		return ccrAppAll;
	}
	/**
	 * <H1>根据业务编号和客户经理选择查询评级历史得分(单笔)</H1>
	 * 查询ccr_app_info中的内容
	 * @param serno
	 * @param actorno
	 * @return
	 * @throws ComponentException 
	 */
	public CcrAppAll loadScoreHis(String serno,String cusId,String modelNo) throws ComponentException{
		//CcrAppInfo ccrAppInfo = this.loadCcrAppInfoHis(serno);
		//String modelNo=ccrAppDetail.getModelNo();
		CcrModelScore ccrModelScore = this.loadModelScoreHis(serno, modelNo);
		ArrayList ccrMGroupScoreList = this.loadCcrMGroupScoreHis(serno,modelNo);
		ArrayList ccrGIndScoreList = this.loadCcrGIndScoreHis(serno,cusId,modelNo);
		
		CcrAppAll ccrAppAll = new CcrAppAll();
		//ccrAppAll.setCcrAppInfo(ccrAppInfo);
		ccrAppAll.setCcrModelScore(ccrModelScore);
		ccrAppAll.setCcrMGroupScoreList(ccrMGroupScoreList);
		ccrAppAll.setCcrGIndScoreList(ccrGIndScoreList);
		
		return ccrAppAll;
	}
	/**
	 * <H1>根据业务编号和客户经理选择查询评级得分(批量)</H1>
	 * 不查询CcrAppInfo表中内容
	 * @param serno
	 * @param cusId
	 * @return
	 * @throws Exception 
	 */
	public CcrAppAll loadScore(String serno,String cusId,String modelNo) throws Exception{
		CcrModelScore ccrModelScore = this.loadModelScore(serno, modelNo);
		ArrayList ccrMGroupScoreList = this.loadCcrMGroupScore(serno,modelNo,cusId);
		ArrayList ccrGIndScoreList = this.loadCcrGIndScore(serno,cusId,modelNo);
		
		CcrAppAll ccrAppAll = new CcrAppAll();
		ccrAppAll.setCcrModelScore(ccrModelScore);
		ccrAppAll.setCcrMGroupScoreList(ccrMGroupScoreList);
		ccrAppAll.setCcrGIndScoreList(ccrGIndScoreList);
		
		return ccrAppAll;
	}
	/**
	 * <h1>获取组得分</h1>
	 * <p>通过模型编号,获取该模型的所有组的得分.以列表返回</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList  loadCcrMGroupScore(String serno,String modelNo,String cusId) throws ComponentException{
		//创建业务代理类
        CcrAgent ccrAgent = (CcrAgent)
							this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		return ccrAgent.queryMGroupDomainList(modelNo, serno,cusId);
	}
	/**
	 * <h1>获取组得分</h1>
	 * <p>通过模型编号,获取该模型的所有组的得分.以列表返回</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList  loadCcrMGroupScoreHis(String serno,String modelNo) throws ComponentException{
		//创建业务代理类
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		return ccrAgent.queryMGroupDomainListHis(modelNo, serno);
	}
	/**
	 * <h1>读取指标得分</h1>
	 * <p>通过传入的模型编号,返回该模型下的所有指标得分</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList  loadCcrGIndScore(String serno,String cusId,String modelNo) throws ComponentException{
		
		//创建业务代理类
        CcrAgent ccrAgent = (CcrAgent) this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        ArrayList  queryGIndDomainList = ccrAgent.queryGIndDomainList(serno,cusId,modelNo);
        //取得指标domain之后将readonly属性填入domain中的groupName字段中
        // this.setGIndReadonly(queryGIndDomainList);已弃用
        if(queryGIndDomainList.size()==0){
        	String message = "未找到业务编号为["+serno+"]的指标得分,请删除该笔申请,并重新创建申请";
        	logger.error(message);
        	throw new ComponentException(this.toString()+"\n"+message);
        }
		return queryGIndDomainList;
	}
	/**
	 * <h1>读取指标历史得分</h1>
	 * <p>通过传入的模型编号,返回该模型下的所有指标得分</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList  loadCcrGIndScoreHis(String serno,String cusId,String modelNo) throws ComponentException{
		
		//创建业务代理类
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		ArrayList  queryGIndDomainList = ccrAgent.queryGIndDomainListHis(serno,cusId,modelNo);
		//取得指标domain之后将readonly属性填入domain中的groupName字段中
		// this.setGIndReadonly(queryGIndDomainList);已弃用
		
		return queryGIndDomainList;
	}
	/**
	 * 取改业务编号下所有定量指标的指标值
	 * @param serno			评级流水号
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> loadIndResultVal(String serno) throws ComponentException{
		IndResultValIface indIface = (IndResultValIface)this.getComponentInterface(IndPubConstant.IND_INSERT_RESULT_VAL);
		ArrayList<HashMap> arr = indIface.queryIndResultValHm(serno);
		return arr;
	}
	
		/**
	 * 为得分添加是否只读参数，保存入domain中的groupName中
	 * 已弃用
	 * @param queryGIndDomainList
	 * groupName
	 * @return
	 * @throws ComponentException 
	 */
	public ArrayList setGIndReadonly(ArrayList queryGIndDomainList) throws ComponentException{
		String indexNo=null;
		String readonly=null;
        Iterator ccrGIndDomainListIterator=queryGIndDomainList.iterator();

        IndModGrpIndIface indModGrpIndIFace=(IndModGrpIndIface)this.getComponentInterface(IndPubConstant.IND_MOD_GRP_INDEX_IFACE_ID);
        
        
        while(ccrGIndDomainListIterator.hasNext()){
        	CcrGIndScore ccrGIndDomain= (CcrGIndScore)ccrGIndDomainListIterator.next();
        	indexNo = ccrGIndDomain.getIndexNo();
        	try {
        		IndLibDomain indDomain= (IndLibDomain) indModGrpIndIFace.queryIndLibDetail(indexNo);
        		/**
        		 * inputType 有3种，1为
        		 * 1.	类
				 * 2.	手工录入
				 * 3.	映射
        		 */
        		logger.error("获取到指标["+indDomain.getIndexName()+"]的取数类型\n"+indDomain.getInputType());
        		if("2".equals(indDomain.getInputType())){
        			readonly="false";
        		}else{
        			readonly="true";
        		}
        	} catch (EMPException e) {
        		logger.error("获取指标信息失败\n"+e);
        		throw new ComponentException(CMISMessage.QUERYERROR,"获取指标信息失败"+e);
        	}
        	//将readonly信息存入domain中的groupName里
        	ccrGIndDomain.setGroupName(readonly);
        }		
		return queryGIndDomainList;
	}
	
	/**
	 * <h1>读取模型得分</h1>
	 * <p>通过模型编号,业务编号读取指定评级任务的指定模型的得分</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws Exception 
	 */
	
	public CcrModelScore loadModelScore(String serno,String modelNo) throws Exception{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		* 查询数据库中是否存在业务编号为serno的数据
		* 返回true false;
		*/
		try {
			return ccrAgent.queryCcrModelScore(serno, modelNo);
		} catch (CMISException e) {
			logger.error("获取模型得分失败\n"+e);
        	throw new ComponentException(e);
		}		
	}
	/**
	 * <h1>读取模型历史得分</h1>
	 * <p>通过模型编号,业务编号读取指定评级任务的指定模型的得分</p>
	 * @param serno
	 * @param modelNo
	 * @return
	 * @throws ComponentException
	 */
	
	public CcrModelScore loadModelScoreHis(String serno,String modelNo) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		 * 查询数据库中是否存在业务编号为serno的数据
		 * 返回true false;
		 */
		try {
			return ccrAgent.queryCcrModelScoreHis(serno, modelNo);
		} catch (CMISException e) {
			logger.error("获取模型得分失败\n"+e);
			throw new ComponentException(e);
		}		
	}
	/**
	 * <h1>读取信用评级主表信息(单笔)</h1>
	 * <p>通过业务编号读取对应的信用评级主表信息,用于单笔任务</p>
	 * @param serno 业务编号
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppInfo loadCcrAppInfo(String serno) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		* 查询数据库中是否存在业务编号为serno的数据
		* 返回true false;
		*/
		return ccrAgent.queryCcrAppInfo(serno);
	}
	/**
	 * <h1>读取评级明细信息(单笔)</h1>
	 * <p>根据业务编号查找对应的评级明细,只用于单笔查询.</p>
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public ArrayList getCcrAppDetailList(String serno) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		* 查询数据库中是否存在业务编号为serno的数据
		* 返回true false;
		*/
		return ccrAgent.getAppDetailList(serno);
	}
	
	/**
	 * <h1>读取评级明细信息(批量)</h1>
	 * <p>根据业务编号查找对应的评级明细,只用于单笔查询.</p>
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppDetail loadCcrAppDetail(String serno) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		 * 查询数据库中是否存在业务编号为serno的数据
		 * 返回true false;
		 */
		return ccrAgent.queryCcrAppDetail(serno);
	}
	/**
	 * <h1>读取评级明细历史信息(批量)</h1>
	 * <p>根据业务编号查找对应的评级明细,只用于单笔查询.</p>
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppDetail loadCcrAppDetailHis(String serno,String cusId) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		/*
		 * 查询数据库中是否存在业务编号为serno的数据
		 * 返回true false;
		 */
		return ccrAgent.queryCcrAppDetailHis(serno,cusId);
	}
	

	/**
	 *<h1>检验评级申请是否存在</h1>
	 * <p>检验指定客户是否在评级明细表中存在,如果存在则返回true,否则返回false</p>
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public boolean checkAppExist(String cusId) throws ComponentException {
		//创建业务代理类
     //   CcrAgent ccrAgent = (CcrAgent) this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        /*
		 * 查询数据库中是否存在业务编号为cusId的数据
		 * 返回true false;
		 */
  //     CcrAppDetail ccrAppDetail = ccrAgent.queryCcrAppDetailByCusId(cusId);
  //    if (cusId.equals(ccrAppDetail.getCusId())){
    	   return true;
  //     }else{
  //  	   return false;
   //    }
	}
	/**
	 * <h1>评分并且保存指标得分</h1>
	 * <p>通过传入参数,调用ratingApp()方法评分,将评分结果保存入各个表中
	 * <ul>
	 * <li>模型得分表(CCR_MODEL_SCORE)</li>
	 * <li>模型组得分表(CCR_M_GROUP_SCORE)</li>
	 * <li>指标得分表(CCR_G_IND_SCORE)</li>
	 * </ul>
	 * </p>
	 * @param ccrAppAll CcrAppAll类型domain对象
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppAll saveAndRating(CcrAppAll ccrAppAll) throws ComponentException{
		//获取ccrAgent对象
		try {
			CcrAgent ccrAgent = (CcrAgent)this.getAgentInstance(CcrPubConstant.CCR_AGENT);
			//通过模型号读取组信息,
		//	this.ratingApp(ccrAppAll);
			this.ratingAppWithShuffle(ccrAppAll);
			//保存指标选项和得分
			ArrayList ccrGIndScoreList=ccrAppAll.getCcrGIndScoreList();
			ArrayList ccrMGroupScoreList=ccrAppAll.getCcrMGroupScoreList();
			CcrAppDetail ccrAppDetail = ccrAppAll.getCcrAppDetail();
			CcrModelScore ccrModelScore = ccrAppAll.getCcrModelScore();
			CcrAppInfo ccrAppInfo = ccrAppAll.getCcrAppInfo();
			ccrAgent.updateCcrAppInfo(ccrAppInfo);
			ccrAgent.updateCcrGIndScore(ccrGIndScoreList);
			ccrAgent.updateCcrAppDetail(ccrAppDetail);
			ccrAgent.updateCcrModelScore(ccrModelScore);
			ccrAgent.updateCcrMGroupScore(ccrMGroupScoreList);
			return ccrAppAll;
		
		} catch (AgentException e) {
			logger.error("保存指标选项失败\n"+e.toString());
			throw new ComponentException(e);
		}
	}
	/**
	 * <h1>信用评级评分方法</h1>
	 * <p>通过传入参数,逐步运算出指标得分,指标组得分,模型得分,并将得分赋值回参数的domain对象中</p>
	 * @param ccrAppAll
	 * @return
	 * @throws ComponentException
	 */
		
	public String ratingApp(CcrAppAll ccrAppAll) throws ComponentException{
		try{
			IndCalScoreIface indCalScoreIface = (IndCalScoreIface)this.getComponentInterface(IndPubConstant.IND_CAL_SCORE_IFACE_ID);
			
			CcrAppDetail ccrAppDetail=ccrAppAll.getCcrAppDetail();
			CcrModelScore ccrModelScore = ccrAppAll.getCcrModelScore();
			String modelNo=ccrAppDetail.getModelNo();
			ArrayList ccrGIndScoreList = ccrAppAll.getCcrGIndScoreList();
			
			Iterator ccrGIndScoreListIter = ccrGIndScoreList.iterator();
			//评指标得分
			HashMap GroupScoreMap = new HashMap();
			HashMap IndexScoreGroup = null;
			String groupnoOld="";
			while(ccrGIndScoreListIter.hasNext()){
				CcrGIndScore ccrGIndScore = (CcrGIndScore)ccrGIndScoreListIter.next();
				String grpno = ccrGIndScore.getGroupNo();
				String indexno = ccrGIndScore.getIndexNo();
				String indexval = ccrGIndScore.getIndexValue();
				String indOrgVal =ccrGIndScore.getIndOrgVal();
				HashMap<String,String> hm=new HashMap<String,String>();
				hm.put("grpno", grpno);
				hm.put("indexno", indexno);
				hm.put("indexval", indexval);
				hm.put("orgvalue", indOrgVal);
				hm.put("cusId", ccrAppAll.getCcrAppInfo().getCusId());
				hm.put("modelNo", ccrAppAll.getCcrAppDetail().getModelNo());
				String indexScore = indCalScoreIface.getIndScore(hm);
				ccrGIndScore.setIndexScore(indexScore);
				if(!grpno.equals(groupnoOld)){
					if(IndexScoreGroup!=null){
						GroupScoreMap.put(groupnoOld, IndexScoreGroup);
					}
					IndexScoreGroup = new HashMap();
					groupnoOld=grpno;
				}
				IndexScoreGroup.put(indexno, indexScore);
			}
			if(IndexScoreGroup!=null){
				GroupScoreMap.put(groupnoOld, IndexScoreGroup);
			}		
			ArrayList ccrMGroupScoreList = ccrAppAll.getCcrMGroupScoreList();
			Iterator ccrMGroupScoreListIter = ccrMGroupScoreList.iterator();
			
			HashMap modelGroupScore= new HashMap();
			
			//评组得分
			while(ccrMGroupScoreListIter.hasNext()){
				CcrMGroupScore ccrMGroupScore= (CcrMGroupScore)ccrMGroupScoreListIter.next();
				String grpno = ccrMGroupScore.getGroupNo();
				String groupScore = indCalScoreIface.getGrpScore(grpno, (HashMap)GroupScoreMap.get(grpno));
				ccrMGroupScore.setGroupScore(groupScore);
				modelGroupScore.put(grpno, groupScore);
			}
			
			//评模型得分
			//得分 将由"得分|等级" 组成 
			modelGroupScore.put("cusid", ccrAppAll.getCcrAppInfo().getCusId());//将客户号放入 
			//modelGroupScore.put("com_grp_mode", ccrAppAll.getCcrAppDetail().getComGrpMode());//将集团客户类型放入 
			//modelGroupScore.put("grp_crd_grade", ccrAppAll.getCcrAppDetail().getGrpCrdGrade());//将将母公司评级放入 
			//modelGroupScore.put("new_com_flag", ccrAppAll.getCcrAppDetail().getNewComFlag());  //放入 是否新增企业 标识
			modelGroupScore.put("serno", ccrAppAll.getCcrAppInfo().getSerno());  //放入 是否新增企业 标识
			String allScore = indCalScoreIface.getModelScore(modelNo, modelGroupScore,ccrGIndScoreList);
			if(allScore==null||"".equals(allScore)){
				String errMsg="获取模型得分失败,请检查模型配置";
				logger.error(errMsg);
				throw new ComponentException(errMsg);
			}
			String [] scoreInfo = allScore.split("#");
			String autoGrade =scoreInfo[1];
			allScore = scoreInfo[0];
			ccrModelScore.setAllScore(allScore);
			
			ccrAppDetail.setAutoScore(new BigDecimal(allScore));
			
			/**
			 * 设置限制等级
			 */
//			CcrLimitGradeComponent clgComponent = (CcrLimitGradeComponent)this.getComponent(CcrPubConstant.CcrLimitGrade);
//			HashMap<String,String> hm = clgComponent
//					.getModelLimitValue(modelNo, autoGrade, ccrAppAll.getCcrAppDetail().getSerno(),
//							ccrGIndScoreList, ccrAppAll.getCcrAppDetail().getNewComFlag());
//			ccrAppDetail.setLimit_grade(hm.get("limit_grade"));
//			ccrAppDetail.setLimit_reason(hm.get("reason"));
//			
			
			//将自动评级等级保存入评级信息domain中
			//后边将把ccrAppInfo保存入数据库.
			ccrAppDetail.setAutoGrade(autoGrade);
		}catch(ComponentException e){
			logger.error(e.getMessage(),e);
			throw e;
		}
		return null;
	}
	
	/**
	 * 
	 * <h1>信用评级规则评分方法</h1>
	 * <p>通过传入参数,逐步进行规则运算出指标得分,指标组得分,模型得分,并将得分赋值回参数的domain对象中</p>
	 * @param ccrAppAll
	 * @return
	 * @throws ComponentException
	 */
	public String ratingAppWithShuffle(CcrAppAll ccrAppAll) throws ComponentException{
		try{
			// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory .getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi .getModualServiceById("shuffleServices", "shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0, "getModualServiceById error!", e);
				throw new EMPException(e);
			}
			
			// 调用指标管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			IndServiceInterface indService = null;
			try {
				indService = (IndServiceInterface) serviceJndi .getModualServiceById("indServices", "ind");
			} catch (Exception e) {
				EMPLog.log("ind", EMPLog.ERROR, 0, "getModualServiceById error!", e);
				throw new EMPException(e);
			}
			CcrAppDetail ccrAppDetail=ccrAppAll.getCcrAppDetail();
			CcrModelScore ccrModelScore = ccrAppAll.getCcrModelScore();
			//获得表模型编号
			String modelNo=ccrAppDetail.getModelNo();
			ArrayList ccrGIndScoreList = ccrAppAll.getCcrGIndScoreList();
			Iterator ccrGIndScoreListIter = ccrGIndScoreList.iterator();
			//评指标得分
			HashMap GroupScoreMap = new HashMap();
			HashMap IndexScoreGroup = null;
			String groupnoOld="";
			BigDecimal dGrpScore=new BigDecimal(0); 
			while(ccrGIndScoreListIter.hasNext()){
				CcrGIndScore ccrGIndScore = (CcrGIndScore)ccrGIndScoreListIter.next();
				String grpno = ccrGIndScore.getGroupNo();
				String indexno = ccrGIndScore.getIndexNo();
				if(indexno.endsWith("_orgVal"))
					continue;
				String indexval = ccrGIndScore.getIndexValue();
				String indOrgVal =ccrGIndScore.getIndOrgVal();
				HashMap<String,String> hm=new HashMap<String,String>();
				hm.put("grpno", grpno);
				hm.put("indexno", indexno);
				hm.put("IN_输入值", indexval);
				hm.put("orgvalue", indOrgVal);
				hm.put("IN_客户码", ccrAppAll.getCcrAppInfo().getCusId());
				hm.put("modelNo", modelNo);
				hm.put("IN_业务编号", ccrAppAll.getCcrAppInfo().getSerno());
				hm.put("IN_会计年份",ccrAppAll.getCcrAppDetail().getFncYear());
				hm.put("IN_会计月份",ccrAppAll.getCcrAppDetail().getFncMonth());
				hm.put("IN_报表周期类型",ccrAppAll.getCcrAppDetail().getStatPrdStyle());
				System.out.println(indexno);
				String indexno1=indexno.split("\\$")[0];
				String indexno2=indexno.split("\\$")[1];
				System.out.println(indexno1);
				System.out.println(indexno2);
   			    Map outmap=shuffleService.fireTargetRule(indexno1,indexno2, hm);
				String indexScore = (String) outmap.get("OUT_得分");
				String indexValue = (String) outmap.get("OUT_指标值");
				ccrGIndScore.setIndexScore(indexScore);
				ccrGIndScore.setIndexValue(indexValue);
				if(!grpno.equals(groupnoOld)){
					if(IndexScoreGroup!=null){
						GroupScoreMap.put(groupnoOld, IndexScoreGroup);
						GroupScoreMap.put(groupnoOld+"_得分", dGrpScore.toString());
					}
					IndexScoreGroup = new HashMap();
					groupnoOld=grpno;
					dGrpScore=new BigDecimal(0); 
				}
				IndexScoreGroup.put(indexno, indexScore);
				IndexScoreGroup.put(indexno.split("\\$")[1]+"_得分", indexScore);//用于指标组规则计算
				dGrpScore=dGrpScore.add(new BigDecimal(indexScore));
			}
			if(IndexScoreGroup!=null){
				GroupScoreMap.put(groupnoOld, IndexScoreGroup);
				GroupScoreMap.put(groupnoOld+"_得分", dGrpScore.toString());
			}		
			ArrayList ccrMGroupScoreList = ccrAppAll.getCcrMGroupScoreList();
			Iterator ccrMGroupScoreListIter = ccrMGroupScoreList.iterator();
			
			HashMap modelGroupScore= new HashMap();

			BigDecimal dModelScore=new BigDecimal(0); 
			//评组得分
			while(ccrMGroupScoreListIter.hasNext()){
				CcrMGroupScore ccrMGroupScore= (CcrMGroupScore)ccrMGroupScoreListIter.next();
				String grpno = ccrMGroupScore.getGroupNo();
				//取得指标组对应的规则交易
				KeyedCollection kcoll=indService.getIndGroup(grpno, this.getConnection());
				//交易id
				String transid=(String) kcoll.getDataValue("trans_id");
				//自动汇总指标组得分
				String groupScore=(String)GroupScoreMap.get(grpno+"_得分");
				Map indexScoreMap=(HashMap)GroupScoreMap.get(grpno);
				if(indexScoreMap==null){
					indexScoreMap=new HashMap();
					groupScore="0";
				}
				indexScoreMap.put("IN_汇总得分", groupScore);
				//组评分规则得到最终得分
				String rule=(String) kcoll.getDataValue("rating_rules");//组评分规则由规则集id+"_"+规则id组成
				//如果定义了指标组得分规则，根据规则取得分
				if(rule!=null&&rule.contains("\\$")){
					Map outmap = shuffleService.fireTargetRule(rule.split("\\$")[0],rule.split("\\$")[1], indexScoreMap);
					groupScore=(String) outmap.get("OUT_得分");
				}
				ccrMGroupScore.setGroupScore(groupScore);
				modelGroupScore.put(transid+"_得分", groupScore);
				dModelScore=dModelScore.add(new BigDecimal(groupScore));
			}
			
			//评模型得分
			//得分 将由"得分|等级" 组成 
			modelGroupScore.put("cusid", ccrAppAll.getCcrAppInfo().getCusId());//将客户号放入 
			//modelGroupScore.put("com_grp_mode", ccrAppAll.getCcrAppDetail().getComGrpMode());//将集团客户类型放入 
			//modelGroupScore.put("grp_crd_grade", ccrAppAll.getCcrAppDetail().getGrpCrdGrade());//将将母公司评级放入 
			//modelGroupScore.put("new_com_flag", ccrAppAll.getCcrAppDetail().getNewComFlag());  //放入 是否新增企业 标识
			modelGroupScore.put("serno", ccrAppAll.getCcrAppInfo().getSerno());  //放入 是否新增企业 标识
			modelGroupScore.put("IN_汇总得分", dModelScore.toString());
			//取得指标模型对应的规则交易
			KeyedCollection kcoll=indService.getIndModel(modelNo, this.getConnection());
			//模型评分规则
			String rule=(String) kcoll.getDataValue("rating_rules");//组评分规则由规则集id+"_"+规则id组成
			Map outmap = shuffleService.fireTargetRule(rule.split("\\$")[0],rule.split("\\$")[1], modelGroupScore);
			String allScore = (String) outmap.get("OUT_评级总分");
			String autoScore = (String) outmap.get("OUT_得分");
			String autoGrade =(String) outmap.get("OUT_等级");
			String limitGrade = (String) outmap.get("OUT_限定等级");
			String limitReason = (String) outmap.get("OUT_限定原因");
			if(allScore==null||"".equals(allScore)){
				String errMsg="获取模型得分失败,请检查模型配置";
				logger.error(errMsg);
				throw new ComponentException(errMsg);
			}
			ccrAppDetail.setAllScore(allScore);
			ccrAppDetail.setAutoScore(new BigDecimal(autoScore));
			ccrAppDetail.setLimit_grade(limitGrade);
			ccrAppDetail.setLimit_reason(limitReason);
			
			/**
			 * 设置限制等级
			 */
			//CcrLimitGradeComponent clgComponent = (CcrLimitGradeComponent)this.getComponent(CcrPubConstant.CcrLimitGrade);
			//HashMap<String,String> hm = clgComponent
			//		.getModelLimitValue(modelNo, autoGrade, ccrAppAll.getCcrAppDetail().getSerno(),
			//				ccrGIndScoreList, ccrAppAll.getCcrAppDetail().getNewComFlag());
			//ccrAppDetail.setLimit_grade(hm.get("limit_grade"));
			//ccrAppDetail.setLimit_reason(hm.get("reason"));
			
			//将自动评级等级保存入评级信息domain中
			//后边将把ccrAppInfo保存入数据库.
			ccrAppDetail.setAutoGrade(autoGrade);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}
		return null;
	}
	
	/**
	 * 将财务指标值插入指标结果表中
	 * @param  cusId  	客户编号或保证人编号
	 * @param  serno	业务流水号 
	 * @param  ind_date  分类日期
	 * @return
	 * @throws ComponentException
	 */		
//	private void insertCusComIndResult(String serno,String ind_date,String modelNo,String cusId) throws ComponentException
//	{
//		
//		/*分解日期*/
//		int iyear=0;
//		int imonth=0;
//		int iday=0;
//		if(ind_date==null||ind_date.trim().equals("")||ind_date.indexOf("-")<0){
//			logger.info("方法  insertIndResult:日期参数 ind_date="+ind_date+" 非法!");
//			return ;
//		}
//		String dates[]=ind_date.split("-");
//		if(dates==null||dates.length!=3){
//			logger.info("方法  insertIndResult:日期参数 ind_date="+ind_date+" 非法!");
//			return;
//		}
//		/*取上年末的报表*/
//		iyear=Integer.parseInt(dates[0])-1;
//		imonth=12;
//		iday=0;
//		/*获得组件类*/
//    	Fnc4FnaInterface FncFna=(Fnc4FnaInterface)this.getComponentInterface(FNAPubConstant.FNA_IFACE);
//    	
//    	/*判断上一年度的财务报表是否录入*/
//    	boolean bret = false;
//		try {
//			/**
//			 * 需要改进
//			 * 判断 是否需要三年的财务报表
//			 */
//			bret = FncFna.isExistStatBase(cusId, "4", ""+iyear+""+imonth);
//		} catch (EMPException e) {
//			e.printStackTrace();
//		}
//    	if(!bret){
//    		throw new ComponentException("上年度客户的财务报表数据不完整，不能评级!");
//    	}
//    	IndResultValIface indResVal = (IndResultValIface)this.getComponentInterface(IndPubConstant.IND_INSERT_RESULT_VAL);
//    	
//    	IndModGrpIndIface indModGrpIndIFace=(IndModGrpIndIface)this.getComponentInterface(IndPubConstant.IND_MOD_GRP_INDEX_IFACE_ID);
//    	ArrayList array = indModGrpIndIFace.getFncIndexArray(modelNo);
//    	
//      if(array==null||array.size()==0){
//    	  String errMsg="通过评级指标模块获取财务报表编号失败,请在指标库中录入该指标对应的\"财务报表编号\"";
//    	  logger.error(errMsg);
//    	  throw new ComponentException(errMsg);
//      }
//      for (int i = 0 ; i < array.size(); i++)
//      {
//    	  HashMap<String,String> hs = new HashMap<String, String>();
//  		  String [] str = (String[])array.get(i); 
//  			  indResVal.deleteIndResultVal(serno, iyear, imonth, 0, str[0]);
//  			  hs.put("serno", serno);
//  			  hs.put("ind_year", ""+iyear);
//  			  hs.put("ind_month", ""+imonth);
//  			  hs.put("ind_day","0");
//  			  hs.put("index_no", str[0]);
//  			  hs.put("index_name", str[1]);
//  			  hs.put("index_value", "");
//  			  indResVal.insertIndResultVal(hs); 
//      }
//	}
	/**
	 * 更新申请得分字段.
	 * @param ccrAppInfo
	 * @return
	 * @throws ComponentException
	 */
	public String updateCcrAppAll(CcrAppInfo ccrAppInfo,CcrAppDetail ccrAppDetail) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        ccrAgent.updateCcrAppInfo(ccrAppInfo);
        ccrAgent.updateCcrAppDetail(ccrAppDetail);
		return CMISMessage.SUCCESS;
	}
	
	/**
	 * 更新申请明主表的建议得分字段.
	 * @param ccrAppInfo
	 * @return
	 * @throws ComponentException
	 */
	public String updateCcrAppInfo(CcrAppInfo ccrAppInfo) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        ccrAgent.updateCcrAppInfo(ccrAppInfo);
		return CMISMessage.SUCCESS;
	}
	
	/**
	 * 更新申请明细表的建议得分字段.
	 * @param ccrAppDetail
	 * @return
	 * @throws ComponentException
	 */
	public String updateCcrAppDetail(CcrAppDetail ccrAppDetail) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
        ccrAgent.updateCcrAppDetail(ccrAppDetail);
		return CMISMessage.SUCCESS;
	}
	
	public CcrRatDirect getCcrRatDirectAppInfoBySerno(String serno) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		CcrRatDirect crd = new CcrRatDirect();
		crd = (CcrRatDirect)ccrAgent.findCMISDomainByKeyword(crd, "CcrRatDirect", serno);
		return crd;
	}

	/**
	 * 交易是否该客户存在未通过的业务申请
	 * @param cusId
	 * @throws ComponentException
	 */
	public int checkCusApp(String cusId) throws ComponentException{
		CcrAgent ccrAgent = (CcrAgent)
		this.getAgentInstance(CcrPubConstant.CCR_AGENT);
		return ccrAgent.checkCusApp(cusId);
	}
	//融资性担保公司列表查询sql
	public String getSql(String condition){
		String conStrSql =  "select a.*,b.is_authorize from ccr_app_info a,ccr_app_detail b where a.flag='4' and a.serno=b.serno"+condition;
		return conStrSql;
	}
	//根据客户编号，办结日期获得其上次评级日期及评级等级
	public KeyedCollection GetDateGrade(String cusId,String flag,String appEndDate){
		KeyedCollection result = new KeyedCollection();
		Map<String,String> map = new HashedMap();
		map.put("cusId", cusId);
		map.put("flag", flag);
		map.put("appEndDate",appEndDate);
		try {
			IndexedCollection ic = SqlClient.queryList4IColl("getDateGradeByCusID",map,this.getConnection());
			if(ic.size()>0){
			  result = (KeyedCollection) ic.get(0);
			}else{
			  try {
				result.addDataField("app_end_date", "");
				result.addDataField("adjusted_grade", "");
			  } catch (InvalidArgumentException e) {
				e.printStackTrace();
			  } catch (DuplicatedDataNameException e) {
				e.printStackTrace();
			  }
			  
			}
		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}

package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtAppJointCoopFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			String table_name = wfiMsg.getTableName();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollJc = dao.queryDetail(table_name, serno, connection);
			String openDate = (String)context.getDataValue(PUBConstant.OPENDAY);
			kCollJc.setDataValue("over_date", openDate);
			//更新
			dao.update(kCollJc, connection);
			
			String term_type = (String)kCollJc.getDataValue("term_type");
			String term  = (String)kCollJc.getDataValue("term");
			kCollJc.remove("app_date");
			kCollJc.remove("over_date");
			kCollJc.remove("term_type");
			kCollJc.remove("term");
			kCollJc.remove("approve_status");
			//起始日期  到期日期  协议状态
			kCollJc.put("start_date", openDate);
			String end_date = LmtUtils.computeEndDate(openDate, term_type, term);
			kCollJc.put("end_date", end_date);
			kCollJc.put("agr_flag","0");
			String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);//协议编号
			kCollJc.put("agr_no",agrNo);
			kCollJc.put("agr_status","002");
			kCollJc.setName("LmtAgrJointCoop");
			//add协议数据
			dao.insert(kCollJc, connection);
			/**add by lisj 2015-3-4 需求编号 :【XD150306018】关于信贷联保授信业务变更 begin**/
			IndexedCollection LAN = dao.queryList("LmtAppNameList", "where serno='" + serno + "' and sub_type='03'", connection);
			for(Iterator<KeyedCollection> iterator4LAN = LAN.iterator();iterator4LAN.hasNext();){
				KeyedCollection temp4LAN = (KeyedCollection)iterator4LAN.next();
				String member_cus_id = temp4LAN.getDataValue("cus_id").toString();//联保小组成员ID
				IndexedCollection lmtAgrDetailsIC = dao.queryList("LmtAgrDetails", "where cus_id='"+member_cus_id+"' and sub_type='03'", connection);
				if(lmtAgrDetailsIC!=null && lmtAgrDetailsIC.size()>0){
					for(Iterator<KeyedCollection> iterator = lmtAgrDetailsIC.iterator();iterator.hasNext();){
						KeyedCollection temp = (KeyedCollection)iterator.next();
						temp.setDataValue("lmt_status", "30");
						temp.setName("LmtAgrDetails");
						dao.update(temp, connection);//更新额度状态为失效
					}
				}		
			}
			
			/**add by lisj 2015-3-4 需求编号 :【XD150306018】关于信贷联保授信业务变更 end**/
			
			/**
			 * 查找小组申请的名单,将名单数据转存批后名单表，并将申请分项信息插入台账表
			 */
			KeyedCollection kCollTemp = null;
			IndexedCollection iColl = dao.queryList("LmtAppNameList", "where serno='" + serno + "'", connection);
			for(int i=0;i<iColl.size(); i++){
				kCollTemp = (KeyedCollection) iColl.get(i);
				//该客户是否设置额度
				String is_limit_set = (String)kCollTemp.getDataValue("is_limit_set");
				if("1".equals(is_limit_set)){
					String cus_id = (String)kCollTemp.getDataValue("cus_id");
					String condition = " where cus_id='"+cus_id+"' and serno='"+serno+"'";
					IndexedCollection iCollAppDet = dao.queryList("LmtAppDetails", condition, connection);
					for(int j=0;j<iCollAppDet.size();j++){
						KeyedCollection kCollAppDet = (KeyedCollection)iCollAppDet.get(j);
						kCollAppDet.put("agr_no", agrNo);
						kCollAppDet.put("start_date", openDate);
						String termType = (String)kCollAppDet.getDataValue("term_type");
						String termDet = (String)kCollAppDet.getDataValue("term");
						String endDate = LmtUtils.computeEndDate(openDate, termType, termDet);
						kCollAppDet.put("end_date", endDate);
						kCollAppDet.put("lmt_status", "10");//设置额度状态为正常
						kCollAppDet.put("enable_amt", kCollAppDet.getDataValue("crd_amt"));//启用金额直接赋值为授信金额
						kCollAppDet.remove("serno");
						kCollAppDet.remove("ori_crd_amt");
						kCollAppDet.remove("org_limit_code");
						kCollAppDet.setName("LmtAgrDetails");
						dao.insert(kCollAppDet, connection);
					}
				}
				//名单表信息
				kCollTemp.put("agr_no", agrNo);
				kCollTemp.put("cus_status", "1");//客户状态置为有效
				kCollTemp.remove("serno");
				kCollTemp.setName("LmtNameList");
				dao.insert(kCollTemp, connection);
			}
			
			/**
			 * 查找保证金信息，并将协议编号回写保证金信息表
			 */
			IndexedCollection iCollBai = dao.queryList("PubBailInfo", "where serno='" + serno + "'", connection);
			for(int i=0;i<iCollBai.size();i++){
				KeyedCollection kCollBai = (KeyedCollection) iCollBai.get(i);
				kCollBai.setDataValue("cont_no", agrNo);//协议编号
				dao.update(kCollBai, connection);
			}
			
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl = new KeyedCollection();
//			kColl.put("CLIENT_NO", kCollJc.getDataValue("cus_id"));
//			kColl.put("BUSS_SEQ_NO", "");
//			kColl.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl, context, connection);	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, context)){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		String instanceId = "";//流程实例号
		String tableName = "";//表名
		String openDay = (String)this.getContext().getDataValue("OPENDAY");
		//获取流程实例号作为规则参数
		String condition = "where pk_value='"+pkVal+"' and table_name='"+tabModelId+"'";
		IndexedCollection iCollJoin = dao.queryList("WfiJoin", condition, this.getConnection());
		if(iCollJoin.size()>0){
			KeyedCollection kCollJoin = (KeyedCollection)iCollJoin.get(0);
			instanceId = (String)kCollJoin.getDataValue("instanceid");
		}
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		
		/**责任人存在多个机构时取责任机构*/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		OrganizationServiceInterface userService;
		ShuffleServiceInterface shuffleService;
		List<SOrg> orgslist = null;
		try {
			userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			//上级机构
			SOrg supOrg = userService.getSupOrg(manager_br_id, conn);
			param.put("super_org_id", supOrg.getOrganno());
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		/**modified by lisj 2015-4-20  需求编号：【XD150407025】分支机构授信审批权限配置 begin**/
		/** 联保授信特殊授权检查（石狮支行-布料行业 单户500w 总额81100w） */
		if(tabModelId.equals("LmtAppJointCoop")){
			tableName = "lmt_app_joint_coop";
		}else{
			tableName = "lmt_app_joint_coop_redi";
		}
		//计算到期日(当前日期减去6个月)
		openDay = LmtUtils.computeEndDate(openDay, "002", "-6");
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("IN_SERNO", pkVal);
		modelMap.put("IN_INSTANCEID", instanceId);
		modelMap.put("IN_OPENDAY",openDay);
		modelMap.put("IN_TABLENAME", tableName);
		modelMap.put("IN_CUSID", (String)kc.getDataValue("cus_id"));
		modelMap.put("IN_BIZ_BRID", manager_br_id);
		/**modified by lisj 需求编号:【XD140916059】2015-1-7 凤里支行特别授权改造 begin**/
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			//Map<String, String> outMap=shuffleService.fireTargetRule("LMTJOINTCOOPSPECIALCHK", "SHISHIBULIAOAUTH", modelMap);
			//Map<String, String> outMap4FLCG=shuffleService.fireTargetRule("LMTJOINTCOOPSPECIALCHK", "FENGLICHILDRENSGARAUTH", modelMap);//凤里支行童装与儿童产业联保特别授权
			//Map<String, String> outMap4FLCP=shuffleService.fireTargetRule("LMTJOINTCOOPSPECIALCHK", "FENGLICASUALPANTSAUTH", modelMap);//凤里支行休闲裤联保特别授权
			Map<String, String> outMap4LmtSCORule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSCOPRATIONRULE", modelMap);//特别授权入口规则（无条件执行支行权限）
			String flowType4SCORule = (String) outMap4LmtSCORule.get("OUT_FLOWTYPE");
			Map<String, String> outMap4LmtSCRRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTSPECIALCREDITRIGHT", modelMap);//授信特殊权限授权（数据需后台维护）
			String flowType4SCRRule  = (String) outMap4LmtSCRRule.get("OUT_FLOWTYPE");
			Map<String, String> outMap4LmtLCRGRule=shuffleService.fireTargetRule("LMTBPRIGHT", "LMTLCRGENERALRULE", modelMap);//授信权限配置总规则
			String flowType4LmtLCRGRule= (String)outMap4LmtLCRGRule.get("OUT_FLOWTYPE");
			//String flowType = (String)outMap.get("OUT_FLOWTYPE");
			//String flowType4FLCG = (String)outMap4FLCG.get("OUT_FLOWTYPE");
			//String flowType4FLCP = (String)outMap4FLCP.get("OUT_FLOWTYPE");
			if("ALLOW".equals(flowType4SCRRule)){
				param.put("approve_org", "S0200");
			}/**else if("ALLOW".equals(flowType)){
				param.put("approve_org", "S0200");//岗位	
			}else if("ALLOW".equals(flowType4FLCG)){
				param.put("approve_org", "S0200");//岗位	
			}else if("ALLOW".equals(flowType4FLCP)){
				param.put("approve_org", "S0200");//岗位	
			}**/else if("ALLOW".equals(flowType4SCORule)){
				param.put("approve_org", "S0200");
			}else if("ALLOW".equals(flowType4LmtLCRGRule)){
				param.put("approve_org", "S0200");
			}else if("ALLOWBRANCH".equals(flowType4LmtLCRGRule)){
				param.put("approve_org", "S0226");//分行权限
			}else{
				param.put("approve_org", "");
			}
			/**modified by lisj 需求编号:【XD140916059】2015-1-7 凤里支行特别授权改造 end**/
			param.put("approve_supor_org", "");//支行行长-->总行权限
			KeyedCollection  SOrg = dao.queryAllDetail("SOrg", manager_br_id, this.getConnection());
			String suporganno = (String) SOrg.getDataValue("suporganno");//上级机构
			if(suporganno!=null && !"".equals(suporganno) && "9350500000".equals(suporganno)){
				param.put("approve_supor_org", "D0005");//支行行长-->总行权限
			}
			param.put("cus_id", cus_id);
			/**modified by lisj 2015-4-20  需求编号：【XD150407025】分支机构授信审批权限配置  end**/
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		
		return param;
	}

}
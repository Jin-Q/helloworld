package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBizFlowComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class IqpBizFlowImpl extends CMISComponent implements BIZProcessInterface {

	private static final String IQPMAINMODELID = "IqpLoanApp";//业务申请主表模型
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/** 审批同意时执行业务处理逻辑 */
		String status = "";
		String serno = "";
		String prdId = "";
		try {
			status = wfiMsg.getWfiStatus();
			if(status.equals("997")){//流程审批通过
				/**根据不同的业务类型调用不同的处理方法,后期流程配置后,可直接调用*/
				serno = wfiMsg.getPkValue();
				TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection mainKColl = dao.queryDetail(IQPMAINMODELID, serno, this.getConnection());
				prdId = (String)mainKColl.getDataValue("prd_id");
				
//				//暂时使用
//				if(prdId==null){
//					KeyedCollection rpKColl = dao.queryDetail("IqpRpddscnt", serno, this.getConnection());
//					prdId = (String)rpKColl.getDataValue("prd_id");
//				}
//				
//				//暂时使用
//				if(prdId==null){
//					KeyedCollection rpKColl = dao.queryDetail("IqpAssetstrsf", serno, this.getConnection());
//					prdId = (String)rpKColl.getDataValue("prd_id");
//				}
				
				IqpBizFlowComponent IqpBizFlowComponent = (IqpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
							.getComponentInstance(AppConstant.IQPBIZFLOWCOMPONENT, this.getContext(), this.getConnection());
				//银行承兑汇票
				if(prdId.equals("200024")){
					IqpBizFlowComponent.doWfAgreeForIqpAccp(serno,AppConstant.IQPCONTTYPE4YC);
				}
				//直贴
				else if(prdId.equals("300021") || prdId.equals("300020")){
					IqpBizFlowComponent.doWfAgreeForIqpDisc(serno,AppConstant.IQPCONTTYPE4TX);
				}
//				//转贴现
//				else if(prdId.equals("300024")||prdId.equals("300023")||prdId.equals("300022")){
//					IqpBizFlowComponent.doWfAgreeForIqpRpddscnt(serno,"");
//				}
				//境内保函
				else if(prdId.equals("400021")){
					IqpBizFlowComponent.doWfAgreeForIqpGuarant(serno,AppConstant.IQPCONTTYPE4BH);
				}
//				else if(prdId.equals("")){//委托贷款委托合同100063
//					IqpBizFlowComponent.doWfAgreeForIqpCsgnLoan(serno,"W");
//				}
				//贸易融资
				else if(prdId.equals("500027") || prdId.equals("500028") || prdId.equals("500026") || prdId.equals("500021") || prdId.equals("500020") ||
						 prdId.equals("500032") || prdId.equals("500029") || prdId.equals("500031") || prdId.equals("500022") || prdId.equals("500025") || 
						 prdId.equals("500024") || prdId.equals("500023") || prdId.equals("400020") || prdId.equals("700020") || prdId.equals("700021")){
					IqpBizFlowComponent.doWfAgreeForIqpTfLoan(serno,this.getContext(),this.getConnection());
				}
//				//资产转受让
//				else if(prdId.equals("600020")){
//					IqpBizFlowComponent.doWfAgreeForIqpAsset(serno,"");
//				}
				//普通贷款
				else{
					IqpBizFlowComponent.doWfAgreeForIqpLoan(serno,AppConstant.IQPCONTTYPE4DK);
				} 
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行业务处理逻辑

	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// 审批否决时执行业务处理逻辑
		String serno = "";
		try {
			serno = wfiMsg.getPkValue();
			//删除授信关系
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, this.getContext(), this.getConnection());
			iqpLoanAppComponent.deleteLmtRelation(serno);
			//担保关系撤销
			SqlClient.update("cancelGrtLoanGurBySerno", serno, null, null, this.getConnection());
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(IQPMAINMODELID, serno, this.getConnection());
			String prd_id = (String) kColl.getDataValue("prd_id");
			KeyedCollection kCollForBatch = null;
			if(prd_id.equals("300021") || prd_id.equals("300020")){//判断是否贴现业务
				String condition = " where serno='"+serno+"'";
				kCollForBatch=dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
				if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
					kCollForBatch.setDataValue("status", "04");//改为【作废】状态
					dao.update(kCollForBatch, this.getConnection());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("否决流程业务处理异常，请检查业务处理逻辑！");
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		try { 
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpBizFlowComponent IqpBizFlowComponent = (IqpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBIZFLOWCOMPONENT, this.getContext(), this.getConnection());
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			
			Map<String, String> param = new HashMap<String, String>();
			String manager_id = null;
			String is_space_back = "N";
			String IsBranch = "Y/N";
			//---XD150828065   非标债权业务权限变更 Edited by FCL 20150909 --start
			String is_org_up = "N";
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			String condition = "where is_main_manager='1' and serno='"+pkVal+"'";
			IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
			if(iqpIColl.size()>0){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
				manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
			} 
			String is_trust_loan = (String)kc.getDataValue("is_trust_loan");
			String prd_id = (String)kc.getDataValue("prd_id");//2014-09-11 Edited by FCL 调整位置  XD140911058
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			param.put("is_trust_loan", is_trust_loan);
			String sqlWhere = " where organno='"+manager_br_id+"' and end_date>='"+this.getCurDate()+"'";
			IndexedCollection idxColl = dao.queryList("WfOrgRightUp", sqlWhere, this.getConnection());
			if(idxColl.size()>0){
				is_org_up = "Y";
			}
			param.put("is_org_up",is_org_up);
			//获取客户条线,申请金额
			String belgLine = null;
			String cus_id = (String)kc.getDataValue("cus_id");
			//获取实时汇率  start
			String cur_type = (String) kc.getDataValue("apply_cur_type");
			KeyedCollection kCollRate = service.getHLByCurrType(cur_type, this.getContext(), this.getConnection());
			if("failed".equals(kCollRate.getDataValue("flag"))){
				logger.error("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			//获取实时汇率  end
			BigDecimal apply_amount = BigDecimalUtil.replaceNull(kc.getDataValue("apply_amount"));
			apply_amount = apply_amount.multiply(exchange_rate);
			//如果是定向资管委托贷款则需要计算单户金额（本笔金额加上+生成有效合同部分金额）
			KeyedCollection kCollSub = dao.queryDetail("IqpLoanAppSub", pkVal, this.getConnection());
			if(kCollSub != null ){
				String sernoSelect = (String)kCollSub.getDataValue("serno");
				if(sernoSelect != null && !"".equals(sernoSelect)){
					//2014-09-11 Edited by FCL 需求编号:XD140911058 委托贷款全部按单户计算权限总额
					if("100063".equals(prd_id) || "100065".equals(prd_id)){
						apply_amount = IqpBizFlowComponent.caculateAmt4Cus(cus_id, apply_amount);
					}
					//-------------------XD140911058--END-----------------------------------
				}
			}
			BigDecimal amt = apply_amount.divide(new BigDecimal(10000), BigDecimal.ROUND_UP);
			String apply_amt = amt.toString();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
			belgLine = Cus.getBelgLine();
			param.put("bizline", belgLine);
			param.put("amt", apply_amt);
			
			//判断是否为贸易融资额度合同项下支用，如果是，则需走放款中心审查岗
			String isLimitContPay = (String)kc.getDataValue("is_limit_cont_pay");
			param.put("isLimitContPay", isLimitContPay);
			
			
			//用信判断 2014.6.6  WangShuo添加
			String app_type = "";//业务分类
			BigDecimal sigBill_amt = new BigDecimal(0);//单票金额
			BigDecimal sig_amt = new BigDecimal(0);//单笔金额
			BigDecimal single_amt = new BigDecimal(0);//单日累计金额
			
			//判断管理机构上级机构是否是泉州银行
			OrganizationServiceInterface serviceUser;
			serviceUser = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			String IsTeam="no";
			/**责任人存在多个机构时取责任机构*/
			/*KeyedCollection kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			if(kColl4STO != null && kColl4STO.containsKey("team_no") && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				List<SOrg> orgslist = serviceUser.getDeptOrgByActno(manager_id, this.getConnection());
				if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
					manager_br_id = orgslist.get(0).getOrganno();
				}
				IsTeam="no";
			}*/
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			SOrg org_info = serviceUser.getOrgByOrgId(manager_br_id, this.getConnection());//根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();//获取主管客户经理所在机构的上级机构
			if("9350500000".equals(suporganno)){//（若上级机构不是泉州分行，则需要走分行行长）
				IsBranch ="Y";
			}else{
				IsBranch ="N";
			}
			//票据直贴（单票）
			if("300020".equals(prd_id) || "300021".equals(prd_id)){
				app_type = "iqp_30002x";
				//取该业务下单笔票面金额最大的票的票面金额
				String sql_select =	"where porder_no in( select b.porder_no from iqp_batch_bill_rel b " +
						            "where b.batch_no in(select a.batch_no from iqp_batch_mng a where a.serno='"+pkVal+"')) " +
						     		"order by drft_amt desc";
				IndexedCollection iCollBillDetail = dao.queryList("IqpBillDetail", sql_select, this.getConnection());
				if(iCollBillDetail.size()>0){
					KeyedCollection kColl = (KeyedCollection)iCollBillDetail.get(0);
					sigBill_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("drft_amt"));
				}
			//委托贷款
			}else if("100063".equals(prd_id) || "100065".equals(prd_id)){
					app_type = "iqp_10006x";
					sig_amt = apply_amount;
			//贷款，银承，保函
			}else if(prd_id.startsWith("10") || "200024".equals(prd_id) || "400020".equals(prd_id) || "400021".equals(prd_id)){
				//龙岩分行
				String organNo = (String)this.getContext().getDataValue("organNo");
				if("9350800000".equals(organNo) || "9350801001".equals(organNo)){
					is_space_back = "Y";
				}else{
					is_space_back = "N";
				}
				app_type = "iqp_01";
			}else{
				app_type = "iqp_02";
			}
			Map<String, String> modelMap = new HashMap<String, String>();
			modelMap.put("IN_APP_TYPE", app_type);
			modelMap.put("IN_ISBRANCH", IsBranch);
			modelMap.put("IN_IS_SPACE_BACK", is_space_back);
			modelMap.put("IN_单笔金额", sig_amt.toString());
			modelMap.put("IN_单日累计金额", single_amt.toString());
			modelMap.put("IN_单票金额", sigBill_amt.toString());
			modelMap.put("IN_机构", (String)this.getContext().getDataValue("organNo"));
			/**add by lisj 2015-05-14 需求编号：【XD150407025】分支机构授信审批权限配置 (三安钢铁贴现业务特别授权)begin**/
			modelMap.put("IN_CUSID", cus_id);
			modelMap.put("IN_PRDID", prd_id);
			/**add by lisj 2015-05-14 需求编号：【XD150407025】分支机构授信审批权限配置 (三安钢铁贴现业务特别授权)end**/
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			Map<String, String> outMap=shuffleService.fireTargetRule("IQPPOWERRULE", "HEADIQPRULE", modelMap);
			
			if("1".endsWith(is_trust_loan) && "iqp_01".equals(app_type)){
				outMap.put("OUT_终审岗位", "");
			}
			  
			 
			param.put("approve_org", (String)outMap.get("OUT_终审岗位"));//岗位
			param.put("is_space_back", is_space_back);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			param.put("IsTeam", IsTeam);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			param.put("IsBranch", IsBranch);
			param.put("suporganno", suporganno);
			param.put("prd_id", prd_id);
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

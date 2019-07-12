package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBizFlowComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class IqpRpddscntBizFlowImpl extends CMISComponent implements
		BIZProcessInterface {
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
		try {
			status = wfiMsg.getWfiStatus();
			if(status.equals("997")){//流程审批通过
				serno = wfiMsg.getPkValue();
				IqpBizFlowComponent IqpBizFlowComponent = (IqpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
							.getComponentInstance(AppConstant.IQPBIZFLOWCOMPONENT, this.getContext(), this.getConnection());
				IqpBizFlowComponent.doWfAgreeForIqpRpddscnt(serno,"");
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
			
			//更新票据批次包业务编号，去除关联关系。
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String condition = " where serno='"+serno+"'";
			KeyedCollection kCollForBatch=dao.queryFirst("IqpBatchMng", null, condition, this.getConnection());
			if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
				kCollForBatch.setDataValue("status", "04");//改为【作废】状态
				dao.update(kCollForBatch, this.getConnection());
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
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();	
			IqpBizFlowComponent iqpBizFlowComponent = (IqpBizFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBIZFLOWCOMPONENT, this.getContext(), this.getConnection());
			Map<String, String> param = new HashMap<String, String>();
			String manager_id = null;
			String batch_type = null;
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			String condition = "where is_main_manager='1' and serno='"+pkVal+"'";
			IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
			if(iqpIColl.size()>0){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
				manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
			} 
			
			BigDecimal total_amt = BigDecimalUtil.replaceNull(kc.getDataValue("bill_total_amt"));//票面总金额(万元)
			BigDecimal amt = total_amt.divide(new BigDecimal(10000));
			String apply_amt = amt.toString();
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			String rpddscnt_type = (String)kc.getDataValue("rpddscnt_type");//转贴现方式
			String prd_id = (String)kc.getDataValue("prd_id");//产品种类
			if("300023".equals(prd_id) && "02".equals(rpddscnt_type)){//外部转贴现 && 买入返售
				batch_type = "01";
			}else if("300023".equals(prd_id) && "04".equals(rpddscnt_type)){//外部转贴现 && 卖出回购
				batch_type = "02"; 
			}else{
				batch_type = "00";
			}
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			KeyedCollection kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			String IsTeam="";
			if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				IsTeam="no";
			}
			param.put("IsTeam", IsTeam);
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			param.put("amt", apply_amt);
			param.put("bus_type", rpddscnt_type);
			param.put("batch_type", batch_type);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			
			//用信判断 2014.6.6  WangShuo添加
			String app_type = "";//业务分类
			BigDecimal sigBill_amt = new BigDecimal(0);//单票金额
			BigDecimal sig_amt = new BigDecimal(0);//单笔金额
			BigDecimal single_amt = new BigDecimal(0);//单日累计金额
			String wfiDutyNoList = "";
			
			if("300024".equals(prd_id) || "300023".equals(prd_id)){
				//再贴现
				if("300024".equals(prd_id)){
					app_type = "iqp_300024";
					wfiDutyNoList = "D0026,S0211,S0240,S0002,S0001";
				}else if("300023".equals(prd_id) && "04".equals(rpddscnt_type)){
					app_type = "iqp_300023_04";
					wfiDutyNoList = "D0008,D0029,S0002,S0001";
				}else if("300023".equals(prd_id) && "02".equals(rpddscnt_type)){
					app_type = "iqp_300023_02";
					wfiDutyNoList = "D0008,D0029,S0002,S0001";
				}else if("300023".equals(prd_id) && "03".equals(rpddscnt_type)){
					app_type = "iqp_300023_03";
					wfiDutyNoList = "D0026,S0211,S0240,S0002,S0001";
				}else if("300023".equals(prd_id) && "01".equals(rpddscnt_type)){
					app_type = "iqp_300023_01";
					wfiDutyNoList = "D0026,S0211,S0240,S0002,S0001";
				}
				sig_amt = total_amt;
				String dutyNoList =(String)this.getContext().getDataValue("dutyNoList");
				
				String dutyCurrent = iqpBizFlowComponent.findCurrentDuty(dutyNoList, wfiDutyNoList);
				logger.error("当前登录人岗位（根据本笔流程）:"+dutyCurrent);
				single_amt = iqpBizFlowComponent.getAllAmt("0018", rpddscnt_type, dutyCurrent,total_amt,pkVal);
				logger.error("单日累计:"+single_amt);
			}
			
			
			Map<String, String> modelMap = new HashMap<String, String>();
			modelMap.put("IN_APP_TYPE", app_type);
			modelMap.put("IN_单笔金额", sig_amt.toString());
			modelMap.put("IN_单日累计金额", single_amt.toString());
			modelMap.put("IN_单票金额", sigBill_amt.toString());
			modelMap.put("IN_机构", (String)this.getContext().getDataValue("organNo"));
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			Map outMap=shuffleService.fireTargetRule("IQPPOWERRULE", "HEADIQPRULE", modelMap);
			param.put("approve_org", (String)outMap.get("OUT_终审岗位"));//岗位
			logger.error("终审岗位:"+outMap.get("OUT_终审岗位")+"");
			
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

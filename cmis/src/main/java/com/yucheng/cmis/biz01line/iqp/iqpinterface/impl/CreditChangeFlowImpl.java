package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpCreditChangeFlowComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CreditChangeFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	private static final String IQPMAINMODELID = "IqpCreditChangeApp";//业务申请主表模型
	
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
		String modelId = ""; 
		try {
			status = wfiMsg.getWfiStatus();
			if(status.equals("997")){//流程审批通过 
				serno = wfiMsg.getPkValue();
				modelId = wfiMsg.getTableName();
				TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection mainKColl = dao.queryDetail(modelId, serno, this.getConnection());
				prdId = (String)mainKColl.getDataValue("prd_id");  
				
				IqpCreditChangeFlowComponent iqpCreditChangeFlowComponent = (IqpCreditChangeFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
							.getComponentInstance(AppConstant.IQPCREDICHANGEFLOWCOMPONENT, this.getContext(), this.getConnection());
				if("700020".equals(prdId) || "700021".equals(prdId)){//信用证修改
					iqpCreditChangeFlowComponent.doWfAgreeForCreditChange(serno);
				}else if("400020".equals(prdId) || "400021".equals(prdId)){//保函修改
					iqpCreditChangeFlowComponent.doWfAgreeForGuarantChange(serno);
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("否决流程业务处理异常，请检查业务处理逻辑！");
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		Map<String, String> param = new HashMap<String, String>();
		String manager_id = null;
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String condition = "where is_main_manager='1' and cont_no='"+kc.getDataValue("cont_no")+"'";
		IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
		
		if(iqpIColl.size()>0){ 
			KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
			manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
		}
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
		String IsTeam="";
		KeyedCollection kColl4STO = new KeyedCollection();
		try {
			kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
		} catch (SQLException e) {}
		if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
			IsTeam="yes";
		}else{
			IsTeam="no";
		}
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		param.put("IsTeam", IsTeam);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		return param;
	}

}

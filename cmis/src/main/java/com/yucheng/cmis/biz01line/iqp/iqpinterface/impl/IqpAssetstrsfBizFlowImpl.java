package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBizFlowComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class IqpAssetstrsfBizFlowImpl extends CMISComponent implements
		BIZProcessInterface {

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
				IqpBizFlowComponent.doWfAgreeForIqpAsset(serno,AppConstant.IQPCONTTYPE4DK);
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
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("IqpAssetstrsf", serno, this.getConnection());
			String asset_no = (String) kColl.getDataValue("asset_no");
			KeyedCollection assetkColl = dao.queryDetail("IqpAsset", asset_no, this.getConnection());
			assetkColl.setDataValue("status", "03");//改为【已办结】状态
			dao.update(assetkColl, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("否决流程业务处理异常，请检查业务处理逻辑！");
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			Map<String, String> param = new HashMap<String, String>();
			String manager_id = null;
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			String condition = "where is_main_manager='1' and serno='"+pkVal+"'";
			IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
			if(iqpIColl.size()>0){
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
				manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
			} 
			
			String takeover_type = (String)kc.getDataValue("takeover_type");//管理机构
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			param.put("takeover_type", takeover_type);
			
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

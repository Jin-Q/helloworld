package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;

public class IqpChkStoreTaskFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	private static final String modelId = "IqpChkStoreTask";//核/巡库待办任务信息
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/** 审批同意时执行业务处理逻辑 */
		String task_id = "";
		try {
			task_id = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(modelId, task_id, this.getConnection());
			kColl.setDataValue("prc_status", "2");
			kColl.setDataValue("act_complete_time", this.getContext().getDataValue("OPENDAY"));
			dao.update(kColl, this.getConnection());
			
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
		
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		Map<String, String> param = new HashMap();
		for(int i=0;i<kc.size();i++){
			DataElement element = (DataElement)kc.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				String value = "";
				if(aField.getValue() == null){
					value = "";
				}else {
					value = aField.getValue().toString();
				}
				param.put(aField.getName(), value);
			}
		}
		
		return param;
	}

}

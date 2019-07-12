package com.yucheng.cmis.biz01line.psp.pspinterface.impl;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class PspRavelSignalFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	private static final String modelIdSig = "PspAltSignal";//预警信号表
	private static final String ModelIdList = "PspRavelSignalList";//解除预警信号列表
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		try {
			serno = wfiMsg.getPkValue();
			//审批通过后将预警信号状态置为“注销”
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			String condition = " where serno = '"+serno+"'";
			IndexedCollection iColl = dao.queryList(ModelIdList, condition, this.getConnection());
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String pk_id = (String) kColl.getDataValue("pk_id");//预警信号id
				String condition1 = " where pk_id='"+pk_id+"'";
				KeyedCollection valueKColl = dao.queryFirst(modelIdSig, null, condition1, this.getConnection());
				valueKColl.setDataValue("signal_status", "3");//将该预警信号状态置为“注销”
				dao.update(valueKColl, this.getConnection());
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
		
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		try{
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		 
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
		CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
		String belgLine = Cus.getBelgLine();

		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("cus_id", cus_id);
		param.put("bizline", belgLine);
		return param;
		}catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

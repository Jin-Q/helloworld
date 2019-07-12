package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;

public class IqpAverageAssetAppFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	private static final String IQPMAINMODELID = "IqpAverageAssetApp";
	private static final String IQPMODELID = "IqpAverageAsset";
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/** 审批同意时执行业务处理逻辑 */
		String status = "";
		String sernoApp = "";
		try {
			status = wfiMsg.getWfiStatus();
			if(status.equals("997")){//流程审批通过
				/**根据不同的业务类型调用不同的处理方法,后期流程配置后,可直接调用*/
				sernoApp = wfiMsg.getPkValue();
				TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection mainKColl = dao.queryDetail(IQPMAINMODELID, sernoApp, this.getConnection());
				mainKColl.setName(IQPMODELID);
				mainKColl.put("average_status","1" );//正常
				dao.insert(mainKColl, this.getConnection());
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
			String cont_no = (String)kc.getDataValue("cont_no");
			if(cont_no!=null && !"".equals(cont_no)){
				KeyedCollection ctrKColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
				String manager_br_id = (String)ctrKColl.getDataValue("manager_br_id");
				
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'";
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, this.getConnection());
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				} 
				//XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-22  ----start
				String IsTeam = "";
				KeyedCollection kColl4tm = dao.queryFirst("STeamUser",null,"where 1=1 and mem_no = '"+manager_id+"'", this.getConnection());
				if(kColl4tm!=null && kColl4tm.size()>0&&kColl4tm.getDataValue("team_no")!=null&&!"".equals((String)kColl4tm.getDataValue("team_no"))){
					IsTeam = "yes";
				}else{
					IsTeam = "no";
				}
				param.put("IsTeam", IsTeam);
				//XD150918069 丰泽鲤城区域团队业务流程改造 Edited by FCL 2015-09-22  ----end
				param.put("manager_br_id", manager_br_id);
				param.put("manager_id", manager_id);
			}
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

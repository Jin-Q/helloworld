package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpflow;

import java.sql.Connection;
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

public class IqpNetMagInfoFlow extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		try {
			Connection connection = this.getConnection();
			String net_agr_no = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			//对入网申请操作
			String condition ="where net_agr_no='"+net_agr_no+"' and app_type='02'";
			IndexedCollection IniColl = dao.queryList("IqpCoreConNet", null, condition, connection);
			if(!IniColl.isEmpty()){
				KeyedCollection InkColl=(KeyedCollection)IniColl.get(0);	
				InkColl.setDataValue("approve_status", "997");//更改审批状态为通过
				dao.update(InkColl, connection);
				String conditonstr="where net_agr_no='"+net_agr_no+"' and status='1'";//获取状态为待入网的成员
				IndexedCollection listiColl = dao.queryList("IqpMemMana", conditonstr, connection);
				for(int i=0;i<listiColl.size();i++){
					KeyedCollection listkColl=(KeyedCollection)listiColl.get(i);
					listkColl.setDataValue("status", "2");
					dao.update(listkColl, connection);
				}
			}								
			//对退网申请操作
			String condition2 ="where net_agr_no='"+net_agr_no+"' and app_type='03'";
			IndexedCollection IniColl2 = dao.queryList("IqpCoreConNet", condition2, connection);
			if(!IniColl2.isEmpty()){
				KeyedCollection InkColl2=(KeyedCollection)IniColl2.get(0);
				InkColl2.setDataValue("approve_status", "997");//更改审批状态为通过
				dao.update(InkColl2, connection);
				String conditonstr2="where net_agr_no='"+net_agr_no+"' and status='3'";//获得待退网成员
				IndexedCollection listiColl2 = dao.queryList("IqpMemMana", conditonstr2, connection);
				for(int i=0;i<listiColl2.size();i++){
					KeyedCollection listkColl2=(KeyedCollection)listiColl2.get(i);
					listkColl2.setDataValue("status", "4");
					dao.update(listkColl2, connection);
				}
			}		
			
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
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}

}

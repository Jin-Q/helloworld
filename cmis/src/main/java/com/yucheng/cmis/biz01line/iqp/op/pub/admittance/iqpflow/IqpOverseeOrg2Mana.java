package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpflow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;

public class IqpOverseeOrg2Mana extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			String serno = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollApp = dao.queryDetail("IqpAppOverseeOrg", serno, connection);
			String openday = context.getDataValue("OPENDAY").toString();//获取系统当前日期
			String end_date = (String)kCollApp.getDataValue("end_date");//获取合作到期日
			String oversee_org_id = (String)kCollApp.getDataValue("oversee_org_id");
			
			String conditionStr = "where oversee_org_id='"+oversee_org_id+"' and oversee_org_status='01'";//监管机构表中状态为'有效'的监管机构
			
			KeyedCollection kCollTemp = dao.queryFirst("IqpOverseeOrg", null, conditionStr, connection);
			
			if(kCollTemp.getDataValue("serno") != null){//存在状态为'有效'的监管机构
				kCollTemp.setDataValue("oversee_org_status", "02");//状态置为'无效'
				dao.update(kCollTemp, connection);
			}
			
			//移除申请表中的字段
			kCollApp.remove("approve_status");
			kCollApp.remove("flow_type");
			//给监管机构管理表中的字段赋值
			kCollApp.setDataValue("eval_time", openday);
			kCollApp.addDataField("oversee_org_status", "01");//监管机构管理状态设为"有效"
			kCollApp.setName("IqpOverseeOrg");
			dao.insert(kCollApp, connection); //插入监管协议信息表
			
			//修改评级表的审批状态
			KeyedCollection KcollCcr = dao.queryAllDetail("CcrAppInfo", serno, connection);
			if(KcollCcr!=null&&KcollCcr.getDataValue("serno")!=null&&!"".equals(KcollCcr.getDataValue("serno"))){
				KcollCcr.setDataValue("approve_status", "997");
				KcollCcr.setDataValue("expiring_date",end_date);
				dao.update(KcollCcr, connection);
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

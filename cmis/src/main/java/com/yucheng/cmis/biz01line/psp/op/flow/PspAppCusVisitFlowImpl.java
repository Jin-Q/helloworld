package com.yucheng.cmis.biz01line.psp.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class PspAppCusVisitFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		
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
		try{
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		String manager_br_id = (String)kc.getDataValue("input_br_id");//客户走访中没有管理机构与责任人，取登记机构与登记人
		String manager_id = (String)kc.getDataValue("input_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
		String IsTeam="";
		 
		Map<String, String> param = new HashMap<String, String>();
	 
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("cus_id", cus_id);
		
		
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
		CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
	    String belgLine = Cus.getBelgLine();
		param.put("bizline", belgLine);
		
		
		
		
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		return param;
		}catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}
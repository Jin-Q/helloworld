package com.yucheng.cmis.biz01line.iqp.iqpinterface.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.dao.SqlClient;

public class IqpChkMarketTaskFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "------------- 盯市任务管理，流程结束处理开始 ---------------", null);
		String serno = "";
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl_app = dao.queryDetail(wfiMsg.getTableName(), serno, connection);
			
			//修改申请表的办结日期
			kColl_app.put("over_date", context.getDataValue("OPENDAY"));
			dao.update(kColl_app, connection);
			
			String pk_id = (String) kColl_app.getDataValue("adj_pk");
			
			KeyedCollection valueAdjkColl4update = dao.queryDetail("IqpMortValueAdj", pk_id, connection);
			valueAdjkColl4update.put("status", "2");//已处理
			valueAdjkColl4update.put("inure_date", context.getDataValue("OPENDAY"));
			int count=dao.update(valueAdjkColl4update, connection);
			if(count!=1){
				EMPLog.log("MESSAGE", EMPLog.INFO, 0, "盯市任务管理审批时根据主键更新价格调整记录，更新到记录数："+count+"，请联系管理员！", null);
			}
			
			String value_no = (String) valueAdjkColl4update.getDataValue("value_no");
			KeyedCollection kColl4val = dao.queryDetail("IqpMortValueMana", value_no, connection);
			kColl4val.put("auth_date", context.getDataValue("OPENDAY"));
			
			//根据调整后价格更新货物单价及总价
			String market_value = (String)valueAdjkColl4update.getDataValue("change_valve");//此次商品核准价格(调整后)
			if(market_value!=null&&!"".equals(market_value)&&Double.valueOf(market_value).compareTo(0.00)>0){
				SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
			}else{
				market_value = (String)valueAdjkColl4update.getDataValue("org_valve");//此次商品核准价格（未调整）
				SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
			}
			kColl4val.put("market_value", market_value);
			dao.update(kColl4val, connection);
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("盯市任务管理流程审批报错，错误描述："+e.getMessage());
		}
		EMPLog.log("MESSAGE", EMPLog.INFO, 0, "------------- 盯市任务管理，流程结束处理结束 ---------------", null);
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行业务处理逻辑

	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// 审批否决时执行业务处理逻辑
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		try {
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			String manager_id = (String)kc.getDataValue("manager_id");
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			KeyedCollection kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			String IsTeam="";
			if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				IsTeam="no";
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("IsTeam", IsTeam);
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}

package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class InitIqpChkMarketTaskAppOp extends CMISOperation {
	
	private final String modelId = "IqpChkMarketTaskApp";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		String approve_status = "000";
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String value_no = context.getDataValue("value_no")+"";  //价格编号
			String adj_pk = context.getDataValue("adj_pk")+"";   //价格调整编号
			
			KeyedCollection conf_kColl = (KeyedCollection)dao.queryFirst(modelId, null, " WHERE VALUE_NO='"+value_no+"'AND ADJ_PK='"+adj_pk+"'", connection);
			//流水号不为空说明已生成申请记录
			if(null!=conf_kColl && null!=conf_kColl.getDataValue("serno") && !"".equals(conf_kColl.getDataValue("serno"))){
				serno = conf_kColl.getDataValue("serno")+"";  //返回流水号，走流程
				approve_status = conf_kColl.getDataValue("approve_status")+"";  //流程审批状态
			}else{  //实时生成申请记录
				KeyedCollection kcoll = new KeyedCollection();
				kcoll.setName("IqpChkMarketTaskApp");
				
				String manager_br_id = context.getDataValue("organNo")+""; 
				serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
				
				kcoll.addDataField("serno", serno);
				kcoll.addDataField("value_no", value_no);
				kcoll.addDataField("adj_pk", adj_pk);
				kcoll.addDataField("flow_type", "01");  //流程类型默认为”正常流程“
				kcoll.addDataField("approve_status", "000");  //流程状态默认为 ”待发起“
				kcoll.addDataField("manager_br_id", context.getDataValue("organNo"));  //管理机构-用于提交流程
				kcoll.addDataField("manager_id", context.getDataValue("currentUserId"));  //责任人-用于提交流程
				dao.insert(kcoll, connection);
			}
			context.put("serno", serno);
			context.put("approve_status", approve_status);
			context.put("msg", "Y");
		} catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException eee) {
				eee.printStackTrace();
			}
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, e.getMessage(), null);
			context.put("serno","");
			context.put("msg", null==e.getCause()?e.getMessage():e.getCause().getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}

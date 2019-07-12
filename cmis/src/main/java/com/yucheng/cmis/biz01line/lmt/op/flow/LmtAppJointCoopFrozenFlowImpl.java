package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.dao.SqlClient;

public class LmtAppJointCoopFrozenFlowImpl extends CMISComponent implements BIZProcessInterface {

	private static final String LMTAPPDETAILS = "LmtAppDetails";//授信分项申请表
	private static final String LMTAGRDETAILS = "LmtAgrDetails";//授信分项台账表
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();

			String serno_value = wfiMsg.getPkValue();
			String table = wfiMsg.getTableName();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(table, serno_value, connection);
			String app_type = (String) kColl.getDataValue("app_type");//申请类型
			String agr_no = (String) kColl.getDataValue("agr_no");//协议编号
			
			String condition = "where serno = '"+serno_value+"'";
			IndexedCollection iColl = dao.queryList(LMTAPPDETAILS, condition, connection);
			
			if(app_type.equals("03")){//冻结
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
					KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
					//将冻结金额置为授信金额的值
					argKcoll.setDataValue("froze_amt", argKcoll.getDataValue("crd_amt"));
					dao.update(argKcoll, connection);
				}
				KeyedCollection kCollAgr = dao.queryDetail("LmtAgrJointCoop", agr_no, connection);
				kCollAgr.put("agr_status", "004");//协议状态置为冻结
				dao.update(kCollAgr, connection);
			}else if(app_type.equals("04")){//解冻
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
					KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
					//解冻将冻结金额置空
					argKcoll.setDataValue("froze_amt", "");
					dao.update(argKcoll, connection);
				}
				KeyedCollection kCollAgr = dao.queryDetail("LmtAgrJointCoop", agr_no, connection);
				kCollAgr.put("agr_status", "002");//协议状态置为生效
				dao.update(kCollAgr, connection);
			}else{
				throw new EMPException("非冻结解冻类型！");
			}
			
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("单一法人授信流程审批报错，错误描述："+e.getMessage());
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
		String cus_id = (String)kc.getDataValue("cus_id");
		Map<String, String> param = new HashMap<String, String>();
		param.put("orgid", manager_br_id);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		/** modified by wangj 2015-9-25 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
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
		param.put("cus_id", cus_id);
		param.put("IsTeam", IsTeam);
		/** modified by wangj 2015-9-25 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		return param;
	}

}

package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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
import com.yucheng.cmis.pub.dao.SqlClient;

public class LmtCoopFrozenFlow extends CMISComponent implements BIZProcessInterface {

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
//			String openDate = context.getDataValue("OPENDAY").toString();
		
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("LmtAppJointCoop", serno_value, connection);
			String app_type = (String)kColl.getDataValue("app_type");//申请类型
			String agr_no = (String)kColl.getDataValue("agr_no");//协议编号
			KeyedCollection kCollAgr = dao.queryDetail("LmtAgrJointCoop", agr_no, connection);
			
			if(app_type.equals("03")){//冻结
				if(kColl.getDataValue("froze_amt")!=null&&!"".equals(kColl.getDataValue("froze_amt"))){
					BigDecimal det_froze_amt = new BigDecimal(kColl.getDataValue("froze_amt")+"");
					String froze_amt_str = (String) kCollAgr.getDataValue("froze_amt");
					BigDecimal froze_amt = new BigDecimal("0");
					if(froze_amt_str!=null&&!froze_amt_str.equals("")){
						froze_amt = new BigDecimal(froze_amt_str);
					}
					froze_amt=froze_amt.add(det_froze_amt);
					kCollAgr.setDataValue("froze_amt", froze_amt);
					dao.update(kCollAgr, connection);
				}
			}else if(app_type.equals("04")){//解冻
				if(kColl.getDataValue("unfroze_amt")!=null&&!"".equals(kColl.getDataValue("unfroze_amt"))){
					BigDecimal det_unfroze_amt = new BigDecimal(kColl.getDataValue("unfroze_amt")+"");
					BigDecimal froze_amt = new BigDecimal(kCollAgr.getDataValue("froze_amt")+"");
					froze_amt=froze_amt.subtract(det_unfroze_amt);
					kCollAgr.setDataValue("froze_amt", froze_amt);
					dao.update(kCollAgr, connection);
				}
			}else{
				throw new EMPException("非冻结解冻类型！");
			}
		}catch(Exception e){
			throw new EMPException("授信流程审批报错，错误描述："+e);
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
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("cus_id", cus_id);
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
		param.put("IsTeam", IsTeam);
		/** modified by wangj 2015-9-25 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/

		return param;
	}

}

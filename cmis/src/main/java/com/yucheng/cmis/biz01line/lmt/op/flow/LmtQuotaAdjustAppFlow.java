package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface;
import com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar.FinGuarUtils;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.pubopera.PubOperaComponent;

public class LmtQuotaAdjustAppFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String serno_value = wfiMsg.getPkValue();
			String conditionStr = "where fin_serno='"+serno_value+"'order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList("LmtQuotaAdjustApp",null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				kColl.put("approve_status", "992");
				dao.update(kColl, connection);
			}
			
		} catch (Exception e) {
			throw new EMPException("融资担保公司用信限额调整申请流程审批报错，错误描述："+e.getMessage());
		}
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String serno_value = wfiMsg.getPkValue();
			String conditionStr = "where fin_serno='"+serno_value+"'order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList("LmtQuotaAdjustApp",null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				kColl.put("approve_status", "993");
				dao.update(kColl, connection);
			}
			
		} catch (Exception e) {
			throw new EMPException("融资担保公司用信限额调整申请流程审批报错，错误描述："+e.getMessage());
		}
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();		
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl_App = dao.queryDetail(wfiMsg.getTableName(), serno_value, connection);
			kColl_App.put("approve_status", "997");
			dao.update(kColl_App, connection);
			
			String conditionStr = "where fin_serno='"+serno_value+"'order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList("LmtQuotaAdjustApp",null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				kColl.put("approve_status", "997");
				kColl.put("status", "1");
				dao.update(kColl, connection);
			}
			if(iColl!=null&&iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
				String fin_agr_no = "";
				if(kColl!=null&&kColl.getDataValue("fin_agr_no")!=null&&!"".equals(kColl.getDataValue("fin_agr_no"))){
					fin_agr_no = (String)kColl.getDataValue("fin_agr_no");
				}
				String condition = " where fin_agr_no = '"+fin_agr_no+"' ";
				PubOperaComponent pubOperaComponent = (PubOperaComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance("PubOpera", context, connection);
				pubOperaComponent.deleteDateByTableAndCondition("LMT_QUOTA_ADJUST", condition);
			}
			for(Object obj:iColl){
				KeyedCollection kColl2 = (KeyedCollection)obj;
				kColl2.put("approve_status", "997");
				kColl2.put("status", "1");
				kColl2.remove("approve_status");
				kColl2.remove("fin_serno");
				kColl2.remove("serno");
				kColl2.setName("LmtQuotaAdjust");
				dao.insert(kColl2, connection);
			}	
			
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("融资担保公司用信限额调整申请流程审批报错，错误描述："+e.getMessage());
		}
	}

	
	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);

			String conditionStr = "where fin_serno='"+serno_value+"'order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList("LmtQuotaAdjustApp",null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				kColl.put("approve_status", "111");
				dao.update(kColl, connection);
			}
			
		}catch(Exception e){
			throw new EMPException("融资担保公司用信限额调整申请流程审批报错，错误描述："+e.getMessage());
		}
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try {
			connection = this.getConnection();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String serno_value = wfiMsg.getPkValue();
			String conditionStr = "where fin_serno='"+serno_value+"'order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList("LmtQuotaAdjustApp",null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				kColl.put("approve_status", "998");
				dao.update(kColl, connection);
			}
			
		} catch (Exception e) {
			throw new EMPException("融资担保公司用信限额调整申请流程审批报错，错误描述："+e.getMessage());
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("input_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("input_id");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}
}

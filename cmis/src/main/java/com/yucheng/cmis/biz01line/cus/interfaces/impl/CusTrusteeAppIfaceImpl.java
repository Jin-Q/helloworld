package com.yucheng.cmis.biz01line.cus.interfaces.impl;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusTrusteeAppComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeApp;
import com.yucheng.cmis.biz01line.cus.interfaces.CusTrusteeAppIface;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class CusTrusteeAppIfaceImpl extends CMISComponent implements BIZProcessInterface{
	
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
			String openDate = context.getDataValue("OPENDAY").toString();		
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl_App = dao.queryDetail(wfiMsg.getTableName(), serno_value, connection);
			kColl_App.put("approve_status", "997");
			dao.update(kColl_App, connection);
			
			KeyedCollection kColl_Info = new KeyedCollection();
			kColl_Info.put("serno", kColl_App.getDataValue("serno"));
			kColl_Info.put("consignor_type", kColl_App.getDataValue("consignor_type"));
			kColl_Info.put("is_provid_accredit", kColl_App.getDataValue("is_provid_accredit"));
			kColl_Info.put("consignor_id", kColl_App.getDataValue("consignor_id"));
			kColl_Info.put("consignor_br_id", kColl_App.getDataValue("consignor_br_id"));
			kColl_Info.put("trustee_id", kColl_App.getDataValue("trustee_id"));
			kColl_Info.put("trustee_br_id", kColl_App.getDataValue("trustee_br_id"));
			kColl_Info.put("trustee_detail", kColl_App.getDataValue("trustee_detail"));
			kColl_Info.put("trustee_date", kColl_App.getDataValue("trustee_date"));
			kColl_Info.put("supervise_date", kColl_App.getDataValue("supervise_date"));
			kColl_Info.put("trustee_status", "1");
			kColl_Info.put("input_id", kColl_App.getDataValue("input_id"));
			kColl_Info.put("input_br_id", kColl_App.getDataValue("input_br_id"));
			kColl_Info.put("input_date", kColl_App.getDataValue("input_date"));
			kColl_Info.setName("CusTrusteeInfo");
			dao.insert(kColl_Info, connection);
			
			KeyedCollection kColl_Detail = new KeyedCollection();
			kColl_Detail.put("consignor_id", kColl_App.getDataValue("consignor_id"));
			kColl_Detail.put("trustee_id", kColl_App.getDataValue("trustee_id"));
			kColl_Detail.put("trustee_date", kColl_App.getDataValue("trustee_date"));
			kColl_Detail.setName("CusTrustee");
			dao.insert(kColl_Detail, connection);			
			
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("托管申请流程审批报错，错误描述："+e.getMessage());
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
		String manager_br_id = (String)kc.getDataValue("input_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("input_id");
		String dutyNoList =(String) (this.getContext().getDataValue("dutyNoList")==null?"":this.getContext().getDataValue("dutyNoList"));
		String isBranch="";

		String condition = " where suporganno = '9350000000' and organno = '"+manager_br_id+"' ";
		IndexedCollection iCollHead = dao.queryList("SOrg", condition, this.getConnection());
		if(iCollHead.size()>0){
			if(dutyNoList!=null&&!"".equals(dutyNoList)&&dutyNoList.contains("S0002")){//总行行长
				isBranch = "1";
			}else if(dutyNoList!=null&&!"".equals(dutyNoList)&&!dutyNoList.contains("S0002")&&dutyNoList.contains("S0004")){//总行中高层管理岗且非总行行长
				isBranch = "2";
			}else{
				isBranch = "3";
			}
		}else{
			isBranch = "4";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("isBranch", isBranch);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}


	
}

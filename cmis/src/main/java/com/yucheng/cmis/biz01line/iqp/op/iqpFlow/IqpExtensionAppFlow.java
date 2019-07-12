package com.yucheng.cmis.biz01line.iqp.op.iqpFlow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class IqpExtensionAppFlow extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		String serno = "";
		
		try {			
			connection = this.getConnection();
			serno = wfiMsg.getPkValue();			
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection iqpKColl = dao.queryDetail("IqpExtensionApp", serno, this.getConnection());
			String cont_no = (String)iqpKColl.getDataValue("cont_no");
			KeyedCollection ctrKColl = dao.queryDetail("CtrLoanCont", cont_no, this.getConnection());
			String prd_id = (String)ctrKColl.getDataValue("prd_id");
			
			String bizType = null;
			
			//贸易融资表内业务展期
			if("500020".equals(prd_id) || "500021".equals(prd_id) || "500022".equals(prd_id) || "500023".equals(prd_id) || 
			   "500024".equals(prd_id) || "500025".equals(prd_id) || "500026".equals(prd_id) || "500027".equals(prd_id) || 
			   "500028".equals(prd_id) || "500029".equals(prd_id) || "500031".equals(prd_id) ){
				bizType = AppConstant.IQPCONTTYPE4GJZQ;
			}
			//其他普通贷款展期
			else{
				bizType = AppConstant.IQPCONTTYPE4ZQ;
			}
			
			KeyedCollection tranc_kColl = new KeyedCollection();//传到dao中的值
			context.put("inputOrg", iqpKColl.getDataValue("input_br_id"));
			String agr_no = CMISSequenceService4Cont.querySequenceFromDB("HT", bizType, bizType, connection, context);
			tranc_kColl.addDataField("serno", serno);
			tranc_kColl.addDataField("agr_no", agr_no);
			
			CatalogManaComponent cmisComponent = (CatalogManaComponent)CMISComponentFactory.
			getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
			cmisComponent.excuteSql("IqpExtensionApp", tranc_kColl);
			
		} catch (Exception e) {
			throw new EMPException("展期申请流程审批报错，错误描述：" + e.getMessage());
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
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
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
		Map<String, String> param = new HashMap<String, String>();
		try {
			param.put("IsTeam", IsTeam);
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
			param.put("cus_id",cus_id);
			
			 
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
		    String belgLine = Cus.getBelgLine();
			param.put("bizline", belgLine);
		} catch (Exception e) {
			throw new EMPException("展期申请流程审批报错，错误描述：" + e.getMessage());
		}
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		return param;		
	}

}

package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class LmtAppFinSubpayFlow extends CMISComponent implements BIZProcessInterface {

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
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String serno = wfiMsg.getPkValue();
			KeyedCollection kCollApp = dao.queryDetail("LmtAppFinSubpay", serno, connection);
			
			//修改申请表中的“申请状态”
			
			kCollApp.setDataValue("approve_status", "997");
				int i = dao.update(kCollApp, connection);
				if(i!=1){
					throw new EMPException("update Failed! Record i: " + i);
				}
			
		}catch(EMPException e){
			throw new EMPException("授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	
	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		Connection connection = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, connection);
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		String isIntbank = "N";
		String IsBranch = "" ;//分行下是否存在支行;Y--存在支行；N--不存在
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		OrganizationServiceInterface serviceUser;
		try {
			serviceUser = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			/**责任人存在多个机构时取责任机构*/
			List<SOrg> orgslist = serviceUser.getDeptOrgByActno(manager_id, this.getConnection());
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg org_info = serviceUser.getOrgByOrgId(manager_br_id, connection);//根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();//获取主管客户经理所在机构的上级机构
			if(suporganno.equals("9350500000")){//（若上级机构不是泉州分行，则需要走分行行长）
				IsBranch ="N";
			}else{
				IsBranch ="Y";
			}
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("isIntbank", isIntbank);
		param.put("IsBranch",IsBranch);
		param.put("cus_id", cus_id);
		return param;
	}
}

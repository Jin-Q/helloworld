package com.yucheng.cmis.biz01line.arp.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class ArpCollDispAppFlow extends CMISComponent implements
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
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			serno = wfiMsg.getPkValue();
			String now = (String) context.getDataValue("OPENDAY");
			KeyedCollection ArpCollDispApp = dao.queryDetail("ArpCollDispApp",serno, this.getConnection());
			ArpCollDispApp.setDataValue("end_date",now);//办结日期。
			String asset_disp_mode = (String) ArpCollDispApp.getDataValue("asset_disp_mode");
			if("02".equals(asset_disp_mode)){//转固（ArpAssetPegInfo）
				KeyedCollection kc = dao.queryFirst("ArpAssetPegInfo",null,"where serno='"+serno+"'", this.getConnection());
				kc.setDataValue("asgn_date",now);
				dao.update(kc, connection);
			}else if("03".equals(asset_disp_mode)){//核销（ArpAssetWriteoffInfo）
				KeyedCollection kc = dao.queryFirst("ArpAssetWriteoffInfo",null,"where serno='"+serno+"'", this.getConnection());
				kc.setDataValue("writeoff_date",now);
				dao.update(kc, connection);
			}
			dao.update(ArpCollDispApp, connection);
			
			/*** 调用命名sql完成流程后处理，肖迪做的以物抵债，不知道和没做有什么区别 ***/
			KeyedCollection tranc_kColl = new KeyedCollection("TransValue");//传到dao中的值
			String asset_disp_no = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			tranc_kColl.addDataField("serno", serno);
			tranc_kColl.addDataField("asset_disp_no", asset_disp_no);
			tranc_kColl.addDataField("asset_disp_mode", asset_disp_mode);
			//流程结束处理类
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			cmisComponent.delExecuteSql("dealCollDispAppToAcc", tranc_kColl);
			
		} catch (Exception e) {
			throw new EMPException("抵债资产处置申请流程审批报错，错误描述：" + e.getMessage());
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
		String IsBranch = "";
		String DebtInAmt;//抵入金额是否高于五百万;Y--是；N--否
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		try {
			String disp_amt = (String) kc.getDataValue("disp_amt");
			double amt = Double.parseDouble(disp_amt);
			if(amt>5000000){
				DebtInAmt="Y";//处置金额高于五百万 
			}else{
				DebtInAmt="N";//处置金额低于五百万
			}
			
			//判断管理机构上级机构是否是泉州银行
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface serviceUser;
			serviceUser = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			/**责任人存在多个机构时取责任机构*/
			List<SOrg> orgslist = serviceUser.getDeptOrgByActno(manager_id, this.getConnection());
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg org_info = serviceUser.getOrgByOrgId(manager_br_id, this.getConnection());//根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();//获取主管客户经理所在机构的上级机构
			if("9350500000".equals(suporganno)){//（若上级机构不是泉州分行，则需要走分行行长）
				IsBranch ="N";
			}else{
				IsBranch ="Y";
			}
			
		} catch (Exception e) {
			throw new EMPException("流程审批初始化参数报错，错误描述："+e.getMessage());
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);	
		param.put("DispAmt", DebtInAmt);
		param.put("IsBranch",IsBranch);
		return param;
	}

}
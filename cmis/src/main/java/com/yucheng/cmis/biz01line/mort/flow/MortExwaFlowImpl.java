package com.yucheng.cmis.biz01line.mort.flow;

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
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.mort.component.MortFlowComponent;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class MortExwaFlowImpl extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {//打回
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {//拿回
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			MortFlowComponent MortFlowComponent = (MortFlowComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("MortFlowComponent", this.getContext(), this.getConnection());
			//审批通过的同时生成授权信息
			MortFlowComponent.doWfAgreeForMort(serno_value);
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection mortstorexwainfokColl = dao.queryDetail("MortStorExwaInfo", serno_value, this.getConnection());
			String stor_exwa_mode = (String)mortstorexwainfokColl.getDataValue("stor_exwa_mode");//出入库方式
			String warrant_state = "";
			if(stor_exwa_mode.equals("04")){//入库
				warrant_state = "2";
			}else if(!stor_exwa_mode.equals("02")){//出库
				warrant_state = "9";
			}else{
				warrant_state = "9";//临时借出
			}
			//更新权证状态为借出
			//押品编号
			String guaranty_no="";
			//权证编号
			String warrant_no="";
			//权证类型
			String warrant_type="";
			KeyedCollection mortstorexwadetailkColl= new KeyedCollection("MortStorExwaDetail");
			/** 通过业务流水号查询出生成质押出入库从表信息**/
			IndexedCollection mortstorexwadetailiColl = dao.queryList("MortStorExwaDetail","where serno ='"+serno_value+"'", this.getConnection());
			if(null==mortstorexwadetailiColl||mortstorexwadetailiColl.size()==0){
				throw new EMPException("根据业务流水号获取出入库权证条数为零！");
			}else{
				for(int i=0;i<mortstorexwadetailiColl.size();i++){
					mortstorexwadetailkColl = (KeyedCollection) mortstorexwadetailiColl.get(i);
					guaranty_no = (String) mortstorexwadetailkColl.getDataValue("guaranty_no");//押品编号
					warrant_no = (String) mortstorexwadetailkColl.getDataValue("warrant_no");//权证编号
					warrant_type = (String) mortstorexwadetailkColl.getDataValue("warrant_type");//权证类型
					mortstorexwadetailkColl.put("warrant_state", warrant_state);//更新状态为借出
					
					HashMap map = new HashMap();
					map.put("warrant_type", warrant_type);
					map.put("warrant_no", warrant_no);
					KeyedCollection mortGuarantyCertiInfokColl = dao.queryDetail("MortGuarantyCertiInfo", map, this.getConnection());
					if(mortGuarantyCertiInfokColl!=null&&mortGuarantyCertiInfokColl.getDataValue("warrant_no")!=null&&!"".equals(mortGuarantyCertiInfokColl.getDataValue("warrant_no"))){
						mortGuarantyCertiInfokColl.put("warrant_state", warrant_state);//更新状态为借出
					}
					dao.update(mortstorexwadetailkColl, this.getConnection());
					dao.update(mortGuarantyCertiInfokColl, this.getConnection());
				}
			}
			//if(!stor_exwa_mode.equals("02")){
				/**调用ESB接口，发送报文*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				serviceRel.tradeDZYWQZCRKYM(serno_value, context, connection);
			//}
		}catch(Exception e){
			throw new EMPException("出库申请流程审批报错，错误描述："+e.getMessage());
		}
		
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {//审批中
	
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {//否决
		Connection connection = this.getConnection();
		String serno_value = wfiMsg.getPkValue();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		/***出库申请否决掉 ，权证信息将状态改为“在库” add by tangzf 2014.04.25********/
		String condition = " where serno='"+serno_value+"'";
		IndexedCollection iCollDet = dao.queryList("MortStorExwaDetail", condition, connection);
		for(int i=0;i<iCollDet.size();i++){
			KeyedCollection kCollParam = new KeyedCollection();
			KeyedCollection kCollDet = (KeyedCollection)iCollDet.get(i);
			kCollParam.put("warrant_no", kCollDet.getDataValue("warrant_no"));
			kCollParam.put("warrant_type",kCollDet.getDataValue("warrant_type"));
			try {
				SqlClient.update("updateMortCertiStatus", kCollParam, "3", null, connection);
			} catch (SQLException e) {
				throw new EMPException("出库申请流程否决报错，错误描述："+e.getMessage());
			}
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,String pkVal, KeyedCollection modifyVar) throws EMPException {
		Map<String, String> param = new HashMap<String, String>();
		Connection connection = null;
		try {
			connection = this.getConnection();
			// 设置业务数据至流程变量之中
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, connection);
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			String manager_id = (String)kc.getDataValue("manager_id");
			String IsBranch ;//分行下是否存在支行;Y--存在支行；N--不存在
			/**责任人存在多个机构时取责任机构*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			List<SOrg> orgslist = userService.getDeptOrgByActno(manager_id, connection);
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg org_info = userService.getOrgByOrgId(manager_br_id, connection);//根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();//获取主管客户经理所在机构的上级机构
			if(suporganno.equals("9350500000")){//（若上级机构不是泉州分行，则需要走分行行长）
				IsBranch ="N";
			}else{
				IsBranch ="Y";
			}
			param.put("manager_br_id",manager_br_id);
			param.put("manager_id", manager_id);
			param.put("IsBranch",IsBranch);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return param;
	}

}

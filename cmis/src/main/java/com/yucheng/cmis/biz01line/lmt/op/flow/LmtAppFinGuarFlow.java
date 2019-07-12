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
import com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar.FinGuarUtils;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class LmtAppFinGuarFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		try{
			String serno = wfiMsg.getPkValue();
			//通过融资担保公司帮助类处理协议数据
			FinGuarUtils.buildFinGuarAgrInfo(context, serno);			
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
		try {
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
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("isIntbank", isIntbank);
		param.put("IsBranch",IsBranch);
		param.put("cus_id",cus_id);
		return param;
	}
}

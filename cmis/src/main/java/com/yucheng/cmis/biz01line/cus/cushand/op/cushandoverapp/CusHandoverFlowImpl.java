package com.yucheng.cmis.biz01line.cus.cushand.op.cushandoverapp;

import java.sql.Connection;
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
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusHandoverAppComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.biz01line.cus.cushand.component.CusHandoverCfgComponent;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;


public class CusHandoverFlowImpl  extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection appKcoll = dao.queryDetail("CusHandoverApp", serno, connection);
			String handoverScope= (String)appKcoll.getDataValue("handover_scope");//移交范围
			String handoverMode =(String) appKcoll.getDataValue("handover_mode");//移交方式
			String handoverBrId = (String) appKcoll.getDataValue("handover_br_id");//移交机构
			String handoverId =(String)appKcoll.getDataValue("handover_id");//移交人
			String receiverBrId = (String) appKcoll.getDataValue("receiver_br_id");//接收机构
			String receiverId = (String) appKcoll.getDataValue("receiver_id");//接收人
//			String orgType = (String) appKcoll.getDataValue("org_type");
			
			IndexedCollection listIcoll = dao.queryList("CusHandoverLst", "where serno = '"+serno+"'", connection);
			
			KeyedCollection kColTmp = null;
			ArrayList<String> cusList=null;
			for(Iterator it = listIcoll.iterator();it.hasNext();){
				kColTmp = (KeyedCollection)it.next();
				if(cusList == null){
					cusList = new ArrayList<String>();
				}
				//获取合并的客户号，封装为list进行操作
				cusList.add((String)kColTmp.getDataValue("cus_id"));
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("handoverScope", handoverScope);
			map.put("handoverMode", handoverMode);
			map.put("receivesorg", receiverBrId);
			map.put("receiveid", receiverId);
			map.put("handoverid", handoverId);
			map.put("handoversorg", handoverBrId);
			map.put("serno", serno);
			map.put("cusidlist", cusList);
            
            CusHandoverCfgComponent cushandovercfgcomponent = (CusHandoverCfgComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERCFG, context,connection);
            cushandovercfgcomponent.middle(map);//获取配置中的sql文件修改
			
			CusHandoverAppComponent cusHandoverAppComponent = (CusHandoverAppComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSHANDOVERAPP,context,connection);
			ComponentHelper cHelper = new ComponentHelper();
	        CusHandoverApp cusHandoverApp = new CusHandoverApp();
	        cHelper.kcolTOdomain(cusHandoverApp, appKcoll);
	        cusHandoverAppComponent.doReceive(cusHandoverApp);//增加日志文件
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！");
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
		String receiver_br_id = (String)kc.getDataValue("receiver_br_id");
		Map<String, String> param = new HashMap<String, String>();
		try {
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			String IsTeam="";
			/**责任人存在多个机构时取责任机构*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			KeyedCollection kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				List<SOrg> orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
				if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
					manager_br_id = orgslist.get(0).getOrganno();
				}
				IsTeam="no";
			}
			param.put("IsTeam", IsTeam);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			SOrg supOrg = userService.getSupOrg(manager_br_id, this.getConnection());//上级机构
			param.put("super_org_id", supOrg.getOrganno());
		} catch (Exception e) {
			throw new EMPException(e);
		}
		param.put("receiver_br_id", receiver_br_id);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}
}

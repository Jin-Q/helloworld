package com.yucheng.cmis.biz01line.cus.op.cuscogniz.cuscoginzflow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberApplyComponent;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CusCognizFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		String tableName = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			tableName = wfiMsg.getTableName();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			if("CusScaleApply".equals(tableName)){	//条线认定申请
				KeyedCollection kCollScale = dao.queryDetail("CusScaleApply", serno, connection);
				String cus_id = (String)kCollScale.getDataValue("cus_id");	//客户码
				String scale_type = (String)kCollScale.getDataValue("scale_type");//01小微条线 02公司条线
				KeyedCollection kCollCusBase = dao.queryDetail("CusBase", cus_id, connection);
				if("01".equals(scale_type)){
					kCollCusBase.setDataValue("belg_line", "BL200");
				}else if("02".equals(scale_type)){
					kCollCusBase.setDataValue("belg_line", "BL100");
				}
				kCollCusBase.setDataValue("cus_crd_grade", ""); //清除原有评级信息
				kCollCusBase.setDataValue("cus_crd_dt", ""); //清除原有评级信息
				dao.update(kCollCusBase, connection);
				//调用授信模块接口
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				service.updateLmtUnUse("1", cus_id, null, null, connection);
			}else if("CusCognizApply".equals(tableName)){	//客户认定
				KeyedCollection kCollCogniz = dao.queryDetail("CusCognizApply", serno, connection);
				String cus_id = (String)kCollCogniz.getDataValue("cus_id");	//客户码
				String scale_type = (String)kCollCogniz.getDataValue("scale_type");//01房地产开发 02政府融资平台
				KeyedCollection kCollCusCom = dao.queryDetail("CusCom", cus_id, connection);
				if("01".equals(scale_type)){
					kCollCusCom.setDataValue("hou_exp", "1");
				}else if("02".equals(scale_type)){
					kCollCusCom.setDataValue("gover_fin_ter", "1");
				}
				dao.update(kCollCusCom, connection);
			}else if("CusOrgApp".equals(tableName)){	//评估机构认定			
				/*** 评估机构认定流程后处理 ***/
				CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("CusBase",context,connection);
				cusComponent.delSubmitRecord("CUS_ORG_APP", serno);
			}else if("CusGrpInfoApply".equals(tableName)){	//集团客户认定
				CusGrpMemberApplyComponent cmisComponent = (CusGrpMemberApplyComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPMEMBERAPPLYCOMPONENT,context,connection);
				KeyedCollection kCollGrpApp = dao.queryDetail("CusGrpInfoApply", serno, connection);
				//集团客户认定变更
				String is_change = ((String) kCollGrpApp.getDataValue("is_change")).trim();
				String grp_no = (String) kCollGrpApp.getDataValue("grp_no");
				String cusIds_bef = "";	//修改前成员名单
				if(is_change.equals("1")){
					//先查询集团客户原有成员，为调用授信接口做准备
					String conditionMem = " where grp_no='"+grp_no+"' ";
					IndexedCollection iCollMem = dao.queryList("CusGrpMember", conditionMem, connection);
					for(int i=0;i<iCollMem.size();i++){
						KeyedCollection kCollMem = (KeyedCollection)iCollMem.get(i);
						cusIds_bef = cusIds_bef + kCollMem.getDataValue("cus_id") + ",";
					}
					if(cusIds_bef!=null&&!"".equals(cusIds_bef)){
						cusIds_bef = cusIds_bef.substring(0, cusIds_bef.length()-1);
					}

					cmisComponent.setGrpToCom("delete", grp_no); //集团变更删除cus_com旧集团信息，在删除集团数据之前处理
					dao.deleteByPk("CusGrpInfo", grp_no, connection); //集团变更删除CusGrpInfo旧记录
					cmisComponent.deteleCusGrpMemberApply(grp_no,"cusGrpChange");//集团变更删除cus_grp_member旧记录
					
				}
				//将申请信息中没用字段移除插入集团信息表
				kCollGrpApp.remove("serno");
				kCollGrpApp.remove("approve_status");
				kCollGrpApp.remove("is_change");
				kCollGrpApp.setName("CusGrpInfo");
				dao.insert(kCollGrpApp, connection);
				String condition = " where serno='"+serno+"'";
				IndexedCollection iCollMemApp = dao.queryList("CusGrpMemberApply", condition, connection);
				String cusIds_aft = "";	//修改后成员名单
				for(int i=0;i<iCollMemApp.size();i++){
					KeyedCollection kCollMemApp = (KeyedCollection)iCollMemApp.get(i);
					kCollMemApp.remove("serno");
					kCollMemApp.setName("CusGrpMember");
					dao.insert(kCollMemApp, connection);
					cusIds_aft = cusIds_aft + kCollMemApp.getDataValue("cus_id") + ",";
				}
				if(cusIds_aft!=null&&!"".equals(cusIds_aft)){
					cusIds_aft = cusIds_aft.substring(0, cusIds_aft.length()-1);
				}
				//调用授信模块接口
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				service.updateLmtUnUse("2", grp_no, cusIds_bef, cusIds_aft, connection);
				
				cmisComponent.setGrpToCom("add", grp_no); //集团认定更新cus_com集团编号，在生成集团数据之后处理
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！"+e);
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
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		Map<String, String> param = new HashMap<String, String>();
		//若为条线认定则将变更类型放入流程变量中
		if("CusScaleApply".equals(tabModelId)){
			String scale_type = (String)kc.getDataValue("scale_type");//01小微条线认定 02公司条线认定
			param.put("scale_type", scale_type);
		}
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
			SOrg supOrg = userService.getSupOrg(manager_br_id, conn);//上级机构
			param.put("super_org_id", supOrg.getOrganno()); 
			String cus_id = "";
			if(kc.containsKey("cus_id")){
				cus_id = (String)kc.getDataValue("cus_id");
			}else if(kc.containsKey("parent_cus_id")){
				cus_id = (String)kc.getDataValue("parent_cus_id");	
			}
			
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
			String belgLine = Cus.getBelgLine();
			param.put("bizline", belgLine);
		} catch (Exception e) {
			throw new EMPException(e);
		}
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		
		return param;
	}

}
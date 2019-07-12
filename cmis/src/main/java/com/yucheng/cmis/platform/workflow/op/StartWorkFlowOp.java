package com.yucheng.cmis.platform.workflow.op;

import java.io.Serializable;
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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;

/**
 * <p>发起流程，进行初始化</p>
 *	处理逻辑：<br>
 *	1.检查是否已经进行实例初始化；<br>
 *	2.如果是第一次发起（未曾初始化），则根据传入的申请类型到流程关联业务配置匹配物理流程<br>
 *	3.如果是已有实例初始化痕迹，需判断是申请人第一次发起取消后再次提交还是审批打回后提交；<br>
 *	4.是申请人第一次发起取消后再次提交则需执行第2点，判断最新配置的物理流程是否与已经实例化的流程相同。如果相同则继续，否决清除原来的实例化信息并重新初始化最新的流程；<br>
 *	5.审批打回后提交的情况，则继续提交审批。<br>
 *	以上处理逻辑满足新发起流程始终与最新的流程关联配置一致；不改变在途的流程流转。
 * @author liuhw 2013-7-4
 */

public class StartWorkFlowOp extends CMISOperation {
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		String currentUserId =(String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);	//当前操作员
//		String organNo = (String)context.getDataValue(CMISConstance.ATTR_ORGID);//机构号
		String sysId = (String)context.getDataValue(WorkFlowConstance.ATTR_SYSID);  //系统ID
		
		if(currentUserId == null || currentUserId.trim().equals("")){
			throw new AsynException("没有得到当前用户号，因此无法确定所要发起的流程！");
		}
//		if(organNo == null || organNo.trim().equals("")){
//			throw new AsynException("没有得到当前登录机构号，因此无法确定所要发起的流程！");
//		}
		if(sysId == null || sysId.trim().equals("")){
			throw new AsynException("没有得到当前系统ID，因此无法发起流程！");
		}
		
		KeyedCollection joinKcoll = (KeyedCollection) context.get("WfiJoin");//业务流程关联数据
		if(joinKcoll == null || joinKcoll.isEmpty()){
			throw new AsynException("流程与业务关联数据表单[WfiJoin]为空，因此无法发起流程！");
		}
		String modelId = (String) joinKcoll.getDataValue("table_name");
		String applType = (String) joinKcoll.getDataValue("appl_type");
		String pkValue = (String)joinKcoll.getDataValue("pk_value");
		
		if(applType==null||applType.trim().equals("")){
			throw new AsynException("流程标识[wfsign]与申请类型[appltype]均为空，因此无法发起流程！");
		}
		
		Connection conn = null;
		String wfSignInit = null;  //最终发起的流程标识
		boolean isInit = false;  //是否已经初始化
		WFIInstanceVO wfiInstanceVO = null;
		String nodeId = null;
		try{
			conn = this.getConnection(context);
			//add by tangzf
			TableModelDAO tDao = this.getTableModelDAO(context);
			
			String managerId = null;
			String managerBrId = null;
			//业务申请，出账，责任人从客户经理绩效表中取主管客户经理(特殊)
			//其他的从主表中取
			if("IqpLoanApp".equals(modelId)||"IqpRpddscnt".equals(modelId)||"IqpAssetstrsf".equals(modelId)||"IqpAssetTransApp".equals(modelId)||"IqpAssetProApp".equals(modelId)){
				//取主管客户经理Id 
				String condition = "where is_main_manager='1' and serno='"+pkValue+"'";
				IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}
			}else if("CtrLimitApp".equals(modelId)){
				IndexedCollection ic =  tDao.queryList("CusManager", "where is_main_manager='1' and serno='"+pkValue+"'", conn);
				if(ic != null && ic.size() > 0){
					KeyedCollection kc = (KeyedCollection)ic.get(0);
					managerId = (String)kc.getDataValue("manager_id");//取得责任人
				}else {
					KeyedCollection kc = tDao.queryDetail("CtrLimitApp", pkValue, conn);
					String cont_no = (String)kc.getDataValue("cont_no");
					KeyedCollection mKc = tDao.queryFirst("CusManager", null, "where is_main_manager='1' and cont_no='"+cont_no+"'", conn);
					managerId = (String)mKc.getDataValue("manager_id");//取得责任人
				}
				
			}else if("PvpLoanApp".equals(modelId)){ 
				//通过出账主键取合同号，再通过合同号取业务申请流水号
				KeyedCollection pvpKColl = tDao.queryDetail(modelId, pkValue, conn);
				String cont_no = (String)pvpKColl.getDataValue("cont_no");
				String prd_id = (String) pvpKColl.getDataValue("prd_id");
				KeyedCollection ctrKColl = null;
				if("300024".equals(prd_id)||"300023".equals(prd_id)||"300022".equals(prd_id)){
					ctrKColl = (KeyedCollection)tDao.queryDetail("CtrRpddscntCont", cont_no, conn);
				}else if("600020".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)tDao.queryDetail("CtrAssetstrsfCont", cont_no, conn);
				}else if("600021".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)tDao.queryDetail("CtrAssetTransCont", cont_no, conn);
				}else if("600022".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)tDao.queryDetail("CtrAssetProCont", cont_no, conn);
				}else{
					ctrKColl = (KeyedCollection)tDao.queryDetail("CtrLoanCont", cont_no, conn);
				}
				String serno = (String)ctrKColl.getDataValue("serno");
				managerBrId = (String)ctrKColl.getDataValue("manager_br_id");
				String condition = "where is_main_manager='1' and serno='"+serno+"'";  
				IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
				managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人 
			}else if("IqpCreditChangeApp".equals(modelId) || "IqpGuarantChangeApp".equals(modelId) || "IqpGuarChangeApp".equals(modelId)){//信用证变更时取原业务的主管客户经理
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = tDao.queryDetail(modelId, pkValue, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}   
			}else if("IqpAverageAssetApp".equals(modelId)){//资产登记
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = tDao.queryDetail(modelId, pkValue, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}else{
					throw new Exception("获取不到主管客户经理！");
				}    
				KeyedCollection kCollCtr = tDao.queryDetail("CtrLoanCont", cont_no, conn);
				managerBrId = (String)kCollCtr.getDataValue("manager_br_id");
			}else if("IqpRateChangeApp".equals(modelId)){	//利率调整 取责任人
				KeyedCollection kCollRateChange = tDao.queryDetail(modelId, pkValue, conn);
				String bill_no = (String)kCollRateChange.getDataValue("bill_no");
				if(bill_no!=null && !"".equals(bill_no)){
					KeyedCollection kCollAcc = tDao.queryDetail("AccLoan", bill_no, conn);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'";
					IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
					if(iqpIColl.size()>0){
						KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
						managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
					} 
				}
			}else if("PspAppCusVisit".equals(modelId)){//客户走访没有责任人责任机构，取登记人登记机构
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				managerId = (String)kCollTmp.getDataValue("input_id");//取登记人
				managerBrId = (String)kCollTmp.getDataValue("input_br_id");
			}else if("ArpBadassetHandoverApp".equals(modelId)){//不良资产移交申请流程，取原主管客户经理
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				managerId = (String)kCollTmp.getDataValue("fount_manager_id");//取原主管客户经理
				managerBrId = (String)kCollTmp.getDataValue("fount_manager_br_id");//取原主管机构
			}else if("CusTrusteeApp".equals(modelId)){//托管申请流程
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				managerId = (String)kCollTmp.getDataValue("input_id");//取得责任人
				managerBrId = (String)kCollTmp.getDataValue("input_br_id");
			}else if("IqpAssetRegiApp".equals(modelId)){//信贷资产登记
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = tDao.queryDetail(modelId, pkValue, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = tDao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}else{
					throw new Exception("获取不到主管客户经理！");
				}   
				KeyedCollection kCollCtr = tDao.queryDetail("CtrLoanCont", cont_no, conn);
				managerBrId = (String)kCollCtr.getDataValue("manager_br_id");
			/**modified by lisj 2015-8-21 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			}else if("PvpBizModifyRel".equals(modelId)){
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				String biz_cate = (String) kCollTmp.getDataValue("biz_cate");
				if("016".equals(biz_cate)){
					KeyedCollection kColl4IEA = tDao.queryFirst("IqpExtensionAgr", null, (String) kCollTmp.getDataValue("cont_no"), conn);
					//KeyedCollection temp = tDao.queryDetail("IqpExtensionApp", (String) kCollTmp.getDataValue("biz_serno"), conn);
					managerId = (String)kColl4IEA.getDataValue("manager_id");
					managerBrId = (String)kColl4IEA.getDataValue("manager_br_id");
				}else{
					KeyedCollection kColl4CLC = tDao.queryDetail("CtrLoanCont", (String) kCollTmp.getDataValue("cont_no"), conn);
					String condition = "where is_main_manager='1' and serno='"+(String) kColl4CLC.getDataValue("serno")+"'";
					KeyedCollection iqpKColl = tDao.queryFirst("CusManager", null, condition, conn);
					managerId = (String)iqpKColl.getDataValue("manager_id");//取得责任人
					managerBrId = (String)kColl4CLC.getDataValue("manager_br_id");
				}
			}else{
			/**modified by lisj 2015-8-21 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				managerId = (String)kCollTmp.getDataValue("manager_id");//取得责任人
				managerBrId = (String)kCollTmp.getDataValue("manager_br_id");//取得责任机构
			}
			//若责任机构没有取则默认从主表取manager_br_id
			if(managerBrId==null||"".equals(managerBrId)){
				KeyedCollection kCollTmp = tDao.queryDetail(modelId, pkValue, conn);
				managerBrId = (String)kCollTmp.getDataValue("manager_br_id");//取得责任人
			}
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			String IsTeam="no";
			String organNo = managerBrId;//责任人所在机构
			/*KeyedCollection kColl4STO = new KeyedCollection();
			try {
				kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", managerId, null, conn);
			} catch (SQLException e) {}
			if(kColl4STO != null && kColl4STO.containsKey("team_no") && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
				organNo = managerBrId;
			}else{
				List<SOrg> orgs = userService.getDeptOrgByActno(managerId, conn);
				if(orgs.size()==0){
					throw new Exception("该笔业务责任人["+managerId+"]所在机构为空，请检查！");
				}else if(orgs.size()==1) {
					organNo = orgs.get(0).getOrganno();
				}else{//责任人存在多个机构则取责任机构
					organNo = managerBrId;
				}
				IsTeam="no";
			}*/
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
					
			WFIComponent wfiComp = (WFIComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, conn);
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			/** 检查当前申请是否已经发起流程 */
			KeyedCollection kCollWfi = wfiComp.queryMainWfiJoinByBiz(modelId, pkValue);
			String instanceIdTmp = null, wfsignTmp = null;
			if(kCollWfi!=null && kCollWfi.getDataValue("instanceid")!=null && !kCollWfi.getDataValue("instanceid").toString().toLowerCase().equals("null")) {
				instanceIdTmp = (String) kCollWfi.getDataValue("instanceid");
				wfsignTmp = (String) kCollWfi.getDataValue("wfsign");
				String status = (String) kCollWfi.getDataValue("wfi_status");
				if(WorkFlowConstance.WFI_STATUS_APPROVE.equals(status) || WorkFlowConstance.WFI_STATUS_DENIAL.equals(status) || WorkFlowConstance.WFI_STATUS_PASS.equals(status)) {
					throw new AsynException("该业务已经在流程审批中，不能重复发起！业务流水号："+pkValue);
				}
			} else {
				//检查历史表是否有记录，如果有，则此申请流水号不能再发起新流程
				KeyedCollection kcollWfiHis = wfiComp.queryMainWfiJoinHisByBiz(modelId, pkValue);
				if(kcollWfiHis!=null && kcollWfiHis.getDataValue("instanceid")!=null && !kcollWfiHis.getDataValue("instanceid").toString().toLowerCase().equals("null")) {
					throw new AsynException("该业务已经发起流程审批，并且审批已经结束。不能重复发起！业务流水号："+pkValue);
				}
			}
			//从目前流程关联业务的配置中获取流程标识
			wfSignInit = wfiComp.getWfSignByConf(applType, organNo);
			if(instanceIdTmp==null || "".equals(instanceIdTmp.trim())) {  //尚未发起
				if(wfSignInit == null) {
					throw new AsynException("根据申请类型["+applType+"]与机构号["+organNo+"]没有定位到具体的流程，发起流程失败！请联系系统管理员。");
				}
				isInit = false;
			} else {
				/** 流程打回或拿回后重新提交时删除流程中原有审批中变更信息 */
				SqlClient.delete("delWfiBizVarByInstanceId", instanceIdTmp, conn);
				wfiInstanceVO = wfi.getInstanceInfo(instanceIdTmp, currentUserId, null, conn);
				String preNodeId = wfiInstanceVO.getPreNodeId();
				//流程配置发生变更，如果没有经过审批，则删除现有实例信息，重新发起新流程
				if(WorkFlowConstance.ATTR_BEGIN_NODEID.equals(preNodeId) && (wfSignInit!=null && !wfSignInit.equals(wfsignTmp))) {
//					wfiComp.deleteWfInstance(instanceIdTmp, currentUserId);
					wfi.wfDelInstance(pkValue, modelId, conn);
					isInit = false;
				} else {
					nodeId = wfiInstanceVO.getNodeId();
					isInit = true;
				}
			}
			if(!isInit) {
				Map<String, Serializable> paramMap = new HashMap<String, Serializable>();
				//将EMPcontext设置到流程表单数据中
				paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, context);
				//设置应用模块标识（重要），将与工作委托关联；暂时不设置名称appName
				paramMap.put("appId", applType);
				String wfJobName = (String) wfi.getWFPropertyByWfSign(wfSignInit, "wfname");
				WFIVO wfivo = wfi.initWFBySign(wfSignInit, currentUserId, null, pkValue, wfJobName, organNo, sysId, paramMap, conn);
				instanceIdTmp = wfivo.getInstanceId();
				nodeId = wfivo.getCurrentNodeId();
				joinKcoll.put("instanceid", instanceIdTmp);
				joinKcoll.put("wfi_status", WorkFlowConstance.WFI_STATUS_INIT);
				joinKcoll.put("wfsign",wfSignInit);
				wfiComp.addWfiJoin(joinKcoll);
			} else {
				TableModelDAO dao = this.getTableModelDAO(context);
				joinKcoll.put("instanceid", instanceIdTmp);
				joinKcoll.put("wfsign",wfSignInit);
				dao.update(joinKcoll, conn);
			}
			//为配合异步的数据整理
			KeyedCollection wfKcoll = new KeyedCollection("wfResult");
			wfKcoll.put("pkValue", pkValue);
			wfKcoll.put("wfState", "1");
			wfKcoll.put("instanceId", instanceIdTmp);
			wfKcoll.put("nodeId", nodeId);
			wfKcoll.put("wfSign", wfSignInit);
			String scene = wfi.getWFExtPropertyByWfSign(wfSignInit, "scene");	//场景
			wfKcoll.put("scene", scene);
			wfiInstanceVO = wfi.getInstanceInfo(instanceIdTmp, currentUserId, nodeId, conn);
			//是否已经处于拟稿状态
			wfKcoll.put("isdraft", wfiInstanceVO.getIsdraft());
			context.addDataElement(wfKcoll);
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new AsynException(ex);
		} finally {
			if(conn != null)
				this.releaseConnection(context, conn);
		}
		
		return null;
	}
	
}

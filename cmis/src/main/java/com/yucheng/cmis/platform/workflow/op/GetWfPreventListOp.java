package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * <p>根据申请类型从流程关联业务配置中获取风险拦截配置</p>
 * 
 * @author liuhw 2013-7-8
 */
public class GetWfPreventListOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String preventList = "";
		String wfSign = "";
		String applType = null;
		Connection connection = null;
		try {
			KeyedCollection wfiKColl = (KeyedCollection)context.getDataElement("WfiJoin");
			applType = (String) wfiKColl.getDataValue("appl_type");
//			applType = (String) context.getDataValue("applType");
			connection = this.getConnection(context);
			
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			String pk_value = (String) wfiKColl.getDataValue("pk_value");
			String table_name = (String)wfiKColl.getDataValue("table_name");
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " WHERE APPL_TYPE='"+applType+"'";
			IndexedCollection iCollFlow2biz = dao.queryList("WfiWorkflow2biz", condition, connection);
			/**add by tangzf 2013.11.26 一种申请类型对应一个流程时直接取；一种申请类型对应多个流程时通过流程标志申请类型与申请类型获取流程风险拦截方案**/
			if(iCollFlow2biz.size()>0&&iCollFlow2biz.size()==1){
				KeyedCollection kcoll = wfiComponent.getWorkFlow2BizByApplType(applType);
				if(kcoll!=null && kcoll.containsKey("prevent_list")) {
					preventList = (String) kcoll.getDataValue("prevent_list");
					wfSign = (String) kcoll.getDataValue("wfsign");
				}
			}else if(iCollFlow2biz.size()>1){
				KeyedCollection kCollApp = dao.queryDetail(table_name, pk_value, connection);
				String managerId = (String)kCollApp.getDataValue("manager_id");//责任人
				String managerBrId = (String)kCollApp.getDataValue("manager_br_id");//责任机构
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				String userBrId = "";
				/**责任人存在多个机构时取责任机构*/
				if(managerId!=null&&!"".equals(managerId)){
					OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
					List<SOrg> orgslist = userService.getDeptOrgByActno(managerId, connection);
					if(orgslist!=null&&orgslist.size()>1){//存在多个机构，则取责任机构下的人员
						userBrId = managerBrId;
					}else {
//						SUser sUser = userService.getUserByUserId(managerId, connection);
//						userBrId = sUser.getOrgid();//责任人所在机构
						userBrId = orgslist.get(0).getOrganno();
					}
				}
				/**获取流程标志，根据流程标志、申请类型获取风险拦截方案*/
				wfSign = wfiComponent.getWfSignByConf(applType, userBrId);
				KeyedCollection kcoll = wfiComponent.getWorkFlow2BizByWfSignApplType(wfSign, applType);
				if(kcoll!=null && kcoll.containsKey("prevent_list")) {
					preventList = (String) kcoll.getDataValue("prevent_list");
				}
			}
			
			//审批中变更特殊处理（单一法人授信（申请、复议）、个人授信（申请、复议）），当流程退回到发起节点重新提交时，删除审批调整信息
			KeyedCollection kCollJoin = wfiComponent.queryMainWfiJoinByBiz(table_name, pk_value);
			if(kCollJoin != null && kCollJoin.containsKey("instanceid")){
				String instanceId = (String)kCollJoin.getDataValue("instanceid");
				if(instanceId!=null&&!"".equals(instanceId)&&("003".equals(applType)||"3281".equals(applType)||"0061".equals(applType)||"0062".equals(applType))){
					SqlClient.delete("delWfiBizVarByInstanceId", instanceId, connection);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			this.releaseConnection(context, connection);
		}
		context.put("preventList", preventList);
		context.put("wfSign", wfSign);
		return null;
	}

}

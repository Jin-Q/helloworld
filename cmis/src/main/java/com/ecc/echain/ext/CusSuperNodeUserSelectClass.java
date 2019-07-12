package com.ecc.echain.ext;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ecc.echain.workflow.engine.EVO;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CusSuperNodeUserSelectClass implements NodeUserListExtIF{
	/**
	   * 获取节点办理人列表，即流程发起机构所属法人机构下所有用户
	   * @param evo,evo中可以获取wfid、wfsign、wfname、instanceid、currentuserid、nodeid、nodename、connection等
	   * @return List，list中存放用户IDs
	   */
	
	 public List getNodeUserList(EVO evo) throws Exception{
		 
		 	List userList = new ArrayList();
		 	Connection conn = evo.getConnection();
			//接口调用
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			WorkflowServiceInterface service = (WorkflowServiceInterface)serviceJndi.getModualServiceById("workflowService", "workflow");
			WFIInstanceVO wfiVo = service.getInstanceInfo(evo.getInstanceID(), evo.getCurrentUserID(), null, conn);
			Map param = wfiVo.getFormData();
			String managerBrId = (String)param.get("manager_br_id");//责任机构
			String managerId = (String)param.get("manager_id");//责任人
			
//			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
//			SUser sUser = userService.getUserByUserId(managerId, conn);
//			String userBrId = sUser.getOrgid();//责任人所在机构
//			SOrg supOrg = userService.getSupOrg(userBrId, conn);
//			if(supOrg!=null&&supOrg.getOrganno()!=null&&!"".equals(supOrg.getOrganno())){
//				List<SUser> userListTmp = userService.getUsersByOrgId(supOrg.getOrganno(), conn);
//				for(int i=0;i<userListTmp.size();i++){
//					SUser user = userListTmp.get(i);
//					userList.add(user.getActorno());
//				}
//			}
			if(managerId!=null&&!"".equals(managerId)){
				String userBrId = "";
				OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
				List<SOrg> orgslist = userService.getDeptOrgByActno(managerId, conn);
				if(orgslist!=null&&orgslist.size()>1){//存在多个机构，则取责任机构下的人员
					userBrId = managerBrId;
				}else {
					userBrId = orgslist.get(0).getOrganno();
				}
				/** add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
				/**
				 * 校验当前责任人是否属于营销团队，如果是营销团队，那么审批人所在机构为业务责任机构
				 */
				KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", managerId, null, conn);
				if(kColl!=null&&kColl.getDataValue("team_no")!=null&&!"".equals(kColl.getDataValue("team_no"))){
					userBrId = managerBrId;
				}
				/** add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造 end**/
				SOrg supOrg = userService.getSupOrg(userBrId, conn);
				if(supOrg!=null&&supOrg.getOrganno()!=null&&!"".equals(supOrg.getOrganno())){
					List<SUser> userListTmp = userService.getUsersByOrgId(supOrg.getOrganno(), conn);
					for(int i=0;i<userListTmp.size();i++){
						SUser user = userListTmp.get(i);
						userList.add(user.getActorno());
					}
				}
			}
		 return userList;
	 }
}
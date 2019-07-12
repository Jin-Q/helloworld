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

public class CusNodeForCusHandoverClass  implements NodeUserListExtIF{

	@Override
	public List getNodeUserList(EVO evo) throws Exception {
		List userList = new ArrayList();
	 	Connection conn = evo.getConnection();
		//接口调用
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		WorkflowServiceInterface service = (WorkflowServiceInterface)serviceJndi.getModualServiceById("workflowService", "workflow");
		WFIInstanceVO wfiVo = service.getInstanceInfo(evo.getInstanceID(), evo.getCurrentUserID(), null, conn);
		Map param = wfiVo.getFormData();
		String receiverBrId = (String)param.get("receiver_br_id");//责任机构

		if(receiverBrId!=null&&!"".equals(receiverBrId)){//接受部门为普通的客户经理
			
			if("9902".equals("receiverBrId")){
				OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
				List<SUser> userlist = userService.getUsersByOrgIdAndDutyId(receiverBrId, "D0103", conn); 
				 if(userlist!=null&&userlist.size()>0){
					 for(int i=0;i<userlist.size();i++){
						SUser user = userlist.get(i);
						userList.add(user.getActorno());
					}
				 }
			}else if("9903".equals("receiverBrId")){
				OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
				List<SUser> userlist = userService.getUsersByOrgIdAndDutyId(receiverBrId, "D0205", conn); 
				 if(userlist!=null&&userlist.size()>0){
					 for(int i=0;i<userlist.size();i++){
						SUser user = userlist.get(i);
						userList.add(user.getActorno());
					}
				 }
			}else{
				OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
				List<SUser> userlist = userService.getUsersByOrgIdAndDutyId(receiverBrId, "S0404", conn); 
				 if(userlist!=null&&userlist.size()>0){
					 for(int i=0;i<userlist.size();i++){
						SUser user = userlist.get(i);
						userList.add(user.getActorno());
					}
				 }
			}
			
		}
	 return userList;
 }

}

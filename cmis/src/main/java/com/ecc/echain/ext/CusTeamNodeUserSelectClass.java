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
/**
 * 
 *@author yangzy
 *@time 2015-9-18
 *@description TODO 需求:XD150918069,丰泽鲤城区域团队业务流程改造
 *@version v1.0
 * <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
 *
 */
public class CusTeamNodeUserSelectClass implements NodeUserListExtIF{
	/**
	   * 获取团队节点办理人列表，即流程发起客户经理所属团队下所有用户
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

			if(managerId!=null&&!"".equals(managerId)){
				//判断是否存在团队
				KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", managerId, null, conn);
				String team_no = "";
				if(kColl!=null&&kColl.getDataValue("team_no")!=null&&!"".equals(kColl.getDataValue("team_no"))){
					team_no = (String) kColl.getDataValue("team_no");
				}
				//存在则获取团队长
				String mem_no = "";
				KeyedCollection kColl4T = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByTeamNo", team_no, null, conn);
				if(kColl4T!=null&&kColl4T.getDataValue("mem_no")!=null&&!"".equals(kColl4T.getDataValue("mem_no"))){
					mem_no = (String) kColl4T.getDataValue("mem_no");
					userList.add(mem_no);
				}
			}
		 return userList;
	 }
}
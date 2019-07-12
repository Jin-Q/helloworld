package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>根据节点获取下一办理人信息</p>
 * @author liuhw
 */
public class GetNodeUserListOp0711 extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String instanceId, nodeId, currentUserId;
		try {
			connection = this.getConnection(context);
			instanceId = (String) context.getDataValue("instanceId");
			nodeId = (String) context.getDataValue("nodeId");
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			
			String nodetransacttype = (String) wfi.getWFNodeProperty(nodeId, WorkFlowConstance.NODE_PROPERTY_TRANSACTTYPE);
			String nodeuserscompute = (String) wfi.getWFNodeProperty(nodeId, WorkFlowConstance.NODE_PROPERTY_USERSCOMPUTE);
			String multeitFlag = "n";
//			multeitFlag = NodeUsersAssign.equals("2")?"n":NodeTransactType.equals("0")||NodeTransactType.equals("1")?"1":"n"
			if("1".equals(nodetransacttype) || "0".equals(nodetransacttype)) {  //目前不考虑“办理人员指定”，默认实现全部人员列表选择
				multeitFlag = "1";
			}
			
			StringBuffer useridStrBuf = new StringBuffer("");
			StringBuffer usernameStrBuf = new StringBuffer("");
			
			if(!"3".equals(nodeuserscompute)) {  //办理人员原始配置，则后台不继续执行操作
				
				WFIComponent wfiComp =  (WFIComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
				KeyedCollection wfiJoin = wfiComp.queryWfiJoin(instanceId);
				wfiJoin.setName("WfiJoin");
				context.addDataElement(wfiJoin);
				
				List<WFIUserVO> userList = wfi.getNodeUserList(instanceId, currentUserId, nodeId, connection);
				
//				String ifselectuser = "1";
				
				//判断是否使用外置会签策略
				boolean  isUserSign=false;//是否调用独立会签模块
				String signId="0";//会签策略ID
				try{
				    signId=wfi.getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
				    isUserSign=signId!=null&&!"0".equals(signId)&&!"".equals(signId);
				}catch(Exception e){
				    e.printStackTrace();
				}
				if(isUserSign){
				    userList.clear();
//				    ifselectuser="1";
				    useridStrBuf.append("U.signUser");
				    usernameStrBuf.append("会签成员");
				    multeitFlag = "1";
				} else if(userList!=null && userList.size()>0 && userList.size()<=5) {
					for(int i=0; i<userList.size(); i++) {
						WFIUserVO wfUser = userList.get(i);
						String userid = wfUser.getUserId();
//						if(("U."+currentUserId).equals(userid))  //过滤当前办理本人
//							continue;
						useridStrBuf.append(userid).append(";");
						String username = wfUser.getUserName();
						usernameStrBuf.append(username).append(";");
					}
				} else if(userList.size() > 5) {  //人员列表大于5个，不直接显示
					nodeuserscompute = "3";  //原始配置
				}
			}
			
			KeyedCollection kColl = new KeyedCollection(WorkFlowConstance.WF_NEXT_USER_LIST);
			kColl.addDataField("nodeuserscompute", nodeuserscompute);
			kColl.addDataField("multeitFlag", "1".equals(multeitFlag)? "1" : "n"); 
			kColl.addDataField("userid", useridStrBuf.toString());
			kColl.addDataField("username", usernameStrBuf.toString());
			
			context.addDataElement(kColl);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return "0";
	}

}

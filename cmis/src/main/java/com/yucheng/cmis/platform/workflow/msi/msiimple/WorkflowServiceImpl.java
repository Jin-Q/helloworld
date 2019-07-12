package com.yucheng.cmis.platform.workflow.msi.msiimple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.ext.TaskPool;
import com.ecc.echain.log.WfLog;
import com.ecc.echain.org.model.RoleModel;
import com.ecc.echain.util.WfPropertyManager;
import com.ecc.echain.workflow.cache.OUCache;
import com.ecc.echain.workflow.cache.WFCache;
import com.ecc.echain.workflow.engine.EVO;
import com.ecc.echain.workflow.engine.OrgClass;
import com.ecc.echain.workflow.engine.WorkFlowClient;
import com.ecc.echain.workflow.model.CommentVO;
import com.ecc.echain.workflow.model.GatherActionVO;
import com.ecc.echain.workflow.model.GatherVO;
import com.ecc.echain.workflow.model.VO_wf_node_property;
import com.ecc.echain.workflow.model.VO_wf_whole_property;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFICommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIFormActionVO;
import com.yucheng.cmis.platform.workflow.domain.WFIFormFieldVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.domain.WfiBizVarRecordVO;
import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ResourceUtils;

/**
 * 流程引擎接入接口默认实现
 * @author liuhw 2013-6-17
 *
 */
public class WorkflowServiceImpl extends CMISModualService implements WorkflowServiceInterface {

	/**
	 * echain流程引擎客户端
	 */
	private WorkFlowClient wfc = WorkFlowClient.getInstance();
	
	public WFIVO initWFByID(String wfid, String currentuserid,
			String instanceId, String bizseqno, String wfjobname, String orgid,
			String sysid, Map paramMap, Connection connection)
			throws WFIException {
		
		WFIVO wfivo = new WFIVO() ;
		EVO evo = new EVO();
		evo.setWFID(wfid);
		evo.setCurrentUserID(currentuserid);
		evo.setInstanceID(instanceId);
		evo.setBizseqno(bizseqno);
		evo.setJobName(wfjobname);
		evo.setSysid(sysid);
		evo.setOrgid(orgid);
		evo.setConnection(connection);
		if(paramMap!=null && paramMap.size()>0) {
			String appId = (String) paramMap.get("appId");  //应用模块标识，信贷对应申请类型applType
			String appName = (String) paramMap.get("appName");
			if(appId!=null)
				evo.setAppID(appId);
			if(appName!=null)
				evo.setAppName(appName);
			
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.initializeWFWholeDocUNID(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "按流程ID启动流程失败[initWFByID]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	
	public WFIVO initWFBySign(String wfsign, String currentuserid,
			String instanceid, String bizseqno, String wfjobname, String orgid,
			String sysid, Map paramMap, Connection connection)
			throws WFIException {
		
		WFIVO wfivo = new WFIVO() ; 
		EVO evo = new EVO();
		evo.setWFSign(wfsign);
		evo.setCurrentUserID(currentuserid);
		evo.setSysid(sysid);
		if(orgid!=null && orgid.length()>0)
			evo.setOrgid(orgid);
		if(instanceid!=null && instanceid.length()>0)
			evo.setInstanceID(instanceid);
		evo.setBizseqno(bizseqno);
		if(wfjobname!=null && wfjobname.length()>0)
			evo.setJobName(wfjobname);
		if(paramMap != null) {
			String appId = (String) paramMap.get("appID");  //应用模块标识，信贷对应申请类型applType
			String appName = (String) paramMap.get("appName");
			if(appId!=null)
				evo.setAppID(appId);
			if(appName!=null)
				evo.setAppName(appName);
			
			evo.paramMap.putAll(paramMap);
		}
		if(paramMap!=null && paramMap.get("formid")!=null)
			evo.setFormid((String)paramMap.get("formid"));
		if(connection!=null)
			evo.setConnection(connection);
		try {
			evo = wfc.initializeWFWholeDocUNID(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "按流程标识启动流程失败[initWFBySign]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo); 
		return wfivo;
	}

	public WFIVO wfSaveJob(String instanceid, String currentuserid, String orgId, 
			Map paramMap, Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceid);
		evo.setCurrentUserID(currentuserid);
		evo.setOrgid(orgId);
		if(paramMap != null)
			evo.paramMap.putAll(paramMap);
		if(paramMap!=null &&! paramMap.isEmpty()){
			if(paramMap.get("appsign")!=null)
				evo.setAppSign((String)paramMap.get("appsign"));
		}
		if(connection!=null)
			evo.setConnection(connection);
		try {
			evo=wfc.wfSaveJob(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程保存失败[wfSaveJob]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfCompleteJob(String instanceid, String nodeId,
			String currentUserId, String nextNodeId, String nextNodeUser, String nextAnnouceUser,String entrustModel,
			Map paramMap, String orgId, Connection connection)
			throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceid);
		if(nodeId!=null && nodeId.length()>0)
			evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentUserId);
		if(nextNodeId!=null && nextNodeId.length()>0)
			evo.setNextNodeID(nextNodeId);
		//人员可能存在多值，用"@"分割，每个单值内部的多人用";"隔开,带格式，如：U.admin
		//格式有T.XXX项目池、U.XXX人员、G.XXX群组、R.XXX角色、D.XXX部门、O.XXX机构、A.XXX关系、X.XXX应用扩展、"${alluser}"所有用户
		StringBuffer nextNodeUserBuf = new StringBuffer();
		if(WorkFlowConstance.WFI_SYS_NODEUSER.equals(nextNodeUser)) { //如果是系统指定用户,则置空,让引擎自动计算
			nextNodeUser = null;
		} else {
			String[] usersByNode = nextNodeUser.split("@");
			for(String str : usersByNode) {
				if(str!=null && !str.equals("")) {
					String[] users = str.split(";");
					for(String s : users) {
						if(s!=null && !s.equals("")) {
							if(!s.startsWith("T.")&&!s.startsWith("U.")&&!s.startsWith("G.")&&
									!s.startsWith("R.")&&!s.startsWith("D.")&&!s.startsWith("O.")&&
									!s.startsWith("A.")&&!s.startsWith("X.")) { //没有标注是是任何类型，则默认处理为人员
								s = "U."+s;
							}
							nextNodeUserBuf.append(s).append(";");
						}
					}
					nextNodeUserBuf.append("@");
				}
				
			}
		}
		String nextNodeUserTo = null;
		if(nextNodeUserBuf.length() > 0) {
			nextNodeUserTo = nextNodeUserBuf.substring(0, nextNodeUserBuf.length()-1);
		}
		if(nextNodeUserTo!=null && nextNodeUserTo.length()>0)
			evo.setNextNodeUser(nextNodeUserTo);
		if(nextAnnouceUser!=null && nextAnnouceUser.length()>0) {
			evo.setAnnounceUser(nextAnnouceUser);
		}
		if(entrustModel!=null && entrustModel.length()>0) {
			evo.setEntrustModel(entrustModel);
		}
		if(orgId!=null && orgId.length()>0)
			evo.setOrgid(orgId);
		if(paramMap != null) {
			evo.paramMap.putAll(paramMap);
		}
		if(paramMap!=null && !paramMap.isEmpty()){
			if(paramMap.get("AnnounceUser")!=null)
				evo.setAnnounceUser((String)paramMap.get("AnnounceUser"));
		}
		if(connection!=null)
			evo.setConnection(connection);
		try {
			evo=wfc.wfCompleteJob(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程提交失败[wfCompleteJob]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfUrge(String instanceId, String currentuserid, Map paramMap,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		if(paramMap!=null && !paramMap.isEmpty())
			evo.paramMap.putAll(paramMap);
		evo.setConnection(connection);
		try {
			evo = wfc.wfUrge(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程催办失败[wfUrge]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
		
	}

	public WFIVO instanceSignIn(String instanceId, String currentuserid, Map paramMap, 
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		if(connection!=null) {
			evo.setConnection(connection);
		}
		if(paramMap!=null && paramMap.size()>0) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo=wfc.instanceSignIn(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程实例签收失败[instanceSignIn]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO instanceSignOff(String instanceId, String currentuserid, Map paramMap, 
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		if(connection!=null) {
			evo.setConnection(connection);
		}
		if(paramMap!=null && paramMap.size()>0) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo=wfc.instanceSignOff(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程实例撤销签收失败[instanceSignOff]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public List<WFIUserVO> getChangeUser(String instanceId, String nodeId, String currentUserId,
			Connection connection) throws WFIException {
		
		List<WFIUserVO> users = new ArrayList<WFIUserVO>();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentUserId);
		evo.setConnection(connection);
		try {
			evo = wfc.getChangeUser(evo);
			if(evo.isExb()) { //所有用户
				WFIUserVO allUser = new WFIUserVO();
				allUser.setUserId(WorkFlowConstance.ALL_USER);
				users.add(allUser);
			} else {
				Map userMap = evo.paramMap;
				if(userMap!=null && userMap.size()>0) {
					Iterator iterator = userMap.keySet().iterator();
					while(iterator.hasNext()) {
						String userId = (String) iterator.next();
						if(userId.startsWith("U.")) {
							String userIdTmp = userId.substring(2);
							if(userIdTmp.equals(currentUserId)) {  //排除自身  add by liuhw@20131022
								continue;
							}
							WFIUserVO user = new WFIUserVO();
							String userName = (String) userMap.get(userId);
							user.setUserId(userId);
							user.setUserName(userName);
							users.add(user);
						}
					}
				}
			}
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取流程转办人员列表失败[getChangeUser]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return users;
	}

	public WFIVO wfChange(String instanceId, String nodeId,
			String currentuserid, String nextNodeUser, Map paramMap,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentuserid);
		evo.setNextNodeUser(nextNodeUser);
		if(paramMap!=null && !paramMap.isEmpty())
			evo.paramMap.putAll(paramMap);
		evo.setConnection(connection);
		try {
			evo = wfc.wfChange(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程转办失败[wfChange]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public List<WFINodeVO> getWFNodeList(String instanceId, String wfid,
			Connection connection) throws WFIException {

		List<WFINodeVO> wfiNodes = new ArrayList<WFINodeVO>();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setWFID(wfid);
		evo.setConnection(connection);
		try {
			Map nodeMap = wfc.getWFNodeList(evo);
			if(nodeMap.size() > 0) {
				Iterator iterator = nodeMap.keySet().iterator();
				while(iterator.hasNext()) {
					WFINodeVO wfiNode = new WFINodeVO();
					String nodeId = (String) iterator.next();
					String nodeName = (String) nodeMap.get(nodeId);
					//根据办理人员设置，判断是否项目池节点
					String taskPoolNode = ((String) getWFNodeProperty(nodeId, "NodeUsersList")).indexOf("T.")==-1?"":"T";
					//暂时设置在节点类型字段上
					wfiNode.setNodeType(taskPoolNode);
					wfiNode.setNodeId(nodeId);
					wfiNode.setNodeName(nodeName);
					String nodeTransactType = (String) getWFNodeProperty(nodeId, "NodeTransactType");
					wfiNode.setNodeTransactType(nodeTransactType);
					wfiNodes.add(wfiNode);
				}
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取流程节点列表失败[getWFNodeList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return wfiNodes;
	}

	public List<WFINodeVO> getWFTreatedNodeList(String instanceId, String nodeId, String currentUserId,
			String orgId, Connection connection) throws WFIException {
		
		List<WFINodeVO> wfiNodes = new ArrayList<WFINodeVO>();
		
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setOrgid(orgId);
		evo.setConnection(connection);
		try {
			List al = wfc.getWFTreatedNodeList(evo);
			/**
			 * 通过审批意见，来排除拿回、追回的操作及人员 modify by liuhw@20131104
			 */
			List<WFICommentVO> comments = getAllComments(instanceId, currentUserId, false, connection);
			Map<String, String> commentsMap = new HashMap<String, String>();
			for(WFICommentVO c : comments) {
				String commentSign = c.getCommentSign();
				if(commentSign!=null && (commentSign.equals(WorkFlowConstance.WFI_RESULT_AGAIN)||
						commentSign.equals(WorkFlowConstance.WFI_RESULT_AGAIN_FIRST)||
						commentSign.equals(WorkFlowConstance.WFI_RESULT_HANG)||
						commentSign.equals(WorkFlowConstance.WFI_RESULT_WAKE)||
						commentSign.equals(WorkFlowConstance.WFI_RESULT_URGE))) {
					continue; //拿回、追回、挂起、唤醒、催办等操作和办理人过滤掉
				}
				String nodeIdTmp = c.getNodeId();
				String userId = c.getUserId();
				if(commentsMap.containsKey(nodeIdTmp)) {
					String userIdHas = commentsMap.get(nodeIdTmp);
					userIdHas = userIdHas + ";" + userId;
					commentsMap.put(nodeIdTmp, userIdHas);					
				} else {
					commentsMap.put(nodeIdTmp, userId);
				}
			}
			
			String wfId = nodeId.split("_")[0];
			String firstNodeId = (String) getWFPropertyByWfId(wfId, "WFFirstNodeDocID");
			String wfIdTmp = null;
			if(al.size() > 0) {
				for(Object obj : al) {
					WFINodeVO wfiNode = new WFINodeVO();
					Map nodeMap = (Map) obj;
					String nodeIdTmp = (String) nodeMap.get("nodeid");
					//不包含在意见map中，且不是第一个节点，则排除 modify by liuhw@20131104
					if(!commentsMap.containsKey(nodeIdTmp) && !nodeIdTmp.equals(firstNodeId)) {
						continue;
					}
					
					String nodeType = (String) getWFNodeProperty(nodeIdTmp, "NodeType");
					//排除非人工节点
					if(!"A".equals(nodeType)) {
						continue;
					}
					wfIdTmp = nodeIdTmp.split("_")[0];
					//存在子流程的情况中打回，只能打回给子流程或主流程自身中的节点；根据流程ID进行排除
					//排除当前节点
					//if(!wfId.equals(wfIdTmp) || nodeIdTmp.equals(nodeId)) {
					if(!wfId.equals(wfIdTmp)) { //泉州银行要求自循环节点可以打回当前节点，故不排除
						continue;
					}
					//如果是会签节点则排除（打回重新会签，应该是打回到会签秘书节点，再发起会签）
					String signConfig = getWFNodeExtProperty(nodeIdTmp, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
					if(!"0".equals(signConfig)) {
						continue;
					}
					String nodeNameTmp = (String) nodeMap.get("nodename");
					wfiNode.setNodeId(nodeIdTmp);
					wfiNode.setNodeName(nodeNameTmp);
					
					String userIdsTmp = (String) nodeMap.get("userid");
					String userNamesTmp = (String) nodeMap.get("username");
					String[] userIdArr = userIdsTmp.split(";");
					String[] userNameArr = userNamesTmp.split(";");
					List<WFIUserVO> wfiUsers = new ArrayList<WFIUserVO>();
					String userIdComment = commentsMap.get(nodeIdTmp);  //过滤掉拿回、追回、挂起、唤醒、催办的办理人 modify by liuhw@20131104
					for(int i=0; i<userIdArr.length; i++) {
						String userIdTmp = userIdArr[i];
						if(!userIdComment.contains(userIdTmp))  //过滤掉拿回、追回、挂起、唤醒、催办的办理人 modify by liuhw@20131104
							continue;
						String userNameTmp = userNameArr[i];
						WFIUserVO wfiUser = new WFIUserVO();
						wfiUser.setUserId(userIdTmp);
						wfiUser.setUserName(userNameTmp);
						wfiUsers.add(wfiUser);
					}
					wfiNode.setUserList(wfiUsers);
					String nodeTransactType = (String) getWFNodeProperty(nodeIdTmp, "NodeTransactType");
					wfiNode.setNodeType(nodeType);
					wfiNode.setNodeTransactType(nodeTransactType);
					wfiNodes.add(wfiNode);
				}
			}
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取流程已办理节点列表失败[getWFTreatedNodeList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		
		return wfiNodes;
	}

	public WFIVO wfJump(String instanceId, String nodeId, String currentuserid,
			String nextNodeId, String nextNodeUser, Map paramMap,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentuserid);
		evo.setNextNodeID(nextNodeId);
		evo.setNextNodeUser(nextNodeUser);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		evo.setConnection(connection);
		try {
			evo = wfc.wfJump(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程跳转失败[wfJump]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfEnd(String instanceId, String nodeID, String currentuserid,
			Map paramMap, String SPStatus, Connection connection)
			throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeID);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		evo.setSPStatus(SPStatus);
		if(paramMap!=null && !paramMap.isEmpty())
			evo.paramMap.putAll(paramMap);
		try {
			evo = wfc.wfEnd(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程结束失败[wfEnd]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfReturnBack(String instanceId, String currentUserId,
			String isDraft, Map paramMap, Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentUserId);
		evo.setIsDraft(isDraft);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfReturnBack(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程退回失败[wfReturnBack]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfCallBack(String instanceId, String nodeId,
			String currentUserId, String nextNodeId, String nextNodeUser,
			String callBackModel, Map paramMap, Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentUserId);
		evo.setNextNodeID(nextNodeId);
		evo.setNextNodeUser(nextNodeUser);
		evo.setCallBackModel(callBackModel);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfCallBack(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程打回失败[wfCallBack]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
		
		
	}

	public WFIVO wfTakeBack(String instanceId, String currentuserid,
			Map paramMap, Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfTakeBack(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程拿回失败[wfTakeBack]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}
	
	public WFIVO wfTakeBackFirst(String instanceId, String nodeId, String currentUserId, Map paramMap, Connection connection) throws WFIException {
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentUserId);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfJump2First(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程追回失败[wfTakeBackFirst]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfCancel(String instanceId, String currentuserid,
			Map paramMap, Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfCancel(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程撤办失败[wfCancel]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfHang(String instanceId, String currentuserid, Map paramMap,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfHang(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程挂起失败[wfHang]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfWake(String instanceId, String currentuserid, Map paramMap,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfWake(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行流程唤醒失败[wfWake]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfWithdrawUser(String instanceId, String nodeId,
			String currentuserid, String userId, Map paramMap,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentuserid);
		evo.setCurrentUserID(userId);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfWithdrawUser(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行撤销办理人失败[wfWithdrawUser]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO wfDelInstance(String instanceId, String currentuserid, Map paramMap, 
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		try {
			if(connection == null)
				connection = DbControl.getInstance().getConnection();
			//1 查询是否有子流程信息（如果有则先删除子流程实例信息）
			IndexedCollection subIcoll = new IndexedCollection();
			String sqlIdSub = "getSubInstanceidByInstanceId";
			try {
				Collection col = SqlClient.queryList(sqlIdSub, instanceId, connection);
				Iterator colItr = col.iterator();
				while(colItr.hasNext()) {
					KeyedCollection kcoll = (KeyedCollection) colItr.next();
					subIcoll.add(kcoll);
				}
			} catch (Exception e) {
				WfLog.log(WfLog.ERROR, "根据主流程实例号["+instanceId+"]查询子流程中间表列表信息出错。异常信息："+e.getMessage(), e);
				throw new WFIException(e);
			}
			if(subIcoll.size() > 0) {  //存在子流程信息，则先删除子流程相关信息
				for(Object obj : subIcoll) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					String subInstanceId = (String) kcoll.getDataValue("instanceid");
					if(!subInstanceId.equals(instanceId))  //发起同步子流程mainInstanceId与instanceId相同，应该过滤，否则死循环
						wfDelInstance(subInstanceId, currentuserid, paramMap, connection);
				}
			}
			IndexedCollection subHisIcoll = new IndexedCollection();
			String sqlIdSubHis = "getSubInstanceidHisByInstanceId";
			try {
				Collection col = SqlClient.queryList(sqlIdSubHis, instanceId, connection);
				Iterator colItr = col.iterator();
				while(colItr.hasNext()) {
					KeyedCollection kcoll = (KeyedCollection) colItr.next();
					subHisIcoll.add(kcoll);
				}
			} catch (Exception e) {
				WfLog.log(WfLog.ERROR, "根据主流程实例号["+instanceId+"]查询子流程中间表历史列表信息出错。异常信息："+e.getMessage(), e);
				throw new WFIException(e);
			}
			if(subHisIcoll.size() > 0) {  //存在子流程信息，则先删除子流程相关信息
				for(Object obj : subHisIcoll) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					String subInstanceId = (String) kcoll.getDataValue("instanceid");
					if(!subInstanceId.equals(instanceId))
						wfDelInstance(subInstanceId, currentuserid, paramMap, connection);
				}
			}
			
			//2 删除接入数据
			String sqlId1 = "delWfiBizVarByInstanceId";
			String sqlId1_ = "delWfiBizVarHisByInstanceId";
			String sqlId2 = "delWfiMsgByInstanceId";
			String sqlId2_ = "delWfiMsgHisByInstanceId";
			String sqlId3 = "delWfiJoinByInstanceId";
			String sqlId3_ = "delWfiJoinHisByInstanceId";
			SqlClient.delete(sqlId1, instanceId, connection);
			SqlClient.delete(sqlId1_, instanceId, connection);
			SqlClient.delete(sqlId2, instanceId, connection);
			SqlClient.delete(sqlId2_, instanceId, connection);
			SqlClient.delete(sqlId3, instanceId, connection);
			SqlClient.delete(sqlId3_, instanceId, connection);
			/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start */
			String sqlId4 = "delWfiBizCommentRecordByInstanceId";
			SqlClient.delete(sqlId4, instanceId, connection);
			/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end */
			//3 删除流程实例
			EVO evo = new EVO();
			evo.setInstanceID(instanceId);
			evo.setCurrentUserID(currentuserid);
			if(paramMap != null)
				evo.paramMap.putAll(paramMap);
			evo.setConnection(connection);
			try {
				evo = wfc.wfDelInstance(evo);
			} catch (Exception e) {
				WfLog.log(WfLog.ERROR, "执行删除流程实例失败[wfDelInstance]。异常信息为："+e.getMessage(), e);
				e.printStackTrace();
				throw new WFIException(e);
			}
			wfivo = evo2Wfivo(evo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return wfivo;
	}
	
	public WFIVO wfDelInstance(String pkVal, String modelId, Connection connection) throws WFIException {
		WFIVO wfivo = new WFIVO();
		try {
			if(connection == null)
				connection = DbControl.getInstance().getConnection();
			String sqlId = "getMainWfiJoinByBiz";
			KeyedCollection WJInfo = null;
			try {
				KeyedCollection condi = new KeyedCollection();
				condi.addDataField("table_name", modelId);
				condi.addDataField("pk_value", pkVal);
				WJInfo = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, connection);
			} catch (SQLException e) {
				WfLog.log(WfLog.ERROR, "根据业务表模型ID["+modelId+"]、业务流水号（主键值）["+pkVal+"]查询信贷流程中间表信息出错。异常信息："+e.getMessage(), e);
				throw new WFIException(e);
			}
			if(WJInfo!=null&&WJInfo.size()>0){
				String instanceId = (String) WJInfo.getDataValue("instanceid");
				if(instanceId!=null) {
					Map paramMap = new HashMap();
					paramMap.put("delBiz", "true"); //重要，标识由删除业务时调用
					//added by yangzy 20150818 实例删除日志 start
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"DelWorkFlowOp-PART3",null);
					//added by yangzy 20150818 实例删除日志 end
					wfivo = wfDelInstance(instanceId, null, paramMap, connection);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return wfivo;
	}

	/*public WFIVO wfReject(String instanceId, String currentuserid,
			Map paramMap, Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		if(paramMap!=null && !paramMap.isEmpty()) {
			evo.paramMap.putAll(paramMap);
		}
		try {
			evo = wfc.wfReject(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行撤销办理人失败[wfWithdrawUser]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}*/

	public WFIVO wfAssist(String instanceId, String nodeId,
			String currentUserId, String nextNodeUser, Connection connection)
			throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentUserId);
		evo.setNextNodeUser(nextNodeUser);
		evo.setConnection(connection);
		try {
			evo = wfc.wfAssist(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "执行协助办理失败[wfAssist]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
		
	}

	public List<WFINodeVO> getNextNodeList(String instanceId,
			String currentuserid, String nodeId, Connection connection)
			throws WFIException {

		List<WFINodeVO> wfiNodes = new ArrayList<WFINodeVO>();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentUserID(currentuserid);
		evo.setConnection(connection);
		try {
			evo = wfc.getNextNodeList(evo);
			Map paramMap = evo.paramMap;
			if(!paramMap.isEmpty()) {
				Iterator iterator = paramMap.keySet().iterator();
				while(iterator.hasNext()) {
					String nodeIdTmp = (String) iterator.next();
					Map nodeMap = (Map) paramMap.get(nodeIdTmp);
					String nodeNameTmp = (String) nodeMap.get("nodename");
					String nodeTypeTmp = (String) nodeMap.get("nodetype");
					String routeNameTmp = (String) nodeMap.get("routename");
					int routeSeqTmp = (Integer) (nodeMap.get("routeseq")==null?1:nodeMap.get("routeseq"));
					String ifselectuserTmp = (String) nodeMap.get("ifselectuser");
					String nodetransacttypeTmp = (String) nodeMap.get("nodetransacttype");
					WFINodeVO wfiNode = new WFINodeVO();
					wfiNode.setNodeId(nodeIdTmp);
					wfiNode.setNodeName(nodeNameTmp);
					wfiNode.setNodeType(nodeTypeTmp);
					wfiNode.setRouteName(routeNameTmp);
					wfiNode.setRouteseq(routeSeqTmp);
					wfiNode.setIfselectuser(ifselectuserTmp);
					wfiNode.setNodeTransactType(nodetransacttypeTmp);
					wfiNodes.add(wfiNode);
				}
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取下一节点列表失败[getNextNodeList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		
		return wfiNodes;
	}

	public List<WFIUserVO> getNodeUserList(String instanceId,
			String currentuserid, String nodeId, Connection connection)
			throws WFIException {

		List<WFIUserVO> wfiUsers = new ArrayList<WFIUserVO>();
		String signConfig = getWFNodeExtProperty(nodeId, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
		if(!"0".equals(signConfig)) { //会签节点
			WFIUserVO signUser = new WFIUserVO();
			signUser.setUserId(WorkFlowConstance.SIGN_USER);
			signUser.setUserName("会签成员");
			signUser.setOrgId(WorkFlowConstance.SIGN_USER);
			wfiUsers.add(signUser);
			return wfiUsers;
		}
		//办理人员指定设置为:2.系统指定 或者人员计算模式设置为:3.原始配置,则前台页面不选择具体办理人员
		String nodeUsersAssign = (String) getWFNodeProperty(nodeId, "NodeUsersAssign"); //0.人员列表选择;1.上一办理人指定;2.系统指定
		String nodeUsersCompute = getWFNodeExtProperty(nodeId, "NodeUsersCompute");  //0.并集;1.交集;2.组间交集;3.原始配置
		if("2".equals(nodeUsersAssign) || "3".equals(nodeUsersCompute)) {
			WFIUserVO sysUser = new WFIUserVO();
			sysUser.setUserId(WorkFlowConstance.WFI_SYS_NODEUSER);
			sysUser.setUserName("系统指定");
			wfiUsers.add(sysUser);
			return wfiUsers;
		}
		
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setNodeID(nodeId);
		evo.setConnection(connection);
		try {
			evo = wfc.getNodeUserList(evo);
			String multeitFlag= evo.getMulteitFlag();
			if(evo.isExb()) { //表示办理人员范围为所有用户
				WFIUserVO wfiUser = new WFIUserVO();
				wfiUser.setUserId(WorkFlowConstance.ALL_USER);
				wfiUser.setUserName("所有用户");
				wfiUser.setUserIsmu(multeitFlag==null||multeitFlag.equals("1")?"false":"true");
				wfiUsers.add(wfiUser);
			} else {
				Map paramMap = evo.paramMap;
				if(!paramMap.isEmpty()) {
					Iterator iterator = paramMap.keySet().iterator();
					while(iterator.hasNext()) {
						String useridTmp = (String) iterator.next();
						/**
						 * useridTmp的格式有T.XXX项目池、U.XXX人员、G.XXX群组、R.XXX角色、D.XXX部门、O.XXX机构、A.XXX关系、X.XXX应用扩展、"${alluser}"所有用户
						 * 处理方式：
						 * 1.只需处理T.XXX项目池、U.XXX人员两种结果；
						 * 2.如果发现有T.XXX表示选择的节点为项目池，则也忽略U.XXX人员的结果。
						 */
						if(useridTmp.startsWith("T.")) {
							WFIUserVO wfiUser = new WFIUserVO();
							wfiUser.setUserId(useridTmp);
							wfiUser.setUserName("任务池");
							wfiUser.setUserIsmu("false");
							wfiUser.setChineseFull("");
							wfiUser.setChineseHead("");
							wfiUser.setOrgId("");
							wfiUser.setOrgName("");
							wfiUsers.add(wfiUser);
							break;
						} else if(useridTmp.startsWith("U.")){
							String userNameTmp = (String) paramMap.get(useridTmp);
							String uid = useridTmp.substring(useridTmp.indexOf("U.")+2);
							WFIUserVO wfiUser = new WFIUserVO();
							wfiUser.setUserId(uid);
							wfiUser.setUserName(userNameTmp);
							wfiUser.setUserIsmu(multeitFlag==null||multeitFlag.equals("1")?"false":"true");
							String[] pinyin = WorkFlowUtil.toChinese(userNameTmp);
							wfiUser.setChineseFull(pinyin[0]);
							wfiUser.setChineseHead(pinyin[1]);
							//String orgId = OrgFactory.getInstance().getOrgClass().getOrgIdByUser(uid, connection);
							//String orgName = OrgClass.getInstance().getNameByID(orgId, "O."+orgId, connection);
							//wfiUser.setOrgId(orgId);
							//wfiUser.setOrgName(orgName);
							wfiUsers.add(wfiUser);
						}
					}
				}
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取节点办理人员列表失败[getNodeUserList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		
		return wfiUsers;
	}

	public List<WFIUserVO> getNodeAnnounceUserList(String instanceId,
			String currentuserid, String nodeId, Connection connection)
			throws WFIException {

		List<WFIUserVO> wfiUsers = new ArrayList<WFIUserVO>();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setNodeID(nodeId);
		evo.setConnection(connection);
		try {
			evo = wfc.getAnnounceUserList(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取节点抄送人员列表失败[getNodeAnnounceUserList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		if(evo.isExb()) {
			WFIUserVO wfiUser = new WFIUserVO();
			wfiUser.setUserId(WorkFlowConstance.ALL_USER);
			wfiUsers.add(wfiUser);
		} else {
			Map paramMap = evo.paramMap;
			Iterator iterator = paramMap.keySet().iterator();
			while(iterator.hasNext()) {
				String userIdTmp = (String) iterator.next();
				String userNameTmp = (String) paramMap.get(userIdTmp);
				WFIUserVO wfiUser = new WFIUserVO();
				wfiUser.setUserId(userIdTmp);
				wfiUser.setUserName(userNameTmp);
				wfiUsers.add(wfiUser);
			}
		}
		return wfiUsers;
	}

	/*public boolean saveWFComment(WFICommentVO wfCommentVO, Connection connection)
			throws WFIException {
		
		boolean result = false;
		EVO evo = new EVO();
		CommentVO commentVO = wfiComment2EchainComment(wfCommentVO);
		evo.setCommentVO(commentVO);
		evo.setConnection(connection);
		try {
			result = wfc.setComment(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "保存流程意见失败[saveWFComment]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return result;
	}*/

	public List<WFICommentVO> getAllComments(String instanceId,
			String currentuserid, boolean notAll, Connection connection) throws WFIException {
		
		List<WFICommentVO> wfiCommentVOs = new ArrayList<WFICommentVO>();
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO cvo = new CommentVO();
		cvo.setInstanceID(instanceId);
		cvo.setUserID(currentuserid);
		cvo.setCommentType("0");  //设置取流程意见，意见类别,0：流程意见 1：业务意见 2：内部意见
		evo.setCommentVO(cvo);
		evo.setExb(notAll);
		try {
			Vector vecResult = wfc.getAllComments(evo);
			for(int i=0; i<vecResult.size(); i++) {
				CommentVO cvoTmp = (CommentVO) vecResult.elementAt(i);
				WFICommentVO wfiCommentVO = echainComment2WfiComment(cvoTmp);
				wfiCommentVOs.add(wfiCommentVO);
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取当前实例所有的意见列表失败[getAllComments]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return wfiCommentVOs;
	}

	public List<WFICommentVO> getWFComments(String instanceId,
			String currentuserid, String nodeId, Connection connection)
			throws WFIException {
		//此接口暂不实现，可能需废除
		
		/*List<WFICommentVO> wfiCommentVOs = new ArrayList<WFICommentVO>();
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO cvo = new CommentVO();
		cvo.setInstanceID(instanceId);
		cvo.setUserID(currentuserid);
		evo.setCommentVO(cvo);
		evo.setExb(true);
		try {
			Vector vecResult = wfc.getAllComments(evo);
			for(int i=0; i<vecResult.size(); i++) {
				CommentVO cvoTmp = (CommentVO) vecResult.elementAt(i);
				WFICommentVO wfiCommentVO = echainComment2WfiComment(cvoTmp);
				wfiCommentVOs.add(wfiCommentVO);
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取当前权限内的意见列表失败[getWFComments]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return wfiCommentVOs;*/
		
		return null;
	}

	public WFIInstanceVO getInstanceInfo(String instanceId,
			String currentuserid, String nodeId, Connection connection)
			throws WFIException {

		WFIInstanceVO wfiInstanceVO = new WFIInstanceVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentuserid);
		evo.setNodeID(nodeId);
		evo.setConnection(connection);
		try {
			evo = wfc.getInstanceInfo(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取流程实例信息失败[getInstanceInfo]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfiInstanceVO.setAppId(evo.getAppID());
		wfiInstanceVO.setAppName(evo.getAppName());
		wfiInstanceVO.setAppSign(evo.getAppSign());
		wfiInstanceVO.setAuthor(evo.getAuthor());
		Map paramMap = evo.paramMap;
		if(paramMap.containsKey("getAllComments")) {
			Vector vecResult = (Vector) paramMap.get("getAllComments");
			List<WFICommentVO> wfiCommentVOs = new ArrayList<WFICommentVO>();
			for(int i=0; i<vecResult.size(); i++) {
				CommentVO cvoTmp = (CommentVO) vecResult.elementAt(i);
				WFICommentVO wfiCommentVO = echainComment2WfiComment(cvoTmp);
				wfiCommentVOs.add(wfiCommentVO);
			}
			wfiInstanceVO.setCommentList(wfiCommentVOs);
		}
		wfiInstanceVO.setCurrentNodeUser(evo.getCurrentNodeUser());
		wfiInstanceVO.setCurrentUserId(evo.getCurrentUserID());
		if(paramMap.containsKey("getNodeControlFormField")) {
			Map formField = (Map) paramMap.get("getNodeControlFormField");
			Iterator iterator = formField.keySet().iterator();
			List<WFIFormFieldVO> fieldList = new ArrayList<WFIFormFieldVO>();
			while(iterator.hasNext()) {
				String fieldcode = (String) iterator.next();
				Map fieldMap = (Map) formField.get(fieldcode);
				WFIFormFieldVO formFieldVO = new WFIFormFieldVO();
				formFieldVO.setEditcontrol((String) fieldMap.get("editcontrol"));
				formFieldVO.setFieldCode(fieldcode);
				formFieldVO.setFieldId((String) fieldMap.get("fieldid"));
				formFieldVO.setFieldName((String) fieldMap.get("fieldname"));
//				formFieldVO.setFormId();
				fieldList.add(formFieldVO);
			}
			wfiInstanceVO.setFieldList(fieldList);
		}
		if(paramMap.containsKey("getNodeControlFormAction")) {
			Map actionMap = (Map) paramMap.get("getNodeControlFormAction");
			WFIFormActionVO actionVO = new WFIFormActionVO();
			String track = (String) actionMap.get("track");
			actionVO.setTrack("1".equals(track)?true:false);
			String viewcomment = (String) actionMap.get("viewcomment");
			actionVO.setViewcomment("1".equals(viewcomment)?true:false);
			String cancel = (String) actionMap.get("cancel");
			actionVO.setCancel("1".equals(cancel)?true:false);
			String hang = (String) actionMap.get("hang");
			actionVO.setHang("1".equals(hang)?true:false);
			String wake = (String) actionMap.get("wake");
			actionVO.setWake("1".equals(wake)?true:false);
			String setwf = (String) actionMap.get("setwf");
			actionVO.setSetwf("1".equals(setwf)?true:false);
			String signin = (String) actionMap.get("signin");
			actionVO.setSignin("1".equals(signin)?true:false);
			String signoff = (String) actionMap.get("signoff");
			actionVO.setSignoff("1".equals(signoff)?true:false);
			String tasksignoff = (String) actionMap.get("tasksignoff");
			actionVO.setTasksignoff("1".equals(tasksignoff)?true:false);
			String jump = (String) actionMap.get("jump");
			actionVO.setJump("1".equals(jump)?true:false);
			String save = (String) actionMap.get("save");
			actionVO.setSave("1".equals(save)?true:false);
			String submit = (String) actionMap.get("submit");
			actionVO.setSubmit("1".equals(submit)?true:false);
			String setcomment = (String) actionMap.get("setcomment");
			actionVO.setSetcomment("1".equals(setcomment)?true:false);
			String change = (String) actionMap.get("change");
			actionVO.setChange("1".equals(change)?true:false);
			String returnback = (String) actionMap.get("returnback");
			actionVO.setReturnback("1".equals(returnback)?true:false);
			String announce = (String) actionMap.get("announce");
			actionVO.setAnnounce("1".equals(announce)?true:false);
			String callsubflow = (String) actionMap.get("callsubflow");
			actionVO.setCallsubflow(callsubflow==null||"".equals(callsubflow)?null:callsubflow);
			String callback = (String) actionMap.get("callback");
			//控制不能连续打回操作（连续打回操作目前引擎执行可能会产生混乱） 
			//连续打回操作泉州需要连续退回和打回做的修改，如果不需要连续，可以控制2013年12月27日 cyg
			/*try {
				String sqlId = "getWfCommentSign";
				String commentSign = (String) SqlClient.queryFirst(sqlId, instanceId, null, connection);
				if(WorkFlowConstance.WFI_RESULT_CALLBACK.equals(commentSign)) {
					callback = "2";
				}
			} catch (SQLException e) {
				WfLog.log(WfLog.ERROR, "获取流程实例最近的意见标识出错。异常信息为："+e.getMessage(), e);
				e.printStackTrace();
				throw new WFIException(e);
			}*/
			actionVO.setCallback("1".equals(callback)?true:false);
			String assist = (String) actionMap.get("assist");
			actionVO.setAssist("1".equals(assist)?true:false);
			String gather = (String) actionMap.get("gather");
			actionVO.setGather("1".equals(gather)?true:false);
			String nodevirend = (String) actionMap.get("nodevirend");
			actionVO.setNodevirend("1".equals(nodevirend)?true:false);
			String withdraw = (String) actionMap.get("withdraw");
			actionVO.setWithdraw("1".equals(withdraw)?true:false);
			String author = evo.getAuthor();
			//判断author是否为null，办结后取出的evo中为null add by liuhw@20131121
			if(author!=null && author.equals(currentuserid)) {
				actionVO.setAgainFirst(true);
			}
			//允许同时用户追回与拿回操作权限
//			} else {
//				String again = (String) actionMap.get("again");
//				actionVO.setAgain("1".equals(again)?true:false);
//			}
				String again = (String) actionMap.get("again");
				actionVO.setAgain("1".equals(again)?true:false);
			
			String urge = (String) actionMap.get("urge");
			actionVO.setUrge("1".equals(urge)?true:false);
			//根据是否有流向结束节点的路由来控制【否决】权限
			actionVO.setReject(false);
			String nodeIdTmp = evo.getNodeID();
			if(nodeIdTmp != null) {
				//modify by liuhw@20140110 否决权限根据扩展属性控制
				/*VO_wf_node_property nodeProperty = WFCache.getInstance().getNodeProperty(nodeIdTmp);
				Map hmNodeRouteProperty = nodeProperty.hmNodeRouteProperty;
				Iterator iterator = hmNodeRouteProperty.keySet().iterator();
				while(iterator.hasNext()) {
					String routeIdTmp = (String) iterator.next();
					VO_wf_node_route_property routeProp = (VO_wf_node_route_property) hmNodeRouteProperty.get(routeIdTmp);
					String routeNodeId = routeProp.NodeRouterNodeID;
					if(routeNodeId.indexOf("e") != -1) {
						actionVO.setReject(true);
						break;
					}
				}*/
				String canReject = this.getWFNodeExtProperty(nodeIdTmp, "canexe20");
				if("1".equals(canReject)) { //允许可执行否决
					actionVO.setReject(true);
				}
			}
			//0：正常办理；1：催办；2：办理结束；3：待签收；4：拿回；5：退回；6：挂起；7：打回；8：审批协助；9：取消办结；10：虚似办结
			String nodeStatus = evo.getNodeStatus();
			if("8".equals(nodeStatus)) {  //协助办理只允许提交给协办办理发起人
				actionVO.setAgain(false);
				actionVO.setAssist(false);
				actionVO.setCallback(false);
				actionVO.setCallsubflow(null);
				actionVO.setCancel(false);
				actionVO.setChange(false);
				actionVO.setGather(false);
				actionVO.setHang(false);
				actionVO.setJump(false);
				actionVO.setNodevirend(false);
				actionVO.setReject(false);
				actionVO.setReturnback(false);
				actionVO.setWithdraw(false);
			}
			wfiInstanceVO.setFormActionVO(actionVO);
		}
		if(paramMap.containsKey("getNodeFormData")) {
			Map formData = (Map) paramMap.get("getNodeFormData");
			wfiInstanceVO.setFormData(formData);
		}
		wfiInstanceVO.setFormFlow(evo.getFormflow());
		wfiInstanceVO.setFormId(evo.getFormid());
		wfiInstanceVO.setInstanceId(evo.getInstanceID());
		wfiInstanceVO.setMainInstanceId(evo.getMainInstanceID());
		wfiInstanceVO.setIsdraft(evo.getIsDraft());
		wfiInstanceVO.setIsProcessed(evo.getIsProcessed());
		wfiInstanceVO.setNodeId(evo.getNodeID());
		wfiInstanceVO.setNodeName(evo.getNodeName());
		wfiInstanceVO.setMainNodeId(evo.getMainNodeID());
		wfiInstanceVO.setNodePlanEndTime(evo.getNodePlanEndTime());
		wfiInstanceVO.setNodeStartTime(evo.getNodeStartTime());
		wfiInstanceVO.setNodeStatus(evo.getNodeStatus());
		wfiInstanceVO.setPreNodeId(evo.getPreNodeID());
		wfiInstanceVO.setPreNodeName(evo.getPreNodeName());
		wfiInstanceVO.setPreUser(evo.getPreUser());
		wfiInstanceVO.setSpStatus(evo.getSPStatus());
		wfiInstanceVO.setWfId(evo.getWFID());
		wfiInstanceVO.setWfSign(evo.getWFSign());
		wfiInstanceVO.setWfJobName(evo.getJobName());
		wfiInstanceVO.setWfName(evo.getWFName());
		wfiInstanceVO.setWfStatus(evo.getWFStatus());
		//获取当前节点用户的暂存意见，用于回显
		List<WFICommentVO> commentVO = getAllComments(instanceId, currentuserid, true, connection);
		wfiInstanceVO.setCommentList(commentVO);
		/**
		 * add by liuhw@20140126 会签结束后回到流程审批需要控制流程流转权限（通过提交或不通过否决）
		 */
		if(commentVO!=null && commentVO.size()>0) {
			WFICommentVO cvo = commentVO.get(0);
			String commentResult = cvo.getCommentSign();
			String nodeIdTmp = evo.getNodeID();
			if(nodeIdTmp != null) { //不是流程结束
				String signConfig = this.getWFNodeExtProperty(nodeIdTmp, WorkFlowConstance.NODE_EXT_PROPERTY_SIGNCONFIG);
				if(signConfig!=null && !"0".equals(signConfig)) { //扩展会签节点
					wfiInstanceVO.getFormActionVO().setAgain(false);
					wfiInstanceVO.getFormActionVO().setAgainFirst(false);
					wfiInstanceVO.getFormActionVO().setAssist(false);
					wfiInstanceVO.getFormActionVO().setCallback(false);
					wfiInstanceVO.getFormActionVO().setCancel(false);
					wfiInstanceVO.getFormActionVO().setChange(false);
					wfiInstanceVO.getFormActionVO().setJump(false);
					wfiInstanceVO.getFormActionVO().setReturnback(false);
					wfiInstanceVO.getFormActionVO().setWithdraw(false);
					wfiInstanceVO.getFormActionVO().setSubmit(false);
					wfiInstanceVO.getFormActionVO().setReject(false);
					if(WorkFlowConstance.WFI_RESULT_AGREE.equals(commentResult)) {
						wfiInstanceVO.getFormActionVO().setSubmit(true);
					} else {
						wfiInstanceVO.getFormActionVO().setReject(true);
					}
				}
			}
		}
		
		return wfiInstanceVO;
	}

	public List<WFIUserVO> getInstanceNodeUserList(String instanceId,
			String nodeId, String orgid, Connection connection)
			throws WFIException {

		List<WFIUserVO> wfiUsers = new ArrayList<WFIUserVO>();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setOrgid(orgid);
		evo.setConnection(connection);
		try {
			evo = wfc.getInstanceNodeUserList(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取实例当前节点办理人列表失败[getInstanceNodeUserList]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		Map userMap = evo.paramMap;
		Iterator iterator = userMap.keySet().iterator();
		while(iterator.hasNext()) {
			String userId = (String) iterator.next();
			String userName = (String) userMap.get(userId);
			WFIUserVO wfiUser = new WFIUserVO();
			wfiUser.setUserId(userId);
			wfiUser.setUserName(userName);
			wfiUsers.add(wfiUser);
		}
		return wfiUsers;
	}

	public boolean saveWFComment(String commentId,String instanceId, String nodeId,
			String currentuserid, String commentSign, String commentContent,String orgid, Connection connection)
			throws WFIException {

		boolean result = false;
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO commentVO = new CommentVO();
		commentVO.setCommentID(commentId);
		commentVO.setInstanceID(instanceId);
		commentVO.setCommentSign(commentSign);
		commentVO.setCommentContent(commentContent);
		commentVO.setOrgid(orgid);
		commentVO.setCommentType("0"); //意见类别,0：流程意见 1：业务意见 2：内部意见
		commentVO.setNodeID(nodeId);
		String nodeName = (String) getWFNodeProperty(nodeId, "NodeName");
		commentVO.setNodeName(nodeName);
		commentVO.setUserID(currentuserid);
		evo.setCommentVO(commentVO);
		try {
			result = wfc.setComment(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "保存流程意见失败[setComment]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return result;
	}
	
	public boolean saveWFComment4CusTrustee(String commentId,String instanceId, String nodeId,
			String currentuserid, String currentusername, String commentSign, String commentContent,String orgid, Connection connection)
			throws WFIException {

		boolean result = false;
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO commentVO = new CommentVO();
		commentVO.setCommentID(commentId);
		commentVO.setInstanceID(instanceId);
		commentVO.setCommentSign(commentSign);
		commentVO.setCommentContent(commentContent);
		commentVO.setOrgid(orgid);
		commentVO.setCommentType("0"); //意见类别,0：流程意见 1：业务意见 2：内部意见
		commentVO.setNodeID(nodeId);
		String nodeName = (String) getWFNodeProperty(nodeId, "NodeName");
		commentVO.setNodeName(nodeName);
		commentVO.setUserID(currentuserid);
		commentVO.setUserName(currentusername);
		evo.setCommentVO(commentVO);
		try {
			result = wfc.setComment(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "保存流程意见失败[setComment]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return result;
	}
	
	public Vector getUserTodoWorkList(String currentuserid, String wfsign,
			String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getUserDoneWorkList(String currentuserid, String wfsign,
			String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getWFStatusEndWorkList(String currentuserid, String wfsign,
			String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getUserReturnBackWorkList(String currentuserid,
			String wfsign, String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getUserCallBackWorkList(String currentuserid, String wfsign,
			String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getExceptionWorkList(String currentuserid, String wfsign,
			String nodeId, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getUserSignInWorkList(String currentuserid, String wfsign,
			String NodeID, Map paramMap, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<WFIVO> getWFNameList(String currentuserid, String orgid, String sysid,
			Connection connection) throws WFIException {
		List<WFIVO> list = new ArrayList<WFIVO>();
		
		EVO evo = new EVO();
		evo.setCurrentUserID(currentuserid);
		evo.setOrgid(orgid);
		evo.setSysid(sysid);
		evo.setConnection(connection);
		Vector wfList = wfc.getWFNameList(evo);
		for(int i=0; i<wfList.size(); i++) {
			EVO evoTmp = (EVO) wfList.elementAt(i);
			WFIVO wfivo = new WFIVO();
			wfivo.setWfId(evoTmp.getWFID());
			wfivo.setWfName(evoTmp.getWFName());
			wfivo.setWfSign(evoTmp.getWFSign());
			wfivo.setWfVer(evoTmp.getTip());
			wfivo.setWfAppId(evoTmp.getAppID());
			wfivo.setWfAppName(evoTmp.getAppName());
			wfivo.setWfmainformid(evoTmp.getWFMainForm());
			wfivo.setWfAdmin(evoTmp.getWFAdmin());
			wfivo.setWfReaders(evoTmp.getWFReaders());
			wfivo.setAuthor(evoTmp.getAuthor());
			list.add(wfivo);
		}
		return list;
	}

	public List getWorkFlowHistory(String instanceId, String currentuserid, String nodeId, String orgId, 
			Connection connection) throws WFIException {
		EVO vo=new EVO();
    	vo.setOrgid(orgId);
    	vo.setInstanceID(instanceId);
    	vo.setNodeID(nodeId);
    	vo.setConnection(connection);
    	List list = new ArrayList();
    	
    	//把流程所对应的流程意见关联出来，合并到流程跟踪中，泉州需要做的修改
    	List<WFICommentVO> wfiCommentVOs = new ArrayList<WFICommentVO>();
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO cvo = new CommentVO();
		cvo.setInstanceID(instanceId);
		cvo.setUserID(currentuserid);
		cvo.setCommentType("0");  //设置取流程意见，意见类别,0：流程意见 1：业务意见 2：内部意见
		evo.setCommentVO(cvo);
		Vector vecResult ;
 		try {
			  vecResult = wfc.getAllComments(evo);
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取当前实例所有的意见列表失败[getAllComments]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		
		try {
			Vector vect=wfc.getWorkFlowHistory(vo);
			OrganizationCacheServiceInterface orgCacheIF = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			//nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser
			int count = 0 ;
			CommentVO cvoTmp = new CommentVO() ;
			//前一步骤,时间处理上更加合理
			EVO proEvo = new EVO() ;
			for(int i=0; i<vect.size(); i++) {
				  evo = (EVO) vect.elementAt(i);
				proEvo = (EVO) vect.elementAt(i==vect.size()-1?i:i+1);
				Map tmpMap = new HashMap();
				tmpMap.put("instanceid", evo.getInstanceID());
				tmpMap.put("nodename", evo.getNodeName());
				tmpMap.put("nodestarttime", proEvo.getNodeStartTime());
				//列出当前节点结束时间，就是上一节点开始时间
				tmpMap.put("nodeendtime", evo.getNodeStartTime());
				tmpMap.put("username", evo.getUserName());  //办理人
				tmpMap.put("nextnodeid", evo.getNextNodeID());
				tmpMap.put("nextnodename", evo.getNextNodeName());
				if(evo.getNextNodeUser()!=null&&"signUser".equals(evo.getNextNodeUser())){//会签节点
					tmpMap.put("nextnodeuser", "会签审批");
				}else{
					tmpMap.put("nextnodeuser", evo.getNextNodeUser());
				}
				tmpMap.put("nextnodeuser", evo.getNextNodeUser());
				String orgid = evo.getOrgname(); //没错就是机构ID
				String orgName = orgCacheIF.getOrgName(orgid);
				tmpMap.put("orgid", orgid);
				tmpMap.put("orgname", orgName);
				/********追回操作特殊处理（操作名称、处理岗位） add by tangzf 2014-01-23 START**********/
				String nodenametmp = evo.getNodeName();//实际办理人岗位
				String mehtods = evo.getMethods();
				if("执行跳转操作,完成当前节点办理".equals(mehtods)){//追回操作，操作名称重新赋值
					mehtods = "执行追回操作！";
					nodenametmp = evo.getNextNodeName();
				}
				/***** 拿回操作，办理岗位重新赋值  2014-06-30 唐顺岩     BEGIN  **/
				if("执行重办操作，该文档被拿回重新办理！".equals(mehtods)){
					nodenametmp = evo.getNextNodeName();
				}
				/***** 拿回操作，办理岗位重新赋值  2014-06-30 唐顺岩     END  **/
				tmpMap.put("nodenametmp", nodenametmp);
				tmpMap.put("methods", mehtods); //操作名称
				/********追回操作特殊处理（操作名称、处理岗位） add by tangzf 2014-01-23 END**********/
				//把流程意见时间和节点的处理时间相同的对应起来，并且标注该节点是否有业务要素的变更，
				for (Iterator iterator = vecResult.iterator(); iterator.hasNext();) {
					 cvoTmp = (CommentVO) iterator.next();
//					 if(evo.getNodeStartTime().equals(cvoTmp.getCommentTime())){
//						 tmpMap.put("commentid", cvoTmp.getCommentID());
//						 tmpMap.put("commentcontent", cvoTmp.getCommentContent());
//						 tmpMap.put("modifiedbizvar", this.getModifiedBizVarWithString(cvoTmp.getCommentID(), connection)==true?"1":"0");
//						 break;
//					 }else if(i==0){
//						 tmpMap.put("commentid", cvoTmp.getCommentID());
//						 tmpMap.put("commentcontent", cvoTmp.getCommentContent());
//						 tmpMap.put("modifiedbizvar", this.getModifiedBizVarWithString(cvoTmp.getCommentID(), connection)==true?"1":"0");
//						 break;
//					 }
//					 else
//					 {
//					     //流程拿回没有流程意见，意见和上一操作一样
//						 tmpMap.put("commentid", cvoTmp.getCommentID());
//						 tmpMap.put("commentcontent", "同前一节点意见！");
//					 }
					 //通过NodeActionId关联审批历史及流程意见
//					 if(evo.getNodeStartTime().equals(cvoTmp.getCommentTime())){
					 if(evo.getNodeActionId().equals(cvoTmp.getNodeActionId())){
						 tmpMap.put("commentid", cvoTmp.getCommentID());
						 tmpMap.put("commentcontent", cvoTmp.getCommentContent());
						 tmpMap.put("curnodeid", cvoTmp.getNodeID());//节点id
						 tmpMap.put("username", cvoTmp.getUserName());  //办理人(从意见列表中取-托管特殊处理)
						 tmpMap.put("modifiedbizvar", this.getModifiedBizVarWithString(cvoTmp.getCommentID(), connection)==true?"1":"0");
						 /* added by yangzy 2014/11/17 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能,流程历史增加展示流程意见（附） start */
						 tmpMap.put("commentcontentother", this.getWfiBizCommentRecordWithString(evo.getInstanceID(),cvoTmp.getCommentID(), connection));
						 /* added by yangzy 2014/11/17 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能,流程历史增加展示流程意见（附） end */
						 /********否决操作特殊处理（操作名称） add by tangzf 2014-02-11 START**********/
						 if(cvoTmp.getCommentSign()!=null&&!"".equals(cvoTmp.getCommentSign())&&"20".equals(cvoTmp.getCommentSign())){
							 tmpMap.put("methods", "执行否决操作！"); //操作名称
						 }
						 /********否决操作特殊处理（操作名称） add by tangzf 2014-02-11 END**********/
						 break;
					 }
				}
				list.add(tmpMap);
 			} 
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取简单流程跟踪信息失败[getWorkFlowHistory]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return list;
	}
	
	
	public boolean getModifiedBizVarWithString(String commentId, Connection connection) throws WFIException {
		boolean flag = false;
		try {
			String sqlId = "queryWfiBizVarByCommentId";
			KeyedCollection param = new KeyedCollection();		
			param.addDataField("commentId", commentId);
			WfiBizVarRecordVO varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			if(varVo==null || varVo.getInstanceid()==null) {
				sqlId = "queryWfiBizVarByCommentIdHis";
				varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			}
			if(varVo != null) {
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return flag;
	}
	/**
	 * 获取流程意见扩展表
	 * @param instanceId
	 * @param commentId
	 * @return commentcontent
	 * <pre>
     *    修改后版本:        修改人：         修改日期:              修改内容: 
     *    1.00.00           yangzy     2014/11/27             需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能
     * </pre>
	 */
	public String getWfiBizCommentRecordWithString(String instanceId, String commentId, Connection connection) throws WFIException {
		String commentcontent = "";
		try {
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("INSTANCEID", instanceId);
			pkMap.put("COMMENTID", commentId);
			KeyedCollection kcoll = (KeyedCollection)SqlClient.queryFirst("queryWfiBizCommentRecord", pkMap , null, connection);
			if(kcoll!=null&&kcoll.getDataValue("commentcontent")!=null){
				commentcontent = (String) kcoll.getDataValue("commentcontent");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return commentcontent;
	}
	
	public List statUserTodo(String currentuserid, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List statUserTodoByApplType(String currentuserid,
			Connection connection) throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List statUserDone(String currentuserid, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List statUserDoneByApplType(String currentuserid,
			Connection connection) throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List statUserEnd(String currentuserid, Connection connection)
			throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public List statUserEndByApplType(String currentuserid,
			Connection connection) throws WFIException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWfidByWfSign(String wfsign, String currentuserid,
			String orgid) throws WFIException {

		String wfid = null;
		VO_wf_whole_property wholeProperty = WFCache.getInstance().getCacheWFObj(wfid);
		wfid = wholeProperty.WFID;
		return wfid;
		
	}

	public WFIVO synSubFlowSetSubmit(String mainInstanceId, String mainNodeId,
			String subWfSign, String currentUserId, String orgId, String sysId,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setMainInstanceID(mainInstanceId);
		evo.setMainNodeID(mainNodeId);
		String subWfid = (String) getWFPropertyByWfSign(subWfSign, "WFID");
		evo.setWFID(subWfid);
		//下一个节点为子流程的第一个节点
		String subNextNodeId = (String) getWFPropertyByWfSign(subWfSign, "WFFirstNodeDocID");
		evo.setNodeID(subNextNodeId);
		//子流程的第一个节点办理人(发起人)为当前操作人
		evo.setNextNodeUser(currentUserId);
		evo.setCurrentUserID(currentUserId);
		evo.setOrgid(orgId);
		evo.setSysid(sysId);
		evo.setConnection(connection);
		try {
			evo = wfc.synSubFlowSetSubmit(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "用户同步子流程提交失败[synSubFlowSetSubmit]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO asynSubFlowSetSubmit(String mainInstanceId, String mainNodeId,
			String subWfSign, String currentUserId, String appId, String orgId, String sysId,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setMainInstanceID(mainInstanceId);
		evo.setMainNodeID(mainNodeId);
		String subWfid = (String) getWFPropertyByWfSign(subWfSign, "WFID");
		evo.setWFID(subWfid);
		evo.setCurrentUserID(currentUserId);
		evo.setAppID(appId);
		evo.setOrgid(orgId);
		evo.setSysid(sysId);
		evo.setConnection(connection);
		//客户端操作中仍然要需要设置实例号与节点,是否合理?
		evo.setNodeID(mainNodeId);
		evo.setInstanceID(mainInstanceId);
		try {
			evo = wfc.asynSubFlowSetSubmit(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "用户异步子流设置初始化失败[asynSubFlowSetSubmit]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO setSPStatus(String instanceId, String spStatus,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setSPStatus(spStatus);
		evo.setConnection(connection);
		try {
			evo = wfc.setSPStatus(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "设置流程审批状态失败[setSPStatus]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
		
	}

	public String getWorkFlowVersion() {

		return wfc.getWorkFlowVersion();
	}

	public void sendMail(String sender, String sendto, String content,
			String orgid, Map paramMap) {
		// TODO Auto-generated method stub
		
	}

	public void sendSMS(String sender, String sendto, String content,
			String orgid, Map paramMap) {
		// TODO Auto-generated method stub
		
	}

	public WFIVO resetUrgentTreat(String instanceId, String planEndTime,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setWFPlanEndTime(planEndTime);
		evo.setConnection(connection);
		try {
			evo = wfc.resetUrgentTreat(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "重置工作流过期通知时间失败[resetUrgentTreat]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO taskSignIn(String instanceId, String currentUserId,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentUserId);
		evo.setConnection(connection);
		try {
			evo = wfc.taskSignIn(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "任务认领失败[taskSignIn]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO taskSignOff(String instanceId, String currentUserId,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setCurrentUserID(currentUserId);
		evo.setConnection(connection);
		try {
			evo = wfc.taskSignOff(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "撤销任务认领失败[taskSignOff]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public WFIVO resetCurrentNodeUser(String instanceId, String nodeId,
			String resetNodeUser, String currentUserId, WFICommentVO commentVO,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		EVO evo = new EVO();
		evo.setInstanceID(instanceId);
		evo.setNodeID(nodeId);
		evo.setCurrentNodeUser(resetNodeUser);
		evo.setCurrentUserID(currentUserId);
		evo.setConnection(connection);
		if(commentVO!=null) {
			CommentVO cvo = wfiComment2EchainComment(commentVO);
			evo.setCommentVO(cvo);
		}
		try {
			evo = wfc.resetCurrentNodeUser(evo);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "重置当前节点办理人失败[resetCurrentNodeUser]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = evo2Wfivo(evo);
		return wfivo;
	}

	public String getWFExtPropertyByWfId(String wfid, String key)
			throws WFIException {
		
		String value = null;
		if(wfid==null||wfid.trim().equals("")||key==null||key.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程扩展属性失败[getWFExtPropertyByWfId]。原因：参数wfid、key不能为空。wfid=["+wfid+"]，key=["+key+"]");
			throw new WFIException("获取流程扩展属性失败[getWFExtPropertyByWfId]。原因：参数wfid、key不能为空。wfid=["+wfid+"]，key=["+key+"]");
		}
		VO_wf_whole_property wholeProperty = WFCache.getInstance().getCacheWFObj(wfid);
		Map extMap = wholeProperty.hmAppExtProperty;  //扩展属性map
		Iterator iterator = extMap.keySet().iterator();
		while(iterator.hasNext()) {
			String keyTmp = (String) iterator.next();
			if(keyTmp.toLowerCase().equals(key.toLowerCase())) {
				value = (String) extMap.get(keyTmp);
				break;
			}
		}
		return value;
	}

	public String getWFExtPropertyByWfSign(String wfsign, String key)
			throws WFIException {
		
		String value = null;
		if(wfsign==null||wfsign.trim().equals("")||key==null||key.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程扩展属性失败[getWFExtPropertyByWfSign]。原因：参数wfsign、key不能为空。wfsign=["+wfsign+"]，key=["+key+"]");
			throw new WFIException("获取流程扩展属性失败[getWFExtPropertyByWfSign]。原因：参数wfsign、key不能为空。wfsign=["+wfsign+"]，key=["+key+"]");
		}
		VO_wf_whole_property wholeProperty = WFCache.getInstance().getCacheWFObjByWFSign(wfsign);
		Map extMap = wholeProperty.hmAppExtProperty;
		Iterator iterator = extMap.keySet().iterator();
		while(iterator.hasNext()) {
			String keyTmp = (String) iterator.next();
			if(keyTmp.toLowerCase().equals(key.toLowerCase())) {
				value = (String) extMap.get(keyTmp);
				break;
			}
		}
		return value;
	}

	public Object getWFPropertyByWfId(String wfid, String key)
			throws WFIException {

		Object value = null;
		if(wfid==null||wfid.trim().equals("")||key==null||key.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfId]。原因：参数wfid、key不能为空。wfid=["+wfid+"]，key=["+key+"]");
			throw new WFIException("获取流程属性设置失败[getWFPropertyByWfId]。原因：参数wfid、key不能为空。wfid=["+wfid+"]，key=["+key+"]");
		}
		VO_wf_whole_property wholeProperty = WFCache.getInstance().getCacheWFObj(wfid);
		Field[] fields = wholeProperty.getClass().getFields();
		for(Field field : fields) {
			String fieldName = field.getName();
			if(fieldName.toLowerCase().equals(key.toLowerCase())) {
				try {
					value = field.get(wholeProperty);
					break;
				} catch (IllegalArgumentException e) {
					WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfId]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				} catch (IllegalAccessException e) {
					WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfId]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				}
			}
		}
		return value;
	}

	public Object getWFPropertyByWfSign(String wfsign, String key)
			throws WFIException {

		Object value = null;
		if(wfsign==null||wfsign.trim().equals("")||key==null||key.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfSign]。原因：参数wfsign、key不能为空。wfsign=["+wfsign+"]，key=["+key+"]");
			throw new WFIException("获取流程属性设置失败[getWFPropertyByWfSign]。原因：参数wfsign、key不能为空。wfsign=["+wfsign+"]，key=["+key+"]");
		}
		VO_wf_whole_property wholeProperty = WFCache.getInstance().getCacheWFObjByWFSign(wfsign);
		Field[] fields = wholeProperty.getClass().getFields();
		for(Field field : fields) {
			String fieldName = field.getName();
			if(fieldName.toLowerCase().equals(key.toLowerCase())) {
				try {
					value = field.get(wholeProperty);
					break;
				} catch (IllegalArgumentException e) {
					WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfSign]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				} catch (IllegalAccessException e) {
					WfLog.log(WfLog.ERROR, "获取流程属性设置失败[getWFPropertyByWfSign]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				}
			}
		}
		return value;
	}

	public String getWFNodeExtProperty(String nodeid, String extKey)
			throws WFIException {
		if(nodeid==null||nodeid.trim().equals("")||extKey==null||extKey.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程节点扩展属性失败[getWFNodeExtProperty]。原因：参数nodeid、extKey不能为空。nodeid=["+nodeid+"]，extKey=["+extKey+"]");
			throw new WFIException("获取流程节点扩展属性失败[getWFNodeExtProperty]。原因：参数nodeid、extKey不能为空。nodeid=["+nodeid+"]，extKey=["+extKey+"]");
		}
		String value = null;
		VO_wf_node_property nodeProperty = WFCache.getInstance().getNodeProperty(nodeid);
		Map extMap = nodeProperty.hmAppExtProperty;
		Iterator iterator = extMap.keySet().iterator();
		while(iterator.hasNext()) {
			String keyTmp = (String) iterator.next();
			if(keyTmp.toLowerCase().equals(extKey.toLowerCase())) {
				value = (String) extMap.get(keyTmp);
				break;
			}
		}
		return value;
	}

	public Object getWFNodeProperty(String nodeid, String key)
			throws WFIException {
		Object value = null;
		if(nodeid==null||nodeid.trim().equals("")||key==null||key.trim().equals("")) {
			WfLog.log(WfLog.ERROR, "获取流程节点属性失败[getWFNodeProperty]。原因：参数nodeid、key不能为空。nodeid=["+nodeid+"]，key=["+key+"]");
			throw new WFIException("获取流程节点属性失败[getWFNodeProperty]。原因：参数nodeid、key不能为空。nodeid=["+nodeid+"]，key=["+key+"]");
		}
		VO_wf_node_property nodeProperty = WFCache.getInstance().getNodeProperty(nodeid);
		Field[] fields = nodeProperty.getClass().getFields();
		for(Field field : fields) {
			String fieldName = field.getName();
			if(fieldName.toLowerCase().equals(key.toLowerCase())) {
				try {
					value = field.get(nodeProperty);
					break;
				} catch (IllegalArgumentException e) {
					WfLog.log(WfLog.ERROR, "获取流程节点属性失败[getWFNodeProperty]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				} catch (IllegalAccessException e) {
					WfLog.log(WfLog.ERROR, "获取流程节点属性失败[getWFNodeProperty]。异常信息为："+e.getMessage(), e);
					e.printStackTrace();
					throw new WFIException(e);
				}
			}
		}
		return value;
	}

	public WFIVO initializeGather(String bizSeqNo, String gatherTitle, String gatherDesc,
			String currentUserId, String gatherUserList, String endUserId, String beforeInstanceId,
			String mainInstanceId, String mainNodeId, Connection connection)
			throws WFIException {

		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		gatherVO.setGatherStartUserID(currentUserId);
		String userName = OrgClass.getInstance().getUserNameByUserID(currentUserId, connection);
		gatherVO.setGatherStartUserName(userName);
		gatherVO.setCurrentGatherUserList(gatherUserList);
		gatherVO.setGatherEndUserID(endUserId);
		String endUserName = OrgClass.getInstance().getUserNameByUserID(endUserId, connection);
		gatherVO.setGatherEndUserName(endUserName);
		gatherVO.setBeforeInstanceID(beforeInstanceId);
		gatherVO.setMainInstanceID(mainInstanceId);
		gatherVO.setMainNodeID(mainNodeId);
		String nodeName = (String) getWFNodeProperty(mainNodeId, "NodeName");
		gatherVO.setMainNodeName(nodeName);
		gatherVO.setGatherTitle(gatherTitle);
		gatherVO.setGatherDesc(gatherDesc);
		gatherVO.setBizSeqNo(bizSeqNo);
		try {
			gatherVO = wfc.initializeGather(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "发起会办失败[initializeGather]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = gatherVO2Wfivo(gatherVO);
		return wfivo;
	}

	public WFIVO wfCompleteGather(String gatherInstanceId,
			String currentUserId, WFIGatherCommentVO commentVO,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		GatherActionVO actionVO = wfiGatherComment2GatherAction(commentVO);
		gatherVO.setGatherActionVO(actionVO);
		try {
			gatherVO = wfc.wfCompleteGather(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "会办办理人完成工作，提交给会办汇总人失败[wfCompleteGather]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = gatherVO2Wfivo(gatherVO);
		return wfivo;
	}

	public WFIVO wfFinishGatherJob(String gatherInstanceId,
			String currentUserId, WFIGatherCommentVO commentVO,
			Connection connection) throws WFIException {
		
		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		GatherActionVO actionVO = wfiGatherComment2GatherAction(commentVO);
		gatherVO.setGatherActionVO(actionVO);
		gatherVO.setConnection(connection);
		try {
			gatherVO = wfc.wfFinishGatherJob(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "会办汇总人结束会办 失败[wfFinishGatherJob]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = gatherVO2Wfivo(gatherVO);
		return wfivo;
	}

	public WFIGatherInstanceVO getGatherInstanceInfo(String gatherInstanceId,
			String currentUserId, Connection connection) throws WFIException {

		WFIGatherInstanceVO instanceVO = new WFIGatherInstanceVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		try {
			gatherVO = wfc.getGatherInstanceInfo(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取会办实例失败[getGatherInstanceInfo]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		if(gatherVO.getSign() == EVO.SIGN_FAIL) {
			WfLog.log(WfLog.ERROR, "获取会办实例失败[getGatherInstanceInfo]。异常信息为："+gatherVO.getTip());
			throw new WFIException(gatherVO.getTip());
		}
		instanceVO.setInstanceId(gatherVO.getInstanceID());
		instanceVO.setBeforeInstanceId(gatherVO.getBeforeInstanceID());
		instanceVO.setMainInstanceId(gatherVO.getMainInstanceID());
		instanceVO.setMainNodeId(gatherVO.getMainNodeID());
		instanceVO.setMainNodeName(gatherVO.getMainNodeName());
		instanceVO.setMainJobName(gatherVO.getMainJobName());
		instanceVO.setGatherStartUserId(gatherVO.getGatherStartUserID());
		instanceVO.setGatherStartUserName(gatherVO.getGatherStartUserName());
		instanceVO.setGatherEndUserId(gatherVO.getGatherEndUserID());
		instanceVO.setGatherEndUserName(gatherVO.getGatherEndUserName());
		instanceVO.setGatherTitle(gatherVO.getGatherTitle());
		instanceVO.setGatherDesc(gatherVO.getGatherDesc());
		instanceVO.setCurrentGatherUserList(gatherVO.getCurrentGatherUserList());
		instanceVO.setAllProcessor(gatherVO.getAllProcessor());
		instanceVO.setAllProcessorName(gatherVO.getAllProcessorName());
		instanceVO.setCurrentGatherProcessors(gatherVO.getCurrentGatherProcessors());
		instanceVO.setGatherEndTime(gatherVO.getGatherEndTime());
		instanceVO.setGatherStartTime(gatherVO.getGatherStartTime());
		return instanceVO;
	}

	public List<WFIGatherCommentVO> getGatherComment(String gatherInstanceId,
			String currentUserId, String commentType, Connection connection)
			throws WFIException {

		List<WFIGatherCommentVO> commentVOs = new ArrayList<WFIGatherCommentVO>();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		GatherActionVO actionVO = new GatherActionVO();
		actionVO.setCommentType(commentType);
		gatherVO.setGatherActionVO(actionVO);
		gatherVO.setConnection(connection);
		try {
			Vector<GatherActionVO> actionVOs = wfc.getGatherActions(gatherVO);
			for(GatherActionVO vo : actionVOs) {
				WFIGatherCommentVO commentVO = new WFIGatherCommentVO();
				commentVO.setActionId(vo.getActionID());
				commentVO.setGatherInstaceId(gatherInstanceId);
				commentVO.setMainInstanceId(vo.getMainInstanceID());
				commentVO.setMainNodeId(vo.getMainNodeID());
				commentVO.setMainNodeName(vo.getMainNodeName());
				commentVO.setTransActor(vo.getTransActor());
				commentVO.setTransActorName(vo.getTransActorName());
				commentVO.setActTime(vo.getActTime());
				commentVO.setActionName(vo.getActionName());
				commentVO.setSuggest(vo.getSuggest());
				commentVO.setMemo(vo.getMemo());
				commentVO.setNextActorId(vo.getNextActorID());
				commentVO.setCommentLevel(vo.getCommentLevel());
				commentVO.setCommentType(vo.getCommentType());
				commentVOs.add(commentVO);
			}
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取会办的操作痕迹与会办意见失败[getGatherComment]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return commentVOs;
		
	}

	public WFIVO wfCheckIsFinishGather(String gatherInstanceId,
			String currentUserId, Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		try {
			gatherVO = wfc.wfCheckIsFinishGather(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "判断是否可以结束会办失败[wfCheckIsFinishGather]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo.setSign(gatherVO.getSign());
		wfivo.setMessage(gatherVO.getTip());
		return wfivo;		
	}

	public WFIVO wfResetGatherProcessor(String gatherInstanceId,
			String curGatherUserList, String currentUserId,
			Connection connection) throws WFIException {

		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentGatherUserList(curGatherUserList);
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		try {
			gatherVO = wfc.wfResetProcessor(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "重置会办参与人失败[wfResetGatherProcessor]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = gatherVO2Wfivo(gatherVO);
		return wfivo;
	}

	public WFIVO wfChangeGather(String gatherInstanceId,
			String curGatherUserList, String currentUserId,
			WFIGatherCommentVO commentVO, Connection connection)
			throws WFIException {

		WFIVO wfivo = new WFIVO();
		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentGatherUserList(curGatherUserList);
		gatherVO.setCurrentUserID(currentUserId);
		GatherActionVO actionVO = wfiGatherComment2GatherAction(commentVO);
		gatherVO.setGatherActionVO(actionVO);
		gatherVO.setConnection(connection);
		try {
			gatherVO = wfc.wfChangeGather(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "会办参与人转办失败[wfChangeGather]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		wfivo = gatherVO2Wfivo(gatherVO);
		return wfivo;
	}

	public String getGatherStatus(String gatherInstanceId,
			String currentUserId, Connection connection) throws WFIException {

		GatherVO gatherVO = new GatherVO();
		gatherVO.setInstanceID(gatherInstanceId);
		gatherVO.setCurrentUserID(currentUserId);
		gatherVO.setConnection(connection);
		String statusStr;
		try {
			statusStr = wfc.getGatherStatus(gatherVO);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "查看会办状态失败[getGatherStatus]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return statusStr;
	}
	
	public Vector queryUserTaskPool(String currentUserId, Connection connection)
			throws WFIException {
		
		OrganizationServiceInterface orgIF = null;
		Vector vecData = new Vector();
		try {
			orgIF = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			List<SDuty> dutyList = orgIF.getDutysByUserId(currentUserId, connection);
			if(dutyList!=null && dutyList.size()>0) {
				List<RoleModel> roleList = new ArrayList<RoleModel>();
				for(SDuty d : dutyList) {
					String dutyNo = d.getDutyno();
					RoleModel roleModel = new RoleModel();
					roleModel.setRoleid(dutyNo);
					roleList.add(roleModel);
				}
				if(roleList.size()>0) {
					TaskPool taskPool = new TaskPool();
					//sysId传null，无需过滤
					vecData = taskPool.queryByRole(null, roleList);
				}
			}
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "据用户岗位权限查询用户的项目池失败[queryUserTaskPool]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		return vecData;
	}
	
	/**
	 * echain传值对象转换为信贷流程接入传值对象
	 * @param evo echain传值对象
	 * @return
	 */
	private WFIVO evo2Wfivo(EVO evo) {
		WFIVO wfivo = new WFIVO();
		int rs = evo.getSign();
		wfivo.setSign(rs);
		wfivo.setMessage(evo.getTip()) ;
		if (rs==WFIVO.SIGN_SUCCESS){
			wfivo.setInstanceId( evo.getInstanceID());
			wfivo.setWfSign(evo.getWFSign());
			wfivo.setCurrentUserId( evo.getCurrentUserID());
			wfivo.setCurrentNodeId(evo.getNodeID());
			wfivo.setCurrentNodeName( evo.getNodeName());
			wfivo.setNextNodeUser(evo.getNextNodeUser());
			wfivo.setNextNodeUserName(evo.getNextNodeUser());
			try {
				OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
				String nextNodeUser = evo.getNextNodeUser();
				if(nextNodeUser != null) {
					String[] nextNodeUsers = nextNodeUser.split(";");
					String userNames = "";
					for(String uid : nextNodeUsers) {
						String nameTmp = orgCacheMsi.getUserName(uid);
						if(nameTmp!=null && !nameTmp.equals("null") && !nameTmp.equals("")) {
							userNames += orgCacheMsi.getUserName(uid) + ";";
						} else {
							userNames += uid + ";";
						}
					}
					wfivo.setNextNodeUserName(userNames.equals("")?nextNodeUser:userNames);
				}
			} catch (Exception e) {
			}
			wfivo.setNextNodeId( evo.getNextNodeID());
			wfivo.setNextNodeName( evo.getNextNodeName());
		}
		return wfivo;
	}

	/**
	 * 信贷流程接入审批意见传值对象转换为echain意见传值对象
	 * @param wfiCommentVO 信贷流程接入审批意见传值对象
	 * @return
	 */
	private CommentVO wfiComment2EchainComment(WFICommentVO wfiCommentVO) {
		CommentVO commentVO = new CommentVO();
		commentVO.setCommentID(wfiCommentVO.getCommentId());
		commentVO.setInstanceID(wfiCommentVO.getInstanceId());
		commentVO.setNodeID(wfiCommentVO.getNodeId());
		commentVO.setNodeName(wfiCommentVO.getNodeName());
		commentVO.setUserID(wfiCommentVO.getUserId());
		commentVO.setUserName(wfiCommentVO.getUserName());
		commentVO.setCommentSign(wfiCommentVO.getCommentSign());
		commentVO.setCommentContent(wfiCommentVO.getCommentContent());
		commentVO.setCommentTime(wfiCommentVO.getCommentTime());
		commentVO.setCommentLevel(wfiCommentVO.getCommentLevel());
		commentVO.setOrgid(wfiCommentVO.getOrgId());
		return commentVO;
	}
	
	/**
	 * echain意见传值对象转换为信贷流程接入审批意见传值对象
	 * @param cvo echain意见传值对象
	 * @return
	 */
	private WFICommentVO echainComment2WfiComment(CommentVO cvo) {
		WFICommentVO wfiCommentVO = new WFICommentVO();
		wfiCommentVO.setCommentContent(cvo.getCommentContent());
		wfiCommentVO.setCommentId(cvo.getCommentID());
		wfiCommentVO.setCommentLevel(cvo.getCommentLevel());
		String commentSign = cvo.getCommentSign();
		wfiCommentVO.setCommentSign(commentSign);
		wfiCommentVO.setCommentTime(cvo.getCommentTime());
		wfiCommentVO.setInstanceId(cvo.getInstanceID());
		wfiCommentVO.setNodeId(cvo.getNodeID());
		wfiCommentVO.setNodeName(cvo.getNodeName());
		wfiCommentVO.setOrgId(cvo.getOrgid());
		wfiCommentVO.setUserId(cvo.getUserID());
		wfiCommentVO.setUserName(cvo.getUserName());
		return wfiCommentVO;
	}
	
	/**
	 * 会办传值对象转换为信贷流程接入传值对象
	 * @param gatherVO 会办传值对象
	 * @return
	 */
	private WFIVO gatherVO2Wfivo(GatherVO gatherVO) {
		WFIVO wfivo = new WFIVO();
		wfivo.setInstanceId(gatherVO.getInstanceID());
		wfivo.setSign(gatherVO.getSign());
		wfivo.setMessage(gatherVO.getTip());
		return wfivo;
	}
	
	/**
	 * 信贷流程接入会办意见传值对象转换为echain会办意见传值对象
	 * @param wfiGatherCommentVO 信贷流程接入会办意见传值对象
	 * @return
	 */
	private GatherActionVO wfiGatherComment2GatherAction(WFIGatherCommentVO wfiGatherCommentVO) {
		GatherActionVO actionVO = new GatherActionVO();
		actionVO.setSuggest(wfiGatherCommentVO.getSuggest());
		actionVO.setCommentType(wfiGatherCommentVO.getCommentType());
		actionVO.setCommentLevel(wfiGatherCommentVO.getCommentLevel());
		return actionVO;
	}
	
	public boolean loadWorkFlowCache(boolean isReload) throws WFIException {
		boolean result = false;
		FileInputStream inputStream = null;
		try {
			//支持echainstudiopath使用classpath设置 add by liuhw@20131122
			Properties p = new Properties();
			File file = ResourceUtils.getFile("classpath:echain.properties");
			inputStream = new FileInputStream(file);
	    	p.load(inputStream);
			String path = ResourceUtils.getFile(p.getProperty("echainstudiopath")).getAbsolutePath()+ File.separator;
	        WfPropertyManager.getInstance().echainstudiopath=path;
	        
	        if(isReload) {
				WfLog.log(WfLog.INFO,"－－－－－－－－重新加载引擎流程缓存－－－－－－－－");
			    WFCache.getInstance().reloadData();
			    WfPropertyManager.getInstance().reload();
			    WfPropertyManager propertyManager = WfPropertyManager.getInstance();
			    if(propertyManager.isOUCache) {
			    	WfLog.log(WfLog.INFO,"－－－－－－－－重新加载引擎组织用户缓存－－－－－－－－");
			        OUCache.getInstance().reloadData();
			    }
			} else {
				WfLog.log(WfLog.INFO,"－－－－－－－－加载引擎流程缓存－－－－－－－－");
			    WFCache.getInstance();
			    WfPropertyManager propertyManager = WfPropertyManager.getInstance();
			    if(propertyManager.isOUCache) {
			    	WfLog.log(WfLog.INFO,"－－－－－－－－加载引擎组织用户缓存－－－－－－－－");
			        OUCache.getInstance();
			    }
			}
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "加载echain流程缓存出错[loadWorkFlowCache]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		} finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new WFIException(e); 
				}
		}
	    result = true;
		return result;
	}

	/**
	 * <p> 泉州信贷根据需求调整的流程审批历史接口 </p>
	 * @param instanceId 流程实例号
	 * @param orgId 机构ID
	 * @param connection 数据库连接(可选)。
	 * @return List,List里面是Map,Map里面：nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser
	 */
	public Vector getWorkFlowHistoryQz(String instanceId, String orgId,	Connection connection) throws WFIException {
		EVO vo=new EVO();
    	vo.setOrgid(orgId);
    	vo.setInstanceID(instanceId);
    	vo.setNodeID("");
    	vo.setConnection(connection);
    	List list = new ArrayList();
    	
    	//把流程所对应的流程意见关联出来，合并到流程跟踪中，泉州需要做的修改
    	List<WFICommentVO> wfiCommentVOs = new ArrayList<WFICommentVO>();
		EVO evo = new EVO();
		evo.setConnection(connection);
		CommentVO cvo = new CommentVO();
		cvo.setInstanceID(instanceId);
		cvo.setUserID("9999");	//传入一个非空值，取所有流程意见
		cvo.setCommentType("0");  //设置取流程意见，意见类别,0：流程意见 1：业务意见 2：内部意见
		evo.setCommentVO(cvo);
		Vector vecResult ;
 		try {
			  vecResult = wfc.getAllComments(evo);
			
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "获取当前实例所有的意见列表失败[getAllComments]。异常信息为："+e.getMessage(), e);
			e.printStackTrace();
			throw new WFIException(e);
		}
		
		return vecResult;
	}
	
}

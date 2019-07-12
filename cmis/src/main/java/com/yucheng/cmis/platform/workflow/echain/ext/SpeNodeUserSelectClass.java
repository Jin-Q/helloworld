package com.yucheng.cmis.platform.workflow.echain.ext;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.echain.ext.NodeUserListExtIF;
import com.ecc.echain.workflow.engine.EVO;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WfiApproveCountVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class SpeNodeUserSelectClass implements NodeUserListExtIF{
	/**
	   * <p>根据配置信息计算出特定岗位下的审批人员，只有一个</p>
	   * <p>满足业务需求：</p>
	   * 1.排除例外审批人员
	   * 2.可以添加固定参与人员（没有均衡审批岗）
	   * 3.可以设置机构业务审批人员（优先级最高）
	   * <p>其他业务要求</p>
	   * 1.同一实例均衡分配节点已经分配过给某一用户，那么再次经过该节点时，仍然由该用户来办理，并且不计数；（满足均衡审批节点被拿回追回后，再次提交到均衡审批节点时，仍然由第一次分配的人员办理）
	   * 2.均衡配置有变更，第二天生效，每天均衡配置的节点会在第一次审批时进行初始化
	   * @param evo,evo中可以获取wfid、wfsign、wfname、instanceid、currentuserid、nodeid、nodename、connection等
	   * @return List，list中存放用户IDs
	   * 
	   * V1.2 author:tsy
	   * 放款审查岗位配置、休假登记、机构参与人员、固定参与人员等实时生效，节点审批人员每次提交业务到该节点都进行初始化，将岗位下不存在的人员加入审批队列
	   */
	
	 public List getNodeUserList(EVO evo) throws Exception{
		 
		 	List<Object> userList = new ArrayList<Object>();
		 	Connection conn = evo.getConnection();
		 	String nextNodeId = evo.getNextNodeID();
		 	String instanceId = evo.getInstanceID();
		 	/**
		 	 * 优先判断是否已经审批分配过办理人。如果是，则直接返回
		 	 */
		 	String sqlId = "queryWfNodeHisUser";
		 	KeyedCollection paramKcoll = new KeyedCollection();
		 	paramKcoll.put("instanceid", instanceId);
		 	paramKcoll.put("nextnodeid", nextNodeId);
		 	KeyedCollection retKcoll = (KeyedCollection) SqlClient.queryFirst(sqlId, paramKcoll, null, conn);
		 	if(retKcoll!=null && retKcoll.containsKey("sendto")) {
		 		String sendto = (String) retKcoll.getDataValue("sendto");
		 		if(sendto!=null && !"".equals(sendto) && !"null".equalsIgnoreCase(sendto)) {
		 			userList.add(sendto);
					return userList;
		 		}
		 	}
			//接口调用
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			WorkflowServiceInterface service = (WorkflowServiceInterface)serviceJndi.getModualServiceById("workflowService", "workflow");
			WFIInstanceVO wfiVo = service.getInstanceInfo(evo.getInstanceID(), evo.getCurrentUserID(), null, conn);
			Context context = (Context) evo.paramMap.get(WorkFlowConstance.ATTR_EMPCONTEXT);
			String applType = (String)context.getDataValue("applType");//业务类型
			String openDay = (String)context.getDataValue("OPENDAY");
//			String nodeid = (String)context.getDataValue("nodeId");//节点ID
			Map param = wfiVo.getFormData();
			String managerBrId = (String)param.get("manager_br_id");//责任机构
			//String managerId = (String)param.get("manager_id");//责任人      不用变量注释   2014-09-12
			
			/**修改均衡筛选放款审查岗例外审批为实时生效，实时查询生成该岗位下人员计数表，与岗位下人员匹配每次业务提交到该节点都实时生成人员计数表数据  
			 * 需求编号：XD140812045  author：唐顺岩   	*/
			//获取扩展属性均衡岗位设置，格式为S0226;S0200;S0200
			String balanceduty = WorkFlowUtil.getWFNodeExtProperty(evo.getNextNodeID(), "balanceduty");//均衡审批岗位
			String balancedutys[] = balanceduty.split(";");
			String cfgDutys = "";
			//将岗位拼接成 查询语句的 条件，格式：'岗位1','岗位2'   2014-09-11
			if(balancedutys.length > 1) {
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<balancedutys.length;i++){
					sb.append("'").append(balancedutys[i]).append("',");
				}
				cfgDutys = sb.toString();
				if(cfgDutys.endsWith(",")){
					cfgDutys = cfgDutys.substring(0,cfgDutys.length()-1);
				}
			} else {
				cfgDutys = "'"+balancedutys[0]+"'";
			}
			cfgDutys = "("+cfgDutys+")";
			Map<String,String> paramMap = new HashMap<String,String>();   //加泛型 
			paramMap.put("dutyno", cfgDutys);
			paramMap.put("appl_type", applType);
			paramMap.put("nodeid", nextNodeId);
			//查询在配置的均衡审批岗位和固定参与人员中，但排除例外人员
			CMISDao dao = new CMISDao();
			dao.setContext(context);
			dao.setConnection(conn);
			String sqlStr ="SELECT ACTORNO FROM (SELECT ACTORNO FROM WFI_APPROVER_USER WHERE CONFID IN (SELECT CONFID FROM WFI_APPROVER_CONF WHERE CONF_TYPE = '2' AND " +
					"APPL_TYPE = '"+applType+"' AND NODEID = '"+nextNodeId+"' AND ORGID IS NULL) UNION SELECT actorno FROM S_DUTYUSER WHERE DUTYNO in "+cfgDutys+") a " +
							"WHERE a.ACTORNO NOT IN (SELECT ACTORNO FROM WFI_APPROVER_USER WHERE CONFID IN (SELECT CONFID FROM WFI_APPROVER_CONF WHERE CONF_TYPE = '1' AND " +
							"APPL_TYPE = '"+applType+"' AND NODEID = '"+nextNodeId+"' AND ORGID IS NULL))" +
							//剔除已经生成的数据  2014-09-12  唐顺岩
							" AND A.ACTORNO NOT IN (SELECT ACTORNO FROM WFI_APPROVE_COUNT WHERE APPL_TYPE = '"+applType+"' AND NODEID = '"+nextNodeId+"')";  
			IndexedCollection iCollNotOrg = dao.queryICollBySql("Temp", sqlStr);
			
			if(iCollNotOrg!=null && iCollNotOrg.size()>0){
				//查询同流程同节点下生效用户的最小审批记录数    2014-09-15 唐顺岩    
				String count = SqlClient.queryFirst("queryMinApproveQnt", paramMap, null, conn)+"";
				int approveQnt = Integer.parseInt(count);   //转换为int
				
				List<WfiApproveCountVO> listVar = new ArrayList<WfiApproveCountVO>();
				for(int i=0;i<iCollNotOrg.size();i++){
					KeyedCollection kCollTmp = (KeyedCollection)iCollNotOrg.get(i);
					WfiApproveCountVO wfcvo = new WfiApproveCountVO();
					wfcvo.setActorno((String)kCollTmp.getDataValue("actorno"));
					wfcvo.setApplType(applType);
					wfcvo.setApproveDate(openDay);   //该日期放加入审批记录的当天，以后不在更改
					wfcvo.setApproveQnt(approveQnt);   //初始化时计数默认都为 同流程同节点下生效人员的最小笔数  2014-09-15  唐顺岩
					wfcvo.setNodeid(nextNodeId);
					wfcvo.setStatus("1");  //默认都为生效-新加标志位字段    2014-09-12 唐顺岩
					listVar.add(wfcvo);
				}
				SqlClient.executeBatch("insertWfiApproveCount", listVar, conn);
			}
			/**END**/
			
			KeyedCollection kCollParam = new KeyedCollection();
			kCollParam.put("appl_type", applType);
			kCollParam.put("nodeid", nextNodeId);
			kCollParam.put("orgid", managerBrId);
			//kCollParam.put("openday", openDay);   //不再根据日期统计人员审批计数    需求编号：XD140812045
			//查询筛选类型为机构业务审批人员的计数数据，存在数据则直接根据计数数据均衡分配
			KeyedCollection kColl = (KeyedCollection) SqlClient.queryFirst("getWfiApproverCount", kCollParam, null, conn);
			if(kColl!=null && !"".equals(kColl.getDataValue("actorno"))){
				userList.add(kColl.getDataValue("actorno"));
				kCollParam.put("actorno", kColl.getDataValue("actorno"));
				SqlClient.executeUpd("updateWfiApproverCountAddOne", kCollParam, null, null, conn);
			}else{
				/***例外审批人、机构人员、固定人员等实时生效改造---机构审批人员在页面配置时实时加入到人员计数表中      需求编号：XD140812045  2014-09-11 author：唐顺岩
				//查询筛选类型为机构业务审批人员的均衡配置
				List list = (List) SqlClient.queryList("getWfiApproverConfAct", kCollParam, conn);
				if(list!=null && list.size()>0){//机构业务审批人员有数据
					List listVar = new ArrayList();
					for(int i=0;i<list.size();i++){
						KeyedCollection kCollTmp = (KeyedCollection)list.get(i);
						WfiApproveCountVO wfcvo = new WfiApproveCountVO();
						wfcvo.setActorno((String)kCollTmp.getDataValue("actorno"));
						wfcvo.setApplType(applType);
						wfcvo.setApproveDate(openDay);
						wfcvo.setApproveQnt(0);//初始化为0
						wfcvo.setNodeid(nextNodeId);
						wfcvo.setOrgid(managerBrId);
						listVar.add(wfcvo);
					}
					SqlClient.executeBatch("insertWfiApproveCount", listVar, conn);
					userList.add(((WfiApproveCountVO)listVar.get(0)).getActorno());
					kCollParam.put("actorno", ((WfiApproveCountVO)listVar.get(0)).getActorno());
					SqlClient.executeUpd("updateWfiApproverCountAddOne", kCollParam, null, null, conn);
				}else{*/
					kCollParam.remove("orgid");
					//查询筛选类型为非机构业务审批人员的计数数据
					KeyedCollection kCollNotOrg = (KeyedCollection) SqlClient.queryFirst("getWfiApproverCountNotOrg", kCollParam, null, conn);
					if(kCollNotOrg!=null && !"".equals(kCollNotOrg.getDataValue("actorno"))){
						userList.add(kCollNotOrg.getDataValue("actorno"));
						kCollParam.put("actorno", kCollNotOrg.getDataValue("actorno"));
						SqlClient.executeUpd("updateWfiApproverCountAddOneNotOrg", kCollParam, null, null, conn);
					}else{
						/**修改均衡筛选放款审查岗例外审批为实时生效，实时查询生成该岗位下人员计数表，与岗位下人员匹配 此处代码注释   需求编号：XD140812045   2014-09-11  author：唐顺岩
						 
						//获取扩展属性均衡岗位设置，格式为S0226;S0200;S0200
						String balanceduty = WorkFlowUtil.getWFNodeExtProperty(evo.getNextNodeID(), "balanceduty");//均衡审批岗位
						String balancedutys[] = balanceduty.split(";");
						String cfgDutys = "";
						if(balancedutys.length > 1) {
							StringBuffer sb = new StringBuffer();
							for(int i=0;i<balancedutys.length;i++){
								sb.append("'").append(balancedutys[i]).append("',");
							}
							cfgDutys = sb.toString();
							if(cfgDutys.endsWith(",")){
								cfgDutys = cfgDutys.substring(0,cfgDutys.length()-1);
							}
						} else {
							cfgDutys = "'"+balancedutys[0]+"'";
						}
						cfgDutys = "("+cfgDutys+")";
						Map paramMap = new HashMap();
						paramMap.put("dutyno", cfgDutys);
						paramMap.put("appl_type", applType);
						paramMap.put("nodeid", nextNodeId);
						//查询在配置的均衡审批岗位和固定参与人员中，但排除例外人员
						CMISDao dao = new CMISDao();
						dao.setContext(context);
						dao.setConnection(conn);
						String sqlStr ="SELECT ACTORNO FROM (SELECT ACTORNO FROM WFI_APPROVER_USER WHERE CONFID IN (SELECT CONFID FROM WFI_APPROVER_CONF WHERE CONF_TYPE = '2' AND " +
								"APPL_TYPE = '"+applType+"' AND NODEID = '"+nextNodeId+"' AND ORGID IS NULL) UNION SELECT actorno FROM S_DUTYUSER WHERE DUTYNO in "+cfgDutys+") a " +
										"WHERE a.ACTORNO NOT IN (SELECT ACTORNO FROM WFI_APPROVER_USER WHERE CONFID IN (SELECT CONFID FROM WFI_APPROVER_CONF WHERE CONF_TYPE = '1' AND " +
										"APPL_TYPE = '"+applType+"' AND NODEID = '"+nextNodeId+"' AND ORGID IS NULL))";
						IndexedCollection iCollNotOrg = dao.queryICollBySql("Temp", sqlStr);
//						IndexedCollection iCollNotOrg = SqlClient.queryList4IColl("getWfiApproverConfActNotOrg", paramMap, conn);
						if(iCollNotOrg!=null && iCollNotOrg.size()>0){
							List listVar = new ArrayList();
							for(int i=0;i<iCollNotOrg.size();i++){
								KeyedCollection kCollTmp = (KeyedCollection)iCollNotOrg.get(i);
								WfiApproveCountVO wfcvo = new WfiApproveCountVO();
								wfcvo.setActorno((String)kCollTmp.getDataValue("actorno"));
								wfcvo.setApplType(applType);
								wfcvo.setApproveDate(openDay);
								wfcvo.setApproveQnt(0);//初始化为0
								wfcvo.setNodeid(nextNodeId);
								listVar.add(wfcvo);
							}
							SqlClient.executeBatch("insertWfiApproveCount", listVar, conn);
							
							userList.add(((WfiApproveCountVO)listVar.get(0)).getActorno());
							kCollParam.put("actorno", ((WfiApproveCountVO)listVar.get(0)).getActorno());
							SqlClient.executeUpd("updateWfiApproverCountAddOneNotOrg", kCollParam, null, null, conn);
						}
					}**/
				}
			}
			
		 return userList;
	 }
}
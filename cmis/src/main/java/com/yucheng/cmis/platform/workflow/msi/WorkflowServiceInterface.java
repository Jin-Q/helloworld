package com.yucheng.cmis.platform.workflow.msi;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.yucheng.cmis.platform.workflow.domain.WFICommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO;
import com.yucheng.cmis.platform.workflow.domain.WFINodeVO;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 信贷接入工作流引擎服务接口
 * <p>提供信贷审批流程中所需的工作流引擎服务，如提交、退回、打回等审批操作及工作台、审批意见等功能；但是不包括如获取审批要素修改信息服务等功能。
 * @author liuhw 2013/6/14
 */

@ModualService(modualId="workflow",modualName="工作流引擎接入模块",serviceId="workflowService",serviceDesc="工作流引擎模块对外服务接口",className="com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface")
public interface WorkflowServiceInterface {

	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>按流程ID启动流程</ol>
	 * <h2>功能描述</h2>
	 * 		根据传入的流程ID启动流程，有以下三种情况：<br>
	 * 		<li>如果传入流程ID，且流程ID不等于“wffreerouter”，根据流程ID初始化流程</li>
	 * 		<li>如果传入流程ID为“wffreerouter”，初始化自由流程</li>
	 * 		<li>如果没有传入流程ID，将按启动条件启动流程,找到对应的wfid</li>
	 * </p>
	 * @param wfid 流程ID
	 * @param currentuserid 当前用户
	 * @param instanceid 工作流实例号（可选,主动传入的话需保证instanceid唯一性）
	 * @param bizseqno 业务流水号（可选）
	 * @param wfjobname 工作任务名称（允许为空）
	 * @param orgid 组织机构ID
	 * @param sysid 系统ID
	 * @param paramMap 流程参数（可选，由应用系统将必要的业务数据放到该Map中，会自动赋值给流程路由脚本中的变量。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 				message：结果提示信息；
	 * 				instanceId：流程实例号
	 * 		  		currentNodeid:当前节点ID（流程第一个节点ID）;
	 * @throws WFIException
	 */
	@MethodService(method="initWFByID",desc="按流程ID启动流程",inParam={
			@MethodParam(paramName="wfid",paramDesc="流程ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户ID"),
			@MethodParam(paramName="instanceid",paramDesc="工作流实例号（可选）"),
			@MethodParam(paramName="bizseqno",paramDesc="业务流水号（可选）"),
			@MethodParam(paramName="wfjobname",paramDesc="工作任务名称（允许为空）"),
			@MethodParam(paramName="orgid",paramDesc="组织机构ID"),
			@MethodParam(paramName="sysid",paramDesc="系统ID"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO initWFByID(String wfid,String currentuserid,String instanceid,String bizseqno,String wfjobname,String orgid,String sysid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>按流程标识启动流程</ol>
	 * <h2>功能描述</h2>
	 * 		根据传入的流程标识启动流程，有以下两种情况：<br>
	 * 		<li>如果传入流程标识，根据流程标识初始化流程</li>
	 * 		<li>如果没有传入流程标识，将按启动条件启动流程,找到对应的wfid</li>
	 * </p>
	 * @param wfsign 流程标识
	 * @param currentuserid 当前用户
	 * @param instanceid 工作流实例号（可选 ,主动传入的话需保证instanceid唯一性）
	 * @param bizseqno 业务流水号（可选）
	 * @param wfjobname 工作任务名称（允许为空）
	 * @param orgid 组织机构ID
	 * @param sysid 系统ID
	 * @param paramMap 流程参数（可选,_emp_context,formid 表单号，由应用系统将必要的业务数据放到该Map中，会自动赋值给流程路由脚本中的变量。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 				message：结果提示信息；
	 * 				instanceId：流程实例号
	 * 		  		currentNodeid:当前节点ID（流程第一个节点ID）;
	 */
	@MethodService(method="initWFBySign",desc="按流程标识启动流程",inParam={
			@MethodParam(paramName="wfsign",paramDesc="流程标识"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="instanceid",paramDesc="工作流实例号（可选）"),
			@MethodParam(paramName="bizseqno",paramDesc="业务流水号（可选）"),
			@MethodParam(paramName="wfjobname",paramDesc="工作任务名称（允许为空）"),
			@MethodParam(paramName="orgid",paramDesc="组织机构ID"),
			@MethodParam(paramName="sysid",paramDesc="系统ID"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO initWFBySign(String wfsign,String currentuserid,String instanceid,String bizseqno,String wfjobname,String orgid,String sysid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程保存</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>执行流程保存，主要将流程参数paramMap（业务_emp_context数据域）保存到流程表单字段中</ol>
	 * </p>
	 * @param instanceid 流程实例号
	 * @param currentuserid 当前用户
	 * @param orgId 当前机构
	 * @param paramMap 流程参数（可选,_emp_context,appsign。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 				message：结果提示信息；
	 * 				instanceId：流程实例号
	 */
	@MethodService(method="wfSaveJob",desc="流程保存",inParam={
			@MethodParam(paramName="instanceid",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="orgId",paramDesc="当前机构"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfSaveJob(String instanceid,String currentuserid,String orgId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述：</h2>
	 * 		<ol>流程提交,完成当前节点办理，提交工作任务</ol>
	 * <h2>功能描述：</h2>
	 * 		<li>如果未选择下一节点（不建议出现这种情况），流程引擎会自动计算下一节点，以及下一节点的办理人员</li>
	 *		<li>如果未选择下一节点办理人（不建议出现这种情况），流程引擎会自动计算并根据流程配置将流程流转到下一节点的所有办理人员； 
	 *			<ol>流程配置为竞争办理，则下一节点所有人都可以在待办事项中看到该流程，当有一人办理完成后，其它办理人员不可见，且不可办理</ol>
	 *			<ol>流程配置为签收，则下一节点所有人都可以在待签收列表中看到该流程，当有一人签收后，其它办理人员不可见</ol>
	 *		</li>
	 * 		<li>paramMap，由应用系统将必要的业务数据放到该Map中，会自动赋值给流程路由脚本中的变量。(缺点，Map中的key和value全是String类型，脚本变量在使时，可能需要类型转换)</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserid 当前办理人
	 * @param nodeId 当前节点ID
	 * @param nextNodeId 下一节点ID，可能存在多值，用"@"分割
	 * @param nextNodeUser 下一节点办理人，可能存在多值，用"@"分割，每个单值内部的多人用";"隔开,带格式，如：U.admin
	 * @param nextAnnouceUser 抄送人员
	 * @param entrustModel 委托代办模式：0代办人办理，1原办理人代人都可以办理，2原办理人办理。没有委托设置传null
	 * @param paramMap 流程参数，主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来
	 * @param orgId 当前办理人所属机构号
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextNodeid:下一节点ID;
	 * 			nextNodename：下一点名称；
	 * 			nextNodeuser：下一节点办理人；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfCompleteJob",desc="流程提交",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentUserid",paramDesc="当前办理人"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="nextNodeId",paramDesc="下一节点ID"),
			@MethodParam(paramName="nextNodeUser",paramDesc="下一节点办理人"),
			@MethodParam(paramName="nextAnnouceUser",paramDesc="抄送人员"),
			@MethodParam(paramName="entrustModel",paramDesc="委托代办模式"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数"),
			@MethodParam(paramName="orgId",paramDesc="当前办理人所属机构号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")	},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfCompleteJob(String instanceid, String nodeId, String currentUserId, String nextNodeId, String nextNodeUser, String nextAnnouceUser, String entrustModel, Map paramMap, String orgId, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程催办</ol>
	 * <h2>功能描述</h2>
	 * 		流程手工催办，需检查流程配置的催办权限。允许催办，则实现以下功能：<br>
	 * 		<li>日志记录催办信息</li>
	 * 		<li>将流程数据中心节点状态设置为催办及更新流程跟踪中心的节点过期通知属性</li>
	 * 		<li>调用消息组件发送邮件或短信通知</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextNodeid:下一节点ID;
	 * 			nextNodename：下一点名称；
	 * 			nextNodeuser：下一节点办理人；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfUrge",desc="流程手工催办",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfUrge(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程实例签收</ol>
	 * <h2>功能描述</h2>
	 * 		根据传入的流程实例号，执行流程签收；签收完成后，流程实例进入到待办任务列表
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程变量，如果流程扩展处理需获取EMPContext，则要将其设置进来
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="instanceSignIn",desc="流程实例签收",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程变量，如果流程扩展处理需获取EMPContext，则要将其设置进来"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	WFIVO instanceSignIn(String instanceId,String currentuserid, Map paramMap, Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ol>流程实例撤销签收</ol>
	 * 	<h2>功能描述</h2>
	 * 		根据传入的流程实例号，执行流程撤销签收；成功后，流程实例进入到待签收列表。本操作需满足以下条件：<br>
	 * 		<li>当前实例节点处于正常办理状态</li>
	 * 		<li>当前实例节点还未进行办理</li>
	 * 		<li>当前节点为单人办理</li>
	 * 		<li>当前用户为节点当前办理人员</li>
	 * </p>
	 * <br>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程变量，如果流程扩展处理需获取EMPContext，则要将其设置进来
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="",desc="",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程变量，如果流程扩展处理需获取EMPContext，则要将其设置进来"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO instanceSignOff(String instanceId,String currentuserid, Map paramMap, Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ol>获取流程转办人员列表</ol>
	 * 	<h2>功能描述</h2>
	 * 		获取节点配置其他人员（除当前办理人）列表。必要条件：<br>
	 * 		<li>流程允许转办权限</li>
	 * 		<li>节点允许转办权限</li>
	 * 		<li>转交人员范围为该节点的其它可办理人(单人)</li>
	 * 		<li>当前流程状态（拟稿或者非正常流转状态）不允许进行转办操作</li>
	 * 		<li>不是当前节点办理人，不能进行转办操作</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 流程当前办理人
	 * @param nodeId 转办节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIUser userId-用户ID；userName-用户名称。如果是所有用户则list只设置一个WFIUser实例，并且其userId为${alluser}。
	 */
	@MethodService(method="getChangeUser",desc="获取流程转办人员列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="转办节点ID"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="userList",paramDesc="转办人员")})
	public List<WFIUserVO> getChangeUser(String instanceId, String nodeId, String currentUserId, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程转办</ol>
	 * <h2>功能描述</h2>
	 * 		将当前流程转交给选择的人员办理。必要条件：
	 * 		<li>流程允许转办权限</li>
	 * 		<li>节点允许转办权限</li>
	 * 		<li>转交人员范围为该节点的其它可办理人</li>
	 * 		<li>当前流程状态（拟稿或者非正常流转状态）不允许进行转办操作</li>
	 * 		<li>不是当前节点办理人，不能进行转办操作</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 节点ID
	 * @param currentuserid 当前用户
	 * @param nextNodeUser 下一节点办理人
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			nextNodeid 下一办理节点id；
	 * 			nextNodename 下一办理节点名称；
	 * 			nextNodeuser 下一办理人；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfChange",desc="流程转办",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nextNodeUser",paramDesc="下一节点办理人"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfChange(String instanceId,String nodeId,String currentuserid,String nextNodeUser,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取流程节点列表</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>根据实例号或流程ID来获取当前流程所有节点列表；参数实例号与流程ID可以传递其中一个或两个参数同时传值。</ol>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param wfid 流程ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List 存放WFINode的list；
	 */
	@MethodService(method="getWFNodeList",desc="获取流程节点列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="wfid",paramDesc="流程ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="nodeList",paramDesc="流程节点List")})
	public List<WFINodeVO> getWFNodeList(String instanceId,String wfid,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取流程已办理节点列表</ol>
	 * <h2>功能描述</h2>
	 * 		根据实例号获取当前流程已办理过的节点列表，主要应用场景：<br>
	 * 		<li>流程打回处理</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 当前节点ID
	 * @param currentUserId 当前用户
	 * @param orgId （可选，如果传入需要岗位、角色与机构存在关联，信贷暂时不传）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,list里面放WFINode
	 */
	@MethodService(method="getWFTreatedNodeList",desc="获取流程已办理节点列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="orgId",paramDesc="（可选，如果传入需要岗位、角色与机构存在关联，信贷暂时不传）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="nodeList",paramDesc="流程已办理节点List")})
	public List<WFINodeVO> getWFTreatedNodeList(String instanceId,String nodeId, String currentUserId, String orgId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ol>流程跳转</ol>
	 * 	<h2>功能描述</h2>
	 * 		将流程从当前节点跳转到任意节点办理。必要条件：
	 * 		<li>流程需要允许跳转权限</li>
	 * 		<li>节点需要允许跳转权限</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 当前节点ID
	 * @param currentuserid 当前用户
	 * @param nextNodeId 下一节点ID
	 * @param nextNodeUser 下一节点办理人（可选，不传入时引擎自动根据办理人员配置计算）
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			nextnodeid 下一办理节点id；
	 * 			nextnodename 下一办理节点名称；
	 * 			nextnodeuser 下一办理人；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfJump",desc="流程跳转",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nextNodeId",paramDesc="下一节点ID"),
			@MethodParam(paramName="nextNodeUser",paramDesc="下一节点办理人（可选，不传入时引擎自动根据办理人员配置计算）"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfJump(String instanceId,String nodeId,String currentuserid,String nextNodeId,String nextNodeUser,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程结束</ol>
	 * <h2>功能描述</h2>
	 * 		执行流程结束操作：
	 * 		<li>如果是固定流程或自由流程，正常结束</li>
	 * 		<li>如果是在固定流程中使用严格子流程（echain暂不支持在固定流程中使用自由子流程），子流程结束，回到上级流程继续运行</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeID 节点ID
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param SPStatus 审批状态（可选）：0：审批中,1：同意,2：不同意,3：部分同意,4：不明确
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   		message：提示信息；
	 */
	@MethodService(method="wfEnd",desc="流程结束",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeID",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="SPStatus",paramDesc="审批状态（可选）"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfEnd(String instanceId,String nodeID,String currentuserid,Map paramMap,String SPStatus,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ol>流程退回</ol>
	 *	<h2>功能描述</h2>
	 *		执行流程退回操作，下一办理人提交时会重新计算下下节点信息；根据isDraft决定退回流程的第一个节点还是退回上一步。必要条件：
	 *		<li>并行节点不允许退回</li>
	 *		<li>流程允许退回权限</li>
	 *		<li>节点允许退回权限</li>
	 *		<li>流程状态（拟稿或者非正常流转状态）不允许进行退回操作</li>
	 *		<li>不是当前节点办理人，不能进行退回操作</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 当前办理人ID
	 * @param isDraft 1-退回第一节点，其它，退回上一节点
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:上一节点ID;
	 * 			nextnodename：上一点名称；
	 * 			nextNodeUser:上一节点办理人
	 * 			wfSign：流程标识;
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfReturnBack",desc="流程退回",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="isDraft",paramDesc="1-退回第一节点，其它，退回上一节点"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数"),
			@MethodParam(paramName="commentVO",paramDesc="审批意见vo"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfReturnBack(String instanceId, String currentUserId, String isDraft, Map paramMap, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * 	<h2>简述：</h2>
	 * 		<ol>流程打回</ol>
	 * 	<h2>功能描述：</h2>
	 * 		打回已处理过的任一节点,根据打回模式可以设置打回后直接提交给打回发起人，或是打回后逐级提交。必要条件：
	 * 		<li>当前节点办理人且是当前节点的最后办理人
	 * 		<li>流程节点中充许打回权限
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 当前节点ID
	 * @param currentUserId 当前办理人ID
	 * @param nextNodeId 打回节点ID
	 * @param nextNodeUser 打回节点的办理人
	 * @param callBackModel 打回模式：0-提交给打回发起人(默认) 1-逐级提交
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			wfSign：流程标识;
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfCallBack",desc="流程打回",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="nextNodeId",paramDesc="打回节点ID"),
			@MethodParam(paramName="nextNodeUser",paramDesc="打回节点的办理人"),
			@MethodParam(paramName="callBackModel",paramDesc="打回模式：0-提交给打回发起人(默认) 1-逐级提交"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public	WFIVO wfCallBack(String instanceId, String nodeId,String currentUserId, String nextNodeId, String nextNodeUser, String callBackModel, Map paramMap, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程拿回</ol>
	 * <h2>功能描述</h2>
	 * 		上一办理人在已办事项中，将流程拿回，重新办理。必要条件：<br>
	 * 		<li>需要节点允许拿回（重办）权限</li>
	 * 		<li>并行节点不允许重办</li>
	 * 		<li>当前操作人是上一环节办理人</li>
	 * 		<li>当前节点还没有开始办理</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeuser:下一节点办理人
	 * 			wfSign：流程标识;
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfTakeBack",desc="流程拿回",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfTakeBack(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程追回</ol>
	 * <h2>功能描述</h2>
	 * 		流程发起人在已办事项中，将流程从当前节点（可以是任意节点）追回，重新办理。必要条件：<br>
	 * 		<li>当前用户是流程发起人</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 流程节点ID
	 * @param currentUserId 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			nextnodeid 下一办理节点id；
	 * 			nextnodename 下一办理节点名称；
	 * 			nextnodeuser 下一办理人；
	 * 			instanceId：流程实例号
	 * @throws WFIException
	 */
	@MethodService(method="wfTakeBackFirst",desc="流程追回",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfTakeBackFirst(String instanceId, String nodeId, String currentUserId, Map paramMap, Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>流程撤办</ol>
	 * <h2>功能描述</h2>
	 * 		执行流程撤办操作，流程实例将被异常终止；流程跟踪中心迁移到历史中。必要条件：<br>
	 * 		<li>需要流程允许撤办权限。</li>
	 * </p>
	 * 
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfCancel",desc="流程撤办",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfCancel(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程挂起</ol>
	 * <h2>功能描述</h2>
	 * 		执行流程挂起操作，可以通过流程唤醒将其回到正常状态。必要条件：
	 * 		<li>需要流程允许挂起权限。</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfHang",desc="流程挂起",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfHang(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>流程唤醒</ol>
	 * <h2>功能描述</h2>
	 * 		将挂起状态的流程唤醒，继续进行办理。必要条件：
	 * 		<li>需要流程允许唤醒权限</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			wfSign：流程标识;
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfWake",desc="流程唤醒",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfWake(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>撤销办理人</ol>
	 * <h2>功能描述</h2>
	 * 		单个撤销流程办理人，将指定的撤销人员userId从流程当前办理人中移除。如果当前撤销人员是当前流程节点最后办理人，流程直接往下提交（userId是节点最后办理人时，不建议执行此操作）。必要条件：
	 * 		<li>撤销人userId需在当前节点办理人范围</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 当前节点ID
	 * @param currentuserid 当前操作人
	 * @param userId 撤销人员ID
	 * @param paramMap 流程参数（可选,主要用于流程脚本变量赋值。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfWithdrawUser",desc="撤销办理人",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="当前节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="userId",paramDesc="撤销人员ID"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfWithdrawUser(String instanceId,String nodeId,String currentuserid,String userId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>删除实例</ol>
	 * <h2>功能描述</h2>
	 * 		根据流程实例号删除流程实例，此操作一旦执行后不可恢复。
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户（可选）
	 * @param paramMap 流程参数（可选）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 * @throws WFIException
	 */
	@MethodService(method="wfDelInstance",desc="删除实例",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfDelInstance(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>删除实例</ol>
	 * <h2>功能描述</h2>
	 * 		根据业务流水号删除流程实例，此操作一旦执行后不可恢复。只考虑流程没有审批结束的业务才可能被删除
	 * </p>
	 * @param pkVal 业务流水号
	 * @param modelId 业务表模型ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfDelInstance",desc="根据业务流水号删除流程实例",inParam={
			@MethodParam(paramName="pkVal",paramDesc="业务流水号"),
			@MethodParam(paramName="modelId",paramDesc="业务表模型ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfDelInstance(String pkVal, String modelId, Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>否决(不同意)流程实例所代表的业务申请</ol>
	 * <h2>功能描述</h2>
	 * 		执行业务否决，流程实例将结束。主要应用于业务系统中途否决操作，此操作不可撤销。必要条件：
	 * 		<li>当前环节办理人或者流程启动者才可以结束流程</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param paramMap 流程参数（信贷需放入CONTEXT）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 			instanceId：流程实例号
	 */
	/*@MethodService(method="wfReject",desc="业务否决",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="paramMap",paramDesc="流程参数（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfReject(String instanceId,String currentuserid,Map paramMap,Connection connection) throws WFIException ;*/
	
	
	/**
	 * <p>
	 * 	<h2>简述：</h2>
	 * 		<ol>协助办理</ol>
	 * 	<h2>功能描述：</h2>
	 * 		<li>协助办理，节点不会发生变化即节点内部流转，只有办理人发生变化，且协助办理后会返回协助审批发起人
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 节点ID
	 * @param currentUserId 当前办理人
	 * @param nextNodeUser 下一办理人
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	tip：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			wfSign：流程标识;
	 * 			instanceId：流程实例号
	 */
	@MethodService(method="wfAssist",desc="协助办理",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前办理人"),
			@MethodParam(paramName="nextNodeUser",paramDesc="下一办理人"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfAssist(String instanceId, String nodeId, String currentUserId, String nextNodeUser, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>获取下一节点列表</ol>
	 * <h2>功能描述</h2>
	 * 		返回流程下一节点列表：
	 * 		<li>已经设置了下下处理节点信息，直接返回</li>
	 * 		<li>条件处理，根据路由条件计算结果；如果没有符合的路由，判断是否有缺省路由（路由条件为"@{otherwise}"；如果还是没有符合的路由，则放开权限</li>
	 * 		<li>非条件处理，正常计算返回</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param nodeId 节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List  WFINode设置 nodeId,nodeName,nodeType,routeName,ifSelectUser,routeSeq(Integer),nodeTransactType；nodetransacttype；
	 */
	@MethodService(method="getNextNodeList",desc="获取下一节点列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="nodeList",paramDesc="流程下一节点列表")})
	public List<WFINodeVO> getNextNodeList(String instanceId,String currentuserid,String nodeId,Connection connection) throws WFIException ;
	
			
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取节点办理人员列表</ul>
	 * <h2>功能描述</h2>
	 * 		根据节点办理人员信息配置，获取节点办理人员列表。特殊情况有：
	 * 		<li>如果已经设置了下下处理节点信息，直接返回</li>
	 * 		<li>如果办理人设置所有用户，则返回“${alluser}”</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param nodeId 节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,WFIUser设置userid人员id（格式为U.XXX）,username名称
	 */
	@MethodService(method="getNodeUserList",desc="获取节点办理人员列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="userList",paramDesc="节点办理人员列表")})
	public List<WFIUserVO> getNodeUserList(String instanceId,String currentuserid,String nodeId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取节点抄送人员列表</ul>
	 * <h2>功能描述</h2>
	 * 		<li>抄送控制为：1.全范围选择抄送，返回“{alluser}”</li>
	 * 		<li>其他情况，根据配置计算返回</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param nodeId 节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,WFIUser设置userid人员id（格式为U.XXX）,username名称
	 */
	@MethodService(method="getNodeAnnounceUserList",desc="获取节点抄送人员列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="userList",paramDesc="节点抄送人员列表")})
	public List<WFIUserVO> getNodeAnnounceUserList(String instanceId,String currentuserid,String nodeId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述：</h2>
	 * 		<ol>保存流程意见</ol>
	 * 	<h2>功能描述：</h2>
	 * 		<li>保存流程意见，也可以保存业务要素修改的信息，业务要素放在WFCommentVO对象中的VA-VZ字段中
	 * </p>
	 * @param wfCommentVO 流程意见对象
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return true-保持成功；false-保存失败
	 *//*
	@MethodService(method="saveWFComment",desc="保存流程意见",inParam={
			@MethodParam(paramName="wfCommentVO",paramDesc="流程意见对象"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="boolean",paramDesc="操作结果")})
	public boolean saveWFComment(WFICommentVO wfCommentVO , Connection connection) throws WFIException;*/
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取当前实例所有的意见列表</ul>
	 * <h2></h2>
	 * 		<li>获取当前实例所有的意见列表，用于审批意见历史查询
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param notAll false则查询所有意见，权限控制；true-查询当前用户的操作id为空的那条记录，用于回显，无权限控制
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,List里面是WFICommentVO
	 */
	@MethodService(method="getAllComments",desc="获取当前实例所有的意见列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="notAll",paramDesc="false则查询所有意见，权限控制；true-查询当前用户的操作id为空的那条记录，用于回显，无权限控制"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="commentList",paramDesc="意见列表")})
	public List<WFICommentVO> getAllComments(String instanceId,String currentuserid,boolean notAll,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取当前权限内的意见列表</ul>
	 * <h2></h2>
	 * 		<li>获取当前实例所有的意见列表，用于审批意见历史查询
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param nodeId 当前节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,List里面是WFICommentVO
	 */
	@MethodService(method="getWFComments",desc="获取当前权限内的意见列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="commentList",paramDesc="意见列表")})
	public List<WFICommentVO> getWFComments(String instanceId,String currentuserid,String nodeId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取流程实例信息</ul>
	 * <h2>功能描述</h2>
	 * 		<li>参数传入节点ID，则nodeId也将作为获取实例的条件，可用于并行节点的场景</li>
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentuserid 当前用户
	 * @param nodeId 节点ID（可选）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIInstanceVO
    */
	@MethodService(method="getInstanceInfo",desc="获取流程实例信息",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIInstanceVO",paramDesc="信贷流程实例对象")})
	public WFIInstanceVO getInstanceInfo(String instanceId,String currentuserid,String nodeId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取当前实例当前节点办理人列表</ul>
	 * <h2>功能描述</h2>
	 * 		<li>根据流程实例号获取当前节点办理人列表</li>
	 * </p>
	 * @param instanceId 实例号
	 * @param nodeId 节点ID
	 * @param orgid 机构ID(可选)
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,WFIUser设置userid人员id（格式为U.XXX）,username名称
	 */
	@MethodService(method="getInstanceNodeUserList",desc="获取当前实例当前节点办理人列表",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="orgid",paramDesc="机构ID(可选)"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="userList",paramDesc="节点办理人列表")})
	public List<WFIUserVO> getInstanceNodeUserList(String instanceId,String nodeId,String orgid,Connection connection) throws WFIException ;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>保存意见</ul>
	 * <h2>功能描述<h2>
	 * 		<li>保存流程审批意见</li>
	 * </p>
	 * @param commentId 意见ID（除需特殊指定，否则为null）
	 * @param instanceId 流程实例号
	 * @param nodeId 流程节点（可选）
	 * @param currentuserid 当前用户
	 * @param commentSign 审批意见标识
	 * @param commentContent 意见内容
	 * @param orgid 所属机构
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return boolean
	 */
	@MethodService(method="saveWFComment",desc="保存流程审批意见",inParam={
			@MethodParam(paramName="commentId",paramDesc="意见ID（可以为null）"),
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="commentSign",paramDesc="审批意见标识"),
			@MethodParam(paramName="commentContent",paramDesc="意见内容"),
			@MethodParam(paramName="orgid",paramDesc="所属机构"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="boolean",paramDesc="是否操作成功")})
	public boolean saveWFComment(String commentId,String instanceId,String nodeId,String currentuserid,String commentSign,String commentContent,String orgid,Connection connection) throws WFIException ;
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>保存意见</ul>
	 * <h2>功能描述<h2>
	 * 		<li>保存流程审批意见</li>
	 * </p>
	 * @param commentId 意见ID（除需特殊指定，否则为null）
	 * @param instanceId 流程实例号
	 * @param nodeId 流程节点（可选）
	 * @param currentuserid 当前用户
	 * @param commentSign 审批意见标识
	 * @param commentContent 意见内容
	 * @param orgid 所属机构
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return boolean
	 */
	@MethodService(method="saveWFComment",desc="保存流程审批意见",inParam={
			@MethodParam(paramName="commentId",paramDesc="意见ID（可以为null）"),
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="currentusername",paramDesc="当前用户"),
			@MethodParam(paramName="commentSign",paramDesc="审批意见标识"),
			@MethodParam(paramName="commentContent",paramDesc="意见内容"),
			@MethodParam(paramName="orgid",paramDesc="所属机构"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="boolean",paramDesc="是否操作成功")})
	public boolean saveWFComment4CusTrustee(String commentId,String instanceId,String nodeId,String currentuserid,String currentusername,String commentSign,String commentContent,String orgid,Connection connection) throws WFIException ;	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取用户待办列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户的待办列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId 流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getUserTodoWorkList",desc="获取用户待办列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getUserTodoWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取用户已办列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前页用户已办事项列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId 流程节点ID(可选，若有的话只取当前节点为nodeId的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo。如果流程扩展处理需获取EMPContext，则要将其设置进来）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getUserDoneWorkList",desc="获取用户已办列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getUserDoneWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取已办结实例列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户已经办结的流程实例列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getWFStatusEndWorkList",desc="获取已办结实例列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getWFStatusEndWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取被退回的实例列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户被退回的流程实例列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getUserReturnBackWorkList",desc="获取被退回的实例列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getUserReturnBackWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取被打回的实例列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户被打回的流程实例列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getUserCallBackWorkList",desc="获取被打回的实例列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getUserCallBackWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取异常状态文档列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户异常状态文档列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param nodeId (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getExceptionWorkList",desc="获取异常状态文档列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getExceptionWorkList(String currentuserid,String wfsign,String nodeId,Map paramMap,Connection connection) throws WFIException ;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取用户待签收列表</ul>
	 * <h2>功能描述</h2>
	 * 		查询当前用户待签收列表。
	 * 		<li>若paramMap存放业务特殊查询字段，则设置selFieldsBiz(格式为：xxx1,xxx2,xxx3)，添加到列表查询的字段中</li>
	 * 		<li>若paramMap存放业务特殊查询条件，则设置whereStrBiz(开头不带WHERE、AND等)，添加到列表查询的条件中</li>
	 * 		<li>若paramMap存放分页信息，则设置pageInfo，将进行分页查询</li>
	 * </p>
	 * @param currentuserid 当前用户
	 * @param wfsign 流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）
	 * @param NodeID (可选，若有的话只取当前节点为NodeID的工作列表)
	 * @param paramMap 参数（可选，fromRow,toRow）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return Vector,Vector里面是行记录map（key字段名，value值）
	 */
	@MethodService(method="getUserSignInWorkList",desc="获取用户待签收列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="wfsign",paramDesc="流程标识（可选，若有的话只取当前流程标识wfsign的工作列表）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID(可选，若有的话只取当前节点为NodeID的工作列表)"),
			@MethodParam(paramName="paramMap",paramDesc="变量参数（可选，selFieldsBiz，whereStrBiz，pageInfo）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Vector",paramDesc="Vector里面是行记录map（key字段名，value值）")})
	public Vector getUserSignInWorkList(String currentuserid,String wfsign,String NodeID,Map paramMap,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取可用流程列表
	 * <h2>功能描述</h2>
	 * 		按以下方式获取：
	 * 		<li>取得流程启动权限为所有人的流程
	 * 		<li>取得流程启动者为第一节点办理人并且当前登录人在第一节点办理人内的流程
	 * 		<li>取得流程启动权限符合启动条件的流程
	 * 		<li>系统自动追加的自由流程（wfid=wffreerouter）
	 * </p>
	 * @param currentuserid 当前用户
	 * @param orgid 组织机构ID
	 * @param sysid 系统ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,List里面是WFIVO,WFIVO设置：wfid,wfname,wfsign,wfver,appid,appname,formid,wfadmin,author
	 */
	@MethodService(method="getWFNameList",desc="获取可用流程列表",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="orgid",paramDesc="组织机构ID"),
			@MethodParam(paramName="sysid",paramDesc="系统ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面是WFIVO,WFIVO设置：wfid,wfname,wfsign,wfver,appid,appname,formid,wfadmin,author")})
	public List<WFIVO> getWFNameList(String currentuserid,String orgid,String sysid,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取简单流程跟踪信息</ul>
	 * <h2>功能描述</h2>
	 * 		根据流程实例号获取简单流程跟踪信息
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 当前用户（可选）
	 * @param nodeId 流程节点ID
	 * @param orgId 机构ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List,List里面是Map,Map里面：nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser
	 */
	@MethodService(method="getWorkFlowHistory",desc="获取简单流程跟踪信息",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="nodeId",paramDesc="流程节点ID"),
			@MethodParam(paramName="orgId",paramDesc="机构ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面是Map,Map里面：nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser")})
	public List getWorkFlowHistory(String instanceId,String currentUserId,String nodeId,String orgId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>个人待办任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户待办任务，按物理流程
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum
	 */
	@MethodService(method="statUserTodo",desc="个人待办任务统计（按物理流程）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum")})
	public List statUserTodo(String currentuserid, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>个人待办任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户待办任务，按申请类型
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum
	 */
	@MethodService(method="statUserTodoByApplType",desc="个人待办任务统计（按申请类型）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum")})
	public List statUserTodoByApplType(String currentuserid, Connection connection) throws WFIException;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>已办任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户待办任务，按物理流程
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum
	 */
	@MethodService(method="statUserDone",desc="已办任务统计（按物理流程）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum")})
	public List statUserDone(String currentuserid, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>已办任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户待办任务，按申请类型
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum
	 */
	@MethodService(method="statUserDoneByApplType",desc="已办任务统计（按申请类型）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum")})
	public List statUserDoneByApplType(String currentuserid, Connection connection) throws WFIException;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>个人办结任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户办结任务，按物理流程
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum
	 */
	@MethodService(method="statUserEnd",desc="已办任务统计（按物理流程）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,流程名称wfname，流程标识wfsign,实例数目wfnum")})
	public List statUserEnd(String currentuserid, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>个人办结任务统计</ul>
	 * <h2>功能描述</h2>
	 * 		统计当前用户办结任务，按申请类型
	 * </p>
	 * @param currentuserid 当前用户
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum
	 */
	@MethodService(method="statUserEnd",desc="已办任务统计（按申请类型）",inParam={
			@MethodParam(paramName="currentuserid",paramDesc="当前用户（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面放Map,申请类型ID-appltype，申请类型名称appltypename,实例数目wfnum")})
	public List statUserEndByApplType(String currentuserid, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述<h2>
	 * 		<ul>获取流程ID</ul>
	 * <h2>功能描述</h2>
	 * 		根据流程标识获取流程ID
	 * </p>
	 * @param wfsign 流程标识
	 * @param currentuserid 当前操作用户ID
	 * @param orgid 当前组织机构
	 * @return wfid
	 */
	@MethodService(method="getWfidByWfSign",desc="获取流程ID",inParam={
			@MethodParam(paramName="wfsign",paramDesc="流程标识"),
			@MethodParam(paramName="currentuserid",paramDesc="当前用户"),
			@MethodParam(paramName="orgid",paramDesc="当前组织机构")},
			outParam={@MethodParam(paramName="wfid",paramDesc="流程ID")})
	public String getWfidByWfSign(String wfsign,String currentuserid,String orgid) throws WFIException;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ul>用户同步子流程提交</ul>
	 * 	<h2>功能描述</h2>
	 * 		发起用户同步子流程。成功后，流程的当前节点为子流程的第一个节点，流程节点当前办理人为当前操作人。
	 * </p>
	 * @param mainInstanceId 主流程实例号
	 * @param mainNodeId 主流程节点ID
	 * @param subWfSign 子流程标识
	 * @param currentUserId 当前办理人ID
	 * @param orgId 机构id
	 * @param sysId 系统id
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			instanceId：流程实例号
	 * @throws WFIException
	 */
	@MethodService(method="synSubFlowSetSubmit",desc="用户同步子流程提交",inParam={
			@MethodParam(paramName="mainInstanceId",paramDesc="主流程实例号"),
			@MethodParam(paramName="mainNodeId",paramDesc="主流程节点ID"),
			@MethodParam(paramName="subWfSign",paramDesc="子流程标识"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="orgId",paramDesc="机构id"),
			@MethodParam(paramName="sysId",paramDesc="系统id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO synSubFlowSetSubmit(String mainInstanceId,String mainNodeId,String subWfSign,String currentUserId,String orgId,String sysId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>用户异步子流设置初始化</ul>
	 * <h2>功能描述</h2>
	 * 		发起用户异步子流程。
	 * </p>
	 * @param mainInstanceId 主流程实例号
	 * @param mainNodeId 主流程节点ID
	 * @param subWfSign 子流程标识
	 * @param currentUserId 当前办理人ID
	 * @param appId 应用模块Id（信贷对应申请类型）
	 * @param orgId 机构id
	 * @param sysId 系统id
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * 		   	nextnodeid:下一节点ID;
	 * 			nextnodename：下一点名称；
	 * 			nextNodeUser:下一节点办理人
	 * 			instanceId：流程实例号
	 * @throws WFIException
	 */
	@MethodService(method="asynSubFlowSetSubmit",desc="用户异步子流设置初始化",inParam={
			@MethodParam(paramName="mainInstanceId",paramDesc="主流程实例号"),
			@MethodParam(paramName="mainNodeId",paramDesc="主流程节点ID"),
			@MethodParam(paramName="subWfSign",paramDesc="子流程标识"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="appId",paramDesc="应用模块Id"),
			@MethodParam(paramName="orgId",paramDesc="机构id"),
			@MethodParam(paramName="sysId",paramDesc="系统id"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO asynSubFlowSetSubmit(String mainInstanceId,String mainNodeId,String subWfSign,String currentUserId, String appId, String orgId,String sysId,Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>设置流程审批状态</ul>
	 * <h2>功能描述</h2>
	 * 		根据流程实例号设置流程审批状态为指定的状态。
	 * </p>
	 * @param instanceId 流程实例号
	 * @param spStatus 审批状态：0：审批中,1：同意,2：不同意,3：部分同意,4：不明确
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="setSPStatus",desc="设置流程审批状态",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="spStatus",paramDesc="审批状态"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO setSPStatus(String instanceId, String spStatus, Connection connection) throws WFIException ;
	
	  
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>获取工作流版本</ul>
	 * <h2>功能描述</h2>
	 * 		获取工作流版本。
	 * </p>
	 * @return String 当前工作流版本
	 */
	@MethodService(method="getWorkFlowVersion",desc="获取工作流版本",
			outParam={@MethodParam(paramName="version",paramDesc="当前工作流版本")})
	public String getWorkFlowVersion() ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>发送邮件</ul>
	 * <h2>功能描述</h2>
	 * 		发送邮件。
	 * </p>
	 * @param sender 发送人
	 * @param sendto 发送对象(用户ID）
	 * @param content 内容
	 * @param orgid 机构
	 * @param paramMap 表单数据，设置(String)paramMap.get("addressee") 发送对象邮件地址
	 */
	@MethodService(method="sendMail",desc="发送邮件",inParam={
			@MethodParam(paramName="sender",paramDesc="发送人"),
			@MethodParam(paramName="sendto",paramDesc="发送对象(用户ID）"),
			@MethodParam(paramName="content",paramDesc="内容"),
			@MethodParam(paramName="orgid",paramDesc="机构"),
			@MethodParam(paramName="paramMap",paramDesc="表单数据")})
	public void sendMail(String sender, String sendto, String content, String orgid, Map paramMap);
	

	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>发送短信</ul>
	 * <h2>功能描述</h2>
	 * 		发送短信。
	 * </p>
	 * @param sender 发送人
	 * @param sendto  发送对象
	 * @param content 内容
	 * @param orgid 机构
	 * @param paramMap
	 */
	@MethodService(method="sendSMS",desc="发送短信",inParam={
			@MethodParam(paramName="sender",paramDesc="发送人"),
			@MethodParam(paramName="sendto",paramDesc="发送对象(用户ID）"),
			@MethodParam(paramName="content",paramDesc="内容"),
			@MethodParam(paramName="orgid",paramDesc="机构"),
			@MethodParam(paramName="paramMap",paramDesc="表单数据")})
	public void sendSMS(String sender, String sendto, String content, String orgid, Map paramMap);
	
	  
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>重置流程过期处理</ul>
	 * <h2>功能描述</h2>
	 * 		重置工作流过期通知时间。
	 * </p>
	 * @param instanceId 实例号
	 * @param planEndTime 定义新的流程办理期限，流程办理期限可以是相对时间，如：D6（6日）d6（6工作日）H6（6小时）h6（6工作小时）；也可以是绝对时间，如yyyy-MM-dd HH:mm:ss（必须是这样的格式）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="resetUrgentTreat",desc="重置流程过期处理",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="planEndTime",paramDesc="新的流程办理期限"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO resetUrgentTreat(String instanceId, String planEndTime, Connection connection) throws WFIException ;
	 
	 
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>任务认领</ul>
	 * <h2>功能描述</h2>
	 * 		审批流程项目池中进行任务认领。
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 当前办理人ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="taskSignIn",desc="任务认领",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO taskSignIn(String instanceId, String currentUserId, Connection connection) throws WFIException ;
	 
	 
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>撤销任务认领</ul>
	 * <h2>功能描述</h2>
	 * 		撤销任务认领，任务重新进入任务池。
	 * </p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 当前办理人ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="taskSignOff",desc="撤销任务认领",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO taskSignOff(String instanceId, String currentUserId, Connection connection) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		<ul>重置当前节点办理人</ul>
	 * <h2>功能描述</h2>
	 * 		重置当前节点办理人（该操作不做权限检查），一般将此功能提供给管理员或有权限人员。
	 * </p>
	 * @param instanceId 流程实例号
	 * @param nodeId 节点ID
	 * @param resetNodeUser 重置办理的用户（必须单人）
	 * @param currentUserId 当前操作用户ID
	 * @param commentVO 意见VO（可选）
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="resetCurrentNodeUser",desc="重置当前节点办理人",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="nodeId",paramDesc="节点ID"),
			@MethodParam(paramName="resetNodeUser",paramDesc="重置办理的用户（必须单人）"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人ID"),
			@MethodParam(paramName="commentVO",paramDesc="意见VO（可选）"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO resetCurrentNodeUser(String instanceId,String nodeId,String resetNodeUser,String currentUserId,WFICommentVO commentVO,Connection connection) throws WFIException ;
	   
	   
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	<ul>获取流程扩展属性</ul>
	 * <h2>功能描述</h2>
	 * 	<ul>根据传入的流程ID及流程扩展属性key，获取属性key对应的value</ul>
	 * </p>
	 * @param wfid 流程ID
	 * @param key 属性key（忽略大小写）
	 * @return String 扩展属性key对应的value
	 * @throws WFIException
	 */
	@MethodService(method="getWFExtPropertyByWfId",desc="获取流程扩展属性",inParam={
			@MethodParam(paramName="wfid",paramDesc="流程ID"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="扩展属性key对应的value")})
	public String getWFExtPropertyByWfId(String wfid, String key) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	<ul>获取流程扩展属性</ul>
	 * <h2>功能描述</h2>
	 * 	<ul>根据传入的流程标识及流程扩展属性key，获取属性key对应的value</ul>
	 * </p>
	 * @param wfsign 流程标识
	 * @param key 属性key（忽略大小写）
	 * @return 扩展属性key对应的value
	 * @throws WFIException
	 */
	@MethodService(method="getWFExtPropertyByWfSign",desc="获取流程扩展属性",inParam={
			@MethodParam(paramName="wfsign",paramDesc="流程标识"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="扩展属性key对应的value")})
	public String getWFExtPropertyByWfSign(String wfsign, String key) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	<ul>获取流程属性设置</ul>
	 * <h2>功能描述</h2>
	 * 	<ul>根据传入的流程ID及流程属性key，获取属性key对应的value</ul>
	 * </p>
	 * @param wfid 流程ID
	 * @param key 属性key（忽略大小写）
	 * @return 属性key对应的obj
	 * @throws WFIException
	 */
	@MethodService(method="getWFPropertyByWfId",desc="获取流程属性",inParam={
			@MethodParam(paramName="wfid",paramDesc="流程ID"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="属性key对应的obj")})
	public Object getWFPropertyByWfId(String wfid, String key) throws WFIException ;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	<ul>获取流程属性设置</ul>
	 * <h2>功能描述</h2>
	 * 	<ul>根据传入的流程标识及流程属性key，获取属性key对应的value</ul>
	 * </p>
	 * @param wfsign 流程标识
	 * @param key 属性key（忽略大小写）
	 * @return 属性key对应的value
	 * @throws WFIException
	 */
	@MethodService(method="getWFPropertyByWfSign",desc="获取流程属性",inParam={
			@MethodParam(paramName="wfsign",paramDesc="流程标识"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="属性key对应的obj")})
	public Object getWFPropertyByWfSign(String wfsign, String key) throws WFIException ;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * <ul>
	 * 获取流程节点扩展属性
	 * </ul>
	 * <h2>功能描述</h2>
	 * <ul>
	 * 根据传入的节点ID及节点属性key，获取扩展属性key对应的value
	 * </ul>
	 * </p>
	 * 
	 * @param nodeid
	 *            节点id
	 * @param extKey
	 *            扩展属性key（忽略大小写）
	 * @return String 扩展属性value
	 */
	@MethodService(method="getWFNodeExtProperty",desc="获取流程节点扩展属性",inParam={
			@MethodParam(paramName="nodeid",paramDesc="节点id"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="扩展属性key对应的value")})
	public String getWFNodeExtProperty(String nodeid, String extKey) throws WFIException ;

	/**
	 * <p>
	 * <h2>简述</h2>
	 * <ul>
	 * 获取流程节点属性
	 * </ul>
	 * <h2>功能描述</h2>
	 * <ul>
	 * 根据传入的节点ID及节点属性key，获取节点属性key对应的value
	 * </ul>
	 * </p>
	 * 
	 * @param nodeid
	 *            节点id
	 * @param key
	 *            节点属性key（忽略大小写）
	 * @return Object 节点属性value
	 */
	@MethodService(method="getWFNodeProperty",desc="获取流程节点属性",inParam={
			@MethodParam(paramName="nodeid",paramDesc="节点id"),
			@MethodParam(paramName="key",paramDesc="属性key")},
			outParam={@MethodParam(paramName="value",paramDesc="属性key对应的value")})
	public Object getWFNodeProperty(String nodeid, String key) throws WFIException ;
	
	
	/**
	 * <p>
	 * 	<h2>简述</h2>
	 * 		<ul>发起会办</ul>
	 * 	<h2>功能描述</h2>
	 * 		初始化会办实例，发起会办
	 * </p>
	 * @param bizSeqNo 业务流水号
	 * @param gatherTitle 会办主题
	 * @param gatherDesc 会办描述
	 * @param currentUserId 当前用户
	 * @param gatherUserList 会办办理人员列表
	 * @param endUserId 会办汇总人ID
	 * @param beforeInstanceId 上级会办实例号，如果没有为null
	 * @param mainInstanceId 主流程实例编号
	 * @param mainNodeId 主流程节点ID
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="initializeGather",desc="发起会办",inParam={
			@MethodParam(paramName="gatherTitle",paramDesc="会办主题"),
			@MethodParam(paramName="gatherDesc",paramDesc="会办描述"),
			@MethodParam(paramName="currentUserId",paramDesc="当前用户"),
			@MethodParam(paramName="gatherUserList",paramDesc="会办办理人员列表"),
			@MethodParam(paramName="endUserId",paramDesc="会办汇总人ID"),
			@MethodParam(paramName="beforeInstanceId",paramDesc="上级会办实例号，如果没有为null"),
			@MethodParam(paramName="mainInstanceId",paramDesc="主流程实例编号"),
			@MethodParam(paramName="mainNodeId",paramDesc="主流程节点ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO initializeGather(String bizSeqNo, String gatherTitle,String gatherDesc,String currentUserId,String gatherUserList,String endUserId,String beforeInstanceId,String mainInstanceId,String mainNodeId,Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	会办审批交办
	 * <h2>功能描述</h2>
	 * 会办办理人完成工作，提交给会办汇总人
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param commentVO 会办意见vo
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfCompleteGather",desc="会办审批交办",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="commentVO",paramDesc="会办意见vo"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfCompleteGather(String gatherInstanceId,String currentUserId,WFIGatherCommentVO commentVO,Connection connection) throws WFIException;
	
		
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 结束会办
	 * <h2>功能描述</h2>
	 * 会办汇总人结束会办
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param commentVO 会办意见
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfFinishGatherJob",desc="结束会办",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="commentVO",paramDesc="会办意见"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfFinishGatherJob(String gatherInstanceId,String currentUserId,WFIGatherCommentVO commentVO,Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 获取会办实例
	 * <h2>功能描述</h2>
	 * 根据会办实例号获取会办实例
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIGatherInstanceVO
	 * @throws WFIException
	 */
	@MethodService(method="getGatherInstanceInfo",desc="获取会办实例",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIGatherInstanceVO",paramDesc="会办实例")})
	public WFIGatherInstanceVO getGatherInstanceInfo(String gatherInstanceId,String currentUserId,Connection connection) throws WFIException;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 获取会办的操作痕迹与会办意见
	 * <h2>功能描述</h2>
	 * 根据会办实例号获取会办的操作痕迹与会办意见；如果意见类别不为空，则作为过滤条件。
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param commentType 意见类别
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return List<WFIGatherCommentVO> 会办意见list
	 * @throws WFIException
	 */
	@MethodService(method="getGatherComment",desc="获取会办的操作痕迹与会办意见",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="commentType",paramDesc="意见类别"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List<WFIGatherCommentVO>",paramDesc="会办意见list")})
	public List<WFIGatherCommentVO> getGatherComment(String gatherInstanceId,String currentUserId,String commentType,Connection connection) throws WFIException;
	

	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		判断是否可以结束会办
	 * <h2>功能描述</h2>
	 * 		会办汇总人在执行结束会办操作时，系统先判断是否可以结束会办。如果还有会办参与人未填写意见，则表示不可以正常结束会办。
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfCheckIsFinishGather",desc="判断是否可以结束会办",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfCheckIsFinishGather(String gatherInstanceId,String currentUserId,Connection connection) throws WFIException;
	

	/**
	 * <p>
	 * <h2>简述</h2>
	 *  	重置会办参与人
	 * <h2>功能描述</h2>
	 * 		会办发起人重置会办参与人
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param curGatherUserList 会办办理人员列表
	 * @param currentUserId 当前办理人
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfResetGatherProcessor",desc="重置会办参与人",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="curGatherUserList",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfResetGatherProcessor(String gatherInstanceId,String curGatherUserList,String currentUserId,Connection connection) throws WFIException;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 	会办参与人转办
	 * <h2>功能描述</h2>
	 * 会办当前办理人执行会办转办操作。将自己移致“会办已办人员列表中”，将所选用户更新至“currentGatherUserList”;<br>
	 * “会办参与人ID列表”不会修改（发起会办到不同的部门-即会办参与人，而该方法是实现部门内部 流转）
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param curGatherUserList 下一会办办理人（可以是多人）
	 * @param currentUserId 当前办理人
	 * @param commentVO 转办意见
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return WFIVO sign:0-成功、1-失败、2-成功但有异常、3-部分成功、4-未知、5-结束；
	 * 		   	message：提示信息；
	 * @throws WFIException
	 */
	@MethodService(method="wfChangeGather",desc="会办参与人转办",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="curGatherUserList",paramDesc="下一会办办理人（可以是多人）"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="commentVO",paramDesc="转办意见"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="WFIVO",paramDesc="信贷流程基础值对象")})
	public WFIVO wfChangeGather(String gatherInstanceId,String curGatherUserList,String currentUserId,WFIGatherCommentVO commentVO,Connection connection) throws WFIException;

	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 查看会办状态
	 * <h2>功能描述</h2>
	 * 查看会办状态，用于前端页面提示
	 * </p>
	 * @param gatherInstanceId 会办实例号
	 * @param currentUserId 当前办理人
	 * @param connection 数据库连接(可选)。如果参数connection为null，引擎会初始化一个连接；但如果要使流程引擎与应用在同一个事务中，则传入数据库连接为必要条件。
	 * @return 会办状态描述
	 * @throws WFIException
	 */
	@MethodService(method="getGatherStatus",desc="查看会办状态",inParam={
			@MethodParam(paramName="gatherInstanceId",paramDesc="会办实例号"),
			@MethodParam(paramName="currentUserId",paramDesc="当前办理人"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="status",paramDesc="会办状态描述")})
	public String getGatherStatus(String gatherInstanceId,String currentUserId,Connection connection) throws WFIException;
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 查询用户项目池
	 * <h2>功能描述</h2>
	 * 根据用户岗位权限查询用户的项目池
	 * </p>
	 * @param currentUserId 用户ID
	 * @param connection 数据库连接
	 * @return 查询结果vector，vector存放的为行记录的vector（包括tpid、tpname、tpdsc）
	 * @throws WFIException
	 */
	@MethodService(method="queryUserTaskPool",desc="查询用户项目池",inParam={
			@MethodParam(paramName="currentUserId",paramDesc="用户ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="vectorData",paramDesc="查询结果vector，vector存放的为行记录的vector（包括tpid、tpname、tpdsc）")})
	public Vector queryUserTaskPool(String currentUserId, Connection connection) throws WFIException;
	
	
	/**
	 * <p>
	 * <h2>简述</h2>
	 * 		加载流程缓存
	 * <h2>功能描述</h2>
	 * 		加载流程缓存，包括：
	 * 		<li>加载引擎流程缓存
	 * 		<li>加载组织用户缓存
	 * </p>
	 * @param isReload 是否是请求重载流程缓存操作，true是，false否
	 * @return 加载是否成功，true成功，false失败
	 * @throws WFIException 
	 */
	@MethodService(method="loadWorkFlowCache",desc="加载流程缓存",inParam={
			@MethodParam(paramName="isReload",paramDesc="是否是请求重载流程缓存操作，true是，false否")},
			outParam={@MethodParam(paramName="boolean",paramDesc="加载是否成功，true成功，false失败")})
	public boolean loadWorkFlowCache(boolean isReload) throws WFIException ;
	
	
	/**
	 * <p> 泉州信贷根据需求调整的流程审批历史接口 </p>
	 * @param instanceId 流程实例号
	 * @param orgId 机构ID
	 * @param connection 数据库连接(可选)。
	 * @return List,List里面是Map,Map里面：nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser
	 */
	@MethodService(method="getWorkFlowHistory",desc="获取简单流程审批历史",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="orgId",paramDesc="机构ID"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="List",paramDesc="List里面是Map,Map里面：nodeid,nodename,username,nodestarttime,method,nextnodeid,nextnodename,nextnodeuser")})
	public Vector getWorkFlowHistoryQz(String instanceId,String orgId,Connection connection) throws WFIException ;
		
}

package com.yucheng.cmis.platform.workflow.msi;

import java.sql.Connection;
import java.util.Map;

import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * <p>流程审批变更服务接口</p>
 * @author liuhw
 *
 */
@ModualService(modualId="workflow",modualName="工作流引擎接入模块",serviceId="workflow4BizService",serviceDesc="流程审批变更服务接口",className="com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface")
public interface Workflow4BIZIface {
	
	/**
	 * <p>根据业务要素域名获取最新的修改后的值，并且值为string类型</p>
	 * @param instanceId 流程实例号
	 * @param varKey 业务要素域名
	 * @param connection 数据库连接
	 * @return 返回为String对象的最新修改后的业务要素的值
	 * @throws WFIException
	 */
	@MethodService(method="getModifiedBizVarWithString",desc="根据业务要素域名获取最新的修改后的值，并且值为string类型",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="varKey",paramDesc="业务要素域名"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="String",paramDesc="最新修改后的业务要素的值")})
	public String getModifiedBizVarWithString(String instanceId, String varKey, Connection connection) throws WFIException;
	
	/**
	 * <p>根据业务要素域名获取最新的修改后的值，并且值类型为double</p>
	 * @param instanceId 流程实例号
	 * @param varKey 业务要素域名
	 * @param connection 数据库连接
	 * @return 返回为Double对象的最新修改后的业务要素的值
	 * @throws WFIException
	 */
	@MethodService(method="getModifiedBizVarWithDouble",desc="根据业务要素域名获取最新的修改后的值，并且值类型为double",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="varKey",paramDesc="业务要素域名"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Double",paramDesc="最新修改后的业务要素的值")})
	public Double getModifiedBizVarWithDouble(String instanceId, String varKey, Connection connection) throws WFIException;
	
	/**
	 * <p>根据业务要素域名获取最新的修改后的值，并且值类型为int</p>
	 * @param instanceId 流程实例号
	 * @param varKey 业务要素域名
	 * @param connection 数据库连接
	 * @return 返回为Integer对象的最新修改后的业务要素的值
	 * @throws WFIException
	 */
	@MethodService(method="getModifiedBizVarWithInteger",desc="根据业务要素域名获取最新的修改后的值，并且值类型为int",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="varKey",paramDesc="业务要素域名"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Integer",paramDesc="最新修改后的业务要素的值")})
	public Integer getModifiedBizVarWithInteger(String instanceId, String varKey, Connection connection) throws WFIException;
	
	/**
	 * <p>获取所有最新的修改后的业务要素的值</p>
	 * @param instanceId 流程实例号
	 * @param connection 数据库连接
	 * @return 返回键值对（其中KEY为域名）
	 * @throws WFIException
	 */
	@MethodService(method="getAllModifiedBizVar",desc="获取所有最新的修改后的业务要素的值",inParam={
			@MethodParam(paramName="instanceId",paramDesc="流程实例号"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Map",paramDesc="键值对（其中KEY为域名）")})
	public Map getAllModifiedBizVar(String modelId, Connection connection) throws WFIException;
	
	/**
	 * <p>获取所有最新的修改后的业务要素的值</p>
	 * @param serno 业务流水号
	 * @param modelId 表模型
	 * @param connection 数据库连接
	 * @return 返回键值对（其中KEY为域名）
	 * @throws WFIException
	 */
	@MethodService(method="getAllModifiedBizVarBySerno",desc="通过serno、modelId获取所有最新的修改后的业务要素的值",inParam={
			@MethodParam(paramName="serno",paramDesc="业务流水号"),
			@MethodParam(paramName="modelId",paramDesc="表模型"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")},
			outParam={@MethodParam(paramName="Map",paramDesc="键值对（其中KEY为域名）")})
	public Map getAllModifiedBizVarBySerno(String serno, String modelId, Connection connection) throws WFIException;
	
}

package com.yucheng.cmis.platform.riskmanage.msi;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 
 * 页面接口定义，能过JAVA注解决可以方便的读取出来，该类不需要实现,通过JAVA自身的访问控制来控制其可见性
 * 
 * @author yuhq
 *
 */

@ModualService(serviceId="riskmanageJspServices",serviceDesc="风险拦截对外提供的页面服务接口",
				modualId="riskmanage",modualName="风险拦截模块",className="com.yucheng.cmis.platform.riskmanage.msi.RiskmanageJSPServiceInterface")
interface RiskmanageJSPServiceInterface {

	String url1 = "";
	
	@MethodService(method="procRiskInspect.do", desc="无流程调用风险拦截检查",
			inParam={
				@MethodParam(paramName="wfid",paramDesc="流程id"),
				@MethodParam(paramName="nodeId",paramDesc="流程节点，起始节点赋值000"),
				@MethodParam(paramName="pkVal",paramDesc="业务主键值"),
				@MethodParam(paramName="modelId",paramDesc="业务表模型"),
				@MethodParam(paramName="pvId",paramDesc="拦截方案，可以传多个以,分割"),
			},
			outParam={
			@MethodParam(paramName="list",paramDesc="输出为拦截项目检查结果，以页面展示")
		},methodType=MethodType.JSP,
			example="procRiskInspect.do?wfid=wfi_107_jr&nodeId=000&pkVal=SQ00000001&modelId=IqpLoanApp&pvId=FFFA27800134C0A6E9F203C01240D4C1")
	public void rulespop();
	
	@MethodService(method="procRiskInspect4WF.do", desc="适用流程调用风险拦截检查",
			inParam={
				@MethodParam(paramName="applType",paramDesc="流程id"),
				@MethodParam(paramName="pkVal",paramDesc="业务主键值"),
				@MethodParam(paramName="modelId",paramDesc="业务表模型"),
				@MethodParam(paramName="pvId",paramDesc="拦截方案，可以传多个以,分割"),
			},
			outParam={
			@MethodParam(paramName="list",paramDesc="输出为拦截项目检查结果，以页面展示")
		},methodType=MethodType.JSP,
			example="procRiskInspect4WF.do?applType=001&pkVal=SQ00000001&modelId=IqpLoanApp&pvId=FFFA27800134C0A6E9F203C01240D4C1")
	public void getRiskPop();
}

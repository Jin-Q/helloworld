package com.yucheng.cmis.platform.shuffle.msi;

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

@ModualService(serviceId="shuffleJspServices",serviceDesc="规则引擎对外提供的页面服务接口",
				modualId="shuffle",modualName="规则管理模块",className="com.yucheng.cmis.platform.shuffle.msi.ShuffleJSPServiceInterface")
interface ShuffleJSPServiceInterface {

	String url1 = "";
	
	@MethodService(method="rulespop", desc="查询并获取规则POP框:shuffle/shufflemanage/rulespop.jsp",
			inParam={
				@MethodParam(paramName="id",paramDesc="需要赋值的字段全名")
			},
			outParam={
			@MethodParam(paramName="rulesetid_ruleid",paramDesc="输出为【规则集id】_【规则id】，调用时将该值拆分为规则集ID和规则ID")
		},methodType=MethodType.JSP,
			example="shuffle/shufflemanage/rulespop.jsp?id=IndGroup.rating_rules")
	public void rulespop();
	
	@MethodService(method="querySfTransPopList.do", desc="查询规则交易POP框",
			outParam={
				@MethodParam(paramName="trans_id",paramDesc="规则交易id"),
				@MethodParam(paramName="trans_name",paramDesc="规则交易名称")
			},methodType=MethodType.JSP,
			example="querySfTransPopList.do")
	public void querySfTransPopList();
}

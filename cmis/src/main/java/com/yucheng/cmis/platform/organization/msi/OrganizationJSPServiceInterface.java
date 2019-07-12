package com.yucheng.cmis.platform.organization.msi;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;
import com.yucheng.cmis.base.CMISConstance;

/**
 * 
 * 页面接口定义，能过JAVA注解决可以方便的读取出来，该类不需要实现,通过JAVA自身的访问控制来控制其可见性
 * 
 * @author yuhq
 *
 */

@ModualService(serviceId="organizationJspServices",serviceDesc="组织机构管理模块对外提供的页面服务接口",
				modualId=CMISConstance.ORGANIZATION_MODUAL_ID,modualName="组织机构管理模块",className="com.yucheng.cmis.standard.platform.organization.interfaces.OrganizationServiceInterface")
interface OrganizationJSPServiceInterface {

	String url1 = "";
	
	@MethodService(method="queryCusPop.do", desc="查询客户POP框",
			inParam={
				@MethodParam(paramName="cus_type",paramDesc="客户类型(01-对公客户;02-对私客户)"),
				@MethodParam(paramName="select_model",paramDesc="选择模式(1-单选;2-多选)"),
			},
			outParam={
				@MethodParam(paramName="cus_id",paramDesc="客户码"),
				@MethodParam(paramName="cus_name",paramDesc="客户名称")
			},methodType=MethodType.JSP,
			example="getModualJSPService.do?modualId=organization&jspServiceId=queryCusPop.do&cus_type=01&select_model=1")
	public void getUrl1();
}

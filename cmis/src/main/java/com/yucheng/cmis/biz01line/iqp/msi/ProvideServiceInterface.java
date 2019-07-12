package com.yucheng.cmis.biz01line.iqp.msi;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 供应链模块向外提供的接口
 * @author 唐顺岩
 */
@ModualService(modualId="iqp",modualName="供应链管理",serviceId="provideServices",serviceDesc="供应链模块对外提供服务接口",
	className="com.yucheng.cmis.biz01line.iqp.msi.ProvideServiceInterface")
public interface ProvideServiceInterface {
	
	/**
	 * 展示核心企业POP框
	 */
	@MethodService(method="searchCoreConPop.do", desc="展示核心企业POP框",
		inParam={
		},
		outParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="cus_name",paramDesc="客户名称"),
			@MethodParam(paramName="input_id",paramDesc="登记人"),
			@MethodParam(paramName="input_br_id",paramDesc="登记机构"),
			@MethodParam(paramName="input_date",paramDesc="登记日期")
		},
		example="searchCoreConPop.do")
	public void searchCoreConPop()throws EMPException;
	
}

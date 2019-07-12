package com.yucheng.cmis.biz01line.cus.msi;

import com.yucheng.cmis.pub.CusPubConstant;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 
 * 页面接口定义
 * 
 *
 */

@ModualService(serviceId="cusJspServices",serviceDesc="客户管理模块对外提供的页面服务接口",
				modualId=CusPubConstant.CUS_MODUAL_ID,modualName="客户管理模块",className="com.yucheng.cmis.biz01line.cus.msi.CusJSPServiceInterface")
interface CusJSPServiceInterface {

	String url1 = "";
	
	@MethodService(method="queryAllCusPop.do", desc="查询客户POP框",
			inParam={
				@MethodParam(paramName="cusTypCondition",paramDesc="查询类型(Com-对公客户;Ind-对私客户……)"),
			},
			outParam={
				@MethodParam(paramName="cus_id",paramDesc="客户码"),
				@MethodParam(paramName="cus_name",paramDesc="客户名称"),
				@MethodParam(paramName="cert_type",paramDesc="证件类型"),
				@MethodParam(paramName="cert_code",paramDesc="证件号码"),
				@MethodParam(paramName="cust_mgr",paramDesc="主管客户经理"),
				@MethodParam(paramName="main_br_id",paramDesc="主管机构"),
				@MethodParam(paramName="loan_card_id",paramDesc="贷款卡编码"),
				@MethodParam(paramName="cus_type",paramDesc="客户类型"),
				@MethodParam(paramName="belg_line",paramDesc="条线")
			},methodType=MethodType.JSP,
			example="queryAllCusPop.do?cusTypCondition=Com")
	public void queryAllCusPop();
	
	@MethodService(method="queryRelaCusByLmtSernoPop.do", desc="查询关联客户POP框（个人额度申请专用，包含主借款人及配偶、共同债务人及配偶、保证人及配偶）",
			inParam={
				@MethodParam(paramName="lmt_serno",paramDesc="额度申请流水号"),
			},
			outParam={
				@MethodParam(paramName="cus_id",paramDesc="客户码"),
				@MethodParam(paramName="cus_name",paramDesc="客户名称"),
				@MethodParam(paramName="cert_type",paramDesc="证件类型"),
				@MethodParam(paramName="cert_code",paramDesc="证件号码"),
				@MethodParam(paramName="cust_mgr",paramDesc="主管客户经理"),
				@MethodParam(paramName="main_br_id",paramDesc="主管机构"),
				@MethodParam(paramName="loan_card_id",paramDesc="贷款卡编码"),
				@MethodParam(paramName="cus_type",paramDesc="客户类型"),
				@MethodParam(paramName="belg_line",paramDesc="条线"),
				@MethodParam(paramName="cus_attr",paramDesc="客户属性")
			},methodType=MethodType.JSP,
			example="queryRelaCusByLmtSernoPop.do?lmt_serno=lmt_serno")
	public void queryRelaCusByLmtSernoPop();
	
	@MethodService(method="getCusViewPage.do", desc="查看客户信息详情",
			inParam={
				@MethodParam(paramName="cusId",paramDesc="客户码"),
			},
			outParam={
				
			},methodType=MethodType.JSP,
			example="getCusViewPage.do?cusId=1018623817")
	public void getCusViewPage();
	
	@MethodService(method="getIntbankViewPage.do", desc="查看同业客户信息详情",
			inParam={
				@MethodParam(paramName="cus_id",paramDesc="同业客户码"),
			},
			outParam={
				
			},methodType=MethodType.JSP,
			example="getIntbankViewPage.do?cus_id=1018623817")
	public void getIntbankViewPage();
	
	@MethodService(method="queryCusOrgAppMngPopList.do", desc="选择评估机构pop",
			inParam={
			},
			outParam={
				@MethodParam(paramName = "cus_id", paramDesc = "评估机构客户码"),
				@MethodParam(paramName = "cus_name", paramDesc = "评估机构名称"),
				@MethodParam(paramName = "extr_eval_org", paramDesc = "组织机构代码")
			},methodType=MethodType.JSP,
			example="queryCusOrgAppMngPopList.do")
	public void queryCusOrgAppMngPopList();
	
	@MethodService(method="queryCusGoverFinTerPop.do", desc="政府融资平台pop",
			inParam={
			},
			outParam={
				@MethodParam(paramName = "cus_id", paramDesc = "政府融资平台客户码")
			},methodType=MethodType.JSP,
			example="queryCusGoverFinTerPop.do")
	public void queryCusGoverFinTerPop();
}

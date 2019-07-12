package com.yucheng.cmis.biz01line.prd.msi;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 产品模块页面接口定义
 * @author Pansq
 */
@ModualService(modualId="prd",modualName="产品配置模块页面接口",serviceId="prdJspServices",serviceDesc="产品配置模块对外提供页面服务接口",
		className="com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface")
public interface PrdJSPServiceInterface {
	@MethodService(method="showDicTree_PRD.do", desc="产品目录树结构POP框",
			outParam={
				@MethodParam(paramName="data",paramDesc="目录对象，包括目录ID、目录名称等属性"),
			},
			methodType=MethodType.JSP,
			example="showDicTree_PRD.do")
	public void getPrdCatalogTreePop();
	
	@MethodService(method="showPrdTreeDetails.do", desc="单选产品树结构pop框",
			inParam={
				@MethodParam(paramName="bizline",paramDesc="所属业务线编码"),
			},
			outParam={
			@MethodParam(paramName="data",paramDesc="产品对象，包括所选产品ID、产品名称等属性"),
			},methodType=MethodType.JSP,
			example="showPrdTreeDetails.do?bizline=BL100")
	public void getRadioPrdTreePop();
	
	@MethodService(method="showPrdCheckTreeDetails.do", desc="多选产品树结构pop框",
			inParam={
				@MethodParam(paramName="bizline",paramDesc="所属业务线编码"),
			},
			outParam={
			@MethodParam(paramName="data",paramDesc="产品对象，包括所选产品ID、产品名称等属性"),
			},methodType=MethodType.JSP,
			example="showPrdCheckTreeDetails.do?bizline=BL100")
	public void getCheckPrdTreePop();
	
	@MethodService(method="getPrdBankInfoPopList.do", desc="银行行号pop框",
			outParam={
			@MethodParam(paramName="data",paramDesc="行号,行名,上级行号等属性"),
	},methodType=MethodType.JSP,
	example="getPrdBankInfoPopList.do")
	public void getPrdBankInfoPopList();
}

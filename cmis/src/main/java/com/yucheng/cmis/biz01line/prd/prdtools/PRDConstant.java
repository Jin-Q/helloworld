package com.yucheng.cmis.biz01line.prd.prdtools;
/**
 * 产品配置模块全局常量管理类
 * @author Pansq
 *
 */
public class PRDConstant {
	/** 流程xml文件路径标识，来源于echain.properties*/
	public static final String ECHAINSTUDIOPATH = "echainstudiopath";
	/** 流程xml文件路径标识，来源于cmis.properties,用于未引入流程时调用*/
	public static final String CMISSTUDIOPATH = "cmisstudionpath";
	/** 流程字典项*/
	public static final String FLOW_TYPE = "FLOW_TYPE";
	public static final String PRDPOLCYSCHEMECOMPONENT = "PrdPolcySchemeComponent";
	public static final String PRDPOLCYSCHEMEAGENT = "PrdPolcySchemeAgent";
	public static final String PRDPOLCYSCHEMEDAO = "PrdPolcySchemeDao";
	
	public static final String ATTR_TREEPRDSERVICE = "treePrdService";
	
	/** 默认利率类型，获取利率信息使用*/
	public static final String RATEALL = "9999";
	
}

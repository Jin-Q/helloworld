package com.yucheng.cmis.biz01line.lmt.msi;

import java.math.BigDecimal;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 授信模块向外提供的接口
 * @author 唐顺岩
 */
@ModualService(modualId="lmt",modualName="授信管理",serviceId="lmtPageServices",serviceDesc="授信模块对外提供页面服务接口",
		className="com.yucheng.cmis.biz01line.lmt.msi.LmtPageServiceInterface")
public interface LmtPageServiceInterface {
	
	/**
	 * 根据授信类型（01-单一法人 02-同业客户 03-合作方），展示额度POP框
	 * @param cus_id 客户码
	 * @param lmt_type 授信类型（01-单一法人 02-同业客户 03-合作方）
	 * @param guar_type 担保方式
	 * @param prd_id 产品
	 * @param outstnd_amt 占用金额（本次需占用的额度）
	 * @param limit_type 额度类型（01-循环额度 02-一次性额度），该参数在选择单一法人授信时必须传入
	 */
	@MethodService(method="selectLmtAgrDetails.do", desc="根据授信类型（01-单一法人 02-同业客户 03-合作方），展示额度POP框",
		inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="lmt_type",paramDesc="授信类型（01-单一法人 02-同业客户 03-合作方）"),
			@MethodParam(paramName="guar_type",paramDesc="担保方式"),
			@MethodParam(paramName="prd_id",paramDesc="产品"),
			@MethodParam(paramName="outstnd_amt",paramDesc="占用金额（业务敞口金额）"),
			@MethodParam(paramName="limit_type",paramDesc="额度类型（01-循环额度 02-一次性额度），该参数在选择单一法人授信时必须传入")
		},
		outParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="cus_name",paramDesc="客户名称"),
			@MethodParam(paramName="agr_no",paramDesc="授信协议编号"),
			@MethodParam(paramName="limit_code",paramDesc="额度编号"),
			@MethodParam(paramName="sub_type",paramDesc="分项类别"),
			@MethodParam(paramName="limit_type",paramDesc="额度类型"),
			@MethodParam(paramName="guar_type",paramDesc="担保方式"),
			@MethodParam(paramName="cur_type",paramDesc="币种"),
			@MethodParam(paramName="crd_amt",paramDesc="授信金额（授信金额-冻结金额）"),
			@MethodParam(paramName="start_date",paramDesc="起始日期"),
			@MethodParam(paramName="end_date",paramDesc="到期日期")
		},
		methodType=MethodType.JSP,
		example="selectLmtAgrDetails.do?cus_id=****&lmt_type=01&guar_type=100&prd_id=100052&outstnd_amt=0.00")
	public void selectLmtAgrDetails(String cus_id,String lmt_type,String guar_type,String pri_id,BigDecimal outstnd_amt,String limit_type)throws EMPException;
	
	
	/**
	 * 根据授信协议号查询客户授信协议信息
	 * @param agr_no 协议编号
	 * @param show 是否显示[返回列表]按钮，固定为none
	 * @param menuId 菜单ID，为挂接授信台账的主模块，固定为crd_agr
	 */
	@MethodService(method="getLmtAgrInfoViewPage.do", desc="根据授信协议号查询客户授信协议信息",
		inParam={
			@MethodParam(paramName="agr_no",paramDesc="协议编号"),
			@MethodParam(paramName="show",paramDesc="是否显示[返回列表]按钮，固定为none"),
			@MethodParam(paramName="menuId",paramDesc="菜单ID，为挂接授信台账的主模块，固定为crd_agr")
		},
		outParam={ },
		methodType=MethodType.JSP,
		example="getLmtAgrInfoViewPage.do?agr_no=****&show=none&menuId=crd_agr")
	public void getLmtAgrInfoViewPage(String agr_no,String show,String menuId)throws EMPException;
	
	/**
	 * 根据客户码查询客户授信否决历史信息
	 * @param cus_id 客户码
	 * @param overrule 否决标志
	 * @param type 查询类型，固定为his
	 * @param menuId 主菜单ID，固定为corp_crd_query
	 */
	@MethodService(method="queryLmtApplyList.do", desc="根据客户码查询客户授信否决历史信息",
		inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="overrule",paramDesc="否决标志，固定为Y"),
			@MethodParam(paramName="type",paramDesc="查询类型，固定为his"),
			@MethodParam(paramName="menuId",paramDesc="主菜单ID，固定为corp_crd_query")
		},
		outParam={ },
		methodType=MethodType.JSP,
		example="queryLmtApplyList.do?cus_id=***&type=his&overrule=Y&menuId=corp_crd_query")
	public void queryLmtApplyList(String cus_id,String overrule,String type,String menuId)throws EMPException;
	
	/**
	 * 根据客户码查询客户存量授信
	 * @param cus_id 客户码
	 * @param op 操作权限，固定为view
	 * @param menuId 主菜单ID，固定为crd_agr
	 */
	@MethodService(method="queryLmtAgrInfoList.do", desc="根据客户码查询客户存量授信",
		inParam={
			@MethodParam(paramName="cus_id",paramDesc="客户码"),
			@MethodParam(paramName="op",paramDesc="操作权限，固定为view"),
			@MethodParam(paramName="menuId",paramDesc="主菜单ID，固定为crd_agr")
		},
		outParam={ },
		methodType=MethodType.JSP,
		example="queryLmtAgrInfoList.do?cus_id=***&menuId=crd_agr")
	public void queryLmtAgrInfoList(String cus_id,String op,String menuId)throws EMPException;
	
	/**
	 * 查询联保/合作方协议POP框
	 */
	@MethodService(method="queryLmtAgrJointPop.do", desc="查询联保/合作方协议POP框",
		inParam= {
			@MethodParam(paramName="condition",paramDesc="查询条件")
		},
		outParam={ },
		methodType=MethodType.JSP,
		example="queryLmtAgrJointPop.do?condition=AND AGR_STATUS='002' AND COOP_TYPE='010' AND ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-mm-dd'),6) >= (SELECT TO_DATE(OPENDAY,'yyyy-mm-dd') FROM PUB_SYS_INFO)")
	public void queryLmtAgrJointPop(String condition)throws EMPException;

	/**
	 * 查询授信台账POP框
	 */
	@MethodService(method="queryLmtAgrDetailsPop.do", desc="查询授信台账POP框",
		inParam= { },
		outParam={ },
		methodType=MethodType.JSP,
		example="queryLmtAgrDetailsPop.do?condition=AND SUB_TYPE IN('01','05') AND AGR_NO IN(SELECT AGR_NO FROM LMT_AGR_INFO WHERE GRP_AGR_NO IS NULL UNION ALL SELECT AGR_NO FROM LMT_AGR_INDIV)")
	public void queryLmtAgrDetailsPop(String condition)throws EMPException;
	
}

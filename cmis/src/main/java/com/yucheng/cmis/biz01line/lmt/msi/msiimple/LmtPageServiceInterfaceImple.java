package com.yucheng.cmis.biz01line.lmt.msi.msiimple;

import java.math.BigDecimal;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.lmt.msi.LmtPageServiceInterface;
import com.yucheng.cmis.pub.CMISModualService;

public class LmtPageServiceInterfaceImple extends CMISModualService implements LmtPageServiceInterface {

	/**
	 * 根据授信类型（01-单一法人   02-同业客户   03-合作方），展示额度POP框
	 * @param cus_id 客户码
	 * @param lmt_type 授信类型（01-单一法人   02-同业客户   03-合作方）
	 * @param guar_type 担保方式
	 * @param prd_id 产品
	 * @param outstnd_amt 占用金额（本次需占用的额度）
	 * @param limit_type 额度类型（01-循环额度 02-一次性额度），该参数在选择单一法人授信时必须传入
	 */
	public void selectLmtAgrDetails(String cusId, String lmtType,String guar_type,String pri_id, BigDecimal outstndAmt,String limit_type) {
		// TODO 此方法为URL，无需实现
	}

	/**
	 * 根据授信协议号查询客户授信协议信息
	 * @param agr_no 协议编号
	 * @param show 是否显示[返回列表]按钮，固定为none
	 * @param menuId 菜单ID，为挂接授信台账的主模块，固定为crd_agr
	 */
	public void getLmtAgrInfoViewPage(String agrNo, String show, String menuId) throws EMPException {
		// TODO 此方法为URL，无需实现
	}
	
	/**
	 * 根据客户码查询客户授信否决历史信息
	 * @param cus_id 客户码
	 * @param overrule 否决标志
	 * @param type 查询类型，固定为his
	 * @param menuId 主菜单ID，固定为corp_crd_query
	 */
	public void queryLmtApplyList(String cus_id,String overrule, String type,String menuId) throws EMPException {
		// TODO 此方法为URL，无需实现
	}

	/**
	 * 根据客户码查询客户授存量授信
	 * @param cus_id 客户码
	 * @param op 操作权限，固定为view
	 * @param menuId 主菜单ID，固定为crd_agr
	 */
	public void queryLmtAgrInfoList(String cus_id,String op,String menuId) throws EMPException {
		// TODO 此方法为URL，无需实现
	}
	
	/**
	 * 查询联保/合作方协议POP框
	 */
	public void queryLmtAgrJointPop(String condition)throws EMPException{
		// TODO 此方法为URL，无需实现
	}
	
	/**
	 * 查询授信台账POP框
	 */
	public void queryLmtAgrDetailsPop(String condition)throws EMPException{
		// TODO 此方法为URL，无需实现
	}
	
}

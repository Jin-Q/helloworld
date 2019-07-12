package com.yucheng.cmis.biz01line.cus.groupinterface;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface CusGrpInfoApplyIface {
	
	/**
	 * 关联集团新增申请
	 * @param serno
	 * @throws ComponentException
	 */
	public void insertCusGrpInfoAndCusGrpMember(String serno) throws ComponentException;
	/**
	 * 关联集团变更
	 * @param serno
	 * @throws ComponentException
	 */
	public void updateCusGrpInfoAndCusGrpMember(String serno) throws ComponentException;
	/**
	 * 判断该集团客户是否有正在进行的一般授信或是一般授信变更操作
	 * @param serno 申请序列号
	 */
	public String checkLmtApplyAndLmtModAppBySerno(String serno) throws ComponentException;
}

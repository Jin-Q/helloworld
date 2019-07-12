package com.yucheng.cmis.biz01line.cus.groupinterface.impl;

import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoApplyComponent;
import com.yucheng.cmis.biz01line.cus.groupinterface.CusGrpInfoApplyIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusGrpInfoApplyImpl extends CMISComponent implements CusGrpInfoApplyIface{
	/**
	 * 关联集团新增申请
	 * @param serno
	 * @throws ComponentException
	 */
	public void insertCusGrpInfoAndCusGrpMember(String serno)
			throws ComponentException {
		// TODO Auto-generated method stub
		CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent)this.getComponent(PUBConstant.CUSGRPINFOAPPLYCOMPONENT);
		
		cusGrpInfoApplyComponent.insertCusGrpInfoAndCusGrpMember(serno);
		
	}
	/**
	 * 判断该集团客户是否有正在进行的一般授信或是一般授信变更操作
	 * @param serno 申请序列号
	 */
	public String checkLmtApplyAndLmtModAppBySerno(String serno) throws ComponentException{
		CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent)this.getComponent(PUBConstant.CUSGRPINFOAPPLYCOMPONENT);
		String msg = "";
		msg = cusGrpInfoApplyComponent.checkLmtApplyAndLmtModAppBySerno(serno);
		return msg;
	}
	/**
	 * 关联集团变更
	 * @param serno
	 * @throws ComponentException
	 */
	public void updateCusGrpInfoAndCusGrpMember(String serno)
			throws ComponentException {
		// TODO Auto-generated method stub
		CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent)this.getComponent(PUBConstant.CUSGRPINFOAPPLYCOMPONENT);
		
		cusGrpInfoApplyComponent.updateCusGrpInfoAndCusGrpMember(serno);
		
	}

}

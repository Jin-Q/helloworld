package com.yucheng.cmis.biz01line.cus.interfaces.impl;



import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.interfaces.CusComRelIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusComRelIfaceImpl extends CMISComponent implements CusComRelIface {
	public boolean isInAutoCusGrp(String cusId) throws ComponentException {
		CusBaseRelComponent component = (CusBaseRelComponent)this.getComponent("CusComIntellectRel");
		return component.isInCusComRelTree(cusId);
	}
	
	/**
	 * 是否是集团客户判断(嘉兴)
	 * @param cusId 客户编号
	 * @return
	 * @throws ComponentException
	 */
	public boolean isGrpCus(String cusId) throws ComponentException{
		CusBaseRelComponent component = (CusBaseRelComponent)this.getComponent("CusComIntellectRel");
		return component.isGrpCus(cusId);
	}
	
	public List<String> getAutoCusGrp(String cusId) throws ComponentException{
		CusBaseRelComponent component = (CusBaseRelComponent)this.getComponent("CusComIntellectRel");
		return component.getCusComRelList(cusId);
	}
	
	public List<String> getComRelCus(String cusId) throws EMPException{
		
		CusBaseRelComponent component = (CusBaseRelComponent)this.getComponent("CusComIntellectRel");
		return component.getComRelCus(cusId);
	}
	
}


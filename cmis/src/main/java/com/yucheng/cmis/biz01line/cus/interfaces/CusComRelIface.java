package com.yucheng.cmis.biz01line.cus.interfaces;



import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface CusComRelIface {
	/**
	 * 根据cusId判断该客户是否有系统自定义的集团客户
	 * @param cusId
	 * @return 如果有关联客户，返回true
	 * @throws ComponentException
	 */
	public boolean isInAutoCusGrp(String cusId) throws ComponentException;
	
	
	/**
	 * 是否是集团客户判断(嘉兴)
	 * @param cusId 客户编号
	 * @return
	 * @throws ComponentException
	 */
	public boolean isGrpCus(String cusId) throws ComponentException;
	
	/**
	 * 根据cusId取到关联客户的id
	 * @param cusId
	 * @return 如果有关联客户，返回true
	 * @throws ComponentException
	 */
	public List<String> getAutoCusGrp(String cusId) throws ComponentException;
	

	/**
	 * 根据cusId取得股东
	 * @param cusId
	 * @return 如果有关联客户，返回true
	 * @throws ComponentException
	 */
	public List<String> getComRelCus(String cusId) throws EMPException;
	
}

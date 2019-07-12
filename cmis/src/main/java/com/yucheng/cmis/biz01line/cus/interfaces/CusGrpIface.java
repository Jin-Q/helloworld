package com.yucheng.cmis.biz01line.cus.interfaces;

import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 
 *@Classname	CusBlkCheckinappIface
 *@Version  
 *@Since   	2009-6-27
 *@Copyright 	
 *@Description：
 *@Lastmodified 	2009-6-27
 */
public interface CusGrpIface {

	
	/**
	 *根据集团编号获取  集团信息domian
	 * @param  grpNo  集团编号
	 * @throws  ComponentException
	 * 
	 */
	public CusGrpInfo getCusGrpInfo(String  grpNo) throws Exception;
	
	/**
	 * 检查改客户是否是集团客户成员
	 * @param cusId	客户编号
	 * @return
	 * @throws ComponentException
	 */
	public boolean checkIsGrpMember(String cusId) throws Exception;
	
	/**
	 * 检查改客户是否是集团客户成员
	 * @param cusId	客户编号
	 * @return
	 * @throws ComponentException
	 */
	public String getIsGrpMember(String cusId) throws Exception;
}

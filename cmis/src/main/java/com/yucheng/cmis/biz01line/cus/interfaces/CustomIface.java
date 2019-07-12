package com.yucheng.cmis.biz01line.cus.interfaces;



import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;
/**
 * 
 *@Classname	CustomIface
 *@Version  
 *@Since   	2008-9-16
 *@Copyright 	
 *@Author 	wqgang	
 *@Description：提供与其他业务组件调用的方法。因为没有客户基类，所以对公对私的方法分开处理
 *@Lastmodified 2008-9-16
 *@Author wqgang
 */
public interface CustomIface {

	
	/**
	 * 根据客户码获得其客户基本信息Domain(无视客户类型，Domain中包含客户类型)
	 */
	public CusBase getCusBase(String cusId)throws ComponentException;
	
	/**
	 * 根据客户代码获取详细的对公客户信息
	 * @param  String cusId 客户代码
	 * @return CusIndiv 对公客户对象
	 * @throws EMPException	
	 */
	public CusCom getCusCom(String cusId) throws EMPException, AgentException;
	
	/**
	 * 根据客户代码获取详细的对私客户信息
	 * @param  String cusId 客户代码
	 * @return CusIndiv 对私客户对象
	 * @throws EMPException	
	 */
	public CusIndiv getCusIndiv(String cusId)throws EMPException;

	/**
	 * 根据客户码获得其所在的集团客户DOMAIN
	 * */
	public CusGrpInfo getGrpNoByCusId(String cusId) throws Exception;
	
	/**
	 * 修改客户信息根据CusBase对象更新数据库数据
	 * 
	 * @param CusBase
	 * @return String 返回String类型用于描述更新状态
	 * @throws EMPException
	 */
	public String modifyCusBase(CusBase cusBase) throws EMPException,ComponentException;
	
	/**
	 * 根据企业客户号查询对应关键人客户号列表
	 * @author loujc
	 * @throws ComponentException
	 */
	public List<String> queryRelCusIdListByCusId(String cusId) throws ComponentException;

	/**
	 * 通过condition 查询不宜客户情况
	 * @param certType
	 * @param certCode
	 * @return
	 * @throws EMPException
	 */
	public List<CMISDomain> getCusBlkListByCondition(String condition)throws EMPException;

	
	/**
	 * 根据客户号获取其所在集团的DOMAIN
	 * */
	public CusGrpInfo getCusGrpInfoDomainByCusId(String cus_id) throws ComponentException;
	
	/**
	 * 根据CUS_ID,CUS_TYPE获取企业客户的高管信息中的法人代表或个人客户的社会信息中的配偶信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId,String cusType) throws ComponentException;
	
	public CusGrpInfo getCusGrpInfoDomainByGrpNo(String grpNo) throws Exception;
	/**
	 * 根据CUS_ID,获取企业客户的实际控制人
	 * @author xukaixi
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryControlRelCusListByCusId(String cus_id) throws Exception;
}

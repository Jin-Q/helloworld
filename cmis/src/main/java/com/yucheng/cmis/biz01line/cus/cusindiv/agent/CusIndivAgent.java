package com.yucheng.cmis.biz01line.cus.cusindiv.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.dao.CusIndivDao;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivSocRel;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 *@Classname CusIndivAgent
 *@Version 0.1
 *@Since 2008-9-27
 *@Copyright yuchengtech
 *@Author wqgang
 *@Description：本类主要处理对私客户基本信息相关业务数据
 *@Lastmodified 2008-9-27
 *@Author wqgang
 */
public class CusIndivAgent extends CMISAgent {

	/**
	 * 插入客户基本信息 (使用的是父类的方法的代码)
	 * 
	 * @param CusIndiv 客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public String insert(CusIndiv cusIndiv) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息

		// 新增客户信息
		int intMessage = this.insertCMISDomain(cusIndiv, PUBConstant.CUSINDIV);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;
		}
		return strMessage;
	}

	/**
	 * 根据客户ID删除客户基本信息
	 * 
	 * @param cusId 客户基本信息
	 * @return 暂定返回null 表示成功 其他表示异常
	 * @throws EMPException
	 */
	public String delete(String cusId) throws EMPException {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息

		// 创建MAP存储 高管家族成员客户代码 和 客户代码
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);

		// 根据主键删除客户信息
		int count = this.removeCMISDomainByKeywords(PUBConstant.CUSINDIV,pk_values); // 1成功 其他失败
		// 如果失败，给标志信息赋值
		if (1 == count) {
			strMessage = CMISMessage.SUCCESS; // 成功
		}

		return strMessage;
	}

	// 根据CUS_ID删除配偶信息
	public int deleteSpsByCusId(String cusId) throws Exception {
		CusIndivDao cusIndivDao = (CusIndivDao)this.getDaoInstance("CusIndiv");
		Connection conn = this.getConnection();
		int intReturnMessage = 0;
		try {
			intReturnMessage = cusIndivDao.deleteSpsByCusId(cusId, conn);
		} catch (EMPException e) {
			throw e;
		}
		return intReturnMessage;
	}

	/**
	 * 根据客户ID查找其基本信息
	 * 
	 * @param cusId
	 *            客户ID
	 * @return 客户基本信息
	 * @throws EMPException
	 */
	public CusIndiv findCusIndivByCusId(String cusId) throws EMPException {
		CusIndiv cusIndiv = new CusIndiv();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		cusIndiv = (CusIndiv) this.findCMISDomainByKeywords(cusIndiv,PUBConstant.CUSINDIV, pk_values);

		return cusIndiv;
	}

	public String checkSPS(String cusId) throws Exception {
		Connection conn = null;
		conn = this.getConnection();
//		CusIndivDao cusIndivDao = new CusIndivDao();
		CusIndivDao cusIndivDao = (CusIndivDao)this.getDaoInstance("CusIndiv");
		return cusIndivDao.checkSPS(cusId, conn);
	}

	/**
	 * 根据证件类型，证件号码从CUS_BANK_RELATION表取出的"客户与我行关系"信息
	 * 
	 * @param String certTyp 证件类型
	 * @param String certCode 证件号码
	 * @return String cusBankRel 客户与我行关系
	 * @throws ComponentException
	 * added by tzb     2009-12-24
	 */
	public String getIndivCusBankRelat(String certTyp, String certCode)
			throws Exception {
		// 客户与我行关系
		String cusBankRelat = null;
		Connection conn = null;
		conn = this.getConnection();
//		CusIndivDao cusIndivDao = new CusIndivDao();
		CusIndivDao cusIndivDao = (CusIndivDao)this.getDaoInstance("CusIndiv");
		cusBankRelat = cusIndivDao.getIndivCusBankRelat(certTyp, certCode,conn);
		return cusBankRelat;
	}
	
	/**
	 * 根据关键人客户编号查询所在企业信息中的配偶信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId) throws ComponentException {
		List<CMISDomain> list = new ArrayList<CMISDomain>();
		list = this.findListByCondition(CusIndivSocRel.class, " where cus_id = '"+cusId+"' and indiv_cus_rel = '1'");
		return list;
	}
	/**
	 * 2015-01-28 根据客户码更新手机号
	 * @param cusId
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public int updateMobileByCusId(String cusId,String mobile) throws Exception {
		CusIndivDao cusIndivDao = (CusIndivDao)this.getDaoInstance("CusIndiv");
		Connection conn = this.getConnection();
		int intReturnMessage = 0;
		try {
			intReturnMessage = cusIndivDao.updateMobileByCusId(cusId, mobile, conn);
		} catch (EMPException e) {
			throw e;
		}
		return intReturnMessage;
	}
}

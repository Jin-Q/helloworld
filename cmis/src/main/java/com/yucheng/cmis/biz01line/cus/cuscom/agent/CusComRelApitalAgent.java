package com.yucheng.cmis.biz01line.cus.cuscom.agent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.dao.CusComRelApitalDao;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComRelApital;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 
 * @Classname CusComAgent.java
 * @Version 1.0
 * @Since 1.0 Sep 12, 2008 4:55:31 PM
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：本类主要代理客户基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */

public class CusComRelApitalAgent extends CMISAgent {

	/**
	 * 插入客户基本信息 (使用的是父类的方法的代码)
	 * 
	 * @param cusCom
	 *            客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public String insert(CusComRelApital cusComRelApital) throws AgentException {
		String strMessage = CMISMessage.ADDDEFEAT; // 错误信息
		// 新增关联关系！
		int intMessage = this.insertCMISDomain(cusComRelApital,
				PUBConstant.CUSCOMRELAPITAL);
		if (1 == intMessage) {
			strMessage = CMISMessage.ADDSUCCEESS;// "添加成功！";
		}
		return strMessage;

	}

	/*
	 * 新增时候的验证出资比例
	 */
	public String getTotelInvtPerc(CusComRelApital pdomain) throws EMPException {
		String strMessage = "1"; // 默认出资比例大于1
		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
		Connection conn = this.getConnection();
		double invtPerc = cusComRelApitalDao.getTotelInvtPerc(pdomain
				.getCusId(), conn);
		if ((invtPerc + pdomain.getInvtPerc()) <= 1) {
			// 加上这次的出资比例也不超过1，可以新增
			strMessage = "0";
		}
		return strMessage;

	}

	/**
	 * 检查资本构成总和是否超过注册资本
	 * 
	 * @param pdomain
	 * @return
	 * @throws EMPException
	 */
//	public String checkTotalInvtAmt(CusComRelApital pdomain)
//			throws EMPException {
//		String strMessage = "n";
//		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
//		Connection conn = this.getConnection();
//		// 获得客户现有资本构成总和
//		double invtAmt = cusComRelApitalDao.getTotalInvtAmt(pdomain.getCusId(),
//				conn);
//		// 获得客户现有的注册资本
//		double regCapAmt = cusComRelApitalDao.getRegCapAmt(pdomain.getCusId(),
//				conn);
//		// 新增的资本构成资金
//		double newInvtAmt = pdomain.getInvtAmt();
//
//		if (regCapAmt < (invtAmt + newInvtAmt)) {
//			strMessage = "y";
//		}
//		return strMessage;
//	}
//
//	public String checkUpdateTotalInvtAmt(CusComRelApital pdomain)
//			throws EMPException {
//		String strMessage = "n";
//		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
//		Connection conn = this.getConnection();
//		// 获得客户现有资本构成总和
//		double invtAmt = cusComRelApitalDao.getTotalInvtAmt(pdomain.getCusId(),
//				conn);
//		// 获得客户现有的注册资本
//		double regCapAmt = cusComRelApitalDao.getRegCapAmt(pdomain.getCusId(),
//				conn);
//		// 原有出资金额
//		double oldAmt = cusComRelApitalDao.getUpdateTotalInvtAmt(pdomain.getCusId(), pdomain.getCusIdRel(), conn);
//		// 新增的资本构成资金
//		double newInvtAmt = pdomain.getInvtAmt();
//
//		if (regCapAmt < (invtAmt - oldAmt + newInvtAmt)) {
//			strMessage = "y";
//		}
//		return strMessage;
//	}

	/*
	 * 修改时候的验证出资比例
	 */
	public String getUpdateTotelInvtPerc(CusComRelApital pdomain)
			throws EMPException {
		String strMessage = "1"; // 默认出资比例大于1
		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
		Connection conn = this.getConnection();
		Map<String,Double> values = cusComRelApitalDao.getUpdateTotelInvtPerc(pdomain.getCusId(), pdomain.getCusIdRel(), conn);
		double totel = (Double) values.get("totelInvtPerc");
		double invtPercOld = (Double) values.get("invt_perc");
		double invtPercNew = (Double) pdomain.getInvtPerc();
		if ((totel - invtPercOld + invtPercNew) <= 1) {
			// 加上这次的出资比例也不超过1，可以新增
			strMessage = "0";
		}
		return strMessage;
	}

	public CusComRelApital checkExist(String cusId, String cusIdRel) throws AgentException {
		CusComRelApital pCusComRelApital = new CusComRelApital();
		String modelId = PUBConstant.CUSCOMRELAPITAL;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		pk_values.put("cus_id_rel", cusIdRel);
		CusComRelApital toDb = new CusComRelApital();
		pCusComRelApital = (CusComRelApital) this.findCMISDomainByKeywords(toDb, modelId, pk_values);
		return pCusComRelApital;
	}
	
	/**
	 * 根据客户ID获得实收注册资金,币种
	 * @param cusId
	 * @return
	 * @throws EMPException
	 */
	public Map getRegAmt(String cusId) throws EMPException{
		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
		Connection conn = this.getConnection();
		return cusComRelApitalDao.getRegCapAmt(cusId, conn);
	}
	
	public CusComRelApital getCusComApital(String cusId,String cusIdRel) throws EMPException{
		CusComRelApital ccra = new CusComRelApital();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		pk_values.put("cus_id_rel", cusIdRel);
		ccra = (CusComRelApital) this.findCMISDomainByKeywords(ccra,PUBConstant.CUSCOMRELAPITAL, pk_values);
		return ccra;
	}
	
	public double getSumPerc(String cusId) throws EMPException{
		CusComRelApitalDao cusComRelApitalDao = new CusComRelApitalDao();
		Connection conn = this.getConnection();
		return cusComRelApitalDao.getTotelInvtPerc(cusId, conn);
	}
}

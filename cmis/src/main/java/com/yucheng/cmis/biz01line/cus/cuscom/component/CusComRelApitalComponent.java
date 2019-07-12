package com.yucheng.cmis.biz01line.cus.cuscom.component;

import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusComRelApitalAgent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComRelApital;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 
 * @Classname CusComComponent
 * @Version 0.1
 * @Since 2008-9-18
 * @Copyright yuchengtech
 * @Author wqgang
 * @Description：
 * @Lastmodified 2008-9-18
 * @Author wqgang
 */
public class CusComRelApitalComponent extends CMISComponent {

	/**
	 * 添加对启用公客户信息基 与数据库无关的校验都已经在前端处理完成了，所以在组件中暂不考虑 而与主键相关的有DAO处理
	 * 
	 * @param CusCom
	 *            对公客户基本信息
	 * @return Sring 方法执行状态码
	 * @throws EMPException
	 */

	public String addCusComRelApital(CusComRelApital pdomain)
			throws EMPException, ComponentException {
		// 返回信息
		String strReturnMessage = CMISMessage.ADDDEFEAT;
		CusComRelApital fromDb = null;
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
				.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		// 查询出资人的 证件类型+证件号码 是否已经存在
		fromDb = cusComRelApitalAgent.checkExist(pdomain.getCusId(), pdomain.getCusIdRel());
		if (fromDb.getCusId() == null) {
			// 新增
			strReturnMessage = cusComRelApitalAgent.insert(pdomain);
		} else {
			// 已存在，不允许新增.
			strReturnMessage = "yes";
		}

		return strReturnMessage;
	};

	/*
	 * 新增时候的验证出资比例
	 */
	public String cheakInvtPerc(CusComRelApital pdomain) throws EMPException,
			ComponentException {
		// 返回信息,默认为"1"，不可以新增
		String strReturn = "1";
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
				.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		strReturn = cusComRelApitalAgent.getTotelInvtPerc(pdomain);

		return strReturn;
	};

	/**
	 * 新增时检查资本构成总和
	 * 
	 * @param pdomain
	 * @return
	 * @throws EMPException
	 * @throws ComponentException
	 */
//	public String checkTotalInvtAmt(CusComRelApital pdomain)
//			throws EMPException, ComponentException {
//		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
//				.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
//		return cusComRelApitalAgent.checkTotalInvtAmt(pdomain);
//	}
//
//	public String checkUpdateTotalInvtAmt(CusComRelApital pdomain)
//			throws EMPException, ComponentException {
//		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
//				.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
//		return cusComRelApitalAgent.checkUpdateTotalInvtAmt(pdomain);
//	}

	/*
	 * 修改时候的验证出资比例
	 */
	public String cheakUpdateInvtPerc(CusComRelApital pdomain)
			throws EMPException, ComponentException {
		// 返回信息,默认为"1"，不可以修改
		String strReturn = "1";
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
				.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		strReturn = cusComRelApitalAgent.getUpdateTotelInvtPerc(pdomain);

		return strReturn;
	};
	
	public Map getRegAmt(String cusId) throws EMPException{
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
		.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		return cusComRelApitalAgent.getRegAmt(cusId);
	}
	
//	public double getSumInvrtAmt(String cusId) throws EMPException{
//		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
//		.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
//		return cusComRelApitalAgent.getSumInvrtAmt(cusId);
//	}
	
	//根据主键查询该条投资信息是否存在
	public CusComRelApital getCusComRelApital(String cusId,String cusIdRel) throws EMPException{
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
		.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		return cusComRelApitalAgent.getCusComApital(cusId,cusIdRel);
	}
	
	public double getSumPerc(String cusId) throws EMPException{
		CusComRelApitalAgent cusComRelApitalAgent = (CusComRelApitalAgent) this
		.getAgentInstance(PUBConstant.CUSCOMRELAPITAL);
		return cusComRelApitalAgent.getSumPerc(cusId);
	}
}

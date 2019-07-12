package com.yucheng.cmis.biz01line.cus.cusindiv.component;

import java.util.List;

import com.dc.eai.aop.ThrowsAdvice;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.agent.CusIndivAgent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusIndivComponent extends CMISComponent {
	
	/**
	 * 通过客户代码和查询条件获取cusIndiv对象 condition的处理方式根据表模型的DAO来具体确定，目前暂不考虑
	 * 
	 * @param cusId 客户代码
	 * @param condition 查询条件
	 * @return CusIndiv 对私客户对象
	 * @throws AgentException
	 * @throws EMPException
	 */
	public CusIndiv getCusIndiv(String CusId) throws EMPException, ComponentException, AgentException {
		CusIndiv cusIndiv = new CusIndiv();
		if (CusId == null || CusId.trim().length() == 0) {
		} else {
			// 创建对应的代理类
			CusIndivAgent cusIndivAgent = (CusIndivAgent) this.getAgentInstance(PUBConstant.CUSINDIV);
			cusIndiv = cusIndivAgent.findCusIndivByCusId(CusId);
		}
		return cusIndiv;
	};

	public String checkSPS(String cusId) throws Exception {
		// 创建对应的代理类
		CusIndivAgent cusAgent = (CusIndivAgent) this
				.getAgentInstance(PUBConstant.CUSINDIV);
		return cusAgent.checkSPS(cusId);
	}

	// 根据CUS_ID删除配偶信息
	public int deleteSpsByCusId(String cusId) throws Exception {
		CusIndivAgent cusAgent = (CusIndivAgent) this
				.getAgentInstance(PUBConstant.CUSINDIV);
		int intReturnMessage = 0;
		try {
			intReturnMessage = cusAgent.deleteSpsByCusId(cusId);
		} catch (EMPException e) {
			throw e;
		}
		return intReturnMessage;
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

		// 创建对应的代理类
		CusIndivAgent cusAgent = (CusIndivAgent) this
				.getAgentInstance(PUBConstant.CUSINDIV);
		cusBankRelat = cusAgent.getIndivCusBankRelat(certTyp, certCode);
		return cusBankRelat;
	}

	/**
	 * 根据CUS_ID,CUS_TYPE获取企业个人客户的社会信息中的配偶信息
	 * @author zwhu
	 * @throws ComponentException 
	 */
	public List<CMISDomain> queryRelCusListByCusId(String cusId,String cusType)
			throws ComponentException {
		CusIndivAgent cusAgent = (CusIndivAgent) this.getAgentInstance(PUBConstant.CUSINDIV);
		return cusAgent.queryRelCusListByCusId(cusId);
	}
	public int updateMobileByCusId(String cusId,String mobile) throws Exception{
		CusIndivAgent cusAgent = (CusIndivAgent) this.getAgentInstance(PUBConstant.CUSINDIV);
		int i = 0;
		try {
			i=cusAgent.updateMobileByCusId(cusId, mobile);
		} catch (Exception e) {
			throw e;
		}
		return i;
	}

}
package com.yucheng.cmis.biz01line.cus.cuscom.component;

import java.sql.SQLException;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.agent.CusObisDepositAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/** 
 * 
 * @author zhoujf
 * @version 1.0
 * @copyright yuchengtech
 * @since 2009-07-03
 * 
 */
public class CusObisDepositComponent extends CMISComponent {
	/**
	 * 
	 * @param cusId
	 *            客户号
	 * @param orgName
	 *            行名
	 * @param acctNo
	 *            帐号
	 * @return <p>
	 *         yes:CusObisDeposit表中客户号为cusId的客户在orgName中没有新增帐号,
	 *         </p>
	 *         <p>
	 *         no:CusObisDeposit表中客户号为cusId的客户在orgName中已经新增帐号
	 *         </p>
	 * @throws EMPException
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public String checkExist(String accNo) throws Exception {
		CusObisDepositAgent cusObisDepositAgent = (CusObisDepositAgent)this.getAgentInstance(PUBConstant.CUSOBISDEPOSIT);
		return (cusObisDepositAgent.checkExsit(accNo));
	}
}

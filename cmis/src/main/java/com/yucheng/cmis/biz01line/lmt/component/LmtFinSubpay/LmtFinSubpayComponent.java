package com.yucheng.cmis.biz01line.lmt.component.LmtFinSubpay;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.lmt.agent.LmtFinSubpay.LmtFinSubpayAgent;
import com.yucheng.cmis.pub.CMISComponent;

public class LmtFinSubpayComponent extends CMISComponent {

	public double getTotalSubpay(String serno, Connection conn)
			throws EMPException, SQLException {
		LmtFinSubpayAgent agent = (LmtFinSubpayAgent) this
				.getAgentInstance("LmtSubpayList");
		return agent.getTotalSubpay(serno, conn);
	}

	public double getSubpayTimes(String serno, Connection conn)
			throws EMPException, SQLException {
		LmtFinSubpayAgent agent = (LmtFinSubpayAgent) this
				.getAgentInstance("LmtSubpayList");
		return agent.getSubpayTimes(serno, conn);
	}

}

package com.yucheng.cmis.biz01line.lmt.agent.LmtFinSubpay;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.lmt.dao.LmtFinSubpay.LmtFinSubpayDao;
import com.yucheng.cmis.pub.CMISAgent;

public class LmtFinSubpayAgent extends CMISAgent {
	public double getTotalSubpay(String serno, Connection conn)
			throws EMPException, SQLException {
		LmtFinSubpayDao dao = (LmtFinSubpayDao) this
				.getDaoInstance("LmtSubpayList");
		return dao.getTotalSubpay(serno, conn);
	}

	public double getSubpayTimes(String serno, Connection conn)
			throws EMPException, SQLException {
		LmtFinSubpayDao dao = (LmtFinSubpayDao) this
				.getDaoInstance("LmtSubpayList");
		return dao.getSubpayTimes(serno, conn);
	}
}

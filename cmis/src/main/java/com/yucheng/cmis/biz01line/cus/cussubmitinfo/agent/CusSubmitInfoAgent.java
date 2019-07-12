package com.yucheng.cmis.biz01line.cus.cussubmitinfo.agent;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.biz01line.cus.cussubmitinfo.dao.CusSubmitInfoDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISDaoFactory;

public class CusSubmitInfoAgent extends CMISAgent {

	private final String comId = "CusSubmitInfo";
	
	public String getCurOrgLeastTaskUser(Context context, Connection connection) throws Exception {
		CusSubmitInfoDao crdDao = (CusSubmitInfoDao)
		CMISDaoFactory.getDaoFactoryInstance().getDaoInstance(comId);
		
		return crdDao.getCurOrgLeastTaskUser(context,connection);
	}

}

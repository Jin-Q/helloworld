package com.yucheng.cmis.biz01line.cus.cuscom.agent;

import com.yucheng.cmis.biz01line.cus.cusindiv.dao.CusIndivDao;
import com.yucheng.cmis.pub.CMISAgent;

/**
 * 
 * @author zhoujf
 * @version 1.0
 * @copyright yuchengtech
 * @since 2009-07-03
 *
 */
public class CusObisDepositAgent extends CMISAgent{
	public String checkExsit(String accNo) throws Exception {
        CusIndivDao cusIndivDao=new CusIndivDao();
        return cusIndivDao.checkExistCusObisDeposit(accNo, this.getConnection());
	}
}

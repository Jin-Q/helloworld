package com.yucheng.cmis.biz01line.cus.cusindiv.agent;

import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivIncome;
import com.yucheng.cmis.pub.CMISAgent;
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

public class CusIndivIncomeAgent extends CMISAgent {
	

	/**
	 * 插入客户基本信息 (使用的是父类的方法的代码)
	 * 
	 * @param cusCom
	 *            客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public CusIndivIncome checkExist(CusIndivIncome cusIndivIncome) throws AgentException {
		CusIndivIncome pCusIndivIncome= new CusIndivIncome();
		String modelId = PUBConstant.CUSINDIVINCOME;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusIndivIncome.getCusId());	
		pk_values.put("indiv_sur_year", cusIndivIncome.getIndivSurYear());
		pk_values.put("indiv_deposits", cusIndivIncome.getIndivDeposits());
		pCusIndivIncome = (CusIndivIncome)this.findCMISDomainByKeywords(cusIndivIncome, modelId, pk_values);
		return pCusIndivIncome;

	}
}

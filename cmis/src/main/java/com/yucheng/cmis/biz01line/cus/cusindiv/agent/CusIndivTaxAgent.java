package com.yucheng.cmis.biz01line.cus.cusindiv.agent;

import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndivTax;
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

public class CusIndivTaxAgent extends CMISAgent {
	

	/**
	 * 插入客户基本信息 (使用的是父类的方法的代码)
	 * 
	 * @param cusCom
	 *            客户基本信息
	 * @return String 暂定返回null 表示成功 其他表示异常
	 * @throws Exception
	 */
	public CusIndivTax checkExist(CusIndivTax cusIndivTax) throws AgentException {
		CusIndivTax pcusIndivTax= new CusIndivTax();
		String modelId = PUBConstant.CUSINDIVTAX;
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusIndivTax.getCusId());	
		pk_values.put("indiv_taxes", cusIndivTax.getIndivTaxes());
		pcusIndivTax = (CusIndivTax)this.findCMISDomainByKeywords(cusIndivTax, modelId, pk_values);
		return pcusIndivTax;

	}
}

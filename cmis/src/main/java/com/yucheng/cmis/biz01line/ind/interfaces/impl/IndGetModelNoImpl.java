package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.util.HashMap;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.interfaces.IndGetModelNoIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IndGetModelNoImpl  extends CMISComponent implements IndGetModelNoIface
	{
	/**
	 * 根据客户类型，企业规模，业务品种，财务报表类型返回模型编号
	 * @param custype
	 * @param com_opt_scale
	 * @param com_biz_kind
	 * @return
	 */
	public String getModelNo(HashMap<String,String> hm)throws ComponentException { 
		 
		IndComponent indcom = (IndComponent) this
				.getComponent(IndPubConstant.IND_COMPONENT); 
		return  indcom.getModelNoForCcr(hm); 
		 
	}

	public String getModelScore(String modelNo) throws ComponentException {
		// TODO Auto-generated method stub
		IndComponent indcom = (IndComponent) this
			.getComponent(IndPubConstant.IND_COMPONENT); 
		return  indcom.getModelAllScore(modelNo); 
	}

}

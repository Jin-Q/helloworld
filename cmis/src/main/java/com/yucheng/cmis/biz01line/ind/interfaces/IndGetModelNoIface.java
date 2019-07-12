package com.yucheng.cmis.biz01line.ind.interfaces;

import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndGetModelNoIface {
	/**
	 * 根据客户类型，企业规模，业务品种，财务报表类型返回模型编号
	 * @param custype
	 * @param com_opt_scale
	 * @param com_biz_kind
	 * @return
	 */
	public String getModelNo(HashMap<String,String> hm) throws ComponentException;
	
	/**
	 * 根据模型编号 得到该模型的总得分
	 * @param modelNo	模型编号
	 * @return
	 * @throws ComponentException
	 */
	public String getModelScore(String modelNo) throws ComponentException;
	
}

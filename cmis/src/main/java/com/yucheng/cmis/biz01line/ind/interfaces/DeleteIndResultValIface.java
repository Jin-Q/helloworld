package com.yucheng.cmis.biz01line.ind.interfaces;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface DeleteIndResultValIface {
	/**
	 * 根据编号，日期删除指标结果表里面的记录
	 * @param cus_id  编号
	 * @param app_begin_date 日期
	 * @return
	 * @throws ComponentException
	 */
	public int deleteIndResultVal(String cus_id,String app_begin_date) throws ComponentException;
}

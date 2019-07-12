/*
 * Yangfei's New Files
 *
 * Create on 2009-4-7
 * Copyrigh 2005 Evergreen International Corp.
 */

package com.yucheng.cmis.biz01line.ind.interfaces;

import com.yucheng.cmis.pub.exception.ComponentException;


public interface GetIndOptionDescIface {
	/**
	 * 根据指标值，指标选项值获取指标名称
	 * @param indexNo  指标编号
	 * @param indexValue 指标选项值
	 * @return
	 * @throws ComponentException
	 */
	public String getIndOptionDesc(String indexNo,String indexValue) throws ComponentException;
}

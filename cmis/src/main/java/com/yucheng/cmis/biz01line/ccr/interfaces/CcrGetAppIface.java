package com.yucheng.cmis.biz01line.ccr.interfaces;

import java.util.List;

import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface CcrGetAppIface {
	
	
	/**
	 * 通过业务编号"serno",以及客户内部编码"cusId" 查询CcrAppDetail Domain对象
	 * @param serno
	 * @param cusId
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppDetail getCcrAppDetail(String serno,String cusId) throws ComponentException;
	/**
	 * 通过业务编号"serno", 查询批量评级明细CcrAppDetail Domain对象列表
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public List<CcrAppDetail> getCcrAppDetailList(String serno) throws ComponentException;
	
}

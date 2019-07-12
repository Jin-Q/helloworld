package com.yucheng.cmis.biz01line.ind.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndGroupScoreIface {
	/**
	 * 取组得分
	 * @param grpno 
	 * @param hm 组下各个指标的得分集
	 * @return
	 * @throws ComponentException
	 */
	public String getGrpScore(String grpno,HashMap hm,Connection conn) throws ComponentException;
}

package com.yucheng.cmis.biz01line.ind.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndIndexScoreIface {
	/**
	 * 取指标得分
	 * @param grpno 组编号
	 * @param indexno 指标编号
	 * @param indval 指标值
	 * @param conn  数据库连接
	 * @return
	 * @throws ComponentException
	 */
	public String getIndScore(String grpno,String indexno,String indval,HashMap<String,String> para,Connection conn) throws ComponentException;
}

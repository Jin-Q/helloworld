package com.yucheng.cmis.biz01line.ind.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;
/*
 * 风险分类非企事业模型得分计算
 */
public interface IndRscNotCusComMSIface {
	/**
	 * 计算模型得分
	 * @param modelno 模型编号
	 * @param hm 参数键值对
	 * @param conn 连接
	 * @return
	 * @throws ComponentException
	 */
	public HashMap<String,String> getModelScore(String modelno,HashMap hm,Connection conn) throws ComponentException;
}

package com.yucheng.cmis.biz01line.ind.interfaces;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndModelScoreIface {
	/**
	 * 取模型得分
	 * @param modelno
	 * @param hm  模型下各个组的得分集
	 * @return
	 * @throws ComponentException
	 */
	public String getModelScore(String modelno,HashMap hm,ArrayList list,Connection conn) throws ComponentException;
}

package com.yucheng.cmis.biz01line.ind.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface IndexValueIface {
	/**
	 * 取指标值
	 * @param hm
	 * @return
	 */
	public String getValue(HashMap<String,String> hm,Connection conn,Context context)throws ComponentException ;
	
}

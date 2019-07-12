package com.yucheng.cmis.pub.dao.config;

import java.util.HashMap;

public class SqlConfigBuffer {

	private static HashMap<String,SqlConfig> cfgBuf;
	
	static {
		cfgBuf = new HashMap<String,SqlConfig>();
	}
	
	public synchronized static SqlConfig getSqlConfigById(String sqlId){

		return cfgBuf.get(sqlId);
	}
	
	public synchronized static void setSqlConfig(String sqlId, SqlConfig sqlConfig){
		cfgBuf.put(sqlId, sqlConfig);
	}
	
}

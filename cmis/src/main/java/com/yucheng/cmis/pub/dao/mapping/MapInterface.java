package com.yucheng.cmis.pub.dao.mapping;

import java.util.HashMap;


public interface MapInterface {
	
	public void setObject2HashMap(Object _vobj, HashMap<String,Object> _mapdata) throws Exception;
	
	public void setHashMap2Object(HashMap<String,Object> _mapdata, Object _vobj) throws Exception;
	
	public String getFieldType(String fieldId, Object obj);
}

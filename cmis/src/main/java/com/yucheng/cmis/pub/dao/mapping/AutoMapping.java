package com.yucheng.cmis.pub.dao.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * 	自定义Domain自动映射
 * 	以前版本需要手工生成自定义的Domain的映射，在系统启动的时候加载映射类，在SqlClient被调用
 * </p>
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
public class AutoMapping implements MapInterface {
	private Map<String, String> typeMap = new HashMap<String, String>();
	
	private AutoMapping(){
		
	}
	
	public static AutoMapping getInstance(){
		//注：因为有成员变量typeMap，不能是单例的，否则在并发情况下，typeMap中的数据会串
		return new AutoMapping();
	}
	
	
	/**
	 * <p>
	 * 	<h3>将obj转成Map<h3>	
	 * 	将对象obj中成员名以key的形式放在mapData中，将成员的值以value放在mapData中
	 * 	
	 * 	<ul>
	 * 	逻辑：
	 * 		通过反射机制，找到obj所有的成员变量，再高用该成员对应的get方法(boolean类型的成员方法不同),得到成员名和值，
	 * 		以key-value的形式放到mapData中		
	 * 	</ul>
	 * 
	 * 	<ol>注：这里不校验obj是否是基础类型，由调用程序来保证其是一个domain</ol>
	 * </p>
	 */
	public void setObject2HashMap(Object obj, HashMap<String, Object> mapData)throws Exception {
		if(mapData==null) mapData = new HashMap<String, Object>();
		try {
			Class c = null;
			if(obj instanceof String)
				c = Class.forName((String)obj).newInstance().getClass();
			else
				c = obj.getClass(); 
			 
			Field[] fields = c.getDeclaredFields();
			//得到所有成员属性
			for (int i = 0; i < fields.length; i++) {
				//成员名称
				String fieldName = fields[i].getName();
				String methodName = "";
				//boolean类型特殊理
				if(fields[i].getType().getName().equals("boolean")){
					methodName = "is"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				}else{
					methodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				}
				
				try {
					//成员变量对应的get方法
					Method m = c.getMethod(methodName, null);
					Object value = m.invoke(obj, null);
					mapData.put(fieldName, value);
				} catch (Exception e) {
					//考虑到定义了成员变量，没有对应的get方法，就略过该成员变量
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			throw new Exception(e);
		}

	}
	
	/**
	 * <p>
	 * 	<h3>将Map中的值赋给obj</h3>
	 * 	将Map中的key与obj中的成员映射，为obj赋值
	 * <ul>
	 * 	逻辑
	 * 		通过mapData的key
	 * </ul>
	 * 
	 * </p>
	 */
	public void setHashMap2Object(HashMap<String, Object> mapData, Object obj)throws Exception {
		try {
			Class c = null;
			if(obj instanceof String)
				c = Class.forName((String)obj).newInstance().getClass();
			else
				c = obj.getClass();
			
			Method[] methods = c.getMethods();
			for (Iterator iterator = mapData.keySet().iterator(); iterator.hasNext();) {
				String fieldName = (String) iterator.next();
				Object value = mapData.get(fieldName);
				//根据成员方法找到该对象成员的set方法	
				String methodName = "";
				methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				
				for (int i = 0; i < methods.length; i++) {
					if(methods[i].getName().equals(methodName)){
						methods[i].invoke(obj, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * 	返回该obj中成员的类型
	 * </p>
	 * @param fieldId 成员变量名
	 * @param 对象 
	 * @return String
	 */
	public String getFieldType(String fieldId, Object obj) {
		try {
			
			if(typeMap.size()==0){
				Class c = null;
				if(obj instanceof String)
					c = Class.forName((String)obj).newInstance().getClass();
				else
					c = obj.getClass();
				
				Field[] fields = c.getDeclaredFields();
				//得到所有成员属性
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					String type = field.getType().toString();
					typeMap.put(field.getName(), field.getType().getName());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return typeMap.get(fieldId);
	}
	
	public String getFieldType(String fieldId) {
		return null;
	}

}

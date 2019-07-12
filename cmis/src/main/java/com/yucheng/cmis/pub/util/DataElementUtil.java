package com.yucheng.cmis.pub.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;

public class DataElementUtil {
 
	 //判断obj是否KeyedCollection
	public static boolean isKcoll(Object obj){
		boolean isKcoll=false;
		try{
			if(obj instanceof KeyedCollection)
				isKcoll=true;
			
		}catch(Exception e){
			isKcoll=false;
		}
		 
		  
		
		return isKcoll;
	}
	 //判断obj是否KeyedCollection
	public static boolean isIcoll(Object obj){
		boolean isIcoll=false;
		try{
			if(obj instanceof IndexedCollection)
				isIcoll=true;
		}catch(Exception e){
			isIcoll=false;
		}
		 
		  
		
		return isIcoll;
	}
	
	/*desp:降congtext中的kcoll组装成参数串
	 * kColl 转换的kcoll
	 * destName kcoll的名字
	 * isEncode是否对#%&+\=?进行转码，如果true则需要解码 java.net.URLDecoder.decode(String)
	 * */
	public static String context2Str(KeyedCollection kColl,String destName,boolean isEncode) throws InvalidArgumentException, ObjectNotFoundException{
		 String params="";
		 if(destName==null)destName="";
		if(kColl!=null&&!kColl.isEmpty()&&kColl.size()>0){
			for(int i=0;i<kColl.size();i++){
				DataElement dataField=kColl.getDataElement(i);
				String filedName=dataField.getName();
				if(!(filedName!=null&&(filedName.equals("requestUrl")||filedName.equals("_ServletRequest")))){
					Object value=null; 
					 Object dataKcoll2=kColl.getDataElement(filedName);
					 String _value="";
					 if(!DataElementUtil.isKcoll(dataKcoll2)){
						  value=(Object)kColl.getDataValue(filedName);
						  if(value==null)value="";
						  if(destName!=null && !destName.equals("")){
							  if(isEncode)
							    _value="&"+destName+"."+filedName+"="+java.net.URLEncoder.encode(value.toString());
							  else _value="&"+destName+"."+filedName+"="+value.toString();
						  }
						  else {
							if(isEncode)
								 _value="&"+filedName+"="+java.net.URLEncoder.encode(value.toString());
							else  _value="&"+filedName+"="+value.toString();
						  }
						 
					 }else{
						 _value=context2Str((KeyedCollection)dataKcoll2,filedName,isEncode);
					 }
					 
					 params+=_value;
				}
				 
			}
		}
		 
		return params;
	}

	/*desp:降congtext中的kcoll组装成参数串
	 * kColl 转换的kcoll
	 * destName kcoll的名字
	 * isEncode是否对#%&+\=?进行转码，如果true则需要解码 java.net.URLDecoder.decode(String)
	 * */
	public static Map context2Map(KeyedCollection kColl,String destName,boolean isEncode) throws InvalidArgumentException, ObjectNotFoundException{
		 Map map=new HashMap();
		 if(destName==null)destName="";
		if(kColl!=null&&!kColl.isEmpty()&&kColl.size()>0){
			for(int i=0;i<kColl.size();i++){
				DataElement dataField=kColl.getDataElement(i);
				String filedName=dataField.getName();
				if(!(filedName!=null&&(filedName.equals("requestUrl")||filedName.equals("_ServletRequest")))){
					Object value=null; 
					 Object dataKcoll2=kColl.getDataElement(filedName);
					 if(!DataElementUtil.isIcoll(dataKcoll2)){
						 if(!DataElementUtil.isKcoll(dataKcoll2)){
							  value=(Object)kColl.getDataValue(filedName);
							  if(value==null)value="";
							  if(destName!=null && !destName.equals("")){
								  if(isEncode){
									  map.put(destName+"."+filedName, java.net.URLEncoder.encode(value.toString()));  
								  }else{
									  map.put(destName+"."+filedName,value.toString());
								  }
							  }
							  else {
								if(isEncode){
									 map.put(filedName, java.net.URLEncoder.encode(value.toString()));
								}else{
									map.put(filedName,value.toString());
								}
							  }
							 
						 }else{
							 map.putAll(context2Map((KeyedCollection)dataKcoll2,filedName,isEncode));
						 }
					 }
					 
					 
					 
				}
				 
			}
		}
		 
		return map;
	}
	
/*
 * 分解url action.do?a=b&c=d
 *   urlParam action名字
 */

	public static Map splitUrl(String url,String urlActionName){
		Map map=new HashMap();	
		String[] params = null;
		
		if(url!=null&& !url.trim().equals("")&&urlActionName!=null&& !urlActionName.trim().equals("")){
			 String action="";
			 int _ind=url.indexOf("?");
			 if(_ind!=-1){
				 action=url.substring(0, _ind);
				 url=url.substring(_ind+1);
			 }else{
				 action=url;
			 }
			 map.put(urlActionName, action);
		 }
		url=url.replaceAll("\\?", "&");	
		int ind=url.indexOf("&");
		if(ind!=-1){
			params=url.split("&");
		}
		if(params!=null&&params.length>0){
			for (int i = 0; i < params.length; i++) {
				try {
					String param = params[i];
					if (param != null && param.indexOf("=") > 0) {
						int indp = param.indexOf("=");
						String paramN = param.substring(0, indp);
						String paramV = param.substring(indp+1);
						if (!map.containsKey(paramN))
							map.put(paramN, paramV);
					}
				} catch (Exception e) {

				}

			}
		}
		 
		return map;
	}
	
	public static Map putMap2Map(Map map,Map childMap){
		if(map==null||childMap==null)return map;
		 Iterator it = childMap.entrySet().iterator();
         Map.Entry entry = null;
         if (it != null) {
			while (it.hasNext()) {
				entry = (Map.Entry) it.next();
			    if(map.containsKey(entry.getKey())){
			    	childMap.remove(entry.getKey());
			    }
			}
		}
         map.putAll(childMap);
		return map;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}

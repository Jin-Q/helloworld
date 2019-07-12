package com.yucheng.cmis.biz01line.cus.cusRelTree.util;

import java.util.HashMap;

import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
import com.yucheng.cmis.pub.util.StringUtil;

public class TreeUtil {

public static final String nodeTypeGg="TreeGg"; //实际控制人/高管关键人
public static final String nodeTypeFr="TreeFr";
public static final String nodeTypeZbgc="TreeZbgc";
public static final String nodeTypeDwtz="TreeDwtz";
public static final String nodeTypeQs="TreeQs";
public static final String cusCom="TreeCusCom";
public static final String cusIndiv="TreeCusIndiv";
public  static HashMap<String,CusRelTree> putMapObj(HashMap<String,CusRelTree> map,CusRelTree cusRelTree) throws Exception{
	try{

		//if(map==null)
		//	map=new HashMap<String,CusRelTree>();
		if(cusRelTree!=null&&cusRelTree.getNodeId()!=null&&!cusRelTree.getNodeId().trim().equals("")){
		    cusRelTree.setNewID(StringUtil.getRandomId());
		    //cusRelTree.setNewParentID(StringUtil.getRandomId());
			if(!map.containsKey(StringUtil.getRandomId()) ){
				 map.put(StringUtil.getRandomId(), cusRelTree);
			}
		}
		
	}catch(Exception e){
		e.printStackTrace();
		throw new Exception(e);
	} 
	
	return map;	
		
	}
	
}

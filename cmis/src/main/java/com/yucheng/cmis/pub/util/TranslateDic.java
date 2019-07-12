package com.yucheng.cmis.pub.util;


import java.util.Iterator;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.dic.CMISDataDicService;

public class TranslateDic {
       
	public String getCnnameByOpttypeAndEnname(KeyedCollection dictColl,String opttype,String enname) throws EMPException{
		IndexedCollection icol = null;
		boolean flag = false;
		String cnname = "";
		try {
			icol = (IndexedCollection)dictColl.getDataElement(opttype.trim());
			if( icol == null ){
				throw new EMPException("未找到『"+opttype+"』相关的字典项");
			}
			Iterator itr = icol.iterator();
			while( itr.hasNext() ){
				KeyedCollection kcol = (KeyedCollection)itr.next();
				if( enname.equals(kcol.get(CMISDataDicService.ATTR_ENNAME)) ){
					flag = true;
					cnname = (String)kcol.get(CMISDataDicService.ATTR_CNNAME);
					break;
				}
			}
			if( flag ){
				return cnname;
			}else{
			     return enname;
				//throw new EMPException("未找到『"+opttype+"』下『"+enname+"』的字典项");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("翻译字典项『"+opttype+"』时失败,"+e.getMessage());
		} 
	}
	
	public String getEnnameByOpttypeAndCnname(KeyedCollection dictColl, String opttype, String cnname) throws EMPException{
		IndexedCollection icol = null;
		boolean flag = false;
		String enname = "";
		try {
			if( cnname != null ) cnname = cnname.trim();
			else cnname = "";
			icol = (IndexedCollection)dictColl.getDataElement(opttype);
			if( icol == null ){
				throw new EMPException("未找到『"+opttype+"』相关的字典项");
			}
			Iterator itr = icol.iterator();
			while( itr.hasNext() ){
				KeyedCollection kcol = (KeyedCollection)itr.next();
				if( cnname.equals(kcol.get(CMISDataDicService.ATTR_CNNAME )) ){
					flag = true;
					enname = (String)kcol.get(CMISDataDicService.ATTR_ENNAME);
					break;
				}
			}
			if( flag ){
				return enname;
			}else{
				throw new EMPException("未找到『"+opttype+"』下『"+cnname+"』的字典项");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("翻译字典项『"+opttype+"』时失败,"+e.getMessage());
		} 
	}
}

package com.yucheng.cmis.pub;

import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;

/**
 * �ֵ���ݹ���
 * @author Crystal
 * Last Modified 2009-7-16
 */
public class DicUtil {
	/*
	 * ���Enname��ȡCnname
	 */
	static public String getCnnameByEnname(String enname, String opttype, Context context){
		try{
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
			IndexedCollection iColl = (IndexedCollection)kColl.getDataElement(opttype);
			Iterator it = iColl.iterator();
			while(it.hasNext()){
				KeyedCollection dic = (KeyedCollection)it.next();
				if(enname.equals(dic.getDataValue(CMISDataDicService.ATTR_ENNAME))){
					return (String)dic.getDataValue(CMISDataDicService.ATTR_CNNAME);					
				}
			}
		}catch(Exception e){}
		return enname;		
	}
	
	/*
	 * ���Cnname��ȡEnname
	 */
	static public String getEnnameByCnname(String cnname, String opttype, Context context){
		try{
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
			IndexedCollection iColl = (IndexedCollection)kColl.getDataElement(opttype);
			Iterator it = iColl.iterator();
			while(it.hasNext()){
				KeyedCollection dic = (KeyedCollection)it.next();
				if(cnname.equals(dic.getDataValue(CMISDataDicService.ATTR_CNNAME))){
					return (String)dic.getDataValue(CMISDataDicService.ATTR_ENNAME);					
				}
			}
		}catch(Exception e){}
		return cnname;
	}
	
	/*
	 * ����ʱ�����޸�context���ֵ����
	 */
	static public void setDicByOpttype(String enname, String cnname,
			String opttype, Context context){
		try{
			boolean exist = false;
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
			IndexedCollection iColl = (IndexedCollection)kColl.getDataElement(opttype);
			if(null == iColl){
				return;
			}			
			Iterator it = iColl.iterator();
			while(it.hasNext()){
				KeyedCollection dic = (KeyedCollection)it.next();
				if(enname.equals(dic.getDataValue(CMISDataDicService.ATTR_ENNAME))){
					exist = true;
					dic.setDataValue(CMISDataDicService.ATTR_CNNAME, cnname);
					break; //break While Loop
				}
			}
			if(!exist){ //ԭ4������ʱ������
				KeyedCollection dic = new KeyedCollection();
				dic.addDataField(CMISDataDicService.ATTR_ENNAME, enname);
				dic.addDataField(CMISDataDicService.ATTR_CNNAME, cnname);
				iColl.addDataElement(dic);
			}
		}catch(Exception e){}		
	}
	
	/*
	 * ����ʱ��context��ɾ���ֵ����
	 */
	static public void delDic(String enname, String opttype, Context context){
		try{
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
			IndexedCollection iColl = (IndexedCollection)kColl.getDataElement(opttype);
			Iterator it = iColl.iterator();
			while(it.hasNext()){
				KeyedCollection dic = (KeyedCollection)it.next();
				if(enname.equals(dic.getDataValue(CMISDataDicService.ATTR_ENNAME))){
					it.remove();										
				}
			}
		}catch(Exception e){}
	}	
}

package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;

public class SystemTransUtils {
	 /**
	   * 替换方式
	   */
	  public static final String REPL="replace";
	  /**
	   * 增加方式
	   */
	  public static final String ADD="add";
	/**
	 * 通用公共处理翻译
	 * @param icol 翻译对象icol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 翻译的名称对象
	 * @throws EMPException
	 */
	public  static void dealName(IndexedCollection icol,
			String[] arg,String mode,Context context,String[] modelId,String[]modelForeign,String[] fieldName) throws EMPException{
		for (int i = 0; i < icol.size(); i++) {

			KeyedCollection kCol = (KeyedCollection) icol.get(i);
			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							String str=getName(context,modelId[k],fieldName[k],modelForeign[k],(String)aField.getValue());
							if(str==null||"".equals(str)){
								str = (String)aField.getValue();
							}
							if(ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * 通用公共处理翻译
	 * @param kcol 翻译对象kcol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型 
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 用于取得翻译信息的字段名称
	 * @throws EMPException
	 */
	public  static void dealName(KeyedCollection kCol,
			String[] arg,String mode,Context context,String[] modelId,String[] modelForeign,String[] fieldName) throws EMPException{
			for(int j=0;j<kCol.size();j++){
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							//加转换函数
							String str=getName(context,modelId[k],fieldName[k],modelForeign[k],(String)aField.getValue());
							if(str==null||"".equals(str)){
								str = (String)aField.getValue();
							}
							if(ADD.equals(mode)){
								kCol.addDataField((String)aField.getName()+"_displayname", str);
							}else if(REPL.equals(mode)){
								aField.setValue(str);
							}
							
						}
					}
				}
			}
		}
	

	/**
	 * 根据字段id的取值查询表模型modelId中的字段name
	 * @param modelId
	 * @param Id
	 * @param value
	 * @return
	 * @throws EMPJDBCException 
	 */
	private static String getName(Context context,String modelId,String name, String Id,String value) throws EMPJDBCException {
		String rt=null;
		TableModelDAO dao=(TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		List<String> list = new ArrayList<String>();
		list.add(name);
		String condition="where "+Id+"='"+value+"'";
		KeyedCollection kColl=dao.queryFirst(modelId, list, condition, connection);
		try {
			rt=(String) kColl.getDataValue(name);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return rt;
	}
	
	/**
	 * 根据字段id的取值查询表模型modelId中的字段name
	 * @param modelId
	 * @param Id
	 * @param value
	 * @return
	 * @throws EMPJDBCException 
	 */
	private static String getName(Context context,String modelId,String name, Map<String, String> pkMap) throws EMPJDBCException {
		String rt="";
		TableModelDAO dao=(TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
		try {
			rt=(String) kColl.getDataValue(name);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return rt;
	}
	
	
	/**
	 * 通用公共处理翻译
	 * @param icol 翻译对象icol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 翻译的名称对象
	 * @throws EMPException
	 */
	public  static String dealQueryName(KeyedCollection queryData,
			String[] arg,Context context,String[] modelId,String[]modelForeign,String[] fieldName) throws EMPException{
		
		TableModelLoader modelLoader = (TableModelLoader) context.getService(CMISConstance.ATTR_TABLEMODELLOADER);
		StringBuffer conditionBuffer = new StringBuffer();
		String temp="";
		if(queryData!=null){
			for(int i=0;i<arg.length;i++){
				if (queryData.containsKey(arg[i]+"_displayname")) {
					TableModel model = modelLoader.getTableModel(modelId[i]);
					String tableName=model.getDbTableName();
					String dataValue = null;
					try {
						dataValue = (String) queryData.getDataValue(arg[i]+"_displayname");
					} catch (Exception e) {
					}
					temp +=getQueryCondition4Oracle(arg[i],tableName,modelForeign[i],fieldName[i],dataValue) ;
				}
				if(i<arg.length-1){
					temp+=" and ";
				}
			}
			
		}
		if(temp!=null&&temp.length()>0){
			if(temp.endsWith("and")){
				temp=temp.substring(0,temp.length()-3);
			}
			conditionBuffer.append("( ");
			conditionBuffer.append(temp);
			conditionBuffer.append(" )");
		}
		
        return conditionBuffer.toString();
	}
	
	/**
	 * 机构翻译
	 * @param kColl  默认翻译kColl中的bln_org
	 * @param context
	 */
	public static void containCommaORG2CN(KeyedCollection kColl,Context context){
		String fieldName = "belg_org";
		containCommaORG2CN(fieldName,kColl,context);
	}
	public static void containCommaORG2CN(String fieldName,KeyedCollection kColl,Context context){
		String coStr = "";
		try {
			coStr = (String) kColl.getDataValue(fieldName);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		containCommaORG2CN(coStr,context,kColl,fieldName);
	}
	/**
	 * 
	 * @param coStr 机构码  一个或用 "," 分隔的多个  JSP页面显示  *_displayname
	 * @param context
	 * @param kColl
	 * @param fieldName
	 * @return
	 */
	public static void containCommaORG2CN(String coStr,Context context, KeyedCollection kColl,String fieldName){
		String cnStr = "";
		TableModelDAO dao=(TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection(dataSource);
			if(coStr.indexOf(",") == -1){
				//直接翻译 ORGANNO   ORGANNAME
				List<String> l = new ArrayList<String>();
				l.add("organname");
				KeyedCollection kc = dao.queryFirst("SOrg", l, " where ORGANNO = '" + coStr + "'", connection);
				cnStr = (String) kc.getDataValue("organname");
			}else{
				for(int i=0; coStr.indexOf(",") != -1; i++){
					int index = coStr.indexOf(",");
					if(index == -1){
						index = coStr.length();
					}
					String organno = coStr.substring(0,index);
					List<String> l = new ArrayList<String>();
					l.add("organname");
					KeyedCollection kc = dao.queryFirst("SOrg", l, " where ORGANNO = '" + organno + "'", connection);
					cnStr += (String) kc.getDataValue("organname") + ",";
					
					coStr = coStr.substring(index+1);
				}
				List<String> l = new ArrayList<String>();
				l.add("organname");
				KeyedCollection kc = dao.queryFirst("SOrg", l, " where ORGANNO = '" + coStr + "'", connection);
				cnStr += (String) kc.getDataValue("organname");
			}
			kColl.addDataField(fieldName + "_displayname", cnStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 取得链表查询语句
	 * @param context
	 * @param queryField 本表外键
	 * @param modelId 查询的外表模型
	 * @param getField 查询的外表关联主键
	 * @param name 查询的翻译字段名称
	 * @param value 查询值
	 * @return
	 */
	private static String getQueryCondition4Oracle(String queryField,String tableName,String getField,String name,String value){
		
		String conndition= queryField+" in(select "+getField+" from "+tableName+" where "+name+" like '%"+value+"%') ";
		return conndition;
	}
	
	/**
	 * 指定翻译结果名称的翻译方法
	 * @param icol 翻译对象icol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 翻译的名称对象
	 * @param resultName 翻译结果的名称
	 * @throws EMPException
	 * @author by GC
	 */
	public static void dealPointName(IndexedCollection icol, String[] arg,String mode, Context context, String[] modelId,
			String[] modelForeign, String[] fieldName, String[] resultName) throws EMPException {
		for (int i = 0; i < icol.size(); i++) {
			KeyedCollection kCol = (KeyedCollection) icol.get(i);
			for (int j = 0; j < kCol.size(); j++) {
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							String str = getName(context, modelId[k],fieldName[k], modelForeign[k],(String) aField.getValue());
							if (ADD.equals(mode)) {
								kCol.put(resultName[k], str);
							} else if (REPL.equals(mode)) {
								aField.setValue(str);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 指定翻译结果名称的翻译方法
	 * @param kcol 翻译对象kcol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型 
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 用于取得翻译信息的字段名称
	 * @param resultName 翻译结果的名称
	 * @throws EMPException
	 * @author by GC
	 */
	public static void dealPointName(KeyedCollection kCol, String[] arg,String mode, Context context,
			String[] modelId,String[] modelForeign, String[] fieldName, String[] resultName) throws EMPException {
		for (int j = 0; j < kCol.size(); j++) {
			DataElement element = (DataElement) kCol.getDataElement(j);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				for (int k = 0; k < arg.length; k++) {
					if (arg[k].equals(aField.getName())) {
						String str = getName(context, modelId[k], fieldName[k],modelForeign[k], (String) aField.getValue());
						if (str == null || "".equals(str)) {
							str = "";
						}
						if (ADD.equals(mode)) {
							kCol.addDataField(resultName[k], str);
						} else if (REPL.equals(mode)) {
							aField.setValue(str);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 带联合主键的翻译处理 for kColl
	 * @param modelId 查询对象表模型
	 * @param modelForeign 对象表联合主键，多个字段以逗号隔开
	 * @param sourceNames 传入值
	 * @param fieldName 被翻译字段
	 * @param resultName 翻译结果名称
	 * @throws EMPException
	 * @author by GC20140106
	 */
	public static void dealUnionTrans(KeyedCollection kColl,Context context,String modelId,KeyedCollection TransKcoll,
			String sourceNames,String fieldName ,String resultName) throws EMPException {
		Map<String, String> pkMap = new HashMap<String, String>();
		for (int j = 0; j < kColl.size(); j++) {
			if (sourceNames.indexOf(",") > -1) {
				String[] sourceName = sourceNames.split(",");
				for (int i = 0; i < sourceName.length; i++) {
					String name = kColl.getDataElement(j).getName();
					if (sourceName[i].equals(name)) {
						String value = kColl.getDataValue(name).toString();
						pkMap.put(TransKcoll.getDataValue(name).toString(), value );
					}
				}
			}
		}
		String str = getName(context, modelId, fieldName,pkMap);
		str = (str == null)?"":str;
		if(resultName==null || resultName.equals("")){
			kColl.addDataField(fieldName+"_displayname", str);
		}else{
			kColl.addDataField(resultName, str);
		}
	}
	
	/**
	 * 带联合主键的翻译处理 for iColl
	 * @param modelId 查询对象表模型
	 * @param modelForeign 对象表联合主键，多个字段以逗号隔开
	 * @param sourceNames 传入值
	 * @param fieldName 被翻译字段
	 * @param resultName 翻译结果名称
	 * @throws EMPException
	 * @author by GC20140106
	 */
	public static void dealUnionTrans(IndexedCollection iColl,Context context,String modelId,KeyedCollection TransKcoll,
			String sourceNames,String fieldName ,String resultName) throws EMPException {		
		for (int i = 0; i < iColl.size(); i++) {
			KeyedCollection kColl = (KeyedCollection) iColl.get(i);
			dealUnionTrans(kColl, context, modelId, TransKcoll, sourceNames, fieldName, resultName);
		}		
	}
	
	/**
	 * 指定翻译结果名称的翻译方法，并扩展了传入条件的处理。
	 * @param icol 翻译对象icol
	 * @param arg 要翻译的对象
	 * @param mode 翻译模式 add:增加_displayname对象 replace：替换原对象
	 * @param context 资源上下文
	 * @param modelId 翻译需要查询的表模型
	 * @param modelForeign 表模型对应的外键
	 * @param fieldName 翻译的名称对象
	 * @param resultName 翻译结果的名称
	 * @param condition 条件
	 * @throws EMPException
	 * @author by GC20140304
	 */
	public static void dealPointNameByCondition(IndexedCollection icol, String[] arg,String mode, Context context, String[] modelId,
			String[] modelForeign, String[] fieldName, String[] resultName,String[] condition) throws EMPException {
		for (int i = 0; i < icol.size(); i++) {
			KeyedCollection kCol = (KeyedCollection) icol.get(i);
			for (int j = 0; j < kCol.size(); j++) {
				DataElement element = (DataElement) kCol.getDataElement(j);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					for (int k = 0; k < arg.length; k++) {
						if (arg[k].equals(aField.getName())) {
							String str = getNameByCondition(context, modelId[k],fieldName[k], modelForeign[k],(String) aField.getValue(),condition[k]);
							if (ADD.equals(mode)) {
								kCol.addDataField(resultName[k], str);
							} else if (REPL.equals(mode)) {
								aField.setValue(str);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 根据字段id的取值查询表模型modelId中的字段name
	 * 新增了condition处理 by GC20140304
	 * @throws EMPJDBCException 
	 */
	private static String getNameByCondition(Context context, String modelId,
			String name, String Id, String value,String conditions) throws EMPJDBCException {
		String rt=null;
		TableModelDAO dao=(TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		List<String> list = new ArrayList<String>();
		list.add(name);
		String condition="where "+Id+"='"+value+"'" + conditions;
		KeyedCollection kColl=dao.queryFirst(modelId, list, condition, connection);
		try {
			rt=(String) kColl.getDataValue(name);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		return rt;
	}
	
}
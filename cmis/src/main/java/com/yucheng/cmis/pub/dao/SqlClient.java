package com.yucheng.cmis.pub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.dao.config.SqlConfig;
import com.yucheng.cmis.pub.dao.config.SqlConfigBuffer;
import com.yucheng.cmis.pub.dao.mapping.AutoMapping;
import com.yucheng.cmis.pub.dao.mapping.MapInterface;
import com.yucheng.cmis.pub.util.NewStringUtils;


/**
 * 
 * <p>
 * 		命名SQL客户端
 * <p>
 * 
 * 注：
 * 	<ul>潜规则一：SQL的resultClass是domain时：数据库表字段名映射到代码中DOMAIN的域名，字段名中下加线 _ 之后的第一个字母大写，其余字母小写;去掉下加线，
 * 			如果domain的字段命名不符合该规则，将不会被赋值
 * <ul>
 * 
 *
 */
public class SqlClient {
	
	private static final String REP_LEFT_Exp = "${";
	private static final String REP_RIGHT_Exp = "}";
	private static final String LOGTYPE = "CMISDAO"; 
	/** 单个输入参数在SQL中的代号 (整句SQL的只有唯一个输入条件时)*/
	private static final String KEYCODE_PARAMETER_SINGLE = "_SIG_VALUE";
	
	/**
	 * <p>查询多条记录</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 符合条件记录集
	 * @throws Exception
	 */
	public static Object queryFirst(String sqlId, Object parameter, String[] conditionId, Connection conn) throws SQLException{
		Collection coData = queryList(sqlId, parameter, conditionId, 0, 0,conn);
		if(coData != null && coData.size() >= 0 && coData.iterator().hasNext()){
			return coData.iterator().next();
		}
		return null;
	}
	
	/**
	 * <p>查询多条记录</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 符合条件记录集
	 * @throws Exception
	 */
	public static Collection queryList(String sqlId, Object parameter, String[] conditionId, Connection conn) throws SQLException{
		return queryList(sqlId, parameter, conditionId, 0, 0, conn);
	}
	
	/**
	 * <p>查询多条记录</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conn 数据库连接
	 * @return 符合条件记录集
	 * @throws Exception
	 */
	public static Collection queryList(String sqlId, Object parameter, Connection conn) throws SQLException{
		return queryList(sqlId, parameter, null, 0, 0, conn);
	}
	
	/**
	 * <p>查询多条记录</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conditionId 可选条件ID
	 * @param start 返回记录启始条数
	 * @param end   返回记录结束条数
	 * @param conn 数据库连接
	 * @return 符合条件记录集
	 * @throws Exception
	 */
	public static Collection queryList(String sqlId, Object parameter, int start, int end, Connection conn) throws SQLException{
		return queryList(sqlId, parameter, null, start, end, conn);
	}
	/**
	 * <p>查询多条记录，返回ICOL集合</p>
	 * <p>注：只有返回值类型为com.ecc.emp.data.KeyedCollection</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conn 数据库连接
	 * @return 返回ICOL集合
	 * @throws SQLException
	 */
	public static IndexedCollection queryList4IColl(String sqlId, Object parameter, Connection conn) throws SQLException{
		Collection col = queryList(sqlId, parameter, null, 0, 0, conn);
		IndexedCollection iCol = new IndexedCollection();
		iCol.addAll(col);
		return iCol;
	}
	
	/**
	 * <p>查询多条记录，返回ICOL集合</p>
	 * <p>注：只有返回值类型为com.ecc.emp.data.KeyedCollection</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param start 返回记录启始条数
	 * @param end   返回记录结束条数
	 * @param conn 数据库连接
	 * @return 返回ICOL集合
	 * @throws SQLException
	 */
	public static IndexedCollection queryList4IColl(String sqlId, Object parameter, int start, int end, Connection conn) throws SQLException{
		Collection col = queryList(sqlId, parameter, null, start, end, conn);
		IndexedCollection iCol = new IndexedCollection();
		iCol.addAll(col);
		return iCol;
	}
	
	/**
	 * <p>查询多条记录，返回ICOL集合</p>
	 * <p>注：只有返回值类型为com.ecc.emp.data.KeyedCollection</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数
	 * @param conditionId 可选条件ID
	 * @param start 返回记录启始条数
	 * @param end   返回记录结束条数
	 * @param conn 数据库连接
	 * @return 返回ICOL集合
	 * @throws SQLException
	 */
	public static IndexedCollection queryList4IColl(String sqlId, Object parameter, String[] conditionId, int start, int end, Connection conn) throws SQLException{
		Collection col = queryList(sqlId, parameter, conditionId, start, end, conn);
		IndexedCollection iCol = new IndexedCollection();
		iCol.addAll(col);
		return iCol;
	}
	
	/**
	 * <p>查询多条记录，并将结果存至文件</p>
	 * @param sqlId
	 * @param parameter
	 * @param conditionId
	 * @param start
	 * @param end
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static String queryList2File(String sqlId, Object parameter, String[] conditionId, int start, int end, Connection conn)throws SQLException{
		
		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		/** 解释SQL配置 , 得到原始配置*/
		long star = System.currentTimeMillis();
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfig(sqlConfig, conditionId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/** 将OBJECT中的数据放至SQL中 */
		Collection colResult = null; //结果集
		String paramClassname = sqlConfig.getParameterClass();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Map _paramData = object2Map(parameter, paramClassname);;
			List<String> pL = digestParam(sqlOrigin);//得到SQL中所有的参数
			sqlOrigin = replaceValAtSql(sqlOrigin, pL, _paramData);
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL：" + sqlOrigin, null);
			//System.err.println("执行SQL：" + sqlOrigin);
			
			/** 执行SQL */			
		    ps = conn.prepareStatement(sqlOrigin);
		    int paramIdx = 1;
			for(int n=0; n<pL.size(); n++){
				String _key = pL.get(n);
				if(_key != null && !_key.trim().equals("") && _paramData.get(_key) != null){
			       ps.setObject(paramIdx++, _paramData.get(_key));
				}
			}
			
			if(start > 0 && end > 0){
				//如果设置了翻页
				ps.setInt(paramIdx++, start);
				ps.setInt(paramIdx++, end);
			}
			
			rs = ps.executeQuery();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
		}
		return "";
	}
	
	/**
	 * <p>查询多条记录</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入配置参数
	 * @param conditionId 可选条件ID
	 * @param start 返回记录启始条数
	 * @param end   返回记录结束条数
	 * @param conn 数据库连接
	 * @return 符合条件记录集
	 * @throws Exception
	 */
	public static Collection queryList(String sqlId, Object parameter, String[] conditionId, int start, int end, Connection conn) throws SQLException{

		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		/** 解释SQL配置 , 得到原始配置*/
		long star = System.currentTimeMillis();
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfigWithParamter(sqlConfig, parameter, conditionId);//判断是否空值
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/** 将OBJECT中的数据放至SQL中 */
		Collection colResult = null; //结果集
		String paramClassname = sqlConfig.getParameterClass();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Map _paramData = object2Map(parameter, paramClassname);;
			List<String> pL = digestParam(sqlOrigin);//得到SQL中所有的参数
			sqlOrigin = replaceValAtSql(sqlOrigin, pL, _paramData);
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL：" + sqlOrigin, null);
			//System.err.println("执行SQL：" + sqlOrigin);
			
			if(start > 0 && end > 0){
				//如果设置了翻页
				sqlOrigin = " select * from (  select T.*, rownum  rownum_ from (  " +
						sqlOrigin+" ) T  where rownum<= "+String.valueOf(end)+" ) WHERE rownum_ >=  "+String.valueOf(start);
			}
			/** 执行SQL */			
		    ps = conn.prepareStatement(sqlOrigin);
		    int paramIdx = 1;
			for(int n=0; n<pL.size(); n++){
				String _key = pL.get(n);
				if(_key != null && !_key.trim().equals("") && _paramData.get(_key) != null){
			       ps.setObject(paramIdx++, _paramData.get(_key));
				}
			}
			
			
			
			rs = ps.executeQuery();
			
			String resClassname = sqlConfig.getResultClass();
			String[] colNameList = null;
			
			int colCount = 1;
			if(!isBasicDataType(resClassname)){
				/** 非基本型，则使用 ResultSetMetaData 得到返回的字段*/
				ResultSetMetaData rmeta = rs.getMetaData();
				colCount = rmeta.getColumnCount();
				colNameList = new String[colCount];
				for(int c=1; c<=colCount; c++){
	
				   String fieldId = null;
				   if(resClassname.equals("com.ecc.emp.data.KeyedCollection")){
					   /** 对于KCOL,直接使用数据库字段名（小写） */
					   fieldId = rmeta.getColumnName(c).toLowerCase(); 
				   }else{
					   /** 对于非KCOL，将数据库字段名转成DOMINA的域名 */
					   fieldId = DBColumn2Field(rmeta.getColumnName(c));
				   }
 				   colNameList[c-1] = fieldId; 
				}
			}
	
			MapInterface mapping = AutoMapping.getInstance();
			String[] colType = null;
			
			if(!isBasicDataType(resClassname)
				       && !resClassname.equals("com.ecc.emp.data.KeyedCollection") && !resClassname.equals("java.util.HashMap")){
				
			    if(mapping == null){
			    	throw new Exception("系统没有加载类"+resClassname+"的映射类");
			    }
				colType = new String[colCount];
				for(int c=0; c<colCount; c++){
					colType[c] = mapping.getFieldType(colNameList[c],resClassname);
					
				}
			}
 		
			int count = 0;
			HashMap _TmpData = new HashMap(colCount,1f);
			
			Class classV = null;
			if(!isBasicDataType(resClassname) && !resClassname.equals("com.ecc.emp.data.KeyedCollection")){
			   classV = Class.forName(resClassname);
			}
			colResult = new ArrayList();
			while(rs.next()){
				count ++;
				if(!isBasicDataType(resClassname)){
					Object _vobj = null;
					if(resClassname.equals("com.ecc.emp.data.KeyedCollection")){
						 KeyedCollection _kcolData = new KeyedCollection();
						 for(int c=1; c<=colCount; c++){
						    //_TmpData.put(colNameList[c-1], rs.getObject(c));
							 _kcolData.addDataField(colNameList[c-1], rs.getObject(c));
						 }
						 _vobj = _kcolData;
					}else if(resClassname.equals("java.util.HashMap")){
						Map _retMap = new HashMap();
						for(int c=1; c<=colCount; c++){
							_retMap.put(colNameList[c-1], rs.getObject(c));
						}
						_vobj = _retMap;
					}else{
						 for(int c=1; c<=colCount; c++){
							 if(colType[c-1] != null && (colType[c-1].trim().equals("double") || colType[c-1].trim().equals("java.lang.Double"))){
								 _TmpData.put(colNameList[c-1], rs.getDouble(c));
							 } else if(colType[c-1] != null && (colType[c-1].trim().equals("int") ||colType[c-1].trim().equals("java.lang.Integer"))){
								 _TmpData.put(colNameList[c-1], rs.getInt(c));
							 } else if(colType[c-1] != null && (colType[c-1].trim().equals("java.lang.String")))
								 _TmpData.put(colNameList[c-1], rs.getString(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("float") ||colType[c-1].trim().equals("java.lang.Float")))
								 _TmpData.put(colNameList[c-1], rs.getFloat(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("short") ||colType[c-1].trim().equals("java.lang.Short")))
								 _TmpData.put(colNameList[c-1], rs.getShort(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("boolean") ||colType[c-1].trim().equals("java.lang.Boolean")))
								 _TmpData.put(colNameList[c-1], rs.getBoolean(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("byte") ||colType[c-1].trim().equals("java.lang.Byte")))
								 _TmpData.put(colNameList[c-1], rs.getByte(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("long") ||colType[c-1].trim().equals("java.lang.Long")))
								 _TmpData.put(colNameList[c-1], rs.getLong(c));
							 else if(colType[c-1] != null){
						       _TmpData.put(colNameList[c-1], rs.getObject(c));
							 }
						 }
						 _vobj = classV.newInstance();
						 mapping.setHashMap2Object(_TmpData, _vobj);
						 _TmpData.clear();
					}
					 colResult.add(_vobj);
				}else{
					/** 单值输入(是JAVA中的基本数据类),则仅取第一列*/
					Object _vobj = rs.getObject(1);
					colResult.add(_vobj);
				}
				if(sqlConfig.isOnlyReturnFirst() && count == 1){
					break;
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "处理结果用时： c=" + count + " "+ (System.currentTimeMillis() - star), null);
			//System.err.println("处理结果用时： c=" + count + " "+ (System.currentTimeMillis() - star));
			star = System.currentTimeMillis();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
		}
				
		return colResult;
	}
	
	/**
	 * <p>根据命名SQLID跟传入参数，得到执行语句</p>
	 * @param sqlId SQL的配置ID 
	 * @param parameter 输入配置参数
	 * @param conditionId 可选条件ID
	 * @return select_sql 返回查询语句
	 * @throws Exception
	 * @author 唐顺岩  2013-08-22
	 */
	public static String joinQuerySql(String sqlId, Object parameter, String[] conditionId) throws SQLException{
		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		/** 解释SQL配置 , 得到原始配置*/
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfig(sqlConfig, conditionId);
		} catch (Exception e) {
			throw new SQLException("可选条件追加错误，"+e.getMessage());
		}
		/** 将OBJECT中的数据放至SQL中 */
		String paramClassname = sqlConfig.getParameterClass();
		try {
			Map _paramData = object2Map(parameter, paramClassname);
			List<String> pL = digestParam(sqlOrigin);//得到SQL中所有的参数
			
			for (int n = 0; n < pL.size(); n++) {
				String _key = pL.get(n);
				if (_key != null && !_key.trim().equals("")) {
					if (_paramData.get(_key) != null) {
						sqlOrigin = replaceAll(sqlOrigin, _key, _paramData.get(_key).toString());
					} else {
						/** 对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理 */
						EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理", null);
						//System.err.println("对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理");
						sqlOrigin = replaceAll(sqlOrigin, _key, "");
					}
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL：" + sqlOrigin, null);
			//System.err.println("执行SQL：" + sqlOrigin);
		}catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		return sqlOrigin;	
	}
	
	/**
	 * <p>分页查询命名SQL，返回IndexedCollection</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入配置参数
	 * @param conditionId 可选条件ID
	 * @param start 返回记录启始条数
	 * @param end   返回记录结束条数
	 * @param conn 数据库连接
	 * @return 符合条件的IndexedCollection记录集
	 * @throws Exception
	 * @author 唐顺岩  2013-08-21
	 */
	public static IndexedCollection queryList4ICollByJoin(String sqlId, Object parameter, String[] conditionId, int start, int end, Connection conn) throws SQLException{

		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		/** 解释SQL配置 , 得到原始配置*/
		long star = System.currentTimeMillis();
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfig(sqlConfig, conditionId);
		} catch (Exception e) {
			throw new SQLException("可选条件追加错误，"+e.getMessage());
		}
		/** 将OBJECT中的数据放至SQL中 */
		Collection colResult = null; //结果集
		String paramClassname = sqlConfig.getParameterClass();
		Statement ps = null;
		ResultSet rs = null;
		try {
			Map _paramData = object2Map(parameter, paramClassname);
			List<String> pL = digestParam(sqlOrigin);//得到SQL中所有的参数
			
			for (int n = 0; n < pL.size(); n++) {
				String _key = pL.get(n);
				if (_key != null && !_key.trim().equals("")) {
					if (_paramData.get(_key) != null) {
						sqlOrigin = replaceAll(sqlOrigin, _key, _paramData.get(_key).toString());
					} else {
						/** 对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理 */
						EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理", null);
						//System.err.println("对NULL值处理建议用配置中的可选条件‘OPT_CONDITION’来处理");
						sqlOrigin = replaceAll(sqlOrigin, _key, "");
					}
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL：" + sqlOrigin, null);
			//System.err.println("执行SQL：" + sqlOrigin);
			
			/** 执行SQL */			
		    ps = conn.createStatement();
			rs = ps.executeQuery(sqlOrigin);
			
			String resClassname = sqlConfig.getResultClass();
			String[] colNameList = null;
			
			int colCount = 1;
			if(!isBasicDataType(resClassname)){
				/** 非基本型，则使用 ResultSetMetaData 得到返回的字段*/
				ResultSetMetaData rmeta = rs.getMetaData();
				colCount = rmeta.getColumnCount();
				colNameList = new String[colCount];
				for(int c=1; c<=colCount; c++){
	
				   String fieldId = null;
				   if(resClassname.equals("com.ecc.emp.data.KeyedCollection")){
					   /** 对于KCOL,直接使用数据库字段名（小写） */
					   fieldId = rmeta.getColumnName(c).toLowerCase(); 
				   }else{
					   /** 对于非KCOL，将数据库字段名转成DOMINA的域名 */
					   fieldId = DBColumn2Field(rmeta.getColumnName(c));
				   }
 				   colNameList[c-1] = fieldId; 
				}
			}
	
			MapInterface mapping = AutoMapping.getInstance();
			String[] colType = null;
			
			if(!isBasicDataType(resClassname)
				       && !resClassname.equals("com.ecc.emp.data.KeyedCollection") && !resClassname.equals("java.util.HashMap")){
				
			    if(mapping == null){
			    	throw new Exception("系统没有加载类"+resClassname+"的映射类");
			    }
				colType = new String[colCount];
				for(int c=0; c<colCount; c++){
					colType[c] = mapping.getFieldType(colNameList[c],resClassname);
					
				}
			}
 		
			int count = 0;
			HashMap _TmpData = new HashMap(colCount,1f);
			
			Class classV = null;
			if(!isBasicDataType(resClassname) && !resClassname.equals("com.ecc.emp.data.KeyedCollection")){
			   classV = Class.forName(resClassname);
			}
			colResult = new ArrayList();
			while(rs.next()){
				count ++;
				if(!isBasicDataType(resClassname)){
					Object _vobj = null;
					if(resClassname.equals("com.ecc.emp.data.KeyedCollection")){
						 KeyedCollection _kcolData = new KeyedCollection();
						 for(int c=1; c<=colCount; c++){
						    //_TmpData.put(colNameList[c-1], rs.getObject(c));
							 _kcolData.addDataField(colNameList[c-1], rs.getObject(c));
						 }
						 _vobj = _kcolData;
					}else if(resClassname.equals("java.util.HashMap")){
						Map _retMap = new HashMap();
						for(int c=1; c<=colCount; c++){
							_retMap.put(colNameList[c-1], rs.getObject(c));
						}
						_vobj = _retMap;
					}else{
						 for(int c=1; c<=colCount; c++){
							 if(colType[c-1] != null && (colType[c-1].trim().equals("double") || colType[c-1].trim().equals("java.lang.Double"))){
								 _TmpData.put(colNameList[c-1], rs.getDouble(c));
							 } else if(colType[c-1] != null && (colType[c-1].trim().equals("int") ||colType[c-1].trim().equals("java.lang.Integer"))){
								 _TmpData.put(colNameList[c-1], rs.getInt(c));
							 } else if(colType[c-1] != null && (colType[c-1].trim().equals("java.lang.String")))
								 _TmpData.put(colNameList[c-1], rs.getString(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("float") ||colType[c-1].trim().equals("java.lang.Float")))
								 _TmpData.put(colNameList[c-1], rs.getFloat(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("short") ||colType[c-1].trim().equals("java.lang.Short")))
								 _TmpData.put(colNameList[c-1], rs.getShort(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("boolean") ||colType[c-1].trim().equals("java.lang.Boolean")))
								 _TmpData.put(colNameList[c-1], rs.getBoolean(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("byte") ||colType[c-1].trim().equals("java.lang.Byte")))
								 _TmpData.put(colNameList[c-1], rs.getByte(c));
							 else if(colType[c-1] != null && (colType[c-1].trim().equals("long") ||colType[c-1].trim().equals("java.lang.Long")))
								 _TmpData.put(colNameList[c-1], rs.getLong(c));
							 else if(colType[c-1] != null){
						       _TmpData.put(colNameList[c-1], rs.getObject(c));
							 }
						 }
						 _vobj = classV.newInstance();
						 mapping.setHashMap2Object(_TmpData, _vobj);
						 _TmpData.clear();
					}
					 colResult.add(_vobj);
				}else{
					/** 单值输入(是JAVA中的基本数据类),则仅取第一列*/
					Object _vobj = rs.getObject(1);
					colResult.add(_vobj);
				}
				if(sqlConfig.isOnlyReturnFirst() && count == 1){
					break;
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "处理结果用时： c=" + count + " "+ (System.currentTimeMillis() - star), null);
			//System.err.println("处理结果用时： c=" + count + " "+ (System.currentTimeMillis() - star));
			star = System.currentTimeMillis();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
		}
		IndexedCollection iCol = new IndexedCollection();
		iCol.addAll(colResult);
		return iCol;
	}
	
	
	/**
	 * <p>自动插入数据库（无需写SQL）</p>
	 * @param sqlId SQL的配置ID
	 * @param value 更新值 (注：自动时不支持基本型数据)
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws SQLException
	 */
	public static int insertAuto(String sqlId, Object value, Connection conn) throws SQLException{
		return executeUpdAuto(sqlId, value, null, conn);
	}
	
	/**
	 * <p>执行数据插入操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param value 更新值
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int insert(String sqlId, Object value, Connection conn) throws SQLException{
		return executeUpd(sqlId, null, value, null,conn);
	}
	
	/**
	 * <p>执行数据插入操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param value 更新值
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int insert(String sqlId, Object parameter,Object value, Connection conn) throws SQLException{
		return executeUpd(sqlId,parameter,value, null,conn);
	}
	
	/**
	 * <p>执行数据插入操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param value 更新值
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int insert(String sqlId, Object parameter,Object value, String[] conditionId, Connection conn) throws SQLException{
		return executeUpd(sqlId,parameter,value,conditionId,conn);
	}
	
	/**
	 * <p>自动修改数据库（无需写SQL）</p>
	 * @param sqlId SQL的配置ID
	 * @param value 更新值 (注：自动时不支持基本型数据)
	 * @param pKeyField 主键字段名列表（注：为字段名，不是域名，该参数仅仅用于更新时，作为更新的过滤条件）
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws SQLException
	 */
	public static int updateAuto(String sqlId, Object value, String[] pKeyField, Connection conn) throws SQLException{
		return executeUpdAuto(sqlId, value, pKeyField, conn);
	}
	
	/**
	 * <p>执行数据变更操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param value 更新值
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int update(String sqlId, Object parameter, Object value, String[] conditionId, Connection conn) throws SQLException{
		return executeUpd(sqlId,parameter,value,conditionId,conn);
	}
	
	/**
	 * <p>执行数据删除操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int delete(String sqlId, Object parameter, Connection conn) throws SQLException{
		return executeUpd(sqlId,parameter,null,null,conn);
	}	
	
	
	/**
	 * <p>根据SQL语句执行数据删除操作</p>
	 * @param sql  删除SQL语句
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 * @author 唐顺岩
	 * @date  2013-11-05
	 */
	public static int deleteBySql(String sql, Connection conn) throws SQLException{
		int ret = -1;
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(sql.toString());
			/** 执行SQL */
			ret = ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * <p>执行数据删除操作</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int delete(String sqlId, Object parameter, String[] conditionId, Connection conn) throws SQLException{
		return executeUpd(sqlId,parameter,null,conditionId,conn);
	}
	
	/**
	 * <p>自动插入/修改数据库（无需写SQL）</p>
	 * @param sqlId SQL的配置ID
	 * @param value 更新值 (注：自动时不支持基本型数据)
	 * @param pKeyField 主键字段名列表（注：为字段名，不是域名，该参数仅仅用于更新时，作为更新的过滤条件）
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws SQLException
	 */
	public static int executeUpdAuto(String sqlId, Object value, String[] pKeyField, Connection conn) throws SQLException{
		
		int ret = -1;
		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		String sqlType = sqlConfig.getSqlType();
		if(sqlType == null 
			|| sqlType.trim().toUpperCase().equals("SELECT") 
			   || sqlType.trim().toUpperCase().equals("DELETE")){
			throw new SQLException("[" + sqlId + "]对应的配置的操作为[" + sqlType + "]，只有插入INSERT、更新UPDATE操作才支持无SQL自动更新功能");
		}
		
		String tableNm = sqlConfig.getUpdTableName();
		if(tableNm == null || tableNm.trim().equals("")){
			throw new SQLException("没有指定待操作的表名updTableName，无法SQL自动更新功能");
		}
		
		String valueClassname = sqlConfig.getValueClass();
		if(valueClassname == null || valueClassname.trim().equals("")){
			throw new SQLException("没有指定待值对象类名valueClass，无法SQL自动更新功能");
		}
		
		if(sqlType.trim().toUpperCase().equals("UPDATE")
				&& (pKeyField == null || pKeyField.length <= 0)){
			if(!sqlConfig.isCanUpdateAll()){
			    throw new SQLException("[" + sqlId + "]配置中限制了全表更新功能,所以没有过滤条件约束更新操作功能将被禁用");
			}
		}
		
		PreparedStatement ps = null;
		try {
			Map _valueData = object2Map(value,valueClassname);
			
			
			StringBuffer fieldNm = new StringBuffer();
			StringBuffer fieldParam = new StringBuffer();
			StringBuffer fieldNmParam = new StringBuffer();
			for(Iterator<String> itr=_valueData.keySet().iterator(); itr.hasNext();){
				String _key = itr.next();
				if(_key != null && !_key.trim().equals("")){
					
					if(!valueClassname.trim().equals("com.ecc.emp.data.KeyedCollection")){
						/** 不为KCOL时，将域名转换成数据库字段名 */
				  	   _key = Field2DBColumn(_key);
					}
					
					fieldNm.append(",").append(_key);
					fieldParam.append(",?");
					fieldNmParam.append(",").append(_key).append("=?");
				}
			}

			String field = fieldNm.substring(1);
			String param = fieldParam.substring(1);
			String fieldNP = fieldNmParam.substring(1);
			
			StringBuffer sql = new StringBuffer();
			if(sqlType.trim().toUpperCase().equals("INSERT")){
				
				sql.append("INSERT INTO ").append(tableNm).append(" (").append(field)
				   .append(") VALUES (").append(param).append(")");

			} else if(sqlType.trim().toUpperCase().equals("UPDATE")){
				sql.append("UPDATE ").append(tableNm).append(" SET ").append(fieldNP);
				if(pKeyField != null && pKeyField.length > 0){
					sql.append(" WHERE 1=1");
					for(int n=0; n<pKeyField.length; n++){
						if(pKeyField[n] != null && !pKeyField[n].trim().equals("")){
							sql.append(" AND ").append(pKeyField[n]).append("=?");
						}else{
							throw new SQLException("更新条件域存在空值，无法执行自动更新操作!!");
						}
					}
				}
			} else {
				throw new SQLException("自动更新不支持" +sqlType+ "操作，只有插入INSERT、更新UPDATE操作才支持无SQL自动更新功能");
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "执行自动SQL：" + sql.toString(), null);
			//System.err.println("执行自动SQL：" + sql.toString());
			ps = conn.prepareStatement(sql.toString());
			
			int parameterIndex = 1;
			for(Iterator<String> itr=_valueData.keySet().iterator(); itr.hasNext();){
				String _key = itr.next();
				if(_key != null && !_key.trim().equals("")){
					ps.setObject(parameterIndex++, _valueData.get(_key));
				}
			}
			if(pKeyField != null && pKeyField.length > 0){
				for(int n=0; n<pKeyField.length; n++){
					if(pKeyField[n] != null && !pKeyField[n].trim().equals("")){
						if(!valueClassname.trim().equals("com.ecc.emp.data.KeyedCollection")){
							ps.setObject(parameterIndex++, _valueData.get(DBColumn2Field(pKeyField[n])));
						}else{
							ps.setObject(parameterIndex++, _valueData.get(pKeyField[n]));
						}
					}else{
						throw new SQLException("更新条件域存在空值，无法执行自动更新操作!!");
					}
				}
			}
			
			/** 执行SQL */
			ret = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * <p>执行数据变更操作（包含INSERT、UPDATE、DELETE）</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param value 更新值
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static int executeUpd(String sqlId, Object parameter,Object value, String[] conditionId, Connection conn) throws SQLException{
		int ret = -1;
		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		
		if(value == null){
			//throw new SQLException("待更新的值对象为空 ，无法执行数据库操作");
		}
		
		/** 解释SQL配置 , 得到原始配置*/
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfigWithParamter(sqlConfig, parameter, conditionId);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		String paramClassname = sqlConfig.getParameterClass();
		String valueClassname = sqlConfig.getValueClass();
		PreparedStatement ps = null;
		
		try {
			 
			Map _paramData = object2Map(parameter, paramClassname);
			Map _valueData = object2Map(value,valueClassname);
			
			/** 按SQL中的第一个WHERE，将SQL分成A、B两部分，A部分为值域部分，B部分为条件域部分 
			 * （注：这里处理有点粗糙，但对于INSERT、UPDATE语法是正确的，对于DELETE没有A部分） */
			int spIdx = sqlOrigin.toLowerCase().indexOf("where"); 
			
			List<String> vL_A = null;
			List<String> vL_B = null;
			
			if(spIdx > 0){
				String sqlA = sqlOrigin.substring(0,spIdx); ///值部分SQL
				String sqlB = sqlOrigin.substring(spIdx);   ///条件部分SQL
				
				vL_A = digestParam(sqlA);//得到SQL中所有的参数
				if(_valueData != null && vL_A != null && vL_A.size() > 0){
				   sqlA = replaceValAtSql(sqlA, vL_A, _valueData);
				}
				
				vL_B = digestParam(sqlB);//得到SQL中所有的参数
				if(_paramData != null && vL_B != null && vL_B.size() > 0){
				   sqlB = replaceValAtSql(sqlB, vL_B, _paramData);
				}
				sqlOrigin = sqlA + sqlB;
			}else{
				//没有WHERE条件时
				vL_A = digestParam(sqlOrigin);//得到SQL中所有的参数
				if(_valueData != null && vL_A != null && vL_A.size() > 0){
					sqlOrigin = replaceValAtSql(sqlOrigin, vL_A, _valueData);
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "执行SQL：" + sqlOrigin, null);
			//System.err.println("执行SQL：" + sqlOrigin);
		
		    ps = conn.prepareStatement(sqlOrigin);
		    int paramIdx = 1;
		    
		    if(vL_A != null){
			  for(int n=0; n<vL_A.size(); n++){
				String _key = vL_A.get(n);
				if(_key != null && !_key.trim().equals("") && _valueData.get(_key) != null){

			       ps.setObject(paramIdx++, _valueData.get(_key));
				}
			  }
		    }
			if(vL_B != null){
			  for(int n=0; n<vL_B.size(); n++){
				String _key = vL_B.get(n);
				if(_key != null && !_key.trim().equals("") && _paramData.get(_key) != null){

			       ps.setObject(paramIdx++, _paramData.get(_key));
				}
			  }
			}
			ret = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally{
			if(ps != null) ps.close();
		}
		return ret;
	}
	
	/**
	 * <p>从配置中得到等待执行的SQL</p>
	 * <p>将SQL与可选条件组装起来</p>
	 * @param sqlConfig SQL配置
	 * @param conditionId 可选过滤条件ID集合
	 * @return SQL
	 */
	public static String  getSqlFromConfig(SqlConfig sqlConfig, String[] conditionId) throws Exception{
		
		if(sqlConfig == null){
			throw new Exception("对应的SQL配置不存在，无法执行数据库操作");
		}
		
		String _sql = sqlConfig.getSql();
		if(_sql == null || _sql.trim().equals("")){
			throw new Exception("对应的SQL配置中SQL为空，无法执行数据库操作");
		}
		
		if(conditionId != null && conditionId.length > 0){
			/** 如果 可选过滤条件ID不为空，则替换SQL中*/
			if(sqlConfig.getOptCondition() != null){
			   for(int n=0; n<conditionId.length; n++){
			
				  if(sqlConfig.getOptCondition().containsKey(conditionId[n])){
					  /** 执行替换*/
					  String _condi = (String)sqlConfig.getOptCondition().get(conditionId[n]);
					  if(_condi != null){
						  
						  ////将SQL中对应可选条件ID的部份换成条件  
						  _sql = replaceAll(_sql, conditionId[n], _condi);
					  }else{
						  EMPLog.log(LOGTYPE, EMPLog.WARNING, 0, "对应的可选条件ID有对应的配置为空");
					  }
					  
				  }else{
					EMPLog.log(LOGTYPE, EMPLog.WARNING, 0, "对应的可选条件ID没有对应的配置");
				  }
			   }
			   
			   /** 清除SQL当前没有选择的 conditionId*/
			   for(Iterator<String> itr=sqlConfig.getOptCondition().keySet().iterator(); itr.hasNext();){
				   String _key = itr.next();
				   if(_key != null){
					   boolean hasSelect = false; ///是否已经选择过滤条件
					   for(int n=0; n<conditionId.length; n++){
						   if(conditionId[n].equals(_key)){
							   hasSelect = true;
							   break;
						   }
					   }
					   if(!hasSelect){
						   ///没有选择过滤条件，从SQL中清除掉
						   _sql = replaceAll(_sql, _key, "");
					   }
				   }
			   }
			}else{
				EMPLog.log(LOGTYPE, EMPLog.DEBUG, 0, "没有可选条件配置");
			}
		}else{
			//EMPLog.log(LOGTYPE, EMPLog.DEBUG, 0, "没有输入对应的可选条件ID");
			   /** 清除SQL当前没有选择的 conditionId*/
		  if(sqlConfig.getOptCondition() != null){
		    for(Iterator<String> itr=sqlConfig.getOptCondition().keySet().iterator(); itr.hasNext();){
			   String _key = itr.next();
			   if(_key != null){
 
				   ///没有选择过滤条件，从SQL中清除掉
				   _sql = replaceAll(_sql, _key, "");
			   }
		    }
		  }
		}
		
		return _sql;
	}
	private static void objectToMap(Object value, Map params){
		if(params == null) return;
		if ( value != null){		
			if ("java.util.Map".equalsIgnoreCase(value.getClass().getName()) || "java.util.HashMap".equalsIgnoreCase(value.getClass().getName())  ){
				params.putAll((Map)value);
			} else if (value instanceof KeyedCollection){				
				KeyedCollection kCol = (KeyedCollection)value;
				Iterator it = kCol.keySet().iterator();
				while (it.hasNext()){
					String key = (String)it.next();
					params.put(key, kCol.get(key));
				}			   
			}  else if (isBasicDataType(value.getClass().getName())){				
				params.put(KEYCODE_PARAMETER_SINGLE, value.toString());
			}
		}
	}

	/**
	 * <p>从配置中得到等待执行的SQL</p>
	 * <p>将SQL与可选条件组装起来</p>
	 * @param sqlConfig SQL配置
	 * @param conditionId 可选过滤条件ID集合
	 * @return SQL
	 */
	public static String  getSqlFromConfigWithParamter(SqlConfig sqlConfig,Object parameter, String[] conditionId) throws Exception{
		
		if(sqlConfig == null){
			throw new Exception("对应的SQL配置不存在，无法执行数据库操作");
		}
		
		String _sql = sqlConfig.getSql();
		if(_sql == null || _sql.trim().equals("")){
			throw new Exception("对应的SQL配置中SQL为空，无法执行数据库操作");
		}
		
		
		Map params = new HashMap();
		objectToMap(parameter,params);//

		
	//	if(conditionId != null && conditionId.length > 0){
			/** 如果 可选过滤条件ID不为空，则替换SQL中*/
			if(sqlConfig.getOptCondition() != null){
		//	   for(int n=0; n<conditionId.length; n++){
		for(Iterator<String> itr=sqlConfig.getOptCondition().keySet().iterator(); itr.hasNext();){
				 String _key = itr.next();
					  /** 执行替换*/
					  String _condi = (String)sqlConfig.getOptCondition().get(_key);
					  //获取到condi中所需要的值是否在map中
					  /** 清除传入参数中值为空的 conditionId*/
					  String key ="";
					    int start = 0;
					    int end = 0;
					    boolean nullFlag = false;
					    while (start >= 0) {
					      start = _condi.indexOf("${", start);
					      if (start < 0) break;
					      end = _condi.indexOf("}", start + 1);
					      key = _condi.substring(start + 2, end);
					      if(NewStringUtils.isBlank(key)||params.get(key)==null||"".equals(params.get(key)==null)){
					    	  nullFlag=true;
					    	  break;
					      }
					      start = end;
					    }
					    
					  if(_condi != null&&!nullFlag){
						  
						  ////将SQL中对应可选条件ID的部份换成条件  
						  _sql = replaceAll(_sql, _key, _condi);
					  }else if(nullFlag){
						  _sql = replaceAll(_sql, _key, "");
				      }else{
						  EMPLog.log(LOGTYPE, EMPLog.WARNING, 0, "对应的可选条件ID有对应的配置为空");
					  }
		      }
			}
			   
//			   /** 清除SQL当前没有选择的 conditionId*/
//			  
//			   for(Iterator<String> itr=sqlConfig.getOptCondition().keySet().iterator(); itr.hasNext();){
//				   String _key = itr.next();
//				   if(_key != null){
//					   boolean hasSelect = false; ///是否已经选择过滤条件
//					   
//					   for(int n=0; n<conditionId.length; n++){
//						   if(conditionId[n].equals(_key)){
//							   hasSelect = true;
//							   break;
//						   }
//					   }
//					   if(!hasSelect){
//						   ///没有选择过滤条件，从SQL中清除掉
//						   _sql = replaceAll(_sql, _key, "");
//					   }
//				   }
//			   }
//			}else{
//				EMPLog.log(LOGTYPE, EMPLog.DEBUG, 0, "没有可选条件配置");
//			}
//		}else{
//			//EMPLog.log(LOGTYPE, EMPLog.DEBUG, 0, "没有输入对应的可选条件ID");
//			   /** 清除SQL当前没有选择的 conditionId*/
//		  if(sqlConfig.getOptCondition() != null){
//		    for(Iterator<String> itr=sqlConfig.getOptCondition().keySet().iterator(); itr.hasNext();){
//			   String _key = itr.next();
//			   if(_key != null){
// 
//				   ///没有选择过滤条件，从SQL中清除掉
//				   _sql = replaceAll(_sql, _key, "");
//			   }
//		    }
//		  }
//		}
//		
		return _sql;
	}
	
	public static String replaceAll(String str,String oldStr,String newStr) {
	    if(str!=null && oldStr!=null && newStr!=null ) {
	      int start = 0;
          oldStr = REP_LEFT_Exp + oldStr + REP_RIGHT_Exp;
	      while((start = str.indexOf(oldStr,start))!=-1) {
	        int i = start;

           str = str.substring(0,i)+newStr+str.substring(i+oldStr.length());
	       start += newStr.length();
	      }
	    }
	    return str;
	 }
	
   /**
    * <p>数据库表字段名映射到代码中DOMAIN的域名</p>
    * <p>映射规则： （1）字段名中下加线 _ 之后的第一个字母大写，其余字母小写;（2）去掉下加线</p>
    * @param dbColumn 数据库表字段名
    * @return 代码中DOMAIN的域名
    */
   public static String DBColumn2Field(String dbColumn){
	
	   String _field = dbColumn.toLowerCase();
	   String[] _fieldAry = _field.split("");
	   for(int n=0; n<_fieldAry.length; n++){
		   
		   if(n != 0 && n < _fieldAry.length - 1 
				   && _fieldAry[n].equals("_")){
			   _fieldAry[n + 1] = _fieldAry[n + 1].toUpperCase();
		   }
	   }
	   
	   StringBuffer fieldBuf = new StringBuffer(); 
	   
	   for(int n=0; n<_fieldAry.length; n++){
		   if(!_fieldAry[n].equals("_")){ //去掉 _
		      fieldBuf.append(_fieldAry[n]);
		   }
	   }
 
	   return fieldBuf.toString();
   }
	
   /**
    * <p>代码中DOMAIN的域名映射到数据库表字段</p>
    * <p>映射规则：（1）大写字母前加上下加线 _ （2）所有字母变小写</p>
    * @param field DOMAIN的域名
    * @return 数据库表字段名
    */
   public static String Field2DBColumn(String field){
	  
	  StringBuffer DBColumn = new StringBuffer();
	  
	  int lastStart = 0;
	  for(int n=0; n<field.length(); n++){
	  
	     if(Character.isUpperCase(field.charAt(n))){
	    	 /** 是大写字母 */
	    	 String sub = field.substring(lastStart, n);
	    	 DBColumn.append("_").append(sub);
	    	 lastStart = n;
	     }
	  }
	  DBColumn.append("_").append(field.substring(lastStart));
	  return DBColumn.substring(1).toLowerCase();   
   }
   
	/** 
	 * <p>从字符串中分解出所有参数名</p>
	 * @param str
	 * @return
	 */
	public static List<String> digestParam(String str){
		List<String> paramList = new ArrayList<String>();
		int start = 0;
		int end = 0;
		while(start >= 0){
			
			start = str.indexOf(REP_LEFT_Exp,start);
			if(start < 0) break;
			end = str.indexOf(REP_RIGHT_Exp,start + 1);
			paramList.add(str.substring(start+2, end));
			start = end;
		}
		return paramList;
	}
	
	/**
	 * <p>是否为基本型数据</p>
	 * @param Classname 类名
	 * @return true是基本型数据
	 */
	public static boolean isBasicDataType(String Classname){
		if(Classname.equals("java.lang.Double")
				 || Classname.equals("java.lang.Integer") 
					|| Classname.equals("java.lang.Float")
					    || Classname.equals("java.lang.String")
                                                || Classname.equals("java.math.BigDecimal")){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map object2Map(Object obj, String objClassname) throws Exception{
		Map retMap = null;
		if(obj != null){
			HashMap mapdata = new HashMap();
			if(!isBasicDataType(objClassname)
					       && !objClassname.equals("com.ecc.emp.data.KeyedCollection") && !objClassname.equals("java.util.HashMap")&& !objClassname.equals("java.util.Map")){
				MapInterface mapping = AutoMapping.getInstance();
			    if(mapping == null){
			    	throw new Exception("系统没有加载类"+objClassname+"的映射类");
			    }
				mapping.setObject2HashMap(obj, mapdata);//映射转换至MAP中
				retMap = mapdata;
			} else if(objClassname.equals("com.ecc.emp.data.KeyedCollection")){
				retMap = (Map)obj;
			} else if(objClassname.equals("java.util.HashMap")){
				retMap = (Map)obj;
			}else{
				/** 单值输入(是JAVA中的基本数据类)*/
				mapdata.put(KEYCODE_PARAMETER_SINGLE,obj);
				retMap = mapdata;
			}
		}
		return retMap;
	}
	
	/**
	 * <p>替换SQL中的变量</p>
	 * @param sql 
	 * @param valNameList 变量名列表
	 * @param mapData 变量值
	 * @return
	 */
	private static String replaceValAtSql(String sql, List<String> valNameList, Map mapData){
		
		for(int n=0; n<valNameList.size(); n++){
			String _key = valNameList.get(n);
			//System.err.println(">>>" + _key);
			if(_key != null && !_key.trim().equals("")){
          
			  if(mapData.get(_key) != null){
				  sql = replaceAll(sql, _key, "?");
			  } else {
				  /** 对NULL值处理建议用配置中的可选条件‘OPT_CONDITION’来处理 */
				  EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理", null);
				  //System.err.println("对NULL值处理建议用配置中的可选条件[OPT_CONDITION]来处理");
				  sql = replaceAll(sql, _key, "null");
			  }
			}
		}
		return sql;
	}
	
	/**
	 * 批量 插入 操作
	 * @param sqlId
	 * @param value
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static void executeBatch(String sqlId, Object value, Connection conn) throws SQLException{
		  executeBatch(sqlId, null, value, null,conn);
	}
	
	/**
	 * <p>执行批量操作（包含INSERT、UPDATE、DELETE）</p>
	 * @param sqlId  SQL的配置ID 
	 * @param parameter 输入参数（查询过滤条件）
	 * @param value 更新值
	 * @param conditionId 可选条件ID
	 * @param conn 数据库连接
	 * @return 影响记录条数
	 * @throws Exception
	 */
	public static void executeBatch(String sqlId, Object parameter,Object value, String[] conditionId, Connection conn) throws SQLException{
		int ret = -1;
		/** 取得对应的SQL配置 */
		SqlConfig sqlConfig = SqlConfigBuffer.getSqlConfigById(sqlId);
		if(sqlConfig == null){
			throw new SQLException("[" + sqlId + "]对应的SQL配置不存在，无法执行数据库操作");
		}
		/** 解释SQL配置 , 得到原始配置*/
		String sqlOrigin = null;
		try {
			sqlOrigin = getSqlFromConfig(sqlConfig, conditionId);
		} catch (Exception e) {
			e.printStackTrace();
			 
		}

		String paramClassname = sqlConfig.getParameterClass();
		String valueClassname = sqlConfig.getValueClass();
		PreparedStatement ps = null;
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL==1==：" + sqlOrigin, null);
		//System.err.println("执行SQL==1==：" + sqlOrigin);
		try {		   
			List list = (List) value;
			if(list==null||list.size()<1)
				return;
			Object objValue = list.get(0);	
			
		 	Map _paramData = object2Map(parameter, paramClassname);
			Map _valueData = object2Map(objValue,valueClassname);
			/** 按SQL中的第一个WHERE，将SQL分成A、B两部分，A部分为值域部分，B部分为条件域部分 
			 * （注：这里处理有点粗糙，但对于INSERT、UPDATE语法是正确的，对于DELETE没有A部分） */
			int spIdx = sqlOrigin.toLowerCase().indexOf("where"); 
			
			List<String> vL_A = null;
			List<String> vL_B = null;
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "spIdx===="+spIdx, null);
			//System.out.println("spIdx===="+spIdx);
			if(spIdx > 0){
				String sqlA = sqlOrigin.substring(0,spIdx); ///值部分SQL
				String sqlB = sqlOrigin.substring(spIdx);   ///条件部分SQL
				
				vL_A = digestParam(sqlA);//得到SQL中所有的参数
				if(_valueData != null && vL_A != null && vL_A.size() > 0){
				   sqlA = replaceValAtSql(sqlA, vL_A, _valueData);
				}
				
				vL_B = digestParam(sqlB);//得到SQL中所有的参数
				if(_paramData != null && vL_B != null && vL_B.size() > 0){
				   sqlB = replaceValAtSql(sqlB, vL_B, _paramData);
				}
				sqlOrigin = sqlA + sqlB;
			}else{
				//没有WHERE条件时
				vL_A = digestParam(sqlOrigin);//得到SQL中所有的参数
				if(_valueData != null && vL_A != null && vL_A.size() > 0){
					sqlOrigin = replaceValAtSql(sqlOrigin, vL_A, _valueData);
				}
			}
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "执行SQL：" + sqlOrigin, null);
			//("执行SQL：" + sqlOrigin);
		
		    ps = conn.prepareStatement(sqlOrigin);
		    conn.setAutoCommit(false);
		    long startTime = System.currentTimeMillis();
		    for(int i=0;i<list.size();i++){
		    	int paramIdx = 1;
	 
		    	objValue = list.get(i);
			   _valueData = object2Map(objValue,valueClassname);
			    if(vL_A != null){
				  for(int n=0; n<vL_A.size(); n++){
					String _key = vL_A.get(n);
					if(_key != null && !_key.trim().equals("") && _valueData.get(_key) != null){
			
				       ps.setObject(paramIdx++, _valueData.get(_key));
					}
				  }
			    }
				if(vL_B != null){
				  for(int n=0; n<vL_B.size(); n++){
					String _key = vL_B.get(n);
					if(_key != null && !_key.trim().equals("") && _paramData.get(_key) != null){

				       ps.setObject(paramIdx++, _paramData.get(_key));
					}
				  }
				}
			    ps.addBatch();
			    if(i%1000==0){
			    	ps.executeBatch();
			    }			
			 }
		     ps.executeBatch();
		    long endTime = System.currentTimeMillis();
//			conn.commit();
		    EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "消耗时间："+(endTime-startTime)+"ms", null);
			//System.out.println("消耗时间："+(endTime-startTime)+"ms");
//			System.out.println("lenth=2=="+a.length);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally{
			if(ps != null) ps.close();
		}
		 
	}
	
	
	public static void main(String[] args) throws Exception{
		
	}
	
}

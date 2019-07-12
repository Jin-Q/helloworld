package com.yucheng.cmis.platform.msiview.dao;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.dao.SqlClient;

public class MsiviewDao {

	/**
	 * 根据服务ID查询服务集信息
	 * @param methodId
	 * @param conn
	 * @return
	 */
	public KeyedCollection queryClassByMethodId(String methodId, Connection conn) throws Exception{
		KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryClassByMethodId", methodId, null, conn);
		return kColl;
	}
	
	
	/**
	 * 查询MSI中注册的所有模块
	 *	@param conn 数据库连接
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> queryAllMsiModual(Connection conn) throws Exception{
		return (List<Map<String, String>>)SqlClient.queryList("queryAllMsiModual", null, conn);
	}
	
	
	/**
	 * 查询模块下的服务集
	 * @param modualId 模块ID
	 * @param conn 数据库连接
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> queryClassByModualId(String modualId, Connection conn) throws Exception{
		return (List<Map<String, String>>)SqlClient.queryList("queryClassByModualId", modualId, conn);
	}
	
	/**
	 * 查询服务集下的具体服务
	 * @param conn 数据库连接
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> queryMethodByClassId(String classId, Connection conn) throws Exception{
		return (List<Map<String, String>>)SqlClient.queryList("queryMethodByClassId", classId, conn);
	}
	
	
	/**
	 * 根据服务集ID查询该服务集的描述信息
	 * @param classId 服务集ID
	 * @param con 数据库连接
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection queryClassViewByClassId(String classId,Connection con)throws Exception{
		KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryClassViewByClassId", classId,null, con);
		return kColl;
	}
	
	/**
	 * 根据服务ID查询该服务的信息
	 * @param methodId 服务ID
	 * @param con 数据库连接
	 * @return KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection queryMethodByMethodId(String methodId, Connection con)throws Exception{
		KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryMethodByMethodId", methodId,null, con);
		return kColl;
	}
	
	/**
	 * 清空s_msi_class_view表数据
	 * @param con 数据库连接
	 */
	public void deleteAllMsiClassView(Connection con) throws Exception{
		SqlClient.delete("deleteAllMsiCalssView", null, con);
	}
	
	/**
	 * 清空s_msi_method_view表数据
	 * @param con 数据库连接
	 */
	public void deleteAllMsiMethodView(Connection con) throws Exception{
		SqlClient.delete("deleteAllMsiMethodView", null, con);
	}
	
	
	/**
	 * 保存数据到s_msi_class_view
	 * @param con 数据库连接
	 * @param classMap 数据集合
	 * @throws Exception
	 */
	public void insertMsiClassView(Connection con, Map<String, Map<String, String>> classMap) throws Exception{
		try {
			//数据库操作
			for (Iterator it = classMap.values().iterator(); it.hasNext();) {
				Map<String, String> map = (Map<String, String>) it.next();
				
				SqlClient.insert("insertMsiCalssView", map, con);
				
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally{
		}
	}
	
	/**
	 * 保存数据到s_msi_method_view
	 * @param con 数据库连接
	 * @param classMap 数据集合
	 * @throws Exception
	 */
	public void insertMsiMethodView(Connection con,Map<String, Map<String, String>> methodMap) throws Exception {
		try {
			for (Iterator it = methodMap.values().iterator(); it.hasNext();) {
				Map<String, String> map = (Map<String, String>) it.next();
				
				SqlClient.insert("insertMsiMethodView", map, con);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
		}
	}
	
}

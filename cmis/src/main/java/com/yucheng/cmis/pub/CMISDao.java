package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.dbmodel.service.TableModelLoader;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class CMISDao {
	private String id;
	private String describe;
	private Context context;
	private Connection connection;
	private HashMap configTable = new HashMap();
	
	/**
	 * <p>设置业务组件配置参数</p>
	 * <p>其在通过业务组件工厂实例化组件时获得</p>
	 * <p>注：本方法不用于设置单个配置数，且该方法一般情况下，只用于组件工厂</p>
	 * @param configs 配置参数列表
	 */
	public void setParameter(HashMap configs){
		
		this.configTable = configs;
	}
	
	/**
	 * <p>由组件配置参数名获取业务组件的配置信息</p>
	 * <p>若组件没有设置configTable，或对应参数名在configTable中不存在，则返回缺省值</p>
	 * @param parameterid 配置参数名
	 * @param defvalue    缺省值
	 * @return 配置参数对应的值（注，统一以String类型返回）
	 */
	public String getParameter(String parameterid, String defvalue) {
		String st_val = null;
		if(this.configTable != null){
			st_val = (String)this.configTable.get(parameterid);
		}else{
			System.err.println("该组件配置表为空");
			st_val = null;
		}
		if(st_val == null){
			st_val = defvalue; //从配置表中取数为空则使用缺省值
		}
		return st_val;
	}
	
	/**
	 * <p>由组件配置参数名获取业务组件的配置信息</p>
	 * <p>若组件没有设置configTable，或对应参数名在configTable中不存在，则返回空</p>
	 * @param parameterid 配置参数名
	 * @return 配置参数对应的值（注，统一以String类型返回）
	 */
	public String getParameter(String parameterid){
		return this.getParameter(parameterid, null);
	}
	
/*	protected Connection getConnection() throws ComponentException {
		DataSource dataSource = null;
		
		if(dataSource == null)
			dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);		
		if (dataSource == null)
			throw new ComponentException(this.id,"在Context中数据源为空");
		
		try {
			this.connection = ConnectionManager.getConnection(dataSource);
		} catch (GetConnectionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ComponentException(this.id,"取数据源发生异常：" + e.getMessage());
		}		
		return connection;
	}*/
			
	
	public Connection getConnection() {
		return connection;
	}

	public HashMap getConfigTable() {
		return configTable;
	}
	public void setConfigTable(HashMap configTable) {
		this.configTable = configTable;
	}
//	public Connection getConnection() {
//		return connection;
//	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//根据表模型、自定义SQL查询 返回 List<KeyedCollection> -g
   public List<KeyedCollection> queryListMapBySql(String modelId,String sql) throws DaoException{
	   ResultSet rs = null;
	   Statement stmt = null;
	    List<KeyedCollection> col=null;
	   try{
		   stmt = connection.createStatement();
		   EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,"query sql: "+sql);
		   rs = stmt.executeQuery(sql); 
		   if(rs!=null){
			   col= new ArrayList<KeyedCollection>();
			   ResultSetMetaData rsm = rs.getMetaData();
	           int column = rsm.getColumnCount();
	           while (rs.next()) {
	        	   KeyedCollection rowKcoll=new KeyedCollection(modelId);
	               for (int i = 1; i <= column; i++) {
	            	   String key = rsm.getColumnName(i);
	            	   key=key.toLowerCase();
	                   Object value = rs.getObject(i);
	                   rowKcoll.addDataField(key, value);
	               }
	               col.add(rowKcoll);
	           } 
		   }   
	   }catch(Exception e){
		   e.printStackTrace();
		   throw new DaoException(e.getMessage());
	   }finally{
		   try{
			    if (rs != null) {
					rs.close();
					rs = null;
				 }
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
		   }catch(Exception e){
			   
		   }
	   }
	 return col;	
   }
   
   //根据表模型、自定义SQL查询 返回IndexedCollection 其中IndexedCollection名为modelId+List  -g
   public IndexedCollection queryICollBySql(String modelId,String sql) throws DaoException{
	   ResultSet rs = null;
	   Statement stmt = null;
	   IndexedCollection iColl=null;
	   try{
		   stmt = connection.createStatement();
		   EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,"query sql: "+sql);
		   rs = stmt.executeQuery(sql); 
		   if(rs!=null){
			   iColl= new IndexedCollection(modelId+"List");
			   ResultSetMetaData rsm = rs.getMetaData();
	           int column = rsm.getColumnCount();
	           while (rs.next()) {
	        	   KeyedCollection rowKcoll=new KeyedCollection(modelId);
	               for (int i = 1; i <= column; i++) {
	            	   String key = rsm.getColumnName(i);
	            	   key=key.toLowerCase();
	                   Object value = rs.getObject(i);
	                   rowKcoll.addDataField(key, value);
	               }
	               iColl.addDataElement(rowKcoll);
	           } 
		   }   
	   }catch(Exception e){
		   e.printStackTrace();
		   throw new DaoException(e.getMessage());
	   }finally{
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	 return iColl;	
   }
   
   ///根据表模型、自定义SQL查询 返回第一条结果的Kcoll  -g
   public KeyedCollection queryFirstKCollBySql(String modelId,String sql) throws DaoException{
	   ResultSet rs = null;
	   Statement stmt = null;
	   KeyedCollection kColl=null;
	   try{
		   stmt = connection.createStatement();
		   EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,"query sql: "+sql);
		   rs = stmt.executeQuery(sql); 
		   
		   ResultSetMetaData rsm = rs.getMetaData();
           int column = rsm.getColumnCount();
		   if(rs!=null&&rs.next()){
        	   kColl=new KeyedCollection(modelId);
               for (int i = 1; i <= column; i++) {
            	   String key = rsm.getColumnName(i);
            	   key=key.toLowerCase();
                   Object value = rs.getObject(i);
                   kColl.addDataField(key, value);
               }
		   }
	   }catch(Exception e){
		   e.printStackTrace();
		   throw new DaoException(e.getMessage());
	   }finally{
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	 return kColl;	
   }
   
   
	/**
	 * 根据传入的sql返回 一条或者零条数据
	 * @param sql  查询sql
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String> queryDataOfMapByCondition(String sql) throws DaoException {
		PreparedStatement psmt = null;
		ResultSet rs = null;
		HashMap<String,String> hm = new HashMap<String,String>();
		try {
			psmt = this.getConnection().prepareStatement(sql);
			rs = psmt.executeQuery();
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "executeSql:"+sql);
			if(rs!=null&&rs.next()){
			   ResultSetMetaData rsm = rs.getMetaData();
	           int column = rsm.getColumnCount();
	         
	           for (int i = 1; i <= column; i++) {
            	   String key = rsm.getColumnName(i);
            	   key=key.toLowerCase();
                   String value = rs.getString(i);
                   hm.put(key, value);
	           }
		              
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} finally{
			try {
				if(rs!=null)rs.close();
				if(psmt!=null)psmt.close();
			} catch (SQLException e) {}
		}
		
		return hm;
	}
	
	/**
	 * 根据传入的sql 返回一个记录列表 里面可能有 一条或者多条记录 或者无记录存在
	 * @param sql 查询sql
	 * @return
	 * @throws AgentException
	 */
	public List<HashMap<String,String>>queryDataOfMapListByCondition(String sql) throws DaoException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		ArrayList<HashMap<String,String>> arrList = new ArrayList<HashMap<String,String>>();
		try {
			
			psmt = this.getConnection().prepareStatement(sql);
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "executeSql:"+sql);
			rs = psmt.executeQuery();
			
			while(rs!=null&&rs.next()){
			   HashMap<String,String> hm = new HashMap<String,String>();
			   ResultSetMetaData rsm = rs.getMetaData();
	           int column = rsm.getColumnCount();
	         
	           for (int i = 1; i <= column; i++) {
            	   String key = rsm.getColumnName(i);
            	   key=key.toLowerCase();
                   String value = rs.getString(i);
                   hm.put(key, value);
	           }
	           arrList.add(hm);
			  } 
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} finally{
			try {
				if(rs!=null)rs.close();
				if(psmt!=null)psmt.close();
			} catch (SQLException e) {}
		}
		
		return arrList;
	}
	
	/**
	 * 更加传入的表名 和该表的字段列表 返回查询字段的结果数据
	 * @param tableName 表名 如s_dic
	 * @param queryCol	需要查询的列名称  如 cnname,enname 两个字段之间用','分隔
	 * @param condition 条件 如 cnname='jack'
	 * @return
	 * @throws AgentException
	 */
	public HashMap<String,String> queryDataOfMapByCondition(String tableName,String queryCol,String condition)throws DaoException{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		HashMap<String,String> hm = new HashMap<String,String>();
		try {
			//拼装Sql
			StringBuffer sb = new StringBuffer("");
			sb.append("select ")
			  .append(queryCol)
			  .append(" ").append(tableName)
			  .append(" ").append(condition);
			
			psmt = this.getConnection().prepareStatement(sb.toString());
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "executeSql:"+sb.toString());
			rs = psmt.executeQuery();
			
			if(rs!=null&&rs.next()){
				   
				   ResultSetMetaData rsm = rs.getMetaData();
		           int column = rsm.getColumnCount();
		         
		           for (int i = 1; i <= column; i++) {
		            	   String key = rsm.getColumnName(i);
		            	   key=key.toLowerCase();	//列名转小写
		                   String value = rs.getString(i);
		                   hm.put(key, value);
		           }
		              
			  } 
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} finally{
			try {
				if(rs!=null)rs.close();
				if(psmt!=null)psmt.close();
			} catch (SQLException e) {}
		}
		
		return hm;
	} 
	
	/**
	 * 对 更新、删除、新增 操作进行托管 执行传入语句
	 * @param sql 查询sql
	 * @return 1---成功 2---失败
	 * @throws AgentException
	 */
	public String executeSql(String sql)throws DaoException{
		String retvalue = "2";	//返回状态 1--成功 2--失败
		PreparedStatement psmt = null;
		try {
			
			psmt = this.getConnection().prepareStatement(sql);
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "executeSql:"+sql);
			psmt.execute(sql);
			retvalue = "1";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} finally{
			try {
				if(psmt!=null)psmt.close();
			} catch (SQLException e) {}
		}
		return retvalue;
	}
	
	
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws DaoException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws DaoException {
	    PreparedStatement state = null;
	    TableModelLoader modelLoader = (TableModelLoader)this.getContext().getService(CMISConstance.ATTR_TABLEMODELLOADER);
        TableModel refModel = modelLoader.getTableModel(model);
      
	    int j;
	    try{
	        List<TableModelField> deleteFieldList = new ArrayList<TableModelField>();
	        String strSQL = null;
	     
	        StringBuffer strSQLBuf = new StringBuffer((new StringBuilder("DELETE FROM ")).append(refModel.getDbTableName()).append(" WHERE ").toString());
	        for(Iterator iterator = conditionFields.keySet().iterator(); iterator.hasNext();){
	            String fieldId = (String)iterator.next();
	            TableModelField modelField = refModel.getModelField(fieldId);
	            if(modelField != null){
	                deleteFieldList.add(modelField);
	                if(iterator.hasNext()){
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? AND ").toString());
	                }else{
	                    strSQLBuf.append((new StringBuilder(String.valueOf(modelField.getColumnName()))).append(" = ? ").toString());
	                }
	            }
	        }
	
	        if(deleteFieldList.isEmpty()){
	            throw new DaoException((new StringBuilder("Delete TableModel[")).append(refModel.getId()).append("]未获取到字段信息").toString());
	        }
	        strSQL = strSQLBuf.toString();
	        Connection con = this.getConnection();  //得到数据库连接
	        state = con.prepareStatement(strSQL);
	        for(int i = 0; i < deleteFieldList.size(); i++){
	            TableModelField field = (TableModelField)deleteFieldList.get(i);
	            Object fieldValue = conditionFields.get(field.getId());
	            if(fieldValue != null){
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value = ").append(fieldValue).toString());
	                state.setObject(i + 1, fieldValue);
	            } else{
	                EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder("Set condition's field [")).append(field.getColumnName()).append("]'s value with null").toString());
	                state.setNull(i + 1, field.getColumnType());
	            }
	        }
	
	        int result = state.executeUpdate();
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, (new StringBuilder(String.valueOf(result))).append(" records in tableModel [").append(refModel.getId()).append("] has been DELETE.").toString());
	        state.close();
	        j = result;
	    }catch(SQLException se){
	        EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, (new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString(), se);
	        throw new DaoException((new StringBuilder("Failed to DELETE record in tableModel [")).append(refModel.getId()).append("] due to SQLException !").toString());
	    }
	    if(state != null){
	        try{
	            state.close();
	        }
	        catch(SQLException sqlexception) { }
	    }
	    return j;
	}
}

package com.yucheng.cmis.pub.pubopera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.util.TableModelUtil;

public class PubOperaDao extends CMISDao {
	/**
	 * 根据表明和条件删除表中数据
	 * 
	 * @param tableName
	 * @param condition
	 * @param conn
	 */
	public int deleteDateByTableAndCondition(String tableName,
			String condition, Connection conn) throws EMPException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		int count = 0; // 更新的记录条数
		try {
			stmt = conn.createStatement();
			String sql = "delete from " + tableName + " " + condition;
			System.out.println("SQL= " + sql);
			
			count = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	/**
	 * 根据表明和条件删除表中数据
	 * 
	 * @param tableName
	 * @param condition
	 * @param conn
	 */
	public int deleteDateByTableAndConditionMap(String tableName,
			Map<String, String> conditionMap, Connection conn)
			throws EMPException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		String conditionSql = "";
		int count = 0; // 更新的记录条数
		try {
			stmt = conn.createStatement();

			Iterator conditionIt = conditionMap.entrySet().iterator(); // 拼成条件sql
			while (conditionIt.hasNext()) {
				Map.Entry conditionEntry = (Map.Entry) conditionIt.next();
				if (!"".equals(conditionSql)) {
					conditionSql += " and ";
				}
				conditionSql += conditionEntry.getKey() + " = '"
						+ conditionEntry.getValue() + "'";
			}

			String sql = "delete from " + tableName + " where  " + conditionSql;

			System.out.println("SQL= " + sql);

			count = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	/**
	 * 根据表明和需要更新的值和条件更新表的数据
	 * 
	 * @param tableName
	 *            表名
	 * @param valueMap
	 *            需要更新的字段和值
	 * @param conditionMap
	 *            条件字段和值
	 * @param connection
	 * @return
	 * @throws EMPException
	 */
	public int updateDateByTableAndValueAndCondition(String tableName,
			HashMap<String, String> valueMap,
			HashMap<String, String> conditionMap, Connection connection)
			throws EMPException {
		Statement stmt = null;
		int count = 0; // 更新的记录条数

		try {
			stmt = connection.createStatement();
			String valueSql = "";
			String conditionSql = "";

			Iterator valueIt = valueMap.entrySet().iterator(); // 拼成值的sql
			while (valueIt.hasNext()) {
				Map.Entry valueEntry = (Map.Entry) valueIt.next();
				if (!"".equals(valueSql)) {
					valueSql += " , ";
				}
				if (valueEntry.getValue().toString().indexOf("+") == -1)
					valueSql = valueSql + valueEntry.getKey() + " = '" + valueEntry.getValue() + "'";
				else
					valueSql = valueSql + valueEntry.getKey() + " = " + valueEntry.getValue();
				
			}

			Iterator conditionIt = conditionMap.entrySet().iterator(); // 拼成条件sql
			while (conditionIt.hasNext()) {
				Map.Entry conditionEntry = (Map.Entry) conditionIt.next();
				if (!"".equals(conditionSql)) {
					conditionSql += " and ";
				}
				conditionSql += conditionEntry.getKey() + " = '"
						+ conditionEntry.getValue() + "'";
			}

			String sql = "update " + tableName + " set " + valueSql + " where "
					+ conditionSql + "";

			System.out.println("SQL=" + sql);

			count = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	/**
	 * 根据表明和条件查询指定的字段值
	 * 
	 * @param tableName
	 *            表名
	 * @param retValues
	 *            需要返回的字段
	 * @param conditionMap
	 *            条件字段和值
	 * @return
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByTableAndCondition(String tableName,
			String retValues, HashMap<String, String> conditionMap,
			Connection conn) throws EMPException {
		ResultSet rs = null;
		Statement stmt = null;
		IndexedCollection iColl = new IndexedCollection();
		KeyedCollection kColl = null;
		String conditionSql = ""; // 条件SQL
		int numberOfColumns = 0; // 返回的列名的个数

		try {
			stmt = conn.createStatement();

			Iterator conditionIt = conditionMap.entrySet().iterator(); // 拼成条件sql
			while (conditionIt.hasNext()) {
				Map.Entry conditionEntry = (Map.Entry) conditionIt.next();
				if (!"".equals(conditionSql)) {
					conditionSql += " and ";
				}
				conditionSql += conditionEntry.getKey() + " = '"
						+ conditionEntry.getValue() + "'";
			}

			String sql = "select " + retValues + " from " + tableName
					+ " where " + conditionSql; // 查询条件的SQL
			System.out.println("SQL=" + sql);

			rs = stmt.executeQuery(sql);

			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				numberOfColumns = rsmd.getColumnCount(); // 求列数

				while (rs.next()) {
					kColl = new KeyedCollection();

					for (int i = 1; i <= numberOfColumns; i++) {
						kColl.addDataField(rsmd.getColumnName(i).toLowerCase(),
								rs.getString(i)); // 把对应的字段名和字段值add到kColl里面
					}

					iColl.add(kColl); // 生成iColl
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
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

	/**
	 * 根据表名和主键条件查询指定的字段值
	 * 
	 * @param tableName
	 *            表名
	 * @param retValues
	 *            需要返回的字段
	 * @param conditionMap
	 *            主键和值
	 * @return
	 * @throws EMPException
	 */
	public KeyedCollection selectValuesByTableAndKeyWords(String tableName,
			String retValues, HashMap<String, String> conditionMap,
			Connection conn) throws EMPException {
		ResultSet rs = null;
		Statement stmt = null;
		KeyedCollection kColl = new KeyedCollection();
		String conditionSql = ""; // 条件SQL
		int numberOfColumns = 0; // 返回的列名的个数

		try {
			stmt = conn.createStatement();

			Iterator conditionIt = conditionMap.entrySet().iterator(); // 拼成条件sql
			while (conditionIt.hasNext()) {
				Map.Entry conditionEntry = (Map.Entry) conditionIt.next();
				if (!"".equals(conditionSql)) {
					conditionSql += " and ";
				}
				conditionSql += conditionEntry.getKey() + " = '"
						+ conditionEntry.getValue() + "'";
			}

			String sql = "select " + retValues + " from " + tableName
					+ " where " + conditionSql; // 查询条件的SQL
			System.out.println("SQL=" + sql);

			rs = stmt.executeQuery(sql);

			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				numberOfColumns = rsmd.getColumnCount(); // 求列数
				if (rs.next()) {
					for (int i = 1; i <= numberOfColumns; i++) {
						kColl.addDataField(rsmd.getColumnName(i).toLowerCase(),
								rs.getString(i)); // 把对应的字段名和字段值add到kColl里面
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
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
	 * 根据表名和条件查询指定的字段值
	 * 
	 * @param tableName 	表名
	 * @param retValues 	需要返回的字段
	 * @param condition 	查询条件
	 * @param connection 	连接
	 * @return IndexedCollection 	返回值的iColl
	 * @throws EMPException
	 */
	public IndexedCollection selectValuesByCondition(String tableName, String colName,
			String condition,Connection connection ) throws EMPException {
		String tables[];
		String table[];
		String conditionSql = "";
		IndexedCollection iColl = null;
		table = null;
		try {
			// 拼接查询SQL语句
			if ((tableName.trim()).equals("") || tableName == null) {
				throw new EMPException("查询表名不能为空！");
			}
			if ((colName.trim()).equals("") || colName == null) {
				if (tableName.trim().indexOf(',') == -1) {
					colName = "*";
				} else {
					tables = tableName.trim().split(",");
					for (int i = 0; i < tables.length; i++) {
						String tmp = tables[i].trim();
						if (tmp.contains(" ")) {
							table[i] = tmp.substring(tmp.lastIndexOf(" "), tmp
									.length() - 1);
						} else {
							table[i] = tmp;
						}
					}
					for (int j = 0; j < table.length; j++) {
						colName += table[j] + ".*";
						if (j != table.length - 1) {
							colName += ",";
						}
					}
				}
			}
			if (condition.trim() == "" || condition == null) {
				condition = " 1=1 ";
			}
			conditionSql += "select " + colName.trim();
			conditionSql += " from " + tableName.trim();
			if (!condition.trim().toLowerCase().startsWith("where")) {
				conditionSql += " where " + condition.trim();
			} else {
				conditionSql += " " + condition;
			}
			iColl = executeQrySql(connection, conditionSql);
		} catch (SQLException e) {
			throw new EMPException(e);
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			
		}
		return iColl;
	}
	
	/**
	 * 根据表名和条件查询指定的字段值，只返回一个值
	 * 
	 * @param tableName 	表名
	 * @param retValues 	需要返回的字段
	 * @param condition 	查询条件
	 * @return IndexedCollection 	返回值的iColl
	 * @throws EMPException
	 * @throws SQLException 
	 */
	public String selectSingleValueByCondition(String tableName, String colName,
			String condition,Connection connection) throws EMPException{
		String retValue = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String conditionStr = "select " + colName + " from "+ tableName + " " + condition;
			
			pstmt = connection.prepareStatement(conditionStr);
			System.out.println("SQL=" + conditionStr);
			
			rs = pstmt.executeQuery();
			if(rs != null){	
				if(rs.next()){
					retValue = rs.getString(1);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new EMPException(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new EMPException(e);
			}
		}
		return retValue;
	}
	
	/**
	 * 执行SQL语句查询
	 * @param connection	连接
	 * @param conditionStr	查询SQL语句
	 * @return IndexedCollection 返回值的iColl	
	 * @throws SQLException 
	 * @throws SQLException,InvalidArgumentException,DuplicatedDataNameException
	 */
	public IndexedCollection executeQrySql(Connection connection,String conditionStr) throws SQLException, InvalidArgumentException, DuplicatedDataNameException {
		IndexedCollection iColl = new IndexedCollection();
		KeyedCollection kColl = new KeyedCollection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numberOfColumns = 0;
		try {
			pstmt = connection.prepareStatement(conditionStr);
			System.out.println("SQL=" + conditionStr);
			rs = pstmt.executeQuery();
			if(rs != null){	
				ResultSetMetaData rsmd = rs.getMetaData();
			    numberOfColumns = rsmd.getColumnCount(); //求列数	
				while(rs.next()){
					kColl = new KeyedCollection();
					for(int i = 1; i <= numberOfColumns; i++){
						kColl.addDataField(rsmd.getColumnName(i).toLowerCase(),rs.getString(i));//把对应的字段名和字段值add到kColl里面
					}
					iColl.add(kColl);	//生成iColl
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.getMessage());
			}
		}
		return iColl;
	}
	
	/**
	 * 通过查询条件分页查询
	 * @param connection
	 * @param conditionStr
	 * @param pageInfo
	 * @return IndexedCollection
	 * @throws SQLException,InvalidArgumentException,DuplicatedDataNameException
	 **/
	public IndexedCollection queryByPageInfo(Connection connection,String conditionStr,PageInfo pageInfo) throws EMPException {
		IndexedCollection iColl = new IndexedCollection();
		KeyedCollection kColl = new KeyedCollection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int numberOfColumns = 0;
		int beginIndex,endIndex;
		beginIndex = 1;
        endIndex = 10;
		if (pageInfo.pageIdx != 1) {
			beginIndex = 10 * pageInfo.pageIdx - 9;
			endIndex = 10 * pageInfo.pageIdx;
		}
		
		String condition = "SELECT * from (select rownum num,x.*  FROM (" + conditionStr 
						 + ")x WHERE ROWNUM <= " + endIndex + ") where num >= " + beginIndex;
		try {
			pstmt = connection.prepareStatement(condition);
			rs = pstmt.executeQuery();
			if(rs != null){	
				ResultSetMetaData rsmd = rs.getMetaData();
			    numberOfColumns = rsmd.getColumnCount(); //求列数	
				while(rs.next()){
					kColl = new KeyedCollection();
					for(int i = 1; i <= numberOfColumns; i++){
						kColl.addDataField(rsmd.getColumnName(i).toLowerCase(),rs.getString(i));//把对应的字段名和字段值add到kColl里面
					}
					iColl.add(kColl);	//生成iColl
				}
			}
			pageInfo.setRecordSize(String.valueOf(getCount(connection, conditionStr)));
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new EMPException(e.getMessage());
			}
		}
		return iColl;
	}
	
	/**
	 * 获取查询的记录总数
	 * @param conn
	 * @param conditionStr
	 * @return int
	 * @throws SQLException 
	 * @throws SQLException,InvalidArgumentException,DuplicatedDataNameException
	 */
	public int getCount(Connection conn, String conditionStr) throws EMPException{
		int count = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql = "select count(*) count from (" + conditionStr + " )";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				count = Integer.parseInt(rs.getString("count")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		} finally {
			try{
				if (rs != null){
					rs.close();
					rs = null;
				}
				if (pstmt != null){
					pstmt.close();
					pstmt = null;
				}
			}catch(SQLException e){
				e.printStackTrace();
				throw new EMPException(e.getMessage());
			}
		}
		return count;
	}
	
	/**
	 * 向表中插入一条记录
	 * 
	 * @param tableName	表名
	 * @param valueMap	需要插入的字段MAP
	 * @param conn		连接
	 * @return boolean
	 * @throws EMPException
	 */
	public boolean insertValuesByTableAndFieldMap(String tableName,
			HashMap<String, String> valueMap,Connection conn) throws EMPException {
		PreparedStatement pstmt = null;
		String retSql = "";   //insert into [tableName] (retSql) values (valueSql);
		String valueSql = "";
		String insertSql = ""; // 插入的SQL语句 ==  insert into (tableName) (retSql) values (valueSql);
		boolean success = false;
		int count = 0;
		try {

			Iterator conditionIt = valueMap.entrySet().iterator(); // 拼成insert SQL
			while (conditionIt.hasNext()) {
				Map.Entry conditionEntry = (Map.Entry) conditionIt.next();
				if ("".equals(retSql)) {
					retSql += "( " + conditionEntry.getKey();
				} else {
					retSql += ", " + conditionEntry.getKey();
				}
				if ("".equals(valueSql)) {
					valueSql += "( '" + conditionEntry.getValue() + "'";
				}else {
					valueSql += ", '" + conditionEntry.getValue() + "'";
				}
			}
			retSql += ")";
			valueSql += ")";
			
			insertSql = "insert into " + tableName + retSql + " values " + valueSql; // insert SQL
			System.out.println("insert SQL ::: \n" + insertSql);
			
			pstmt = conn.prepareStatement(insertSql);
			count = pstmt.executeUpdate();
			
			if(count == 1){
				success = true;
			}else{
				success = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
		
	}
}

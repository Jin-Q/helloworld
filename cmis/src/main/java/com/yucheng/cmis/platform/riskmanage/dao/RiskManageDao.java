/**
 * 
 */
package com.yucheng.cmis.platform.riskmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.riskmanage.domain.IqpPvRiskResult;
import com.yucheng.cmis.pub.CMISDao;

/**
 * @author zhangpu
 * @since 1.2
 */
public class RiskManageDao extends CMISDao {

	/**
	 * 日志类
	 */
	// private static final Logger log = Logger.getLogger(RiskManageDao.class);
	/**
	 * 根据主表外键删除子表信息
	 * 
	 * @param modelId
	 *            表模型ID
	 * @param pkId
	 *            主键编号
	 * @param pkValue
	 *            主键值
	 * @param conn
	 *            链接
	 * @return int 是否成功
	 * @throws Exception
	 */
	public int deleteSubTabByPk(String modelId, String pkId, String pkValue,
			Connection conn) throws Exception {
		int returnFlag = 1;
		try {
			String ExecuteSql = " delete " + modelId + " where " + pkId
					+ " = ?";
			// log.info("执行SQL:" + ExecuteSql + "参数值：" + pkValue);
			PreparedStatement ps = conn.prepareStatement(ExecuteSql);
			ps.setString(1, pkValue);
			try {
				ps.execute();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				if (ps != null) {
					ps.close();
				}
			}
		} catch (Exception e) {
			returnFlag = 0;
			throw e;
		}
		return returnFlag;
	}

	/**
	 * 根据表名以及条件删除相应的记录
	 * 
	 * @param tableName
	 *            表名
	 * @param condition
	 *            条件mp
	 * @param con
	 *            连接
	 * @throws Exception
	 */
	public void deleteTableBycondition(String tableName, Map condition,
			Connection con) throws Exception {

		String sql = "delete from " + tableName + " where '1'='1' ";
		Iterator it = condition.keySet().iterator();
			while (it.hasNext()) {
				String tmStr = (String) it.next();
				sql += " and " + tmStr + "='" + condition.get(tmStr) + "'";
	
			}
			PreparedStatement ps = con.prepareStatement(sql);
			try {
				ps.execute();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally {
				if (ps != null) {
					ps.close();
				}
			}
	}
	
	public List ListgetAllItem(String condition,Connection con) throws SQLException{
		
		List ls= new ArrayList();
		String sql = "select * from IQP_PV_RISK_RESULT where "+condition;
		
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		IqpPvRiskResult iqppvriskresult = null;
		
		try {
			
			while(rs.next()){
				
				iqppvriskresult=new IqpPvRiskResult();
				
				String passState = rs.getString("PASS_STATE");
				iqppvriskresult.setItemDesc(rs.getString("ITEM_DESC"));
				iqppvriskresult.setItemDesc(rs.getString("ITEM_DESC"));
				iqppvriskresult.setItemId(rs.getString("ITEM_ID"));
				iqppvriskresult.setItemName(rs.getString("ITEM_NAME"));
				iqppvriskresult.setPassState(rs.getString("PASS_STATE"));
				iqppvriskresult.setRiskLevel(rs.getString("RISK_LEVEL"));
				iqppvriskresult.setLinkUrl(rs.getString("LINK_URL"));
				ls.add(iqppvriskresult);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
		return ls;
		
		
	}
	
	public List getPreRiskItems(String serno,Connection con) throws SQLException{
		
		List ls= new ArrayList();
		String sql = "select distinct SERNO,wfid,node_id,RISK_LEVEL," +
				"PASS_STATE,ITEM_DESC,ITEM_ID from iqp_pv_risk_result where serno='"+serno+"'";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		IqpPvRiskResult iqppvriskresult = null;
		try {
		
			while(rs.next()){
				
				iqppvriskresult=new IqpPvRiskResult();
				
				String passState = rs.getString("PASS_STATE");
				if(passState.equals("2"))
					iqppvriskresult.setItemDesc(rs.getString("ITEM_DESC")+"[拦截]");
				else{
					iqppvriskresult.setItemDesc(rs.getString("ITEM_DESC"));
				}
				iqppvriskresult.setItemId(rs.getString("ITEM_ID"));
				iqppvriskresult.setPassState(rs.getString("PASS_STATE"));
				iqppvriskresult.setRiskLevel(rs.getString("RISK_LEVEL"));
				
				ls.add(iqppvriskresult);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
		return ls;
		
	}
	 /**
     * 通过拦截方案ID获取拦截场景列表
     * @param preventIdValue 拦截方案ID
     * @param connection
     * @return
     * @throws Exception
     */
	public IndexedCollection queryListByPreventId(String preventIdValue, Connection connection) throws Exception {
		IndexedCollection ic = new IndexedCollection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select distinct(wfid) from prd_pv_risk_scene where prevent_id=?";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, preventIdValue);
			rs = pstmt.executeQuery();
			while(rs.next()){
				KeyedCollection kc = new KeyedCollection();
				kc.addDataField("prevent_id", preventIdValue);
				kc.addDataField("wfid", rs.getString("wfid"));
				ic.add(kc);
			}
		} catch (Exception e) {
			throw new Exception();
		} finally {
			if(pstmt != null){
				pstmt.close();
				pstmt = null;
			}
		}
		return ic;
	}
	/**
	 * 执行SQL，返回执行结果，特殊情况使用
	 * @param sql 执行SQL
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection doSqlExecute(String sql ,Connection connection) throws Exception {
		IndexedCollection ic = new IndexedCollection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs!=null){
				   ResultSetMetaData rsm = rs.getMetaData();
		           int column = rsm.getColumnCount();
		           while (rs.next()) {
		        	   KeyedCollection rowKcoll=new KeyedCollection("");
		               for (int i = 1; i <= column; i++) {
		            	   String key = rsm.getColumnName(i);
		            	   key=key.toLowerCase();
		                   Object value = rs.getObject(i);
		                   rowKcoll.addDataField(key, value);
		               }
		               ic.addDataElement(rowKcoll);
		           } 
			   }   
		} catch (Exception e) {
			throw new Exception();
		} finally {
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
		}
		return ic;
	}
	/**
	 * 执行删除SQL语句
	 * @param sql 删除SQL
	 * @param connection
	 * @throws Exception
	 */
	public void doDeleteSqlExecute(String sql ,Connection connection) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.execute();
			
		} catch (Exception e) {
			throw new Exception();
		} finally {
			if(pstmt != null){
				pstmt.close();
				pstmt = null;
			}
		}
	}
}

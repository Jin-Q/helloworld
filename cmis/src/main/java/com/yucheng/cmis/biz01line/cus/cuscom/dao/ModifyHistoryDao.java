package com.yucheng.cmis.biz01line.cus.cuscom.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.ModifyHistory;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class ModifyHistoryDao extends CMISDao {
	/**
	 * 把修改记录的XML字符串插入数据库中
	 * PS. sql语句如果使用for update时会有死锁问题,所以使用同步
	 * @param modifyHistory
	 * @param conn
	 * @throws EMPException
	 */
	public synchronized void insertBlobData(ModifyHistory modifyHistory, Connection conn) throws EMPException {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			//先插入空的BLOB值
			String insertSql = "insert into modify_history(key_id, table_name, cus_id, modify_record, modify_time, modify_user_id, modify_user_ip, modify_status, modify_user_br_id) values (?, ?, ?, EMPTY_BLOB(), ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(insertSql);
			ps.setString(1, modifyHistory.getKeyId());
			ps.setString(2, modifyHistory.getTableName());
			ps.setString(3, modifyHistory.getCusId());
			ps.setString(4, modifyHistory.getModifyTime());
			ps.setString(5, modifyHistory.getModifyUserId());
			ps.setString(6, modifyHistory.getModifyUserIp());
			ps.setString(7, modifyHistory.getModifyStatus());
			ps.setString(8, modifyHistory.getModifyUserBrId());
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,
					EMPLog.WARNING, 0, "insertSql: " + insertSql);
			ps.executeUpdate();

			//从数据库取出该BLOG值,修改这个值
			String selectSql = "select modify_record from modify_history where key_id ='"
					+ modifyHistory.getKeyId() + "'";
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,
					EMPLog.WARNING, 0, "selectSql: " + selectSql);
			ps1 = conn.prepareStatement(selectSql);
			rs = ps1.executeQuery();
			if (rs.next()) {
				Blob blob = rs.getBlob("modify_record");
				blob.setBytes(1, modifyHistory.getModifyRecord().getBytes()); 
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
			throw new EMPException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (ps1 != null) {
					ps1.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
	}

	/**
	 * 从数据库中取到修改的详细信息的XML字符串
	 * @param keyId
	 * @param conn
	 * @return
	 * @throws EMPException
	 */
	public String getDetailKColl(String keyId, Connection conn)
			throws EMPException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select modify_record from modify_history where key_id='"+ keyId + "'";
			ps = conn.prepareStatement(sql);
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,
					EMPLog.WARNING, 0, "sql: " + sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				Blob record = rs.getBlob("modify_record");
				byte[] bytes = record.getBytes(1, (int) record.length());
				return new String(bytes, "GBK");
			}

		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	
	/**
	 * 根据表模型删除原来的配置字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String deleteModifyCfgByModelId(String model_id,Connection conn) throws EMPException, SQLException {
		SqlClient.executeUpd("delModifyHistoryCfg", model_id, null, null, conn);
		return "";
	}
	
	/**
	 * 根据表模型插入字段配置信息
	 * @param model_id
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertModifyCfgByModelId(IndexedCollection iColl,Connection conn) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into modify_history_cfg (model_id,column_name,opttype,popordic) values ('"+kCollTmp.getDataValue("model_id")+"','"+kCollTmp.getDataValue("column_name")+"','"+kCollTmp.getDataValue("opttype")+"','"+kCollTmp.getDataValue("popordic")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log(PUBConstant.MODIFY_HISTORY_COMPONENT,EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
}

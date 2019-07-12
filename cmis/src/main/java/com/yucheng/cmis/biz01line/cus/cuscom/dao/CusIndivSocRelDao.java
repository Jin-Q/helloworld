package com.yucheng.cmis.biz01line.cus.cuscom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname CusComManagerDao.java
 *@Version 1.0
 *@Since 1.0 2008-9-18 下午03:24:05
 *@Copyright yuchengtech
 *@Author yuch
 *@Description：
 *@Lastmodified
 *@Author
 */
public class CusIndivSocRelDao {

	/**
	 * 检查社会关系是否存在
	 * 
	 * @param cusId
	 * @param relType
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String checkSocRelExist(String cusId, String relType,
			String cusIdRel, Connection conn) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String message = "n";// 返回信息，默认为不存在
		int num = 0;
		String sql = "select count(*) as num from cus_indiv_soc_rel where cus_id='"
				+ cusId
			//	+ "' and indiv_cus_rel='"
			//	+ relType
				+ "' and cus_id_rel='" + cusIdRel + "'";// 根据客户代码和与客户关系判断是否存在社会关系
		System.out.println("校验是否存在的SQL"+sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("num");
				if (num > 0) {
					message = "y";// 如果NUM>0则表示存在该社会关系
				}
			}
		} catch (SQLException e) {
			throw new ComponentException();
		} finally {
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
				throw new ComponentException();
			}
		}
		return message;
	}
	
	public String checkLegalSpsExist(String cusId,Connection conn)throws ComponentException{
		Statement stmt = null;
		ResultSet rs = null;
		String message = "n";// 返回信息，默认为不存在
		int num = 0;
		String sql = "select count(*) as num from cus_indiv_soc_rel where cus_id='"
				+ cusId
				+ "' and indiv_cus_rel='1'";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("num");
				if (num > 0) {
					message = "y";// 如果NUM说明该公司法人存在配偶信息，不能进行新增操作
				}
			}
		} catch (SQLException e) {
			throw new ComponentException();
		} finally {
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
				throw new ComponentException();
			}
		}
		return message;

	} 
}

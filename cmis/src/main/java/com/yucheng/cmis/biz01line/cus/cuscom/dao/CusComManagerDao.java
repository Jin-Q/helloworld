package com.yucheng.cmis.biz01line.cus.cuscom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComManager;

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
public class CusComManagerDao {

	/**
	 * 根据对公客户号查询到高管表查询法人代表信息
	 * 
	 * @param cusId
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public CusComManager getCusComManager(String cusId, Connection conn) throws SQLException {
		CusComManager ccm = new CusComManager();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String sql = "select cus_id_rel from cus_com_manager where cus_id='"
					+ cusId + "' and com_mrg_typ='02'";
			System.out.println("取法人高管SQL" + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ccm.setCusIdRel(rs.getString("cus_id_rel"));// 法人代表客户号
			}
		} catch (SQLException e) {
			throw e;
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
				throw e;
			}
		}

		return ccm;
	}
	/**根据客户号和高管类别校验是否存在相同的高管类别
	 * 如果高管类别是法人代表、总经理、财务负责人、董事长，则只能唯一
	 * @author ZhouJianFeng
	 * @param comMrgType
	 * @param cusId
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public boolean getCntByComMrgType(String com_mrg_typ,String cusId,Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		boolean flag = false;//默认不存在
		try {
			stmt = conn.createStatement();
			String sql = "select count(*) cnt from cus_com_manager " +
					"where cus_id='"+cusId+"' and com_mrg_typ ='"+com_mrg_typ+"'";
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				if(rs.getInt(1)>0){
					flag = true;
				}
			}
		} catch (SQLException e) {
			throw e;
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
				throw e;
			}
		}
		return flag;
	}
}
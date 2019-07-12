package com.yucheng.cmis.biz01line.fnc.detail.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FncDetailBaseDao {
	
	public String getPkByCusIdAndYm(String cusId,String ym,Connection conn) {
		
		String pk="";
		Statement stmt=null;
		ResultSet rs=null;

		try {
			stmt = conn.createStatement();
			String sql = "select pk  from fnc_detail_base "
					+ "where CUS_ID='" + cusId+"'"
					+ "  and FNC_YM='" + ym+"'";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				pk=(rs.getString("pk"));	
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
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
				if (conn != null) {
					//conn.close();
					//conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pk;
	}
}

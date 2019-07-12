package com.yucheng.cmis.biz01line.cus.cusbase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yucheng.cmis.pub.CMISDao;

public class CusTrusteeAppDao extends CMISDao {

	
	public int updateCusHandoverAppStatus(String serno,String status,Connection conn){
		
		int intReturnMessage=0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update cus_trustee_app set status=? where serno=? ");
			pstmt.setString(1, status);
			pstmt.setString(2, serno);
			pstmt.executeUpdate();
			intReturnMessage=1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if(conn != null) {
					//conn.close();
					//conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return intReturnMessage;
	}
	
	public int updateLogRetractDate(String serno,String date,Connection conn){
		
		int intReturnMessage=0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update cus_trustee_log set retract_date=? where serno=? ");
			pstmt.setString(1, date);
			pstmt.setString(2, serno);
			pstmt.executeUpdate();
			intReturnMessage=1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if(conn != null) {
					//conn.close();
					//conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return intReturnMessage;
	}
	
	public boolean hasCusTrusteeList(String serno, Connection conn){
		Statement stmt=null;
		ResultSet rs=null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "select * from  cus_trustee_lst where serno='" + serno + "'";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean getExistCusTrustee(String consignorId,String trusteeId, Connection conn){
		Statement stmt=null;
		ResultSet rs=null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "select * from  cus_trustee where consignor_id = '"+consignorId+"' and trustee_id = '"+trusteeId+"'";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	

	public boolean checkCusHandoverApp(String managerId, String consignorId,Connection conn) {
		// TODO Auto-generated method stub
		
		Statement stmt=null;
		ResultSet rs=null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "select * from  cus_trustee_app where consignor_id='" + consignorId + "' and trustee_id='"+managerId+"' and status not in('997','998')";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	public boolean hasCusTrustee(String consignorId,String trustee_id,Connection con){
		
		Statement stmt=null;
		ResultSet rs=null;
		String sql;
		try {
			stmt = con.createStatement();
			sql = "select * from  cus_trustee where consignor_id='" + consignorId + "' and trustee_id='"+trustee_id+"'";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;		
	}
	
	public boolean hasCusTrusteeApp(String consignorId,String trustee_id,Connection con){
		
		Statement stmt=null;
		ResultSet rs=null;
		String sql;
		try {
			stmt = con.createStatement();
			sql = "select * from  cus_trustee_app where consignor_id='" + consignorId + "' and trustee_id='"+trustee_id+
					"' and approve_status in('000','111','991','992')";
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;		
	}
}

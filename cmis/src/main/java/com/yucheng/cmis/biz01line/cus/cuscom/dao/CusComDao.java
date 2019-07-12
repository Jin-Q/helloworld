package com.yucheng.cmis.biz01line.cus.cuscom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusComDao extends CMISDao {

	/**
	 * 
	 * @param GrpMode
	 *            集团客户类型
	 * @param GrpNo
	 *            集团编号
	 * @param innerCusId
	 *            内部客户代码
	 * @return int 返回信息码
	 * @throws EMPException
	 * @see
	 */
	public int updateGrpModeAndNo(String GrpMode, String GrpNo,String CusId, Connection conn) throws ComponentException {
		int intReturnMessage = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update Cus_Com set Com_Grp_Mode=?,GRP_NO=? where cus_id=?");
			pstmt.setString(1, GrpMode);
			pstmt.setString(2, GrpNo);
			pstmt.setString(3, CusId);
			pstmt.executeUpdate();
			intReturnMessage = 1;
		} catch (SQLException e) {
			throw new ComponentException();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw new ComponentException();			}
		}

		return intReturnMessage;
	}
	
	/**
	 * 
	 * @param GrpMode 集团客户类型
	 * @param GrpNo 集团编号
	 * @param innerCusId 内部客户代码
	 * @return int 返回信息码
	 * @throws EMPException
	 * @see
	 */
	public int updateGrpJiesanModeAndNo(String GrpMode, String GrpNo,
			Connection conn) throws ComponentException {
		int intReturnMessage = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("update Cus_Com set Com_Grp_Mode=?,GRP_NO=null where cus_id in (select cus_id from cus_grp_member where grp_no = ?)");
			pstmt.setString(1, GrpMode);
			pstmt.setString(2, GrpNo);
			pstmt.executeUpdate();
			intReturnMessage = 1;
		} catch (SQLException e) {
			throw new ComponentException();

		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw new ComponentException();
			}
		}
		return intReturnMessage;
	}

	/**
	 * 检查贷款卡号是否在系统中存在
	 * 
	 * @param loanCardId
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String checkLoanCardIdExist(String loanCardId, String cusNo,Connection conn) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String message = "n";
		int num = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(*) as num from (select loan_card_id from cus_base where loan_card_id='"+loanCardId+"' and cus_id<>'"+cusNo+"') " ;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("num");
				if (num > 0) {
					message = "y";
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

	/**
	 * 根据证件类型和证件号码检查其在系统同是否已经存在
	 * 
	 * @param certCode
	 *            　证件号码
	 * @param certType
	 *            　证件类型
	 * @param cusType
	 *            客户类型
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String checkCertCodeExist(String certCode, String certType, Connection conn) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String message = "n";
		int num = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(*) as num from cus_base where cert_type='"+ certType + "' and cert_code='" + certCode + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("num");
				if (num > 0) {
					message = "y";
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

	/**
	 * 根据帐号判断基本存款账户和一般结算账户是否在系统中存在
	 * 
	 * @param accNo
	 * @param accType
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String AccExist(String accNo, String accType, String cusNo,Connection conn) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String message = "n";
		int num = 0;
		try {
			stmt = conn.createStatement();
			String sql = "";
			if (accType.equals("bas")) {
				sql = "select count(*) as num from cus_com where bas_acc_no='"+ accNo + "' and cus_id <> '" + cusNo + "'";// 基本存款账户
			}
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("num");
				if (num > 0) {
					message = "y";
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

	/**
	 * 根据GRO_NO删除成员表
	 * 
	 * @param grpNo
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public int deleteGrpMemberByGrpNo(String grpNo, Connection conn)
			throws ComponentException {
		Statement stmt = null;
		int num = 0;
		try {
			stmt = conn.createStatement();
			String sql = "delete from cus_grp_member where grp_no='" + grpNo+ "' and gen_type='1' and grp_corre_type <> '3'";
			num = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw new ComponentException();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new ComponentException();
			}
		}
		return num;
	}
	
	/**
	 * 执行查询sql
	 */
	public String querySql(String sql) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String message = "false";
		try {
			stmt = this.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				message = rs.getString("cc");
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
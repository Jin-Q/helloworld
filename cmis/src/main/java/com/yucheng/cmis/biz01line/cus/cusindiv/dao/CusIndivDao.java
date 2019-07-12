package com.yucheng.cmis.biz01line.cus.cusindiv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusIndivDao extends CMISDao {

	public String checkSPS(String cusId, Connection conn) throws Exception {
		String returnMessage = "n";
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int num = 0;
		try {
			stmt = conn.createStatement();
			sql = "select count(*) as no from cus_indiv_soc_rel where cus_id='"
					+ cusId + "' and indiv_cus_rel='1'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("no");
				if (num > 0) {
					returnMessage = "y";
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

		return returnMessage;
	}

	//检查父母是否已存在两个
	public String checkParents(String cusId, Connection conn) throws Exception {
		String returnMessage = "n";
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int num = 0;
		try {
			stmt = conn.createStatement();
			sql = "select count(*) as no from cus_indiv_soc_rel where cus_id='"
					+ cusId + "' and indiv_cus_rel='2'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("no");
				if (num > 1) {
					returnMessage = "y";
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

		return returnMessage;
	}
	
	/**
	 * @param cusId 客户号
	 * @param acctNo 帐号
	 * @param orgName  行名
	 * @param conn
	 * @return 同一帐号的数量
	 * @throws Exception 
	 */
	public int getAcctNoCount(String cusId, String acctNo, String orgName,
			Connection conn) throws Exception {
		int intflag = 0;
		PreparedStatement ps = null;
		String sql = "select count(*) as no from  cus_obis_deposit WHERE cus_id='"
				+ cusId + "'" + " and org_name='" + orgName + "' and acct_no='"
				+ acctNo + "'";
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				intflag = rs.getInt("no");
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			throw e;
		}
		return intflag;
	}

	public String checkExistCusObisDeposit(String accNo, Connection conn)
			throws SQLException {
		PreparedStatement ps = null;
		String cus_id = "";
		String sql = "select *  from  cus_obis_deposit WHERE  acc_no='" + accNo + "'";
		ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			cus_id = rs.getString("cus_id");
		}

		return cus_id;
	}

	// 根据CUS_ID删除配偶信息
	public int deleteSpsByCusId(String cusId, Connection conn)
			throws Exception {
		int intReturnMessage = 0;
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String sql = "delete from cus_indiv_soc_rel where cus_id='" + cusId + "' and indiv_cus_rel='1'";
			stmt.executeUpdate(sql);
			sql = "delete from cus_indiv_soc_rel where cus_id_rel='" + cusId + "' and indiv_cus_rel='1'";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}
		return intReturnMessage;
	}

	/**
	 * 根据证件类型，证件号码从CUS_BANK_RELATION表取出的"客户与我行关系"信息
	 * 
	 * @param String
	 *            certTyp 证件类型
	 * @param String
	 *            certCode 证件号码
	 * @return String cusBankRel 客户与我行关系
	 * @throws ComponentException
	 *             added by tzb 2009-12-24
	 */
	public String getIndivCusBankRelat(String certTyp, String certCode,
			Connection conn) throws Exception {
		// 客户与我行关系
		String cusBankRelat = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "select b.cus_bank_relat as cus_bank_relat from Cus_Indiv a ,Cus_Bank_Relation b "
					+ "where a.cert_type = b.cert_type and a.cert_type = b.cert_type and a.cert_type='"
					+ certTyp + "'" + "  and a.cert_code='" + certCode + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cusBankRelat = rs.getString("cus_bank_relat");
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

		return cusBankRelat;
	}

	/**
	 * 
	 * @param cusId
	 * @param cusIdRel
	 * @param relType
	 * @param conn
	 * @return
	 * @throws EMPException
	 */
	public int deleteCusIndivSocRel(String cusId,String cusIdRel,String relType,Connection conn) throws Exception{
		Statement stmt = null;
		String sql = null;
		int i = 0;
		try{
			stmt = conn.createStatement();
			sql = "delete  from cus_indiv_soc_rel where cus_id='"+cusId+"' and cus_id_rel='"+cusIdRel+"'";
			stmt.executeUpdate(sql);
			sql = "delete  from cus_indiv_soc_rel where cus_id='"+cusIdRel+"' and cus_id_rel='"+cusId+"'";
			stmt.execute(sql);
		}catch(SQLException e){
			throw e;
		}finally{
			if(stmt != null ){
				try {
					stmt.close();
				} catch (SQLException e) {
					throw e;
				}
				stmt = null;
			}
		}
		return i;
	}
	/**
	 * 2015-01-28 根据客户码更新手机号码
	 * @param cusId
	 * @param phone
	 * @param conn
	 * @return
	 * @throws Exception
	 * @author FChengLiang
	 */
	public int updateMobileByCusId(String cusId,String mobile,Connection conn) throws Exception{
		Statement stmt = null;
		String sql = null;
		int i = 0 ;
		try{
			stmt = conn.createStatement();
			sql = "update cus_indiv set mobile='"+mobile+"' where cus_id='"+cusId+"'";
			i = stmt.executeUpdate(sql);
		}catch(SQLException e){
			throw e;
		}finally{
			if(stmt != null ){
				try {
					stmt.close();
				} catch (SQLException e) {
					throw e;
				}
				stmt = null;
			}
		}
		return i;
	}
}

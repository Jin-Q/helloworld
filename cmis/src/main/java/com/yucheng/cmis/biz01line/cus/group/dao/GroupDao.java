package com.yucheng.cmis.biz01line.cus.group.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMemberApply;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class GroupDao extends CMISDao {

	/**
	 * 根据集团成员的客户码查询他所在的集团信息
	 * 
	 * @param cusId
	 *            集团成员客户码
	 * @param conn
	 * @return rt 集团信息的LIST
	 * @throws SQLException 
	 */
	public List<CusGrpInfo> findCusGrpInfoByMemCusId(String cusId, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		List<CusGrpInfo> rt = new ArrayList<CusGrpInfo>();

		try {
			stmt = conn.createStatement();
			String sql = "select * from CUS_GRP_INFO where grp_no in"
					+ "(select grp_no from CUS_GRP_MEMBER where cus_id='"
					+ cusId + "')";
			System.out.println(">>>>>>>>>>>>>" + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				CusGrpInfo cgi = new CusGrpInfo();
				cgi.setGrpNo(rs.getString("grp_no"));
				cgi.setGrpName(rs.getString("grp_name"));
				cgi.setParentCusId(rs.getString("parent_cus_id"));
				cgi.setGrpFinanceType(rs.getString("grp_finance_type"));
				cgi.setGrpDetail(rs.getString("grp_detail"));
				cgi.setManagerBrId(rs.getString("manager_br_id"));
				cgi.setManagerId(rs.getString("manager_id"));
				cgi.setInputId(rs.getString("input_id"));
				cgi.setInputDate(rs.getString("input_date"));
				cgi.setInputBrId(rs.getString("input_br_id"));
				rt.add(cgi);
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
		return rt;
	}

	/**
	 * 查询指定的客户码是否作为母公司已存在
	 * 
	 * @param cusId
	 *            客户代码
	 * @return 信息编码
	 * @throws AgentException
	 * @throws Exception 
	 */
	public int CheakParCusId(String cusId, Connection conn)
			throws Exception {

		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		int count = 0,a =0,b =0;

		try {
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			String sql1 = "select count(*) a from cus_grp_info WHERE "+ "parent_cus_id= '" + cusId + "'";
			String sql2 = "select count(*) b from cus_grp_info_apply where parent_cus_id='" + cusId + "'" +
					" and approve_status not in('997','998','990')";
			
			rs1 = stmt1.executeQuery(sql1);
			rs2 = stmt2.executeQuery(sql2);
			
			if(rs1.next()){
				a = rs1.getInt("a"); 
			}
			if(rs2.next()){
				b = rs2.getInt("b"); 
			}
			
			if ((a>0) || (b>0)) {
				return 1;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {

				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (rs2 != null) {
					rs2.close();
					rs2 = null;
				}
				if (stmt1 != null) {
					stmt1.close();
					stmt1 = null;
				}
				if (stmt2 != null) {
					stmt2.close();
					stmt2 = null;
				}
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		return count;
	}
	/**
	 * 查询指定的客户码是否作为母公司已存在,集团客户表和集团客户申请表中查询
	 * 
	 * @param cusId 客户代码
	 * @return 信息编码
	 * @throws AgentException
	 */
	public int CheakParCusIdApply(String cusId,Connection conn) throws Exception {
		Statement stmt1 = null;
		ResultSet rs1 = null;
		int count = 0,a =0;
		try {
			stmt1 = conn.createStatement();
			String sql1 = "select count(*) a from cus_grp_info WHERE "
					+ "parent_cus_id= '" + cusId + "'";
			rs1 = stmt1.executeQuery(sql1);
			if(rs1.next()){
				a = rs1.getInt("a"); 
			}
			if (a>0) {
				count = 1;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (stmt1 != null) {
					stmt1.close();
					stmt1 = null;
				}
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		return count;
	}
	
	/**
	 * 根据客户码查询是否存在集团中
	 * 
	 * @param cusId
	 *            客户码
	 * @return String
	 * @throws Exception 
	 * @throws EMPException
	 */
	public CusGrpMember findCusGrpInfoByCusId(String cusId, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		CusGrpMember cgm = new CusGrpMember();

		try {
			stmt = conn.createStatement();
			String sql = "select grp_no,cus_id,grp_corre_type from cus_grp_member where " +
					"cus_id ='"	+ cusId + "'";
			// System.out.println(">>>>>>>>>>>>>" + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cgm.setGrpNo(rs.getString("grp_no"));
				cgm.setCusId(rs.getString("cus_id"));
				cgm.setGrpCorreType(rs.getString("grp_corre_type"));
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
		return cgm;
	}
	/**
	 * 根据客户码查询是否存在集团中
	 * 
	 * @param cusId
	 *            客户码
	 * @return String
	 * @throws Exception 
	 * @throws EMPException
	 */
	public CusGrpMemberApply findCusGrpInfoApplyObjectByCusId(String cusId, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		CusGrpMemberApply cgma = new CusGrpMemberApply();

		try {
			stmt = conn.createStatement();
			String sql = "select grp_no,cus_id,grp_corre_type from cus_grp_member_apply where cus_id ='"
					+ cusId + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cgma.setGrpNo(rs.getString("grp_no"));
				cgma.setCusId(rs.getString("cus_id"));
				cgma.setGrpCorreType(rs.getString("grp_corre_type"));
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
		return cgma;
	}
	
	/**
	 * 根据客户码查询是否存在集团中 是否有在途申请
	 * 
	 * @param cusId
	 *            客户码
	 * @return String
	 * @throws Exception 
	 * @throws EMPException
	 */
	public int findCusGrpInfoApplyIntByCusId(String cusId, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select count(1) as count from cus_grp_member_apply "
					+ "where  serno in (select serno from cus_grp_info_apply where approve_status in  ('111','991','992','000','993' ))"
					+ "and cus_id = '"+cusId+"'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;
	}
	/**
	 * 根据客户码查询是否存在集团成员和集团成员申请中
	 * 
	 * @param cusId
	 *            客户码
	 * @return String 存在返回1 否则返回 0
	 * @throws Exception 
	 * @throws EMPException
	 */
	public int findCusGrpInfoApplyByCusId(String cusId, Connection conn) throws Exception {
		Statement stmt1 = null;
		ResultSet rs1 = null;
		int a = 0;
		try {
			stmt1 = conn.createStatement();
			String sql1 = "select count(*) a from cus_grp_member where cus_id ='" + cusId + "'";
			
			rs1 = stmt1.executeQuery(sql1);
			if(rs1.next()){
				a = rs1.getInt("a");
			};
			if (a>0) {
				return 1;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (stmt1 != null) {
					stmt1.close();
					stmt1 = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}
		return 0;
	}

	// 根据集团号删除集团成员信息 add by zhoujf 2009.07.26
	public int deleteGrpMemberByGrpNo(String grpNo, Connection conn)
			throws Exception {
		Statement stmt = null;
		int flag = 0;
		String sql = "";
		try {
			sql = "delete from cus_grp_member where grp_no='" + grpNo + "'";
			stmt = conn.createStatement();
			flag = stmt.executeUpdate(sql);
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
		return flag;
	}

	public CusGrpInfo getCusGrpInfoDomainByCusId(String cus_id, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		CusGrpInfo cgi = new CusGrpInfo();

		try {
			stmt = conn.createStatement();
			String sql = "select " +
					"grp_no,grp_name,parent_cus_id,grp_finance_type,grp_detail,manager_br_id,manager_id,input_id,input_date,input_br_id " +
					"from cus_grp_info " +
					"where grp_no in(select grp_no from cus_grp_member where cus_id='"+cus_id+"')";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cgi.setManagerId(rs.getString("manager_id"));
				cgi.setGrpDetail(rs.getString("grp_detail"));
				cgi.setGrpFinanceType(rs.getString("grp_finance_type"));
				cgi.setGrpName(rs.getString("grp_name"));
				cgi.setGrpNo(rs.getString("grp_no"));
				cgi.setInputBrId(rs.getString("input_br_id"));
				cgi.setInputDate(rs.getString("input_date"));
				cgi.setInputId(rs.getString("input_id"));
				cgi.setManagerBrId(rs.getString("manager_br_id"));
				cgi.setParentCusId(rs.getString("parent_cus_id"));
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
		return cgi;
	}
	//将cus_grp_member_apply插入到cus_grp_member
	public void insertCusGrpMember(String serno,String cusId,Connection conn) throws DaoException{
		String sql = "insert into cus_grp_member " +
				"(grp_no,cus_id,grp_corre_type,grp_corre_detail," +
				"input_id,input_date,input_br_id,gen_type) " +
				"select d.grp_no,d.cus_id,d.grp_corre_type,d.grp_corre_detail," +
				"d.input_id,d.input_date,d.input_br_id,d.gen_type" +
				" from cus_grp_member_apply d where d.serno ='"+serno+"' and d.cus_id ='"+cusId+"'";
		PreparedStatement psmt = null;
		
		try {
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0,sql);
			psmt = conn.prepareStatement(sql);
			psmt.execute();
			psmt.clearParameters();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}finally{
			try {
				if(psmt!=null)
				   psmt.close();
				   psmt = null;
			} catch (SQLException e) {
				throw new DaoException(e.getMessage());
			}
		}
	}
	
	/**
	 * 关联集团新增申请 
	 * 将cus_grp_info_apply插入到cus_grp_info
	 * @param serno
	 * @param conn
	 * @throws DaoException
	 */
	public void insertCusGrpInfo(String serno,Connection conn) throws DaoException{
		String sql = "insert into cus_grp_info a" +
				"(a.grp_no,a.grp_name,a.parent_cus_id," +
				"a.grp_finance_type,a.grp_detail,a.manager_br_id,a.manager_id,a.input_id,a.input_date,a.input_br_id) " +
				"select b.grp_no,b.grp_name,b.parent_cus_id," +
				"b.grp_finance_type,b.grp_detail,b.manager_br_id,b.manager_id,b.input_id,b.input_date,b.input_br_id " +
				" from cus_grp_info_apply b where b.serno='"+serno+"'";
		PreparedStatement psmt = null;
		
		try {
			EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0,sql);
			psmt = conn.prepareStatement(sql);
			psmt.execute();
			psmt.clearParameters();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}finally{
			try {
				if(psmt!=null)
				   psmt.close();
				   psmt = null;
			} catch (SQLException e) {
				throw new DaoException(e.getMessage());
			}
		}
		
	}
	
	/**
	 * 检查该客户是否存在CusGrpMember
	 * 返回1表示存在 0表示不再在
	 * @throws Exception 
	 * */
	public int isExistCusGrpMember(String cus_id,Connection con) throws DaoException, Exception{
		
		Statement stmt = null;
		ResultSet rs = null;
		int ct =0;
		try {
			stmt = con.createStatement();
			String sql = "select count(*) ct from cus_grp_member where cus_id= '" + cus_id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				ct = rs.getInt("ct");
			}
			if (ct >0) {
				return 1;
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
				if (con != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		return 0;
		
	}
	
	/*
	 * 查询该集团下是否存在集团成员 是 返回1，否 返回0
	 * 
	 * @param grp_no 集团编号
	 *            
	 * @return 信息编码
	 * @throws DaoException
	 * 
	 * */
	public int checkCusGrpMember(String serno,Connection conn) throws Exception{
		
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0, ct =0;

		try {
			stmt = conn.createStatement();
			String sql = "select count(*) ct from cus_grp_member_apply WHERE "
					+ "serno= '" + serno + "'";
//			System.out.println("sql is >>>>>>>>>>>>" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				ct = rs.getInt("ct");
			}
			if (ct >0) {
				return 1;
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
				if (conn != null) {
					// conn.close();
					// conn = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		return count;
		
	}
}

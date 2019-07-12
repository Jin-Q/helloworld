package com.yucheng.cmis.biz01line.cus.group.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusGrpMemberApplyDao extends CMISDao {
	/**
	 * 根据集团号 删除集团成员信息
	 * 
	 * @param grp_no
	 *            集团成员客户码
	 * @param conn
	 * @author g
	 * @throws SQLException 
	 */
	public int deteleCusGrpMemberApply(String grp_no,String serno,Connection conn) throws SQLException{
		String sql = "";
		Statement stmt = null;
		try {
			// 功能模块
			if(serno.equals("cusGrpChange")){
				sql = "delete from cus_grp_member where grp_no='" + grp_no+"' ";
			}else{
				sql = "delete from cus_grp_member_apply where grp_no='" + grp_no+"' and serno ='"+serno+"'";
			}
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			return 0;
		} catch (SQLException ex) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"删除数据出错:" + ex.toString());
//			return 1;
			throw ex;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				} catch (SQLException e) {
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"删除数据,关闭流出错:" + e.toString());
//					return 1;
					throw e;
				}
			}
		}
	}
	/**
	 * 根据集团号 和集团成员号 删除集团成员信息
	 * 
	 * @param grp_no
	 *            集团成员客户码
	 * @param conn
	 * @author xukaixi
	 * @throws SQLException 
	 */
	public int deteleSomeCusGrpMember(String cus_ids,Connection conn) throws SQLException{
		String sql = "";
		Statement stmt = null;
		try {
			// 功能模块
			sql = "delete from cus_grp_member where cus_id in ("+cus_ids+")";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			return 0;
		} catch (SQLException ex) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"删除数据出错:" + ex.toString());
//			return 1;
			throw ex;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				} catch (SQLException e) {
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"删除数据,关闭流出错:" + e.toString());
//					return 1;
					throw e;
				}
			}
		}
	}
	
	/**
	 * 将集团信息更新到客户信息
	 * @param act_type 是删除集团信息还是插入:delete/add
	 * @param grp_no 集团编号
	 * @return
	 * @throws ComponentException
	 */
	public void setGrpToCom(String act_type, String grp_no) throws ComponentException {
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			stmt = this.getConnection().createStatement();
			
			if(act_type.equals("delete")){
				/*** 删除旧集团信息 ***/
				sql = "update cus_com a set  a.grp_no = ''  where cus_id in (select cus_id from cus_grp_member where grp_no = '"+grp_no+"')";
				stmt.executeUpdate(sql);
			}else if(act_type.equals("add")){
				/*** 将集团信息更新到cus_com ***/
				sql = "update cus_com a set a.grp_no = '"+grp_no+ "' where cus_id in (select cus_id from cus_grp_member  where grp_no = '"+grp_no+"')";
				stmt.executeUpdate(sql);
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
	}

}

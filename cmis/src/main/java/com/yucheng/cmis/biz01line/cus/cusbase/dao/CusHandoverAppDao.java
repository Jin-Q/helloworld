package com.yucheng.cmis.biz01line.cus.cusbase.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.DaoException;

public class CusHandoverAppDao extends CMISDao {
	/**
	 * 根据客户移交申请流水号更新申请状态
	 * @param serno
	 * @param status
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int updateCusHandoverAppStatus(String serno, String status,Connection conn) throws Exception {
		int count=0;
		try {
			count = SqlClient.executeUpd("updateCusHandoverAppStatus", serno, status, null, conn);
		} catch (Exception e) {
			throw new DaoException("根据流水号更新申请状态出错，错误原因："+e.getMessage());
		}
		return count;
	}
	/**
	 * 根据客户号查找该接受人是否存在当前客户经理申请中
	 * 
	 * @param cusId
	 *            客户码
	 * @return String
	 * @throws Exception 
	 * @throws EMPException
	 */
	public CusHandoverApp findCusHandoverApp(String cusId,String handoverId,Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		CusHandoverApp cha = new CusHandoverApp();

		try {
			stmt = conn.createStatement();
			String sql = "select receiver_id from cus_handover_app where " +
					"receiver_id ='"+ cusId + "' and handover_id ='"+handoverId+"' and status not in('997','998')";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cha.setReceiverId(rs.getString("receiver_id"));
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
		return cha;
	}

	public boolean hasCusTrusteeList(String serno, Connection conn) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int ct =0;
		try {
			stmt = conn.createStatement();
			sql = "select count(*) ct from  cus_handover_lst where serno='" + serno + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ct =rs.getInt("ct");
			}
			if(ct >0){
				return true;
			}
		} catch (SQLException e) {
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
		return false;
	}
	
	/**
	 * 客户资料移交时
	 * @param handoverId
	 * @param receiverId
	 * @param receiverBrId
	 * @param businessCode
	 * 批处理sql语句
	 */
	
//	public String updateCom(String handoverId,String receiverId,String receiverBrId,String businessCode) throws ComponentException{
//		
//		Statement stmt = null;
//		
//		Statement stmt1 = null;
//		
//		//对公对私客户信息
//		String sql1 = " update cus_base set cust_mgr='"+receiverId+"', main_br_id='"+receiverBrId+"'," +
//				"input_id='"+receiverId+"',input_br_id='"+receiverBrId+"'" +
//				"where cus_id='"+businessCode+"' and cust_mgr='"+handoverId+"'";
//		
//		//删除共享客户经理是当前移交接受人的记录--处理cus_loan_rel
//		String sql3 =" delete from cus_loan_rel where cus_id='"+businessCode+"' and tru_cus_mgr<>main_cus_mgr " +
//				" and main_cus_mgr='"+handoverId+"' and tru_cus_mgr='"+receiverId+"'";
//		String sql4 = "update cus_loan_rel set br_id='"+receiverBrId+"',main_cus_mgr='"+receiverId+"',tru_cus_mgr='"+receiverId+"' " +
//				" where cus_id='"+businessCode+"' and tru_cus_mgr=main_cus_mgr and main_cus_mgr='"+handoverId+"'";
//		String sql5 = "update cus_loan_rel set br_id='"+receiverBrId+"',main_cus_mgr='"+receiverId+"' " +
//				" where cus_id='"+businessCode+"' and tru_cus_mgr<>main_cus_mgr and main_cus_mgr='"+handoverId+"' and tru_cus_mgr<>'"+receiverBrId+"'";
//		
//		
//		//对于集团的处理cus_loan_rel...
//		String sql6 = " update cus_grp_info set manager_br_id='"+receiverBrId+"',manager_id='"+receiverId+"' " +
//						"where parent_cus_id='"+businessCode+"'and manager_id='"+handoverId+"' ";
//		String sql7 = " update cus_grp_member set manager_br_id='"+receiverBrId+"',manager_id='"+receiverId+"' " +
//						"where cus_id='"+businessCode+"'and manager_id='"+handoverId+"' ";
//		
//		//保证群
//		String sql8 = "update Lmt_Grt_Guar_Cont set manager_id='"+receiverId+"',manager_br_id='"+receiverBrId+"' " +
//				" where borrower_no='"+businessCode+"' and manager_id='"+handoverId+"'";
//		//授信
//		String sql9 = " update lmt_cont set manager_id='"+receiverId+"', manager_br_id='"+receiverBrId+"'" +
//		"where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		
//		// 担保品
//		String sql10 = " update grt_g_basic_info set manager_id='"+receiverId+"', manager_br_id='"+receiverBrId+"' " +
//				"where guaranty_id in(select guaranty_id from grt_guaranty_re  " +
//				"where guar_cont_no in(select guar_cont_no from grt_guar_cont where manager_id='"+handoverId+"' and borrower_no='"+businessCode+"' ))";
//		
//		String sql11 = " update grt_p_basic_info set manager_id='"+receiverId+"', manager_br_id='"+receiverBrId+"' " +
//				"where guaranty_id in(select guaranty_id from grt_guaranty_re  " +
//				"where guar_cont_no in(select guar_cont_no from grt_guar_cont where manager_id='"+handoverId+"' and borrower_no='"+businessCode+"' ))";
//		
//		String sql12 = " update grt_guarantee set create_user_no='"+receiverId+"'" +
//				"where guaranty_id in(select guaranty_id from grt_guaranty_re  " +
//				"where guar_cont_no in(select guar_cont_no from grt_guar_cont where manager_id='"+handoverId+"' and borrower_no='"+businessCode+"' ))";
//		
//		//第三方合作协议
//		String sql13 = " update Prj_Cooperate_Cont set manager_id='"+receiverId+"', manager_br_id='"+receiverBrId+"' " +
//		"where cop_cus_no='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		try {
//			Connection conn = this.getConnection();
//			stmt1= conn.createStatement();
//			stmt1.execute(sql3);
//
//			stmt = conn.createStatement();
//			stmt.addBatch(sql1);
//			stmt.addBatch(sql4);
//			
//			stmt.addBatch(sql5);
//			stmt.addBatch(sql6);
//			stmt.addBatch(sql7);
//			stmt.addBatch(sql8);
//			stmt.addBatch(sql9);
//			stmt.addBatch(sql10);
//			stmt.addBatch(sql11);
//			stmt.addBatch(sql12);
//			stmt.addBatch(sql13);
//			
//			stmt.executeBatch();
//			
//		} catch (SQLException e) {
//			logger.error(e.getMessage(),e);
//			throw new ComponentException(e);
//		}finally{
//			
//			if(stmt !=null){
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					logger.error(e.getMessage(),e);
//				}
//				stmt = null;
//			}
//			if(stmt1 !=null){
//				try {
//					stmt1.close();
//				} catch (SQLException e) {
//					logger.error(e.getMessage(),e);
//				}
//				stmt1 = null;
//			}
//		}
//		
//		return CMISMessage.SUCCESS;
//	}
//	/**
//	 * 客户资料移交时 修改业务表 
//	 * @param handoverId
//	 * @param receiverId
//	 * @param receiverBrId
//	 * @param businessCode
//	 */
//	
//	public String updateComAndBusiness(String handoverId,String receiverId,String receiverBrId,String businessCode) throws ComponentException{
//		
//		Statement stmt = null;
//		//银票协议和台帐
//		String sql1 = " update ctr_accp_cont set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql2 = " update acc_accp set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		//保函协议和台帐
//		String sql3 = " update ctr_cvrg_cont set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql4 = " update acc_cvrg set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		//贴现协议和台帐
//		String sql5 = " update ctr_disc_cont set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql6 = " update acc_disc set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		//贷款协议和台帐
//		String sql7 = " update ctr_loan_cont set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql8 = " update acc_loan set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		//委托贷款协议和台帐
//		String sql9 = " update ctr_authloan_cont set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql10 = " update acc_authloan set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		//信用证协议和台帐
//		String sql11 = " update ctr_tf_loc set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql12 = " update acc_tf_loc set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		//提货担保协议和台帐
//		String sql13 = " update ctr_tf_pgas set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		String sql14 = " update acc_tf_pgas set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		//展期协议
//		String sql15 = " update ctr_loan_extend set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//		
//		//信用证修改协议
//		String sql16 = " update ctr_tf_loc_ext set manager_id='"+receiverId+"' where cus_id='"+businessCode+"' and manager_id='"+handoverId+"'";
//	
//		//担保合同
//		String sql17 = "update grt_guar_cont set manager_id='"+receiverId+"',manager_br_id='"+receiverBrId+"' " +
//		" where borrower_no='"+businessCode+"' and manager_id='"+handoverId+"'";
//
//		try {
//			stmt = this.getConnection().createStatement();
//			stmt.addBatch(sql1);
//			stmt.addBatch(sql2);
//			stmt.addBatch(sql3);
//			stmt.addBatch(sql4);
//			stmt.addBatch(sql5);
//			stmt.addBatch(sql6);
//			stmt.addBatch(sql7);
//			stmt.addBatch(sql8);
//			stmt.addBatch(sql9);
//			stmt.addBatch(sql10);
//			stmt.addBatch(sql11);
//			stmt.addBatch(sql12);
//			stmt.addBatch(sql13);
//			stmt.addBatch(sql14);
//			stmt.addBatch(sql15);
//			stmt.addBatch(sql16);
//			stmt.addBatch(sql17);
//	
//			stmt.executeBatch();
//			
//		} catch (SQLException e) {
//			logger.error(e.getMessage(),e);
//			throw new ComponentException(e);
//		}finally{
//			if(stmt !=null){
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					logger.error(e.getMessage(),e);
//				}
//				stmt = null;
//			}
//		}
//		return CMISMessage.SUCCESS;
//	}
}

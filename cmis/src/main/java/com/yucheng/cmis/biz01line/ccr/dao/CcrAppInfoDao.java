package com.yucheng.cmis.biz01line.ccr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CcrAppInfoDao extends CMISDao {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	
	public ArrayList queryCcrAppDetailsBySerno(String serno,Connection conn)throws ComponentException{
		PreparedStatement ps = null;
		ResultSet rs =null;
		CcrAppDetail ccrAppDetail;
		ArrayList ccrAppDetailList = new ArrayList();
		String sql = "select serno, model_no, " +
				" o_rating_unit, o_rating_result, auto_score, " +
				"auto_grade, adjusted_grade, reason" +
				" from ccr_app_detail " +
				"where serno = ?";
		
    	try {
			ps = conn.prepareStatement(sql);
	    	ps.setString(1, serno);
			logger.info("SQL: "+sql);
			rs = ps.executeQuery();
			
			while(!rs.isAfterLast()&&rs.next()){
				ccrAppDetail = new CcrAppDetail();
				ccrAppDetail.setSerno(rs.getString(1));
				ccrAppDetail.setModelNo(rs.getString(2));
				ccrAppDetail.setORatingUnit(rs.getString(3));
				ccrAppDetail.setORatingResult(rs.getString(4));
				ccrAppDetail.setAutoScore(rs.getBigDecimal(5));
				ccrAppDetail.setAutoGrade(rs.getString(6));
				ccrAppDetail.setAdjustedGrade(rs.getString(7));
				ccrAppDetail.setReason(rs.getString(8));
				ccrAppDetailList.add(ccrAppDetail);
			}
			return ccrAppDetailList;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					//doNothing;
					rs=null;
				}
				ps = null;
			}
			if(rs!=null){
				try{
					rs.close();
				}catch (SQLException e) {
					//doNothing;
					rs=null;
				}
			}
		}
	}
	
	/**
	 * 以业务编号作为条件删除指定表得分(ccr_app_detail也可以删除)	
	 * @param tableName 表明
	 * @param serno 业务编号
	 * @param conn 连接
	 * @return
	 * @throws ComponentException
	 */
	public String deleteScoreBySerno(String tableName,String serno,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql = "delete from ";
		sql=sql+tableName+" where serno =?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql);
			int result = ps.executeUpdate();
			logger.info(  result+" rows deleted.");
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	/**
	 * 以业务编号和客户码作为条件删除指定表得分	(ccr_app_detail也可以删除)
	 * @param tableName 表明
	 * @param serno 业务编号
	 * @param cusId 客户码
	 * @param conn 连接
	 * @return
	 * @throws ComponentException
	 */
	public String deleteScoreBySerno(String tableName,String serno,String cusId,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql = "delete from ";
		sql=sql+tableName+" where serno =? and cus_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, serno);
			ps.setString(2, cusId);
			logger.info("SQL: "+sql);
			int result = ps.executeUpdate();
			logger.info( result+" rows deleted.");
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	/**
	 * 修改申请状态(单笔)
	 * @param serno
	 * @param status
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String changeAppStatue(String serno,String status,String appDate,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql="update ccr_app_info set APPROVE_STATUS = ?,APP_END_DATE=?  where serno = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, appDate);
			ps.setString(3, serno);
			logger.info( "SQL: "+sql);
			int result = ps.executeUpdate();
			logger.info(  result+" rows updated.");
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	/**
	 * 修改申请状态(批量)
	 * @param serno
	 * @param status
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String changeBatchAppStatue(String serno,String status,String CrdDt,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql="update ccr_batch_app_info set APPROVE_STATUS = ?, APP_END_DATE=? where serno = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setString(2, CrdDt);
			ps.setString(3, serno);
			logger.info( "SQL: "+sql);
			int result = ps.executeUpdate();
			logger.info( result+" rows updated.");
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	/**
	 * <h1>将申请数据，过渡到历史表(单笔)</h1>
	 * <p>过渡
	 * ccr_app_info
	 * ccr_app_detail
	 * ccr_g_ind_score
	 * ccr_m_group_score
	 * ccr_model_score
	 * 表中的数据转入历史表
	 * </p>
	 * @param serno
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String app2His(String serno,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql="insert into ccr_app_info_his select * from ccr_app_info where serno = ?";
		String sql2="insert into ccr_app_detail_his select * from ccr_app_detail where serno = ?";
		String sql3="insert into ccr_grpind_scr_his select * from ccr_g_ind_score where serno = ?";
		String sql4="insert into ccr_modgrp_scr_his select * from ccr_m_group_score where serno = ?";
		String sql5="insert into ccr_mod_scr_his select * from ccr_model_score where serno = ?";

		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql);
			int result = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info(result+" rows inserted.");
			ps = conn.prepareStatement(sql2);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql2);
			int result2 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql3);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql3);
			int result3 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql4);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql4);
			int result4 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql5);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql5);
			int result5 = ps.executeUpdate();
			logger.info( result+" rows inserted.");
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	
	/**
	 * <h1>将申请数据，过渡到历史表(批量)</h1>
	 * <p>过渡
	 * ccr_app_info
	 * ccr_app_detail
	 * ccr_g_ind_score
	 * ccr_m_group_score
	 * ccr_model_score
	 * 表中的数据转入历史表
	 * </p>
	 * @param serno
	 * @param conn
	 * @return
	 * @throws ComponentException
	 */
	public String app2HisBatch(String serno,Connection conn) throws ComponentException{
		PreparedStatement ps = null;
		String sql="insert into ccr_batapp_his select * from ccr_batch_app_info where serno = ?";
		String sql2="insert into ccr_app_detail_his select * from ccr_app_detail where serno = ?";
		String sql3="insert into ccr_grpind_scr_his select * from ccr_g_ind_score where serno = ?";
		String sql4="insert into ccr_modgrp_scr_his select * from ccr_m_group_score where serno = ?";
		String sql5="insert into ccr_mod_scr_his select * from ccr_model_score where serno = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql);
			int result = ps.executeUpdate();
			logger.info( result+" rows inserted.");
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			ps = conn.prepareStatement(sql2);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql2);
			int result2 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql3);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql3);
			int result3 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql4);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql4);
			int result4 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			ps = conn.prepareStatement(sql5);
			ps.setString(1, serno);
			logger.info( "SQL: "+sql5);
			int result5 = ps.executeUpdate();
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
			logger.info( result+" rows inserted.");
			
			
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(),e);
				}
				ps = null;
			}
		}
		return CMISMessage.SUCCESS;
	}
	
	/**
	 * 根据客户编号 查询 该客户是否是新增企业，1为新增客户，2为存量客户
	 * @param cusId	客户编号
	 * @param conn	数据库连接
	 * @return
	 * @throws ComponentException
	 */
	public String getNewComFlag(String cusId,Connection conn) throws ComponentException{
		
		PreparedStatement ps1 = null;
		String sql1="select cus_id from ACC_LOAN where cus_id=?";
		String retValue = "1";
		
		ResultSet rs1 = null;
		try {
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, cusId);
			logger.info( "SQL: "+sql1);
			rs1 = ps1.executeQuery();
			
			if(rs1.next()){
				String tmpCusId1 = rs1.getString("cus_id");
				if(tmpCusId1!=null||!("".equals(tmpCusId1.trim()))){
					retValue="2";
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new ComponentException(e);
		}finally{
			try {
				if(rs1!=null)rs1.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new ComponentException(e);
			}
			try {
				if(ps1!=null)ps1.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new ComponentException(e);
			}
		}
		return retValue;
	}
	
}

package com.yucheng.cmis.biz01line.fnc.master.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.exception.DaoException;

public class FncStatBaseDao extends CMISDao {


	/**
	 * 得到一个客户的财报对象
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 */
	public FncStatBase queryDetailFncStatBase(String cusId,String statPrdStyle,String statPrd,Connection conn){
		
		FncStatBase fncStatBase = null;
		PreparedStatement ps = null;
		try {
			String sql = "select * from Fnc_Stat_Base where cus_id = ? and stat_prd_style = ? and stat_prd = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, statPrdStyle);
			ps.setString(3, statPrd);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				fncStatBase = new FncStatBase();
				fncStatBase.setCusId(cusId);
				fncStatBase.setStatPrdStyle(statPrdStyle);
				fncStatBase.setStatPrd(statPrd);
				
				fncStatBase.setStatBsStyleId(rs.getString("STAT_BS_STYLE_ID"));//资产样式编号
				fncStatBase.setStatPlStyleId(rs.getString("STAT_PL_STYLE_ID"));//损益表编号
				fncStatBase.setStatCfStyleId(rs.getString("STAT_CF_STYLE_ID"));//现金流量表编号
				fncStatBase.setStatFiStyleId(rs.getString("STAT_FI_STYLE_ID"));//财务指标表编号
				fncStatBase.setStateFlg(rs.getString("STATE_FLG"));//报表状态
				fncStatBase.setInputId(rs.getString("input_id"));
				fncStatBase.setInputDate(rs.getString("input_date"));
				fncStatBase.setInputBrId(rs.getString("input_br_id"));
				fncStatBase.setStatPrd(statPrd);
			}
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(ps !=null){
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
		}
		return fncStatBase;
	}
	public boolean updateStateFlg(String cus_id,String stat_prd_style,String stat_prd,String stat_style,String status,Connection conn)throws DaoException{
		
		String update = "update fnc_stat_base set state_flg='"+status+"'  where cus_id='"+cus_id+"' and stat_prd_style='"+stat_prd_style
		                 +"' and stat_prd='"+stat_prd+"' and stat_style='"+stat_style+"'";
		Statement stmt = null;
		int num = 0;
		try{
			stmt = conn.createStatement();
			num = stmt.executeUpdate(update);
		}catch (Exception e) {
			throw new DaoException(e.toString());
		}finally{
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new DaoException(e.toString());
				}
		}
		if(num == 1)
			return true;
		else
			return false;
	}
}

package com.yucheng.cmis.biz01line.fnc.master.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.CMISDao;

  /**
 *@Classname	ExportXLDao.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-22 上午10:57:22  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class ExportXLDao extends CMISDao{
	
	/**
	 * 计算该报表的第几栏有几行  由数据库定义
	 * @param styleId 报表样式id
	 * @param fncConfCotes 该报表的第几栏
	 * @param conn
	 * @return
	 */
	public int queryCount(String styleId,int fncConfCotes,Connection conn){
		int temp = 0;
		PreparedStatement ps = null;
		StringBuffer sb = new StringBuffer();				//用于存放拼成的sql

		try {
			sb.append("SELECT count(*) from FNC_CONF_DEF_FMT where style_id=? and  fnc_conf_cotes= ? ");
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, styleId);
			ps.setInt(2, fncConfCotes);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temp  = rs.getInt( 1 );	
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
		return temp;
	}
}

package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.PUBConstant;

public class SetSysInfo { 
	public static void init(Context context,Connection conn){
		//HashMap<String,String> hm=SetSysInfo.querySysInfo(conn);
		
		try {
			context.setDataValue(PUBConstant.OPENDAY, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		} catch (Exception e) { 
		}
		
		try{
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"当前系统营业日期>>>>"+context.getDataValue(PUBConstant.OPENDAY));
		}catch(Exception ex){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"无法取到当前系统营业日期");
		}
	}
	
	private static HashMap<String, String> querySysInfo(Connection conn){
		HashMap<String, String> hm=new HashMap<String,String>();
		Statement st=null;
		ResultSet rs=null;
		String sql="select * from PUB_SYS_INFO where 1=1";
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs!=null&&rs.next()){
				hm.put(PUBConstant.OPENDAY, rs.getString("OPENDAY"));
				hm.put(PUBConstant.LAST_OPENDAY, rs.getString("LAST_OPENDAY"));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){ 
					rs.close(); 
				}
				if(st!=null){
					st.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return hm;
	}
}

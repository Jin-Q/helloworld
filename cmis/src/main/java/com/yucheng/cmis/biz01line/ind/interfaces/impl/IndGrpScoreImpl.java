package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ind.interfaces.IndGroupScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 取分类指标组得分
 * @author Administrator
 *
 */
public class IndGrpScoreImpl implements IndGroupScoreIface {
	/**
	 * 取分类指标组得分
	 * @param grpno 组别编号
	 * @param hm 组下指标得分集
	 * @param conn 连接
	 */
	public String getGrpScore(String grpno, HashMap hm, Connection conn)
			throws ComponentException { 
		Statement st=null;
		ResultSet rs=null; 
		String retVal="";
		try{
			st=conn.createStatement();
			String sql="select index_no,weight from ind_group_index where group_no='"+grpno+"'";
			rs=st.executeQuery(sql);
			
			while(rs!=null&&rs.next()){
				String indno=rs.getString("index_no");
				float fweight=rs.getFloat("weight");
				if(fweight<=0.0){
					hm.remove(indno);
				}
			}
			Iterator KeyIter = hm.keySet().iterator(); 
		    int maxValue = -9999; 
		    while(KeyIter.hasNext()){
				 String key=KeyIter.next().toString();
				 String value=(String)hm.get(key);
				 if(value==null||value.trim().equals("")){
					 continue;
				 }
		         int t = Integer.parseInt(value);
		        	 maxValue = maxValue > t ? maxValue:t;
			}
		    retVal = (maxValue == -9999 ? "" :String.valueOf(maxValue)); 
		}catch(Exception e){
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取组得分出错:>>>>="+e.toString());
		}finally{
			if(st!=null){
				  try {
					st.close();
				} catch (SQLException e) {
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取组得分关闭Statement出错:>>>>="+e.toString());
				}
			  }
		}
		return retVal;
	}
	 
}

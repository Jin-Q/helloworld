package com.yucheng.cmis.biz01line.ccr.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.yucheng.cmis.biz01line.ind.interfaces.IndGroupScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 取信用评级指标组得分
 * @author Administrator
 *
 */
public class IndGrpScoreImpl implements IndGroupScoreIface {
	private static final Logger logger = Logger.getLogger(IndGrpScoreImpl.class);
	/**
	 * 取信用评级指标组得分
	 * @param grpno 组别编号
	 * @param hm 组下指标得分集
	 * @param conn 连接
	 */
	public String getGrpScore(String grpno, HashMap hm, Connection conn)
			throws ComponentException { 
		Statement st=null;
		ResultSet rs=null; 
		String retVal="";
		BigDecimal dGrpScore=new BigDecimal(0); 
		logger.info("getGrpScore>>>>"+hm);
		try{
			st=conn.createStatement();
			String sql="select index_no,weight from ind_group_index where group_no='"+grpno+"'";
			rs=st.executeQuery(sql);
			
			while(rs!=null&&rs.next()){
				String indno=rs.getString("index_no");
				BigDecimal fweight=new BigDecimal(rs.getFloat("weight"));
				BigDecimal dIndScore;
				String sIndScore=(String)hm.get(indno);  
				if(sIndScore!=null&&!sIndScore.trim().equals("")){
					try{
						dIndScore=new BigDecimal(sIndScore);
					}catch(Exception e){
						logger.error("取指标:"+indno+"的得分不是数字:"+sIndScore);
						dIndScore=new BigDecimal("0");
					}
					dGrpScore=dGrpScore.add(dIndScore.multiply(fweight));
				}
			}
			retVal=dGrpScore.toPlainString();  
		}catch(Exception e){
			logger.error("取组得分出错:>>>>="+e.toString()); 
		}finally{
			if(st!=null){
				  try {
					st.close();
				} catch (SQLException e) {
					logger.error("取组得分关闭Statement出错:>>>>="+e.toString()); 
				}
			  }
		}
		logger.info("grpno="+grpno+",score="+retVal);
		return retVal;
	}
	 
}

package com.yucheng.cmis.biz01line.ccr.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
 
import com.yucheng.cmis.biz01line.ccr.domain.CcrBizModel; 

public class CcrBizModelDao {
	Logger logger=Logger.getLogger("CcrBizModelDao");
	public CcrBizModel queryCcrBizModel(String comCllTyp,String comOptScale,String indexNo,Connection connection){
		CcrBizModel ccrBizModel = new CcrBizModel();
		PreparedStatement ps = null;
		ResultSet rs =null;
		String sql = "select INDEX_NAME,EXCELLENT_SCORE,GOOD_SCORE,AVERAGE_SCORE,LOWER_SCORE,WORSE_SCORE,WORST_SCORE FROM CCR_BIZ_MODEL WHERE COM_CLL_TYP= ? AND COM_OPT_SCALE= ? AND INDEX_NO = ?";
		
		try {
			ps = connection.prepareStatement(sql);
	    	ps.setString(1, comCllTyp);
	    	ps.setString(2, comOptScale);
	    	ps.setString(3, indexNo);
	    	logger.info("行业标准值参数:");
	    	logger.info("comCllTyp="+comCllTyp);
	    	logger.info("comOptScale="+comOptScale);
	    	logger.info("indexNo="+indexNo);
			rs = ps.executeQuery();
			
			while(!rs.isAfterLast()&&rs.next()){
				ccrBizModel.setIndexName(rs.getString(1));
				ccrBizModel.setExcellentScore(rs.getDouble(2));
				ccrBizModel.setGoodScore(rs.getDouble(3));
				ccrBizModel.setAverageScore(rs.getDouble(4));
				ccrBizModel.setLowerScore(rs.getDouble(5));
				ccrBizModel.setWorseScore(rs.getDouble(6));
				ccrBizModel.setWorstScore(rs.getDouble(7));
			}
		} catch (SQLException e) { 
			logger.error("取行业标准值出错:"+e.getMessage(),e); 
		}finally{
			if(ps !=null){
				try {
					ps.close();
				} catch (SQLException e) {
					//doNothing
					rs=null;
				}
				ps = null;
			}
			if(rs!=null){
				try{
					rs.close();
				}catch (SQLException e) {
					//doNothing
					rs=null;
				}
			}
		}
		return ccrBizModel;
	}
}

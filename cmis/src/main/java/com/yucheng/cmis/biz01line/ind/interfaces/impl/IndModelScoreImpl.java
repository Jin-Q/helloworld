package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.interfaces.IndModelScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 分类模型得分实现类
 * @author Administrator
 *
 */
public class IndModelScoreImpl implements IndModelScoreIface {

	/**
	 *  评分中较好的取值
	 */
	public static final int Better = IndPubConstant.BETTER;
	
	/**
	 *  评分中较差的取值
	 */
	public static final int Inferior = IndPubConstant.INFERIOR;
	
	/**
	 *  评分中跳动一级变化的分值
	 */
	public static final int Skip = IndPubConstant.SKIP;
	
	/**
	 *  风险分类中正常的取值
	 */
	public static final int Rsc_Normal = IndPubConstant.RSC_NORMAL;
	
	/**
	 *  风险分类中损失的取值
	 */
	public static final int Rsc_Lose = IndPubConstant.RSC_LOSE;
	/**
	 * 取分类模型得分
	 * @param modelno 模型编号
	 * @param hm 模型下组得分
	 * @param conn 连接
	 * 
	 */
	public String getModelScore(String modelno, HashMap hm,ArrayList list, Connection conn)
			throws ComponentException { 
		Statement st=null;
		ResultSet rs=null; 
		String retVal="";
		try{
			st=conn.createStatement();
			String sql="select group_no,weight from ind_model_group where model_no='"+modelno+"'";
			rs=st.executeQuery(sql);
			
			while(rs!=null&&rs.next()){
				String indno=rs.getString("group_no");
				float fweight=rs.getFloat("weight");
				if(fweight<=0.0){
					hm.remove(indno);
				}
			}
			Iterator KeyIter = hm.keySet().iterator(); 
		    
			int maxValue = -9999; 
		    int maxValue_2 = -9999;
			while(KeyIter.hasNext()){
				 String key=KeyIter.next().toString();
				 String value=(String)hm.get(key);
		         int t = Integer.parseInt(value);
		         maxValue = maxValue > t ? maxValue:t;
			}
			
		   /*获取返回值*/
		   retVal = (maxValue == -9999 ? "" :String.valueOf(maxValue)); 
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取模型得分出错:>>>>="+e.toString());
		}finally{
			if(st!=null){
				  try {
					st.close();
				} catch (SQLException e) {
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取模型得分关闭Statement出错:>>>>="+e.toString());
				}
			  }
		}
		return retVal;
	}

}

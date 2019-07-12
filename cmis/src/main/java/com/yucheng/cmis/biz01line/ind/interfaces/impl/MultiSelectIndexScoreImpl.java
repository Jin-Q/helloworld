package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.interfaces.IndIndexScoreIface;
import com.yucheng.cmis.pub.exception.ComponentException;


/**
 * 对于多选情况的指标得分实现类
 * @author Administrator
 *
 */
public class MultiSelectIndexScoreImpl implements IndIndexScoreIface {
	
	 private static final Logger logger = Logger.getLogger(MultiSelectIndexScoreImpl.class);
	
	public String getIndScore(String grpno, String indexno, String indval,HashMap para,Connection conn)
			throws ComponentException { 
	  Statement st=null;
	  ResultSet rs=null;
	  String retVal="";
	  try{
		if(indval!=null&&!indval.trim().equals("")){
			 String [] vals=indval.split(IndPubConstant.IND_CHECKBOX_DELIMITER);
			 String conditionStr="";
			 if(vals!=null&&vals.length>0){
				 for(int i=0;i<vals.length;i++){
					if(i==0){
						conditionStr+="'"+vals[i];
					}
					if(i==(vals.length-1)){
						conditionStr+="'";
					}else{
						conditionStr+="','"+vals[i];
					}
				 }
				 if(conditionStr!=null&&!conditionStr.trim().equals("")){
					 st=conn.createStatement();
					 String sql="select max(value_score) as score from ind_opt where index_no='"
						 +indexno+"' and index_value in("+conditionStr+")";
					 EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取指标得分sql>>>>="+sql);
					 rs=st.executeQuery(sql);
					 if(rs!=null&&rs.next()){
						 retVal=rs.getString("score");
					 }
				 }
			 }
		}
	  }catch(Exception e){
		  e.printStackTrace();
		  EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取指标得分出错:>>>>="+e.toString());
		  throw new ComponentException(e);
	  }finally{
		  if(st!=null){
			  try {
				st.close();
			} catch (SQLException e) {
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取指标得分关闭Statement出错:>>>>="+e.toString());
			}
		  }
		  if(rs!=null){
			  try {
				rs.close();
			} catch (SQLException e) {  
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"取指标得分关闭ResultSet出错:>>>>="+e.toString());
			}
		  }
	  }
	return retVal;
	}
	public static void main(String args[]){
		MultiSelectIndexScoreImpl m=new MultiSelectIndexScoreImpl();
		 
	}
}

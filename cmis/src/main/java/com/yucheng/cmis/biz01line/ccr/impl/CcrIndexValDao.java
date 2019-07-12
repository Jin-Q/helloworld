package com.yucheng.cmis.biz01line.ccr.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 指标取数dao
 * @author Administrator
 *
 */
public class CcrIndexValDao {
	private static final Logger logger = Logger.getLogger(CcrIndexValDao.class);
	/**
	 * 取个人评级指标值(部分农户)
	 * @param cusid
	 * @param field
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	 public String getCusIndivValue(String cusid,String indexno,Connection conn) throws Exception {
		 String retVal="";
		 Statement st=null;
		 ResultSet rs = null;
		 try{
			 st=conn.createStatement(); 
			 String sql="select INDEX_VALUE from IND_RESULT_VAL where SERNO='"+cusid+"' and index_no='"+indexno+"'";
			 logger.info(">>>>>"+sql);
			 rs=st.executeQuery(sql);
			 if(rs!=null&&rs.next()){
				 retVal=rs.getString("INDEX_VALUE");
			 }
		 }catch(SQLException se){
			 logger.error("方法  getCusIndivValue 执行失败 ...."+se.getMessage(),se);
		 }finally{
			this.closeResource(st, rs); 
		 }
		  
		 return retVal;
	 }
	 /**
	  * 取出所有无穷大的指标编号
	  * @param cusid
	  * @param conn
	  * @return
	  * @throws Exception
	  */
	 public List<Map<String,String>> getInfiniteIndexList(String modelno,String cusid,Connection conn)throws Exception{
		 List<Map<String,String>> list=null;
		 Statement st=null;
		 ResultSet rs = null;
		 try{
			 st=conn.createStatement(); 
			 String sql="select index_no,index_name from ind_result_val where serno='"+cusid
			 +"' and index_value='9999999999.99'";
			 logger.info("取无穷大指标sql>>>"+sql);
			 rs=st.executeQuery(sql);
			 if(rs!=null){
				 list=new ArrayList<Map<String,String>>();
				 while(rs.next()){
					 Map<String,String> map=new HashMap<String,String>();
					 map.put("index_no", rs.getString("index_no"));
					 map.put("index_name", rs.getString("index_name"));
					 list.add(map);
				 }
			 }
		 }catch(Exception ex){
			 logger.error(ex.getMessage(),ex);
		 }
		 return list;
	 }
	 /**
	  * 取模型下指标的标准分
	  * @param modelno
	  * @param conn
	  * @return
	  * @throws Exception
	  */
	 public Map<String,BigDecimal> getIndexStdScore(String modelno,Connection conn)throws Exception {
		 Statement st=null;
		 ResultSet rs = null;
		 Map<String,BigDecimal> map=null;
		 try{ 
			 st=conn.createStatement(); 
			 String sql="select c.fnc_index_rpt,a.ind_std_score "+
			 			" from ind_group_index a,ind_model_group b,ind_lib c where b.model_no='"+modelno+
			 			"' and b.group_no=a.group_no and a.index_no=c.index_no"; 
			 logger.info("取无穷大指标sql>>>"+sql);
			 rs=st.executeQuery(sql);
			 if(rs!=null){ 
				 map=new HashMap<String,BigDecimal>();
				 while(rs.next()){ 
					 BigDecimal stdscore=rs.getBigDecimal("ind_std_score");
					 map.put(rs.getString("fnc_index_rpt"), stdscore);
				 }
			 }
		 }catch(Exception ex){
			 logger.error(ex.getMessage(),ex);
		 }
		 return map;
	 }

	 private void closeResource(Statement st,ResultSet rs) {
		 try{
			 if(rs!=null){
				 rs.close();
			 }
			 if(st!=null){
				 st.close();
			 }
		 }catch(SQLException se){
			 
		 }
	 }
}

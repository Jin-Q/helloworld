package com.yucheng.cmis.biz01line.ind.pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CcrPubConstant;

public class IndexInit {
	 private static final Logger logger = Logger.getLogger(IndexInit.class);
	
	/**
	 * 每个指标如同一个字典,该方法读取指标选项下的数据，将它组成字典项放在rootCtx下
	 * @param rootCtx
	 * @param conn
	 */
	public static void init(Context rootCtx,Connection conn){ 
		int i=0;
		IndexedCollection iColl=null;
		KeyedCollection kcoll=null;
		try {
			ArrayList<HashMap> list=IndexInit.queryIndexes(conn); 
			if(list!=null&&list.size()>0){
				Iterator<HashMap> it=list.iterator();
			   while (it.hasNext()) {
				    i=0;
					HashMap<String, String> hm = it.next();
					String index = hm.get("index_no"); 
					ArrayList<HashMap> opts=IndexInit.queryOpts(conn, index);
					iColl=new IndexedCollection(index);
				    if (opts != null && opts.size() > 0) {
				    	Iterator<HashMap> itopt=opts.iterator();
						while (itopt.hasNext()) {
							HashMap opt=itopt.next();
							kcoll = new KeyedCollection();
							try {
								kcoll.addDataField("enname", opt.get("index_value"));
								kcoll.addDataField("cnname", opt.get("ind_desc"));
								if (i == 0)
									iColl .setDataElement((KeyedCollection) kcoll.clone());
								iColl.addDataElement(kcoll);
							} catch (Exception ex) {
								logger.error(ex.getMessage(), ex);
							}
							i++;
						}
					}
					try {
						rootCtx.addDataElement(iColl);
						//System.err.println(">>>>>>>>>>>="+iColl.toString());
					} catch (InvalidArgumentException e) { 
						logger.error(e.getMessage(), e);
					} catch (DuplicatedDataNameException e) { 
						logger.error(e.getMessage(), e);
					} 
				}
			}
			IndexInit.initHyYs(conn);
		} catch (Exception e) { 
			logger.error(e.getMessage(), e);
		}
		
	}
	/**
	 * 初始化行业映射
	 * @param conn
	 */
	private static void initHyYs(Connection conn){
		
		logger.info("开始读取行业类型和评级模型配置表...........");
		
		Statement st=null;
		String sql="";
		ResultSet rs = null;
		try {
			st=conn.createStatement();
			sql="select CCR_FLD_TYPE,FLD_NAME,CUS_FLD_TYPE,CCR_FLD_CATEGORY from CCR_FLD_TYPE_MAP";
			rs = st.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					String CCR_FLD_CATEGORY = rs.getString("CCR_FLD_CATEGORY");
					String CUS_FLD_TYPE = rs.getString("CUS_FLD_TYPE");
					String CCR_FLD_TYPE = rs.getString("CCR_FLD_TYPE");
					String FLD_NAME = rs.getString("FLD_NAME");
					if("1".equals(CUS_FLD_TYPE)){
						CcrPubConstant.CCR_COM_SMALL_FLD_TYPE.put(CCR_FLD_CATEGORY, CCR_FLD_TYPE);
					}else if("2".equals(CUS_FLD_TYPE)){
						CcrPubConstant.CCR_COM_FLD_TYPE_JX.put(CCR_FLD_CATEGORY, CCR_FLD_TYPE);
					}else{
						logger.error("评级模块行业类型与客户模块行业类型对应关系建立出错,["+FLD_NAME+"]类型的行业类型大类设置出错");
					}
				}
			}
		} catch (SQLException e) { 
			logger.error(e.getMessage(),e);
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private static ArrayList<HashMap> queryOpts(Connection conn,String index){
		Statement stmt=null;
		String sql="select index_no,ind_desc,index_value from ind_opt where index_no='"+index+"'";
		ResultSet rs=null;
		ArrayList<HashMap> list=null;
		try {
			list= new ArrayList<HashMap>();
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					HashMap<String, String> hm=new HashMap<String,String>(); 
					hm.put("index_no", rs.getString("index_no"));
					hm.put("ind_desc", rs.getString("ind_desc"));
					hm.put("index_value", rs.getString("index_value"));
					list.add(hm);
				}
			}
		} catch (SQLException e) { 
			logger.error(e.getMessage(), e);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}finally{
		  try{
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
		  }catch(Exception ex){
			  logger.error(ex.getMessage(), ex);
		  }
		}
		
		return list;
	}
	private static ArrayList<HashMap> queryIndexes(Connection conn){
		Statement stmt=null;
		String sql="select distinct index_no as index_no from ind_opt order by index_no";
		ResultSet rs=null;
		ArrayList<HashMap> list=null;
		try {
			list= new ArrayList<HashMap>();
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			if(rs!=null){
				while(rs.next()){
					HashMap<String, String> hm=new HashMap<String,String>(); 
					hm.put("index_no", rs.getString("index_no"));
					list.add(hm);
				}
			}
		} catch (SQLException e) { 
			logger.error(e.getMessage(), e);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}finally{
		  try{
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
		  }catch(Exception ex){
			  logger.error(ex.getMessage(), ex);
		  }
		}
		
		return list;
	}
	private Connection getConn(){
		String user = "cmis";
		String password = "cmis";
		String url = "jdbc:oracle:thin:@192.100.2.129:1521:CMIS";
		String driver = "oracle.jdbc.driver.OracleDriver";
		Connection conn=null;
		try {
			 Class.forName(driver); 
			 conn = DriverManager.getConnection(url, user, password); 
			 
		} catch (ClassNotFoundException e) { 
			logger.error(e.getMessage(), e);
		}catch(SQLException ex){
			logger.error(ex.getMessage(), ex);
		}
		return conn;
	}
	public static void main(String args[]){
		Connection conn=null;
		IndexInit ii=new IndexInit();
		try {
		conn=ii.getConn();
		Context root=new Context("root");
		ii.init(root, conn); 
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		}
	}
}

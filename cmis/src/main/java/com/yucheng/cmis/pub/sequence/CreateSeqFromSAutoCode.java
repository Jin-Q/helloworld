package com.yucheng.cmis.pub.sequence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateSeqFromSAutoCode {
	public static void main(String[] args) throws Exception {
	    try {
	      BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\sequencescript.txt"));
	      List<String> l=CreateSeqFromSAutoCode.getData();    
	      String s = null;
	      for(int i=0;i<l.size();i++){
	        s = "CREATE SEQUENCE  "+ (String)l.get(i)+
            " INCREMENT BY 1"+    
            " START WITH 5000"+   
            " NOMAXVALUE"+        
            " NOCYCLE"+           
            " CACHE 10;";  
	        bw.write(s);
	        bw.newLine();
	      }
	      bw.flush();     
	      bw.close();     
	    } catch (IOException e) { e.printStackTrace();}
	    System.out.println("OK!");
	  }

	public static List<String> getData() {
		List<String> rv=new ArrayList<String>();
		ResultSet rs = null;
		Statement stmt = null;
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.16.240:1521:XD", "credit2", "credit2_pass");		
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.16.240:1521:XD", "cmis", "cmis");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from s_autocode");
			while(rs.next()) {
				rv.add(rs.getString("atype")+"_"+rs.getString("owner"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
					rs = null;
				}
				if(stmt != null) {
					stmt.close();
					stmt = null;
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rv;
	}
	
}

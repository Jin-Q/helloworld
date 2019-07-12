package com.yucheng.cmis.biz01line.fnc.master.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FncRptCheckDao {
	
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	
	public FncRptCheckDao(){
		super();
	}
	
	public FncRptCheckDao(Connection con){
		super();
		this.con = con;
	}
	
	public String query(String querySql,String columnname){
		
		String value = "";
		
		try{
			this.stmt = this.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			this.rs = this.stmt.executeQuery(querySql);
			
			while(this.rs.next()){
				value = this.rs.getString(columnname);
			}
			
			rs.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}		
		
		return value;
	}
	
	public void saveRecord(String querySql,String updateSql,String insertSql){
		
		try{
			this.stmt = this.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			this.rs = this.stmt.executeQuery(querySql);
			
			if(rs.next())
				this.stmt.executeUpdate(updateSql);
			else
				this.stmt.executeUpdate(insertSql);
			
			this.rs.close();
			this.stmt.close();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
}

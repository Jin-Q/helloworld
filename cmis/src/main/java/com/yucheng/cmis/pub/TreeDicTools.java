package com.yucheng.cmis.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 树型字典访问类
 * @author xuhui
 *
 */
public class TreeDicTools {
	private static final Logger logger = Logger.getLogger(TreeDicTools.class);
	/**
	 * 根据字节点enname查询出最顶级的父节点
	 * @param   enname     字节点的enname
	 * @param   conn       数据库链接
	 * @return  fatherNode 最顶级父节点的enname
	 * @throws  SQLException
	 */
	public String getFatherNode(String enname,Connection conn) throws SQLException {
		
		if(enname == null || enname.trim().equals("")){
			logger.info("传进来的行业类型编号为空:"+enname);
			return "";
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer();
		String fatherNode=null;
		String locate=null;
		String[] locateList=null;
		try {
			
			schemeSql.append("select locate from s_treedic where enname=?");
			stmt = conn.prepareStatement(schemeSql.toString());
			stmt.setString(1, enname.trim());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				locate=rs.getString("locate");
			}
			if(locate!=null&& !locate.equals("")){
				locateList=locate.split(",");
				fatherNode=locateList[1];
			}
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return fatherNode;
	}
	/**
	 * 根据节点enname查询出树型路径中的所有节点
	 * @param enname
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public String[] getLocateNodes(String enname,Connection conn) throws SQLException {
		if(enname == null || enname.trim().equals("")){
			logger.info("传进来的行业类型编号为空:"+enname);
			return null;
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer(); 
		String locate=null;
		String[] locateList=null;
		try {
			
			schemeSql.append("select locate from s_treedic where enname=? and opttype='STD_GB_4754-2011'");
			stmt = conn.prepareStatement(schemeSql.toString());
			stmt.setString(1, enname.trim());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				locate=rs.getString("locate");
			}
			System.out.println(locate);
			if(locate!=null&& !locate.equals("")){
				locateList=locate.split(","); 
			}
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return locateList;
	}
	
	/**
	 * 根据产品的id查询产品名称
	 * @param nodeid 产品ID
	 * @param conn
	 * @return 返回产品名称
	 * @throws SQLException
	 */
	public String getPrdPopCataLogName(String nodeid,Connection conn) throws SQLException {
		if(nodeid == null || nodeid.trim().equals("")){
			logger.info("传入的产品编号为空:"+nodeid);
			return null;
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer(); 
		String cataLogName="";
		try {
			schemeSql.append("SELECT PRDNAME FROM PRD_BASICINFO WHERE PRDID=?");
			stmt = conn.prepareStatement(schemeSql.toString());
			stmt.setString(1, nodeid.trim());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				cataLogName=rs.getString(1);
			}
			System.out.println(cataLogName);
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return cataLogName;
	}
	
	/**
	 * 根据传入的产品ID串查询出产品名称
	 * @param nodeids 产品ID串
	 * @param conn 
	 * @return 返回产品名称串
	 * @throws SQLException
	 */
	public String getPrdPopName(String nodeids,Connection conn) throws SQLException {
		if(nodeids == null || nodeids.trim().equals("")){
			logger.info("传入的产品编号串为空:"+nodeids);
			return null;
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer(); 
		String cataLogName="";
		try {
			schemeSql.append("SELECT PRDNAME FROM PRD_BASICINFO WHERE PRDID IN("+nodeids.trim()+")");
			stmt = conn.prepareStatement(schemeSql.toString());
			//stmt.setString(1, nodeids.trim());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				cataLogName+=rs.getString(1) +",";
			}
			System.out.println("选择的产品："+cataLogName);
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return cataLogName;
	}
	
	/**
	 * 多值翻译
	 * @param ennames 翻译值
	 * @param opttype 字典名称
	 * @param conn 
	 * @return 返回产品名称串
	 * @throws SQLException
	 */
	public String getDicsName(String ennames,String opttype,Connection conn) throws SQLException {
		if(ennames == null || ennames.trim().equals("")){
			logger.info("传入的翻译值为空:"+ennames);
			return null;
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer(); 
		String cataLogName="";
		try {
			schemeSql.append("SELECT CNNAME FROM S_DIC WHERE OPTTYPE = '"+opttype+"' AND ENNAME IN("+ennames.trim()+")");
			stmt = conn.prepareStatement(schemeSql.toString());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				cataLogName+=rs.getString(1) +",";
			}
			System.out.println("选择的产品："+cataLogName);
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return cataLogName;
	}
	
	private void closeResource(PreparedStatement ps,ResultSet rs){
		try{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	/**
	 * 根据字节点enname查询出最顶级的父节点
	 * @param   enname     字节点的enname
	 * @param   conn       数据库链接
	 * @return  fatherNode 最顶级父节点的enname
	 * @throws  SQLException
	 */
	public String getCnnamebyTypEnm(String opttype,String enname,Connection conn) throws SQLException {
		
		if(enname == null || enname.trim().equals("")){
			logger.info("传进来的行业类型编号为空:"+enname);
			return "";
		}
		PreparedStatement stmt=null;
		ResultSet rs = null;
		StringBuffer schemeSql=new StringBuffer();
		String cnname=null;
		try {
			
			schemeSql.append("select cnname from s_treedic where enname=? and opttype=?");
			stmt = conn.prepareStatement(schemeSql.toString());
			stmt.setString(1, enname.trim());
			stmt.setString(2, opttype.trim());
			logger.info("待执行的schemeDelSql: "+schemeSql);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				cnname=rs.getString("cnname");
			}
		} catch (SQLException e1) {
			conn.rollback();
			e1.printStackTrace();
		}finally {
			this.closeResource(stmt, rs);
			if (rs != null) {
				rs = null;
			}
			if (stmt != null) {
				stmt = null;
			}
		}
		return cnname;
	}

}

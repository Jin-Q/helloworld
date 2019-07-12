package com.yucheng.cmis.biz01line.esb.pub;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.esb.domain.PooConnection;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.dao.config.SqlConfigLoader;
/**
 * 获取数据库连接池(废弃)
 * @author Pansq
 */
public class DBUtil {

	/** -- 数据库用户名 -- */
	private static final String user = "yccsd";
	/** -- 数据库密码 -- */
	private static final String pass = "yccsd";
	/** -- 数据库URL -- */
	private static final String url = "jdbc:oracle:thin:@168.168.241.89:1521:CREDIT";
	/** -- 数据库驱动 -- */
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	/** -- 数据库连接池初始化大小 -- */
	private static final int initConnSize = 5;
	/** -- 数据库递增大小 -- */
	private static final int increateSize = 5;
	/** -- 数据库连接池最大连接数 -- */
	private static int maxConnSize = 100;
	/** -- 数据库连接池向量实体类 -- */
	private Vector<PooConnection> connections = null;
	
	/**
	 * 创建一个数据库连接池，连接池中连接数采用连接池常量实体类
	 * @throws Exception
	 */
	public synchronized void createDataSource() throws Exception {
		if(connections != null){
			return;
		}
		/** 实例化JDBC Driver 中指定的驱动实例 */
		Driver dv =  (Driver)Class.forName(driver).newInstance();
		/** 注册JDBC驱动 */
		DriverManager.registerDriver(dv);
		/** 创建保存的向量 */
		connections = new Vector<PooConnection>();
		/** 创建初始化大小的数据库连接池 */
		createConnections(initConnSize);
	}
	
	/**
	 * 创建指定大小上德数据库连接池，并且把穿件的连接放入connections向量中
	 * @param initConnSize 初始化大小
	 * @throws Exception
	 */
	private void createConnections(int initConnSize) throws Exception {
		for(int x=0;x<initConnSize;x++){
			/** 判断连接池连接数量是否达到最大，如果maxConnSize为0或者负数，则表示没有限制 */
			if(maxConnSize > 0 && this.connections.size() >= initConnSize){
				break;
			}
			try {
				connections.addElement(new PooConnection(newConnection()));
			} catch (Exception e) {
				System.out.println("创建数据库连接失败："+e.getMessage());
			}
		}
	}

	/**
	 * 创建并且返回一个新的数据库连接
	 * @return
	 * @throws Exception
	 */
	public Connection newConnection() throws Exception {
		/** 创建一个新的数据库连接 */
		Connection connection = DriverManager.getConnection(url,user,pass);
		if(this.connections.size() == 0){
			/** 返回数据库允许的最大连接 */
			DatabaseMetaData metaData = connection.getMetaData();
			int dvMaxConn = metaData.getMaxConnections();
			/** 数据库允许的最大连接替换数据库连接池允许的最大连接数 */
			if(dvMaxConn>0 && maxConnSize > dvMaxConn ){
				maxConnSize = dvMaxConn;
			}
		}
		return connection;
	}
	/**
	 * 获取可用的数据库连接，如果没有可用的数据库连接，并且更多的数据库连接不能创建，函数等待一段时间在做尝试
	 * @return
	 * @throws Exception
	 */
	public synchronized Connection getConnection() throws Exception {
		if(connections == null){
			return null;
		}
		/** 获取可用的数据库连接 */
		Connection connection = getFreeConnection();
		while(connection == null){
			wait(250);
			connection = getFreeConnection();
		}
		return connection;
		
	}
	/**
	 * 从连接池想向量connections中获取返回一个可用的连接，如果当前没有可用的数据库连接，则根据当前数据自增大小创建，并添加到向量中
	 * @return 返回一个可用的数据库连接
	 * @throws Exception
	 */
	public Connection getFreeConnection() throws Exception {
		Connection connection = findFreeConnection();
		if(connection == null){
			createConnections(initConnSize);
			connection = findFreeConnection();
			if(connection == null){
				return null;
			}
		}
		return connection;
	}
	/**
	 * 遍历数据库连接池中所有连接，查找一个可用的连接，没有则返回null
	 * @return
	 */
	public Connection findFreeConnection(){
		Connection connection = null;
		PooConnection pc = null;
		Enumeration<PooConnection> enuList = connections.elements();
		while(enuList.hasMoreElements()){
			pc = enuList.nextElement();
			if(!pc.isBusy()){
				connection = pc.getConnection();
				/** 如果连接对象不忙，则获取该连接，并且设置该连接为忙碌 */
				pc.setBusy(true);
				
				pc.setConnection(connection);
				break;
			}
		}
		return connection;
	}
	
	/**
	 * 返回一个连接池对象，并且设置其装填为空闲状态
	 * @param connection
	 */
	public void returnConnection(Connection connection){
		if(connection  == null){
			return;
		}
		PooConnection pc = null;
		Enumeration<PooConnection> enumList = connections.elements();
		while(enumList.hasMoreElements()){
			/** 获取一个连接对象 */
			pc = enumList.nextElement();
			/** 找到需要返回的连接池对象，设置此连接对象为空闲状态 */
			if(connection == pc.getConnection()){
				pc.setBusy(false);
				break;
			}
		}
	}
	
	/**
	 * 刷新连接池中所有对象
	 * @throws Exception
	 */
	public synchronized void refreshConnections() throws Exception {
		/** 保证连接池是否存在 */
		if(connections == null){
			return;
		}
		PooConnection pc = null;
		Enumeration<PooConnection> enumList = connections.elements();
		while(enumList.hasMoreElements()){
			/** 获取一个连接对象 */
			pc = enumList.nextElement();
			if(pc.isBusy()){
				wait(5000);
			}
			/** 关闭此连接，并且用一个新的连接替代 */
			closeConnection(pc.getConnection());
			pc.setConnection(newConnection());
			pc.setBusy(false);
		}
	}
	/**
	 * 关闭连接池中所有连接，并且清空连接池
	 */
	public synchronized void closePooConnection(){
		if(connections == null){
			return;
		}
		PooConnection pc = null;
		Enumeration<PooConnection> enumList = connections.elements();
		while(enumList.hasMoreElements()){
			/** 获取一个连接对象 */
			pc = enumList.nextElement();
			if(pc.isBusy()){
				wait(5000);
			}
			/** 关闭此连接，并且用一个新的连接替代 */
			closeConnection(pc.getConnection());
			/** 从连接池向量中删除 */
			connections.removeElement(pc);
		}
		connections = null;
	}
	/**
	 * 关闭一个数据库连接
	 * @param connection
	 */
	public synchronized void closeConnection(Connection connection){
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("关闭数据库连接出错："+e.getMessage());
		}
	}
	/**
	 * 等待给定时间（毫秒）
	 * @param second
	 */
	public void wait(int second){
		try {
			Thread.sleep(second);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean testConn(Connection connection){
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public Vector<PooConnection> getConnections() {
		return connections;
	}

	public void setConnections(Vector<PooConnection> connections) {
		this.connections = connections;
	}
	public static void main(String[] args) {
		Connection conn;
		try {
			DBUtil t =  new DBUtil();
			t.createDataSource();
			conn=t.getFreeConnection();
			SqlConfigLoader sc = new SqlConfigLoader();
			//sc.loadSqlConfig("F:\\workspace_ws\\cmis\\src\\main\\config\\sql\\biz01line\\esb\\");
			/*KeyedCollection param = new KeyedCollection();
			param.put("CLIENT_NO", "0000005469525009");
			param.put("START_DATE","2013-01-01");
			param.put("END_DATE", "2015-01-01");
			param.put("ACC_STATUS", "1");
			IndexedCollection sxIColl = (IndexedCollection) SqlClient.queryList4IColl("Trade0200300000247_01", param, conn);*/
		/*	KeyedCollection param = new KeyedCollection();
			param.put("CLIENT_NO", "0000005469525009");
			param.put("START_DATE","2013-01-01");
			param.put("END_DATE", "2015-01-01");
			param.put("DISCOUNT_STATUS", "0");
			IndexedCollection IColl = (IndexedCollection) SqlClient.queryList4IColl("Trade0300300000231", param, conn);*/
	/*		KeyedCollection param = new KeyedCollection();
			param.put("CLIENT_NO", "0000005469525009");
			param.put("START_DATE","2013-01-01");
			param.put("END_DATE", "2015-01-01");
			IndexedCollection IColl =null ;
			if("02".equals("01")){
				IColl= (IndexedCollection) SqlClient.queryList4IColl("Trade0200300000248_01", param, conn);
			}else if("02".equals("02")){
				IColl= (IndexedCollection) SqlClient.queryList4IColl("Trade0200300000248_02", param, conn);
			}*/
			KeyedCollection param = new KeyedCollection();
			param.put("CLIENT_NO", "0000005469525009");
			param.put("START_DATE","2013-01-01");
			param.put("END_DATE", "2015-01-01");
			param.put("BILL_STATUS", "0");
			IndexedCollection IColl= (IndexedCollection) SqlClient.queryList4IColl("Trade0200300000247_YP", param, conn);
			System.out.println(IColl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}

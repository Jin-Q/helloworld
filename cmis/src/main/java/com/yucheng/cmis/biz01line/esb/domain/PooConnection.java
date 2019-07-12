package com.yucheng.cmis.biz01line.esb.domain;

import java.sql.Connection;

public class PooConnection {

	/** 数据库连接 */
	Connection connection = null;
	/** 数据库连接是否正在使用 */
	boolean busy = false;
	
	public PooConnection(Connection connection){
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public boolean isBusy() {
		return busy;
	}
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
}

package com.yucheng.cmis.biz01line.esb.pub;

/**
 * FTP连接参数VO类
 * 
* @author yangzy
* @since 2015/07/24
* @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
 */
public class FtpInfo {

	/** 登录用户名 */
	private String name;

	/** 登录密码 */
	private String password;

	/** 登录IP地址 */
	private String ip;

	/** 登录端口 */
	private int port;

	public FtpInfo(String name, String password, String ip, int port) {
		super();
		this.name = name;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}

	public FtpInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}

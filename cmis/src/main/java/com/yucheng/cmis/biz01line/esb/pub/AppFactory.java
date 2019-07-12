package com.yucheng.cmis.biz01line.esb.pub;

import com.yucheng.cmis.biz01line.esb.pub.FtpConnectMap;
import com.yucheng.cmis.biz01line.esb.pub.FtpInfo;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.pub.exception.FTPException;

/**
* FTP连接工厂类
* @author yangzy
* @since 2015/07/24
* @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
*/
public class AppFactory{
	private static AppFactory factory = new AppFactory();
	private AppFactory() {}
	public static AppFactory getInstance() {
		return factory;
	}
	/**
	* 创建一个FtpUtil的实例化对象
	* 
	* @param ftpInfo
	* @return
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public <T> T create(FtpInfo ftpInfo) throws Exception{
	   /** 获取存储对象的实例*/
	   FtpConnectMap ftpMap = FtpConnectMap.getInstance();
	   /** 从HashMap中获取一个连接的实例化对象*/
	   FTPUtil ftpUtil = ftpMap.getFtpUtil(ftpInfo.getName());
	   if(ftpUtil==null) {
	     /** 该连接不存在于HashMap中,实例化一个连接将此连接添加到HashMap中*/
	     try {
			ftpUtil = new FTPUtil(ftpInfo);
		} catch (Exception e) {
			throw new FTPException(e);
		}/** 实例化一个FTP连接*/
	     ftpUtil.connectServer();
	     ftpMap.addFtpUtil(ftpInfo.getName(),ftpUtil);
	   }
	   return (T)ftpUtil;
	}

	}


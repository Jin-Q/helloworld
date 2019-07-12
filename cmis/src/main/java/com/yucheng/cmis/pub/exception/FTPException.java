package com.yucheng.cmis.pub.exception;

import com.yucheng.cmis.base.CMISException;

/**
* FTP异常类
* @author yangzy
* @since 2015/07/24
* @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
*/
public class FTPException extends CMISException{
	private static final long serialVersionUID = 1L;
	public FTPException(Exception e){
		super(e);
	}
	public FTPException(String msg, Exception exception){
		super(msg,exception);
	}
	public FTPException(String msg){
		super(msg);
	}
}

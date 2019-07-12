package com.yucheng.cmis.biz01line.esb.pub;

import java.util.HashMap;

/**
* FTP连接池
* @author yangzy
* @since 2015/07/24
* @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
*/
public class FtpConnectMap {
/** K-V对应的连接池*/
@SuppressWarnings("unchecked")
private static HashMap<String,FTPUtil> connectMap = new HashMap<String,FTPUtil>();

private static FtpConnectMap ftpMap = null;

/**
* 获得FtpConnectMap的实例化对象
* 
* @return
*/
public static FtpConnectMap getInstance() {
   if(ftpMap==null){
    ftpMap = new FtpConnectMap();
   }
   return ftpMap;
}

/**
* 通过Name获取存在HashMap中的FTP连接对象
* 
* @param name
* @return
*/
public FTPUtil getFtpUtil(String name) {
	FTPUtil ftpUtil = (FTPUtil)connectMap.get(name);
   return ftpUtil;
}

    /**
    * 给HashMap添加一个新的FTP连接对象
    * 
    * @param name
    * @param ftpUtil
    */
    public void addFtpUtil(String name,FTPUtil ftpUtil) {
       connectMap.put(name, ftpUtil);
    }
    public static void cleanConnetMap () {
        connectMap = new HashMap<String,FTPUtil>();
    }
}


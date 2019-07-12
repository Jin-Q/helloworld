package com.yucheng.cmis.biz01line.esb.pub;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.oro.text.regex.*;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.dcfs.esb.pack.standardxml.XmlStaxParse;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.exception.FTPException;

/**
 * FTP传送工具类
 * @author Pansq
 */
public class FTPUtil {
	/** 登录用户名,密码,IP等参数*/
	private String name;
	private String password;
	private String ip;
	/** 登录端口*/
	private int port;
	/** 本地文件路径 */
	private String localfilepath;
	/** FTP文件路径 */
	private String remotefilepath;
	/** FTP操作对象*/
	private FTPClient ftpClient;
	/** FTP操作对象上传文件路劲地址名称*/
	private static final String uploadParamName = "ftp.localtempfilepath";
	/** FTP操作对象下传文件路径地址名称*/
	private static final String downloadParamName = "ftp.downloadtempfilepath";
	
	public FTPUtil(){
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		this.name = res.getString("ftp.username");
		this.password = res.getString("ftp.password");
		this.ip = res.getString("ftp.ip");
		this.port = Integer.parseInt(res.getString("ftp.port"));
		this.localfilepath = res.getString("ftp.localtempfilepath");
		this.remotefilepath = res.getString("ftp.remotetempfilepath");
	}
	
	/**
     * 构造方法,初始化传入要连接的FTP参数信息
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
     */
	public FTPUtil(FtpInfo ftpInfo) throws FTPException {
		if (ftpInfo == null) {
			/** 判断如果为null 就抛出初始化异常 */
			throw new FTPException("传递的FTP连接参数对象是null");
		} else {
			this.init(ftpInfo);
		}
	}
	/**
	 * 初始化FTP连接参数
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
	 */
	public void init(FtpInfo ftpInfo) {
		this.name = ftpInfo.getName();
		this.password = ftpInfo.getPassword();
		this.ip = ftpInfo.getIp();
		this.port = ftpInfo.getPort();
	}
	/**
	 * FTP进入服务器文件夹
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
	 */	
	public boolean cd(String path) throws FTPException {
		boolean f = false;
		try {
			f = this.ftpClient.changeWorkingDirectory(path);
		} catch (IOException ex) {
			throw new FTPException(ex);
		}
 
		return f;
	}
	/**
	 * 上传文件到FTP服务器上
	 * @param localFile 本地文件路径
	 * @param remoteFile 服务器文件路径
	 * @return boolean
	 * @throws Exception
	 */
	public boolean upload(String localFile, String remoteFile) throws Exception {
		/** 返回参数 */
		boolean fla = false;
		try {
			/** 设置文件类型 */
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			/** 上传之前先给文件命明为.TMP,防止文件传输中断 */
			String tempName = remoteFile + ".tmp";
			/** 开始上传文件 */
			ftpClient.storeFile(tempName, new FileInputStream(localFile));
			/** 上传完毕之后再该为原名 */
			renameFile(tempName, remoteFile);
			fla = true;
		} catch (Exception ex) {
			EMPLog.log(EMPConstance.TAG_FIELD, EMPLog.INFO, 0,"上传文件到FTP服务器异常！");
			throw new Exception(ex);
		}
		return fla;
	}
	
	/**
	 * 从ftp服务器下载文件
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public boolean download(String filePath,String filename) throws Exception {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String localFileDir = res.getString(downloadParamName);//本地路径
		File outfile = new File(localFileDir, filename);
		OutputStream oStream = null;
		connectServer();//连接ftp
		try {
			oStream = new FileOutputStream(outfile);
			if (filePath!=null&&!filePath.equals("")) {
				ftpClient.changeWorkingDirectory(filePath);//ftp地址
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean result = ftpClient.retrieveFile(filename, oStream);
			if (!result) {
				throw new Exception("下载文件失败");
			}
			return result;
		} catch (IOException e) {
			throw new Exception("ioException", e);
		} finally {
			if (oStream != null) {
				try {
					closeServer();
					oStream.close();
				} catch (IOException e) {
					throw new Exception("ioException", e);
				}
			}
		}
	}
	
	public static void main(String args[]) throws Exception{
		System.out.println("测试ftp下载开始。。。");
		FTPUtil ftp = new FTPUtil();
		ftp.download("/esb_hs/","123.xml");
		System.out.println("测试ftp下载结束。。。");
	}
	
	/**
	 * 断开ftp
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}
	
	/**
	 * 重命名FTP上传文件名称
	 * @param oldName 原名称
	 * @param newName 新名称
	 * @throws Exception
	 */
	public void renameFile(String oldName,String newName) throws Exception{
		try{
		    ftpClient.rename(oldName, newName);
		}catch(Exception e){
			EMPLog.log(EMPConstance.TAG_FIELD, EMPLog.INFO, 0, "上传文件重命名异常！");
		    throw new Exception(e);
		}
	}
	
	/**
	 * 连接FTP服务器
	 * @throws Exception
	 */
	public void connectServer() throws Exception {
		/** 如果当前ftpClient对象是null且没有连接就连接到FTP服务器 */
		if (this.ftpClient == null || !this.ftpClient.isConnected()) {
			try {
				/** 如果ftpClient对象为null就实例化一个新的 */
				this.ftpClient = new FTPClient();
				/** 设置默认的IP地址 */
				this.ftpClient.setDefaultPort(this.port);
				/** 连接到FTP服务器 */
				this.ftpClient.connect(this.ip);
				/** 登录到这台FTP服务器 */
				if (ftpClient.login(this.name, this.password)) {
					EMPLog.log(EMPConstance.TAG_FIELD, EMPLog.INFO, 0,"FTP：连接 " + this.ip + "'s User " + this.name + "成功！");
				} else {
					EMPLog.log(EMPConstance.TAG_FIELD, EMPLog.ERROR, 0,"FTP：连接 " + this.ip + "'s User " + this.name + "异常！");
				}
			} catch (Exception ex) {
				EMPLog.log(EMPConstance.TAG_FIELD, EMPLog.ERROR, 0,"FTP：连接 " + this.ip + "'s User " + this.name + "异常！");
				throw new Exception(ex);
			}
		}
	}
	
	/**
	* 销毁FTP服务器连接
	*/
	public void closeConnect() {
		/** 判断当前ftpClient对象不为null和FTP已经被连接就关闭 */
		if (this.ftpClient != null && this.ftpClient.isConnected()) {
			try {
				// FtpConnectMap.cleanConnetMap();
				this.ftpClient.disconnect();
				/** 销毁FTP连接 */
			} catch (Exception ex) {
			}
		}
	}
	/**
	 * 获取生成的xml文件名称
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @return 文件名称
	 */
	public static String getFileName(String serviceCode, String serviceSence, String serno){
		String fileName = serviceCode+"_"+serviceSence+"_"+serno+".xml";
		return fileName;
	}
	
	public static String getFilePath(){
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		return res.getString("ftp.remotetempfilepath");
	}
	/**
	 * 将结构体写入指定本地目录下，并且返回文件目录路径
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @param reqCD 发送结构体
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public String setLocalFile(String serviceCode, String serviceSence, String serno, CompositeData reqCD) throws IOException{
		/** -------------------------------------将交易信息写入xml文件中本地xml文件中--------------------------------------- */
		//命名发送文件的规则(交易码+_+场景+_+授权码.xml)
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String localFile = res.getString("ftp.localtempfilepath")+FTPUtil.getFileName(serviceCode, serviceSence, serno);
		File file = new File(localFile);
		BufferedOutputStream bos = null;
		try {
			OutputStream os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			String input = new String(PackUtil.pack(reqCD),"UTF-8");
			bos.write(input.getBytes("UTF-8"));
			bos.flush();
		} catch (Exception e) {			
			EMPLog.log("inReport", EMPLog.DEBUG, 0, "***********************************写入本地文件"+FTPUtil.getFileName(serviceCode, serviceSence, serno)+"出错*************************************");
			throw new IOException();
		} finally {
			bos.close();
		}
		return localFile;
	}
	/**
	 * 上传文件到FTP服务器
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @param reqCD 发送结构体
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public static void send2FTP(String serviceCode, String serviceSence, String serno, CompositeData reqCD) throws Exception {
		FTPUtil ftp = new FTPUtil();
		String remotepath = ftp.remotefilepath+FTPUtil.getFileName(serviceCode, serviceSence, serno);
		ftp.connectServer();
		ftp.upload(ftp.setLocalFile(serviceCode, serviceSence, serno, reqCD), remotepath);
		ftp.closeConnect();
	}
	
	/**
	 * 从ftp下载文件
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @param reqCD 发送结构体
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public static void FTP2local(String filePath,String filename) throws Exception {
		FTPUtil ftp = new FTPUtil();
		ftp.download(filePath,filename);
	}
	
	/**
	 * 更具成功标识，将下传结构体保存在指定目录下
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @param reqCD 发送结构体
	 * @param openDay 营业时间
	 * @param succ 成功标识，用于判断营业时间是否一致
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public static String getLocalFileName(String serviceCode, String serviceSence, String serno, CompositeData reqCD, String openDay) throws Exception {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String path = res.getString(downloadParamName);
		File dir = new File(path);
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		String localFile = path+(openDay+"_"+FTPUtil.getFileName(serviceCode, serviceSence, serno));
		File file = new File(localFile);
		BufferedOutputStream bos = null;
		try {
			OutputStream os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			String input = new String(PackUtil.pack(reqCD),"UTF-8");
			bos.write(input.getBytes("UTF-8"));
			bos.flush();
		} catch (Exception e) {
			EMPLog.log("inReport", EMPLog.DEBUG, 0, "***********************************写入本地文件"+(openDay+"_"+FTPUtil.getFileName(serviceCode, serviceSence, serno))+"出错*************************************");
		} finally {
			bos.close();
		}
		return localFile;
	}
	
	/**
	 * 报文文件转成CD
	 * @param filename
	 * @param reqCD
	 * @return
	 * @throws Exception
	 */
	public static CompositeData getFile2CD(String filename, CompositeData reqCD) throws Exception {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String path = res.getString(downloadParamName);
		filename = path + filename;
		File file = new File(filename);
		XmlStaxParse parse = new XmlStaxParse();
		FileInputStream in;
		byte[] bts = null;
		try {
			in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int intLength = 0;
			while ((intLength = in.read()) != -1) {
				out.write(intLength);
			}
			bts = out.toByteArray();
		} catch (Exception e1) {
		}
		parse.parse(bts, reqCD);
		return reqCD;
	}
	/**
	 * 上传文件到FTP服务器 for .txt 文件
	 * @param file .txt 文件
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public static void send2FTP4Txt(String filename,StringBuffer sb) throws Exception {
		FTPUtil ftp = new FTPUtil();
		String remotepath = ftp.remotefilepath+filename;
		ftp.connectServer();
		ftp.upload(ftp.setLocalFile4Txt(filename,sb), remotepath);
		ftp.closeConnect();
	}
	/**
	 * 将txt文件写入指定本地目录下，并且返回文件目录路径
	 * @param file txt文件
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public String setLocalFile4Txt(String filename,StringBuffer sb) throws IOException{
		/** -------------------------------------将文件信息写入本地中--------------------------------------- */
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String localFile = res.getString("ftp.localtempfilepath")+filename;
		BufferedOutputStream bos = null;
		File file = new File(localFile);
		try {
			OutputStream os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			String input = sb.toString();
			bos.write(input.getBytes("UTF-8"));
			bos.flush();
		} catch (Exception e) {			
			EMPLog.log("inReport", EMPLog.DEBUG, 0, "***********************************写入本地文件"+file.getName()+"出错*************************************");
			throw new IOException();
		} finally {
			bos.close();
		}
		return localFile;
	}
	/**
	 * 遍历ftp文件服务器文件夹，并返回文件数组
	 * @param 
	 * @return 文件数组
	 * @throws FTPException
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
	 */
	public FTPFile[] showFiles(String dir) throws FTPException {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		//String dir = res.getString("ftp.ebanktempfilepath");
		FTPFile[] files = null;
		boolean f = false;
		try {
			this.connectServer();
			f = this.cd(dir);
			if(f){
				ftpClient.setControlEncoding("GBK");
				files = this.ftpClient.listFiles();
			}
			
		} catch (Exception e) {
			throw new FTPException(e);
		}finally {
			try {
				closeServer();
			} catch (IOException e) {
				throw new FTPException("ioException", e);
			}
		}
		return files;
	}
	/**
	 * 删除ftp文件
	 * @param filePath 交易码
	 * @return 
	 * @throws Exception
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
	 */
	public boolean deleteFileFtp(String fileName,String filePath) throws Exception {
		//ResourceBundle res = ResourceBundle.getBundle("ftp");
		//String dir = res.getString("ftp.ebanktempfilepath");
		try {
			this.connectServer();
			this.cd(filePath);
			ftpClient.setControlEncoding("GBK");
			fileName = new String(fileName.getBytes("GBK"),"iso-8859-1");
			boolean result = ftpClient.deleteFile(fileName);
			return result;
		} catch (Exception e) {
			throw new FTPException("删除文件失败");
		}finally {
			try {
				closeServer();
			} catch (IOException e) {
				throw new Exception("ioException", e);
			}
		}
	}
	/**
	 * 从ftp服务器下载文件
	 * @param filename
	 * @return
	 * @throws Exception
     * @author yangzy
     * @since 2015/07/24
     * @description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
	 */
	public boolean downloadFile(String filePath,String filename,String filename2) throws Exception {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String localFileDir = res.getString("ftp.ebankdownloadtempfilepath");//本地路径
		String dir = res.getString("ftp.ebanktempfilepath");
		File outfile = new File(localFileDir, filename2);
		OutputStream oStream = null;
		connectServer();//连接ftp
		try {
			oStream = new FileOutputStream(outfile);
			if (filePath!=null&&!filePath.equals("")) {
				ftpClient.changeWorkingDirectory(filePath);//ftp地址
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			filename = new String(filename.getBytes("GBK"),"iso-8859-1");
			boolean result = ftpClient.retrieveFile(filename, oStream);
			if (!result) {
				throw new Exception("下载文件失败");
			}
			return result;
		} catch (IOException e) {
			throw new Exception("ioException", e);
		} finally {
			if (oStream != null) {
				try {
					closeServer();
					oStream.close();
				} catch (IOException e) {
					throw new Exception("ioException", e);
				}
			}
		}
	}
	
	/**
	 * 更具成功标识，将下传结构体保存在指定目录下
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param serno 授权编码
	 * @param reqCD 发送结构体
	 * @param openDay 营业时间
	 * @param succ 成功标识，用于判断营业时间是否一致
	 * @return 生成的本地文件路径
	 * @throws IOException
	 */
	public static String getLocalFileNameForJson(String serviceCode, String serviceSence, String serno, JSONObject jsonObj, String openDay) throws Exception {
		ResourceBundle res = ResourceBundle.getBundle("ftp");
		String path = res.getString(downloadParamName);
		File dir = new File(path);
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		String localFile = path+(openDay+"_"+FTPUtil.getFileName(serviceCode, serviceSence, serno));
		File file = new File(localFile);
		BufferedOutputStream bos = null;
		try {
			OutputStream os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			String input =  jsonObj.toString();
			bos.write(input.getBytes("UTF-8"));
			bos.flush();
		} catch (Exception e) {
			EMPLog.log("inReport", EMPLog.DEBUG, 0, "***********************************写入本地文件"+(openDay+"_"+FTPUtil.getFileName(serviceCode, serviceSence, serno))+"出错*************************************");
		} finally {
			bos.close();
		}
		return localFile;
	}
}

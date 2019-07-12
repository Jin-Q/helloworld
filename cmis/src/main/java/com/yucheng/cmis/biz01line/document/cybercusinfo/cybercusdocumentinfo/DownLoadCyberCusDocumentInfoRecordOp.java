package com.yucheng.cmis.biz01line.document.cybercusinfo.cybercusdocumentinfo;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTPFile;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.operation.CMISOperation;

import com.cfcc.ecus.eft.compress.CompressException;
import com.cfcc.ecus.eft.compress.CompressUtil;
import com.icfcc.foundation.exception.CFCCException;

public class DownLoadCyberCusDocumentInfoRecordOp  extends CMISOperation {
	
	private final String modelId = "CyberCusDocumentInfo";
	
	private final String pk1_name = "file_pk";
	
	private boolean updateCheck = false;
	
	/**
	 * 下载文件
	 * @param context
	 * @return
	 * @throws Exception
	 *@description 需求编号：【XD150603041】福州分行线上服务项目后台数据提取
     *@version v1.0 yezm
	 */
	public String doExecute(Context context) throws EMPException {
		// 定义request对象
		HttpServletRequest request = null;
		// 定义文件输入流
		FileInputStream fis = null;
		String downLoadFile = "";
		Connection connection = null;
		ArrayList<String> batchFileList=new ArrayList<String>();
		String localFileDir = "";
		try{
			connection = this.getConnection(context);
			ResourceBundle res = ResourceBundle.getBundle("ftp");
			localFileDir = res.getString("ftp.ebankdownloadtempfilepath");//本地路径
			// 先清除本地文件
			for (File file : new File(localFileDir).listFiles())
				if (file.isFile() )
					file.delete();
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String fileStr = "";
			
			IndexedCollection icoll = (IndexedCollection)context.getDataElement("CyberCusDocumentInfoList");
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				String tmpFile = (String)kcoll.getDataValue("file_path").toString().trim();
				tmpFile = new String(tmpFile.getBytes("ISO8859-1"),"UTF-8");
				String[]tmpFileList = tmpFile.split("@");
				String filePath2 = tmpFileList[0];
				String fileName = tmpFileList[1];
				String fileName1 = fileName;
				if(fileStr.indexOf(fileName)>=0){
					int total = 0;
					for(String tmp = fileStr;tmp!=null&&tmp.length()>= fileName.length();){
						if(tmp.indexOf(fileName)==0){
							total++;
						}
						tmp = tmp.substring(1);
					}
					String sq = String.valueOf(total)+"_";
					fileName1 = sq+fileName1;
				}
				fileStr += fileName1+"@";
				batchFileList.add(fileName1);
				//下载
				FTPUtil util = new FTPUtil();
				util.downloadFile(filePath2,fileName,fileName1);
			}
			
			if(batchFileList!=null&&batchFileList.size()>1){//多个文件批量打包下载
				String batchFile = this.CompresssZip(batchFileList);
				downLoadFile = localFileDir+batchFile;
				File file = new File(downLoadFile);
				if(!file.exists()){
					throw new EMPException("文件【"+batchFile +"】找不到，请联系系统管理员！");
				}
				batchFileList.add(batchFile);
				fis = new FileInputStream(file);

				request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				request.setAttribute("inputStream", fis);
				request.setAttribute("filename", batchFile);
				request.setAttribute("tmpFile", file);
				
			}else{//单文件下载
				String down_file = batchFileList.get(0);
				downLoadFile = localFileDir+down_file;
				File file = new File(downLoadFile);
				if(!file.exists()){
					throw new EMPException("文件【"+down_file +"】找不到，请联系系统管理员！");
				}
				fis = new FileInputStream(file);

				request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
				request.setAttribute("inputStream", fis);
				request.setAttribute("filename", down_file);
				request.setAttribute("tmpFile", file);
			}					
		}catch (EMPException ee) {
			throw new EMPException("文档下载失败，失败原因："+ee.getMessage());
		} catch(Exception e){
			if (batchFileList.size() > 0) {
				for (int i = 0; i < batchFileList.size(); i++) {
					String tempFileName = (String) batchFileList.get(i);
					tempFileName = localFileDir+tempFileName;
					File tempFile = new File(tempFileName);
					if(tempFile.exists()){
						tempFile.delete();
					}
				}
			}
			throw new EMPException("文档下载失败，失败原因："+e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	public String CompresssZip(ArrayList batchFileList)throws EMPException {
		System.out.println("开始打包批量文件");
		String fzip = "";
		ArrayList<String> FileList=new ArrayList<String>();
		if (batchFileList.size() > 0) {
			ResourceBundle res = ResourceBundle.getBundle("ftp");
			String localFileDir = res.getString("ftp.ebankdownloadtempfilepath");//本地路径
			String fileName = (String) batchFileList.get(0);
			fzip = fileName + ".zip";
			System.out.println("fzip:"+fzip);
			for (int i = 0; i < batchFileList.size(); i++) {
				String tempFileName = (String) batchFileList.get(i);
				tempFileName = localFileDir+tempFileName;
				FileList.add(i, tempFileName);
			}
			try {
				CompressUtil.compressZip(FileList, localFileDir+fzip);
			} catch (CompressException e) {
				File tempZipFile = new File(fzip);
				if(tempZipFile.exists()){
					tempZipFile.delete();
				}
				throw new EMPException("打包批量文件失败:" + e);
			}
		} else {
			throw new EMPException("不存在批量待打包文件");
		}
		return fzip;
	}
}

package com.yucheng.cmis.biz01line.document;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.msi.msiimple.ArpServiceInterfaceImple;
import com.yucheng.cmis.operation.CMISOperation;

public class DownLoadPubDocumentInfoRecordOp  extends CMISOperation {
	
	private final String modelId = "PubDocumentInfo";
	
	private final String pk1_name = "file_pk";
	
	private boolean updateCheck = false;
	
	private static final Logger logger = Logger.getLogger(DownLoadPubDocumentInfoRecordOp.class);
	
	/**
	 * 下载文件
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String doExecute(Context context) throws EMPException {
		// 定义request对象
		HttpServletRequest request = null;
		// 定义文件输入流
		FileInputStream fis = null;
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String filePath = null;
			try {
				filePath = (String)context.getDataValue("file_path").toString().trim();
			} catch (Exception e) {}
			
			String fileName = null;
			try {
				fileName = (String)context.getDataValue("file_name");
				//fileName = new String(fileName.getBytes("ISO8859-1"),"UTF-8");
			} catch (Exception e) {}
			
			String pk1_value = null;
			try {
				pk1_value = (String)context.getDataValue(pk1_name);
			} catch (Exception e) {}
			if(pk1_value == null || pk1_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk1_name+"] cannot be null!");
		
			File file = new File(filePath);
			if(!file.exists()){
				throw new EMPException("文件【"+fileName +"】找不到，请联系系统管理员！");
			}
			fis = new FileInputStream(file);

			request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
			request.setAttribute("inputStream", fis);
			request.setAttribute("filename", fileName);
			request.setAttribute("tmpFile", file);
			
		}catch (EMPException ee) {
			throw new EMPException("文档下载失败，失败原因："+ee.getMessage());
		} catch(Exception e){
			throw new EMPException("文档下载失败，失败原因："+e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}

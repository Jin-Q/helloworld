package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.ResourceUtils;

/** 
 * 通用的导出模板op
 * @author 
 * @version 
 */

public class DownLoadTemplateOp extends CMISOperation{
	
    
  	private final static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
  	Context context=null;
//  	 IExport iexport=null;
  /**
   * 通用的导出模板op
   */
	public String doExecute(Context context) throws EMPException {
		
		
	    String templateName="";
	  	Connection connection = null;
	  	 
	  	HttpServletRequest request = null;
	  	this.context=context;

		
		//连接
		try{
			//KeyedCollection kColl = (KeyedCollection)((IndexedCollection)context.getDataElement("FncConfTemplateList")).get(0);
			templateName=(String)context.getDataValue("fnc_id")+".xls";
			connection = this.getConnection(context);
			
			ResourceBundle res = ResourceBundle.getBundle("cmis");
			//获得cmis配置文件中配置的模板位置
			   String dir = res.getString("template.path");
	           String  template= ResourceUtils.getFile(dir)
	                    .getAbsolutePath()+"/"+templateName+"";
			

			FileInputStream fis = new FileInputStream(template);
			POIFSFileSystem file = new POIFSFileSystem(fis);
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			File tmpFile=outputExcel(workbook);
			
             if (tmpFile != null) {
				
				fis = new FileInputStream(tmpFile);
				
				
				request = (HttpServletRequest) context
						.getDataValue(EMPConstance.SERVLET_REQUEST);
				request.setAttribute("inputStream", fis);
				request.setAttribute("filePath", template);
				
				request.setAttribute("filename", (String)context.getDataValue("fnc_name")+".xls");

				request.setAttribute("tmpFile", tmpFile);
			} else {
				return "0";
			}

		}catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"op层DownLoadTemplateOp处理出错:" + e.getMessage());	
			throw new EMPException(e.getMessage()); 
		} finally {
			if (connection != null)
				try {
					this.releaseConnection(context, connection);
				} catch (EMPJDBCException e) {
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0,"op层DownLoadTemplateOp处理出错:" + e.getMessage());	
					throw new EMPException(e.getMessage()); 
				}
		}
		
		return "0";
	}
	
	public File outputExcel(HSSFWorkbook wb) { 
		File tmpFile=null;
		FileOutputStream fos = null; 
		
		try { 
			String temp = format.format(new Date());
			tmpFile = File.createTempFile(temp, ".XML");

		fos = new FileOutputStream(tmpFile); 
		wb.write(fos);
		// 刷新流
		fos.flush();
		fos.close(); 
		} catch (FileNotFoundException e) { 
		e.printStackTrace(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} 
		return tmpFile;
		} 
	
}

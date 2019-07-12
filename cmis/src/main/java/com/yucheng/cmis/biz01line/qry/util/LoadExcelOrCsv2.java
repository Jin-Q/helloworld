package com.yucheng.cmis.biz01line.qry.util;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.pub.CMISDao;

/**
 * <p>将文本文件导出为Excel或Csv格式</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>yucheng: </p>
 * @author
 * @version 1.0
 */
public class LoadExcelOrCsv2 {
	private static final Logger logger = Logger.getLogger(CMISDao.class);    
	
	private final String OUT_ENCODE = "GBK";
	private final String IN_ENCODE  = "UTF-8";
	public static final long MAXLEN = 50000; //可导出为Excel最大文件长度
	
	public static final int LINE_LEN = 500; //读文件时候缓冲行长度，根据查询分析具体情况暂设定为500，标准的JAVA IO API一般设置为80
	
	public synchronized void loadExcel(List iColl, String chineseName, HttpServletResponse response) throws CMISException{
		File file = null;
		OutputStream outPut = null;
		//InputStream reader = null;
		
		String str = "";
		
		int row_idx = 0;
		
		String path;
		/*ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");  
		URL url=QryGenPageComponent.class.getResource("");
		path=url.getPath(); 
		path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 

		path = path +File.separator+ dir;
*/		//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		
		
		try {
		/*	fileName = path + File.separator + fileName;
			
			file = new File(fileName);
//			if( file.length() > MAXLEN ){
//				throw new CMISException("文件超长，请用CSV格式导出！");
//			}
			reader = new BufferedInputStream(new FileInputStream(file));
	 */		
			response.reset();
			//response.setContentType("application/csv; charset ="+OUT_ENCODE);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=default.xls");
			response.setCharacterEncoding(OUT_ENCODE);
			outPut = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			/*创建sheet*/
			HSSFSheet sheet = wb.createSheet("AnalyseData");
           
			HSSFCell cell = null;
			//创建行
			/*for(Iterator its = map.keySet().iterator(); its.hasNext();) {
				HSSFRow row = sheet.createRow((short) (row_idx++));
				for(int k=0;k < map.size();k++) {
					cell = row.createCell((short) k);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String key = (String) its.next();
					String value = (String) map.get(key);
					cell.setCellValue(value);
				}
			}*/
		
			//写入表头
			String[] cName = chineseName.split("\\|");
			HSSFRow rows = sheet.createRow((short) (row_idx++));
			for(int k = 0;k < cName.length;k++) {
				cell = rows.createCell((short) k);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cName[k]);
			}
			
			
			for (int i = 0; i < iColl.size(); i++) {

				HashMap kColl = (HashMap) iColl.get(i);
				HSSFRow row = sheet.createRow((short) (row_idx++));

				for (Iterator it = kColl.keySet().iterator(); it.hasNext();) {
					for (int j = 0; j < kColl.size(); j++) {
						cell = row.createCell((short) j);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						String key = (String) it.next();
						String value = (String) kColl.get(key);
						cell.setCellValue(value);
					}
				}

			}
				
            /*写入Response中*/
			wb.write(outPut);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException("文件超长，请用CSV格式导出！");
		} finally{
			try {
				if( outPut != null ) outPut.flush();
			} catch (Exception e) {}
			try {
				if( outPut != null ) outPut.close();
			} catch (Exception e) {}
			try {
				response.flushBuffer();
			} catch (Exception e) {}
		}
	}
	
	
    public void loadCsv(List iColl,String chineseName, HttpServletResponse response) throws CMISException{
    	//InputStream reader = null;
		OutputStream outPut = null;
		//int i = -1;
		int row_idx = 0;
		
/*		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");  
		URL url=QryGenPageComponent.class.getResource("");
		path=url.getPath(); 
		path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 
		path = path +File.separator+ dir;
		//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		String str;
		String outStr;
		*/
		try {
		//	fileName = path + File.separator + fileName;
			
			response.reset();
			
			response.setContentType("application/csv; charset ="+OUT_ENCODE);
			response.setHeader("Content-Disposition", "attachment;filename=default.csv");
			response.setCharacterEncoding(OUT_ENCODE);
			outPut = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			/*创建sheet*/
			HSSFSheet sheet = wb.createSheet("AnalyseData");
/*			reader = new BufferedInputStream(new FileInputStream(fileName));

			while( (str=readLine(reader)) != null ){       		              
		                str =new String(str.getBytes(),IN_ENCODE).trim();		            	               
				str = str.replaceAll("\\|", ",");			
				outPut.write(str.getBytes(OUT_ENCODE));
				outPut.write('\n');
			}*/
			
			HSSFCell cell = null;
			//写入表头
			String[] cName = chineseName.split("\\|");
			HSSFRow rows = sheet.createRow((short) (row_idx++));
			for(int k = 0;k < cName.length;k++) {
				cell = rows.createCell((short) k);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cName[k]);
			}
			
			/*for(Iterator its = map.keySet().iterator(); its.hasNext();) {
				HSSFRow row = sheet.createRow((short) (row_idx++));
				for(int k=0;k < map.size();k++) {
					cell = row.createCell((short) k);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String key = (String) its.next();
					String value = (String) map.get(key);
					cell.setCellValue(value);
				}
			}*/
			//创建行
			for (int i = 0; i < iColl.size(); i++) {

				HashMap kColl = (HashMap) iColl.get(i);
				HSSFRow row = sheet.createRow((short) (row_idx++));

				for (Iterator it = kColl.keySet().iterator(); it.hasNext();) {
					for (int j = 0; j < kColl.size(); j++) {
						cell = row.createCell((short) j);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						String key = (String) it.next();
						String value = (String) kColl.get(key);
						cell.setCellValue(value);
					}
				}

			}
			wb.write(outPut);
			outPut.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException(e);
		} finally{
			try {
				if( outPut != null ) outPut.flush();
			} catch (Exception e) {}
			try {
				if( outPut != null ) outPut.close();
			} catch (Exception e) {}
			try {
				response.flushBuffer();
			} catch (Exception e) {}
		}
	}
}

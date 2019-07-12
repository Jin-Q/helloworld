package com.yucheng.cmis.biz01line.qry.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;
import com.yucheng.cmis.pub.CMISDao;

/**
 * <p>将文本文件导出为Excel或Csv格式</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>yucheng: </p>
 * @author 胡宏伟
 * @version 1.0
 */
public class LoadExcelOrCsv {
	private static final Logger logger = Logger.getLogger(CMISDao.class);    
	
	private final String OUT_ENCODE = "GBK";
	private final String IN_ENCODE  = "UTF-8";
	public static final long MAXLEN = 50000; //可导出为Excel最大文件长度
	
	public static final int LINE_LEN = 500; //读文件时候缓冲行长度，根据查询分析具体情况暂设定为500，标准的JAVA IO API一般设置为80
	
	public synchronized void loadExcel(String fileName, HttpServletResponse response) throws CMISException{
		File file = null;
		OutputStream outPut = null;
//		InputStream reader = null;
		BufferedReader bufferReader = null;
		
		String str = "";
		
		int row_idx = 0;
		
		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");  
		URL url=QryGenPageComponent.class.getResource("");
		path=url.getPath(); 
		path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 

		path = path +File.separator+ dir;
		//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		
		
		try {
			fileName = path + File.separator + fileName;
			
			file = new File(fileName);
//			if( file.length() > MAXLEN ){
//				throw new CMISException("文件超长，请用CSV格式导出！");
//			}
//			reader = new BufferedInputStream(new FileInputStream(file));
			bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),IN_ENCODE));
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=default.xls");
		    
			outPut = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			/*创建sheet*/
			HSSFSheet sheet = wb.createSheet("AnalyseData");
			/*创建单元格样式*/
			HSSFCellStyle styleAl=wb.createCellStyle();
			//styleAl.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			HSSFCellStyle style=wb.createCellStyle();
			HSSFDataFormat format=wb.createDataFormat();
			style.setDataFormat(format.getFormat("0.000%"));
			while( (str = bufferReader.readLine()) != null ){
//				str =new String(str.getBytes(),IN_ENCODE).trim();
				
				/**如果数据中包含链接，将连接后面的数据去除         2014-06-11  唐顺岩 */
				if(str.indexOf("&@&")>0){
					String str_last = str.substring(str.indexOf("&@&"));   //截取分隔号后面字串
					str = str.substring(0,str.indexOf("&@&")) + str_last.substring(str_last.indexOf("|"),str_last.length());
				}
				/** END */
				/**如果数据中包含HTML的空格，将其去掉         2014-06-12  唐顺岩 */
				str = str.replaceAll("&nbsp;", "");
				/** END */
				
				String[] split = str.split("\\|");
				
				if( split != null && split.length != 0 ){
					/*创建行*/
					HSSFRow row = sheet.createRow((short)(row_idx++));
					
					for( int i=0; i<split.length; i++ ){
						/*创建元素并设置为字符型*/
						HSSFCell cell = row.createCell((short)i);
						cell.setCellStyle(styleAl);
						if(split[i].indexOf("-num")>0){
							split[i]=split[i].replaceAll("-num","");
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if(split[i].indexOf(".")>0)
						cell.setCellValue(Double.parseDouble(split[i]));
						else cell.setCellValue(Integer.parseInt(split[i]));
						
						}
						else if(split[i].indexOf("%")>0&&isDouble(split[i].substring(0,split[i].indexOf("%")))){
							
							split[i]=split[i].substring(0,split[i].indexOf("%"));
							cell.setCellStyle(style);
							cell.setCellValue(Double.parseDouble(split[i])/100);
										}
					else{
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(split[i]);
				}	
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
				if (bufferReader != null)
					bufferReader.close();
				bufferReader = null;
			} catch (Exception e) {
			}
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
	
	private boolean isDouble(String str) {
		// TODO Auto-generated method stu
		try{
			Double.parseDouble(str);
		return true;}
		catch(Exception e){
			return false;
		}
	}

	/**
	 * 读取文件中一行，但不包括换行符
	 * @param reader  文件流
	 * @return
	 * @throws Exception
	 */
	private String readLine(InputStream reader) throws Exception{
		int len = 0;
		int offset = 0;
		byte[] buffer = new byte[LINE_LEN];
		byte[] middle = null;
		byte[] result = null;
		int bt = -1;
		
		while( (bt=reader.read()) != -1 ){
			if( bt == '\n' ){
				break;
			}
			if( len == LINE_LEN ){
				if( offset != 0 ){
				   middle = new byte[offset];
				   System.arraycopy(result, 0, middle, 0, offset);
				}
				offset += len;
				result = new byte[offset];
				if( offset != len ){
				   System.arraycopy(middle, 0, result, 0, offset-len);
				}
				System.arraycopy(buffer, 0, result, offset-len, len);
				len = 0;
			}
			buffer[len++] = (byte)bt;
		}
		offset += len;
		if( offset == 0 ){
			return null;
		}else{
			if( offset > LINE_LEN ){
				middle = new byte[offset-len];
				System.arraycopy(result, 0, middle, 0, offset-len);
				result = new byte[offset];
				System.arraycopy(middle, 0, result, 0, offset-len);
			}else{
				result = new byte[offset];
			}
			System.arraycopy(buffer, 0, result, offset-len, len);
		}
		return new String(result);
	}
	
    public void loadCsv(String fileName, HttpServletResponse response) throws CMISException{
//    	InputStream reader = null;
		OutputStream outPut = null;
		BufferedReader bufferReader = null;
		
		int i = -1;

		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");  
		URL url=QryGenPageComponent.class.getResource("");
		path=url.getPath(); 
		path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 
		path = path +File.separator+ dir;
		//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		String str;
		String outStr;
		
		try {
			fileName = path + File.separator + fileName;
			
			response.reset();
			
			response.setContentType("application/csv; charset ="+OUT_ENCODE);
			response.setHeader("Content-Disposition", "attachment;filename=default.csv");
			response.setCharacterEncoding(OUT_ENCODE);
			outPut = response.getOutputStream();
			
//			reader = new BufferedInputStream(new FileInputStream(fileName));
			bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),IN_ENCODE));

			while( (str=bufferReader.readLine()) != null ){       		              
//                str =new String(str.getBytes(),IN_ENCODE).trim();		     
				/**如果数据中包含链接，将连接后面的数据去除         2014-06-11  唐顺岩 */
				if(str.indexOf("&@&")>0){
					String str_last = str.substring(str.indexOf("&@&"));   //截取分隔号后面字串
					str = str.substring(0,str.indexOf("&@&")) + str_last.substring(str_last.indexOf("|"),str_last.length());
				}
				/** END */
				/**如果数据中包含HTML的空格，将其去掉         2014-06-12  唐顺岩 */
				str = str.replaceAll("&nbsp;", "");
				/** END */
				str = str.replaceAll("\\|", ",");	
				str=str.replaceAll("-num", "");
				String matchStr = "^[0-9]+$";
				String[] split = str.split(",");
				String str2 = "";
				if( split != null && split.length != 0 ){
					for( int j=0; j<split.length; j++ ){
			            String str1 = split[j];
			            if (str1.matches(matchStr)) {
			              str1="'"+str1;
			              }
			            str2+=str1+",";
			          }
				}
				outPut.write(str2.getBytes(OUT_ENCODE));
				outPut.write('\n');
			}
			
			outPut.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CMISException(e);
		} finally{
			try {
				if (bufferReader != null)
					bufferReader.close();
				bufferReader = null;
			} catch (Exception e) {
			}
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
    
	public synchronized void loadExcelByCode(String fileName, HttpServletResponse response) throws CMISException{
		File file = null;
		OutputStream outPut = null;
		InputStream reader = null;
		
		String str = "";
		
		int row_idx = 0;
		
		String path;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("qry.result.path");  
		URL url=QryGenPageComponent.class.getResource("");
		path=url.getPath(); 
		path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 

		path = path +File.separator+ dir;
		//path=path+"mvcs\\CMISMvc\\ind\\jspfiles\\";
		
		
		try {
			fileName = path + File.separator + fileName;
			
			file = new File(fileName);
//			if( file.length() > MAXLEN ){
//				throw new CMISException("文件超长，请用CSV格式导出！");
//			}
			reader = new BufferedInputStream(new FileInputStream(file));
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=default.xls");
		    
			outPut = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			
			/*创建sheet*/
			HSSFSheet sheet = wb.createSheet("AnalyseData");
          
			while( (str=readLine(reader)) != null ){
				str =new String(str.getBytes(),IN_ENCODE).trim();

				/**如果数据中包含链接，将连接后面的数据去除         2014-06-11  唐顺岩 */
				if(str.indexOf("&@&")>0){
					str = str.substring(0,str.indexOf("&@&")) + str.substring(str.indexOf("|"),str.length());
				}
				/** END */
				
				String[] split = str.split("\\|");
				
				if( split != null && split.length != 0 ){
					/*创建行*/
					HSSFRow row = sheet.createRow((short)(row_idx++));
					
					for( int i=0; i<split.length; i++ ){
						/*创建元素并设置为字符型*/
						HSSFCell cell = row.createCell((short)i);
						if(split[i].indexOf("-num")>0){
							split[i]=split[i].replaceAll("-num","");
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						if(split[i].indexOf(".")>0)
						cell.setCellValue(Double.parseDouble(split[i]));
						else cell.setCellValue(Integer.parseInt(split[i]));
						
						}
					else{
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(split[i]);
				}
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
				if( reader != null ) reader.close();
			} catch (Exception e) {}
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

package com.yucheng.cmis.pub.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.yucheng.cmis.pub.util.CellVO;
import com.yucheng.cmis.pub.util.ExcelVO;
import com.yucheng.cmis.pub.util.SheetVO;

/**
 * <p>
 * Title:Excel文件的处理类
 * </p>
 * <p>
 * Copyright:yucheng Copyright (c) 2008
 * </p>
 * <p>
 * Company: yuchengtech
 * </p>
 * 
 * @author ljy
 * @version 1.0
 */
public class ExcelTreat {

	/**
	 * 读取Excel文件，生成ExcelVO对象
	 * 
	 * @param in
	 * @return ExcelVO
	 * @throws java.lang.Exception
	 */
	public static ExcelVO readExcel(InputStream in) throws Exception {
		ExcelVO evo = new ExcelVO();
		CellVO cvo;
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = null;
		// System.out.println("Sheet工作表数："+wb.getNumberOfSheets());
		evo.sheetnum = wb.getNumberOfSheets();
		evo.sheets = new SheetVO[evo.sheetnum];
		HSSFRow row;
		HSSFCell cell;
		String msg;
		int celltype;
		/**
		 * public static final int CELL_TYPE_NUMERIC = 0; public static final
		 * int CELL_TYPE_STRING = 1; public static final int CELL_TYPE_FORMULA =
		 * 2; public static final int CELL_TYPE_BLANK = 3; public static final
		 * int CELL_TYPE_BOOLEAN = 4; public static final int CELL_TYPE_ERROR =
		 * 5;
		 */
		for (int n = 0; n < evo.sheetnum; n++) {
			sheet = wb.getSheetAt(n);// 取得工作表（从0开始编号）
			if (sheet == null){
				continue;
			}
			evo.sheets[n] = new SheetVO();
			evo.sheets[n].sheetname = wb.getSheetName(n);
			// System.out.println("行数："+sheet.getLastRowNum());
			if (sheet.getLastRowNum() < 1){
				continue;
			}
			
			evo.sheets[n].rownum = sheet.getPhysicalNumberOfRows();// 取得行数（注意从0开始编号，因此比实际的Excel中的编号要小1）
			evo.sheets[n].colnum = sheet.getRow(0).getLastCellNum() + 1;
			evo.sheets[n].cells = new CellVO[evo.sheets[n].rownum][evo.sheets[n].colnum];
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i); // 取得行
				for (int j = 0; j <= row.getLastCellNum(); j++) {
					cvo = new CellVO();
					cvo.cellrownum = i;// 单元格所在的行，从0开始
					cvo.cellcolnum = j;// 单元格所在的列，从0开始
					cell = row.getCell((short) j);
					if (cell != null) {
						celltype = cell.getCellType();
						switch (celltype) {
						case HSSFCell.CELL_TYPE_NUMERIC:
							msg = String.valueOf(cell.getNumericCellValue());
							break;
						case HSSFCell.CELL_TYPE_STRING:
							msg = cell.getStringCellValue();
							
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							//msg = cell.getCellFormula();
							try{
							   msg = String.valueOf(cell.getNumericCellValue());
							}catch(Exception ex){
								msg = "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							msg = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							msg = String.valueOf(cell.getBooleanCellValue());
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							msg = String.valueOf(cell.getErrorCellValue());
							break;
						default:
							msg = "无法识别的单元格格式";
							break;
						}
						cvo.cellbgcolor = cell.getCellStyle()
								.getFillBackgroundColor();// 单元格背景色
					} else {
						msg = "";
						celltype = HSSFCell.CELL_TYPE_ERROR;
					}
					cvo.celltype = celltype;
					cvo.cellvalue = msg;
					evo.sheets[n].cells[i][j] = cvo;
				}
			}
		}
		return evo;
	}

	/**
	 * 读取Excel文件，生成ExcelVO对象
	 * 
	 * @param urlpath
	 *            ,Excel文件的路径
	 * @return ExcelVO
	 * @throws java.lang.Exception
	 */
	public static ExcelVO readExcel(String urlpath) throws Exception {
		InputStream in = new FileInputStream(urlpath);
		ExcelVO evo = readExcel(in);
		evo.urlpath = urlpath;
		in.close();
		return evo;
	}

	/**
	 * 写Excel文件
	 * 
	 * @param evo
	 *            evo,要写入文件的ExcelVO对象
	 * @param out
	 *            out,要写入的输出流
	 * @return boolean
	 * @throws java.lang.Exception
	 */
	public static boolean writeExcel(ExcelVO evo, OutputStream out)
			throws Exception {
		if (evo == null || evo.sheetnum < 1){
			return false;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet;
		HSSFCell cell;
		HSSFRow row;
		CellVO cvo;

        HSSFCellStyle cellStyle = wb.createCellStyle();
        
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		
		int celltype;
		for (int n = 0; n < evo.sheetnum; n++) {
			sheet = wb.createSheet();
			/*wb.setSheetName(n, evo.sheets[n].sheetname,
					(short) HSSFCell.CELL_TYPE_STRING);*/
			
			wb.setSheetName(n, evo.sheets[n].sheetname);
			
			for (int c = 0; c < evo.sheets[n].colnum; c++){
			   int width =  evo.sheets[n].cells[3][c].cellwidth;
			   if(width <= 0){
				   width = 4000;
			   }
		 	   sheet.setColumnWidth(c, width);
			}
			
			for (int i = 0; i < evo.sheets[n].rownum; i++) {
				row = sheet.createRow(i);				
				for (int j = 0; j < evo.sheets[n].colnum; j++) {
					cvo = evo.sheets[n].cells[i][j];
					celltype = cvo.celltype;
					//cell = row.createCell((short) j);
					cell = row.createCell(j);
					
					
					//如果有设置颜色值，则进行填充
					if(cvo.cellbgcolor > 0) {
						
						HSSFCellStyle specStyle = wb.createCellStyle();					        
						specStyle.setBottomBorderColor(HSSFColor.BLACK.index);
						specStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);						
						specStyle.setFillForegroundColor(cvo.cellbgcolor);
						specStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						
						cell.setCellStyle(specStyle);
							
					} else {
	
						cell.setCellStyle(cellStyle);
					}
					

					
					//cell.setCellType(celltype);
					//cell.setEncoding((short) celltype);// 设置单元格编码格式
					switch (celltype) {
					case HSSFCell.CELL_TYPE_NUMERIC:
						
						HSSFCellStyle specStyle = wb.createCellStyle();
						
						specStyle.setBottomBorderColor(HSSFColor.BLACK.index);
						specStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						specStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);						
						specStyle.setFillForegroundColor(HSSFColor.WHITE.index);
						specStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						HSSFDataFormat format = wb.createDataFormat();
					    specStyle.setDataFormat(format.getFormat("#,##0.00"));							
						
				 
						cell.setCellStyle(specStyle);
						cell.setCellType(celltype);
						cell.setCellValue(Double
								.parseDouble((String) cvo.cellvalue));
						
						//System.err.println(">>>>" + cell.getColumnIndex() + "  " + cell.getRowIndex());
						break;
						case 6://百分数显示
						
						HSSFCellStyle specStyle6 = wb.createCellStyle();
						
						specStyle6.setBottomBorderColor(HSSFColor.BLACK.index);
						specStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						specStyle6.setBorderTop(HSSFCellStyle.BORDER_THIN);
						specStyle6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						specStyle6.setBorderRight(HSSFCellStyle.BORDER_THIN);						
						specStyle6.setFillForegroundColor(HSSFColor.WHITE.index);
						specStyle6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						HSSFDataFormat format6 = wb.createDataFormat();
						specStyle6.setDataFormat(format6.getFormat("0.00%"));							
						
				 
						cell.setCellStyle(specStyle6);
						cell.setCellType(celltype);
						cell.setCellValue(Double
								.parseDouble((String) cvo.cellvalue));
						
						//System.err.println(">>>>" + cell.getColumnIndex() + "  " + cell.getRowIndex());
						break;
					case HSSFCell.CELL_TYPE_STRING:
						
						if(cvo.cellvalue != null && ((String) cvo.cellvalue).matches("-?\\d+\\.?\\d*")) {
																					
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							cell.setCellValue(Double.parseDouble((String) cvo.cellvalue));
						}else {
							if(cvo.cellvalue != null && ((String)cvo.cellvalue).equals(".00") ){
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(0.0);
							} else {
								cell.setCellType(celltype);
								cell.setCellValue((String) cvo.cellvalue);
							}
						}
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						//公式使用高亮样式
						
						HSSFCellStyle specStyle2 = wb.createCellStyle();
						specStyle2.setLocked(true);//不知为何没生效，先留在这里
						
						specStyle2.setBottomBorderColor(HSSFColor.BLACK.index);
						specStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						specStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
						specStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						specStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);						
						specStyle2.setFillForegroundColor(HSSFColor.YELLOW.index);
						specStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						HSSFDataFormat format3 = wb.createDataFormat();
					    specStyle2.setDataFormat(format3.getFormat("#,##0.00"));					
						cell.setCellStyle(specStyle2);
						
						cell.setCellType(celltype);
						cell.setCellFormula((String) cvo.cellvalue);
						break;
					case HSSFCell.CELL_TYPE_BLANK:
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						cell.setCellType(celltype);
						cell.setCellValue(Boolean
								.getBoolean((String) cvo.cellvalue));
						break;
					case HSSFCell.CELL_TYPE_ERROR:
						try {
							cell.setCellType(celltype);
							cell.setCellErrorValue(Byte
									.parseByte((String) cvo.cellvalue));
						} catch (Exception e) {
							cell.setCellType(HSSFCell.CELL_TYPE_BLANK);// 设置单元格为空
							//cell.setEncoding((short) HSSFCell.CELL_TYPE_BLANK);
						}
						break;
					default:
						cell.setCellType(celltype);
						cell.setCellValue((String) cvo.cellvalue);
						break;
					}
				}
			}
		}
		wb.write(out);
		return true;
	}

	/**
	 * 写Excel文件
	 * 
	 * @param evo
	 *            evo,要写入文件的ExcelVO对象
	 * @param urlpath
	 *            urlpath,要写入的Excel文件路径
	 * @return boolean
	 * @throws java.lang.Exception
	 */
	public static boolean writeExcel(ExcelVO evo, String urlpath)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(urlpath);
		boolean b = writeExcel(evo, fos);
		fos.close();
		return b;
	}
	
	/**
	 * 读取Excel文件，生成ExcelVO对象
	 * 在原基础上加入了兼容空列处理，为了不影响其他调用另写方法。GC 20131218
	 * @param in
	 * @return ExcelVO
	 * @throws java.lang.Exception
	 */
	public static ExcelVO readExcelPlus(InputStream in) throws Exception {
		ExcelVO evo = new ExcelVO();
		CellVO cvo;
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = null;
		evo.sheetnum = wb.getNumberOfSheets();
		evo.sheets = new SheetVO[evo.sheetnum];
		HSSFRow row;
		HSSFCell cell;
		String msg;
		int celltype;
		int nullNum,nullRow; //兼容空列处理参数

		for (int n = 0; n < evo.sheetnum; n++) {
			nullNum = 0;
			nullRow = 0;
			sheet = wb.getSheetAt(n);// 取得工作表（从0开始编号）
			if (sheet == null){
				continue;
			}
			evo.sheets[n] = new SheetVO();
			evo.sheets[n].sheetname = wb.getSheetName(n);
			if (sheet.getLastRowNum() < 1){
				continue;
			}
			
			evo.sheets[n].rownum = sheet.getPhysicalNumberOfRows();// 取得行数（注意从0开始编号，因此比实际的Excel中的编号要小1）
			while( sheet.getRow(nullRow) == null)nullRow++;	//
			evo.sheets[n].colnum = sheet.getRow(nullRow).getLastCellNum() + 1;
			evo.sheets[n].cells = new CellVO[evo.sheets[n].rownum][evo.sheets[n].colnum];
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i); // 取得行
				if(row != null){
					for (int j = 0; j <= row.getLastCellNum(); j++) {
						cvo = new CellVO();
						cvo.cellrownum = i;// 单元格所在的行，从0开始
						cvo.cellcolnum = j;// 单元格所在的列，从0开始
						cell = row.getCell((short) j);
						if (cell != null) {
							celltype = cell.getCellType();
							switch (celltype) {
							case HSSFCell.CELL_TYPE_NUMERIC:
								msg = String.valueOf(cell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_STRING:
								msg = cell.getStringCellValue();
								
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								//msg = cell.getCellFormula();
								try{
								   msg = String.valueOf(cell.getNumericCellValue());
								}catch(Exception ex){
									msg = "";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								msg = "";
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:
								msg = String.valueOf(cell.getBooleanCellValue());
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								msg = String.valueOf(cell.getErrorCellValue());
								break;
							default:
								msg = "无法识别的单元格格式";
								break;
							}
							cvo.cellbgcolor = cell.getCellStyle()
									.getFillBackgroundColor();// 单元格背景色
						} else {
							msg = "";
							celltype = HSSFCell.CELL_TYPE_ERROR;
						}
						cvo.celltype = celltype;
						cvo.cellvalue = msg;
						if(i-nullNum >= 0)evo.sheets[n].cells[i-nullNum][j] = cvo;
					}
				}else {
					nullNum++;
				}				
			}
		}
		return evo;
	}
	
	/**
	 * 读取Excel文件，生成ExcelVO对象
	 * 在原基础上加入了兼容空列处理，为了不影响其他调用另写方法。GC 20131218
	 * @param urlpath *,Excel文件的路径
	 * @return ExcelVO
	 * @throws java.lang.Exception
	 */
	public static ExcelVO readExcelPlus(String urlpath) throws Exception {
		InputStream in = new FileInputStream(urlpath);
		ExcelVO evo = readExcelPlus(in);
		evo.urlpath = urlpath;
		in.close();
		return evo;
	}
}

/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.yucheng.cmis.pub.util;
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelRead {
	public static ExcelVO readExcel(InputStream in) throws Exception {
		ExcelVO evo = new ExcelVO();

		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = null;

		evo.sheetnum = wb.getNumberOfSheets();

		evo.sheets = new SheetVO[1];

		int rowNum = 0;
		for (int n = 0; n < evo.sheetnum; ++n) {
			sheet = wb.getSheetAt(n);
			if (sheet == null) {
				continue;
			}
			evo.sheets[n] = new SheetVO();
			evo.sheets[n].sheetname = wb.getSheetName(n);

			if (sheet.getLastRowNum() < 1) {
				continue;
			}
			rowNum = sheet.getPhysicalNumberOfRows();
			evo.sheets[n].rownum = rowNum;
			evo.sheets[n].colnum = sheet.getRow(0).getLastCellNum();
			evo.sheets[n].cells = new CellVO[evo.sheets[n].rownum][evo.sheets[n].colnum];
			if (rowNum > 5000) {
				throw new  Exception("暂时只能读取包含表头在内的5000行数据，请分多次上传！");
			}
			for (int i = 0; i <= sheet.getLastRowNum(); ++i) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					for (int j = 0; j < row.getLastCellNum(); ++j) {
						CellVO cvo = new CellVO();
						cvo.cellrownum = i;
						cvo.cellcolnum = j;
						HSSFCell cell = row.getCell(j);
						String msg;
						int celltype = 0;
						if (cell != null) {
							int celltype1 = cell.getCellType(); 
							switch (celltype1) {
							case 0:
								DecimalFormat df = new DecimalFormat("0");
								msg = df.format(cell.getNumericCellValue());
								break;
							case 1:
								msg = cell.getStringCellValue();
								break;
							case 2:
								String msg8;
								try {
									msg = String.valueOf(cell
											.getNumericCellValue());
								} catch (Exception ex) {
									msg = "";
								}
								break;
							case 3:
								msg = "";
								break;
							case 4:
								msg = String
										.valueOf(cell.getBooleanCellValue());
								break;
							case 5:
								msg = String.valueOf(cell.getErrorCellValue());
								break;
							default:
								msg = "无法识别的单元格格式ʽ";
							}

							cvo.cellbgcolor = cell.getCellStyle()
									.getFillBackgroundColor();
						} else {
							msg = "";
							celltype = 5;
						}
						cvo.celltype = celltype;
						cvo.cellvalue = msg;
						evo.sheets[n].cells[i][j] = cvo;
					}
				}
			}
			break;
		}
		return evo;
	}

	public static ExcelVO readExcel(String urlpath) throws Exception {
		InputStream in = new FileInputStream(urlpath);
		ExcelVO evo = readExcel(in);
		evo.urlpath = urlpath;
		in.close();
		return evo;
	}

	public static boolean writeExcel(ExcelVO evo, OutputStream out)
			throws Exception {
		if ((evo == null) || (evo.sheetnum < 1))
			return false;
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFCellStyle cellStyle = wb.createCellStyle();

		cellStyle.setBottomBorderColor((short) 8);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setFillForegroundColor((short) 9);
		cellStyle.setFillPattern((short) 1);

		for (int n = 0; n < evo.sheetnum; ++n) {
			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(n, evo.sheets[n].sheetname);

			for (int c = 0; c < evo.sheets[n].colnum; ++c) {
				int _width = evo.sheets[n].cells[3][c].cellwidth;
				if (_width <= 0) {
					_width = 4000;
				}
				sheet.setColumnWidth(c, _width);
			}

			for (int i = 0; i < evo.sheets[n].rownum; ++i) {
				HSSFRow row = sheet.createRow(i);
				for (int j = 0; j < evo.sheets[n].colnum; ++j) {
					CellVO cvo = evo.sheets[n].cells[i][j];
					int celltype = cvo.celltype;

					HSSFCell cell = row.createCell(j);

					if (cvo.cellbgcolor > 0) {
						HSSFCellStyle specStyle = wb.createCellStyle();
						specStyle.setBottomBorderColor((short) 8);
						specStyle.setBorderBottom((short) 1);
						specStyle.setBorderTop((short) 1);
						specStyle.setBorderLeft((short) 1);
						specStyle.setBorderRight((short) 1);
						specStyle.setFillForegroundColor(cvo.cellbgcolor);
						specStyle.setFillPattern((short) 1);

						cell.setCellStyle(specStyle);
					} else {
						cell.setCellStyle(cellStyle);
					}

					switch (celltype) {
					case 0:
						HSSFCellStyle specStyle = wb.createCellStyle();

						specStyle.setBottomBorderColor((short) 8);
						specStyle.setBorderBottom((short) 1);
						specStyle.setBorderTop((short) 1);
						specStyle.setBorderLeft((short) 1);
						specStyle.setBorderRight((short) 1);
						specStyle.setFillForegroundColor((short) 9);
						specStyle.setFillPattern((short) 1);
						HSSFDataFormat format = wb.createDataFormat();
						specStyle.setDataFormat(format.getFormat("#,##0.000"));

						cell.setCellStyle(specStyle);
						cell.setCellType(celltype);
						cell.setCellValue(Double
								.parseDouble((String) cvo.cellvalue));

						break;
					case 1:
						if ((cvo.cellvalue != null)
								&& (((String) cvo.cellvalue)
										.matches("-?\\d+\\.?\\d*"))) {
							cell.setCellType(0);
							cell.setCellValue(Double
									.parseDouble((String) cvo.cellvalue));
						} else if ((cvo.cellvalue != null)
								&& (((String) cvo.cellvalue).equals(".00"))) {
							cell.setCellType(0);
							cell.setCellValue(0.0D);
						} else {
							cell.setCellType(celltype);
							cell.setCellValue((String) cvo.cellvalue);
						}

						break;
					case 2:
						HSSFCellStyle specStyle2 = wb.createCellStyle();
						specStyle2.setLocked(true);

						specStyle2.setBottomBorderColor((short) 8);
						specStyle2.setBorderBottom((short) 1);
						specStyle2.setBorderTop((short) 1);
						specStyle2.setBorderLeft((short) 1);
						specStyle2.setBorderRight((short) 1);
						specStyle2.setFillForegroundColor((short) 13);
						specStyle2.setFillPattern((short) 1);
						HSSFDataFormat format3 = wb.createDataFormat();
						specStyle2
								.setDataFormat(format3.getFormat("#,##0.000"));
						cell.setCellStyle(specStyle2);

						cell.setCellType(celltype);
						cell.setCellFormula((String) cvo.cellvalue);
						break;
					case 3:
						break;
					case 4:
						cell.setCellType(celltype);
						cell.setCellValue(Boolean
								.getBoolean((String) cvo.cellvalue));
						break;
					case 5:
						try {
							cell.setCellType(celltype);
							cell.setCellErrorValue(Byte
									.parseByte((String) cvo.cellvalue));
						} catch (Exception e) {
							cell.setCellType(3);
						}

						break;
					default:
						cell.setCellType(celltype);
						cell.setCellValue((String) cvo.cellvalue);
					}
				}
			}
		}

		wb.write(out);
		return true;
	}

	public static boolean writeExcel(ExcelVO evo, String urlpath)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(urlpath);
		boolean b = writeExcel(evo, fos);
		fos.close();
		return b;
	}
}
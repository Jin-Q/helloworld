package com.yucheng.cmis.pub.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.service.EMPService;
import com.yucheng.cmis.pub.DicUtil;
import com.yucheng.cmis.pub.XmlForExcelUtil;

public class ExportUtil extends EMPService {
	private Connection conn = null;
	
	public ExportUtil(Connection conn){
		this.conn = conn;
	}
	
	public File exportData(String sql,Map<String, String> tableFields, Map moneyFields,Map<String,String> dicField,Context context) throws EMPException, SQLException, IOException {
		if (tableFields == null){
			return null;
		}
		
		if (moneyFields == null){
			moneyFields = new HashMap<String, String>();
		}
		
		if (dicField == null){
			dicField = new HashMap<String, String>();
		} 		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fieldName = "";
		File tmpFile = null;		
		String val = "";
		ResultSetMetaData rsmd = null;
		FileOutputStream fos = null;
		int colcount = 0;
		int reccount = 0;
		XmlForExcelUtil xml = new XmlForExcelUtil();
		ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		tmpFile = File.createTempFile("temp", ".xml");
		fos = new FileOutputStream(tmpFile);
		
		rsmd = rs.getMetaData();
		 /** 按照tableFields列数显示，tableFields里未添加的，隐藏不显示
		 * begin*/
//		colcount = rsmd.getColumnCount();
		colcount=tableFields.size();

		this.write(fos, xml.beginExcel());
		this.setStyle(fos, xml);
		this.write(fos, xml.beginWorksheet("100", colcount - 1));			
		
		this.write(fos, xml.beginRow());
		for (int i = 1; i <= colcount; i++) {
			fieldName = rsmd.getColumnName(i).toLowerCase();
			this.write(fos, xml.writeHead(tableFields.get(fieldName)));
		}
		this.write(fos, xml.endRow());
		if(null != rs){
			while(rs.next()){
				reccount++;				
				if (reccount >65535){
					break;
				}
				this.write(fos, xml.beginRow());
				for(int i=1; i<= colcount; i++){
					fieldName = rsmd.getColumnName(i).toLowerCase();
					val = trim(rs.getObject(fieldName));
					if (moneyFields.containsKey(fieldName)){
						this.write(fos, xml.writeNumValue(val));
					} else if (dicField.containsKey(fieldName)){
						val = DicUtil.getCnnameByEnname(val, dicField.get(fieldName), context);
						this.write(fos, xml.writeStrValue(val));
					} else {
						this.write(fos, xml.writeStrValue(val));
					}
				}
				this.write(fos, xml.endRow());
				
			}
			
			this.write(fos, xml.endWorksheet());
			this.write(fos, xml.endExcel());
			fos.close();
		}
		return tmpFile;
	}

	private void setStyle(FileOutputStream fos, XmlForExcelUtil xml) throws IOException{
		this.write(fos, xml.beginStyles());
		this.write(fos, xml.setStyle_Default());
		this.write(fos, xml.setStyle_head());
		this.write(fos, xml.setStyle_Number());
		this.write(fos, xml.setStyle_String());
		this.write(fos, xml.endStyles());
	}
	
	
	private void write(FileOutputStream fos, String value) throws IOException{
		fos.write(value.getBytes("UTF-8"));
	}
	
		
	private String trim(Object str){
		return (str == null)?"":str.toString().trim();
	}

	public static String getExportName(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.ENGLISH);
		return sdf.format(new Date()) + ".xls";
	}
}
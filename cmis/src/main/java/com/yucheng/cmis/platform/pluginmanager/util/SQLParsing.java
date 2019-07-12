package com.yucheng.cmis.platform.pluginmanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.yucheng.cmis.platform.pluginmanager.PluginManagerConstance;

/**
 * SQL文件解析类
 * 
 * <p>
 * 	 更新记录：
 * 	 	<ul>SQL文件支持注释,以“--”开头的行，将被忽略。2013-7-17</ul>
 * </p>
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
public class SQLParsing {

	
	
	/**
	 * 	解析SQL
	 * <p>
	 * 	<ul>从文件中解析SQL，每条SQL以“;”结束，一条SQL可以分多行</ul>
	 * 	<ul>支持注释，如果以--开头，则忽略该行</ul>
	 *  <ul>默认的字符集是PluginManagerConstance.PLUGIN_CHARSET</ul>
	 * </p>
	 * 
	 * @param fileName　文件名（含路径）
	 * @return　SQL的集合
	 * @throws Exception　异常
	 */
	public static List<String> sqlParsingFromFile(String fileName) throws Exception{
		return sqlParsingFromFile(fileName, PluginManagerConstance.PLUGIN_CHARSET);
	}
	

	/**
	 * 	解析SQL
	 * <p>
	 * 	<ul>从文件中解析SQL，每条SQL以“;”结束，一条SQL可以分多行</ul>
	 * 	<ul>支持注释，如果以--开头，则忽略该行</ul>
	 * </p>
	 * 
	 * @param fileName　文件名（含路径）
	 * @param encode 字符编码
	 * @return　SQL的集合
	 * @throws Exception　异常
	 */
	public static List<String> sqlParsingFromFile(String fileName, String encode) throws Exception{
		List<String> retList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			File file =  new File(fileName);
			if(!file.isFile())
				return retList;
			
			String sqlLine = "";
			String line = "";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
			while((line=reader.readLine())!=null){
				//如果以--开头，则忽略该行
				if(line.trim().startsWith("--")) continue;
				
				sqlLine += line;
				//以;计算是否属于同一条ＳＱＬ
				if(line.endsWith(";")){
					retList.add(sqlLine.substring(0, sqlLine.length()-1));
					sqlLine = "";
				}
			}
			
		} catch (Exception e) {
			throw new Exception("解析ＳＱＬ文件出错! fileName="+fileName,e);
		} finally{
			if(reader!=null)
				reader.close();
			
		}
		
		return retList;
	}
}

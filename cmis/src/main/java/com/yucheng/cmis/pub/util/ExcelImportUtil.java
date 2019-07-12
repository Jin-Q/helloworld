package com.yucheng.cmis.pub.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.base.CMISConstance;

public class ExcelImportUtil {
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * <ol>
	 * 获取文件路径及名字方法； 后缀以具体时间命名txt文件格式为：YYMMDDHHmmss.txt
	 * </ol>
	 * <h2>功能描述</h2>
	 * <ol>
	 * 请添加功能详细的描述
	 * </ol>
	 * </p>
	 * 
	 * @param context EMP上下文
	 * @param type 文件类型
	 * @return
	 * @throws Exception
	 */
	public static String getFileNamePath(String type, Context context)throws Exception {
		String str = type + "ErrorData";
		String excelErrorFile = "";

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String time = sdf.format(timestamp);

		try {
			String strDay = (String) context.getDataValue(CMISConstance.OPENDAY);
			str += strDay.substring(0, 4) + strDay.substring(5, 7)+ strDay.substring(8, 10);
			str = str + time + ".txt";
			DownLoadForExportLog.map.put((String) context.getDataValue("currentUserId"), str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		ResourceBundle res = ResourceBundle.getBundle("cmis");
		if (res.containsKey("excelErrorFile")) {
			excelErrorFile = res.getString("excelErrorFile");
		}

		return excelErrorFile + str;
	}
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * <ol>
	 * 文件记录无法导入数据的信息
	 * </ol>
	 * <h2>功能描述</h2>
	 * <ol>
	 * 请添加功能详细的描述
	 * </ol>
	 * </p>
	 * 
	 * @param list 无法导入数据信息集合
	 * @param fileName 记录信息生成的文件名
	 * @throws GoverfinterException
	 */
	public static void fileUtil(List<String> list, String fileName)throws Exception {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		// 设置文本内容
		StringBuilder sb = new StringBuilder("");
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				sb.append(i + 1 + "、" + list.get(i) + "。\r\n");
			}
		}else{
			sb.append("此次数据全部导入成功!");

			
		}
		String a = sb.toString();
		
		try {
			bw.write(a);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * <ol>
	 * 对导入数据结果进行判断
	 * </ol>
	 * <h2>功能描述</h2>
	 * <ol>
	 * 请添加功能详细的描述
	 * </ol>
	 * </p>
	 * 
	 * @param count 导入成功的条数
	 * @param list 无法导入信息的记录集合
	 * @return 提示信息
	 */
	public static String checkImportResult(int count, List<String> list) {
		String message = null;
		if (count != 0) {
			if (list.size() != 0) {
				message = "共有" + count + "条数据成功导入，" + list.size() + "条数据导入失败！";
			} else {
				message = "全部数据导入成功";
			}
		}else{
			message = "无成功条数！;";
			for(int i = 0;i<list.size();i++){
				message += list.get(i)+";";
			}
		}
		return message;
	}
}

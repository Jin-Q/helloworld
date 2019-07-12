package com.yucheng.cmis.pub.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.web.servlet.ModelAndView;
import com.ecc.emp.web.servlet.mvc.EMPRequestController;

/**
 * 
 * <p>
 * <h2>简述</h2>
 * 		<ol>下载Excel导入日志信息类</ol>
 * <h2>功能描述</h2>
 * 		<ol>无</ol>
 * <h2>修改历史</h2>
 *    <ol>无</ol>
 * </p>
 * @author xiaoxx
 * @2016-6-29 10:38:45
 * @version 1.0
 */
public class DownLoadForExportLog extends EMPRequestController {
	
	/** 声明一个map集合用来存储导入时信息记录的文件名，以当前操作用户名为key;文件名为value **/
	public static Map<String,String> map = new HashMap<String,String>();	
	
	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>上传下载操作请求处理</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * <h2>修改历史</h2>
	 *    <ol>无</ol>
	 * </p>
	 * @version 1.0
	 */
	@Override
	public ModelAndView doRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获取要下载的文件的ID
		String currentUserId = request.getParameter("currentUserId");
		String fileName = map.get(currentUserId);
		String filePath = null;
		ResourceBundle res = ResourceBundle.getBundle("cmis");
		if (res.containsKey("excelErrorFile")) {
			filePath = res.getString("excelErrorFile");
		}	
		KeyedCollection fileUploadInfo = new KeyedCollection();	
		if(filePath==null||"".endsWith(filePath)){
			String path = request.getSession().getServletContext().getRealPath("/")+"/WEB-INF/classes";
			path = path.substring(0,path.indexOf("classes"));
			fileUploadInfo.put("file_path",(path+"classes/"));
		}else{
			fileUploadInfo.put("file_path",filePath);
		}
		fileUploadInfo.put("file_name",fileName);
		downloadSingleFile(response, fileUploadInfo);			
		return null;
	}

	/**
	 * 
	 * <p>
	 * <h2>简述</h2>
	 * 		<ol>单个文件下载，不打包成zip压缩文件，直接下载</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * <h2>修改历史</h2>
	 *    <ol>无</ol>
	 * </p>
	 * @param response 服务器响应对象
	 * @param fileUploadInfo 文件信息对象
	 * @version 1.0
	 * @throws InvalidArgumentException 
	 * @throws ObjectNotFoundException 
	 */
	public void downloadSingleFile(HttpServletResponse response,
			KeyedCollection fileUploadInfo) throws Exception  {
		// 单文件下载，直接下载当前的文件
		String filePath = (String) fileUploadInfo.getDataValue("file_path");
		String fileName = (String) fileUploadInfo.getDataValue("file_name");
		try {
			if (fileName != null && filePath != null) {
				OutputStream out = response.getOutputStream();
				File file = new File(filePath+fileName);
				
				response.setContentType("application/x-download");
				response.setContentLength((int)file.length());
				response.addHeader(
						"Content-Disposition",
						"attachment;filename="
								+ java.net.URLEncoder.encode(file.getName(), "UTF-8"));
				BufferedInputStream buff = new BufferedInputStream(
						new FileInputStream(file));
				byte[] b = new byte[1024];
				long k = 0;
				// 该值用于计算当前实际下载了多少字节
				// 开始循环下载
				while (k < file.length()) {
					int j = buff.read(b, 0, 1024);
					k += j;
					// 将写入到客户端的内存的数据,刷新到磁盘
					out.write(b, 0, j);
					out.flush();
				}
				buff.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

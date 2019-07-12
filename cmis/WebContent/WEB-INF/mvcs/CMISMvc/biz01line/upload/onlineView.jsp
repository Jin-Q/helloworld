<%@page language="java" contentType="application/octet-stream" import="java.io.*,java.net.*,com.ecc.emp.core.*" pageEncoding="gb2312"%>
<%
	//从emp常量中取值。
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<%
	//20190418 chenBQ 修复weblogic无法在线显示图片
    response.reset();
	response.setContentType("text/html; charset=UTF-8");
	response.setContentType("image/jpeg");
	String filePath = null;
	try {
		filePath = (String)context.getDataValue("file_path").toString().trim();
	} catch (Exception e) {}
	FileInputStream fis = new FileInputStream(filePath);
	OutputStream os = response.getOutputStream();
	try {
	    int count = 0;
	    byte[] buffer = new byte[1024 * 1024];
	    while ((count = fis.read(buffer)) != -1)
	        os.write(buffer, 0, count);
	    os.flush();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (os != null)
	        os.close();
	  		//20190418 chenBQ 修复weblogic无法在线显示图片
	    	//out.clear();
	    	//out = pageContext.pushBody();
	    if (fis != null)
	        fis.close();
	  		//20190418 chenBQ 修复weblogic无法在线显示图片
	    	//out.clear();
	    	//out = pageContext.pushBody();
	}
%>


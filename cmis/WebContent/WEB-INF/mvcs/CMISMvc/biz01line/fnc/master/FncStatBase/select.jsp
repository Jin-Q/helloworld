<%@page language="java" contentType="application/x-download; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="java.util.*,java.io.*"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>   
<%
        Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
        String filePath = (String)context.get("filePath");
		response.reset();  //记住要记住reset浏览器清空缓存,否则可能会出现乱码情况
		
		/*ResourceBundle res = ResourceBundle.getBundle("cmis");
		String dir = res.getString("permission.file.path").replace("permissions", "");  
		String XLSFile = dir+"temp.xls";*/
		
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition","attachment;  filename="+System.currentTimeMillis()+".xls");
		
	
		
		
		//文件输入流
		FileInputStream fin = null;
		byte b[]=new byte[1024];
		OutputStream os = null;
		File file = new File(filePath);
		try{
			System.out.println(response.isCommitted());
			os = response.getOutputStream();
			fin=new FileInputStream(file);
			int n= -1;
			while((n=fin.read(b))!=-1){ 
				os.write(b,0,n);
			}
			os.flush();
			
			
			/**
			*下面两个是必须要的 否则 有问题
			*/
			//modify by jiangcuihua 2019-05-20  weblogic下不需要这两句，tomcat下需要
			//out.clear();
			//out = pageContext.pushBody();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fin != null){fin.close();}
			if(os != null){os.close();}
			file.delete();
		}
		
%>

	<html>
	<head>

	<title>下载页面</title>
	<body>
	</body>
	</html>

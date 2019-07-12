<%@page language="java" contentType="application/octet-stream" import="java.io.*,java.net.*" pageEncoding="gb2312"%><%
    response.reset();
    response.setContentType("application/octet-stream");//设置为字节流
    OutputStream output = null;
    InputStream input = null;
    String fname = "";
    try {
        input = (InputStream)request.getAttribute("inputStream");
        
        if(null == input){
        	throw new Exception("无excel");
        }    	

        fname = (String)request.getAttribute("filename");
        output = response.getOutputStream();        
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fname, "UTF-8"));

        byte[] b = new byte[4096];
        int i = 0;
        while((i = input.read(b)) > 0)
        {
            output.write(b, 0, i);
        }
        output.flush();
    }catch(Exception e) {
        
    }finally {
    	try{
	        if(input != null){
	            input.close();
	            input = null;
	        }if(output != null){
	            output.close();
	            output = null;
	        }
    	}catch(Exception e){
    		//不作任何处理
    	}
    }
%>
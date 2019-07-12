<%@page language="java" contentType="application/octet-stream" import="java.io.*,java.net.*" pageEncoding="gb2312"%>

<%
	response.resetBuffer();
    response.reset();
    response.setContentType("application/octet-stream");//设置为字节流
    OutputStream output = null;
    InputStream input = null;
    String fname = "";
    File tmpFile = null;
    try {
    	fname = (String)request.getAttribute("filename");
        input = (InputStream)request.getAttribute("inputStream");
        if(null == input){
        	throw new Exception();
        }    	

        tmpFile = (File)request.getAttribute("tmpFile");
        
        response.addHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(fname, "UTF-8"));
        output = response.getOutputStream();

        byte[] b = new byte[4096];
        int i = 0;
        while((i = input.read(b)) > 0)
        {
            output.write(b, 0, i);
        }
        output.flush();
        response.flushBuffer();
    }catch(Exception e) {
        
    }finally {
    	try{
	        if(input != null){
	            input.close();
	            input = null;
	        }if(output != null){
	            output.close();
	            output = null;
	            out.clear();
	            out = pageContext.pushBody();
	        }
	       
    	}catch(Exception e){
    		
    		//不作任何处理
    	}
    }
%>


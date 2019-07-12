<%@page language="java" contentType="application/octet-stream" import="java.io.*,java.net.*" pageEncoding="gb2312"%>

<%
	response.resetBuffer();
    response.reset();
    response.setContentType("application/octet-stream");//设置为字节流
    OutputStream output = null;
    InputStream input = null;
    String filepath = null;
    String fname = "";
    File tmpFile = null;
    try {
    	filepath = (String)request.getAttribute("filePath");
        //input = (InputStream)request.getAttribute("inputStream");
        input = new   FileInputStream(filepath);    
        if(null == input){
        	throw new Exception();
        }    	

        fname = (String)request.getAttribute("filename");
        tmpFile = (File)request.getAttribute("tmpFile");
        
        output = response.getOutputStream();       
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fname, "UTF-8"));

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
	        if(tmpFile != null){
	            tmpFile.delete();
	        }
    	}catch(Exception e){
    		
    		//不作任何处理
    	}
    }
%>



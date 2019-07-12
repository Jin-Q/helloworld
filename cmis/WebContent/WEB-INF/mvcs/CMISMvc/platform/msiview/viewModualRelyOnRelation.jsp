<%  
 request.setCharacterEncoding("UTF-8");  
 byte[] bytes = (byte[])request.getAttribute("xmlStr");
 String xmlStr = new String(bytes,"UTF-8");
 System.out.println(xmlStr);
 if(xmlStr != null && !"".equals(xmlStr)) {   
	  response.setContentType("text/xml;charset=UTF-8");
	  response.getWriter().write(xmlStr);  
	  response.flushBuffer();  
	  response.setContentType("application/octet-stream");  
	  response.addHeader("Content-Disposition","inline;filename=1.xml");  
	  
	  response.flushBuffer();
	  out.flush();
 }
%> 
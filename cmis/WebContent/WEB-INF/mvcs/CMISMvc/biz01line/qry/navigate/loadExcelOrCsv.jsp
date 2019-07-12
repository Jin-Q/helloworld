<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.yucheng.cmis.biz01line.qry.util.LoadExcelOrCsv"%>
<%@page import="com.yucheng.cmis.base.CMISException"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>导出Excel或者Csv</title>
<link href="<ctp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<link href="<ctp:file fileName='styles/default/tablelist.css'/>" rel="stylesheet" type="text/css" />
<link href="<ctp:file fileName='styles/default/dataField.css'/>" rel="stylesheet" type="text/css" />
<%
     LoadExcelOrCsv loader = null;
     try{
         String fileName = request.getParameter("fileName");
         String loadType = request.getParameter("loadType");
         loader = new LoadExcelOrCsv();
         if( "EXCEL".equals(loadType) ){
        	 try{
        	     loader.loadExcel(fileName, response);
        	 }catch(CMISException ex){%>
        	<script>
        	   alert("文件过大，请用CSV格式导出!");	
        	   window.close();
        	</script> 
           <%}
         }else if( "CSV".equals(loadType) ){
        	 loader.loadCsv(fileName, response);
         }
     }catch(Exception e){
     	e.printStackTrace();
     %>
    	 <script>
        	   alert("导出文件出错，请稍后再试!");	
         </script> 
   <%}finally{
       out.clear(); 
       out = pageContext.pushBody(); 
   %>
	     <script>
               window.close();
         </script>
   <%}
%>
</head>
<body>
</body>
</html>
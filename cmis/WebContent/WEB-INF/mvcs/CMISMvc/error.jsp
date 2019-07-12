<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%

	Exception exception = null;
	String errorMsg = "";
	String errStack = "";

	try
	{
		exception =(Exception)request.getAttribute("exception");
		errorMsg = exception.getMessage();
		java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
		java.io.PrintStream ps = new java.io.PrintStream(bo);
		exception.printStackTrace(ps);
		errStack = new String( bo.toByteArray() );
	}
	catch (Exception e)
	{
		System.out.println(e);
	}
%>

<HTML><HEAD>
<TITLE>EMP Error info</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<link href="/CMIS/styles/default/lianav3.css" rel="stylesheet" type="text/css" />
<SCRIPT language="javaScript">
	function showMsg(){
	 if(document.all.errorDetialMsg.style.display==''){
	 	document.all.errorDetialMsg.style.display='none';
	 	document.all.btn.innerHTML='>>>>>>>...';
	 }else{
	 	document.all.errorDetialMsg.style.display='';
	 	document.all.btn.innerHTML='<<<<<<<...';
	 }
	}
</SCRIPT>

</HEAD><BODY>

 <style type="text/css">
　　 <!--
　　 a:link { text-decoration: none;color: blue}
　　 a:active { text-decoration:blink}
　　 a:hover { text-decoration:underline;color: red} 
　　 a:visited { text-decoration: none;color: green}
　　 -－> 
　　 </style> 

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">

		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_hint">
		<span class="page_welcome_red">Internal error !</span><br>
		<div class="page_welcome_update" onload="scrollwindow()">
		
	
		<TABLE align="center" width="300px">

        <TR aligh="center">
          <TD><span class="page_welcome_red"> ErrorMessage：<%=errorMsg%></span> </TD>
        </TR>
        
        <TR> 
          <TD align="right">
          	<LABEL id="btn" onclick="javaScript:showMsg();">>>>>>>...</LABEL>
          </TD>
        </TR>
        <TR>
          <TD>
          	<LABEL id="errorDetialMsg" style="display: none"><%=errStack%></LABEL>
         </TD>
        </TR>
      </TABLE>

</div>
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</BODY>
</HTML>

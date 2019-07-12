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
	function doForwardLoginPage(){
		try{			
			if(typeof(parent.document)=='object'){
				parent.document.location.href='signOn.do';
			}
			window.event.returnValue = false;
		} catch(e){
			window.location.href = 'signOn.do';
		}
	}
</SCRIPT>

</HEAD><BODY>

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">

		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_hint">
		<span class="page_welcome_red"></span><br>
		<div class="page_welcome_update" onload="scrollwindow()">
		
	
		<TABLE align="center" width="300px">

        <TR aligh="center">
          <TD>
          	<%if(errorMsg.indexOf("dataSource is null") == -1){%>
          		<span class="page_welcome_red"> ErrorMessage：<%=errorMsg%></span>
          	<%} else { %>
			    <span onclick='doForwardLoginPage()' style='cursor:hand'>会话超时,点击转到登录页面</span>
          	<%} %>
           </TD>
        </TR>
        <%if(errorMsg.indexOf("dataSource is null") != -1){%>
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
        <%}%>
      </TABLE>

</div>
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</BODY>
</HTML>

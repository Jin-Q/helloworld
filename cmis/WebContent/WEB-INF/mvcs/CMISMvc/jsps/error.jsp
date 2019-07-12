<%@page language="java" contentType="text/html; charset=gb2312"%>
<%@page import="com.yucheng.cmis.base.CMISException"%>
<%@page import="com.yucheng.cmis.message.CMISMessageManager"%>
<%

	Exception exception = null;
	String errorMsg = "";
	String errStack = "";

	try
	{
		exception =(Exception)request.getAttribute("exception");
		errorMsg = exception.getMessage();   //错误信息展示 Exception 类的getMessage 信息  2013-07-16 唐顺岩
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
<link href="styles/default/lianav3.css" rel="stylesheet" type="text/css" />
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

</HEAD><BODY leftmargin="0">

<div class="page_welcome" style="padding-top:100px ">

		<TABLE align="center" width="500px">

		<TR>
			<TD>
			    <span onclick='javascript:history.go(-1)' style='cursor:hand'><a id="returnlink">返回上一页面</a></span>
          		<hr size="1">
			</TD>
		</TR>
        <TR aligh="center">        
          <TD><div class="page_welcome_red">
          <%	
			CMISException exception1 =(CMISException)request.getAttribute("exception");
			String display = CMISMessageManager.getMessage(exception1);
			%>
			<%= errorMsg%>   <!-- 错误信息展示 Exception 类的getMessage 信息  2013-07-16 唐顺岩-->
		</div> 
		</TD>
        </TR>
      </TABLE>

</div>
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</BODY>
</HTML>

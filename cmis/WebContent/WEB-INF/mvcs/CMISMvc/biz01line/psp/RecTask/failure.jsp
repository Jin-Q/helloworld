<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/CTP.tld" prefix="ctp" %>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<HTML><HEAD>
<TITLE>EMP Error info</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<link href="/CMIS/styles/default/lianav3.css" rel="stylesheet" type="text/css" />
</HEAD><BODY onload="onLoad()" >
<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">

		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_fail">
		<span class="page_welcome_red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作失败！</span><br>
		<TABLE align="center" width="300px">
        <TR aligh="center">
          <TD>
          		<span class="page_welcome_red"> 
          		<ctp:text dataName="message"></ctp:text>
          		</span>
          		</TD>
        </TR>
        <tr><td>&nbsp;</td></tr>
        <tr><td>&nbsp;</td></tr>
      </TABLE>
</div>
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</BODY>
</HTML>

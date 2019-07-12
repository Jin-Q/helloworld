<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/CTP.tld" prefix="ctp" %>
<HTML><HEAD>
<TITLE>EMP Error info</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<link href="/CMIS/styles/default/lianav3.css" rel="stylesheet" type="text/css" />
<script>
function onLoad(){
	var url = window.top.encodeURL("queryList__<ctp:text dataName="tableName" />.do");
	url+="&menuId=<ctp:text dataName="menuId" />";
	document.getElementById("returnlink").href = url;
	}
</script>
</HEAD><BODY onload="onLoad()" >
<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">

		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
		<span class="page_welcome_red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作已成功！</span><br>
		<TABLE align="center" width="300px">
        <TR aligh="center">
          <TD>		<span class="page_welcome_red"><font color="green"><a id="returnlink">回到列表页面</a></font></span></TD>
        </TR>
        <tr><td>&nbsp;</td></tr>
      </TABLE>


</div>
</BODY>
</HTML>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<emp:page>
<html>
<head>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String errorFlag = (String)context.getDataValue("errorInfo");
%>
<title>操作返回页面</title>
<script type="text/javascript">

	function doReturn(){			
		window.close();
		window.opener.location.reload();
	}

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content" >
<% 
				if(!"successInfo".equals(errorFlag)){
%>

<emp:table icollName="MsgList" pageMode="false" url="">
					<emp:text id="msg" label="错误信息" />
</emp:table>
<br><br><br><br><br><br><br>
<%	
				}else{
%>
				
<br><br><br><br><br><br><br>
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
				
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;导入成功！
			
				
				
			
			</span><br>
<%} %>
			<TABLE align="center" width="300px">
        		<TR align="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" onclick="doReturn()" href="">返回列表</a></font></span>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
	
	<script type="text/javascript">
try{window.parent.unmask();}catch(e){}

</script>
</body>
</html>
</emp:page>
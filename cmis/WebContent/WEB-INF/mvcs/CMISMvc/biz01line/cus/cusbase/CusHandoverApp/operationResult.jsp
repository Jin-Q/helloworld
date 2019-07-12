<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<%
String url=request.getParameter("url");
%>
<title>操作返回页面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		var paramStr ="CusHandoverApp.approve_status=${context.approve_status}";
		var action = '<%=url%>';
		var url = '<emp:url action="' + action + '"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
		//var link = document.getElementById("returnlink");
		//link.href = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</span><br>
			<TABLE align="center" width="300px">
        		<TR aligh="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" href=""></a></font></span>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
</body>
</html>

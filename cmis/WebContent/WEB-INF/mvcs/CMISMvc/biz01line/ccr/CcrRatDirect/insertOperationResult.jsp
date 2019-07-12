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
		var serno = '${context.serno}';
		var action = '<%=url%>';
		var url = '<emp:url action="' + action + '"/>';
		url = EMPTools.encodeURI(url+"?serno="+serno+"&op=update");
		//var link = document.getElementById("returnlink");
		//link.href = url;
		window.location = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>

</body>
</html>

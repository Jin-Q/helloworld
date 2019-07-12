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
		alert("操作成功！");
		var action = '<%=url%>';
		var cus_id = "${context.cus_id}";		
		var url = '<emp:url action="' + action + '"/>&CusComCheck.cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
		//var link = document.getElementById("returnlink");
		//link.href = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">
</body>
</html>

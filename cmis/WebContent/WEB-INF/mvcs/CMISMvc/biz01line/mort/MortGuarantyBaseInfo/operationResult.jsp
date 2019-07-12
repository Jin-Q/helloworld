<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
String url=request.getParameter("url");
String op=request.getParameter("op");
//押品信息跳转页面

%>
<html>
<head>
<title>操作返回页面</title>
<script type="text/javascript">
	
	function test(){
		
		var action = '<%=url%>';
		var op = '<%=op%>';
		var url = '<emp:url action="'+action+'"/>&guaranty_no=${context.guaranty_no}&op='+op+'&guaranty_type=${context.guaranty_type}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="test();">


</body>
</html>
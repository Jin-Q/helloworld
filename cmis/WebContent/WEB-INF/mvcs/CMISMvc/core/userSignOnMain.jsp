<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doOnLoad(){
	var form = document.getElementById("submitForm");
	var userId = '${context.currentUserId}';
	var loginuserid = '${context.loginuserid}';

	url = '<emp:url action="userSignOn.do"/>?currentUserId='+userId+'&loginuserid='+loginuserid;
	url = EMPTools.encodeURI(url);
	window.location = url;
};
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="userSignOn.do" method="POST">
	</emp:form>
</body>
</html>
</emp:page>


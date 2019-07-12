<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String stTaskId = (String)context.getDataValue("st_task_id");
%>
<emp:page>
<html>
<head>
<title>发起会签</title>

<jsp:include page="/include.jsp" flush="true" />
<script type="text/javascript">
window.onload = function() {
	var url = '<emp:url action="getWfiSignTaskUpdatePage.do"/>?st_task_id=<%=stTaskId%>';
	url = EMPTools.encodeURI(url);
	window.location = url;
}
</script>
</head>
<body>
</body>
</html>
</emp:page>
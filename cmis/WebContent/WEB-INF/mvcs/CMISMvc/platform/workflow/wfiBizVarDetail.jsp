<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String bizUrl = (String)context.getDataValue("wfBizUrl");
%>
<emp:page>
<html>
<head>
<title>审批变更详细</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
window.onload = function() {
	var inputs = document.getElementsByTagName("input");
	if(inputs!=null && inputs.length>0) {
		for(var i=0; i<inputs.length; i++) {
			var inputObj = inputs[i];
			inputObj.disabled=true;
		}
	}
}

</script>

</head>
<body>
<jsp:include page="<%=bizUrl %>"></jsp:include>
</body>
</html>
</emp:page>
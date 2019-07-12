<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>璇︽儏鏌ヨ椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfiApproverUserList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiApproverUserGroup" title="WFI_APPROVER_USER" maxColumn="2">
			<emp:text id="WfiApproverUser.confid" label="CONFID" maxlength="40" required="true" />
			<emp:text id="WfiApproverUser.actorno" label="ACTORNO" maxlength="20" required="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="杩斿洖鍒板垪琛ㄩ〉闈�"/>
	</div>
</body>
</html>
</emp:page>

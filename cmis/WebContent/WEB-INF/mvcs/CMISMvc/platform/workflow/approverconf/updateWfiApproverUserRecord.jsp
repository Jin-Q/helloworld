<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>淇敼椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateWfiApproverUserRecord.do" method="POST">
		<emp:gridLayout id="WfiApproverUserGroup" maxColumn="2" title="WFI_APPROVER_USER">
			<emp:text id="WfiApproverUser.confid" label="CONFID" maxlength="40" required="true" />
			<emp:text id="WfiApproverUser.actorno" label="ACTORNO" maxlength="20" required="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="淇敼" op=""/>
			<emp:button id="reset" label="鍙栨秷"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

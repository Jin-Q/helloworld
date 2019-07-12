<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSDeptuserRecord.do" method="POST">
		
		<emp:gridLayout id="SDeptuserGroup" title="机构用户表" maxColumn="2">
			<emp:text id="SDeptuser.organno" label="机构码" maxlength="16" required="true" />
			<emp:text id="SDeptuser.depno" label="部门码" maxlength="16" required="false" />
			<emp:text id="SDeptuser.actorno" label="用户码" maxlength="8" required="true" />
			<emp:text id="SDeptuser.state" label="状态" maxlength="38" required="true" />
			<emp:text id="SDeptuser.orgid" label="组织号" maxlength="16" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


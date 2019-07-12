<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSRoleuserRecord.do" method="POST">
		<emp:gridLayout id="SRoleuserGroup" maxColumn="2" title="角色用户表">
			<emp:text id="SRoleuser.roleno" label="角色码" maxlength="4" required="true" readonly="true" />
			<emp:text id="SRoleuser.actorno" label="用户码" maxlength="8" required="true" readonly="true" />
			<emp:text id="SRoleuser.state" label="状态" maxlength="38" required="true" />
			<emp:text id="SRoleuser.orgid" label="组织号" maxlength="16" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

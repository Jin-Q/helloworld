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
	
	<emp:form id="submitForm" action="addSDutyuserRecord.do" method="POST">
		
		<emp:gridLayout id="SDutyuserGroup" title="岗位用户表" maxColumn="2">
			<emp:text id="SDutyuser.dutyno" label="岗位码" maxlength="4" required="true" />
			<emp:text id="SDutyuser.actorno" label="用户码" maxlength="8" required="true" />
			<emp:text id="SDutyuser.state" label="状态" maxlength="38" required="true" />
			<emp:text id="SDutyuser.orgid" label="组织号" maxlength="16" required="false" />
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


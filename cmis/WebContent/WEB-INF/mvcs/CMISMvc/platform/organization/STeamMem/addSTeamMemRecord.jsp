<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//成员编号
	function setconId(data){
		STeamMem.mem_no._setValue(data.actorno._getValue());
		STeamMem.mem_no_displayname._setValue(data.actorname._getValue());
	}				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSTeamMemRecord.do" method="POST">
		
		<emp:gridLayout id="STeamMemGroup" title="团队成员表" maxColumn="2">
			<emp:text id="STeamMem.team_no" label="团队编号" maxlength="20" required="true" readonly="true"/>
			<emp:select id="STeamMem.team_role" label="团队角色" required="true" dictname="STD_TEAM_ROLE"/>
			<emp:pop id="STeamMem.mem_no" label="成员编号" required="true" colSpan="2" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:text id="STeamMem.mem_no_displayname" label="成员名称" required="false" cssElementClass="emp_field_text_readonly"/>
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


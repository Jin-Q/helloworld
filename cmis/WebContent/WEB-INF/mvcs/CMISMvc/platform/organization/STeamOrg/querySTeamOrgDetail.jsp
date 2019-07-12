<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="STeamOrgGroup" title="团队成员表" maxColumn="2">
			<emp:text id="STeamOrg.team_no" label="团队编号" maxlength="20" required="true" readonly="true"/>
			<emp:text id="STeamOrg.team_no_displayname" label="团队名称" maxlength="20" required="true" readonly="true"/>
			<emp:text id="STeamOrg.team_org_id" label="机构码" required="true" readonly="true" />
			<emp:text id="STeamOrg.team_org_id_displayname" label="机构名称"  required="false" cssElementClass="emp_field_text_readonly"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

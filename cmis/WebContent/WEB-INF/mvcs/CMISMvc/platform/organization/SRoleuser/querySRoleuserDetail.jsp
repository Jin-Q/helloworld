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
		var url = '<emp:url action="querySRoleuserList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SRoleuserGroup" title="角色用户表" maxColumn="2">
			<emp:text id="SRoleuser.roleno" label="角色码" maxlength="4" required="true" />
			<emp:text id="SRoleuser.actorno" label="用户码" maxlength="8" required="true" />
			<emp:text id="SRoleuser.state" label="状态" maxlength="38" required="true" />
			<emp:text id="SRoleuser.orgid" label="组织号" maxlength="16" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
		//var url = '<emp:url action="querySRoleList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SRoleGroup" title="角色表" maxColumn="2">
			<emp:text id="SRole.roleno" label="角色码" maxlength="4" required="true" />
			<emp:text id="SRole.rolename" label="角色名称" maxlength="40" required="true" />
			<emp:text id="SRole.orderno" label="排序字段" maxlength="38" required="false" />
			<emp:text id="SRole.orgid" label="组织号" maxlength="16" required="false" />
			<emp:select id="SRole.type" label="类型"  required="false" hidden="false" dictname="STD_ZB_SROLE_TYPE" />
			<emp:textarea id="SRole.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

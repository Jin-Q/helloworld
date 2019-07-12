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
		var url = '<emp:url action="queryWfTaskpoolList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfTaskpoolGroup" title="项目池" maxColumn="2">
			<emp:text id="WfTaskpool.tpid" label="项目池编号" maxlength="32" required="true" />
			<emp:text id="WfTaskpool.tpname" label="项目池名称" maxlength="100" required="false" />
			<emp:textarea id="WfTaskpool.tpdsc" label="描述" maxlength="255" required="false" colSpan="2"/>
			<emp:text id="WfTaskpool.sysid" label="系统ID" maxlength="32" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

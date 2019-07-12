<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
		
	function setSysId(data) {
		WfTaskpool.sysid._setValue(data.clientsign._getValue());
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfTaskpoolRecord.do" method="POST">
		
		<emp:gridLayout id="WfTaskpoolGroup" title="项目池" maxColumn="2">
			<emp:text id="WfTaskpool.tpid" label="项目池编号" maxlength="32" required="true" />
			<emp:text id="WfTaskpool.tpname" label="项目池名称" maxlength="100" required="true" />
			<emp:textarea id="WfTaskpool.tpdsc" label="描述" maxlength="255" required="false" colSpan="2"/>
			<emp:pop id="WfTaskpool.sysid" label="系统ID" url="queryWfClientPopList.do?returnMethod=setSysId" returnMethod="setSysId" required="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op=""/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


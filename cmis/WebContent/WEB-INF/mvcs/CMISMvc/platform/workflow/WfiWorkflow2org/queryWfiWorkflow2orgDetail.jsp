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
		var url = '<emp:url action="queryWfiWorkflow2orgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiWorkflow2orgGroup" title="流程关联机构配置" maxColumn="2">
			<emp:text id="WfiWorkflow2org.wf2org_id" label="关联ID" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:pop id="WfiWorkflow2org.org_id" label="机构ID" required="true" returnMethod="getOrgID" url="querySOrgPop.do?restrictUsed=false" />
			<emp:text id="WfiWorkflow2org.org_name" label="机构名称" maxlength="40" required="true" readonly="true"/>
			<emp:select id="WfiWorkflow2org.appl_type" label="申请类型" required="true" dictname="ZB_BIZ_CATE" colSpan="2" onchange="changeApplType()"/>
			<emp:pop id="WfiWorkflow2org.wfsign" label="流程标识" url="queryWorkflow2bizByApplType.do?returnMethod=getWfsign" returnMethod="getWfsign" required="true" />
			<emp:text id="WfiWorkflow2org.wfname" label="流程名称" maxlength="50" required="true" readonly="true"/>
			<emp:textarea id="WfiWorkflow2org.remark" label="备注" maxlength="100" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

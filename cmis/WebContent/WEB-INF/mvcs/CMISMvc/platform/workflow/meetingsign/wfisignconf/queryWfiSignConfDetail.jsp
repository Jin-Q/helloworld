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
		var url = '<emp:url action="queryWfiSignConfList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiSignConfGroup" title="会签策略配置" maxColumn="2">
			<emp:text id="WfiSignConf.sign_id" label="会签策略ID" maxlength="40" readonly="true" required="false" />
			<emp:text id="WfiSignConf.sign_name" label="会签策略名" maxlength="50" required="false" />
			<emp:textarea id="WfiSignConf.sign_desc" label="会签策略描述" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="WfiSignConf.sign_class" label="会签策略实现" maxlength="100" required="false" colSpan="2" />
			<emp:select id="WfiSignConf.sign_state" label="会签策略状态" required="false" dictname="WF_SIGNCONF_STATE" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

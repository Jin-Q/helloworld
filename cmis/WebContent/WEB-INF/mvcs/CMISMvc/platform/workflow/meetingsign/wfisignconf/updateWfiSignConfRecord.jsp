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
	
	<emp:form id="submitForm" action="updateWfiSignConfRecord.do" method="POST">
		<emp:gridLayout id="WfiSignConfGroup" maxColumn="2" title="会签策略配置">
			<emp:text id="WfiSignConf.sign_id" label="会签策略ID" maxlength="40" required="true" readonly="true" />
			<emp:text id="WfiSignConf.sign_name" label="会签策略名" maxlength="50" required="true" />
			<emp:textarea id="WfiSignConf.sign_desc" label="会签策略描述" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="WfiSignConf.sign_class" label="会签策略实现" maxlength="100" required="true" colSpan="2" />
			<emp:select id="WfiSignConf.sign_state" label="会签策略状态" required="true" dictname="WF_SIGNCONF_STATE" />
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

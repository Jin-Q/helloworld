<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doNext(){
		if(!PspTaskCheckExpert._checkAll()){
			return;
		}
		var task_create_mode = PspTaskCheckExpert.task_create_mode._getValue();
		var url = '<emp:url action="getPspTaskCheckExpertAddTruePage.do"/>?task_create_mode='+task_create_mode;
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	function doReturn(){
		var url = '<emp:url action="queryPspTaskCheckExpertList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="getPspTaskCheckExpertAddTruePage.do" method="POST">
		
		<emp:gridLayout id="PspTaskCheckExpertGroup" title="贷后检查任务 " maxColumn="1">
			<emp:radio id="PspTaskCheckExpert.task_create_mode" label="任务生成类别" required="true" dictname="STD_ZB_TASK_CREATE"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


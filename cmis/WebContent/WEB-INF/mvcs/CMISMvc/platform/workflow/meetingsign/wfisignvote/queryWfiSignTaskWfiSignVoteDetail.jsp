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
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="WfiSignVoteGroup" title="贷审会投票表" maxColumn="2">
			<emp:text id="WfiSignVote.sv_vote_id" label="投票ID" maxlength="40" required="true" hidden="true"/>
			<emp:text id="WfiSignVote.sv_exe_user" label="任务执行人" maxlength="10" required="false" hidden="true"/>
			<emp:text id="WfiSignVote.sv_exe_user_displayname" label="任务执行人" required="false" />
			<emp:textarea id="WfiSignVote.sv_advice" label="意见" maxlength="1000" required="false" colSpan="2" />
			<emp:select id="WfiSignVote.sv_result" label="投票结果" required="false" dictname="WF_SIGN_RESULT" />
			<emp:select id="WfiSignVote.sv_status" label="任务状态" required="false" dictname="WF_SIGN_STATUS" />
			<emp:text id="WfiSignVote.sv_start_time" label="任务开始时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="WfiSignVote.sv_end_time" label="任务结束时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="WfiSignVote.st_task_id" label="任务ID" maxlength="40" required="true" hidden="true"/>
			<emp:text id="WfiSignVote.sv_request_time" label="要求完成时间" maxlength="10" required="false" dataType="Date" hidden="true"/>
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

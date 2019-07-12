<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfiSignTaskList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewWfiSignVote() {
		var paramStr = WfiSignTask.WfiSignVote._obj.getParamStr(['sv_vote_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryWfiSignTaskWfiSignVoteDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="WfiSignTaskGroup" title="会签任务主表" maxColumn="2">
			<emp:text id="WfiSignTask.st_task_id" label="会签任务ID" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="WfiSignTask.st_task_name" label="任务名称" maxlength="60" required="false" />
			<emp:text id="WfiSignTask.st_exe_user" label="会签安排人" maxlength="10" required="false" />
			<emp:text id="WfiSignTask.st_exe_org" label="执行机构" maxlength="10" required="false" />
			<emp:text id="WfiSignTask.st_config" label="会签策略" maxlength="40" required="false" hidden="true"/>
			<emp:text id="WfiSignTask.st_config_displayname" label="会签策略"  required="false" readonly="true" />
			<emp:textarea id="WfiSignTask.st_advice" label="会签意见" maxlength="1000" required="false" colSpan="2" />
			<emp:text id="WfiSignTask.st_members" label="会签成员" maxlength="500" required="false" />
			<emp:text id="WfiSignTask.st_leader" label="牵头人" maxlength="10" required="false" />
			<emp:text id="WfiSignTask.st_agree_count" label="同意票数" maxlength="0" required="false" dataType="Int" />
			<emp:text id="WfiSignTask.st_reject_count" label="否决票数" maxlength="0" required="false" dataType="Int" />
			<emp:text id="WfiSignTask.st_noidea_count" label="复议票数" maxlength="0" required="false" dataType="Int" />
			<emp:select id="WfiSignTask.st_result" label="会签结果" required="false" dictname="WF_SIGN_RESULT" />
			<emp:select id="WfiSignTask.st_task_status" label="会签状态" required="false" dictname="WF_SIGN_STATUS" />
			<emp:text id="WfiSignTask.serno" label="业务流水号" maxlength="40" required="false" />
			<emp:select id="WfiSignTask.biz_type" label="业务类型" required="false" dictname="ZB_BIZ_CATE" />
			<emp:text id="WfiSignTask.st_start_time" label="开始时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="WfiSignTask.st_end_time" label="结束时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="WfiSignTask.st_task_times" label="会签次数" maxlength="0" required="false" dataType="Int" />
			<emp:text id="WfiSignTask.wfi_node_id" label="流程节点ID" maxlength="40" required="false" hidden="true"/>
			<emp:text id="WfiSignTask.wfi_instance_id" label="流程实例号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="WfiSignTask.wfi_advice_id" label="审批意见ID" maxlength="40" required="false" hidden="true"/>
			<emp:text id="WfiSignTask.st_request_day" label="要求办理天数" maxlength="0" required="true" dataType="Int" />
			<emp:text id="WfiSignTask.st_duty" label="会签成员岗位" maxlength="40" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<br>

	<emp:tabGroup id="WfiSignTask_tabs" mainTab="WfiSignVote_tab">
		<emp:tab id="WfiSignVote_tab" label="贷审会投票表">
			<div align="left">
				<emp:button id="viewWfiSignVote" label="查看" op="view_WfiSignVote"/>
			</div>
			<emp:table icollName="WfiSignTask.WfiSignVote" pageMode="false" url="">
				<emp:text id="sv_vote_id" label="投票ID" hidden="true" />
				<emp:text id="sv_exe_user" label="任务执行人" />
				<emp:text id="sv_result" label="投票结果" dictname="WF_SIGN_RESULT" />
				<emp:text id="sv_status" label="任务状态" dictname="WF_SIGN_STATUS" />
				<emp:text id="sv_start_time" label="任务开始时间" />
				<emp:text id="sv_end_time" label="任务结束时间" />
				<emp:text id="sv_request_time" label="要求完成时间" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>

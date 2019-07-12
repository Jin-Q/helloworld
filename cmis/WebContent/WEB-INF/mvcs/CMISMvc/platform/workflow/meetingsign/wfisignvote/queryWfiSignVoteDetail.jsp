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
		var url = '<emp:url action="queryWfiSignVoteList.do"/>?&menuId=wfisignvote';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup id="WfiSignTaskTab_tabs" mainTab="task_main_tab">
		<emp:tab id="app_main_tab" label="申请信息"  url="${context.WfiSignTask.wfi_biz_page}" />
		<emp:tab id="task_main_tab" label="会签详细信息">
		<emp:gridLayout id="WfiSignVoteGroup" maxColumn="2" title="会签投票明细">
		    <emp:text id="WfiSignVote.st_task_name" label="任务名称" maxlength="60" readonly="true" colSpan="2"/>
		    <emp:text id="WfiSignTask.st_exe_user" label="会签秘书" readonly="true" hidden="true" />
		    <emp:text id="WfiSignTask.st_exe_user_displayname" label="会签秘书" readonly="true" />
		    <emp:textarea id="WfiSignVote.st_advice" label="会议意见" maxlength="1000" readonly="true" colSpan="2" />
		    		    
		    <emp:text id="WfiSignVote.serno" label="业务流水号" maxlength="40" readonly="true"/>
		    <emp:select id="WfiSignVote.biz_type" label="业务类型" dictname="ZB_BIZ_CATE" readonly="true"/>
		    
		    <emp:select id="WfiSignVote.sv_result" label="投票结果" required="false" dictname="WF_SIGN_RESULT" />
			
			<emp:textarea id="WfiSignVote.sv_advice" label="投票意见" maxlength="1000" required="false" colSpan="2" />
			<emp:text id="WfiSignVote.st_task_id" label="任务ID" maxlength="40" required="true" hidden="true"/>
			<emp:text id="WfiSignVote.sv_request_time" label="要求完成时间" maxlength="10" required="false" dataType="Date"  hidden="true"/>
			<emp:text id="WfiSignVote.sv_vote_id" label="投票ID" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="WfiSignVote.sv_exe_user" label="任务执行人" maxlength="10" required="false"  hidden="true"/>
			<emp:text id="WfiSignVote.sv_start_time" label="任务开始时间" maxlength="10" required="false" dataType="Date" readonly="true"/>
			<emp:text id="WfiSignVote.sv_end_time" label="任务结束时间" maxlength="10" required="false" dataType="Date" readonly="true"/>
			<emp:select id="WfiSignVote.sv_status" label="任务状态" required="false" dictname="WF_SIGN_STATUS"  readonly="true"/>
		</emp:gridLayout>
	</emp:tab>
		
	<emp:tab id="app_history_tab" label="审批历史" url="getWfApproveHis.do" reqParams="instanceId=${context.WfiSignTask.wfi_instance_id}&nodeId=${context.WfiSignTask.wfi_node_id}" />
	
<!-- <emp:tab id="app_risk_history_tab" label="风险检查历史" url="showRiskInspectResult.do" reqParams="pkVal=${context.WfiSignTask.serno}&wfsign=${context.WfiSignTask.wfi_sign}&bizType=${context.WfiSignVote.biz_type }" needFlush="true"/>  -->
	</emp:tabGroup>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

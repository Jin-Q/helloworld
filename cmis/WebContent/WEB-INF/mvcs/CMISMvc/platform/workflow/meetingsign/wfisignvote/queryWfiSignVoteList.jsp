<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiSignVote._toForm(form);
		WfiSignVoteList._obj.ajaxQuery(null,form);
	};
	
	function doVoteWfiSignTask() {
		var dataRow =  WfiSignVoteList._obj.getSelectedData()[0];
		var approveStatus = dataRow.sv_status._getValue() ;
		if(checkStatus(approveStatus)){
			var paramStr = WfiSignVoteList._obj.getParamStr(['sv_vote_id']);
			if (paramStr != null) {
				var url = '<emp:url action="getWfiSignVoteUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			} else {
				alert('请先选择一条记录！');
			}
		}
	};
	
	function doViewWfiSignVote() {
		
		var paramStr = WfiSignVoteList._obj.getParamStr(['sv_vote_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiSignVoteViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
	function doReset(){
		page.dataGroups.WfiSignVoteGroup.reset();
	};
	
	/*--user code begin--*/
	function checkStatus(approve){
		if(approve!='211') {
			alert("对不起!该任务已经投票或者任务已经结束!") ;
			return false ;
		}
		return true ;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiSignVoteGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WfiSignVote.serno" label="业务流水号" />
			<emp:select id="WfiSignVote.sv_status" label="任务状态" dictname="WF_SIGN_STATUS" />
			<emp:select id="WfiSignVote.sv_result" label="投票结果" dictname="WF_SIGN_RESULT" />
			<emp:text id="WfiSignVote.sv_advice" label="意见" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<!--emp:button id="getAddWfiSignVotePage" label="新增" op="add"/-->
		<emp:button id="voteWfiSignTask" label="投票" op="vote"/>
		<!--emp:button id="deleteWfiSignVote" label="删除" op="remove"/-->
		<emp:button id="viewWfiSignVote" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiSignVoteList" pageMode="true" url="pageWfiSignVoteQuery.do">
		<emp:text id="serno" label="业务流水号"/>
		<emp:text id="biz_type" label="业务类型" dictname="ZB_BIZ_CATE"/>
		<emp:text id="st_task_name" label="任务名称"/>
        <emp:text id="sv_exe_user_displayname" label="任务执行人"/>
		<emp:text id="sv_result" label="投票结果" dictname="WF_SIGN_RESULT" />
		<emp:text id="sv_status" label="任务状态" dictname="WF_SIGN_STATUS" />
		<emp:text id="sv_start_time" label="任务开始时间" />
		<emp:text id="sv_end_time" label="任务结束时间" />
		<emp:text id="sv_request_time" label="要求完成时间" hidden="true"/>
		<emp:text id="sv_vote_id" label="投票ID"  hidden="true"/>
		<emp:text id="sv_exe_user" label="任务执行人"  hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
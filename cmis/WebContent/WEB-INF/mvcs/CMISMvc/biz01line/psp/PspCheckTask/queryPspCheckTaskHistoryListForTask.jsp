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
		PspCheckTask._toForm(form);
		PspCheckTaskList._obj.ajaxQuery(null,form);
	};
	
	function doViewPspCheckTask() {
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?'+paramStr+'&op=view&openType=grp';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCheckTaskGroup.reset();
	};

	function returnCus(data){
		PspCheckTask.cus_id._setValue(data.cus_id._getValue());
		PspCheckTask.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	
	function setconId(data){
		PspCheckTask.manager_id._setValue(data.actorno._getValue());
		PspCheckTask.manager_id_displayname._setValue(data.actorname._getValue());
	};
	
	function getOrgID(data){
		PspCheckTask.manager_br_id._setValue(data.organno._getValue());
		PspCheckTask.manager_br_id_displayname._setValue(data.organname._getValue());
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="PspCheckTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckTask.task_id" label="任务编号"  /> 
			<emp:pop id="PspCheckTask.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:pop id="PspCheckTask.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择"/>
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:datespace id="PspCheckTask.task_create_date" label="任务生成日期" />
			<emp:datespace id="PspCheckTask.task_request_time" label="要求完成时间" />
			
			<emp:text id="PspCheckTask.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="PspCheckTask.manager_id" label="主管客户经理" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="viewPspCheckTask" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspCheckTaskList" pageMode="true" url="pagePspCheckTaskHistoryQuery.do?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}&restrictFlag=false">
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="qnt" label="业务笔数" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="task_request_time" label="要求完成时间" />
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_id" label="主管客户经理" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		
	</emp:table>
	
</body>
</html>
</emp:page>
    
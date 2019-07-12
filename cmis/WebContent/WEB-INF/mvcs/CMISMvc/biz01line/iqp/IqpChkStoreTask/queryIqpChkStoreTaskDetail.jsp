<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showButton = (String)context.getDataValue("showButton");
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var task_set_id = IqpChkStoreTask.task_id._getValue();
		var url = '<emp:url action="queryIqpChkStoreTaskList.do"/>&task_set_id='+task_set_id+"&menuId=IqpChkStoreTask";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="核/巡待办任务基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpChkStoreTaskGroup" maxColumn="2" title="核/巡库待办任务信息">
			<emp:text id="IqpChkStoreTask.task_id" label="任务执行编号" maxlength="32" required="true" readonly="true" />
			<emp:select id="IqpChkStoreTask.task_set_type" label="任务维度" required="false" dictname="STD_ZB_INSURE_MODE" readonly="true"/>
			<emp:text id="IqpChkStoreTask.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.oversee_agr_no" label="监管协议编号" maxlength="32" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.task_request_time" label="要求完成时间" maxlength="10" required="false" readonly="true"/>
			<emp:date id="IqpChkStoreTask.act_complete_time" label="实际完成时间" readonly="true" />
			<emp:select id="IqpChkStoreTask.prc_status" label="处理状态" required="false" readonly="true" dictname="STD_TASK_PRC_STATUS" />
			<emp:textarea id="IqpChkStoreTask.remarks" label="备注" maxlength="250" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">
			<emp:pop id="IqpChkStoreTask.manager_id_displayname" label="责任人 " url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" readonly="true" buttonLabel="选择"/>
			<emp:pop id="IqpChkStoreTask.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" hidden="true" readonly="true" buttonLabel="选择"/>
			<emp:text id="IqpChkStoreTask.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpChkStoreTask.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpChkStoreTask.input_date" label="登记日期" maxlength="10" required="false" readonly="true" />
			<emp:select id="IqpChkStoreTask.approve_status" label="审批状态" required="false" readonly="true" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpChkStoreTask.input_id" label="登记人" maxlength="20" required="false" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="IqpChkStoreTask.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true"/>
			<emp:text id="IqpChkStoreTask.manager_id" label="责任人 " maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpChkStoreTask.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
		<div align="center">
		<br>
			<%if(!"N".equalsIgnoreCase(showButton)){ %>
				<emp:button id="return" label="返回到列表页面"/>
			<%}else{%>
			<%} %>
		</div>
	</emp:tabGroup>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpChkStoreTaskRecord.do" method="POST">
		
		<emp:gridLayout id="IqpChkStoreTaskGroup" title="核/巡库待办任务信息" maxColumn="2">
			<emp:text id="IqpChkStoreTask.task_id" label="任务执行编号" maxlength="32" readonly="true" required="false" />
			<emp:text id="IqpChkStoreTask.task_set_type" label="任务维度" maxlength="2" required="false" />
			<emp:text id="IqpChkStoreTask.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="IqpChkStoreTask.task_request_time" label="要求完成时间" maxlength="10" required="false" />
			<emp:text id="IqpChkStoreTask.oversee_agr_no" label="监管协议编号" maxlength="32" required="false" />
			<emp:text id="IqpChkStoreTask.act_complete_time" label="实际完成时间" maxlength="10" required="false" />
			<emp:text id="IqpChkStoreTask.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="IqpChkStoreTask.prc_status" label="处理状态" maxlength="5" required="false" />
			<emp:text id="IqpChkStoreTask.manager_id" label="责任人 " maxlength="20" required="false" />
			<emp:text id="IqpChkStoreTask.manager_br_id" label="责任机构" maxlength="20" required="false" />
			<emp:text id="IqpChkStoreTask.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="IqpChkStoreTask.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="IqpChkStoreTask.input_date" label="登记日期" maxlength="10" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


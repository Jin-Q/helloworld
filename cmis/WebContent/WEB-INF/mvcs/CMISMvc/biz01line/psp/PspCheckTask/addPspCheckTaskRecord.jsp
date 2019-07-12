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
	
	<emp:form id="submitForm" action="addPspCheckTaskRecord.do" method="POST">
		
		<emp:gridLayout id="PspCheckTaskGroup" title="贷后检查任务" maxColumn="2">
			<emp:text id="PspCheckTask.task_id" label="任务编号" maxlength="40" required="true" />
			<emp:text id="PspCheckTask.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:select id="PspCheckTask.check_type" label="检查类型（STD_ZB_CHECK_TYPE）" required="false" dictname="STD_ZB_CHECK_TYPE" />
			<emp:text id="PspCheckTask.task_create_date" label="任务生成日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="PspCheckTask.task_request_time" label="要求完成时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="PspCheckTask.qnt" label="笔数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="PspCheckTask.loan_totl_amt" label="贷款总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="PspCheckTask.loan_balance" label="贷款余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="PspCheckTask.task_huser" label="任务执行人" maxlength="40" required="false" />
			<emp:text id="PspCheckTask.task_horg" label="任务执行机构" maxlength="20" required="false" />
			<emp:text id="PspCheckTask.task_divis_person" label="任务分配人" maxlength="40" required="false" />
			<emp:text id="PspCheckTask.task_divis_org" label="任务分配机构" maxlength="20" required="false" />
			<emp:text id="PspCheckTask.main_manager_id" label="主管客户经理" maxlength="40" required="false" />
			<emp:text id="PspCheckTask.main_manager_org" label="主管机构" maxlength="20" required="false" />
			<emp:text id="PspCheckTask.check_time" label="检查时间" maxlength="10" required="false" dataType="Date" />
			<emp:text id="PspCheckTask.check_addr" label="检查地点" maxlength="200" required="false" />
			<emp:text id="PspCheckTask.agreed_person" label="约见人员" maxlength="40" required="false" />
			<emp:text id="PspCheckTask.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="PspCheckTask.input_id" label="登记人" maxlength="10" required="false" />
			<emp:text id="PspCheckTask.input_br_id" label="登记机构" maxlength="10" required="false" />
			<emp:text id="PspCheckTask.input_date" label="登记日期" maxlength="10" required="false" dataType="Date" />
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


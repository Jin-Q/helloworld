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
	
	<emp:form id="submitForm" action="addPspGuarantyValueReevalRecord.do" method="POST">
		
		<emp:gridLayout id="PspGuarantyValueReevalGroup" title="担保品价值重估表（贷后）" maxColumn="2">
			<emp:text id="PspGuarantyValueReeval.pk_id" label="主键" maxlength="32" readonly="true" required="false" />
			<emp:text id="PspGuarantyValueReeval.task_id" label="任务编号" required="true" />
			<emp:text id="PspGuarantyValueReeval.cus_id" label="客户码" maxlength="40" required="true" />
			<emp:text id="PspGuarantyValueReeval.guaranty_no" label="担保品编号" maxlength="40" required="true" />
			<emp:text id="PspGuarantyValueReeval.batch_reeval_value" label="本期批量重估押品价值" maxlength="16" required="false" />
			<emp:text id="PspGuarantyValueReeval.reeval_value" label="本期建议押品价值" maxlength="16" required="false" />
			<emp:text id="PspGuarantyValueReeval.input_id" label="登记人" maxlength="40" required="false" />
			<emp:text id="PspGuarantyValueReeval.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="PspGuarantyValueReeval.input_date" label="登记日期" maxlength="10" required="false" />
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


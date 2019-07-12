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
	
	<emp:form id="submitForm" action="addIqpBatchBillRelRecord.do" method="POST">
		
		<emp:gridLayout id="IqpBatchBillRelGroup" title="批次和票据关系表" maxColumn="2">
			<emp:text id="IqpBatchBillRel.batch_no" label="批次号" maxlength="40" required="true" />
			<emp:text id="IqpBatchBillRel.porder_no" label="汇票号码" maxlength="40" required="true" />
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


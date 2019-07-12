<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtBatchCorreRecord.do" method="POST">
		<emp:gridLayout id="LmtBatchCorreGroup" maxColumn="2" title="批量客户名单关联表">
			<emp:text id="LmtBatchCorre.batch_cus_no" label="批量客户编号" maxlength="32" required="true" readonly="true" />
			<emp:text id="LmtBatchCorre.cus_id" label="客户码" maxlength="32" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

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
	
	<emp:form id="submitForm" action="addLmtIntbankDetailRecord.do" method="POST">
		
		<emp:gridLayout id="LmtIntbankDetailGroup" title="同业客户授信明细" maxColumn="2">
			<emp:text id="LmtIntbankDetail.cus_id" label="客户码" maxlength="32" required="true" />
			<emp:text id="LmtIntbankDetail.variet_no" label="品种编号" maxlength="32" required="false" />
			<emp:text id="LmtIntbankDetail.variet_name" label="品种名称" maxlength="32" required="false" />
			<emp:text id="LmtIntbankDetail.lmt_amt" label="授信额度" maxlength="16" required="false" />
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


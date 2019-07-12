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
	
	<emp:form id="submitForm" action="addIqpBailSubDisDetailRecord.do" method="POST">
		
		<emp:gridLayout id="IqpBailSubDisDetailGroup" title="保证金追加/提取明细" maxColumn="2">
			<emp:text id="IqpBailSubDisDetail.serno" label="业务编号" maxlength="60" required="true" />
			<emp:text id="IqpBailSubDisDetail.cont_no" label="合同编号" maxlength="60" required="true" />
			<emp:text id="IqpBailSubDisDetail.bail_acct_no" label="保证金账号" maxlength="60" required="false" />
			<emp:text id="IqpBailSubDisDetail.origi_bail_bal" label="原保证金余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBailSubDisDetail.adjust_amt" label="追加/提取金额" maxlength="18" required="false" dataType="Currency" />
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


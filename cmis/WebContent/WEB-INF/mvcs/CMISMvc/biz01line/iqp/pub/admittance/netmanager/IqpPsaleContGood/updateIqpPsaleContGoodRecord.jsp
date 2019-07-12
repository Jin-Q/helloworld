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
	
	<emp:form id="submitForm" action="updateIqpPsaleContGoodRecord.do" method="POST">
		<emp:gridLayout id="IqpPsaleContGoodGroup" maxColumn="2" title="购销合同商品结果表">
			<emp:text id="IqpPsaleContGood.psale_cont" label="购销合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpPsaleContGood.commo_name" label="商品名称" maxlength="100" required="true" readonly="true" />
			<emp:text id="IqpPsaleContGood.qnt" label="数量" maxlength="16" required="false" dataType="Int" />
			<emp:text id="IqpPsaleContGood.unit_price" label="单价（元）" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="IqpPsaleContGood.total" label="总价值（元）" maxlength="16" required="false" dataType="Currency" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

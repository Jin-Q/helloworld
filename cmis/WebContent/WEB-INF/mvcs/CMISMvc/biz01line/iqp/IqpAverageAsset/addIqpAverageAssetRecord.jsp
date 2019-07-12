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
	
	<emp:form id="submitForm" action="addIqpAverageAssetRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAverageAssetGroup" title="资产卖出表" maxColumn="2">
			<emp:text id="IqpAverageAsset.serno" label="业务流水号" maxlength="40" required="true" />
			<emp:text id="IqpAverageAsset.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="IqpAverageAsset.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:text id="IqpAverageAsset.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:select id="IqpAverageAsset.average_status" label="资产状态 正常/作废" required="false" dictname="STD_ZB_AVERGER_STATUS" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


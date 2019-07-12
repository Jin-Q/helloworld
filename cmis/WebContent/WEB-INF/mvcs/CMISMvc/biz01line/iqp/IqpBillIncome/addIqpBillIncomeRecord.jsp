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
	
	<emp:form id="submitForm" action="addIqpBillIncomeRecord.do" method="POST">
		
		<emp:gridLayout id="IqpBillIncomeGroup" title="票据收益计算表" maxColumn="2">
			<emp:select id="IqpBillIncome.biz_type" label="业务类型" required="false" />
			<emp:text id="IqpBillIncome.batch_no" label="批次号" maxlength="40" required="true" />
			<emp:text id="IqpBillIncome.porder_no" label="汇票号码" maxlength="40" required="true" />
			<emp:date id="IqpBillIncome.fore_disc_date" label="转/贴现日期" required="false" />
			<emp:text id="IqpBillIncome.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBillIncome.disc_days" label="贴现天数" maxlength="38" required="false" />
			<emp:text id="IqpBillIncome.adj_days" label="调整天数" maxlength="38" required="false" />
			<emp:text id="IqpBillIncome.disc_rate" label="转/贴现利率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpBillIncome.int" label="利息" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="IqpBillIncome.fore_rebuy_date" label="预计回购日期" required="false" />
			<emp:text id="IqpBillIncome.rebuy_days" label="回购天数" maxlength="38" required="false" />
			<emp:text id="IqpBillIncome.rebuy_int" label="回购利息" maxlength="18" required="false" dataType="Currency" />
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


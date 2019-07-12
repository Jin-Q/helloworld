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
	
	<emp:form id="submitForm" action="updateCsgnLoanInfoRecord.do" method="POST">
		<emp:gridLayout id="CsgnLoanInfoGroup" maxColumn="2" title="委托贷款关联信息">
			<emp:text id="CsgnLoanInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CsgnLoanInfo.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:pop id="CsgnLoanInfo.csgn_cus_id" label="委托人客户码" url="null" required="false" />
			<emp:text id="CsgnLoanInfo.csgn_amt" label="委托金额" maxlength="18" required="false" dataType="Currency" />
			<emp:pop id="CsgnLoanInfo.csgn_acct_no" label="委托人一般账号" url="null" required="false" />
			<emp:text id="CsgnLoanInfo.csgn_acct_name" label="委托人一般账户名" maxlength="100" required="false" />
			<emp:text id="CsgnLoanInfo.chrg_rate" label="手续费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="CsgnLoanInfo.csgn_chrg_pay_rate" label="委托人手续费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="CsgnLoanInfo.debit_chrg_pay_rate" label="借款人手续费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="CsgnLoanInfo.csgn_inner_dep_no" label="委托人内部存款账号" maxlength="40" required="false" />
			<emp:text id="CsgnLoanInfo.csgn_inner_dep_name" label="委托人内部存款账户名" maxlength="100" required="false" />
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

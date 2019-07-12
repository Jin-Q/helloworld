<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpCsgnLoanInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpCsgnLoanInfoGroup" title="委托贷款关联信息" maxColumn="2">
			<emp:text id="IqpCsgnLoanInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:pop id="IqpCsgnLoanInfo.csgn_cus_id" label="委托人客户码" url="null" required="false" />
			<emp:text id="IqpCsgnLoanInfo.csgn_amt" label="委托金额" maxlength="18" required="false" dataType="Currency" />
			<emp:pop id="IqpCsgnLoanInfo.csgn_acct_no" label="委托人一般账号" url="null" required="false" />
			<emp:text id="IqpCsgnLoanInfo.csgn_acct_name" label="委托人一般账户名" maxlength="100" required="false" />
			<emp:text id="IqpCsgnLoanInfo.chrg_rate" label="手续费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpCsgnLoanInfo.csgn_chrg_pay_rate" label="委托人手续费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCsgnLoanInfo.debit_chrg_pay_rate" label="借款人手续费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCsgnLoanInfo.csgn_inner_dep_no" label="委托人内部存款账号" maxlength="40" required="false" />
			<emp:text id="IqpCsgnLoanInfo.csgn_inner_dep_name" label="委托人内部存款账户名" maxlength="100" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

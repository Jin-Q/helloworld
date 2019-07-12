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
	
	<emp:form id="submitForm" action="addIqpTrustReceiptLoanRecord.do" method="POST">
		
		<emp:gridLayout id="IqpTrustReceiptLoanGroup" title="信托收据贷款从表" maxColumn="2">
			<emp:text id="IqpTrustReceiptLoan.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpTrustReceiptLoan.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpTrustReceiptLoan.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:text id="IqpTrustReceiptLoan.order_no" label="来单号" maxlength="40" required="false" />
			<emp:select id="IqpTrustReceiptLoan.receipt_cur_type" label="单据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpTrustReceiptLoan.curt_order_amt" label="本次到单金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpTrustReceiptLoan.is_internal_cert_agt" label="是否国内证项下代付" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpTrustReceiptLoan.biz_settl_mode" label="原业务结算方式" required="false" />
			<emp:select id="IqpTrustReceiptLoan.is_replace" label="是否置换" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpTrustReceiptLoan.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
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


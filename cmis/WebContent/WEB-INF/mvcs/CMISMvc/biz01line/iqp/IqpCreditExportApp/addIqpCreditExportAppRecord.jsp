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
	
	<emp:form id="submitForm" action="addIqpCreditExportAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpCreditExportAppGroup" title="信用证项下出口押汇从表" maxColumn="2">
			<emp:text id="IqpCreditExportApp.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpCreditExportApp.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpCreditExportApp.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpCreditExportApp.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpCreditExportApp.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:text id="IqpCreditExportApp.bank_bp_no" label="我行bp号" maxlength="40" required="false" />
			<emp:select id="IqpCreditExportApp.is_internal_cert" label="是否国内证项下" required="false" />
			<emp:select id="IqpCreditExportApp.receipt_cur_type" label="单据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpCreditExportApp.receipt_cur_amt" label="单据金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpCreditExportApp.biz_settl_mode" label="原业务结算方式" required="false" />
			<emp:textarea id="IqpCreditExportApp.credit_advice" label="原国业部审单意见" maxlength="250" required="false" colSpan="2" />
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


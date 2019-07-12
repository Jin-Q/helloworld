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
	
	<emp:form id="submitForm" action="addIqpIntbankAgtRecord.do" method="POST">
		
		<emp:gridLayout id="IqpIntbankAgtGroup" title="同业代付从表" maxColumn="2">
			<emp:text id="IqpIntbankAgt.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpIntbankAgt.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpIntbankAgt.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:pop id="IqpIntbankAgt.order_no" label="来单号" url="null" required="false" />
			<emp:select id="IqpIntbankAgt.receipt_cur_type" label="单据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpIntbankAgt.curt_order_amt" label="本次到单金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpIntbankAgt.is_internal_cert_agt" label="是否国内证项下代付" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpIntbankAgt.agt_bank_no" label="代付行行号" maxlength="20" required="false" />
			<emp:text id="IqpIntbankAgt.agt_bank_name" label="代付行行名" maxlength="100" required="false" />
			<emp:select id="IqpIntbankAgt.biz_settl_mode" label="原业务结算方式" required="false" />
			<emp:select id="IqpIntbankAgt.is_replace" label="是否置换" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpIntbankAgt.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
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


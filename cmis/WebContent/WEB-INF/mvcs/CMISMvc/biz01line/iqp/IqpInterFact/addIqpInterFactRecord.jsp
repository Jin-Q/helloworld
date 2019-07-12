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
	
	<emp:form id="submitForm" action="addIqpInterFactRecord.do" method="POST">
		
		<emp:gridLayout id="IqpInterFactGroup" title="国际保理从表" maxColumn="2">
			<emp:text id="IqpInterFact.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpInterFact.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpInterFact.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpInterFact.fin_type" label="融资类型" required="false" />
			<emp:select id="IqpInterFact.fact_type" label="保理类型" required="false" />
			<emp:select id="IqpInterFact.invc_cur_type" label="发票币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpInterFact.invc_totl_amt" label="发票总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpInterFact.busnes_cont_no" label="贸易合同号" maxlength="40" required="false" />
			<emp:textarea id="IqpInterFact.goods_dec" label="货物描述" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpInterFact.fact_no" label="保理编号" maxlength="40" required="false" />
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


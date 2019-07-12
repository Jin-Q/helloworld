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
	
	<emp:form id="submitForm" action="addIqpExportOrderFinRecord.do" method="POST">
		
		<emp:gridLayout id="IqpExportOrderFinGroup" title="出口订单融资从表" maxColumn="2">
			<emp:text id="IqpExportOrderFin.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpExportOrderFin.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportOrderFin.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpExportOrderFin.order_cont_cur_type" label="订单合同币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpExportOrderFin.order_cont_amt" label="订单合同金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpExportOrderFin.biz_settl_mode" label="原业务结算方式" required="false" />
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


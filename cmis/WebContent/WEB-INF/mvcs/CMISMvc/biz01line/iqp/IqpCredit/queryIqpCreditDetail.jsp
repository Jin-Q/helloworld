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
		var url = '<emp:url action="queryIqpCreditList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpCreditGroup" title="信用证从表" maxColumn="2">
			<emp:text id="IqpCredit.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpCredit.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpCredit.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:date id="IqpCredit.end_date" label="到期日" required="false" />
			<emp:select id="IqpCredit.credit_term_type" label="信用证期限类型" required="false" />
			<emp:text id="IqpCredit.chrg_rate" label="手续费率" required="true" dataType="Rate"/>
			<emp:text id="IqpCredit.fast_day" label="远期天数" maxlength="38" required="false" />
			<emp:select id="IqpCredit.is_revolv_credit" label="是否循环信用证" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpCredit.is_internal_cert" label="是否国内信用证" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpCredit.is_ctrl_gclaim" label="是否可控货权" required="false" />
			<emp:text id="IqpCredit.busnes_cont" label="贸易合同号" maxlength="40" required="false" />
			<emp:text id="IqpCredit.beneficiar" label="受益人" maxlength="80" required="false" />
			<emp:select id="IqpCredit.beneficiar_country" label="受益人所在国家" required="false" dictname="STD_GB_2659-2000" />
			<emp:text id="IqpCredit.floodact_perc" label="溢装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCredit.shortact_perc" label="短装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCredit.credit_type" label="CREDIT_TYPE" maxlength="5" required="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

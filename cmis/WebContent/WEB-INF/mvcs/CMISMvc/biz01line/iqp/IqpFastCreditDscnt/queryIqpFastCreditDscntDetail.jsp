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
		var url = '<emp:url action="queryIqpFastCreditDscntList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpFastCreditDscntGroup" title="远期信用证项下汇票贴现从表" maxColumn="2">
			<emp:text id="IqpFastCreditDscnt.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpFastCreditDscnt.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpFastCreditDscnt.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpFastCreditDscnt.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpFastCreditDscnt.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:text id="IqpFastCreditDscnt.bank_bp_no" label="我行bp号" maxlength="40" required="false" />
			<emp:select id="IqpFastCreditDscnt.is_internal_cert" label="是否国内证项下" required="false" />
			<emp:text id="IqpFastCreditDscnt.post_order_no" label="汇票号码" maxlength="40" required="false" />
			<emp:select id="IqpFastCreditDscnt.bill_cur_type" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpFastCreditDscnt.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="IqpFastCreditDscnt.issue_date" label="出票日期" required="false" />
			<emp:date id="IqpFastCreditDscnt.bill_end_date" label="票据到期日" required="false" />
			<emp:text id="IqpFastCreditDscnt.drwr_name" label="出票人名称" maxlength="80" required="false" />
			<emp:text id="IqpFastCreditDscnt.accptr_name" label="承兑人名称" maxlength="80" required="false" />
			<emp:date id="IqpFastCreditDscnt.dscnt_date" label="贴现日期" required="false" />
			<emp:text id="IqpFastCreditDscnt.dscnt_day" label="贴现天数" maxlength="38" required="false" />
			<emp:text id="IqpFastCreditDscnt.arrangr_deduct" label="预扣款项" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpFastCreditDscnt.pay_amt" label="实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpFastCreditDscnt.biz_settl_mode" label="原业务结算方式" required="false" />
			<emp:date id="IqpFastCreditDscnt.fount_fin_advice" label="原国业部融资意见" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

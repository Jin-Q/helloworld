<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doOnLoad(){
		var optionJosn = "0,1,2";
		var options4ACT =IqpTrustFeeInfo.am_charge_term._obj.element.options;
		var options4LCT =IqpTrustFeeInfo.lm_charge_term._obj.element.options;
		for ( var i = options4ACT.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options4ACT[i].value)<0){
				options4ACT.remove(i);
			}
		} 
		for ( var i = options4LCT.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options4LCT[i].value)<0){
				options4LCT.remove(i);
			}
		}
		var serno = IqpTrustFeeInfo.serno._getValue();
		if(serno ==null || serno==""){
			IqpTrustFeeInfo.serno._setValue('${context.sernoStr}');
		}
   };		
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:gridLayout id="IqpTrustFeeInfoGroup" title="信托贷款费用信息" maxColumn="2">
		<emp:text id="IqpTrustFeeInfo.serno" label="业务流水号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="IqpTrustFeeInfo.am_fee_rate" label="安排撮合费率（年）" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpTrustFeeInfo.am_charge_term" label="安排撮合费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" />
			<emp:text id="IqpTrustFeeInfo.lm_fee_rate" label="贷款管理费率（年）" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpTrustFeeInfo.lm_charge_term" label="贷款管理费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
			<emp:text id="IqpTrustFeeInfo.mm_fee_rate" label="委托管理费率（年）" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpTrustFeeInfo.mm_charge_term" label="委托管理费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" readonly="true"/>
			<emp:text id="IqpTrustFeeInfo.bailee_pay_ratio" label="信托计划受托人支付比例" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpTrustFeeInfo.bailee_pay_amt" label="信托计划受托人支付金额" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
	</emp:gridLayout>
</body>
</html>
</emp:page>
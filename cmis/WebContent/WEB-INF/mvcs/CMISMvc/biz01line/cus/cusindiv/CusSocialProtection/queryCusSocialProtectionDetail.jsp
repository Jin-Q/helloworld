<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>社会保障信息</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var paramStr="CusSocialProtection.cus_id="+CusSocialProtection.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusSocialProtectionList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusSocialProtectionGroup" title="社会保障信息表" maxColumn="2">
		<emp:date id="CusSocialProtection.time_interzone" label="参保时间" required="true" />
		<emp:select id="CusSocialProtection.with_cust_rela" label="与客户关系" dictname="STD_ZB_INDIV_CUS" required="true" defvalue="1"/>
		<emp:text id="CusSocialProtection.social_prot_id" label="社会保障号码" maxlength="40" required="true" />
		<emp:text id="CusSocialProtection.provid_fund_id" label="公积金账号" maxlength="40" required="true" />
		<emp:text id="CusSocialProtection.ent_provid_fund_id" label="单位公积金账号" maxlength="40" required="true" />
		<emp:text id="CusSocialProtection.provid_fund_pay_monthly" label="公积金月缴额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusSocialProtection.provid_fund_bal" label="公积金余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusSocialProtection.supp_pay_monthly" label="补充月缴额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusSocialProtection.supp_provid_fund_bal" label="补充公积金余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusSocialProtection.family_pay_monthly_total" label="家庭月缴合计" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:checkbox id="CusSocialProtection.join_insur" label="参保情况" dictname="STD_JOIN_INSUR" colSpan="2" disabled="true" required="true"/>
		<emp:text id="CusSocialProtection.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
		<emp:text id="CusSocialProtection.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
		<emp:text id="CusSocialProtection.input_date" label="登记日期" maxlength="10" required="false" hidden="true" defvalue="$OPENDAY"/>
		<emp:text id="CusSocialProtection.serno" label="流水号" maxlength="40" required="false" hidden="true"/>
		<emp:text id="CusSocialProtection.cus_id" label="客户码" maxlength="30" required="true" hidden="true" />
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
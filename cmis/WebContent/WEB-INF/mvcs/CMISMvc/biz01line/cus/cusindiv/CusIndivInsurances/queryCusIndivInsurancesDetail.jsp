<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border:1px solid #BCD7E2;
	text-align:left;
	width:450px;
	background-color: #e3e3e3;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivInsu.cus_id="+CusIndivInsu.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusIndivInsurancesList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivInsuGroup" title="持有保险信息" maxColumn="2">
		<emp:text id="CusIndivInsu.indiv_ins_id" label="保险编号" maxlength="40" required="true" hidden="true"/>
		<emp:text id="CusIndivInsu.indiv_ins_cvg" label="保险名称" required="true" maxlength="80" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:text id="CusIndivInsu.indiv_ins_com" label="保险公司" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:select id="CusIndivInsu.indiv_ins_typ" label="保险种类" required="true" dictname="STD_ZB_INV_INS_TYP" />
		<emp:text id="CusIndivInsu.policyholders" label="投保人" maxlength="60" required="true" />
		<emp:text id="CusIndivInsu.beneficiaries" label="受益人" maxlength="60" required="true" />
		<emp:text id="CusIndivInsu.indiv_ins_sub" label="保险标的" maxlength="16" required="true"/>
		<emp:text id="CusIndivInsu.indiv_ins_val" label="保单现有价值(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="CusIndivInsu.indiv_ins_tot_amt" label="应缴保费总额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="CusIndivInsu.indiv_ins_fee" label="已缴保费(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="CusIndivInsu.indiv_ins_amt" label="保险金额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:date id="CusIndivInsu.indiv_ins_str_dt" label="投保日期" required="true" />
		<emp:date id="CusIndivInsu.indiv_ins_end_dt" label="到期日期" required="true" />
		<emp:text id="CusIndivInsu.indiv_ins_und" label="承保公司" maxlength="80" required="false" hidden="true"/>
		<emp:select id="CusIndivInsu.indiv_ins_status" label="抵押状况" required="true" dictname="STD_ZX_YES_NO" />
		<emp:textarea id="CusIndivInsu.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusIndivInsu.input_id_displayname" label="登记人"  hidden="true" />
		<emp:text id="CusIndivInsu.input_br_id_displayname" label="登记机构"  hidden="true" />
		<emp:date id="CusIndivInsu.input_date" label="登记日期" hidden="true"/>
		<emp:text id="CusIndivInsu.last_upd_id_displayname" label="更新人"  hidden="true"/>
		<emp:date id="CusIndivInsu.last_upd_date" label="更新日期" hidden="true"/>
		<emp:text id="CusIndivInsu.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		<emp:text id="CusIndivInsu.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
           <emp:text id="CusIndivInsu.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
           <emp:text id="CusIndivInsu.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
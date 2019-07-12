<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var cus_id  =CusObisLoan.cus_id._obj.element.value;
		var paramStr="CusObisLoan.cus_id="+cus_id;
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="queryCusObisLoanList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="#" method="POST">
		<emp:gridLayout id="CusObisLoanGroup" title="他行交易－他行贷款" maxColumn="2">
			<emp:text id="CusObisLoan.seq" label="序号" maxlength="38" readonly="true" hidden="true"/>
			<emp:text id="CusObisLoan.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:select id="CusObisLoan.loan_typ" label="业务品种" required="true" dictname="STD_ZB_OTHERPRO_TYPE"/>
			<emp:text id="CusObisLoan.org_name" label="开户机构名称" maxlength="80" required="true" />
			<emp:text id="CusObisLoan.cont_no" label="合同号" maxlength="40" required="false" />
			<emp:text id="CusObisLoan.loan_no" label="借据号" maxlength="40" required="false" />
			<emp:select id="CusObisLoan.cont_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY" />
			<emp:text id="CusObisLoan.exchange_rate" label="汇率" required="true" colSpan="2"/>
			<emp:text id="CusObisLoan.cont_amt" label="合同金额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.cont_amt_cny" label="合同金额折算成人民币(元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.loan_blc" label="余额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.loan_blc_cny" label="余额折算成人民币(元)" maxlength="18" required="true" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.cont_rate" label="月利率" required="true" dataType="Rate4Month" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.interest_blc1" label="表内利息余额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.interest_blc1_cny" label="表内利息余额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.interest_blc2" label="表外利息余额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.interest_blc2_cny" label="表外利息余额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.int_overdue_amt" label="逾期金额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.int_overdue_amt_cny" label="逾期金额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.int_overdue_times" label="逾期期数" maxlength="16" required="false" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.gty_perc" label="保证金比例" maxlength="10" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusObisLoan.gty_main_typ" label="主要担保方式" required="true" dictname="G_GUIDE_TYPE" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="CusObisLoan.dbwlx" label="担保物(人)类型" required="true" dictname="STD_ZB_DBWLX" colSpan="2"/>
			<emp:date id="CusObisLoan.loan_str_dt" label="起始日期" required="false" />
			<emp:date id="CusObisLoan.loan_end_dt" label="到期日期" required="false" />
			<emp:text id="CusObisLoan.extend_tm" label="展期次数" maxlength="38" required="false" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.refinance_tm" label="借新还旧次数" maxlength="38" required="false" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusObisLoan.loan_form4" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="CusObisLoan.loan_form5" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="CusObisLoan.law_suit_flg" label="诉讼状态" required="false" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="CusObisLoan.valid_flg" label="有效标志" required="true" dictname="STD_ZB_STATUS" defvalue="1"/>
			<emp:textarea id="CusObisLoan.remarks" label="备注" maxlength="200" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 200)"/>
			<emp:select id="CusObisLoan.cus_typ" label="客户类型" required="false" dictname="STD_ZB_INVESTOR2" hidden="true"/>
			<emp:text id="CusObisLoan.cus_bch_id" label="开户机构代码" maxlength="20" required="false" hidden="true" />
			<emp:text id="CusObisLoan.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusObisLoan.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusObisLoan.input_br_id" label="登记机构" required="false" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusObisLoan.last_upd_id" label="更新人" required="false" hidden="true" />
			<emp:date id="CusObisLoan.last_upd_date" label="更新日期" required="false" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

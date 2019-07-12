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
		var url = '<emp:url action="queryAccLoan1List.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="AccLoan1Group" title="ACC_LOAN" maxColumn="2">
			<emp:text id="AccLoan1.bill_no" label="BILL_NO" maxlength="30" required="true" />
			<emp:text id="AccLoan1.cont_no" label="CONT_NO" maxlength="30" required="false" />
			<emp:text id="AccLoan1.cn_cont_no" label="CN_CONT_NO" maxlength="40" required="false" />
			<emp:text id="AccLoan1.prd_pk" label="PRD_PK" maxlength="32" required="false" />
			<emp:text id="AccLoan1.biz_type" label="BIZ_TYPE" maxlength="6" required="false" />
			<emp:text id="AccLoan1.prd_name" label="PRD_NAME" maxlength="60" required="false" />
			<emp:text id="AccLoan1.prd_type" label="PRD_TYPE" maxlength="2" required="false" />
			<emp:text id="AccLoan1.cus_id" label="CUS_ID" maxlength="30" required="false" />
			<emp:text id="AccLoan1.cus_name" label="CUS_NAME" maxlength="60" required="false" />
			<emp:text id="AccLoan1.biz_type_sub" label="BIZ_TYPE_SUB" maxlength="8" required="false" />
			<emp:text id="AccLoan1.account_class" label="ACCOUNT_CLASS" maxlength="10" required="false" />
			<emp:text id="AccLoan1.loan_account" label="LOAN_ACCOUNT" maxlength="32" required="false" />
			<emp:text id="AccLoan1.loan_form" label="LOAN_FORM" maxlength="1" required="false" />
			<emp:text id="AccLoan1.loan_nature" label="LOAN_NATURE" maxlength="2" required="false" />
			<emp:text id="AccLoan1.loan_type_ext" label="LOAN_TYPE_EXT" maxlength="2" required="false" />
			<emp:text id="AccLoan1.assure_means_main" label="ASSURE_MEANS_MAIN" maxlength="2" required="false" />
			<emp:text id="AccLoan1.assure_means2" label="ASSURE_MEANS2" maxlength="2" required="false" />
			<emp:text id="AccLoan1.assure_means3" label="ASSURE_MEANS3" maxlength="2" required="false" />
			<emp:text id="AccLoan1.cur_type" label="CUR_TYPE" maxlength="3" required="false" />
			<emp:text id="AccLoan1.loan_amount" label="LOAN_AMOUNT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.loan_balance" label="LOAN_BALANCE" maxlength="16" required="false" />
			<emp:text id="AccLoan1.loan_start_date" label="LOAN_START_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.loan_end_date" label="LOAN_END_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.term_type" label="TERM_TYPE" maxlength="1" required="false" />
			<emp:text id="AccLoan1.orig_expi_date" label="ORIG_EXPI_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.ruling_ir" label="RULING_IR" maxlength="10" required="false" />
			<emp:text id="AccLoan1.floating_rate" label="FLOATING_RATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.reality_ir_y" label="REALITY_IR_Y" maxlength="10" required="false" />
			<emp:text id="AccLoan1.overdue_rate" label="OVERDUE_RATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.overdue_ir" label="OVERDUE_IR" maxlength="10" required="false" />
			<emp:text id="AccLoan1.default_rate" label="DEFAULT_RATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.default_ir" label="DEFAULT_IR" maxlength="10" required="false" />
			<emp:text id="AccLoan1.ci_rate" label="CI_RATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.ci_ir" label="CI_IR" maxlength="10" required="false" />
			<emp:text id="AccLoan1.rece_int_cumu" label="RECE_INT_CUMU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.actual_int_cumu" label="ACTUAL_INT_CUMU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.delay_int_cumu" label="DELAY_INT_CUMU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.inner_int_cumu" label="INNER_INT_CUMU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.off_int_cumu" label="OFF_INT_CUMU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.inner_rece_int" label="INNER_RECE_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.overdue_rece_int" label="OVERDUE_RECE_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.off_rece_int" label="OFF_RECE_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.compound_rece_int" label="COMPOUND_RECE_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.inner_off_rece_int" label="INNER_OFF_RECE_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.inner_actl_int" label="INNER_ACTL_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.overdue_actl_int" label="OVERDUE_ACTL_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.off_actl_int" label="OFF_ACTL_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.compound_actl_int" label="COMPOUND_ACTL_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.inner_off_actl_int" label="INNER_OFF_ACTL_INT" maxlength="16" required="false" />
			<emp:text id="AccLoan1.normal_balance" label="NORMAL_BALANCE" maxlength="16" required="false" />
			<emp:text id="AccLoan1.overdue_balance" label="OVERDUE_BALANCE" maxlength="16" required="false" />
			<emp:text id="AccLoan1.sluggish_balance" label="SLUGGISH_BALANCE" maxlength="16" required="false" />
			<emp:text id="AccLoan1.doubtful_balance" label="DOUBTFUL_BALANCE" maxlength="16" required="false" />
			<emp:text id="AccLoan1.integral_y" label="INTEGRAL_Y" maxlength="16" required="false" />
			<emp:text id="AccLoan1.integral_q" label="INTEGRAL_Q" maxlength="16" required="false" />
			<emp:text id="AccLoan1.integral_m" label="INTEGRAL_M" maxlength="16" required="false" />
			<emp:text id="AccLoan1.nor_rec_accu" label="NOR_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.reo_rec_accu" label="REO_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.peel_rec_accu" label="PEEL_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.asset_rec_accu" label="ASSET_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.assure_rec_accu" label="ASSURE_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.cancel_rec_accu" label="CANCEL_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.policy_rec_accu" label="POLICY_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.dte_rec_accu" label="DTE_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.roll_rec_accu" label="ROLL_REC_ACCU" maxlength="16" required="false" />
			<emp:text id="AccLoan1.max_balance_y" label="MAX_BALANCE_Y" maxlength="16" required="false" />
			<emp:text id="AccLoan1.max_balance_q" label="MAX_BALANCE_Q" maxlength="16" required="false" />
			<emp:text id="AccLoan1.max_balance_m" label="MAX_BALANCE_M" maxlength="16" required="false" />
			<emp:text id="AccLoan1.mortgage_flg" label="MORTGAGE_FLG" maxlength="1" required="false" />
			<emp:text id="AccLoan1.repayment_mode" label="REPAYMENT_MODE" maxlength="3" required="false" />
			<emp:text id="AccLoan1.first_disb_date" label="FIRST_DISB_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.loan_direction" label="LOAN_DIRECTION" maxlength="4" required="false" />
			<emp:text id="AccLoan1.revolving_times" label="REVOLVING_TIMES" maxlength="38" required="false" />
			<emp:text id="AccLoan1.extension_times" label="EXTENSION_TIMES" maxlength="38" required="false" />
			<emp:text id="AccLoan1.cap_overdue_date" label="CAP_OVERDUE_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.int_overdue_date" label="INT_OVERDUE_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.over_times_current" label="OVER_TIMES_CURRENT" maxlength="38" required="false" />
			<emp:text id="AccLoan1.over_times_total" label="OVER_TIMES_TOTAL" maxlength="38" required="false" />
			<emp:text id="AccLoan1.max_times_total" label="MAX_TIMES_TOTAL" maxlength="38" required="false" />
			<emp:text id="AccLoan1.bad_loan_flag" label="BAD_LOAN_FLAG" maxlength="1" required="false" />
			<emp:text id="AccLoan1.default_flag" label="DEFAULT_FLAG" maxlength="1" required="false" />
			<emp:text id="AccLoan1.limit_ind" label="LIMIT_IND" maxlength="1" required="false" />
			<emp:text id="AccLoan1.loan_form4" label="LOAN_FORM4" maxlength="2" required="false" />
			<emp:text id="AccLoan1.cla" label="CLA" maxlength="2" required="false" />
			<emp:text id="AccLoan1.cla_date" label="CLA_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.cla_pre" label="CLA_PRE" maxlength="2" required="false" />
			<emp:text id="AccLoan1.cla_date_pre" label="CLA_DATE_PRE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.latest_repay_date" label="LATEST_REPAY_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.cus_manager" label="CUS_MANAGER" maxlength="20" required="false" />
			<emp:text id="AccLoan1.input_br_id" label="INPUT_BR_ID" maxlength="20" required="false" />
			<emp:text id="AccLoan1.fina_br_id" label="FINA_BR_ID" maxlength="20" required="false" />
			<emp:text id="AccLoan1.main_br_id" label="MAIN_BR_ID" maxlength="20" required="false" />
			<emp:text id="AccLoan1.settl_date" label="SETTL_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.latest_date" label="LATEST_DATE" maxlength="10" required="false" />
			<emp:text id="AccLoan1.account_status" label="ACCOUNT_STATUS" maxlength="1" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
	
	<emp:form id="submitForm" action="addAccLoanRecord.do" method="POST">
		
		<emp:gridLayout id="AccLoanGroup" title="贷款台账" maxColumn="2">
			<emp:text id="AccLoan.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="AccLoan.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="AccLoan.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.loan_amt" label="贷款金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.loan_balance" label="贷款余额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccLoan.distr_date" label="发放日期" required="false" />
			<emp:date id="AccLoan.end_date" label="到期日期" required="false" />
			<emp:date id="AccLoan.ori_end_date" label="原到期日期" required="false" />
			<emp:text id="AccLoan.post_count" label="展期次数" maxlength="38" required="false" />
			<emp:text id="AccLoan.overdue" label="逾期期数" maxlength="38" required="false" />
			<emp:date id="AccLoan.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:text id="AccLoan.ruling_ir" label="基准利率" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="AccLoan.ir_float_rate" label="利率浮动比" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="AccLoan.ir_float_point" label="利率浮动点数" maxlength="10" required="false" dataType="Double" />
			<emp:text id="AccLoan.reality_ir_y" label="执行年利率（年）" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="AccLoan.comp_int_balance" label="复利余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.inner_owe_int" label="表内欠息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.out_owe_int" label="表外欠息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.rec_int_accum" label="应收利息累计" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.recv_int_accum" label="实收利息累计" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.normal_balance" label="正常余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.overdue_balance" label="逾期余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.slack_balance" label="呆滞余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccLoan.bad_dbt_balance" label="呆账余额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="AccLoan.twelve_cls_flg" label="十二级分类标志" required="false" />
			<emp:text id="AccLoan.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="AccLoan.fina_br_id" label="账务机构" maxlength="20" required="false" />
			<emp:text id="AccLoan.acc_day" label="日期" maxlength="10" required="true" />
			<emp:text id="AccLoan.acc_year" label="年份" maxlength="5" required="true" />
			<emp:text id="AccLoan.acc_mon" label="月份" maxlength="5" required="true" />
			<emp:date id="AccLoan.writeoff_date" label="核销日期" required="true" />
			<emp:date id="AccLoan.paydate" label="转垫款日" required="true" />
			<emp:select id="AccLoan.five_class" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:text id="AccLoan.acc_status" label="台账状态" maxlength="5" required="true" />
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


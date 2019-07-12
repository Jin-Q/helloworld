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
	
	<emp:form id="submitForm" action="addIqpDiscAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpDiscAppGroup" title="贴现申请从表" maxColumn="2">
			<emp:text id="IqpDiscApp.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpDiscApp.bill_type" label="票据种类" required="false" dictname="STD_BIZ_TYPE" />
			<emp:select id="IqpDiscApp.is_elec_bill" label="是否电子票据" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpDiscApp.disc_type" label="贴现类型" required="false" />
			<emp:text id="IqpDiscApp.disc_rate" label="贴现利息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpDiscApp.net_pay_amt" label="实付总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpDiscApp.bill_qty" label="票据数量" maxlength="38" required="false" />
			<emp:date id="IqpDiscApp.disc_date" label="贴现日期" required="false" />
			<emp:select id="IqpDiscApp.is_agent_disc" label="是否代理贴现" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDiscApp.agent_acct_no" label="代理人账户" maxlength="40" required="false" />
			<emp:text id="IqpDiscApp.agent_acct_name" label="代理人名称" maxlength="80" required="false" />
			<emp:pop id="IqpDiscApp.disc_sett_acct_no" label="贴现人结算账户" url="null" required="false" />
			<emp:text id="IqpDiscApp.disc_sett_acct_name" label="贴现人结算账户户名" maxlength="100" required="false" />
			<emp:textarea id="IqpDiscApp.pvp_pact_cond_memo" label="出账落实条件说明" maxlength="250" required="false" colSpan="2" />
			<emp:textarea id="IqpDiscApp.dscnt_int_pay_mode" label="贴现利息支付方式" maxlength="5" required="false" colSpan="2" />
			<emp:pop id="IqpDiscApp.pint_no" label="付息账号" url="null" required="false" />
			<emp:text id="IqpDiscApp.pint_acct_name" label="付息账户名" maxlength="80" required="false" />
			<emp:select id="IqpDiscApp.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpDiscApp.busdrft_dscnt_mode" label="商票贴现类型" required="true" />
			<emp:select id="IqpDiscApp.loan_type" label="贷款种类" required="false" dictname="STD_POSITION_TYPE" />
			<emp:pop id="IqpDiscApp.agri_loan_type" label="涉农贷款类型" url="null" required="false" />
			<emp:pop id="IqpDiscApp.loan_direction" label="贷款投向" url="null" required="false" />
			<emp:pop id="IqpDiscApp.loan_belong1" label="贷款归属1" url="null" required="false" />
			<emp:pop id="IqpDiscApp.loan_belong2" label="贷款归属2" url="null" required="false" />
			<emp:pop id="IqpDiscApp.loan_belong3" label="贷款归属3" url="null" required="false" />
			<emp:pop id="IqpDiscApp.loan_use" label="借款用途" url="null" required="false" />
			<emp:text id="IqpDiscApp.repay_src_des" label="还款来源" maxlength="250" required="false" />
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


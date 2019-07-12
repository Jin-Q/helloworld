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
	
	<emp:form id="submitForm" action="addIqpCreditChangeAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpCreditChangeAppGroup" title="原信用证信息" maxColumn="2">
			<emp:text id="IqpCreditChangeApp.serno" label="业务编号" maxlength="40" required="true" />
			<emp:date id="IqpCreditChangeApp.apply_date" label="申请日期" required="false" />
			<emp:text id="IqpCreditChangeApp.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="IqpCreditChangeApp.old_serno" label="原业务编号" maxlength="40" required="false" />
			<emp:text id="IqpCreditChangeApp.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="IqpCreditChangeApp.prd_id" label="产品编号" maxlength="6" required="false" />
			<emp:text id="IqpCreditChangeApp.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:select id="IqpCreditChangeApp.assure_main" label="担保方式" required="false" />
			<emp:select id="IqpCreditChangeApp.assure_main_details" label="担保方式细分" required="false" />
			<emp:select id="IqpCreditChangeApp.cont_cur_type" label="币种" required="false" />
			<emp:text id="IqpCreditChangeApp.exchange_rate" label="汇率" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpCreditChangeApp.cont_amt" label="开证金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="IqpCreditChangeApp.security_rate" label="保证金比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCreditChangeApp.same_security_amt" label="视同保证金" hidden="true" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="IqpCreditChangeApp.risk_open_amt" label="风险敞口金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="IqpCreditChangeApp.risk_open_rate" label="敞口比例" maxlength="10" required="false" dataType="Percent" />
			<emp:date id="IqpCreditChangeApp.cont_start_date" label="开证日期" required="false" />
			<emp:date id="IqpCreditChangeApp.cont_end_date" label="到期日期" required="false" />
			<emp:select id="IqpCreditChangeApp.credit_type" label="信用证类型" required="false" />
			<emp:select id="IqpCreditChangeApp.credit_term_type" label="信用证期限类型" required="false" />
			<emp:text id="IqpCreditChangeApp.chrg_rate" label="手续费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpCreditChangeApp.fast_day" label="远期天数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="IqpCreditChangeApp.floodact_perc" label="溢装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCreditChangeApp.shortact_perc" label="短装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:select id="IqpCreditChangeApp.is_revolv_credit" label="是否循环信用证" required="false" />
			<emp:select id="IqpCreditChangeApp.is_internal_cert" label="是否国内信用证" required="false" />
			<emp:select id="IqpCreditChangeApp.is_ctrl_gclaim" label="是否可控货权" required="false" />
			<emp:text id="IqpCreditChangeApp.busnes_cont" label="商务合同编号" maxlength="40" required="false" />
			<emp:text id="IqpCreditChangeApp.beneficiar" label="受益人" maxlength="80" required="false" />
			<emp:select id="IqpCreditChangeApp.beneficiar_country" label="受益人所在国家" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpCreditChangeAppGroup" title="信用证修改信息" maxColumn="2">
			<emp:text id="IqpCreditChangeApp.new_apply_amt" label="修改后信用证金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="IqpCreditChangeApp.new_security_rate" label="修改后保证金比例" maxlength="10" required="false" dataType="Percent" />
			<emp:select id="IqpCreditChangeApp.new_assure_main" label="修改后担保方式" required="false" />
			<emp:select id="IqpCreditChangeApp.new_assure_main_details" label="修改后担保方式细分" required="false" />
			<emp:text id="IqpCreditChangeApp.new_floodact_perc" label="修改后溢装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpCreditChangeApp.new_shortact_perc" label="修改后短装比例" maxlength="10" required="false" dataType="Percent" />
			<emp:select id="IqpCreditChangeApp.new_credit_term_type" label="修改后信用证期限类型" required="false" />
			<emp:text id="IqpCreditChangeApp.new_fast_day" label="修改后远期天数" maxlength="38" required="false" dataType="Int" />
			<emp:date id="IqpCreditChangeApp.new_cont_end_date" label="修改后信用证到期日" required="false" />
			<emp:textarea id="IqpCreditChangeApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpCreditChangeAppGroup" title="登记信息" maxColumn="2">
			<emp:text id="IqpCreditChangeApp.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="IqpCreditChangeApp.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="IqpCreditChangeApp.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="IqpCreditChangeApp.input_date" label="登记日期" maxlength="10" required="false" />
			<emp:select id="IqpCreditChangeApp.approve_status" label="申请状态" required="false" />
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


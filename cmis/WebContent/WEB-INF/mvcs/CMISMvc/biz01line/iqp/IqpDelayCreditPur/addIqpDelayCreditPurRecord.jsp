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
	
	<emp:form id="submitForm" action="addIqpDelayCreditPurRecord.do" method="POST">
		
		<emp:gridLayout id="IqpDelayCreditPurGroup" title="延期信用证项下应收款买入从表" maxColumn="2">
			<emp:text id="IqpDelayCreditPur.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpDelayCreditPur.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDelayCreditPur.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpDelayCreditPur.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpDelayCreditPur.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:text id="IqpDelayCreditPur.fin_day" label="融资天数" maxlength="38" required="false" />
			<emp:text id="IqpDelayCreditPur.bank_bp_no" label="我行bp号" maxlength="40" required="false" />
			<emp:textarea id="IqpDelayCreditPur.fount_fin_advice" label="国业部融资意见" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpDelayCreditPur.arrangr_deduct" label="预扣款项" maxlength="16" required="false" />
			<emp:text id="IqpDelayCreditPur.pay_amt" label="实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpDelayCreditPur.rece_cur_type" label="应收款币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpDelayCreditPur.rece_amt" label="应收款金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="IqpDelayCreditPur.rece_end_date" label="应收款到期日" required="false" />
			<emp:text id="IqpDelayCreditPur.promissory_pyr_name" label="承诺付款人名称" maxlength="80" required="false" />
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


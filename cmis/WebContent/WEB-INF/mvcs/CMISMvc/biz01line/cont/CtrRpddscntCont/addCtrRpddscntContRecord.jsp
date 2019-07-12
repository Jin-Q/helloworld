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
	
	<emp:form id="submitForm" action="addCtrRpddscntContRecord.do" method="POST">
		
		<emp:gridLayout id="CtrRpddscntContGroup" title="转贴现合同表" maxColumn="2">
			<emp:text id="CtrRpddscntCont.serno" label="业务编号" maxlength="40" required="false" />
			<emp:text id="CtrRpddscntCont.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="CtrRpddscntCont.batch_no" label="批次号" maxlength="40" required="false" />
			<emp:text id="CtrRpddscntCont.prd_id" label="产品编码" maxlength="6" required="false" />
			<emp:select id="CtrRpddscntCont.rpddscnt_type" label="转贴现方式" required="false" dictname="STD_ZB_BUSI_TYPE" />
			<emp:pop id="CtrRpddscntCont.toorg_no" label="交易对手行号" url="null" required="false" />
			<emp:text id="CtrRpddscntCont.toorg_name" label="交易对手行名" maxlength="100" required="false" />
			<emp:text id="CtrRpddscntCont.topp_acct_name" label="交易对手户名" maxlength="40" required="false" />
			<emp:pop id="CtrRpddscntCont.tooorg_no" label="交易对手开户行行号" url="null" required="false" />
			<emp:text id="CtrRpddscntCont.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" />
			<emp:text id="CtrRpddscntCont.this_acct_no" label="本行账户" maxlength="40" required="false" />
			<emp:text id="CtrRpddscntCont.this_acct_name" label="本行账户名" maxlength="100" required="false" />
			<emp:pop id="CtrRpddscntCont.acctsvcr_no" label="开户行行号" url="null" required="false" />
			<emp:text id="CtrRpddscntCont.acctsvcr_name" label="开户行行名" maxlength="100" required="false" />
			<emp:select id="CtrRpddscntCont.bill_type" label="票据种类" required="false" dictname="STD_DRFT_TYPE" />
			<emp:select id="CtrRpddscntCont.bill_curr" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CtrRpddscntCont.bill_total_amt" label="票据总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrRpddscntCont.bill_qnt" label="票据数量" maxlength="38" required="false" />
			<emp:date id="CtrRpddscntCont.rpddscnt_date" label="转贴现日期" required="false" />
			<emp:text id="CtrRpddscntCont.rpddscnt_rate" label="转贴现利率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="CtrRpddscntCont.rpddscnt_int" label="总贴现利息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrRpddscntCont.rpay_amt" label="总实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="CtrRpddscntCont.rebuy_date" label="回购日期" required="false" />
			<emp:text id="CtrRpddscntCont.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="CtrRpddscntCont.rebuy_int" label="总回购利息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrRpddscntCont.rebuy_amt" label="总回购金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrRpddscntCont.memo" label="备注" maxlength="250" required="false" />
			<emp:pop id="CtrRpddscntCont.manager_br_id" label="管理机构" url="null" required="false" />
			<emp:pop id="CtrRpddscntCont.input_id" label="登记人" url="null" required="false" />
			<emp:pop id="CtrRpddscntCont.input_br_id" label="登记机构" url="null" required="false" />
			<emp:date id="CtrRpddscntCont.input_date" label="登记日期" required="false" />
			<emp:select id="CtrRpddscntCont.cont_status" label="合同状态" required="false" dictname="STD_ZB_CTRLOANCONT_TYPE" />
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


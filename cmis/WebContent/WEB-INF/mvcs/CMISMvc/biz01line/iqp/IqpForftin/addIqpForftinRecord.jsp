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
	
	<emp:form id="submitForm" action="addIqpForftinRecord.do" method="POST">
		
		<emp:gridLayout id="IqpForftinGroup" title="福费廷从表" maxColumn="2">
			<emp:text id="IqpForftin.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpForftin.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpForftin.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpForftin.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpForftin.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:text id="IqpForftin.porder_no" label="汇票号码" maxlength="40" required="false" />
			<emp:select id="IqpForftin.bill_cur_type" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpForftin.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="IqpForftin.issue_date" label="出票日期" required="false" />
			<emp:date id="IqpForftin.bill_end_date" label="票据到期日" required="false" />
			<emp:text id="IqpForftin.drwr_name" label="出票人名称" maxlength="80" required="false" />
			<emp:text id="IqpForftin.accptr_name" label="承兑人名称" maxlength="80" required="false" />
			<emp:date id="IqpForftin.disc_date" label="贴现日期" required="false" />
			<emp:text id="IqpForftin.disc_day" label="贴现天数" maxlength="38" required="false" />
			<emp:text id="IqpForftin.arrangr_deduct_opt" label="预扣款项" maxlength="16" required="false" />
			<emp:text id="IqpForftin.pay_amt" label="实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpForftin.biz_settl_mode" label="原业务结算方式" required="false" />
			<emp:text id="IqpForftin.fount_fin_advice" label="原国业部融资意见" maxlength="250" required="false" />
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


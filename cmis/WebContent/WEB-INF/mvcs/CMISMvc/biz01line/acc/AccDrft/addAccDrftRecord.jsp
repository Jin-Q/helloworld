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
	
	<emp:form id="submitForm" action="addAccDrftRecord.do" method="POST">
		
		<emp:gridLayout id="AccDrftGroup" title="票据流水台帐" maxColumn="2">
			<emp:text id="AccDrft.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="AccDrft.acc_day" label="日期" maxlength="10" required="false" />
			<emp:text id="AccDrft.acc_year" label="年份" maxlength="5" required="false" />
			<emp:text id="AccDrft.acc_mon" label="月份" maxlength="5" required="false" />
			<emp:text id="AccDrft.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccDrft.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccDrft.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:select id="AccDrft.dscnt_type" label="贴现方式" required="false" dictname="STD_ZB_RPDDSCNT_MODE" />
			<emp:text id="AccDrft.porder_no" label="汇票号码" maxlength="40" required="false" />
			<emp:text id="AccDrft.discount_per" label="贴现人/交易对手" maxlength="40" required="false" />
			<emp:date id="AccDrft.dscnt_date" label="贴现日" required="false" />
			<emp:text id="AccDrft.dscnt_day" label="贴现天数" maxlength="10" required="false" />
			<emp:text id="AccDrft.adjust_day" label="调整天数" maxlength="10" required="false" />
			<emp:text id="AccDrft.dscnt_rate" label="贴现利率" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="AccDrft.cur_type" label="交易币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccDrft.dscnt_int" label="贴现利息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccDrft.rpay_amt" label="实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccDrft.rebuy_date" label="回购日期" required="false" />
			<emp:text id="AccDrft.rebuy_day" label="回购天数" maxlength="10" required="false" />
			<emp:text id="AccDrft.rebuy_rate" label="回购利率" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="AccDrft.overdue_rebuy_rate" label="逾期回购利率" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="AccDrft.rebuy_int" label="回购利息" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccDrft.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccDrft.writeoff_date" label="核销日期" required="false" />
			<emp:select id="AccDrft.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:text id="AccDrft.twelve_cls_flg" label="十二级分类标志" maxlength="5" required="false" />
			<emp:text id="AccDrft.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="AccDrft.fina_br_id" label="账务机构" maxlength="20" required="false" />
			<emp:text id="AccDrft.accp_status" label="台账状态" maxlength="5" required="false" />
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


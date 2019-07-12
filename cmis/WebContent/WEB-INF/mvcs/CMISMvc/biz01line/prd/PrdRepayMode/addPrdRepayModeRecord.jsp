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
	
	<emp:form id="submitForm" action="addPrdRepayModeRecord.do" method="POST">
		
		<emp:gridLayout id="PrdRepayModeGroup" title="还款方式" maxColumn="2">
			<emp:text id="PrdRepayMode.repay_mode_id" label="还款方式代码" maxlength="60" required="true" />
			<emp:text id="PrdRepayMode.repay_mode_dec" label="还款方式描述" maxlength="250" required="false" />
			<emp:select id="PrdRepayMode.repay_mode_type" label="还款方式种类" required="true" />
			<emp:text id="PrdRepayMode.min_term" label="支持最小期限(月)" maxlength="3" required="true" />
			<emp:text id="PrdRepayMode.max_term" label="支持最大期限(月)" maxlength="3" required="true" />
			<emp:text id="PrdRepayMode.incr_decl_basic" label="递增递减基础" maxlength="10" required="false" />
			<emp:text id="PrdRepayMode.cap_interval" label="本金间隔" maxlength="10" required="false" />
			<emp:text id="PrdRepayMode.incr_repay_cycle" label="递增还款周期" maxlength="10" required="false" />
			<emp:text id="PrdRepayMode.cap_incr_decl_perc" label="本金递增递减比例" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="PrdRepayMode.cap_incr_decl_amt" label="本金递增递减金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="PrdRepayMode.repay_interval" label="还款间隔" maxlength="10" required="false" />
			<emp:text id="PrdRepayMode.perc_phase" label="按比例设定阶段期数" maxlength="1" required="false" />
			<emp:text id="PrdRepayMode.cap_perc_unit" label="本金比例单位" maxlength="10" required="false" />
			<emp:text id="PrdRepayMode.firstpay_perc" label="首付比例" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="PrdRepayMode.lastpay_perc" label="尾付比例" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="PrdRepayMode.param_status" label="参数使用状态" maxlength="1" required="false" />
			<emp:text id="PrdRepayMode.change_time" label="最新变更时间" maxlength="20" required="false" />
			<emp:text id="PrdRepayMode.change_user" label="最新变更用户" maxlength="10" required="false" />
			<emp:select id="PrdRepayMode.is_instm" label="是否期供类" required="false" dictname="STD_ZX_YES_NO" />
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


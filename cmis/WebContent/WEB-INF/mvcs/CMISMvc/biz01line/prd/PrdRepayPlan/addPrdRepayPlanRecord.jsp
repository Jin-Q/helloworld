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
	
	<emp:form id="submitForm" action="addPrdRepayPlanRecord.do" method="POST">
		
		<emp:gridLayout id="PrdRepayPlanGroup" title="还款方式策略" maxColumn="2">
			<emp:text id="PrdRepayPlan.serno" label="序号" maxlength="30" required="true" />
			<emp:text id="PrdRepayPlan.repay_mode_id" label="还款方式代码" maxlength="10" required="true" />
			<emp:text id="PrdRepayPlan.exe_times" label="执行期数" maxlength="3" required="false" />
			<emp:text id="PrdRepayPlan.cap_perc" label="本金比例" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="PrdRepayPlan.cal_term" label="计算期限" maxlength="3" required="false" />
			<emp:text id="PrdRepayPlan.repay_mode" label="还款方式" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.repay_type" label="还款类型" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.int_cal_basic" label="利息计算基础" maxlength="10" required="false" />
			<emp:select id="PrdRepayPlan.is_update" label="是否允许修改" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="PrdRepayPlan.rate_type" label="利率类型" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="PrdRepayPlan.rate_mode" label="利率模式" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.rate_sprd" label="利差" maxlength="16" required="false" />
			<emp:text id="PrdRepayPlan.rate_pefloat" label="利率浮动比例" maxlength="16" required="false" dataType="Percent" />
			<emp:text id="PrdRepayPlan.rate_cal_basic" label="利率计算基础" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.new_change_time" label="最新变更时间" maxlength="20" required="false" />
			<emp:text id="PrdRepayPlan.new_change_user" label="最新变更用户" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.repay_trem" label="还款间隔周期" maxlength="10" required="false" />
			<emp:text id="PrdRepayPlan.repay_interval" label="还款间隔" maxlength="3" required="false" />
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


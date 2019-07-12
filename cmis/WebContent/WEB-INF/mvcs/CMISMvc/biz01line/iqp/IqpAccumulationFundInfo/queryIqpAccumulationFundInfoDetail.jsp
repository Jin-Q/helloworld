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
		var url = '<emp:url action="queryIqpAccumulationFundInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAccumulationFundInfoGroup" title="公积金贷款" maxColumn="2">
			<emp:text id="IqpAccumulationFundInfo.serno" label="业务流水号" maxlength="40" required="true" />
			<emp:text id="IqpAccumulationFundInfo.apply_amount" label="申请金额" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="IqpAccumulationFundInfo.apply_cur_type" label="申请币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:select id="IqpAccumulationFundInfo.ir_accord_type" label="利率依据方式" required="false" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="IqpAccumulationFundInfo.ir_type" label="利率种类" required="false" dictname="STD_ZB_RATE_TYPE" />
			<emp:select id="IqpAccumulationFundInfo.ir_adjust_type" label="利率调整方式" required="false" dictname="STD_IR_ADJUST_TYPE" />
			<emp:text id="IqpAccumulationFundInfo.ruling_ir" label="基准利率（年）" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpAccumulationFundInfo.ir_float_type" label="利率浮动方式" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAccumulationFundInfo.ir_float_rate" label="利率浮动比" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpAccumulationFundInfo.ir_float_point" label="利率浮动点数" maxlength="10" required="false" />
			<emp:text id="IqpAccumulationFundInfo.reality_ir_y" label="执行利率（年）" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpAccumulationFundInfo.overdue_float_type" label="逾期利率浮动方式" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAccumulationFundInfo.overdue_rate" label="逾期利率浮动比" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpAccumulationFundInfo.overdue_point" label="逾期利率浮动点" maxlength="10" required="false" />
			<emp:text id="IqpAccumulationFundInfo.overdue_rate_y" label="逾期利率（年）" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpAccumulationFundInfo.default_float_type" label="违约利率浮动方式" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpAccumulationFundInfo.default_rate" label="违约利率浮动比" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpAccumulationFundInfo.default_point" label="违约利率浮动点" maxlength="10" required="false" />
			<emp:text id="IqpAccumulationFundInfo.default_rate_y" label="违约利率（年）" maxlength="16" required="false" dataType="Rate" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

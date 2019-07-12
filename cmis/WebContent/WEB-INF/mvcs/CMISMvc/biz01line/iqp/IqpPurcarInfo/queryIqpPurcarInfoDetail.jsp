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
		var url = '<emp:url action="queryIqpPurcarInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpPurcarInfoGroup" title="机构法人购车信息" maxColumn="2">
			<emp:text id="IqpPurcarInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpPurcarInfo.car_sign" label="汽车品牌" maxlength="40" required="false" />
			<emp:text id="IqpPurcarInfo.car_name" label="汽车名称" maxlength="100" required="false" />
			<emp:text id="IqpPurcarInfo.car_model" label="汽车型号" maxlength="20" required="false" />
			<emp:text id="IqpPurcarInfo.car_no" label="车架号" maxlength="40" required="false" />
			<emp:select id="IqpPurcarInfo.car_type" label="汽车种类" required="false" />
			<emp:select id="IqpPurcarInfo.car_get_type" label="汽车取得方式" required="false" />
			<emp:text id="IqpPurcarInfo.car_use" label="汽车用途" maxlength="250" required="false" />
			<emp:text id="IqpPurcarInfo.car_seller" label="汽车销售商" maxlength="100" required="false" />
			<emp:text id="IqpPurcarInfo.buy_amt" label="购买金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpPurcarInfo.loan_rate" label="贷款比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpPurcarInfo.first_pay_rate" label="首付款比率" maxlength="10" required="false" dataType="Percent" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

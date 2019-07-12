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
	
	function doReturnnnnnnn() {
		window.history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusComCheckGroup" title="客户检查表" maxColumn="2">
			<emp:text id="CusComCheck.cus_id" label="客户码" maxlength="20" required="true" />
			<emp:text id="CusComCheck.stat_prd" label="月份" maxlength="20" required="true" />
			<emp:text id="CusComCheck.ele_cons" label="用电量(度)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.water_cons" label="用水量(吨)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.gas_cons" label="用气量(m³)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.per_tax" label="增值税(元)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.income_tax" label="所得税(元)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.check_person" label="检查人" maxlength="20" required="true" />
			<emp:text id="CusComCheck.check_date" label="日期" maxlength="20" required="false" />
			<emp:textarea id="CusComCheck.remarks" label="备注" maxlength="400" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="returnnnnnnn" label="返回"/>
	</div>
</body>
</html>
</emp:page>

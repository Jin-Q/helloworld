<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_select_select1 {
	width: 350px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<div align="center">
	<emp:gridLayout id="IqpOverseeManagerGroup" title="主要管理人员" maxColumn="1">			
			<emp:text id="IqpOverseeManager.name" label="姓名" maxlength="32" required="true" />
			<emp:select id="IqpOverseeManager.edu" label="学历" required="false" dictname="STD_ZX_EDU" cssFakeInputClass="emp_field_select_select1"/>
			<emp:select id="IqpOverseeManager.duty" label="职务" required="false" dictname="STD_ZX_DUTY" cssFakeInputClass="emp_field_select_select1"/>			
			<emp:text id="IqpOverseeManager.term" label="本公司工作（从业）年限" maxlength="10" required="false" />
			<emp:text id="IqpOverseeManager.manager_id" label="主要管理人员信息编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeManager.serno" label="业务流水号" maxlength="32" required="false" hidden="true"/>
	</emp:gridLayout>
	
	
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

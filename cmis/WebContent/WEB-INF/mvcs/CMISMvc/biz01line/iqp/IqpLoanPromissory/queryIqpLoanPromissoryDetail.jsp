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
		var url = '<emp:url action="queryIqpLoanPromissoryList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpLoanPromissoryGroup" title="贷款承诺" maxColumn="2">
			<emp:text id="IqpLoanPromissory.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpLoanPromissory.receiver" label="接收方" maxlength="40" required="false" />
			<emp:text id="IqpLoanPromissory.chrg_rate" label="手续费率" required="true" dataType="Rate"/>
			<emp:textarea id="IqpLoanPromissory.other_cond_need" label="其他条件和要求" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpLoanPromissory.item_name" label="项目名称" maxlength="100" required="false" />
			<emp:text id="IqpLoanPromissory.item_bground" label="项目背景" maxlength="250" required="false" />
			<emp:text id="IqpLoanPromissory.busnes_bground" label="贸易背景" maxlength="250" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

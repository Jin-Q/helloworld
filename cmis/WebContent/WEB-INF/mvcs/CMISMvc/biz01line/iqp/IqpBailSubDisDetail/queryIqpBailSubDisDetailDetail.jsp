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
		var url = '<emp:url action="queryIqpBailSubDisDetailList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpBailSubDisDetailGroup" title="保证金追加/提取明细" maxColumn="2">
			<emp:text id="IqpBailSubDisDetail.serno" label="业务编号" maxlength="60" required="true" />
			<emp:text id="IqpBailSubDisDetail.cont_no" label="合同编号" maxlength="60" required="true" />
			<emp:text id="IqpBailSubDisDetail.bail_acct_no" label="保证金账号" maxlength="60" required="false" />
			<emp:text id="IqpBailSubDisDetail.origi_bail_bal" label="原保证金余额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBailSubDisDetail.adjust_amt" label="追加/提取金额" maxlength="18" required="false" dataType="Currency" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
		var url = '<emp:url action="queryIqpAssetLawAdvicebookList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetLawAdvicebookGroup" title="法律意见书（资产证券化）" maxColumn="2">
			<emp:text id="IqpAssetLawAdvicebook.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetLawAdvicebook.law_office" label="律师事务所" maxlength="80" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.lawer" label="律师" maxlength="80" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.advice_date" label="意见提供日期" maxlength="10" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.advice_memo" label="意见摘要" maxlength="200" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

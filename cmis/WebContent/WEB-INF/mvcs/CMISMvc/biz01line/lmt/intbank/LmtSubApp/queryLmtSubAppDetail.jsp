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
		var url = '<emp:url action="queryLmtSubAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtSubAppGroup" title="授信分项申请表" maxColumn="2">
			<emp:text id="LmtSubApp.serno" label="业务编号" maxlength="32" required="true" />
			<emp:text id="LmtSubApp.variet_no" label="品种编号" maxlength="32" required="false" />
			<emp:text id="LmtSubApp.variet_name" label="品种名称" maxlength="32" required="false" />
			<emp:text id="LmtSubApp.lmt_amt" label="授信额度" maxlength="16" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

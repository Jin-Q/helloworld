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
		var url = '<emp:url action="queryRBusLmtcreditInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="RBusLmtcreditInfoGroup" title="业务和第三方授信关系表" maxColumn="2">
			<emp:text id="RBusLmtcreditInfo.agr_no" label="授信协议编号" maxlength="40" required="false" />
			<emp:select id="RBusLmtcreditInfo.lmt_type" label="授信类别" required="false" />
			<emp:text id="RBusLmtcreditInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="RBusLmtcreditInfo.cont_no" label="合同编号" maxlength="40" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
		var guaranty_no = MortGuarantySuddenInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantySuddenInfoList.do"/>?menuIdTab=mort_maintain&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="MortGuarantySuddenInfoGroup" title="记录抵质押品意外情况" maxColumn="2">
			<emp:text id="MortGuarantySuddenInfo.accident_no" label="意外情况编码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortGuarantySuddenInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="MortGuarantySuddenInfo.accident_type" label="意外情况类型" required="true" dictname="STD_ACCIDENT_INSU_TYPE" />
			<emp:date id="MortGuarantySuddenInfo.occur_date" label="发生日期" required="true" />
			<emp:textarea id="MortGuarantySuddenInfo.accident_resn" label="意外原因" maxlength="2000" required="true" colSpan="2" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

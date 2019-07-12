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
		var url = '<emp:url action="queryGrtLoanRGurList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">   
	
	<emp:gridLayout id="GrtLoanRGurGroup" title="业务和担保合同关系表" maxColumn="2">
			<emp:text id="GrtLoanRGur.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.guar_cont_no" label="担保合同编号" maxlength="40" required="true" />
			<emp:select id="GrtLoanRGur.is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO" required="true" />
			<emp:text id="GrtLoanRGur.guar_amt" label="本次担保金额" maxlength="18" dataType="Currency" required="true"/>
	</emp:gridLayout> 
</body>
</html>
</emp:page>

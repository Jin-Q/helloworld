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
		var url = '<emp:url action="queryCusFixHistoryList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusFixHistoryGroup" title="客户修改历史" maxColumn="2">
			<emp:text id="CusFixHistory.serno" label="流水号" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusFixHistory.input_id" label="登记人" maxlength="20" required="true" />
			<emp:text id="CusFixHistory.update_id" label="修改人" maxlength="20" required="true" />
			<emp:text id="CusFixHistory.update_type" label="修改类型" maxlength="4" required="true" />
			<emp:text id="CusFixHistory.memo" label="备注" maxlength="400" required="true" />
			<emp:text id="CusFixHistory.update_date" label="修改日期" maxlength="10" required="true" dataType="Date" />
			<emp:text id="CusFixHistory.checkcode" label="校验码" maxlength="20" required="true" />
			<emp:text id="CusFixHistory.input_br_id" label="登记机构" maxlength="20" required="true" />
			<emp:text id="CusFixHistory.cus_id" label="客户码" maxlength="20" required="true" />
			<emp:text id="CusFixHistory.cus_name" label="客户名称" maxlength="80" required="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

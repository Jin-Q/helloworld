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
		var url = '<emp:url action="queryCusTrusteeLstList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusTrusteeLstGroup" title="客户托管明细" maxColumn="2">
			<emp:text id="CusTrusteeLst.serno" label="申请流水号" maxlength="40" required="true" />
			<emp:text id="CusTrusteeLst.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="CusTrusteeLst.cus_name" label="客户名称" maxlength="80" required="true" />
			<emp:pop id="CusTrusteeLst.consignor_id" label="委托人" url="null" required="true" />
			<emp:pop id="CusTrusteeLst.consignor_br_id" label="委托机构" url="null" required="true" />
			<emp:pop id="CusTrusteeLst.trustee_id" label="托管人" url="null" required="true" />
			<emp:pop id="CusTrusteeLst.trustee_br_id" label="托管机构" url="null" required="true" />
			<emp:date id="CusTrusteeLst.trustee_date" label="托管日期" required="false" />
			<emp:date id="CusTrusteeLst.retract_date" label="收回日期" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

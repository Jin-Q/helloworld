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
		var url = '<emp:url action="queryPspCheckCatalogList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspCheckCatalogGroup" title="检查目录表" maxColumn="2">
			<emp:text id="PspCheckCatalog.catalog_id" label="目录编号" maxlength="40" required="true" />
			<emp:text id="PspCheckCatalog.catalog_name" label="目录名称" maxlength="100" required="false" />
			<emp:text id="PspCheckCatalog.memo" label="备注" maxlength="250" required="false" />
			<emp:text id="PspCheckCatalog.input_id" label="登记人" maxlength="20" required="false" />
			<emp:date id="PspCheckCatalog.input_date" label="登记日期" required="false" />
			<emp:text id="PspCheckCatalog.input_br_id" label="登记机构" maxlength="20" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

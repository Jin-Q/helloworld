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
	
	function doClose() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspCatItemRelGroup" title="检查目录和项目关系表" maxColumn="2">
			<emp:text id="PspCatItemRel.catalog_id" label="目录编号" maxlength="40" required="true" />
			<emp:text id="PspCatItemRel.item_id" label="项目编号" maxlength="40" required="true" />
			<emp:text id="PspCatItemRel.seq" label="排序" maxlength="38" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

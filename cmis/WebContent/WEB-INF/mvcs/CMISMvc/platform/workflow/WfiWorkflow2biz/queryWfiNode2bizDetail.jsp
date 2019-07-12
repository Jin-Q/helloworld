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

<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:510px;
}

</style>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfiNode2bizList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiNode2bizGroup" title="流程节点关联业务配置" maxColumn="2">
			<emp:text id="WfiNode2biz.pk1" label="关联配置主键" maxlength="40" required="true" />
			<emp:text id="WfiNode2biz.nodeid" label="节点ID" maxlength="32" required="true" />
			<emp:text id="WfiNode2biz.nodename" label="节点名称" maxlength="50" required="false" />
			<emp:text id="WfiNode2biz.app_url" label="节点自定义申请信息页面" maxlength="100" required="false" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiNode2biz.biz_url" label="节点自定义业务要素修改页面" maxlength="100" required="false" cssElementClass="emp_field_text_input2"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IndOptGroup" title="指标选项值配置" maxColumn="2">
			<emp:text id="IndOpt.index_no" label="指标编号" maxlength="12" required="true" colSpan="2"/>
			<emp:text id="IndOpt.index_value" label="指标选项值" maxlength="10" required="true" readonly="true" />
			<emp:text id="IndOpt.value_score" label="选项值得分" maxlength="16" required="false" />
			<emp:textarea id="IndOpt.ind_desc" label="指标描述" required="false" colSpan="2"/>
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

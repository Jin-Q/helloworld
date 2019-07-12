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
	<emp:gridLayout id="IndModelGroupGroup" title="模型指标组关联设置" maxColumn="2">
			<emp:text id="IndModelGroup.model_no" label="模型编号" maxlength="12" required="true" />
			<emp:text id="IndModelGroup.group_no" label="组别编号" maxlength="12" required="true" />
			<emp:text id="IndModelGroup.group_name" label="组别名称" maxlength="60" required="true" readonly="true"/>
			<emp:text id="IndModelGroup.weight" label="权重" maxlength="9" required="false" />
			<emp:text id="IndModelGroup.seqno" label="顺序号" maxlength="38" required="false" />
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

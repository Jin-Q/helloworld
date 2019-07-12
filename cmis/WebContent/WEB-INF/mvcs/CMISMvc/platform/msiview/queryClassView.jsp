<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title></title>
<style type="text/css">
.text_field_width2 {
	width: 500px;
	border-width: 1px;
	border-color: #e3e3e3;
	border-style: solid;
	text-align: left;
}

.text_field_width {
	width: 200px;
	border-width: 1px;
	border-color: #e3e3e3;
	border-style: solid;
	text-align: left;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
</script>
</head>
<body class="page_content" >

	<emp:gridLayout id="MsiClassViewGroup" title="服务集合" maxColumn="2">
		<emp:text id="MsiClassView.modual_id" label="模块ID" cssElementClass="text_field_width" required="true" readonly="true"/>
		<emp:text id="MsiClassView.modual_name" label="模块名称" cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiClassView.msi_class_id" label="服务ID"  cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiClassView.msi_class_desc" label="接口描述" cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiClassView.msi_class_name" label="接口名称" cssElementClass="text_field_width2" colSpan="2" readonly="true"/> 
	</emp:gridLayout>

	
</body>
</html>
</emp:page>
    
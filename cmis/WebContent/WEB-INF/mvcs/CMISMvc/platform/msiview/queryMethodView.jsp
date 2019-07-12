<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title></title>

<jsp:include page="/include.jsp" flush="true"/>


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

<script type="text/javascript">
	
</script>
</head>
<body class="page_content" >

	<emp:gridLayout id="MsiMethodViewGroup" title="服务信息" maxColumn="2">
		<emp:text id="MsiMethodView.msi_method_name" label="方法名" required="true" cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiMethodView.msi_method_desc" label="方法描述" cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiMethodView.msi_method_param_in" label="输入参数"  cssElementClass="text_field_width2" colSpan="2"  readonly="true"/>
		<emp:text id="MsiMethodView.msi_method_param_out" label="输出参数" cssElementClass="text_field_width2" colSpan="2"  readonly="true"/>
		<emp:text id="MsiMethodView.msi_method_type" label="服务类型"  cssElementClass="text_field_width" readonly="true"/>
		<emp:text id="MsiMethodView.msi_method_example" label="调用示例"  cssElementClass="text_field_width" readonly="true"/>
	</emp:gridLayout>

	
</body>
</html>
</emp:page>
    
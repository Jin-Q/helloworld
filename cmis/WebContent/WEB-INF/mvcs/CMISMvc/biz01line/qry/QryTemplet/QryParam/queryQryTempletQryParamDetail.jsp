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
	<emp:gridLayout id="QryParamGroup" title="查询条件参数配置表" maxColumn="2">
			<emp:text id="QryParam.temp_no" label="查询模板编号" maxlength="20" required="true" />
			<emp:text id="QryParam.param_no" label="条件参数编号" maxlength="20" required="true" />
			<emp:text id="QryParam.cnname" label="参数中文名称" maxlength="40" required="true" />
			<emp:text id="QryParam.enname" label="参数英文名称" maxlength="40" required="true" />
			<emp:select id="QryParam.param_type" label="条件参数类型" required="true" dictname="STD_ZB_PARAM_TYPE" />
			<emp:text id="QryParam.param_dic_no" label="参数选项字典编号" maxlength="20" required="false" />
			<emp:text id="QryParam.orderid" label="排序字段" maxlength="20" required="false" dataType="Int" />
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

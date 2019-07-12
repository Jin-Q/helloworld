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
	<emp:gridLayout id="QryResultGroup" title="查询返回值配置表" maxColumn="2">
			<emp:text id="QryResult.temp_no" label="查询模板编号" maxlength="20" required="true" />
			<emp:text id="QryResult.result_no" label="返回值编号" maxlength="20" required="true" />
			<emp:text id="QryResult.cnname" label="返回值标题名称" maxlength="60" required="false" />
			<emp:text id="QryResult.enname" label="列名称" maxlength="60" required="false" />
			<emp:text id="QryResult.enname2" label="别名" maxlength="80" required="false" />
			<emp:select id="QryResult.result_type" label="返回值类型" required="false" dictname="STD_ZB_QRYREST_TYPE" />
			<emp:text id="QryResult.result_title_displayname" label="参数选项字典编号"/>
			<emp:text id="QryResult.link_temp_no_displayname" label="内部链接" />
			<emp:text id="QryResult.orderid" label="排序字段" maxlength="20" required="false" dataType="Int"/>
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

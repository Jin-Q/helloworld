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
		var url = '<emp:url action="queryIqpGreenDeclInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpGreenDeclInfoGroup" title="绿色减排信息" maxColumn="2">
			<emp:text id="IqpGreenDeclInfo.serno" label="业务流水号" maxlength="40" required="true" />
			<emp:select id="IqpGreenDeclInfo.green_indus" label="绿色产业类型" required="false" dictname="STD_ZB_GREEN_INDUS" />
			<emp:text id="IqpGreenDeclInfo.reduc_coal" label="项目年节约标准煤量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.emission_co2" label="项目年减排二氧化碳量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.emission_cod" label="项目年COD减排量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.emission_an" label="项目年氨氮减排量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.emission_so2" label="项目二氧化硫减排量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.emission_no" label="项目年氮氧化物减排量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.reduc_water" label="项目年节水量" maxlength="16" required="false" />
			<emp:text id="IqpGreenDeclInfo.totl_invest" label="项目总投资额" maxlength="16" required="false" dataType="Currency" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

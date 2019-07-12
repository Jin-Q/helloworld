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
		var url = '<emp:url action="queryQryParamDicList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="QryParamDicGroup" title="查询条件字典配置表" maxColumn="2">
			<emp:text id="QryParamDic.param_dic_no" label="参数选项字典编号" maxlength="20" required="true" />
			<emp:text id="QryParamDic.name" label="字典名称" maxlength="30" required="false" />
			<emp:select id="QryParamDic.par_dic_type" label="参数类型" required="false" dictname="STD_ZB_PAR_DIC_TYPE" />
			<emp:text id="QryParamDic.opttype" label="参数选项字典编号" maxlength="20" required="false" />
			<emp:textarea id="QryParamDic.query_sql" label="查询SQL语句" maxlength="250" required="false" colSpan="2" hidden="true"/>
			<emp:textarea id="QryParamDic.popname" label="POP框信息" maxlength="250" required="false" colSpan="2"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

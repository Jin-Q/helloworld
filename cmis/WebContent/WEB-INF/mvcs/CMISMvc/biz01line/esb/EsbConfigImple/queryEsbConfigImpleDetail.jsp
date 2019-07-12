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
.emp_input{
border:1px solid #b7b7b7;
width:650px;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryEsbConfigImpleList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="EsbConfigImpleGroup" title="接口交易配置表" maxColumn="2">
			<emp:text id="EsbConfigImple.esb_id" label="主键" maxlength="40" hidden="true" required="false" colSpan="2" />
			<emp:text id="EsbConfigImple.service_code" label="交易码" maxlength="30" required="true" />
			<emp:text id="EsbConfigImple.service_sence" label="交易场景" maxlength="5" required="true" />
			<emp:text id="EsbConfigImple.imple_class" label="交易实现类" maxlength="300" required="true" cssElementClass="emp_input"/>
			<emp:textarea id="EsbConfigImple.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="EsbConfigImple.status" label="状态"  hidden="true"  dictname="STD_PRD_STATE" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

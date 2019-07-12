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
		var url = '<emp:url action="queryDaybatTrandInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="DaybatTrandInfoGroup" title="实时交易信息" maxColumn="2">
			<emp:text id="DaybatTrandInfo.pk1" label="主键" maxlength="40" required="false" colSpan="2" />
			<emp:text id="DaybatTrandInfo.consumer_seq_no" label="交易流水" maxlength="40" required="true" />
			<emp:text id="DaybatTrandInfo.service_code" label="交易码" maxlength="40" required="true" />
			<emp:text id="DaybatTrandInfo.service_scene" label="交易场景" maxlength="40" required="true" />
			<emp:text id="DaybatTrandInfo.tran_date" label="交易日期" maxlength="40" required="true" />
			<emp:textarea id="DaybatTrandInfo.locate_file" label="报文文件路径" required="true" colSpan="2" />
			<emp:textarea id="DaybatTrandInfo.trand_msg" label="错误信息" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

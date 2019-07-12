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
		var url = '<emp:url action="queryLmtBatchListLmtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtBatchListLmtGroup" title="批量名单授信" maxColumn="2">
			<emp:text id="LmtBatchListLmt.serno" label="业务编号" maxlength="32" required="true" />
			<emp:text id="LmtBatchListLmt.cus_id" label="客户码" maxlength="32" required="false" />
			<emp:text id="LmtBatchListLmt.cur_type" label="授信币种" maxlength="3" required="false" />
			<emp:text id="LmtBatchListLmt.lmt_amt" label="授信金额" maxlength="16" required="false" />
			<emp:text id="LmtBatchListLmt.term_type" label="期限类型" maxlength="3" required="false" />
			<emp:text id="LmtBatchListLmt.term" label="期限" maxlength="6" required="false" />
			<emp:text id="LmtBatchListLmt.memo" label="备注" maxlength="200" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetPrdEvalGroup" maxColumn="2" title="产品评级信息">
			<emp:text id="IqpAssetPrdEval.prd_id" label="产品代码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpAssetPrdEval.eval_org" label="评级机构" maxlength="20" required="false" />
			<emp:text id="IqpAssetPrdEval.cdt_eval" label="信用评级" maxlength="10" required="false" />
			<emp:date id="IqpAssetPrdEval.eval_date" label="评级日期" required="false" dataType="Date" />
			<emp:text id="IqpAssetPrdEval.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			function doReturnList(){ 
			   window.close();
			}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateIndLibIndOptRecord.do" method="POST">
		<emp:gridLayout id="IndOptGroup" title="指标选项值配置" maxColumn="2">
			<emp:text id="IndOpt.index_no" label="指标编号" maxlength="12" required="true" readonly="true" colSpan="2"/>
			<emp:text id="IndOpt.index_value" label="指标选项值" maxlength="10" required="true" readonly="true" />
			<emp:text id="IndOpt.value_score" label="选项值得分" maxlength="16" required="false" />
			<emp:textarea id="IndOpt.ind_desc" label="指标描述" required="false" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="update_IndOpt"/>
			<emp:button id="returnList" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

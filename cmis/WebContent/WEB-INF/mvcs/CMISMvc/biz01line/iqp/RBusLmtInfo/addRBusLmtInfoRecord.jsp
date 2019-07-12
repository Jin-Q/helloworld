<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addRBusLmtInfoRecord.do" method="POST">
		
		<emp:gridLayout id="RBusLmtInfoGroup" title="授信和业务关系" maxColumn="2">
			<emp:text id="RBusLmtInfo.agr_no" label="授信协议编号" maxlength="40" required="false" />
			<emp:text id="RBusLmtInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="RBusLmtInfo.cont_no" label="合同编号" maxlength="40" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


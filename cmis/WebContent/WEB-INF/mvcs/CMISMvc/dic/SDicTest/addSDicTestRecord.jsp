<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>鏂板椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSDicTestRecord.do" method="POST">
		
		<emp:gridLayout id="SDicTestGroup" title="单表维护测试" maxColumn="2">
			<emp:text id="SDicTest.enname" label="ENNAME" maxlength="50" required="true" />
			<emp:text id="SDicTest.cnname" label="CNNAME" maxlength="200" required="true" />
			<emp:text id="SDicTest.opttype" label="OPTTYPE" maxlength="50" required="true" />
			<emp:text id="SDicTest.memo" label="MEMO" maxlength="100" required="false" />
			<emp:text id="SDicTest.flag" label="FLAG" maxlength="3" required="false" />
			<emp:text id="SDicTest.levels" label="LEVELS" maxlength="2" required="false" />
			<emp:text id="SDicTest.orderid" label="ORDERID" maxlength="38" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="纭畾" op="add"/>
			<emp:button id="reset" label="鍙栨秷"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


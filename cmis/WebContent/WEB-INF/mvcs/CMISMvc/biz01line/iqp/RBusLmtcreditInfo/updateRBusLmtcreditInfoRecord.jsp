<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateRBusLmtcreditInfoRecord.do" method="POST">
		<emp:gridLayout id="RBusLmtcreditInfoGroup" maxColumn="2" title="业务和第三方授信关系表">
			<emp:text id="RBusLmtcreditInfo.agr_no" label="授信协议编号" maxlength="40" required="false" />
			<emp:select id="RBusLmtcreditInfo.lmt_type" label="授信类别" required="false" />
			<emp:text id="RBusLmtcreditInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="RBusLmtcreditInfo.cont_no" label="合同编号" maxlength="40" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

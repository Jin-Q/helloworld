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
	
	<emp:form id="submitForm" action="updateIqpNetCoreConRecord.do" method="POST">
		<emp:gridLayout id="IqpNetCoreConGroup" maxColumn="2" title="网络退/入网申请表">
			<emp:text id="IqpNetCoreCon.serno" label="申请编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpNetCoreCon.net_agr_no" label="网络编号" maxlength="40" required="true" />
			<emp:text id="IqpNetCoreCon.app_flag" label="申请标识(1.退网 0.入网)" maxlength="4" required="false" />
			<emp:text id="IqpNetCoreCon.manager_id" label="责任人" maxlength="20" required="false" />
			<emp:text id="IqpNetCoreCon.manager_br_id" label="责任机构" maxlength="20" required="false" />
			<emp:text id="IqpNetCoreCon.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="IqpNetCoreCon.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="IqpNetCoreCon.input_date" label="登记日期" maxlength="10" required="false" />
			<emp:text id="IqpNetCoreCon.approve_status" label="申请状态" maxlength="4" required="false" />
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

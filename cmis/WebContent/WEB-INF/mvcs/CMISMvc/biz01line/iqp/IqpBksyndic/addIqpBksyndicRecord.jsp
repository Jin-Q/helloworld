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
	
	<emp:form id="submitForm" action="addIqpBksyndicRecord.do" method="POST">
		<emp:gridLayout id="IqpBksyndicGroup" title="银团从表" maxColumn="2">
			<emp:text id="IqpBksyndic.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpBksyndic.bank_syndic_type" label="银团类型" required="false" />
			<emp:select id="IqpBksyndic.agent_org_flag" label="代理行标志" required="false" />
			<emp:text id="IqpBksyndic.bank_syndic_amt" label="银团贷款总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBksyndic.agent_rate" label="代理费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpBksyndic.amt_arra_rate" label="资金安排费率" maxlength="10" required="false" dataType="Rate" />
			<emp:textarea id="IqpBksyndic.bank_syndic_desc" label="银团项目描述" maxlength="250" required="false" colSpan="2" />
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

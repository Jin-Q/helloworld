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
	
	<emp:form id="submitForm" action="updateArpCollDebtReRecord.do" method="POST">
		<emp:gridLayout id="ArpCollDebtReGroup" maxColumn="2" title="抵债物关联表">
			<emp:text id="ArpCollDebtRe.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpCollDebtRe.debt_acc_no" label="抵债台账编号" maxlength="40" required="false" />
			<emp:text id="ArpCollDebtRe.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpCollDebtRe.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="ArpCollDebtRe.status" label="状态" required="false" dictname="STD_ZB_DEBT_RE_STATUS" />
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

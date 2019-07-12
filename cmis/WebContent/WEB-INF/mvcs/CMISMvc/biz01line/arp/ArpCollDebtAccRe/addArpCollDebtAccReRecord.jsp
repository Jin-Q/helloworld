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
	
	<emp:form id="submitForm" action="addArpCollDebtAccReRecord.do" method="POST">
		
		<emp:gridLayout id="ArpCollDebtAccReGroup" title="抵债物关联表(台账)" maxColumn="2">
			<emp:text id="ArpCollDebtAccRe.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="ArpCollDebtAccRe.debt_acc_no" label="抵债台账编号" maxlength="40" required="false" />
			<emp:text id="ArpCollDebtAccRe.guaranty_no" label="押品编号" maxlength="40" required="true" />
			<emp:text id="ArpCollDebtAccRe.debt_in_amt" label="抵入金额" maxlength="16" required="false" />
			<emp:select id="ArpCollDebtAccRe.status" label="状态" required="false" dictname="STD_ZB_DEBT_RE_STATUS" />
			<emp:text id="ArpCollDebtAccRe.rel" label="关系类型（1-引入已存在的，2-新增的抵债物）" maxlength="5" required="false" />
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


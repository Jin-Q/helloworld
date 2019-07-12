<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function returnCusId(data){
		CusBankRel.cus_id._setValue(data.cus_id._getValue()); 
		CusBankRel.cus_name._setValue(data.cus_name._getValue()); 
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusBankRelRecord.do" method="POST">
		<emp:gridLayout id="CusBankRelGroup" maxColumn="2" title="客户与我行关系">
			<emp:pop id="CusBankRel.cus_id" label="客户码" url="queryCusLoanRelList.do?cusTypCondition=''&returnMethod=returnCusId"  required="true" />
			<emp:text id="CusBankRel.cus_name" label="客户名称" maxlength="60" required="false" readonly="true"/>
			<emp:select id="CusBankRel.cus_bank_rel" label="客户与我行关系" dictname="STD_ZB_CUS_BANK" required="false" />
			<emp:date id="CusBankRel.reg_date" label="登记日期"  required="false" />
			<emp:text id="CusBankRel.reg_name" label="登记人" maxlength="60" required="false" hidden="true"/>
			<emp:text id="CusBankRel.reg_org" label="登记机构" maxlength="60" required="false" hidden="true"/>
			<emp:text id="CusBankRel.reg_name_displayname" label="登记人"  required="false" readonly="true"/>		
			<emp:text id="CusBankRel.reg_org_displayname" label="登记机构"  required="false" readonly="true" />
			<emp:text id="CusBankRel.cus_bank_rel_name" label="客户与我行关系名称" maxlength="60" required="false" hidden="true"/>
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

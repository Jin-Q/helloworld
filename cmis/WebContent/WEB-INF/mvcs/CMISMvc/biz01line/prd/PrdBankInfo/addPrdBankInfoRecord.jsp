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
	
	<emp:form id="submitForm" action="addPrdBankInfoRecord.do" method="POST">
		
		<emp:gridLayout id="PrdBankInfoGroup" title="银行信息" maxColumn="2">
			<emp:text id="PrdBankInfo.bank_no" label="行号" maxlength="40" required="true" />
			<emp:text id="PrdBankInfo.bank_name" label="行名" maxlength="100" required="false" />
			<emp:text id="PrdBankInfo.area_code" label="地区代码" maxlength="20" required="false" />
			<emp:text id="PrdBankInfo.phone" label="联系电话" maxlength="20" required="false" />
			<emp:text id="PrdBankInfo.pcode" label="邮政编码" maxlength="20" required="false" />
			<emp:text id="PrdBankInfo.addr" label="地址" maxlength="200" required="false" />
			<emp:text id="PrdBankInfo.last_bank_no" label="上级行号" maxlength="40" required="false" />
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


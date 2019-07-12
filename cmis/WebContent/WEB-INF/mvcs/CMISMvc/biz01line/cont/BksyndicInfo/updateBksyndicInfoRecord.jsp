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
	
	<emp:form id="submitForm" action="updateBksyndicInfoRecord.do" method="POST">
		<emp:gridLayout id="BksyndicInfoGroup" maxColumn="2" title="银团信息">
			<emp:text id="BksyndicInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="BksyndicInfo.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:pop id="BksyndicInfo.prtcpt_bank_no" label="参与行行号" url="null" required="false" />
			<emp:text id="BksyndicInfo.prtcpt_bank_name" label="参与行行名" maxlength="100" required="false" />
			<emp:select id="BksyndicInfo.is_bank" label="是否本行" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="BksyndicInfo.prtcpt_role" label="参与角色" required="false" />
			<emp:select id="BksyndicInfo.prtcpt _cur_type" label="参与币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="BksyndicInfo.prtcpt_amt_rate" label="参与金额比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="BksyndicInfo.prtcpt _amt" label="参与金额" maxlength="18" required="false" dataType="Currency" />
			<emp:pop id="BksyndicInfo.reaccept_int_no" label="代收利息账号" url="null" required="false" />
			<emp:text id="BksyndicInfo.reaccept_int_name" label="代收利息账户名" maxlength="100" required="false" />
			<emp:pop id="BksyndicInfo.bank_acct_no" label="银行账号" url="null" required="false" />
			<emp:text id="BksyndicInfo.bank_acct_name" label="银行账户名" maxlength="100" required="false" />
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

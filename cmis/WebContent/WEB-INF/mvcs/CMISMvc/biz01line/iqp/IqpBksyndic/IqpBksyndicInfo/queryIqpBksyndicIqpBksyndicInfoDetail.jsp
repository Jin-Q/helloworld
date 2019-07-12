<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css"> 
.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
.emp_field_input {
border: 1px solid #b7b7b7;
text-align:left;
width:200px;
}
</style> 
<script type="text/javascript">
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IqpBksyndicInfoGroup" title="银团信息" maxColumn="2">
			<emp:text id="IqpBksyndicInfo.serno" label="业务编号" hidden="true" maxlength="40" required="flase"/>
			<emp:pop id="IqpBksyndicInfo.prtcpt_org_no" label="参与行行号" url="" returnMethod="" required="true" />  
			<emp:text id="IqpBksyndicInfo.prtcpt_org_name" label="参与行行名" maxlength="100" readonly="true" required="true" colSpan="2" cssElementClass="emp_input2"/>  
			<emp:select id="IqpBksyndicInfo.prtcpt_role" label="参与角色" required="true" dictname="STD_PART_ORG_TYPE"/> 
			<emp:select id="IqpBksyndicInfo.prtcpt_curr" label="参与币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpBksyndicInfo.prtcpt_amt_rate" label="参与金额比例" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="IqpBksyndicInfo.prtcpt_amt" label="参与金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/> 
			<emp:text id="IqpBksyndicInfo.agent_int_acct_no" label="代收利息账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" />
			<emp:text id="IqpBksyndicInfo.agent_int_acct_name" label="代收利息账户名" maxlength="80" required="false" />
			<emp:text id="IqpBksyndicInfo.bank_acct_no" label="银行账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" />
			<emp:text id="IqpBksyndicInfo.bank_acct_name" label="银行账户名" maxlength="100" required="false" />
			<emp:text id="IqpBksyndicInfo.pk1" label="PK1" hidden="true" maxlength="40" required="false" /> 
			<emp:select id="IqpBksyndicInfo.is_this_org" label="是否本行" required="false" hidden="true" dictname="STD_ZX_YES_NO" />  
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

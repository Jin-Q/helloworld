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

<script type="text/javascript">
	
	function doReturn() {
		//var url = '<emp:url action="queryPspBankContaccList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspBankContaccGroup" title="银行对账单" maxColumn="2">
			<emp:text id="PspBankContacc.acct_no" label="账号" maxlength="40" required="true" />
			<emp:text id="PspBankContacc.acct_name" label="账户名" maxlength="100" required="true" />
			
			<emp:text id="PspBankContacc.acctsvcr_name" label="开户行行名" maxlength="100" required="true" colSpan="2"/>
			<emp:select id="PspBankContacc.orie_type" label="业务方向" required="true" dictname="STD_PSP_ORIE_TYPE"/>
			<emp:text id="PspBankContacc.qnt" label="笔数" maxlength="38" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PspBankContacc.amt" label="金额" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="PspBankContacc.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2"/>
			<emp:textarea id="PspBankContacc.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspBankContaccGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspBankContacc.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspBankContacc.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspBankContacc.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspBankContacc.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspBankContacc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspBankContacc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspBankContacc.task_id" label="任务编号" required="false" hidden="true"/>
			<emp:text id="PspBankContacc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

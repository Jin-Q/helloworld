<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		menuIds = '&subMenuId=Coll_Debt&op=${context.op}';
		var serno = ArpBusiDebtInfo.serno._getValue();
		var url = '<emp:url action="queryArpBusiDebtInfoList.do"/>?serno='+serno+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addContForm(ArpBusiDebtInfo);
		addBillForm(ArpBusiDebtInfo);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpBusiDebtInfoGroup" title="业务抵债信息表" maxColumn="2">
			<emp:text id="ArpBusiDebtInfo.bill_no" label="借据编号" maxlength="40" required="true" />
			<emp:text id="ArpBusiDebtInfo.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpBusiDebtInfo.loan_amt" label="贷款金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBusiDebtInfo.loan_balance" label="贷款余额" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.debt_cap" label="抵偿本金" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.debt_inner_int" label="抵偿表内利息" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.debt_out_int" label="抵偿表外利息" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.debt_other_expense" label="抵偿其他发生费用" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="ArpBusiDebtInfo.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

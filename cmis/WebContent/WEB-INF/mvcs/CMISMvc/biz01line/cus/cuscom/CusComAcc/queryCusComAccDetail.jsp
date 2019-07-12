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
	/*--user code begin--*/
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComAcc.cus_id._obj.element.value;
		var paramStr="CusComAcc.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComAccList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusComAccGroup" title="结算账户登记" maxColumn="2">
		<emp:text id="CusComAcc.cus_id" label="客户码" maxlength="30" required="true" />
		<emp:text id="CusComAcc.cus_id_displayname" label="客户名称"  required="false" />
		<emp:text id="CusComAcc.acc_no" label="结算账户帐号" maxlength="32" required="true" />
		<emp:text id="CusComAcc.acc_name" label="账户名称" maxlength="80" required="true" />
		<emp:date id="CusComAcc.acc_date" label="账户开户日期"  required="false" />
		<emp:select id="CusComAcc.acc_type" label="账号类型" dictname="STD_ZB_CUS_ACC_TYPE" />
		<emp:text id="CusComAcc.acc_open_org" label="开户机构" maxlength="20" required="false" />
		<emp:text id="CusComAcc.acc_open_orgname" label="开户机构名称" maxlength="80" required="false" />
		<emp:text id="CusComAcc.acc_org" label="核算机构" maxlength="20" required="false" />
		<emp:text id="CusComAcc.acc_orgname" label="核算机构名称" maxlength="80" required="false" />
	</emp:gridLayout>
	
	
		<div class='emp_gridlayout_title'>账户交易流水信息</div>
	<emp:table icollName="AcctTxnHistInfList" url="getAcctTxnHistInfListPage.do" reqParams="acc_no=${context.CusComAcc.acc_no}" pageMode="true">
	    <emp:text id="SeqNo" label="交易流水号" />
		<emp:text id="AcctTxnDt" label="交易日期" />
		<emp:select id="Ccy" label="币种"  dictname="STD_ZX_CUR_TYPE"  cssTDClass="tdCenter"/>
	    <emp:text id="TxnAmt" label="交易金额" dataType="Currency" cssTDClass="tdRight"/>
		<emp:text id="BfrTxnAcctBal" label="交易前账户余额"  dataType="Currency" cssTDClass="tdRight"/>
		<emp:text id="ActBal" label="实际余额" dataType="Currency" /> 
	</emp:table>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
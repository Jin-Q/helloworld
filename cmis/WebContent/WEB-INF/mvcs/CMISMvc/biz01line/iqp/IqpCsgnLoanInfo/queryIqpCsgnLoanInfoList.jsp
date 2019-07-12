<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpCsgnLoanInfo._toForm(form);
		IqpCsgnLoanInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCsgnLoanInfoPage() {
		var paramStr = IqpCsgnLoanInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCsgnLoanInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCsgnLoanInfo() {
		var paramStr = IqpCsgnLoanInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCsgnLoanInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCsgnLoanInfoPage() {
		var url = '<emp:url action="getIqpCsgnLoanInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpCsgnLoanInfo() {
		var paramStr = IqpCsgnLoanInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpCsgnLoanInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCsgnLoanInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpCsgnLoanInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpCsgnLoanInfo.serno" label="业务编号" />
			<emp:text id="IqpCsgnLoanInfo.csgn_cus_id" label="委托人客户码" />
			<emp:text id="IqpCsgnLoanInfo.csgn_acct_no" label="委托人一般账号" />
			<emp:text id="IqpCsgnLoanInfo.csgn_acct_name" label="委托人一般账户名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpCsgnLoanInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpCsgnLoanInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpCsgnLoanInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpCsgnLoanInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpCsgnLoanInfoList" pageMode="true" url="pageIqpCsgnLoanInfoQuery.do">
		<emp:text id="csgn_cus_id" label="委托人客户码" />
		<emp:text id="csgn_amt" label="委托金额" />
		<emp:text id="csgn_acct_no" label="委托人一般账号" />
		<emp:text id="csgn_acct_name" label="委托人一般账户名" />
		<emp:text id="chrg_rate" label="手续费率" />
		<emp:text id="debit_chrg_pay_rate" label="借款人手续费支付比例" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
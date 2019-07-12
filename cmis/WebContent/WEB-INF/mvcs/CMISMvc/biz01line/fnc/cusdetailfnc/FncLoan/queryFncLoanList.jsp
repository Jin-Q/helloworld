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
		FncLoan._toForm(form);
		FncLoanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncLoanPage() {
		var paramStr = FncLoanList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncLoanUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncLoan() {
		var paramStr = FncLoanList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncLoanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncLoanPage() {
		var url = '<emp:url action="getFncLoanAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncLoan() {
		var paramStr = FncLoanList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncLoanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncLoanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncLoanGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncLoan.cus_id" label="客户代码" />
			<emp:text id="FncLoan.cus_name" label="客户姓名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncLoanPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncLoanPage" label="修改" op="update"/>
		<emp:button id="deleteFncLoan" label="删除" op="remove"/>
		<emp:button id="viewFncLoan" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncLoanList" pageMode="true" url="pageFncLoanQuery.do">
		<emp:text id="cus_id" label="客户代码" />
		<emp:text id="cus_name" label="客户姓名" />
		<emp:select id="fnc_loan_typ" label="借款类别" dictname="STD_ZB_FNC_LAN_TYP"/>
		<emp:text id="fnc_amt" label="金额" dataType="Currency" />
		<emp:text id="fnc_blc" label="余额" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人"  />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="pk_id" label="主键" hidden="true"/>

	</emp:table>
	
</body>
</html>
</emp:page>
    
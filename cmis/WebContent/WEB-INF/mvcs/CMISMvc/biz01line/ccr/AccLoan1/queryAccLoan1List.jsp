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
		AccLoan1._toForm(form);
		AccLoan1List._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccLoan1Page() {
		var paramStr = AccLoan1List._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoan1UpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccLoan1() {
		var paramStr = AccLoan1List._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoan1ViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccLoan1Page() {
		var url = '<emp:url action="getAccLoan1AddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccLoan1() {
		var paramStr = AccLoan1List._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteAccLoan1Record.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccLoan1Group.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="AccLoan1Group" title="输入查询条件" maxColumn="2">
			<emp:text id="AccLoan1.prd_name" label="PRD_NAME" />
			<emp:text id="AccLoan1.cus_name" label="CUS_NAME" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddAccLoan1Page" label="新增" op="add"/>
		<emp:button id="getUpdateAccLoan1Page" label="修改" op="update"/>
		<emp:button id="deleteAccLoan1" label="删除" op="remove"/>
		<emp:button id="viewAccLoan1" label="查看" op="view"/>
	</div>

	<emp:table icollName="AccLoan1List" pageMode="true" url="pageAccLoan1Query.do">
		<emp:text id="prd_type" label="PRD_TYPE" />
		<emp:text id="loan_account" label="LOAN_ACCOUNT" />
		<emp:text id="assure_means_main" label="ASSURE_MEANS_MAIN" />
		<emp:text id="assure_means2" label="ASSURE_MEANS2" />
		<emp:text id="bill_no" label="BILL_NO" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
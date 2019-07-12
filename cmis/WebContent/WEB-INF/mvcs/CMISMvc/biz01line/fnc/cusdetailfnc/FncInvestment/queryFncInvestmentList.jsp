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
		FncInvestment._toForm(form);
		FncInvestmentList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncInvestmentPage() {
		var paramStr = FncInvestmentList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncInvestmentUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncInvestment() {
		var paramStr = FncInvestmentList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncInvestmentViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncInvestmentPage() {
		var url = '<emp:url action="getFncInvestmentAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncInvestment() {
		var paramStr = FncInvestmentList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncInvestmentRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncInvestmentGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncInvestmentGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncInvestment.cus_id" label="客户代码" />
			<emp:text id="FncInvestment.fnc_ym" label="年月" />
			<emp:text id="FncInvestment.cus_name" label="客户名称" />
			<emp:text id="FncInvestment.fnc_invt_toward" label="投资资产名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncInvestmentPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncInvestmentPage" label="修改" op="update"/>
		<emp:button id="deleteFncInvestment" label="删除" op="remove"/>
		<emp:button id="viewFncInvestment" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncInvestmentList" pageMode="true" url="pageFncInvestmentQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="fnc_invt_toward" label="投资资产名称" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="pk_id" label="主键" hidden="true" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
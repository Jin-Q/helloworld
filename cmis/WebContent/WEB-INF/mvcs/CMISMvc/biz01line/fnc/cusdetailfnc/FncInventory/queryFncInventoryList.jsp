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
		FncInventory._toForm(form);
		FncInventoryList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncInventoryPage() {
		var paramStr = FncInventoryList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncInventoryUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncInventory() {
		var paramStr = FncInventoryList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncInventoryViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncInventoryPage() {
		var url = '<emp:url action="getFncInventoryAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncInventory() {
		var paramStr = FncInventoryList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncInventoryRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncInventoryGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncInventoryGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncInventory.cus_name" label="客户名称" />
			<emp:text id="FncInventory.cus_id" label="客户代码" />
			<emp:text id="FncInventory.fnc_invy_name" label="存货名称" />
			<emp:text id="FncInventory.fnc_ym" label="年月" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncInventoryPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncInventoryPage" label="修改" op="update"/>
		<emp:button id="deleteFncInventory" label="删除" op="remove"/>
		<emp:button id="viewFncInventory" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncInventoryList" pageMode="true" url="pageFncInventoryQuery.do">
		<emp:text id="cus_id" label="客户码" />	
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="fnc_invy_name" label="存货名称" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="pk_id" label="主键" hidden="true" />

	</emp:table>
	
</body>
</html>
</emp:page>
    
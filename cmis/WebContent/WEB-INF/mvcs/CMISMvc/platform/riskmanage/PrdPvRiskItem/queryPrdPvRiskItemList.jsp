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
		PrdPvRiskItem._toForm(form);
		PrdPvRiskItemList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdPvRiskItemPage() {
		var paramStr = PrdPvRiskItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPvRiskItemUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdPvRiskItem() {
		var paramStr = PrdPvRiskItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPvRiskItemViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdPvRiskItemPage() {
		var url = '<emp:url action="getPrdPvRiskItemAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdPvRiskItem() {
		var paramStr = PrdPvRiskItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdPvRiskItemRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdPvRiskItemGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPvRiskItemGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="PrdPvRiskItem.item_id" label="项目编号" />
			<emp:text id="PrdPvRiskItem.item_name" label="项目名称" />
			<emp:select id="PrdPvRiskItem.used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdPvRiskItemPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdPvRiskItemPage" label="修改" op="update"/>
		<emp:button id="deletePrdPvRiskItem" label="删除" op="remove"/>
		<emp:button id="viewPrdPvRiskItem" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdPvRiskItemList" pageMode="true" url="pagePrdPvRiskItemQuery.do">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
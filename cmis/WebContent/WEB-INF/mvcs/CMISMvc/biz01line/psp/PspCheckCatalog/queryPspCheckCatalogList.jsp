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
		PspCheckCatalog._toForm(form);
		PspCheckCatalogList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspCheckCatalogPage() {
		var paramStr = PspCheckCatalogList._obj.getParamStr(['catalog_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckCatalogUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspCheckCatalog() {
		var paramStr = PspCheckCatalogList._obj.getParamStr(['catalog_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckCatalogViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspCheckCatalogPage() {
		var url = '<emp:url action="getPspCheckCatalogAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspCheckCatalog() {
		var paramStr = PspCheckCatalogList._obj.getParamStr(['catalog_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspCheckCatalogRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCheckCatalogGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspCheckCatalogGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckCatalog.catalog_id" label="目录编号" />
			<emp:text id="PspCheckCatalog.catalog_name" label="目录名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspCheckCatalogPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspCheckCatalogPage" label="修改" op="update"/>
		<emp:button id="deletePspCheckCatalog" label="删除" op="remove"/>
		<emp:button id="viewPspCheckCatalog" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspCheckCatalogList" pageMode="true" url="pagePspCheckCatalogQuery.do">
		<emp:text id="catalog_id" label="目录编号" />
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="memo" label="备注" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" hidden="true" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_br_id" hidden="true" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
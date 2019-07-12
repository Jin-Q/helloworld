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
		PrdCatalog._toForm(form);
		PrdCatalogList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdCatalogPage() {
		var paramStr = PrdCatalogList._obj.getParamStr(['catalogid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdCatalogUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdCatalog() {
		var paramStr = PrdCatalogList._obj.getParamStr(['catalogid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdCatalogViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdCatalogPage() {
		var url = '<emp:url action="getPrdCatalogAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdCatalog() {
		var paramStr = PrdCatalogList._obj.getParamStr(['catalogid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdCatalogRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdCatalogGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdCatalogGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdCatalog.catalogid" label="目录编号" />
			<emp:text id="PrdCatalog.catalogname" label="目录名称" />  
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdCatalogPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdCatalogPage" label="修改" op="update"/>
		<emp:button id="deletePrdCatalog" label="删除" op="remove"/>
		<emp:button id="viewPrdCatalog" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdCatalogList" pageMode="true" url="pagePrdCatalogQuery.do">
		<emp:text id="catalogid" label="目录编号" />
		<emp:text id="catalogname" label="目录名称" />
		<emp:text id="cataloglevel" label="目录层级" hidden="true"/>
		<emp:text id="supcatalogid" label="上级目录编码" />
		<emp:text id="comments" label="备注" />
		<emp:text id="inputid" label="登记人员" hidden="true"/>
		<emp:text id="inputid_displayname" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="orgid" label="登记机构" hidden="true"/>
		<emp:text id="orgid_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		PrdPolcyScheme._toForm(form);
		PrdPolcySchemeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdPolcySchemePage() {
		var paramStr = PrdPolcySchemeList._obj.getParamStr(['schemeid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPolcySchemeUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdPolcyScheme() {
		var paramStr = PrdPolcySchemeList._obj.getParamStr(['schemeid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPolcySchemeViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdPolcySchemePage() {
		var url = '<emp:url action="getPrdPolcySchemeAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdPolcyScheme() {
		var paramStr = PrdPolcySchemeList._obj.getParamStr(['schemeid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdPolcySchemeRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdPolcySchemeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPolcySchemeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPolcyScheme.schemeid" label="方案编号" />
			<emp:text id="PrdPolcyScheme.schemename" label="方案名称" />
			<emp:select id="PrdPolcyScheme.effectived" dictname="STD_ZX_YES_NO" label="是否启用" />
			<emp:text id="PrdPolcyScheme.inputid" label="登记人员" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdPolcySchemePage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdPolcySchemePage" label="修改" op="update"/>
		<emp:button id="deletePrdPolcyScheme" label="删除" op="remove"/>
		<!-- emp:button id="viewPrdPolcyScheme" label="查看" op="view" -->
	</div>

	<emp:table icollName="PrdPolcySchemeList" pageMode="true" url="pagePrdPolcySchemeQuery.do">
		<emp:text id="schemeid" label="方案编号" />
		<emp:text id="schemename" label="方案名称" />
		<emp:select id="effectived" label="是否启用" dictname="STD_ZX_YES_NO" />
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
    
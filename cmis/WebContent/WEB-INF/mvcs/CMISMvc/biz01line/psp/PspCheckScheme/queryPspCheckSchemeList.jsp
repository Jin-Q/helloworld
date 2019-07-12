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
		PspCheckScheme._toForm(form);
		PspCheckSchemeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspCheckSchemePage() {
		var paramStr = PspCheckSchemeList._obj.getParamStr(['scheme_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckSchemeUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspCheckScheme() {
		var paramStr = PspCheckSchemeList._obj.getParamStr(['scheme_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckSchemeViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspCheckSchemePage() {
		var url = '<emp:url action="getPspCheckSchemeAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspCheckScheme() {
		var paramStr = PspCheckSchemeList._obj.getParamStr(['scheme_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspCheckSchemeRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCheckSchemeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspCheckSchemeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckScheme.scheme_id" label="方案编号" />
			<emp:text id="PspCheckScheme.scheme_name" label="方案名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspCheckSchemePage" label="新增" op="add"/>
		<emp:button id="getUpdatePspCheckSchemePage" label="修改" op="update"/>
		<emp:button id="deletePspCheckScheme" label="删除" op="remove"/>
		<emp:button id="viewPspCheckScheme" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspCheckSchemeList" pageMode="true" url="pagePspCheckSchemeQuery.do">
		<emp:text id="scheme_id" label="方案编号" />
		<emp:text id="scheme_name" label="方案名称" />
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
    
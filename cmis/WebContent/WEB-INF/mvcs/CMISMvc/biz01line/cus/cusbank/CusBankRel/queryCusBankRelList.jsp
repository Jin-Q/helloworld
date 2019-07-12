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
		CusBankRel._toForm(form);
		CusBankRelList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusBankRelPage() {
		var paramStr = CusBankRelList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBankRelUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusBankRel() {
		var paramStr = CusBankRelList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBankRelViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusBankRelPage() {
		var url = '<emp:url action="getCusBankRelAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusBankRel() {
		var paramStr = CusBankRelList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusBankRelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusBankRelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusBankRelGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusBankRel.cus_id" label="客户码" />
			<emp:text id="CusBankRel.cus_name" label="客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusBankRelPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusBankRelPage" label="修改" op="update"/>
		<emp:button id="deleteCusBankRel" label="删除" op="remove"/>
		<emp:button id="viewCusBankRel" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusBankRelList" pageMode="true" url="pageCusBankRelQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_bank_rel" label="客户与我行关系" hidden="true"/>
		<emp:text id="reg_date" label="登记日期" />
		<emp:text id="reg_name" label="登记人" hidden="true"/>
		<emp:text id="reg_name_displayname" label="登记人" />
		<emp:text id="reg_org" label="登记机构" hidden="true"/>
		<emp:text id="reg_org_displayname" label="登记机构" />
		<emp:text id="cus_bank_rel_name" label="客户与我行关系名称" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
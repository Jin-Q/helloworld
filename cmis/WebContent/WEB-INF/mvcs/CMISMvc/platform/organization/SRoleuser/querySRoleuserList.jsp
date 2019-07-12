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
		SRoleuser._toForm(form);
		SRoleuserList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSRoleuserPage() {
		var paramStr = SRoleuserList._obj.getParamStr(['roleno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSRoleuserUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSRoleuser() {
		var paramStr = SRoleuserList._obj.getParamStr(['roleno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSRoleuserViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSRoleuserPage() {
		var url = '<emp:url action="getSRoleuserAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSRoleuser() {
		var paramStr = SRoleuserList._obj.getParamStr(['roleno','actorno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteSRoleuserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SRoleuserGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddSRoleuserPage" label="新增" op="add"/>
		<emp:button id="getUpdateSRoleuserPage" label="修改" op="update"/>
		<emp:button id="deleteSRoleuser" label="删除" op="remove"/>
		<emp:button id="viewSRoleuser" label="查看" op="view"/>
	</div>

	<emp:table icollName="SRoleuserList" pageMode="false" url="pageSRoleuserQuery.do">
		<emp:text id="roleno" label="角色码" />
		<emp:text id="actorno" label="用户码" />
		<emp:text id="state" label="状态" />
		<emp:text id="orgid" label="组织号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
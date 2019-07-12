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
		SDeptuser._toForm(form);
		SDeptuserList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSDeptuserPage() {
		var paramStr = SDeptuserList._obj.getParamStr(['organno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDeptuserUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSDeptuser() {
		var paramStr = SDeptuserList._obj.getParamStr(['organno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDeptuserViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSDeptuserPage() {
		var url = '<emp:url action="getSDeptuserAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSDeptuser() {
		var paramStr = SDeptuserList._obj.getParamStr(['organno','actorno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteSDeptuserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SDeptuserGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddSDeptuserPage" label="新增" op="add"/>
		<emp:button id="getUpdateSDeptuserPage" label="修改" op="update"/>
		<emp:button id="deleteSDeptuser" label="删除" op="remove"/>
		<emp:button id="viewSDeptuser" label="查看" op="view"/>
	</div>

	<emp:table icollName="SDeptuserList" pageMode="false" url="pageSDeptuserQuery.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="depno" label="部门码" />
		<emp:text id="actorno" label="用户码" />
		<emp:text id="state" label="状态" />
		<emp:text id="orgid" label="组织号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
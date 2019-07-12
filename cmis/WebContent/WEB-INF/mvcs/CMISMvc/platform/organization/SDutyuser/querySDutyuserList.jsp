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
		SDutyuser._toForm(form);
		SDutyuserList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSDutyuserPage() {
		var paramStr = SDutyuserList._obj.getParamStr(['dutyno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDutyuserUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSDutyuser() {
		var paramStr = SDutyuserList._obj.getParamStr(['dutyno','actorno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDutyuserViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSDutyuserPage() {
		var url = '<emp:url action="getSDutyuserAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSDutyuser() {
		var paramStr = SDutyuserList._obj.getParamStr(['dutyno','actorno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteSDutyuserRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SDutyuserGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddSDutyuserPage" label="新增" op="add"/>
		<emp:button id="getUpdateSDutyuserPage" label="修改" op="update"/>
		<emp:button id="deleteSDutyuser" label="删除" op="remove"/>
		<emp:button id="viewSDutyuser" label="查看" op="view"/>
	</div>

	<emp:table icollName="SDutyuserList" pageMode="false" url="pageSDutyuserQuery.do">
		<emp:text id="dutyno" label="岗位码" />
		<emp:text id="actorno" label="用户码" />
		<emp:text id="state" label="状态" />
		<emp:text id="orgid" label="组织号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
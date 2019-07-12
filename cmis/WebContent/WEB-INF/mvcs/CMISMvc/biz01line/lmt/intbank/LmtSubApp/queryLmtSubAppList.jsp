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
		LmtSubApp._toForm(form);
		LmtSubAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtSubAppPage() {
		var paramStr = LmtSubAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSubAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtSubApp() {
		var paramStr = LmtSubAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSubAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtSubAppPage() {
		var url = '<emp:url action="getLmtSubAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtSubApp() {
		var paramStr = LmtSubAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtSubAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtSubAppGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddLmtSubAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtSubAppPage" label="修改" op="update"/>
		<emp:button id="deleteLmtSubApp" label="删除" op="remove"/>
		<emp:button id="viewLmtSubApp" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtSubAppList" pageMode="true" url="pageLmtSubAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="variet_no" label="品种编号" />
		<emp:text id="variet_name" label="品种名称" />
		<emp:text id="lmt_amt" label="授信额度" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
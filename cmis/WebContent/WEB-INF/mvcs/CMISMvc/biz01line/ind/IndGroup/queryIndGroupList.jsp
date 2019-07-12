<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddIndGroupPage(){
		var url = '<emp:url action="getIndGroupAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIndGroup(){		
		var paramStr = IndGroupList._obj.getParamStr(['group_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndGroupRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateIndGroupPage(){
		var paramStr = IndGroupList._obj.getParamStr(['group_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIndGroupUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndGroup(){
		var paramStr = IndGroupList._obj.getParamStr(['group_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryIndGroupDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IndGroup._toForm(form);
		IndGroupList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IndGroupGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IndGroupGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IndGroup.group_no" label="组别编号" />
			<emp:text id="IndGroup.trans_id_displayname" label="规则交易名称" />
			<emp:text id="IndGroup.group_name" label="组别名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddIndGroupPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndGroupPage" label="修改" op="update"/>
		<emp:button id="deleteIndGroup" label="删除" op="remove"/>
		<emp:button id="viewIndGroup" label="查看" op="view"/>
	</div>
	<emp:table icollName="IndGroupList" pageMode="true" url="pageIndGroupQuery.do">
		<emp:text id="group_no" label="组别编号" />
		<emp:text id="group_name" label="组别名称" />
		<emp:text id="trans_id" label="规则交易" />
		<emp:text id="trans_id_displayname" label="规则交易名称" />
		<emp:text id="group_kind" label="组性质" dictname="STD_ZB_GROUP_PROP" hidden="true"/>
		<emp:text id="rating_rules" label="组评分规则" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
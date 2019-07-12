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
		WfiWorkflow2org._toForm(form);
		WfiWorkflow2orgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiWorkflow2orgPage() {
		var paramStr = WfiWorkflow2orgList._obj.getParamStr(['wf2org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiWorkflow2orgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiWorkflow2org() {
		var paramStr = WfiWorkflow2orgList._obj.getParamStr(['wf2org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiWorkflow2orgViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiWorkflow2orgPage() {
		var url = '<emp:url action="getWfiWorkflow2orgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiWorkflow2org() {
		var paramStr = WfiWorkflow2orgList._obj.getParamStr(['wf2org_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiWorkflow2orgRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiWorkflow2orgGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiWorkflow2orgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WfiWorkflow2org.org_id" label="机构ID" />
			<emp:text id="WfiWorkflow2org.org_name" label="机构名称" />
			<emp:select id="WfiWorkflow2org.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
			<emp:text id="WfiWorkflow2org.wfname" label="流程名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfiWorkflow2orgPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiWorkflow2orgPage" label="修改" op="update"/>
		<emp:button id="deleteWfiWorkflow2org" label="删除" op="remove"/>
		<emp:button id="viewWfiWorkflow2org" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiWorkflow2orgList" pageMode="true" url="pageWfiWorkflow2orgQuery.do">
		<emp:text id="org_id" label="机构ID" />
		<emp:text id="org_name" label="机构名称" />
		<emp:text id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfname" label="流程名称" />
		<emp:text id="wf2org_id" label="关联ID" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
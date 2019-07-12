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
		WfiWorkflow2biz._toForm(form);
		WfiWorkflow2bizList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiWorkflow2bizPage() {
		var paramStr = WfiWorkflow2bizList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiWorkflow2bizUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiWorkflow2biz() {
		var paramStr = WfiWorkflow2bizList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiWorkflow2bizViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiWorkflow2bizPage() {
		var url = '<emp:url action="getWfiWorkflow2bizAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiWorkflow2biz() {
		var paramStr = WfiWorkflow2bizList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiWorkflow2bizRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiWorkflow2bizGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiWorkflow2bizGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="WfiWorkflow2biz.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
			<emp:text id="WfiWorkflow2biz.wfsign" label="流程标识" />
			<emp:text id="WfiWorkflow2biz.wfname" label="流程名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfiWorkflow2bizPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiWorkflow2bizPage" label="修改" op="update"/>
		<emp:button id="deleteWfiWorkflow2biz" label="删除" op="remove"/>
		<emp:button id="viewWfiWorkflow2biz" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiWorkflow2bizList" pageMode="true" url="pageWfiWorkflow2bizQuery.do">
		<emp:text id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfname" label="流程名称" />
		<emp:text id="scene_scope" label="配置适用范围" dictname="WF_2BIZ_SCOPE" />
		<emp:text id="pk1" label="配置主键" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
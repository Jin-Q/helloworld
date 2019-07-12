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
		WfiSignConf._toForm(form);
		WfiSignConfList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiSignConfPage() {
		var paramStr = WfiSignConfList._obj.getParamStr(['sign_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiSignConfUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiSignConf() {
		var paramStr = WfiSignConfList._obj.getParamStr(['sign_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiSignConfViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiSignConfPage() {
		var url = '<emp:url action="getWfiSignConfAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiSignConf() {
		var paramStr = WfiSignConfList._obj.getParamStr(['sign_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiSignConfRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiSignConfGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiSignConfGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WfiSignConf.sign_name" label="会签策略名" />
			<emp:select id="WfiSignConf.sign_state" label="会签策略状态" dictname="WF_SIGNCONF_STATE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfiSignConfPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiSignConfPage" label="修改" op="update"/>
		<emp:button id="deleteWfiSignConf" label="删除" op="remove"/>
		<emp:button id="viewWfiSignConf" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiSignConfList" pageMode="false" url="pageWfiSignConfQuery.do">
		<emp:text id="sign_id" label="会签策略ID" />
		<emp:text id="sign_name" label="会签策略名" />
		<emp:text id="sign_state" label="会签策略状态" dictname="WF_SIGNCONF_STATE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
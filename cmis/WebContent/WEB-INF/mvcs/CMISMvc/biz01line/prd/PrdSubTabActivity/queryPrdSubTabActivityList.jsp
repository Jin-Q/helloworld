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
		PrdSubTabActivity._toForm(form);
		PrdSubTabActivityList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdSubTabActivityPage() {
		var paramStr = PrdSubTabActivityList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdSubTabActivityUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdSubTabActivity() {
		var paramStr = PrdSubTabActivityList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdSubTabActivityViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdSubTabActivityPage() {
		var url = '<emp:url action="getPrdSubTabActivityAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdSubTabActivity() {
		var paramStr = PrdSubTabActivityList._obj.getParamStr(['pkid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdSubTabActivityRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdSubTabActivityGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdSubTabActivityGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdSubTabActivity.mainid" label="主资源ID" />
			<emp:text id="PrdSubTabActivity.mainmodel" label="主资源表模型" />
			<emp:text id="PrdSubTabActivity.subid" label="从资源ID" />
			<emp:text id="PrdSubTabActivity.subname" label="从资源名称" /> 
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdSubTabActivityPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdSubTabActivityPage" label="修改" op="update"/>
		<emp:button id="deletePrdSubTabActivity" label="删除" op="remove"/>
		<emp:button id="viewPrdSubTabActivity" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdSubTabActivityList" pageMode="true" url="pagePrdSubTabActivityQuery.do">
		<emp:text id="pkid" label="主键" />
		<emp:text id="mainid" label="主资源ID" />
		<emp:text id="mainmodel" label="主资源表模型" />
		<emp:text id="mainterm" label="主资源表模型过滤条件" hidden="true"/>
		<emp:text id="subid" label="从资源ID" />
		<emp:text id="subname" label="从资源名称" />
		<emp:text id="subnum" label="从资源标识" />
		<emp:text id="subterm" label="从资源过滤条件" hidden="true"/>
		<emp:text id="num" label="序号" />
		<emp:text id="inputid" label="登记人员" hidden="true"/>
		<emp:text id="inputid_displayname" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="inputorg" label="登记机构" hidden="true"/>
		<emp:text id="inputorg_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		SfTrans._toForm(form);
		SfTransList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSfTransPage() {
		var paramStr = SfTransList._obj.getParamStr(['trans_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getSfTransUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSfTrans() {
		var paramStr = SfTransList._obj.getParamStr(['trans_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getSfTransViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSfTransPage() {
		var url = '<emp:url action="getSfTransAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSfTrans() {
		var paramStr = SfTransList._obj.getParamStr(['trans_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteSfTransRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SfTransGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SfTransGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SfTrans.trans_name" label="交易名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSfTransPage" label="新增" op="add"/>
		<emp:button id="getUpdateSfTransPage" label="修改" op="update"/>
		<emp:button id="deleteSfTrans" label="删除" op="remove"/>
		<emp:button id="viewSfTrans" label="查看" op="view"/>
	</div>

	<emp:table icollName="SfTransList" pageMode="true" url="pageSfTransQuery.do">
		<emp:text id="trans_id" label="交易ID" />
		<emp:text id="trans_name" label="交易名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
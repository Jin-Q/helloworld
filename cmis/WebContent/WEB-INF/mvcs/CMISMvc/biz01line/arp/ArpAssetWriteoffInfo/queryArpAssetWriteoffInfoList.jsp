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
		ArpAssetWriteoffInfo._toForm(form);
		ArpAssetWriteoffInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetWriteoffInfoPage() {
		var paramStr = ArpAssetWriteoffInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetWriteoffInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetWriteoffInfo() {
		var paramStr = ArpAssetWriteoffInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetWriteoffInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetWriteoffInfoPage() {
		var url = '<emp:url action="getArpAssetWriteoffInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetWriteoffInfo() {
		var paramStr = ArpAssetWriteoffInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetWriteoffInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetWriteoffInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpAssetWriteoffInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpAssetWriteoffInfo.serno" label="业务编号" />
			<emp:text id="ArpAssetWriteoffInfo.guaranty_no" label="抵债资产编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpAssetWriteoffInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetWriteoffInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetWriteoffInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetWriteoffInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetWriteoffInfoList" pageMode="true" url="pageArpAssetWriteoffInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="writeoff_amt" label="核销金额" />
		<emp:text id="writeoff_date" label="核销日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
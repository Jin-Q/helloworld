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
		ArpAssetPegInfo._toForm(form);
		ArpAssetPegInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetPegInfoPage() {
		var paramStr = ArpAssetPegInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetPegInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetPegInfo() {
		var paramStr = ArpAssetPegInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetPegInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetPegInfoPage() {
		var url = '<emp:url action="getArpAssetPegInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetPegInfo() {
		var paramStr = ArpAssetPegInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetPegInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetPegInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpAssetPegInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpAssetPegInfo.serno" label="业务编号" />
			<emp:text id="ArpAssetPegInfo.guaranty_no" label="抵债资产编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpAssetPegInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetPegInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetPegInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetPegInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetPegInfoList" pageMode="true" url="pageArpAssetPegInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="to_prop_value" label="转固价值" />
		<emp:text id="asgn_date" label="入账日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
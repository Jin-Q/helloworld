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
		ArpAssetRentInfo._toForm(form);
		ArpAssetRentInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetRentInfoPage() {
		var paramStr = ArpAssetRentInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetRentInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetRentInfo() {
		var paramStr = ArpAssetRentInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetRentInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetRentInfoPage() {
		var url = '<emp:url action="getArpAssetRentInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetRentInfo() {
		var paramStr = ArpAssetRentInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetRentInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetRentInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpAssetRentInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpAssetRentInfo.serno" label="业务编号" />
			<emp:text id="ArpAssetRentInfo.guaranty_no" label="抵债资产编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpAssetRentInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetRentInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetRentInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetRentInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetRentInfoList" pageMode="true" url="pageArpAssetRentInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="debt_in_amt" label="抵入金额" />
		<emp:text id="rent_amt" label="租金" />
		<emp:text id="lease_start_date" label="租约起始日期" />
		<emp:text id="lease_end_date" label="租约到期日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
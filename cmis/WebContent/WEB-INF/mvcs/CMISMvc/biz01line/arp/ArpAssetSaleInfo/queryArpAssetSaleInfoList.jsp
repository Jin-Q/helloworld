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
		ArpAssetSaleInfo._toForm(form);
		ArpAssetSaleInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetSaleInfoPage() {
		var paramStr = ArpAssetSaleInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetSaleInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetSaleInfo() {
		var paramStr = ArpAssetSaleInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetSaleInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetSaleInfoPage() {
		var url = '<emp:url action="getArpAssetSaleInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetSaleInfo() {
		var paramStr = ArpAssetSaleInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetSaleInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetSaleInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpAssetSaleInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpAssetSaleInfo.serno" label="业务编号" />
			<emp:text id="ArpAssetSaleInfo.guaranty_no" label="抵债资产编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpAssetSaleInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetSaleInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetSaleInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetSaleInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetSaleInfoList" pageMode="true" url="pageArpAssetSaleInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="sale_amt" label="出售价格" />
		<emp:text id="sale_date" label="出售日期" />
		<emp:text id="sale_mode" label="出售方式" dictname="STD_ZB_SALE_TYPE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
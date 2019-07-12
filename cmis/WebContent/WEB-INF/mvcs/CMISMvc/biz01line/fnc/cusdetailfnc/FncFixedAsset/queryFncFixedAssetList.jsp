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
		FncFixedAsset._toForm(form);
		FncFixedAssetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncFixedAssetPage() {
		var paramStr = FncFixedAssetList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncFixedAssetUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncFixedAsset() {
		var paramStr = FncFixedAssetList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncFixedAssetViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncFixedAssetPage() {
		var url = '<emp:url action="getFncFixedAssetAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncFixedAsset() {
		var paramStr = FncFixedAssetList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncFixedAssetRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncFixedAssetGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncFixedAssetGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncFixedAsset.cus_id" label="客户码" />
			<emp:text id="FncFixedAsset.fnc_asset_pld_desc" label="抵质押情况" />
			<emp:select id="FncFixedAsset.bb_type" label="报表周期类型"  dictname="STD_ZB_FNC_STAT"/>
			<emp:select id="FncFixedAsset.fnc_asset_obt_mth" label="占用方式" dictname="STD_ZB_ASSETOM"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncFixedAssetPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncFixedAssetPage" label="修改" op="update"/>
		<emp:button id="deleteFncFixedAsset" label="删除" op="remove"/>
		<emp:button id="viewFncFixedAsset" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncFixedAssetList" pageMode="true" url="pageFncFixedAssetQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户姓名" />
		<emp:text id="bb_type" label="报表周期类型"  dictname="STD_ZB_FNC_STAT" hidden="true"/>
		<emp:text id="fnc_asset_name" label="名称" />
		<emp:text id="fnc_asset_amt" label="数量" />
		<emp:text id="fnc_asset_wrr_id" label="权利证书" />
		<emp:text id="fnc_asset_obt_mth" label="占用方式" hidden="true" />
		<emp:text id="fnc_asset_pld_desc" label="抵质押情况" hidden="true" />
		
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		
		<emp:text id="bb_type" label="报表周期类型"  dictname="STD_ZB_FNC_STAT" hidden="true"/>
		<emp:text id="pk_id" label="主键" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
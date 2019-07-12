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
		ArpAssetSaleAccInfo._toForm(form);
		ArpAssetSaleAccInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetSaleAccInfoPage() {
		var paramStr = ArpAssetSaleAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetSaleAccInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetSaleAccInfo() {
		var paramStr = ArpAssetSaleAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetSaleAccInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetSaleAccInfoPage() {
		var url = '<emp:url action="getArpAssetSaleAccInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetSaleAccInfo() {
		var paramStr = ArpAssetSaleAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetSaleAccInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetSaleAccInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddArpAssetSaleAccInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetSaleAccInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetSaleAccInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetSaleAccInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetSaleAccInfoList" pageMode="false" url="pageArpAssetSaleAccInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_disp_no" label="资产处置编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="debt_in_amt" label="抵入金额" />
		<emp:text id="eval_amt" label="评估金额" />
		<emp:text id="sale_amt" label="出售价格" />
		<emp:text id="sale_date" label="出售日期" />
		<emp:text id="sale_mode" label="出售方式" dictname="STD_ZB_SALE_TYPE" />
		<emp:text id="memo" label="备注" />
		<emp:text id="status" label="状态" dictname="STD_ZX_ASSET_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
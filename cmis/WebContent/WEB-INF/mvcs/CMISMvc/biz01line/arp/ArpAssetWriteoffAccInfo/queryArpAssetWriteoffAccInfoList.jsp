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
		ArpAssetWriteoffAccInfo._toForm(form);
		ArpAssetWriteoffAccInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetWriteoffAccInfoPage() {
		var paramStr = ArpAssetWriteoffAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetWriteoffAccInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetWriteoffAccInfo() {
		var paramStr = ArpAssetWriteoffAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetWriteoffAccInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetWriteoffAccInfoPage() {
		var url = '<emp:url action="getArpAssetWriteoffAccInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetWriteoffAccInfo() {
		var paramStr = ArpAssetWriteoffAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetWriteoffAccInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetWriteoffAccInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddArpAssetWriteoffAccInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetWriteoffAccInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetWriteoffAccInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetWriteoffAccInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetWriteoffAccInfoList" pageMode="false" url="pageArpAssetWriteoffAccInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_disp_no" label="资产处置编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="debt_in_amt" label="抵入金额" />
		<emp:text id="writeoff_amt" label="核销金额" />
		<emp:text id="disp_resn" label="处置理由" />
		<emp:text id="writeoff_date" label="核销日期" />
		<emp:text id="memo" label="备注" />
		<emp:text id="status" label="状态" dictname="STD_ZX_ASSET_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
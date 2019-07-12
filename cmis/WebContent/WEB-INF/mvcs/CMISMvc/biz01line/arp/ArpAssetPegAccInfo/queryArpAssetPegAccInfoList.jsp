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
		ArpAssetPegAccInfo._toForm(form);
		ArpAssetPegAccInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetPegAccInfoPage() {
		var paramStr = ArpAssetPegAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetPegAccInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetPegAccInfo() {
		var paramStr = ArpAssetPegAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetPegAccInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetPegAccInfoPage() {
		var url = '<emp:url action="getArpAssetPegAccInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetPegAccInfo() {
		var paramStr = ArpAssetPegAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetPegAccInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetPegAccInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddArpAssetPegAccInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetPegAccInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetPegAccInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetPegAccInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetPegAccInfoList" pageMode="false" url="pageArpAssetPegAccInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_disp_no" label="资产处置编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="debt_in_amt" label="抵入金额" />
		<emp:text id="to_prop_value" label="转固价值" />
		<emp:text id="eval_amt" label="评估金额" />
		<emp:text id="disp_resn" label="处置理由" />
		<emp:text id="asgn_date" label="入账日期" />
		<emp:text id="memo" label="备注" />
		<emp:text id="status" label="状态" dictname="STD_ZX_ASSET_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
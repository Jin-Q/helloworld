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
		ArpAssetRentAccInfo._toForm(form);
		ArpAssetRentAccInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpAssetRentAccInfoPage() {
		var paramStr = ArpAssetRentAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetRentAccInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpAssetRentAccInfo() {
		var paramStr = ArpAssetRentAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpAssetRentAccInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpAssetRentAccInfoPage() {
		var url = '<emp:url action="getArpAssetRentAccInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpAssetRentAccInfo() {
		var paramStr = ArpAssetRentAccInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpAssetRentAccInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpAssetRentAccInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddArpAssetRentAccInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpAssetRentAccInfoPage" label="修改" op="update"/>
		<emp:button id="deleteArpAssetRentAccInfo" label="删除" op="remove"/>
		<emp:button id="viewArpAssetRentAccInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpAssetRentAccInfoList" pageMode="false" url="pageArpAssetRentAccInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_disp_no" label="资产处置编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="debt_in_amt" label="抵入金额" />
		<emp:text id="renta_paid_mode" label="租金收缴方式" />
		<emp:text id="rent_amt" label="租金" />
		<emp:text id="mort_amt" label="押金" />
		<emp:text id="lessee" label="承租人" />
		<emp:text id="lessee_phone" label="承租人电话" />
		<emp:text id="lessee_addr" label="承租人地址" />
		<emp:text id="lessee_cert_type" label="承租人证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="lessee_cert_no" label="承租人证件号码" />
		<emp:text id="lease_start_date" label="租约起始日期" />
		<emp:text id="lease_end_date" label="租约到期日期" />
		<emp:text id="memo" label="备注" />
		<emp:text id="status" label="状态" dictname="STD_ZX_ASSET_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
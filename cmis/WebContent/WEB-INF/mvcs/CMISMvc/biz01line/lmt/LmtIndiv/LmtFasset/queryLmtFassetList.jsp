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
		LmtFasset._toForm(form);
		LmtFassetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtFassetPage() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFassetUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFasset() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFassetViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFassetPage() {
		var url = '<emp:url action="getLmtFassetAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtFasset() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFassetRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtFassetGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddLmtFassetPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtFassetPage" label="修改" op="update"/>
		<emp:button id="deleteLmtFasset" label="删除" op="remove"/>
		<emp:button id="viewLmtFasset" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtFassetList" pageMode="false" url="pageLmtFassetQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" />
		<emp:text id="fasset_type" label="家庭资产类型" />
		<emp:text id="autho_name" label="权属人名称" />
		<emp:text id="asset_seval" label="资产原估值" />
		<emp:text id="asset_ivalue" label="资产认定值" />
		<emp:text id="asset_ivalue" label="资产认定值" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
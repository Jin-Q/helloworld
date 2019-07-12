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
		CusModifyHistoryCfg._toForm(form);
		CusModifyHistoryCfgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusModifyHistoryCfgPage() {
		var paramStr = CusModifyHistoryCfgList._obj.getParamStr(['model_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusModifyHistoryCfgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusModifyHistoryCfg() {
		var paramStr = CusModifyHistoryCfgList._obj.getParamStr(['model_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusModifyHistoryCfgViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusModifyHistoryCfgPage() {
		var url = '<emp:url action="getCusModifyCfgLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusModifyHistoryCfg() {
		var paramStr = CusModifyHistoryCfgList._obj.getParamStr(['model_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusModifyHistoryCfgRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusModifyHistoryCfgGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusModifyHistoryCfgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusModifyHistoryCfg.model_id" label="表名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusModifyHistoryCfgPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusModifyHistoryCfgPage" label="修改" op="update"/>
		<emp:button id="deleteCusModifyHistoryCfg" label="删除" op="remove"/>
		<emp:button id="viewCusModifyHistoryCfg" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusModifyHistoryCfgList" pageMode="true" url="pageCusModifyHistoryCfgQuery.do">
		<emp:text id="model_id" label="表名" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
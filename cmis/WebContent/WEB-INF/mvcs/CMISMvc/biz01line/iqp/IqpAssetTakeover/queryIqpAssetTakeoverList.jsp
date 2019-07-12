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
		IqpAssetTakeover._toForm(form);
		IqpAssetTakeoverList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetTakeoverPage() {
		var paramStr = IqpAssetTakeoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTakeoverUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetTakeover() {
		var paramStr = IqpAssetTakeoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTakeoverViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetTakeoverPage() {
		var url = '<emp:url action="getIqpAssetTakeoverAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetTakeover() {
		var paramStr = IqpAssetTakeoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpAssetTakeoverRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetTakeoverGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetTakeoverGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetTakeover.serno" label="业务编号" />
			<emp:text id="IqpAssetTakeover.toorg_no" label="交易对手行号" />
			<emp:text id="IqpAssetTakeover.toorg_name" label="交易对手行名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetTakeoverPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetTakeoverPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetTakeover" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetTakeover" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetTakeoverList" pageMode="true" url="pageIqpAssetTakeoverQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="asset_total_amt" label="资产总额" />
		<emp:text id="takeover_amt" label="转让金额" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
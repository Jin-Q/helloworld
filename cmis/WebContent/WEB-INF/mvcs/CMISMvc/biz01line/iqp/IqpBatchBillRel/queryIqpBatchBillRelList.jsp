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
		IqpBatchBillRel._toForm(form);
		IqpBatchBillRelList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBatchBillRelPage() {
		var paramStr = IqpBatchBillRelList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBatchBillRelUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBatchBillRel() {
		var paramStr = IqpBatchBillRelList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBatchBillRelViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBatchBillRelPage() {
		var url = '<emp:url action="getIqpBatchBillRelAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBatchBillRel() {
		var paramStr = IqpBatchBillRelList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpBatchBillRelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBatchBillRelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpBatchBillRelPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBatchBillRelPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBatchBillRel" label="删除" op="remove"/>
		<emp:button id="viewIqpBatchBillRel" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBatchBillRelList" pageMode="false" url="pageIqpBatchBillRelQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="porder_no" label="汇票号码" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		LmtBatchListLmt._toForm(form);
		LmtBatchListLmtList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtBatchListLmtPage() {
		var paramStr = LmtBatchListLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtBatchListLmtUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtBatchListLmt() {
		var paramStr = LmtBatchListLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtBatchListLmtViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtBatchListLmtPage() {
		var url = '<emp:url action="getLmtBatchListLmtAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtBatchListLmt() {
		var paramStr = LmtBatchListLmtList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtBatchListLmtRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtBatchListLmtGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtBatchListLmtGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtBatchListLmt.serno" label="业务编号" />
			<emp:text id="LmtBatchListLmt.cus_id" label="客户码" />
			<emp:text id="LmtBatchListLmt.cur_type" label="授信币种" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtBatchListLmtPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtBatchListLmtPage" label="修改" op="update"/>
		<emp:button id="deleteLmtBatchListLmt" label="删除" op="remove"/>
		<emp:button id="viewLmtBatchListLmt" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtBatchListLmtList" pageMode="true" url="pageLmtBatchListLmtQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cur_type" label="授信币种" />
		<emp:text id="lmt_amt" label="授信金额" />
		<emp:text id="term_type" label="期限类型" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		IqpLoanPromissory._toForm(form);
		IqpLoanPromissoryList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpLoanPromissoryPage() {
		var paramStr = IqpLoanPromissoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpLoanPromissoryUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpLoanPromissory() {
		var paramStr = IqpLoanPromissoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpLoanPromissoryViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpLoanPromissoryPage() {
		var url = '<emp:url action="getIqpLoanPromissoryAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpLoanPromissory() {
		var paramStr = IqpLoanPromissoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpLoanPromissoryRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpLoanPromissoryGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpLoanPromissoryGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpLoanPromissory.receiver" label="接收方" />
			<emp:text id="IqpLoanPromissory.item_name" label="项目名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpLoanPromissoryPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpLoanPromissoryPage" label="修改" op="update"/>
		<emp:button id="deleteIqpLoanPromissory" label="删除" op="remove"/>
		<emp:button id="viewIqpLoanPromissory" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpLoanPromissoryList" pageMode="true" url="pageIqpLoanPromissoryQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="receiver" label="接收方" />
		<emp:text id="other_cond_need" label="其他条件和要求" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="item_bground" label="项目背景" />
		<emp:text id="busnes_bground" label="贸易背景" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
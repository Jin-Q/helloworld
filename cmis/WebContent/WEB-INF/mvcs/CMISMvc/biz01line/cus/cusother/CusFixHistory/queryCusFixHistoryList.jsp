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
		CusFixHistory._toForm(form);
		CusFixHistoryList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusFixHistoryPage() {
		var paramStr = CusFixHistoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusFixHistoryUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusFixHistory() {
		var paramStr = CusFixHistoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusFixHistoryViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusFixHistoryPage() {
		var url = '<emp:url action="getCusFixHistoryAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusFixHistory() {
		var paramStr = CusFixHistoryList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusFixHistoryRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusFixHistoryGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddCusFixHistoryPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusFixHistoryPage" label="修改" op="update"/>
		<emp:button id="deleteCusFixHistory" label="删除" op="remove"/>
		<emp:button id="viewCusFixHistory" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusFixHistoryList" pageMode="true" url="pageCusFixHistoryQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="checkcode" label="校验码" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="update_id" label="修改人" />
		<emp:date id="update_date" label="修改日期" />
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
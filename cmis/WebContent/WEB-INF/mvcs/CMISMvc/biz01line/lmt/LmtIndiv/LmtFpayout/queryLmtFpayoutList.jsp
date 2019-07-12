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
		LmtFpayout._toForm(form);
		LmtFpayoutList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtFpayoutPage() {
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFpayoutUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFpayout() {
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFpayoutViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFpayoutPage() {
		var url = '<emp:url action="getLmtFpayoutAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtFpayout() {
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFpayoutRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtFpayoutGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddLmtFpayoutPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtFpayoutPage" label="修改" op="update"/>
		<emp:button id="deleteLmtFpayout" label="删除" op="remove"/>
		<emp:button id="viewLmtFpayout" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtFpayoutList" pageMode="false" url="pageLmtFpayoutQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" />
		<emp:text id="fpayout_type" label="家庭支出类型" dictname="STD_ZB_FPAYOUT_TYPE" />
		<emp:text id="mpayout" label="月支出" />
		<emp:text id="ypayout" label="年支出" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		FncAssure._toForm(form);
		FncAssureList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncAssurePage() {
		var paramStr = FncAssureList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncAssureUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncAssure() {
		var paramStr = FncAssureList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncAssureViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncAssurePage() {
		var url = '<emp:url action="getFncAssureAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncAssure() {
		var paramStr = FncAssureList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncAssureRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncAssureGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncAssureGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncAssure.rel_cus_id" label="对方客户代码" />
			<emp:text id="FncAssure.rel_cus_name" label="对方客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncAssurePage" label="新增" op="add"/>
		<emp:button id="getUpdateFncAssurePage" label="修改" op="update"/>
		<emp:button id="deleteFncAssure" label="删除" op="remove"/>
		<emp:button id="viewFncAssure" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncAssureList" pageMode="true" url="pageFncAssureQuery.do">
		<emp:text id="rel_cus_id" label="对方客户代码" />
		<emp:text id="rel_cus_name" label="对方客户名称" />
		<emp:text id="fnc_amt" label="金额" />
		<emp:text id="fnc_blc" label="余额" />
		<emp:text id="fnc_open_amt" label="敞口金额" />
		<emp:text id="sort_detail" label="明细类别" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="pk_id" label="主键" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
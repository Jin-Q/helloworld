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
		FncAccPayable._toForm(form);
		FncAccPayableList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncAccPayablePage() {
		var paramStr = FncAccPayableList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncAccPayableUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncAccPayable() {
		var paramStr = FncAccPayableList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncAccPayableViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncAccPayablePage() {
		var url = '<emp:url action="getFncAccPayableAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncAccPayable() {
		var paramStr = FncAccPayableList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncAccPayableRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncAccPayableGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncAccPayableGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncAccPayable.fnc_con_cus_name" label="对方客户名称" />
			<emp:text id="FncAccPayable.fnc_con_cus_id" label="对方客户代码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncAccPayablePage" label="新增" op="add"/>
		<emp:button id="getUpdateFncAccPayablePage" label="修改" op="update"/>
		<emp:button id="deleteFncAccPayable" label="删除" op="remove"/>
		<emp:button id="viewFncAccPayable" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncAccPayableList" pageMode="true" url="pageFncAccPayableQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="fnc_sum_amt" label="应付款余额合计" dataType="Currency"/>
		<emp:text id="fnc_imm_trm_amt" label="即期应付款" dataType="Currency"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="pk_id" label="主键" hidden="true" />

	</emp:table>
	
</body>
</html>
</emp:page>
    
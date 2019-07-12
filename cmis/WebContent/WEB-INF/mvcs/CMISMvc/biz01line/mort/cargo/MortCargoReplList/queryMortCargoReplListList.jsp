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
		MortCargoReplList._toForm(form);
		MortCargoReplListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoReplListPage() {
		var paramStr = MortCargoReplListList._obj.getParamStr(['cargo_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoReplListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortCargoReplList() {
		var paramStr = MortCargoReplListList._obj.getParamStr(['cargo_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoReplListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoReplListPage() {
		var url = '<emp:url action="getMortCargoReplListAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortCargoReplList() {
		var paramStr = MortCargoReplListList._obj.getParamStr(['cargo_id','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteMortCargoReplListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoReplListGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddMortCargoReplListPage" label="新增" op="add"/>
		<emp:button id="getUpdateMortCargoReplListPage" label="修改" op="update"/>
		<emp:button id="deleteMortCargoReplList" label="删除" op="remove"/>
		<emp:button id="viewMortCargoReplList" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortCargoReplListList" pageMode="false" url="pageMortCargoReplListQuery.do">
		<emp:text id="cargo_id" label="货物质押编号" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="cargo_name" label="货物名称" />
		<emp:text id="guaranty_catalog" label="押品所属目录" />
		<emp:text id="at_store" label="所属仓库" />
		<emp:text id="disp_bill_no" label="发货单号" />
		<emp:text id="produce_area" label="产地" />
		<emp:text id="produce_vender" label="生产厂家" />
		<emp:text id="sale_area" label="销售区域" />
		<emp:text id="model" label="型号" />
		<emp:text id="value_unit" label="计价单位" />
		<emp:text id="qnt" label="数量" />
		<emp:text id="identy_unit_price" label="银行认定单价" />
		<emp:text id="identy_total" label="银行认定总价" />
		<emp:text id="storage_date" label="入库日期" />
		<emp:text id="exware_date" label="出库日期" />
		<emp:text id="memo" label="备注" />
		<emp:text id="cargo_status" label="状态" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="reg_date" label="登记日期" />
		<emp:text id="serno" label="关联业务流水号" />
		<emp:text id="oper" label="操作类型（1--初次入库，2--补货，3--置出，4--置入，5--提货）" />
		<emp:text id="value_no" label="价格编号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
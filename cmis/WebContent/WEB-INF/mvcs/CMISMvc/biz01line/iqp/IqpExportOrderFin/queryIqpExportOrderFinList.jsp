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
		IqpExportOrderFin._toForm(form);
		IqpExportOrderFinList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExportOrderFinPage() {
		var paramStr = IqpExportOrderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportOrderFinUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExportOrderFin() {
		var paramStr = IqpExportOrderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportOrderFinViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExportOrderFinPage() {
		var url = '<emp:url action="getIqpExportOrderFinAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpExportOrderFin() {
		var paramStr = IqpExportOrderFinList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpExportOrderFinRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpExportOrderFinGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpExportOrderFinGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpExportOrderFin.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportOrderFin.limit_cont_no" label="额度合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpExportOrderFinPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExportOrderFinPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExportOrderFin" label="删除" op="remove"/>
		<emp:button id="viewIqpExportOrderFin" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpExportOrderFinList" pageMode="true" url="pageIqpExportOrderFinQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="order_cont_cur_type" label="订单合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="order_cont_amt" label="订单合同金额" />
		<emp:text id="biz_settl_mode" label="原业务结算方式" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
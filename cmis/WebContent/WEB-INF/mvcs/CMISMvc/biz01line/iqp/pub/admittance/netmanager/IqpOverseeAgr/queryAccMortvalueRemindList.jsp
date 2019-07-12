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
		AccMortvalueRemind._toForm(form);
		AccMortvalueRemindList._obj.ajaxQuery(null,form);
	};
	
	function doViewAccMortvalueRemind() {
		var paramStr = AccMortvalueRemindList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccMortvalueRemindViewPage.do"/>?isAdj=Y&isRemind=Y&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.AccMortvalueRemindGroup.reset();
	};
	function getReturnValueForGuarantyType(data){
		AccMortvalueRemind.catalog_no_displayname._setValue(data.locate_cn);
		AccMortvalueRemind.catalog_no._setValue(data.locate);
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccMortvalueRemindGroup" title="输入查询条件" maxColumn="3">
		<emp:pop id="AccMortvalueRemind.catalog_no_displayname" label="押品类型名称" url="showCatalogManaTree.do?&isMin=N" returnMethod="getReturnValueForGuarantyType"/>
		<emp:text id="AccMortvalueRemind.catalog_no" label="押品类型编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<%--<emp:button id="viewDpoDrfpoMana" label="跌价补偿" op="djbc"/>--%>
	</div>

	<emp:table icollName="AccMortvalueRemindList" pageMode="true" url="pageAccMortvalueRemindQuery.do">
		<emp:text id="catalog_no" label="押品类型代码" />
		<emp:text id="catalog_no_displayname" label="押品类型名称" />
		<emp:text id="market_value" label="最新核准价格" dataType="Currency"/>
		<emp:text id="identy_unit_price" label="原库存价格" dataType="Currency"/>
		<emp:text id="flt_rate" label="价格波动比例" dataType="Percent"/>
		<emp:text id="change_valve" label="价格变动阀值" dataType="Percent"/>
		<emp:text id="auth_date" label="生成日期" />
		<emp:link id="viewAccMortvalueRemind" label="操作" opName="查看" operation="viewAccMortvalueRemind"/>
		<emp:text id="reg_date" label="登记日期" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="value_no" label="价值编号" hidden="true"/>
		<emp:text id="cargo_id" label="货物质押编号" hidden="true"/>
		
	</emp:table>
	
</body>
</html>
</emp:page>
    
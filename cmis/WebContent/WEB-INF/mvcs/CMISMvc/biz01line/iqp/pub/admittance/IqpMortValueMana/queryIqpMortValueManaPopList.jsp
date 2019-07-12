<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpMortValueMana._toForm(form);
		IqpMortValueManaList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpMortValueManaGroup.reset();
	};

	function getReturnValueForGuarantyType(data){
		IqpMortValueMana.catalog_no_displayname._setValue(data.locate_cn);
		IqpMortValueMana.catalog_no._setValue(data.locate);
	}
	/*--user code begin--*/
	function doReturnMethod(){
		var data = IqpMortValueManaList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>
	<emp:gridLayout id="IqpMortValueManaGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpMortValueMana.value_no" label="价格编号" />
		<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="押品类型名称" url="showCatalogManaTree.do?&isMin=N" returnMethod="getReturnValueForGuarantyType"/>
		<emp:select id="IqpMortValueMana.status" label="状态" dictname="STD_ZB_STATUS" />
		<emp:text id="IqpMortValueMana.catalog_no" label="押品类型编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
	<emp:returnButton id="s1" label="选择返回"/>
	</div>
	<emp:table icollName="IqpMortValueManaList" pageMode="true" url="pageIqpMortValueManaPopQuery.do">
		<emp:text id="value_no" label="价格编号" />
		<emp:text id="catalog_name" label="货物名称" hidden="true"/>
		<emp:text id="catalog_no" label="押品类型" hidden="true" />
		<emp:text id="catalog_no_displayname" label="押品类型名称" />
		<emp:text id="produce_vender" label="生产厂家" />
		<emp:text id="produce_area" label="产地Id" hidden="true" />
		<emp:text id="produce_area_displayname" label="产地" />
		<emp:text id="sale_area" label="销售区域ID" hidden="true" />
		<emp:text id="sale_area_displayname" label="销售区域" />
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT"/>
		<emp:text id="market_value" label="市场价" dataType="Currency" />
		<emp:text id="auth_date" label="价格核准时间" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    
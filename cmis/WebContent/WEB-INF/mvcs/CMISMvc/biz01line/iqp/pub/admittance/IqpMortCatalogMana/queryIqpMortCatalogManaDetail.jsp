<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpMortCatalogManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		var _value = IqpMortCatalogMana.attr_type._getValue();
		if("02"==_value){  //含价商品
			IqpMortCatalogMana.imn_rate._obj._renderHidden(false);  //隐藏基准质押率
			IqpMortCatalogMana.model._obj._renderHidden(false); 
			IqpMortCatalogMana.imn_rate._obj._renderRequired(true); 
			IqpMortCatalogMana.model._obj._renderRequired(true); 
		}else{  //非含价商品
			IqpMortCatalogMana.imn_rate._obj._renderHidden(true);  //隐藏基准质押率
			IqpMortCatalogMana.model._obj._renderHidden(true); 
			IqpMortCatalogMana.imn_rate._obj._renderRequired(false); 
			IqpMortCatalogMana.model._obj._renderRequired(false); 
		}
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="IqpMortCatalogManaGroup" title="押品目录管理" maxColumn="2">
			<emp:text id="IqpMortCatalogMana.catalog_no" label="目录编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpMortCatalogMana.catalog_name" label="目录名称" maxlength="100" required="true" />
			<emp:text id="IqpMortCatalogMana.commo_trait" label="商品特性" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpMortCatalogMana.sup_catalog_no" label="上级目录ID" maxlength="100" hidden="true" defvalue="ALL" readonly="true"/>
			<emp:pop id="IqpMortCatalogMana.sup_catalog_no_displayname" label="上级目录" url="showCatalogManaTree.do" returnMethod="setCatalogPath" required="false" defvalue="押品目录"/> 
			<emp:text id="IqpMortCatalogMana.catalog_path" label="目录路径location" maxlength="200" required="true" hidden="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="IqpMortCatalogMana.catalog_path_displayname" label="目录路径"   required="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="IqpMortCatalogMana.catalog_lvl" label="押品目录层级" maxlength="1" required="true" dataType="Int" defvalue="1" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpMortCatalogMana.attr_type" label="类型属性 " required="true" dictname="STD_ZB_ATTR_TYPE" />
			<emp:text id="IqpMortCatalogMana.model" label="型号" required="true" maxlength="40"/>
			<emp:text id="IqpMortCatalogMana.imn_rate" label="基准质押率" maxlength="16" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="IqpMortCatalogMana.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="IqpMortCatalogMana.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpMortCatalogMana.input_br_id_displayname" label="登记机构"   required="true" readonly="true" />
			<emp:text id="IqpMortCatalogMana.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="IqpMortCatalogMana.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:date id="IqpMortCatalogMana.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:select id="IqpMortCatalogMana.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="2" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="return" label="返回列表"/>
		</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
</body>
</html>
</emp:page>

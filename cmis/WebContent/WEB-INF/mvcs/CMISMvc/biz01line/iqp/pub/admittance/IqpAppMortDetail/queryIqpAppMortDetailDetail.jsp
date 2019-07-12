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
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAppMortDetailGroup" title="押品目录准入明细" maxColumn="2">
			<emp:text id="IqpAppMortDetail.serno" label="业务流水号" maxlength="40" required="true" hidden="true"/>
		    <emp:text id="IqpAppMortDetail.catalog_no" label="目录编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpAppMortDetail.catalog_name" label="目录名称" maxlength="100" required="true" />
			<emp:text id="IqpAppMortDetail.commo_trait" label="商品特性" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpAppMortDetail.sup_catalog_no" label="上级目录ID" maxlength="100" hidden="true" defvalue="ALL" readonly="true"/>
			<emp:pop id="IqpAppMortDetail.sup_catalog_no_displayname" label="上级目录" url="showCatalogManaTree.do?isMin=N" returnMethod="setCatalogPath" required="false" defvalue="押品目录"/> 
			<emp:text id="IqpAppMortDetail.catalog_path" label="目录路径location" maxlength="200" required="true" hidden="true" cssElementClass="emp_field_text_readonly" defvalue="ALL"/>
			<emp:text id="IqpAppMortDetail.catalog_path_displayname" label="目录路径"  required="true" colSpan="2" cssElementClass="emp_field_text_readonly" defvalue="押品目录" hidden="true"/>
			<emp:text id="IqpAppMortDetail.catalog_lvl" label="押品目录层级" maxlength="1" required="true" dataType="Int" defvalue="1" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAppMortDetail.attr_type" label="类型属性 " required="true" dictname="STD_ZB_ATTR_TYPE" defvalue="02" readonly="true"/>
			<emp:text id="IqpAppMortDetail.model" label="型号" required="true" maxlength="40"/>
			<emp:text id="IqpAppMortDetail.imn_rate" label="基准质押率" maxlength="16" required="true" dataType="Percent" />
			<emp:textarea id="IqpAppMortDetail.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="IqpAppMortDetail.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpAppMortDetail.input_br_id_displayname" label="登记机构"  required="true" readonly="true" />
			<emp:text id="IqpAppMortDetail.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="IqpAppMortDetail.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" />
			<emp:date id="IqpAppMortDetail.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:select id="IqpAppMortDetail.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="0" hidden="true"/>
			<emp:text id="IqpAppMortDetail.oper_type" label="操作类型：1-准入 2-退出 " maxlength="1" required="false" defvalue="1" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

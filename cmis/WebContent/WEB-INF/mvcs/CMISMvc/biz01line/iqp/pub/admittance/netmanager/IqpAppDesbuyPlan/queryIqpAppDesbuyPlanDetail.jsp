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
<style type="text/css">
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
text-align:left;
width:450px;
border-color: #b7b7b7;
background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAppDesbuyPlanList.do"/>'+"&mem_cus_id=${context.mem_cus_id}"
                  +"&serno=${context.serno}"
                  +"&cus_id=${context.cus_id}"
                  +"&mem_manuf_type=${context.mem_manuf_type}";
        url = EMPTools.encodeURI(url);
        window.location = url; 
	}; 
	function doClose(){
        window.close();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAppDesbuyPlanGroup" title="订货计划信息" maxColumn="2">
			<emp:text id="IqpAppDesbuyPlan.desgoods_plan_no" label="订货流水号" maxlength="40" required="false" hidden="false"/>
			<emp:text id="IqpAppDesbuyPlan.for_manuf" label="供货厂商客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppDesbuyPlan.for_manuf_displayname" label="供货厂商名称" required="true" readonly="true"/>
			<emp:pop id="IqpAppDesbuyPlan.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppDesbuyPlan.commo_name" label="商品名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.desbuy_qnt" label="订购数量" maxlength="16" required="true"  />
			<emp:select id="IqpAppDesbuyPlan.desbuy_qnt_unit" label="订购数量单位" required="true"  dictname="STD_ZB_UNIT" />
			<emp:text id="IqpAppDesbuyPlan.desbuy_unit_price" label="订购单价（元）" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpAppDesbuyPlan.desbuy_total" label="订购总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:date id="IqpAppDesbuyPlan.fore_disp_date" label="预计发货日期"  required="true" />
			<emp:textarea id="IqpAppDesbuyPlan.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="IqpAppDesbuyPlan.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.start_date" label="起始日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.end_date" label="到期日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			
	        <emp:text id="IqpAppDesbuyPlan.cus_id" label="客户码" maxlength="30" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.cus_id_displayname" label="客户名称"   required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

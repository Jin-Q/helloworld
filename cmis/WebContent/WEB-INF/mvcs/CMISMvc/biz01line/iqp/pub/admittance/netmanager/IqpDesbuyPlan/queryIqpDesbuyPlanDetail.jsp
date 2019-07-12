<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
text-align:left;
width:450px;
border-color: #b7b7b7;
background-color: #e3e3e3;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpDesbuyPlanList.do"/>'+"&mem_cus_id=${context.mem_cus_id}"
                                                                 +"&net_agr_no=${context.net_agr_no}"
                                                                 +"&cus_id=${context.cus_id}"
				                                                 +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doClose(){
        window.close();
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpDesbuyPlanGroup" title="订货计划信息" maxColumn="2">
			<emp:text id="IqpDesbuyPlan.desgoods_plan_no" label="订货流水号"  required="true" colSpan="2"/>
			<emp:pop id="IqpDesbuyPlan.cus_id" label="供货厂商客户码" url="" required="false" />
			<emp:text id="IqpDesbuyPlan.cus_id_displayname" label="供货厂商客户名称" required="false" />
			<emp:text id="IqpDesbuyPlan.commo_name" label="商品名称" required="true" hidden="true"/>
			<emp:pop id="IqpDesbuyPlan.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpDesbuyPlan.desbuy_qnt" label="订购数量"  required="true"/>
			<emp:select id="IqpDesbuyPlan.desbuy_qnt_unit" label="订购数量单位" required="true"  dictname="STD_ZB_UNIT" />
			<emp:text id="IqpDesbuyPlan.desbuy_unit_price" label="订购单价（元）"  required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpDesbuyPlan.desbuy_total" label="订购总价（元）"  required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpDesbuyPlan.fore_disp_date" label="预计发货时间" required="true"/>	
			<emp:textarea id="IqpDesbuyPlan.memo" label="备注"  required="false" colSpan="2" />			
			<emp:text id="IqpDesbuyPlan.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:date id="IqpDesbuyPlan.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.net_agr_no" label="网络编号"  required="false" hidden="true"/>
			<emp:date id="IqpDesbuyPlan.start_date" label="起始日期" required="false" hidden="true"/>
			<emp:date id="IqpDesbuyPlan.end_date" label="到期日期" required="false" hidden="true"/>
			<emp:select id="IqpDesbuyPlan.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:text id="IqpDesbuyPlan.input_id" label="登记人"  required="false" hidden="true"/>
			<emp:text id="IqpDesbuyPlan.input_br_id" label="登记机构"  required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

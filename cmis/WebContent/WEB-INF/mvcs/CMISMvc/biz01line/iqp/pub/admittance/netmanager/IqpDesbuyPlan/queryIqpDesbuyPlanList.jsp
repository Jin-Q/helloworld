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
		IqpDesbuyPlan._toForm(form);
		IqpDesbuyPlanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDesbuyPlanPage() {
		var paramStr = IqpDesbuyPlanList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDesbuyPlanUpdatePage.do"/>?'+paramStr
																		  +"&mem_cus_id=${context.mem_cus_id}"
																	      +"&net_agr_no=${context.net_agr_no}"
																	      +"&cus_id=${context.cus_id}"
																	      +"&mem_manuf_type=${context.mem_manuf_type}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDesbuyPlan() {
		var paramStr = IqpDesbuyPlanList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDesbuyPlanViewPage.do"/>?'+paramStr
			                                                           +"&mem_cus_id=${context.mem_cus_id}"
			                                                           +"&net_agr_no=${context.net_agr_no}"
			                                                           +"&cus_id=${context.cus_id}"
																	   +"&mem_manuf_type=${context.mem_manuf_type}";
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDesbuyPlanPage() {
		var url = '<emp:url action="getIqpDesbuyPlanAddPage.do"/>?mem_cus_id=${context.mem_cus_id}'
																+"&net_agr_no=${context.net_agr_no}"
																+"&cus_id=${context.cus_id}"
															    +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpDesbuyPlan() {
		var paramStr = IqpDesbuyPlanList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpDesbuyPlanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDesbuyPlanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:actButton id="viewIqpDesbuyPlan" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpDesbuyPlanList" pageMode="true" url="pageIqpDesbuyPlanQuery.do?net_agr_no=${context.net_agr_no}&mem_manuf_type=${context.mem_manuf_type}&cus_id=${context.cus_id}&mem_cus_id=${context.mem_cus_id}">
		<emp:text id="desgoods_plan_no" label="订货计划流水号" hidden="false"/>
		<emp:text id="for_manuf_displayname" label="供货厂商名称" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="commo_name_displayname" label="商品名称" />
		<emp:text id="desbuy_qnt" label="订购数量" />
		<emp:text id="desbuy_qnt_unit" label="单位" dictname="STD_ZB_UNIT" />
		<emp:text id="desbuy_unit_price" label="订购单价（元）" dataType="Currency" />
		<emp:text id="desbuy_total" label="订购总价（元）" dataType="Currency" />
		<emp:text id="fore_disp_date" label="预计发货日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
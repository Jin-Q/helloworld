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
		PrdRepayPlan._toForm(form);
		PrdRepayPlanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdRepayPlanPage() {
		var paramStr = PrdRepayPlanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdRepayPlan() {
		var paramStr = PrdRepayPlanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdRepayPlanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdRepayPlanPage() {
		var url = '<emp:url action="getPrdRepayPlanAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdRepayPlan() {
		var paramStr = PrdRepayPlanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdRepayPlanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdRepayPlanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPrdRepayPlanPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdRepayPlanPage" label="修改" op="update"/>
		<emp:button id="deletePrdRepayPlan" label="删除" op="remove"/>
		<emp:button id="viewPrdRepayPlan" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdRepayPlanList" pageMode="true" url="pagePrdRepayPlanQuery.do">
		<emp:text id="serno" label="序号" />
		<emp:text id="exe_times" label="执行期数" />
		<emp:text id="repay_mode" label="还款方式" />
		<emp:text id="rate_type" label="利率类型" />
		<emp:text id="rate_sprd" label="利差" />
		<emp:text id="rate_pefloat" label="利率浮动比例" />
		<emp:text id="rate_cal_basic" label="利率计算基础" />
		<emp:text id="repay_interval" label="还款间隔" />
		<emp:text id="repay_trem" label="还款间隔周期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		IqpAppDesbuyPlan._toForm(form);
		IqpAppDesbuyPlanList._obj.ajaxQuery(null,form);
	};	
	
	/*--user code begin--*/
		function doSelect(){
		var methodName = '${context.returnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpAppDesbuyPlanList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:returnButton label="选择返回"/> <br>
	<emp:table icollName="IqpAppDesbuyPlanList" pageMode="true" url="pageIqpDesbuyPlanQuery.do?serno=${context.serno}&mem_cus_id=${cotnext.mem_cus_id}">
		<emp:text id="desgoods_plan_no" label="订货计划流水号" hidden="false"/>
		<emp:text id="for_manuf_displayname" label="供货厂商名称" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="commo_name_displayname" label="商品名称" />
		<emp:text id="desbuy_qnt" label="订购数量" />
		<emp:text id="desbuy_qnt_unit" label="单位" dictname="STD_ZB_UNIT" />
		<emp:text id="desbuy_unit_price" label="订购单价（元）" dataType="Currency" />
		<emp:text id="desbuy_total" label="订购总价（元）" dataType="Currency" />
		<emp:text id="fore_disp_date" label="预计发货日期" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>
    
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>FLASH示例</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/flashcharts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script> 
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>

<script type="text/javascript">
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	
		<div id="chartdiv1" >
			this is pic !
		</div>
	
		<div id="chartdiv2" > 
			this is pic !
		</div>
		
		<div id="chartdiv3"> 
			this is pic !
		</div>
	
	<script type="text/javascript">
		var xml = "${context.singleXML}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","700","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXML}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_MSColumn3D.swf","myChartid","700","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.complexXML}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_MSColumn3DLineDY.swf","myChartid","700","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv3");
	</script>
	
	
	
</body>
</html>
</emp:page>
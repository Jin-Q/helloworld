<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>风险预警信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/dthPubAction.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/flashcharts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

</script>
</head>
<body class="page_content" >
	
		<table>
			<tr><td width="48%"><div id="chartdiv8"></div></td><td width="48%"><div id="chartdiv9"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td width="48%"><div id="chartdiv10"></div></td><td width="48%"><div id="chartdiv11"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td width="48%"><div id="chartdiv12"></div></td><td width="48%"><div id="chartdiv13"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv14"></div></td></tr>
		</table>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLEight}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv8");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLNine}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv9");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTen}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv10");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLEleven}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv11");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTwelve}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv12");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLThirteen}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv13");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLFourteen}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv14");
	</script>
	
</body>
</html>
</emp:page>
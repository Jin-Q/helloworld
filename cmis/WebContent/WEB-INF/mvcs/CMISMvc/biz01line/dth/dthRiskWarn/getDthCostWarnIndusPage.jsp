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
			<tr><td colspan="2"><div id="chartdiv2"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv3"></div></td></tr>
		</table>

	<script type="text/javascript">
		var xml = "${context.multiXMLTwo}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie3D.swf","myChartid","1000","600");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLThree}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","1000","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv3");
	</script>
	
</body>
</html>
</emp:page>
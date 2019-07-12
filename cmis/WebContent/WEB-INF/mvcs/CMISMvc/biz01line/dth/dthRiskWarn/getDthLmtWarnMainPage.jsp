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
	function getLmtWarnDetailFive(obj){
	    var url = "<emp:url action='getDthLmtWarnDetailPage.do'/>&type=Five&value="+obj;
	    getPubDetailsPop(url,"","");
	};
	function getLmtWarnChartThreefour(obj){
	    var url = "<emp:url action='getDthLmtWarnDetailPage.do'/>&type=Threefour&value="+obj;
	    getPubDetailsPop(url,"","");
	};
	function getLmtWarnChartOnetwo(obj){
	    var url = "<emp:url action='getDthLmtWarnDetailPage.do'/>&type=Onetwo&value="+obj;
	    getPubDetailsPop(url,"","");
	};
</script>
</head>
<body class="page_content" >
	<emp:tabGroup id="abs" mainTab="tab1">
		<emp:tab id="tab1" label="授信整体信息" initial="true">
		<table>
			<tr><td colspan="2"><div id="chartdiv1"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv2"></div></td></tr>
		</table>
		</emp:tab>
		<emp:tab id="tab2" label="授信综合信息" initial="true">
		<table>
			<tr><td width="50%"><div id="chartdiv3"></div></td><td width="50%"><div id="chartdiv4"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv5"></div></td></tr>
		</table>
		</emp:tab>
		<emp:tab id="tab3" label="授信十大户信息" initial="true">
		<table>
			<tr><td width="50%"><div id="chartdiv6"></div></td><td width="50%"><div id="chartdiv7"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td width="50%"><div id="chartdiv9"></div></td><td width="50%"><div id="chartdiv8"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td width="50%"><div id="chartdiv10"></div></td><td width="50%"><div id="chartdiv11"></div></td></tr>
		</table>
		</emp:tab>
	</emp:tabGroup>

	<script type="text/javascript">
		var xml = "${context.multiXMLOne}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_MSColumn3D.swf","myChartid","1000","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTwo}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_MSColumn3D.swf","myChartid","1000","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLThree}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv3");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLFour}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv4");
	</script>

	<script type="text/javascript">
		var xml = "${context.multiXMLFive}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","560","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv5");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLSix}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv6");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLSeven}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","550","400");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv7");
	</script>
	
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
	
</body>
</html>
</emp:page>
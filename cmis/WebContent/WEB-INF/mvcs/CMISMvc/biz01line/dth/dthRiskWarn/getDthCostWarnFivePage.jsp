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

	function getCostWarnDetailsBLall(obj){
	    var url = "<emp:url action='getDthCostWarnDetailPage.do'/>?type=four&BL=BLall&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
	function getCostWarnDetailsBL100(obj){
	    var url = "<emp:url action='getDthCostWarnDetailPage.do'/>?type=four&BL=BL100&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
	function getCostWarnDetailsBL200(obj){
	    var url = "<emp:url action='getDthCostWarnDetailPage.do'/>?type=four&BL=BL200&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
	function getCostWarnDetailsBL300(obj){
	    var url = "<emp:url action='getDthCostWarnDetailPage.do'/>?type=four&BL=BL300&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
</script>
</head>
<body class="page_content" >

		<table>
			<tr><td width="48%"><div id="chartdiv4"></div></td><td width="48%"><div id="chartdiv5"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td width="48%"><div id="chartdiv6"></div></td><td width="48%"><div id="chartdiv7"></div></td></tr>
		</table>

	
	<script type="text/javascript">
		var xml = "${context.multiXMLFour}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv4");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLFive}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv5");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLSix}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv6");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLSeven}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv7");
	</script>
	
</body>
</html>
</emp:page>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>贷款指标分析</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/dthPubAction.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/flashcharts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	function getIndivBadAnalyDetailsOne(data){
	    var url = "<emp:url action='getDthIndivBadAnalyDetailPage.do'/>?type=one&value="+data;
	    getPubDetailsPop(url,"","1250");
	};
	
	function getIndivBadAnalyChartTwo(data){
		var url = "<emp:url action='getDthIndivBadAnalyDetailPage.do'/>?type=two&value="+data;
		getPubDetailsPop(url,"","1250");
	};
</script>
</head>

<body class="page_content" >

	<div id="chartdiv1"></div>
	<br>
	<div id="chartdiv2" ></div>
	<script type="text/javascript">
		var xml = "${context.multiXMLOne}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","800","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTwo}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","800","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>

</body>
</html>
</emp:page>
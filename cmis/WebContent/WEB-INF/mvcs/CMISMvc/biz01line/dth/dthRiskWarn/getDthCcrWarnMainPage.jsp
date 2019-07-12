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
	function getCcrWarnDetails(data){
	    var url = "<emp:url action='getDthCcrWarnDetailPage.do'/>?type=one&value="+data;
	    getPubDetailsPop(url,"","");
	};
	
	function getCcrWarnDetailsTwo(data){
		var url = "<emp:url action='getDthCcrWarnDetailPage.do'/>?type=two&value="+data;
		getPubDetailsPop(url,"","");
	};
</script>
</head>

<body class="page_content" >

	<div id="chartdiv1"></div>
	<br>
	<div id="chartdiv2" ></div>
	<script type="text/javascript">
		var xml = "${context.multiXML}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_MSColumn3D.swf","myChartid","1000","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTwo}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","1000","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>

</body>
</html>
</emp:page>
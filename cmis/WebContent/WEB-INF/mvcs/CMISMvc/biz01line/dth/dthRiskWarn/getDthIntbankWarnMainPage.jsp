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
	function getIntbankWarnDetails(obj){
	    var url = "<emp:url action='getDthIntbankWarnDetailPage.do'/>&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
</script>
</head>

<body class="page_content" >

	<div id="chartdiv1">this is pic !</div>
	<script type="text/javascript">
		var xml = "${context.singleXML}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","800","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>

</body>
</html>
</emp:page>
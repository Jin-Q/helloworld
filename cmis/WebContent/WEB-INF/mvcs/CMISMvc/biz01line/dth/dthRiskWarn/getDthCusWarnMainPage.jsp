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
	function getCusWarnDetails(obj){
	    var url = "<emp:url action='getDthCusWarnDetailPage.do'/>&type=one&value="+obj;
	    getPubDetailsPop(url,"450","1350");
	};
	function getCusAgtDetailsF(obj){
	    var url = "<emp:url action='getDthCusWarnDetailPage.do'/>&type=F&value="+obj;
	    getPubDetailsPop(url,"350","900");
	};
	function getCusAgtDetailsZ(obj){
	    var url = "<emp:url action='getDthCusWarnDetailPage.do'/>&type=Z&value="+obj;
	    getPubDetailsPop(url,"350","900");
	};
</script>
</head>

<body class="page_content" >
	
	<emp:tabGroup id="abs" mainTab="tab1">
		<emp:tab id="tab1" label="存款信息" initial="true">
		<table>
			<tr><td colspan="2"><div id="chartdiv1"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv2"></div></td></tr>
		</table>
		</emp:tab>
		<emp:tab id="tab2" label="关联客户信息" initial="true">
		<table>
			<tr><td width="50%"><div id="chartdiv3"></div></td><td width="50%"><div id="chartdiv4"></div></td></tr>
			<tr height="20"><td>&nbsp;</td></tr>
			<tr><td colspan="2"><div id="chartdiv5"></div></td></tr>
		</table>
		</emp:tab>
	</emp:tabGroup>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLOne}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","1000","490");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv3");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLTwo}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","1000","450");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
	<script type="text/javascript">
		var xml = "${context.multiXMLThree}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","1000","450");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv2");
	</script>

</body>
</html>
</emp:page>
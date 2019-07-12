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
	function getLoanBalanceAnalyDetails(obj){	//可以与图形一(贷款余额)共用
	    var url = "<emp:url action='getDthLoanBalanceDetailPage.do'/>&value="+obj;
	    getPubDetailsPop(url,"","1200");
	};
</script>
</head>

<body class="page_content" >

	<div id="chartdiv1">this is pic !</div>
	<script type="text/javascript">
		var xml = "${context.singleXML}"  
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Column3D.swf","myChartid","950","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>

</body>
</html>
</emp:page>
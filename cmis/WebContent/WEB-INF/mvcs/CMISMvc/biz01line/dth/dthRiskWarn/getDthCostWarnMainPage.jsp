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
	function getCostWarnDetailsOne(obj){
	    var url = "<emp:url action='getDthCostWarnDetailPage.do'/>?type=one&value="+obj;
	    getPubDetailsPop(url,"","1000");
	};
</script>
</head>
<body class="page_content" >

	<emp:tabGroup id="abs" mainTab="tab1">
		<emp:tab id="tab1" label="逾期业务信息" initial="true">
		<table>
			<tr><td colspan="2"><div id="chartdiv1"></div></td></tr>
		</table>
		</emp:tab>
		<emp:tab id="tab2" label="行业分类信息" initial="false" url="getDthCostWarnIndusPage.do">
		</emp:tab>
		<emp:tab id="tab3" label="用信五级分类信息" initial="false" url="getDthCostWarnFivePage.do">
		</emp:tab>
		<emp:tab id="tab4" label="用信十大户信息" initial="false" url="getDthCostWarnTenPage.do">
		</emp:tab>
	</emp:tabGroup>

	<script type="text/javascript">
		var xml = "${context.multiXMLOne}" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","1000","300");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv1");
	</script>
	
</body>
</html>
</emp:page>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	int nums = Integer.parseInt(context.getDataValue("nums").toString()) ;
%>
<emp:page>

<html>
<head>
<title>贷款指标分析</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/dthPubAction.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/flashcharts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	function getLoanDirectionDetails(data){
	    var url = "<emp:url action='getDthIndivBadAnalyDetailPage.do'/>?type=prd&value="+data;
	    getPubDetailsPop(url,"","1250");
	};
</script>
</head>

<body class="page_content" >

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr><td width="50%"><div id="chartdiv1"></div></td><td width="50%"><div id="chartdiv2"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
		<tr><td width="50%"><div id="chartdiv3"></div></td><td width="50%"><div id="chartdiv4"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
		<tr><td width="50%"><div id="chartdiv5"></div></td><td width="50%"><div id="chartdiv6"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
		<tr><td width="50%"><div id="chartdiv7"></div></td><td width="50%"><div id="chartdiv8"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
		<tr><td width="50%"><div id="chartdiv9"></div></td><td width="50%"><div id="chartdiv10"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
		<tr><td width="50%"><div id="chartdiv11"></div></td><td width="50%"><div id="chartdiv12"></div></td></tr>
		<tr height="20"><td>&nbsp;</td></tr>
	</table>

	<%
		for(int i = 0;i < nums; i++){
			String xml = context.getDataValue("multiXML"+i).toString();
	%>
	<script type="text/javascript">
		var xml = "<%=xml%>" ;
		var myChart = new FusionCharts("scripts/flashcharts/swfs/FCF_Pie2D.swf","myChartid","550","300");
		myChart.setDataXML(xml);
		var chartdiv = "chartdiv" + "<%=i+1%>" ;
		myChart.render(chartdiv);
	</script>
	<%
		}
	%>
	
</body>
</html>
</emp:page>
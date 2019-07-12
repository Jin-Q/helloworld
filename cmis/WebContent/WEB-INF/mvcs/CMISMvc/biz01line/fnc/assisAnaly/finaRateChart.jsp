<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>财务比率分析页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function setDisplayType(){
		var fnc_type = FinaRate.fnc_type._getValue();
		if(fnc_type == '01'){
			FinaRate.display_type._setValue('2');
			FinaRate.display_type._obj._renderReadonly(true);
		}else{
			FinaRate.display_type._obj._renderReadonly(false);
		}
	};
	function doBarGraph(){
		var result = FinaRate._checkAll();
		if(result){
			perSubmit();
			var url = '<emp:url action="getFinaRateResult.do"/>'+paramStr+'&chartType=bar';
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		}else {
           return ;
		}
	};

	function doLinearGraph(){
		var result = FinaRate._checkAll();
		if(result){
			perSubmit();
			var url = '<emp:url action="getFinaRateResult.do"/>'+paramStr+'&chartType=line';
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		}else {
           return ;
		}
	};

	function doLoad(){
		cus_id = "${context.cus_id}";
		stat_prd_style = "${context.stat_prd_style}";
		stat_prd = "${context.stat_prd}";
		stat_style = "${context.stat_style}";
		paramStr = '?cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd+'&stat_style='+stat_style;
	};

	function perSubmit(){
		var rate_type = FinaRate.rate_type._getValue();
		var fnc_type = FinaRate.fnc_type._getValue();
		var display_type = FinaRate.display_type._getValue();
		paramStr = paramStr + '&rate_type='+rate_type+'&showType='+fnc_type+'&display_type='+display_type;
	};
	
/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doLoad()" >
	<emp:form id="submitForm" action="getFlashChart.do" method="POST">
		<emp:gridLayout id="FinaRateGroup" title="财务比率分析条件选择" maxColumn="2">
			<emp:checkbox id="FinaRate.rate_type" label="财务比率分类（默认全选）" layout="false"  dictname="STD_ZB_FINA_RATE_TYPE" colSpan="2" ></emp:checkbox>
			<emp:select id="FinaRate.fnc_type"  label="结果显示类型"  dictname="STD_ZB_FNC_CHART" defvalue="01" required="true" onchange="setDisplayType()"/>
			<emp:select id="FinaRate.display_type"  label="是否仅显示比较" required="true" dictname="STD_ZX_YES_NO" readonly="true" defvalue="2"/>
		</emp:gridLayout>
		<div align="center">
			<emp:button id="barGraph" label="柱状图" />
			<emp:button	id="linearGraph" label="线状图" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>
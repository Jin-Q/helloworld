<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpHouseInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpHouseInfoGroup" title="厂房信息" maxColumn="2">
			<emp:text id="IqpHouseInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpHouseInfo.house_addr" label="厂房位置" maxlength="100" required="false" />
			<emp:text id="IqpHouseInfo.pur_amt" label="购买金额" maxlength="16" required="false" dataType="Currency" />
			<emp:date id="IqpHouseInfo.pur_time" label="购买时间" required="false" />
			<emp:text id="IqpHouseInfo.fst_pyr_perc" label="首付款比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpHouseInfo.house_squ" label="建筑面积" maxlength="10" required="false" dataType="Double" />
			<emp:text id="IqpHouseInfo.occup_squ" label="占地面积" maxlength="10" required="false" dataType="Double" />
			<emp:select id="IqpHouseInfo.building_structure_cd" label="建筑结构" required="false" dictname="STD_ARCH_STR" />
			<emp:select id="IqpHouseInfo.fitment_degree" label="装修程度" required="false" dictname="STD_FITMENT_DEGREE" />
			<emp:date id="IqpHouseInfo.house_build_year" label="房屋建成年份" required="false" />
			<emp:text id="IqpHouseInfo.durable_years" label="耐用年限" maxlength="10" required="false" dataType="Double" />
			<emp:select id="IqpHouseInfo.street_situation" label="临街状况" required="false" dictname="STD_FRONTAGE_STATUS" />
			<emp:select id="IqpHouseInfo.use_status" label="使用状态" required="false" dictname="STD_USE_STATUS" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

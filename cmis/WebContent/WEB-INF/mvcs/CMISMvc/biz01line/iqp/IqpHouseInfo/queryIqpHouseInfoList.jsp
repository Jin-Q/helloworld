<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpHouseInfo._toForm(form);
		IqpHouseInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpHouseInfoPage() {
		var paramStr = IqpHouseInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpHouseInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpHouseInfo() {
		var paramStr = IqpHouseInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpHouseInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpHouseInfoPage() {
		var url = '<emp:url action="getIqpHouseInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpHouseInfo() {
		var paramStr = IqpHouseInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpHouseInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpHouseInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpHouseInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpHouseInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpHouseInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpHouseInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpHouseInfoList" pageMode="false" url="pageIqpHouseInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="house_addr" label="厂房位置" />
		<emp:text id="pur_amt" label="购买金额" />
		<emp:text id="pur_time" label="购买时间" />
		<emp:text id="fst_pyr_perc" label="首付款比例" />
		<emp:text id="house_squ" label="建筑面积" />
		<emp:text id="occup_squ" label="占地面积" />
		<emp:text id="building_structure_cd" label="建筑结构" dictname="STD_ARCH_STR" />
		<emp:text id="fitment_degree" label="装修程度" dictname="STD_FITMENT_DEGREE" />
		<emp:text id="house_build_year" label="房屋建成年份" />
		<emp:text id="durable_years" label="耐用年限" />
		<emp:text id="street_situation" label="临街状况" dictname="STD_FRONTAGE_STATUS" />
		<emp:text id="use_status" label="使用状态" dictname="STD_USE_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
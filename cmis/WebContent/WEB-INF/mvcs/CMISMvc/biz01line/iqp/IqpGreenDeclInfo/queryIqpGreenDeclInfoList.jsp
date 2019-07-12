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
		IqpGreenDeclInfo._toForm(form);
		IqpGreenDeclInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpGreenDeclInfoPage() {
		var paramStr = IqpGreenDeclInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGreenDeclInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpGreenDeclInfo() {
		var paramStr = IqpGreenDeclInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGreenDeclInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpGreenDeclInfoPage() {
		var url = '<emp:url action="getIqpGreenDeclInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpGreenDeclInfo() {
		var paramStr = IqpGreenDeclInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpGreenDeclInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpGreenDeclInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpGreenDeclInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpGreenDeclInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpGreenDeclInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpGreenDeclInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpGreenDeclInfoList" pageMode="false" url="pageIqpGreenDeclInfoQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="green_indus" label="绿色产业类型" dictname="STD_ZB_GREEN_INDUS" />
		<emp:text id="reduc_coal" label="项目年节约标准煤量" />
		<emp:text id="emission_co2" label="项目年减排二氧化碳量" />
		<emp:text id="emission_cod" label="项目年COD减排量" />
		<emp:text id="emission_an" label="项目年氨氮减排量" />
		<emp:text id="emission_so2" label="项目二氧化硫减排量" />
		<emp:text id="emission_no" label="项目年氮氧化物减排量" />
		<emp:text id="reduc_water" label="项目年节水量" />
		<emp:text id="totl_invest" label="项目总投资额" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
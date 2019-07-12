<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddPrdPreventRiskPage(){
		var url = '<emp:url action="getPrdPreventRiskAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdPreventRisk(){		
		var paramStr = PrdPreventRiskList._obj.getParamStr(['prevent_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdPreventRiskRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdatePrdPreventRiskPage(){
		var paramStr = PrdPreventRiskList._obj.getParamStr(['prevent_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPreventRiskUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewPrdPreventRisk(){
		var paramStr = PrdPreventRiskList._obj.getParamStr(['prevent_id']);
		if (paramStr != null) {
			var url = '<emp:url action="queryPrdPreventRiskDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdPreventRisk._toForm(form);
		PrdPreventRiskList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PrdPreventRiskGroup.reset();
	};
	
	/*--user code begin--*/
	function doTestPrdPreventRisk(){
		var _applType="";
		var _wfid="wfi_107_jr";
		var _modelId="IqpLoanApp";
		var _pkVal="SQ000000000001";
		var _nodeId="";
		var _preventIdLst="FFFA27800134C0A6E9F203C01240D4C1";
		var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&wfid="+_wfid + "&nodeId="+_nodeId+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
	    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
	}
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPreventRiskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPreventRisk.prevent_id" label="方案编号" />
			<emp:text id="PrdPreventRisk.prevent_desc" label="方案名称" />
			<emp:select id="PrdPreventRisk.used_ind" label="是否使用" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddPrdPreventRiskPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdPreventRiskPage" label="修改" op="update"/>
		<emp:button id="deletePrdPreventRisk" label="删除" op="remove"/>
		<emp:button id="viewPrdPreventRisk" label="查看" op="view"/>
		<emp:button id="testPrdPreventRisk" label="风险拦截测试"/>
	</div>
	<emp:table icollName="PrdPreventRiskList" pageMode="true" url="pagePrdPreventRiskQuery.do">
		<emp:text id="prevent_id" label="方案编号" />
		<emp:text id="prevent_desc" label="方案名称" />
		<emp:text id="used_ind" label="是否使用" dictname="STD_ZX_YES_NO" />
	</emp:table>
</body>
</html>
</emp:page>
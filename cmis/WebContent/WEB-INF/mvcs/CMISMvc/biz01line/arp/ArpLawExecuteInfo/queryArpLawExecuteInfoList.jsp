<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpLawExecuteInfo._toForm(form);
		ArpLawExecuteInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpLawExecuteInfoPage() {
		var paramStr = ArpLawExecuteInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawExecuteInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawExecuteInfo() {
		var paramStr = ArpLawExecuteInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawExecuteInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawExecuteInfoPage() {
		var url = '<emp:url action="getArpLawExecuteInfoAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawExecuteInfo() {
		var paramStr = ArpLawExecuteInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawExecuteInfoRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawExecuteInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doLoad(){
		case_no = "${context.case_no}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpLawExecuteInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="ArpLawExecuteInfo.lawsuit_phase" label="当前诉讼阶段" dictname="STD_ZB_LAWSUIT_PHASE" />
			<emp:select id="ArpLawExecuteInfo.exe_status" label="执行现状" dictname="STD_ZB_EXE_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawExecuteInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawExecuteInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawExecuteInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawExecuteInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawExecuteInfoList" pageMode="true" url="pageArpLawExecuteInfoQuery.do?case_no=${context.case_no}">
		<emp:text id="lawsuit_phase" label="当前诉讼阶段" dictname="STD_ZB_LAWSUIT_PHASE" />
		<emp:text id="exe_status" label="执行现状" dictname="STD_ZB_EXE_STATUS" />
		<emp:text id="exe_totl_sub" label="执行总标的" dataType="Currency" />
		<emp:text id="exe_court" label="执行法院" />
		<emp:text id="exe_judge" label="执行法官" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
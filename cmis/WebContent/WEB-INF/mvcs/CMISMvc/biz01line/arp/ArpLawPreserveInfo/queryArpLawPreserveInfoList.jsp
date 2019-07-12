<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpLawPreserveInfo._toForm(form);
		ArpLawPreserveInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpLawPreserveInfoGroup.reset();
	};
	
	function doGetUpdateArpLawPreserveInfoPage() {
		var paramStr = ArpLawPreserveInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawPreserveInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawPreserveInfo() {
		var paramStr = ArpLawPreserveInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawPreserveInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawPreserveInfoPage() {
		var url = '<emp:url action="getArpLawPreserveInfoAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawPreserveInfo() {
		var paramStr = ArpLawPreserveInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawPreserveInfoRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
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

	<emp:gridLayout id="ArpLawPreserveInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpLawPreserveInfo.preserve_writ_no" label="保全裁定文书号" />
			<emp:text id="ArpLawPreserveInfo.preserve_asset_name" label="保全资产名称" />
			<emp:select id="ArpLawPreserveInfo.preserve_asset_type" label="保全资产类型" dictname="STD_ZB_PRESERVE_TYPE" />
			<emp:select id="ArpLawPreserveInfo.seal_type" label="查封类型" dictname="STD_ZB_SEAL_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawPreserveInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawPreserveInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawPreserveInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawPreserveInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawPreserveInfoList" pageMode="true" url="pageArpLawPreserveInfoQuery.do?case_no=${context.case_no}">
		<emp:text id="preserve_writ_no" label="保全裁定文书号" />
		<emp:text id="preserve_asset_name" label="保全资产名称" />
		<emp:text id="preserve_asset_type" label="保全资产类型" dictname="STD_ZB_PRESERVE_TYPE" />
		<emp:text id="seal_type" label="查封类型" dictname="STD_ZB_SEAL_TYPE" />
		<emp:text id="seal_start_date" label="查封起始日期" />
		<emp:text id="seal_end_date" label="查封到期日期" />
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		ArpLawMemberInfo._toForm(form);
		ArpLawMemberInfoList._obj.ajaxQuery(null,form);
	};
	
	function doViewArpLawMemberInfo() {
		var paramStr = ArpLawMemberInfoList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawDebtorInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawMemberInfoPage() {
		var url = '<emp:url action="getArpLawDebtorInfoAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawMemberInfo() {
		var paramStr = ArpLawMemberInfoList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawMemberInfoRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawMemberInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="getAddArpLawMemberInfoPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpLawMemberInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawMemberInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawMemberInfoList" pageMode="true" url="pageArpLawDebtorInfoQuery.do?serno=${context.serno}">
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />		
	</emp:table>
	
</body>
</html>
</emp:page>
    
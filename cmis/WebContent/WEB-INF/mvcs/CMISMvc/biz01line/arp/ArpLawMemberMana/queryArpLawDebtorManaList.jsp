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
		ArpLawMemberMana._toForm(form);
		ArpLawMemberManaList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.ArpLawMemberManaGroup.reset();
	};
	
	function doViewArpLawMemberMana() {
		var paramStr = ArpLawMemberManaList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawDebtorManaViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawMemberManaPage() {
		var url = '<emp:url action="getArpLawDebtorManaAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawMemberMana() {
		var paramStr = ArpLawMemberManaList._obj.getParamStr(['pk_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawMemberManaRecord.do"/>?'+paramStr;
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

	
	<div align="left">
		<emp:actButton id="getAddArpLawMemberManaPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpLawMemberMana" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawMemberMana" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawMemberManaList" pageMode="true" url="pageArpLawMemberManaQuery.do?case_no=${context.case_no}">
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
		<emp:text id="case_no" label="案件编号"  hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
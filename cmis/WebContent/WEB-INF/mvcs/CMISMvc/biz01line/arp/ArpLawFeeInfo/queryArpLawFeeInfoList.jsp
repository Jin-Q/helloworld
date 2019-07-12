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
		ArpLawFeeInfo._toForm(form);
		ArpLawFeeInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpLawFeeInfoPage() {
		var paramStr = ArpLawFeeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawFeeInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawFeeInfo() {
		var paramStr = ArpLawFeeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawFeeInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawFeeInfoPage() {
		var url = '<emp:url action="getArpLawFeeInfoAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawFeeInfo() {
		var paramStr = ArpLawFeeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawFeeInfoRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawFeeInfoGroup.reset();
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

	<emp:gridLayout id="ArpLawFeeInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="ArpLawFeeInfo.fee_phase" label="费用发生阶段" dictname="STD_ZB_FEE_PHASE" />
			<emp:select id="ArpLawFeeInfo.fee_type" label="费用类型" dictname="STD_ZB_FEE_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawFeeInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawFeeInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawFeeInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawFeeInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawFeeInfoList" pageMode="true" url="pageArpLawFeeInfoQuery.do?case_no=${context.case_no}">
		<emp:text id="fee_phase" label="费用发生阶段" dictname="STD_ZB_FEE_PHASE" />
		<emp:text id="fee_type" label="费用类型" dictname="STD_ZB_FEE_TYPE" />
		<emp:text id="fee_amt" label="费用金额" />
		<emp:text id="fee_date" label="费用发生日期" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
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
		ArpLawLawsuitInfo._toForm(form);
		ArpLawLawsuitInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpLawLawsuitInfoPage() {
		var paramStr = ArpLawLawsuitInfoList._obj.getParamStr(['case_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitInfoUpdatePage.do"/>?'+paramStr+'&op=update';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawLawsuitInfo() {
		var paramStr = ArpLawLawsuitInfoList._obj.getParamStr(['case_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitInfoViewPage.do"/>?'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawLawsuitInfoPage() {
		var url = '<emp:url action="getArpLawLawsuitInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawLawsuitInfo() {
		var paramStr = ArpLawLawsuitInfoList._obj.getParamStr(['case_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpLawLawsuitInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawLawsuitInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function returnDefendant(data){
		ArpLawLawsuitInfo.defendant._setValue(data.cus_id._getValue());
    };
	function returnDebtor(data){
		ArpLawLawsuitInfo.debtor._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="ArpLawLawsuitInfoGroup" title="输入查询条件" maxColumn="4">
			<emp:text id="ArpLawLawsuitInfo.case_no" label="案件编号" />
			<emp:select id="ArpLawLawsuitInfo.law_disp_mode" label="法律处置方式" dictname="STD_ZB_LAW_DISP" />
			<emp:textspace id="ArpLawLawsuitInfo.lawsuit_sub" label="诉讼标的" dataType="Currency" colSpan="2"/>
			<emp:pop id="ArpLawLawsuitInfo.defendant" label="被告人"  buttonLabel="选择" 
			url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnDefendant" />
			<emp:pop id="ArpLawLawsuitInfo.debtor" label="债务人"  buttonLabel="选择" 
			url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnDebtor" />
		</emp:gridLayout>
	</form>	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getUpdateArpLawLawsuitInfoPage" label="管理" op="update"/>
		<emp:button id="viewArpLawLawsuitInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawLawsuitInfoList" pageMode="true" url="pageArpLawLawsuitInfoQuery.do">
		<emp:text id="serno" label="诉讼申请编号" />
		<emp:text id="case_no" label="案件编号" />
		<emp:text id="defendant_name" label="被告人名称" />
		<emp:text id="debtor_name" label="债务人名称" />
		<emp:text id="lawsuit_cap" label="诉讼本金" dataType="Currency" />
		<emp:text id="lawsuit_int" label="诉讼利息" dataType="Currency" />
		<emp:text id="lawsuit_sub" label="诉讼标的" dataType="Currency" />
		<emp:text id="law_disp_mode" label="法律处置方式" dictname="STD_ZB_LAW_DISP" />
		<emp:text id="manager_id_displayname" label="管理人员" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />	
		<emp:text id="status" label="状态" dictname="STD_LMT_CUS_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
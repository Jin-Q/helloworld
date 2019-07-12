<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpLawLawsuitApp._toForm(form);
		ArpLawLawsuitAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpLawLawsuitAppPage() {
		var paramStr = ArpLawLawsuitAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpLawLawsuitAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			
			var url = '<emp:url action="getArpLawLawsuitAppUpdatePage.do"/>?'+paramStr+'&op=update';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawLawsuitApp() {
		var paramStr = ArpLawLawsuitAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawLawsuitAppViewPage.do"/>?'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawLawsuitAppPage() {
		var url = '<emp:url action="getArpLawLawsuitAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawLawsuitApp() {
		var paramStr = ArpLawLawsuitAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpLawLawsuitAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="deleteArpLawLawsuitAppRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawLawsuitAppGroup.reset();
	};
	
	/*--user code begin--*/
	function doSumbitArpLawLawsuitApp(){
		var paramStr = ArpLawLawsuitAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var defendant_name = ArpLawLawsuitAppList._obj.getParamValue(['defendant_name']);	//被告人名称
			var debtor_name = ArpLawLawsuitAppList._obj.getParamValue(['debtor_name']);	//债务人名称
			var lawsuit_sub = ArpLawLawsuitAppList._obj.getParamValue(['lawsuit_sub']);	//诉讼标的
			if( defendant_name=='' || debtor_name=='' || lawsuit_sub-0 <= 0 ){
				alert('信息不完整，请录入被告人、债务人、诉讼标的。');
				return;
			}						
			var _status = ArpLawLawsuitAppList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("ArpLawLawsuitApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("024");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.amt._setValue(lawsuit_sub);
			WfiJoin.prd_name._setValue("诉讼申请流程");
			initWFSubmit(false);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function returnDefendant(data){
		ArpLawLawsuitApp.defendant._setValue(data.cus_id._getValue());
    };
	function returnDebtor(data){
		ArpLawLawsuitApp.debtor._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	<emp:gridLayout id="ArpLawLawsuitAppGroup" title="输入查询条件" maxColumn="4">
			<emp:text id="ArpLawLawsuitApp.serno" label="业务编号" />
			<emp:select id="ArpLawLawsuitApp.law_disp_mode" label="法律处置方式" dictname="STD_ZB_LAW_DISP" />
			<emp:textspace id="ArpLawLawsuitApp.lawsuit_sub" label="诉讼标的" dataType="Currency" colSpan="2" />
			<emp:pop id="ArpLawLawsuitApp.defendant" label="被告人"  buttonLabel="选择" 
			url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnDefendant" />
			<emp:pop id="ArpLawLawsuitApp.debtor" label="债务人"  buttonLabel="选择" 
			url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnDebtor" />	
	</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpLawLawsuitAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpLawLawsuitAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpLawLawsuitApp" label="删除" op="remove"/>
		<emp:button id="viewArpLawLawsuitApp" label="查看" op="view"/>
		<emp:button id="sumbitArpLawLawsuitApp" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="ArpLawLawsuitAppList" pageMode="true" url="pageArpLawLawsuitAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="defendant_name" label="被告人名称" />
		<emp:text id="debtor_name" label="债务人名称" />
		<emp:text id="lawsuit_cap" label="诉讼本金" dataType="Currency" />
		<emp:text id="lawsuit_int" label="诉讼利息" dataType="Currency" />
		<emp:text id="lawsuit_sub" label="诉讼标的" dataType="Currency" />
		<emp:text id="law_disp_mode" label="法律处置方式" dictname="STD_ZB_LAW_DISP" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="manager_id_displayname" label="管理人员" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
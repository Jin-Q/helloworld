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
		ArpBadassetHandoverApp._toForm(form);
		ArpBadassetHandoverAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpBadassetHandoverAppPage() {
		var paramStr = ArpBadassetHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpBadassetHandoverAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getArpBadassetHandoverAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpBadassetHandoverApp() {
		var paramStr = ArpBadassetHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBadassetHandoverAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpBadassetHandoverAppPage() {
		var url = '<emp:url action="getArpBadassetHandoverAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function doDeleteArpBadassetHandoverApp() {
		var paramStr = ArpBadassetHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpBadassetHandoverAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="deleteArpBadassetHandoverAppRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doSumbitAddArpBadassetHandover() {
		var paramStr = ArpBadassetHandoverAppList._obj.getParamValue(['serno']);
		cus_id = ArpBadassetHandoverAppList._obj.getParamValue(['cus_id']);//客户码
		cus_name = ArpBadassetHandoverAppList._obj.getParamValue(['cus_id_displayname']);//客户名称
		if (paramStr != null) {
			var _status = ArpBadassetHandoverAppList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("ArpBadassetHandoverApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("021");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("不良资产移交申请流程");
			initWFSubmit(false);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpBadassetHandoverAppGroup.reset();
	};
	
	/*--user code begin--*/

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpBadassetHandoverAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpBadassetHandoverApp.serno" label="业务编号" />
			<emp:text id="ArpBadassetHandoverApp.bill_no" label="借据编号" />
			<emp:select id="ArpBadassetHandoverApp.handover_resn" label="移交原因" dictname="STD_ZB_HANDOVER_RESN" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewArpBadassetHandoverApp" label="查看" op="view"/>
		<emp:button id="getUpdateArpBadassetHandoverAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpBadassetHandoverApp" label="删除" op="remove"/>
		<emp:button id="sumbitAddArpBadassetHandover" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="ArpBadassetHandoverAppList" pageMode="true" url="pageArpBadassetHandoverAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
		<emp:text id="handover_resn" label="移交原因" dictname="STD_ZB_HANDOVER_RESN" />
		<emp:text id="fount_manager_id_displayname" label="原主管客户经理" />
		<emp:text id="fount_manager_br_id_displayname" label="原管理机构" />
		<emp:text id="rcv_person_displayname" label="接收人员" />		
		<emp:text id="rcv_org_displayname" label="接收机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
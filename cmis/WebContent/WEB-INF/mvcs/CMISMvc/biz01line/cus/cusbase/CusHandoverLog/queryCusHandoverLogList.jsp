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
		CusHandoverLog._toForm(form);
		CusHandoverLogList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusHandoverLogPage() {
		var paramStr = CusHandoverLogList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusHandoverLogUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverLog() {
		var paramStr = CusHandoverLogList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusHandoverLogViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusHandoverLogPage() {
		var url = '<emp:url action="getCusHandoverLogAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusHandoverLog() {
		var paramStr = CusHandoverLogList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusHandoverLogRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusHandoverLogGroup.reset();
		/**
		var form = document.getElementById("queryForm");
		for(var i=0;i<form.length;i++){
			if(form[i].name == "CusHandoverLog.handover_date_begin" || form[i].name == "CusHandoverLog.handover_date_end"){
				form[i].value = "";
			}
		}
		*/
	};

	function idHandover(data){
    	//CusHandoverApp.handover_id._setValue(data.actorno._getValue());
		CusHandoverLog.handover_id_displayname._setValue(data.actorname._getValue());
		CusHandoverLog.handover_id._setValue(data.actorno._getValue());
	}

    function orgHandover(data){
    	//CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
		CusHandoverLog.handover_br_id_displayname._setValue(data.organname._getValue());
		CusHandoverLog.handover_br_id._setValue(data.organno._getValue());
	}

	function idReceiver(data){
		var managerId= data.actorno._getValue();
		var managerIdName= data.actorname._getValue();
	    var handoverId = CusHandoverLog.handover_id_displayname._obj.element.value;
		var handoverIdName = CusHandoverLog.handover_id_displayname._obj.element.value;
		if(managerIdName==handoverIdName){
               alert("移出人和接收人不能是同一人!");
               return;
		}else{
			CusHandoverLog.receiver_id_displayname._setValue(managerIdName);
			CusHandoverLog.receiver_id._setValue(managerId);
		}
	}

	function orgReceiver(data){
		var retBrId = data.organno._getValue();
        var retBrIdName = data.organname._getValue();	
		var hanBrIdName = CusHandoverLog.handover_br_id_displayname._obj.element.value;
		if(retBrIdName==hanBrIdName){
			alert("不是机构内移交\n[移出机构]和[接收机构]不能 是同一个！");
			return;
		}
		CusHandoverLog.receiver_br_id_displayname._setValue(retBrIdName);
		CusHandoverLog.receiver_br_id._setValue(retBrId);
    }
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	<emp:gridLayout id="CusHandoverLogGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="CusHandoverLog.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover" />
			<emp:pop id="CusHandoverLog.handover_br_id_displayname" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" />
			<emp:pop id="CusHandoverLog.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver"/>
			<emp:pop id="CusHandoverLog.receiver_br_id_displayname" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver" />
			<emp:text id="CusHandoverLog.handover_id" label="移出人" hidden="true" />
			<emp:text id="CusHandoverLog.handover_br_id" label="移出机构" hidden="true" />
			<emp:text id="CusHandoverLog.receiver_id" label="接收人" hidden="true" />
			<emp:text id="CusHandoverLog.receiver_br_id" label="接收机构" hidden="true" />
			<emp:datespace id="CusHandoverLog.handover_date" label="移交日期" colSpan="2"/>
	</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">

		<emp:button id="viewCusHandoverLog" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusHandoverLogList" pageMode="true" url="pageCusHandoverLogQuery.do">
		<emp:text id="handover_date" label="移交日期" />
		<emp:text id="handover_scope" label="移交范围" dictname="STD_ZB_HAND_SCOPE" />
		<emp:text id="handover_mode" label="移交方式" dictname="STD_ZB_HAND_TYPE" />
		<emp:text id="handover_br_id" label="移出机构" hidden="true" />
		<emp:text id="handover_id" label="移出人" hidden="true" />
		<emp:text id="receiver_br_id" label="接收机构" hidden="true" />
		<emp:text id="receiver_id" label="接收人" hidden="true" />
		<emp:text id="supervise_br_id" label="监交机构" hidden="true" />
		<emp:text id="supervise_id" label="监交人" hidden="true" />
		
		<emp:text id="handover_br_id_displayname" label="移出机构" />
        <emp:text id="handover_id_displayname" label="移出人" />
        <emp:text id="receiver_br_id_displayname" label="接收机构" />
        <emp:text id="receiver_id_displayname" label="接收人" />
        <emp:text id="supervise_br_id_displayname" label="监交机构" hidden="true"/>
        <emp:text id="supervise_id_displayname" label="监交人" hidden="true"/>
		<emp:text id="pk_id" label="主键" hidden="true"></emp:text>
		<emp:text id="serno" label="申请流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
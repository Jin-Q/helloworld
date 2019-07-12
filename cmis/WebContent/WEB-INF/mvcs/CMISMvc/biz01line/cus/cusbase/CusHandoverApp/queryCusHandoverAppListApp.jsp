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
		CusHandoverApp._toForm(form);
		CusHandoverAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusHandoverAppPage() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&update=App";
			var url = '<emp:url action="getCusHandoverAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverApp() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&update=appView";
			var url = '<emp:url action="getCusHandoverAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.CusHandoverAppGroup.reset();
	};
	 function idHandover(data){
	    	//CusHandoverApp.handover_id._setValue(data.actorno._getValue());
			CusHandoverApp.handover_id_displayname._setValue(data.actorname._getValue());
		}

	    function orgHandover(data){
	    	//CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
			CusHandoverApp.handover_br_id_displayname._setValue(data.organname._getValue());
		}

		function idReceiver(data){
			var managerId= data.actorno._getValue();
			var managerIdName= data.actorname._getValue();
		    var handoverId = CusHandoverApp.handover_id_displayname._obj.element.value;
			var handoverIdName = CusHandoverApp.handover_id_displayname._obj.element.value;
			if(managerIdName==handoverIdName){
	               alert("移出人和接收人不能是同一人!");
	               return;
			}else{
				CusHandoverApp.receiver_id_displayname._setValue(managerIdName);
			}
			
		}

		function orgReceiver(data){
			var retBrId = data.organno._getValue();
	        var retBrIdName = data.organname._getValue();	
			var hanBrIdName = CusHandoverApp.handover_br_id_displayname._obj.element.value;
			if(retBrIdName==hanBrIdName){
				
					alert("不是机构内移交\n[移出机构]和[接收机构]不能 是同一个！");
					return;
				}
			    CusHandoverApp.receiver_br_id_displayname._setValue(retBrIdName);
		           
	    }
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusHandoverAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusHandoverApp.serno" label="申请流水号" colSpan="2"/>
			<emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover"  />
			<emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" />
			<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" />
			<emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver"  />
			<emp:datespace id="CusHandoverApp.input_date" label="登记日期" colSpan="2"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">

		<emp:button id="viewCusHandoverApp" label="查看" op="view"/>
		<emp:button id="getUpdateCusHandoverAppPage" label="审批" op="update"/>
	</div>

	<emp:table icollName="CusHandoverAppList" pageMode="true" url="pageCusHandoverAppQuery.do" reqParams="CusHandoverApp.approve_status=10">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="handover_id" label="移出人" hidden="true" />
		<emp:text id="handover_br_id" label="移出机构" hidden="true" />
		<emp:text id="receiver_id" label="接收人" hidden="true" />
		<emp:text id="receiver_br_id" label="接收机构" hidden="true" />
		<emp:text id="supervise_id" label="监交人" hidden="true" />
		<emp:text id="supervise_br_id" label="监交机构" hidden="true" />
        <emp:text id="handover_id_displayname" label="移出人" />
        <emp:text id="handover_br_id_displayname" label="移出机构" />
        <emp:text id="receiver_id_displayname" label="接收人" />
        <emp:text id="receiver_br_id_displayname" label="接收机构" />
        <emp:text id="supervise_id_displayname" label="监交人" />
        <emp:text id="supervise_br_id_displayname" label="监交机构" />		
		<emp:text id="status" label="状态" dictname="STD_ZB_HAND_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
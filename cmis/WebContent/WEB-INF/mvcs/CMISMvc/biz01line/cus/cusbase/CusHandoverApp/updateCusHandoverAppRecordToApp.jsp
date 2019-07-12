<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>审批页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	function doOnLoad(){
		doQuery();
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		
		CusHandoverLst._toForm(form);
		
		CusHandoverLstList._obj.ajaxQuery(null,form);
	};
	function doReturn(){
		var paramStr = "CusHandoverApp.approve_status=10";
		var url = '<emp:url action="queryCusHandoverAppListApp.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doCusHandoverApp() {
		var form = document.getElementById('submitForm');
		var paramStr = "approve_status=10"+"&serno="+CusHandoverApp.serno._obj.element.value+"&updateFlag=1";
		var url = '<emp:url action="updateCusHandoverAppStatusApp.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		CusHandoverApp._toForm(form);
		form.action=url;
		form.submit();

	};
	function doVeto() {
		var form = document.getElementById('submitForm');
		var paramStr = "approve_status=10"+"&serno="+CusHandoverApp.serno._obj.element.value+"&updateFlag=2";
		var url = '<emp:url action="updateCusHandoverAppStatusApp.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		CusHandoverApp._toForm(form);
		form.action=url;
		form.submit();

	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="" method="POST">
		
		<emp:gridLayout id="CusHandoverAppGroup" title="客户移交申请" maxColumn="2">
			<emp:text id="CusHandoverApp.serno" label="申请流水号" maxlength="40" required="true" colSpan="2" readonly="true"/>
			<emp:select id="CusHandoverApp.org_type" label="接收机构与移出机构关系" required="true" dictname="STD_ZB_ORG_TYPE" colSpan="2" onblur="CheakOrgType()"/>
			<emp:select id="CusHandoverApp.handover_scope" label="移交范围" required="true" dictname="STD_ZB_HAND_SCOPE" colSpan="2"/>
			<emp:select id="CusHandoverApp.handover_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" colSpan="2"/>
			<emp:pop id="CusHandoverApp.area_code" label="区域编码" required="false" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_T2260" returnMethod="onReturnStateCode" hidden="true"/>
			<emp:text id="CusHandoverApp.area_name" label="区域名称" maxlength="100" required="false"  colSpan="2" hidden="true"/>
			<emp:pop id="CusHandoverApp.handover_br_id" label="移出机构" url="null" hidden="true" />
			<emp:pop id="CusHandoverApp.handover_id" label="移出人" url="null" hidden="true" />
			<emp:pop id="CusHandoverApp.receiver_br_id" label="接收机构" url="null" hidden="true" />
			<emp:pop id="CusHandoverApp.receiver_id" label="接收人" url="null" hidden="true" defvalue="$currentUserId"/>
			<emp:pop id="CusHandoverApp.supervise_br_id" label="监交机构" url="null" hidden="true" />
			<emp:pop id="CusHandoverApp.supervise_id" label="监交人" url="null" hidden="true" />
			
            <emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="null" required="true" />
            <emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="null" required="true" />
            <emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="null" required="true" />
            <emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="null" required="true" defvalue="$currentUserId"/>
            <emp:pop id="CusHandoverApp.supervise_br_id_displayname" label="监交机构" url="null" required="false" />
            <emp:pop id="CusHandoverApp.supervise_id_displayname" label="监交人" url="null" required="false" />
		
			<emp:textarea id="CusHandoverApp.handover_detail" label="移交说明" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:date id="CusHandoverApp.supervise_date" label="审批日期" required="false" hidden="true"/>
			<emp:date id="CusHandoverApp.receive_date" label="接收日期" required="false" hidden="true"/>
			<emp:select id="CusHandoverApp.approve_status" label="状态" required="true" dictname="STD_ZB_HAND_STATUS" hidden="true"/>
			<emp:text id="CusHandoverApp.input_id" label="登记人" maxlength="20" required="true" readonly="true"/>
			<emp:text id="CusHandoverApp.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true"/>
			<emp:text id="CusHandoverApp.input_date" label="登记日期" maxlength="10" required="true" readonly="true"/>
		</emp:gridLayout>
<form  method="POST" action="#" id="queryForm" >
	</form>
	<emp:text id="CusHandoverLst.serno" label="申请流水号" defvalue="${context.CusHandoverApp.serno}" hidden="true"/>
	
	<emp:table icollName="CusHandoverLstList" pageMode="true" url="pageCusHandoverLstQuery.do" reqParams="CusHandoverLst.serno=$CusHandoverApp.serno;">
		<emp:text id="handover_type" label="业务类型" dictname="STD_ZB_OP_TYPE" />
		<emp:text id="business_code" label="业务编码" />
		<emp:text id="business_detail" label="移交业务说明" />
		<emp:text id="serno" label="申请流水号" hidden="true"/>
	</emp:table>
		<div align="center">
			<br>
			<emp:button id="cusHandoverApp" label="同意" op="update"/>
			<emp:button id="veto" label="否决" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


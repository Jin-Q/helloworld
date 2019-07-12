<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpCollDispAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
	<emp:gridLayout id="ArpCollDispAppGroup" title="抵债物处置申请" maxColumn="2">
			<emp:text id="ArpCollDispApp.serno" label="业务编号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:date id="ArpCollDispApp.app_date" label="申请日期" required="false" hidden="true"/>
			<emp:date id="ArpCollDispApp.end_date" label="办结日期" required="false" hidden="true"/>
			<emp:pop id="ArpCollDispApp.guaranty_no" label="抵债资产编号" required="true" url="queryArpCollDebtAccRePopList.do" returnMethod="setGuaranty" readonly="true"/>
			<emp:text id="ArpCollDispApp.guaranty_no_displayname" label="抵债资产名称" required="false" readonly="true" colSpan="2"/>
			<emp:text id="ArpCollDispApp.debt_in_amt" label="抵入金额" maxlength="16" required="true" dataType="Currency" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDispApp.disp_amt" label="处置金额" maxlength="16" required="true" dataType="Currency" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="ArpCollDispApp.asset_disp_mode" label="资产处置方式" required="true" dictname="STD_ASSET_DISP_MODEL" readonly="true"/>
			<emp:text id="ArpCollDispApp.fore_disp_expense" label="预计处置费用" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="ArpCollDispApp.disp_memo" label="处置说明" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDispApp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDispApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpCollDispAppGroup" title="登记信息" maxColumn="2">
			<emp:pop id="ArpCollDispApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpCollDispApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="true"/>
			<emp:text id="ArpCollDispApp.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="ArpCollDispApp.input_br_id_displayname" label="登记机构"  required="false" readonly="true" defvalue="$organName"/>
			<emp:text id="ArpCollDispApp.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDispApp.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDispApp.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="ArpCollDispApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="ArpCollDispApp.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			
		</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
</body>
</html>
</emp:page>

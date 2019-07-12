<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpCollDebtAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addCusForm(ArpCollDebtApp);
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
	<emp:gridLayout id="ArpCollDebtAppGroup" title="以物抵债申请" maxColumn="2">
			<emp:text id="ArpCollDebtApp.serno" label="业务编号" maxlength="40" required="false" readonly="true" colSpan="2"/>
			<emp:text id="ArpCollDebtApp.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="ArpCollDebtApp.cus_id_displayname" label="客户名称" required="false" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:date id="ArpCollDebtApp.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:date id="ArpCollDebtApp.end_date" label="办结日期" required="false" hidden="true" />
			<emp:select id="ArpCollDebtApp.debt_mode" label="抵债方式" required="true" dictname="STD_ZB_DEBT_MODEL" />
			<emp:text id="ArpCollDebtApp.debt_in_amt" label="抵入金额（元）" maxlength="12" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.debt_cap" label="抵债本金（元）" maxlength="12" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.inner_int" label="抵债表内利息（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.off_int" label="抵债表外利息（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtApp.debt_expense" label="抵债费用（元）" maxlength="12" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="ArpCollDebtApp.coll_debt_resn" label="以物抵债理由" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDebtApp.collect_addr" label="收取地点" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDebtApp.asset_status" label="资产现状" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDebtApp.is_handover" label="是否移交管理" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpCollDebtApp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpCollDebtApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpCollDebtAppGroup" title="登记信息" maxColumn="2">
			<emp:text id="ArpCollDebtApp.manager_id" label="责任人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.manager_br_id" label="责任机构" maxlength="40" required="false" hidden="true"/>
			<emp:pop id="ArpCollDebtApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpCollDebtApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="true"/>
			<emp:text id="ArpCollDebtApp.input_id" label="登记人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.input_br_id" label="登记机构" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtApp.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtApp.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="ArpCollDebtApp.input_date" label="登记日期" required="false" readonly="true"/>
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

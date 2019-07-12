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
		var url = '<emp:url action="queryArpCollDebtAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">		
	<emp:gridLayout id="ArpCollDebtAccGroup" maxColumn="2" title="以物抵债台账">
			<emp:text id="ArpCollDebtAcc.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="ArpCollDebtAcc.debt_acc_no" label="抵债台账编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:date id="ArpCollDebtAcc.app_date" label="申请日期" required="false" readonly="true" hidden="true"/>
			<emp:date id="ArpCollDebtAcc.end_date" label="办结日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="ArpCollDebtAcc.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.approve_file_no" label="批准文件号" maxlength="40" required="true" />
			<emp:select id="ArpCollDebtAcc.debt_mode" label="抵债方式" required="false" dictname="STD_ZB_DEBT_MODEL" readonly="true" />
			<emp:text id="ArpCollDebtAcc.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtAcc.debt_cap" label="抵债本金" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtAcc.inner_int" label="抵债表内利息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtAcc.off_int" label="抵债表外利息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpCollDebtAcc.debt_expense" label="抵债费用" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="ArpCollDebtAcc.coll_debt_resn" label="以物抵债理由" maxlength="200" required="false" colSpan="2" readonly="true"/>
			<emp:textarea id="ArpCollDebtAcc.collect_addr" label="收取地点" maxlength="200" required="false" readonly="true" colSpan="2"/>
			<emp:textarea id="ArpCollDebtAcc.asset_status" label="资产现状" maxlength="200" required="false" readonly="true" colSpan="2"/>
			<emp:date id="ArpCollDebtAcc.debt_in_date" label="抵入日期" required="false" readonly="true" />
			<emp:select id="ArpCollDebtAcc.is_handover" label="是否移交管理" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:textarea id="ArpCollDebtAcc.memo" label="备注" maxlength="200" required="false" colSpan="2" readonly="true"/>
			<emp:select id="ArpCollDebtAcc.status" label="状态" required="false" dictname="STD_ZB_COLL_ACC_STATUS" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpCollDebtAccGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpCollDebtAcc.manager_id_displayname" label="责任人" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.manager_br_id_displayname" label="责任机构" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:text id="ArpCollDebtAcc.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtAcc.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtAcc.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="ArpCollDebtAcc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:date id="ArpCollDebtAcc.input_date" label="登记日期" required="false" readonly="true"/>
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

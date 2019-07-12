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
<style type="text/css">
.emp_field_select_select1 {
	width: 600px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAppDepotAgrList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
        window.close();
	};
	function onload(){
		var fst_deliv_agreed = IqpAppDepotAgr.fst_deliv_agreed._getValue();
        if("02" == fst_deliv_agreed){
        	IqpAppDepotAgr.agreed_rate._obj._renderHidden(false);
        	IqpAppDepotAgr.agreed_rate._obj._renderRequired(true);
        }else{
        	IqpAppDepotAgr.agreed_rate._setValue("0");
        	IqpAppDepotAgr.agreed_rate._obj._renderHidden(true);
        	IqpAppDepotAgr.agreed_rate._obj._renderRequired(false);
        	
        }
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="IqpAppDepotAgrGroup" title="保兑仓协议信息" maxColumn="2">
			<emp:text id="IqpAppDepotAgr.depot_agr_no" label="协议编号" maxlength="40" required="false" hidden="false" />
			<emp:text id="IqpAppDepotAgr.cus_id" label="借款人客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppDepotAgr.cus_id_displayname" label="借款人客户名称"  required="true" readonly="true"/>
			<emp:pop id="IqpAppDepotAgr.psale_cont" label="购销合同" url="queryIqpAppPsaleContPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setPsale" required="true" />
			<emp:text id="IqpAppDepotAgr.fst_bail_perc" label="首次保证金比例" maxlength="16" required="true" dataType="Rate" />
			<emp:select id="IqpAppDepotAgr.fst_deliv_agreed" label="首次提货约定" dictname="STD_ZB_FST_AGREED" required="false" colSpan="2" cssFakeInputClass="emp_field_select_select1" />
			<emp:text id="IqpAppDepotAgr.agreed_rate" label="约定比率" maxlength="10" required="true" dataType="Percent" colSpan="2"/>
			<emp:text id="IqpAppDepotAgr.contacc_freq" label="对账频率"  required="true" dataType="Int"/>
			<emp:select id="IqpAppDepotAgr.contacc_freq_unit" label="对账频率单位" required="true" dictname="STD_ZB_CONTACC_FREP"/>
			<emp:pop id="IqpAppDepotAgr.desgoods_plan_no" label="订货计划" url="queryIqpAppDesbuyPlanPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setDesgood" required="true" />	
			
			<emp:select id="IqpAppDepotAgr.contacc_mode" label="对账方式"  required="false" dictname="STD_ZB_CONTACC_MODE" />

			<emp:textarea id="IqpAppDepotAgr.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			
			<emp:text id="IqpAppDepotAgr.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppDepotAgr.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:text id="IqpAppDepotAgr.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			
			
			<emp:text id="IqpAppDepotAgr.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			
			<emp:text id="IqpAppDepotAgr.start_date" label="协议起始日期" maxlength="10" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.end_date" label="协议到期日期" maxlength="10" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" /> 
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

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
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpLawLawsuitAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doload(){
		checkLawyer();
		var approve_status = ArpLawLawsuitApp.approve_status._getValue();
		if(approve_status == '997'){
			ArpLawLawsuitApp.app_date._obj._renderHidden(false);
			ArpLawLawsuitApp.over_date._obj._renderHidden(false);
		}
		
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}
	};

	function checkLawyer(){
		whether_invite_agt_lawyer = ArpLawLawsuitApp.whether_invite_agt_lawyer._getValue();
		if(whether_invite_agt_lawyer == '1'){
			ArpLawLawsuitApp.agt_type._obj._renderHidden(false);
			ArpLawLawsuitApp.agt_agreed_indgt._obj._renderHidden(false);
			ArpLawLawsuitApp.agcy._obj._renderHidden(false);
			ArpLawLawsuitApp.agcy_link_mode._obj._renderHidden(false);
			ArpLawLawsuitApp.belg_lawyer_office._obj._renderHidden(false);
			ArpLawLawsuitApp.agt_fee_pay_rate._obj._renderHidden(false);
			ArpLawLawsuitApp.agt_fee_pay_amt._obj._renderHidden(false);
		}else{
			ArpLawLawsuitApp.agt_type._setValue('');
			ArpLawLawsuitApp.agt_agreed_indgt._setValue('');
			ArpLawLawsuitApp.agcy._setValue('');
			ArpLawLawsuitApp.agcy_link_mode._setValue('');
			ArpLawLawsuitApp.belg_lawyer_office._setValue('');
			ArpLawLawsuitApp.agt_fee_pay_rate._setValue('');
			ArpLawLawsuitApp.agt_fee_pay_amt._setValue('');
			
			ArpLawLawsuitApp.agt_type._obj._renderHidden(true);
			ArpLawLawsuitApp.agt_agreed_indgt._obj._renderHidden(true);
			ArpLawLawsuitApp.agcy._obj._renderHidden(true);
			ArpLawLawsuitApp.agcy_link_mode._obj._renderHidden(true);
			ArpLawLawsuitApp.belg_lawyer_office._obj._renderHidden(true);
			ArpLawLawsuitApp.agt_fee_pay_rate._obj._renderHidden(true);
			ArpLawLawsuitApp.agt_fee_pay_amt._obj._renderHidden(true);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="诉讼申请信息" id="main_tabs">
		<emp:gridLayout id="ArpLawLawsuitAppGroup" title="诉讼申请信息" maxColumn="2">
			<emp:text id="ArpLawLawsuitApp.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="ArpLawLawsuitApp.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitApp.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitApp.lawsuit_sub" label="诉讼标的" maxlength="16" required="false" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitApp.other_fee" label="其他费用" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpLawLawsuitApp.executable_property_status" label="可执行财产状况" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitApp.law_disp_mode" label="法律处置方式" required="false" dictname="STD_ZB_LAW_DISP" />
			<emp:textarea id="ArpLawLawsuitApp.lawsuit_reason" label="起诉原因" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitApp.whether_invite_agt_lawyer" label="是否聘请代理律师" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="ArpLawLawsuitApp.agt_type" label="代理方式" required="false" dictname="STD_ZB_AGT_TYPE" />
			<emp:textarea id="ArpLawLawsuitApp.agt_agreed_indgt" label="代理约定事项" maxlength="250" required="false" colSpan="2" />
			<emp:text id="ArpLawLawsuitApp.agcy" label="代理人" maxlength="100" required="false" />
			<emp:text id="ArpLawLawsuitApp.agcy_link_mode" label="代理人联系方式" maxlength="50" required="false" dataType="Phone" />
			<emp:text id="ArpLawLawsuitApp.belg_lawyer_office" label="所属律师事务所" maxlength="100" required="false" />
			<emp:text id="ArpLawLawsuitApp.agt_fee_pay_rate" label="代理费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="ArpLawLawsuitApp.agt_fee_pay_amt" label="代理费支付金额" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpLawLawsuitApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpLawLawsuitApp.app_date" label="申请日期" required="false" hidden="true"/>
			<emp:date id="ArpLawLawsuitApp.over_date" label="办结日期" required="false"  hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawLawsuitAppGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawLawsuitApp.manager_id_displayname" label="管理人员" required="true" />
			<emp:text id="ArpLawLawsuitApp.manager_br_id_displayname" label="管理机构"  required="true"  cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpLawLawsuitApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpLawLawsuitApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpLawLawsuitApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpLawLawsuitApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpLawLawsuitApp.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpLawLawsuitApp.input_br_id" label="登记机构" required="true"  hidden="true" />
			<emp:date id="ArpLawLawsuitApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpLawLawsuitApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"  readonly="true" />
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

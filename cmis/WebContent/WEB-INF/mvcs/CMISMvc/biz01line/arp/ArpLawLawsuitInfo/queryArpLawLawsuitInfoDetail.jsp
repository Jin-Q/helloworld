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
		var url = '<emp:url action="queryArpLawLawsuitInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function checkLawyer(){
		whether_invite_agt_lawyer = ArpLawLawsuitInfo.whether_invite_agt_lawyer._getValue();
		if(whether_invite_agt_lawyer == '1'){
			ArpLawLawsuitInfo.agt_type._obj._renderHidden(false);
			ArpLawLawsuitInfo.agt_agreed_indgt._obj._renderHidden(false);
			ArpLawLawsuitInfo.agcy._obj._renderHidden(false);
			ArpLawLawsuitInfo.agcy_link_mode._obj._renderHidden(false);
			ArpLawLawsuitInfo.belg_lawyer_office._obj._renderHidden(false);
			ArpLawLawsuitInfo.agt_fee_pay_rate._obj._renderHidden(false);
			ArpLawLawsuitInfo.agt_fee_pay_amt._obj._renderHidden(false);
		}else{
			ArpLawLawsuitInfo.agt_type._setValue('');
			ArpLawLawsuitInfo.agt_agreed_indgt._setValue('');
			ArpLawLawsuitInfo.agcy._setValue('');
			ArpLawLawsuitInfo.agcy_link_mode._setValue('');
			ArpLawLawsuitInfo.belg_lawyer_office._setValue('');
			ArpLawLawsuitInfo.agt_fee_pay_rate._setValue('');
			ArpLawLawsuitInfo.agt_fee_pay_amt._setValue('');
			
			ArpLawLawsuitInfo.agt_type._obj._renderHidden(true);
			ArpLawLawsuitInfo.agt_agreed_indgt._obj._renderHidden(true);
			ArpLawLawsuitInfo.agcy._obj._renderHidden(true);
			ArpLawLawsuitInfo.agcy_link_mode._obj._renderHidden(true);
			ArpLawLawsuitInfo.belg_lawyer_office._obj._renderHidden(true);
			ArpLawLawsuitInfo.agt_fee_pay_rate._obj._renderHidden(true);
			ArpLawLawsuitInfo.agt_fee_pay_amt._obj._renderHidden(true);
		}
	};
	
	function doload(){
		checkLawyer();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="诉讼申请信息" id="main_tabs">
	<emp:gridLayout id="ArpLawLawsuitInfoGroup" title="诉讼信息" maxColumn="2">
			<emp:text id="ArpLawLawsuitInfo.serno" label="诉讼申请编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpLawLawsuitInfo.case_no" label="案件编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpLawLawsuitInfo.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitInfo.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitInfo.lawsuit_sub" label="诉讼标的" maxlength="16" required="false" dataType="Currency" readonly="true" />
			<emp:text id="ArpLawLawsuitInfo.other_fee" label="其他费用" maxlength="16" required="false" dataType="Currency" />
			<emp:date id="ArpLawLawsuitInfo.lawsuit_start_date" label="诉讼开始日期" required="false" />
			<emp:date id="ArpLawLawsuitInfo.lawsuit_end_date" label="诉讼结束日期" required="false" />
			<emp:textarea id="ArpLawLawsuitInfo.executable_property_status" label="可执行财产状况" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitInfo.law_disp_mode" label="法律处置方式" required="false" dictname="STD_ZB_LAW_DISP" />
			<emp:textarea id="ArpLawLawsuitInfo.lawsuit_reason" label="起诉原因" maxlength="250" required="false" colSpan="2" />
			<emp:select id="ArpLawLawsuitInfo.whether_invite_agt_lawyer" label="是否聘请代理律师" required="false" dictname="STD_ZX_YES_NO" onchange="checkLawyer()" />
			<emp:select id="ArpLawLawsuitInfo.agt_type" label="代理方式" required="false" dictname="STD_ZB_AGT_TYPE" />
			<emp:textarea id="ArpLawLawsuitInfo.agt_agreed_indgt" label="代理约定事项" maxlength="250" required="false" colSpan="2" />
			<emp:text id="ArpLawLawsuitInfo.agcy" label="代理人" maxlength="100" required="false" />
			<emp:text id="ArpLawLawsuitInfo.agcy_link_mode" label="代理人联系方式" maxlength="50" required="false" dataType="Phone" />
			<emp:text id="ArpLawLawsuitInfo.belg_lawyer_office" label="所属律师事务所" maxlength="100" required="false" />
			<emp:text id="ArpLawLawsuitInfo.agt_fee_pay_rate" label="代理费支付比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="ArpLawLawsuitInfo.agt_fee_pay_amt" label="代理费支付金额" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpLawLawsuitInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpLawLawsuitInfo.app_date" label="申请日期" required="false" hidden="true"/>
			<emp:date id="ArpLawLawsuitInfo.over_date" label="办结日期" required="false"  hidden="true"/>

		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawLawsuitInfoGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawLawsuitInfo.manager_id_displayname" label="管理人员" required="true" />
			<emp:text id="ArpLawLawsuitInfo.manager_br_id_displayname" label="管理机构"  required="true" cssElementClass="emp_pop_common_org" />
			<emp:text id="ArpLawLawsuitInfo.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpLawLawsuitInfo.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpLawLawsuitInfo.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpLawLawsuitInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpLawLawsuitInfo.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpLawLawsuitInfo.input_br_id" label="登记机构" required="true"  hidden="true" />
			<emp:date id="ArpLawLawsuitInfo.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpLawLawsuitInfo.status" label="状态" required="true" dictname="STD_LMT_CUS_STATUS" />
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

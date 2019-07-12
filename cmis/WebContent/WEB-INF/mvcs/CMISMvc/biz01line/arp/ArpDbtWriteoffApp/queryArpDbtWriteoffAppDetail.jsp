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
<style type="text/css">
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpDbtWriteoffAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addCusForm(ArpDbtWriteoffApp);

		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}

		var approve_status = ArpDbtWriteoffApp.approve_status._getValue();
		if(approve_status == '997'){
			ArpDbtWriteoffApp.app_date._obj._renderHidden(false);
			ArpDbtWriteoffApp.over_date._obj._renderHidden(false);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="呆账核销信息" id="main_tabs">
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="核销客户信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.serno" label="业务编号" maxlength="40" hidden="false" readonly="true" required="true"/>
			<emp:text id="ArpDbtWriteoffApp.cus_id" label="客户码" required="true"  colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.cus_name" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />			
			<%if("${context.ArpDbtWriteoffApp.belg_line}".equals("BL300")){	%>
			<emp:text id="ArpDbtWriteoffApp.indiv_rsd_addr_displayname" label="客户地址" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.street3" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.mobile" label="联系方式" readonly="true" />
			<%}else{ %>
			<emp:text id="ArpDbtWriteoffApp.acu_addr_displayname" label="客户地址" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.street" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.legal_phone" label="联系方式" readonly="true" />
			<%}%>
			<emp:text id="ArpDbtWriteoffApp.cust_mgr_displayname" label="主管客户经理" readonly="true" />
		</emp:gridLayout>

		<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="呆账核销申请信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.nums" label="核销笔数" maxlength="38" dataType="Int" colSpan="2" readonly="true" defvalue="0" />
			<emp:text id="ArpDbtWriteoffApp.loan_amt_sum" label="借据金额" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.loan_balance_sum" label="借据余额" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.inner_owe_int_sum" label="表内欠息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.out_owe_int_sum" label="表外欠息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.owe_int_sum" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_cap_sum" label="核销本金" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_int_sum" label="核销利息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_amt_sum" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" />		

			<emp:textarea id="ArpDbtWriteoffApp.writeoff_resn" label="核销理由" maxlength="250" required="true" colSpan="2" />
			<emp:select id="ArpDbtWriteoffApp.whether_appx_appeal" label="是否保留追诉权" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpDbtWriteoffApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtWriteoffApp.app_date" label="申请日期" required="false" hidden="true" />
			<emp:date id="ArpDbtWriteoffApp.over_date" label="办结日期" required="false" hidden="true" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_qnt" label="核销笔数" maxlength="38" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.loan_balance" label="贷款余额" maxlength="16" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpDbtWriteoffApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpDbtWriteoffApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org QZ_emp_pop_common_org" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpDbtWriteoffApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpDbtWriteoffApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="ArpDbtWriteoffApp.input_id" label="登记人" required="true" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpDbtWriteoffApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpDbtWriteoffApp.approve_status" label="审批状态" dictname="WF_APP_STATUS" readonly="true" />
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
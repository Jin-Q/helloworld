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
		var url = '<emp:url action="queryArpBondReducAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addCusForm(ArpBondReducApp);

		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}

		var approve_status = ArpBondReducApp.approve_status._getValue();
		if(approve_status == '997'){
			ArpBondReducApp.app_date._obj._renderHidden(false);
			ArpBondReducApp.over_date._obj._renderHidden(false);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="债权减免信息" id="main_tabs">
		<emp:gridLayout id="ArpBondReducAppGroup" title="债权减免申请" maxColumn="2">
			<emp:text id="ArpBondReducApp.serno" label="业务编号" maxlength="40" />
			<emp:select id="ArpBondReducApp.reduc_type" label="减免类型" required="true" dictname="STD_ZB_REDUC_TYPE" />
			<emp:text id="ArpBondReducApp.cus_id" label="客户码" required="true"  colSpan="2" readonly="true" />
			<emp:text id="ArpBondReducApp.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpBondReducApp.reduc_qnt" label="减免笔数" maxlength="38" dataType="Int" hidden="true"/>
			<emp:text id="ArpBondReducApp.nums" label="减免笔数" maxlength="38" 
			required="true" dataType="Int" readonly="true" colSpan="2" defvalue="0"/>
			<emp:text id="ArpBondReducApp.reduc_cap" label="债权减免本金金额" maxlength="16" 
			required="true" dataType="Currency"  readonly="true" defvalue="0.00"/>
			<emp:text id="ArpBondReducApp.reduc_int" label="债权减免利息金额" maxlength="16" 
			required="true" dataType="Currency" readonly="true" defvalue="0.00"/>
			<emp:text id="ArpBondReducApp.reduc_accord_file" label="减免依据文件" maxlength="80" required="true" 
			colSpan="2" cssElementClass="emp_field_text_cusname" />
			<emp:textarea id="ArpBondReducApp.bond_reduc_reason" label="债权减免原因" maxlength="250" required="true" colSpan="2" />
			<emp:textarea id="ArpBondReducApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpBondReducApp.app_date" label="申请日期" required="false" hidden="true" />
			<emp:date id="ArpBondReducApp.over_date" label="办结日期" required="false" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpBondReducAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpBondReducApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpBondReducApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="true"/>
			<emp:text id="ArpBondReducApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpBondReducApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpBondReducApp.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="ArpBondReducApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="ArpBondReducApp.input_id" label="登记人" required="true"  hidden="true"/>
			<emp:text id="ArpBondReducApp.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpBondReducApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpBondReducApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"  readonly="true" />
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
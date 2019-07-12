<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
.emp_field_select_select {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 210px;
}
</style>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusGoverFinTerList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnLoad(){
		LmtApp_tabs.tabs.pro_tab._clickLink();
		CusGoverFinTer.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		changeMoveDate();
	};
	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusGoverFinTer.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function changeMoveDate(){
		var gover_ter_type = CusGoverFinTer.gover_ter_type._getValue();
		if(gover_ter_type == '001'){
			CusGoverFinTer.move_date._obj._renderRequired(true);
			CusGoverFinTer.move_date._obj._renderHidden(false);
		}else{
			CusGoverFinTer.move_date._setValue("");
			CusGoverFinTer.move_date._obj._renderHidden(true);
			CusGoverFinTer.move_date._obj._renderRequired(false);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
<emp:tabGroup id="LmtApp_tabs" mainTab="tab1" >
	<emp:tab id="pro_tab" label="基本信息" initial="true">
		<emp:gridLayout id="CusGoverFinTerGroup" title="基本信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.serno" label="申请流水号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="CusGoverFinTer.cus_id" label="客户码" readonly="true" colSpan="2" />
			<emp:text id="CusGoverFinTer.cus_id_displayname" label="客户名称"  required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusGoverFinTer.gover_fin_loan_type" label="政府融资贷款类型" required="true" dictname="STD_ZB_GOVER_FIN_TYPE" colSpan="2" />
			<emp:select id="CusGoverFinTer.gover_ter_type" label="平台类别"  required="true" dictname="STD_ZB_TER_TYPE" onchange="changeMoveDate()" />
			<emp:date id="CusGoverFinTer.move_date" label="调出平台时间" required="true" />
			<emp:text id="CusGoverFinTer.cash_cover_rate" label="现金流覆盖率" maxlength="16" required="true" dataType="Rate" readonly="true"/>
			<emp:select id="CusGoverFinTer.cash_cover_degree" label="现金流覆盖程度"  required="true" dictname="STD_ZB_COVER_DEGREE" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" title="余额信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.secu_ln_bal" label="担保贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.crd_ln_bal" label="信用贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.land_property_usufruct_bal" label="土地收益权质押贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.land_mortgage_bal" label="其中土地抵押贷款余额(元)" maxlength="18" required="true" dataType="Currency"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" title="缺口信息" maxColumn="2">
			<emp:text id="CusGoverFinTer.guar_gap" label="担保缺口(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.crd_ln_guar_gap" label="对信用贷款的保证估值缺口(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.secu_ln_gap" label="对担保贷款的缺口(元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusGoverFinTer.fina_guar_gap" label="其中增加财务政担保后的估值缺口(元)" maxlength="18" required="true" dataType="Currency" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusGoverFinTerGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusGoverFinTer.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusGoverFinTer.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" readonly="true"/>
			<emp:text id="CusGoverFinTer.input_id_displayname" label="登记人"  readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusGoverFinTer.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusGoverFinTer.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusGoverFinTer.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGoverFinTer.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGoverFinTer.manager_id" label="责任人" maxlength="20" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusGoverFinTer.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="CusGoverFinTer.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY" colSpan="2"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup >
</body>
</html>
</emp:page>

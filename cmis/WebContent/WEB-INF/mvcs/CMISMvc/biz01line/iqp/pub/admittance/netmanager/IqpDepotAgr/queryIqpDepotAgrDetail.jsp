<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	} 
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 300px;
}
.emp_field_select_select1 {
	width: 600px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpDepotAgrList.do"/>'+"&net_agr_no=${context.net_agr_no}"
                                                               +"&mem_cus_id=${context.mem_cus_id}"
                                                               +"&cus_id=${context.cus_id}"
													           +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doClose(){
        window.close();
	};
	function onload(){
		var fst_deliv_agreed = IqpDepotAgr.fst_deliv_agreed._getValue();
        if("02" == fst_deliv_agreed){
        	IqpDepotAgr.agreed_rate._obj._renderHidden(false);
        	IqpDepotAgr.agreed_rate._obj._renderRequired(true);
        }else{
        	IqpDepotAgr.agreed_rate._setValue("0");
        	IqpDepotAgr.agreed_rate._obj._renderHidden(true);
        	IqpDepotAgr.agreed_rate._obj._renderRequired(false);
        	
        }
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="IqpDepotAgrGroup" title="保兑仓协议" maxColumn="2">
			<emp:text id="IqpDepotAgr.net_agr_no" label="网络编号" maxlength="40" required="false" />
			<emp:text id="IqpDepotAgr.depot_agr_no" label="保兑仓协议号" maxlength="40" required="false" />
			<emp:text id="IqpDepotAgr.cus_id" label="借款人客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpDepotAgr.cus_id_displayname" label="借款人客户名称"  required="false" />
			<emp:text id="IqpDepotAgr.psale_cont" label="购销合同" required="false" />
			<emp:text id="IqpDepotAgr.fst_bail_perc" label="首次保证金比例" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpDepotAgr.fst_deliv_agreed" label="首次提货约定" dictname="STD_ZB_FST_AGREED" required="false" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
			<emp:text id="IqpDepotAgr.agreed_rate" label="约定比率" maxlength="10" required="true" dataType="Percent" colSpan="2"/>
			<emp:text id="IqpDepotAgr.contacc_freq" label="对账频率" dataType="Int" required="false" />
			<emp:select id="IqpDepotAgr.contacc_freq_unit" label="对账频率单位" required="true" dictname="STD_ZB_CONTACC_FREP"/>
			<emp:text id="IqpDepotAgr.desgoods_plan_no" label="订货计划" required="false" />	
			<emp:select id="IqpDepotAgr.contacc_mode" label="对账方式" required="false" dictname="STD_ZB_CONTACC_MODE" />
			<emp:textarea id="IqpDepotAgr.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="IqpDepotAgr.input_id_displayname" label="登记人" required="false" />
			<emp:text id="IqpDepotAgr.input_br_id_displayname" label="登记机构"  required="false" />
			<emp:date id="IqpDepotAgr.input_date" label="登记日期" required="false" />
			<emp:select id="IqpDepotAgr.status" label="状态" required="false" dictname="STD_ZB_STATUS" />
			
			<emp:date id="IqpDepotAgr.start_date" label="协议起始日期" required="false" hidden="true"/>
			<emp:date id="IqpDepotAgr.end_date" label="协议到期日" required="false" hidden="true"/>
			<emp:text id="IqpDepotAgr.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpDepotAgr.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("haveButton".equals(flag)){ %>
		<emp:button id="close" label="关闭"/>
		<%} %>	
	</div>
</body>
</html>
</emp:page>

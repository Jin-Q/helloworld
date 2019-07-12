<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_field_select_select1 {
	width: 350px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		window.close();
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtFassetGroup" title="家庭资产" maxColumn="2">
			<emp:pop id="LmtFasset.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=LmtIndiv&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" readonly="true" required="true"/>
			<emp:text id="LmtFasset.cus_id_displayname" label="客户名称"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFasset.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true" colSpan="2"/>
			<emp:select id="LmtFasset.fasset_type" label="家庭资产类型" required="true" dictname="STD_ZB_FASSET_TYPE" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
			<emp:text id="LmtFasset.autho_name" label="权属人名称" maxlength="10" required="false" />
			<emp:text id="LmtFasset.identy_perc" label="认定比例" maxlength="18" required="true" dataType="Percent" onchange="countAmt()"/>
			<emp:text id="LmtFasset.asset_seval" label="资产原估值" maxlength="18" required="true" defvalue="0.00" dataType="Currency" onchange="countAmt()" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtFasset.asset_ivalue" label="资产认定值" maxlength="18" required="true" defvalue="0.00" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="LmtFasset.memo" label="备注" maxlength="60" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>

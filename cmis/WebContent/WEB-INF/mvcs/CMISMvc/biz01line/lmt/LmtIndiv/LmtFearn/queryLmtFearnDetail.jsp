<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_field_select_select1 {
	width: 450px;
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
	
	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtFearnGroup" title="家庭收入" maxColumn="2">
		<emp:pop id="LmtFearn.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=select * from Cus_Indiv_Soc_Rel a where a.indiv_cus_rel in('1','2','3','9') and a.cus_id_rel='${context.cus_id}'&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true"/>
		<emp:text id="LmtFearn.cus_id_displayname" label="客户名"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
		<emp:select id="LmtFearn.cert_type" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" readonly="true"/>
		<emp:text id="LmtFearn.cert_code" label="证件号码" required="false"  readonly="true"/>
		<emp:select id="LmtFearn.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true"/>
		<emp:select id="LmtFearn.earning_sour" label="收入来源" required="true" dictname="STD_ZB_EARNING_SOUR" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
		<emp:text id="LmtFearn.identy_perc" label="认定比例" maxlength="4" required="false" readonly="true" colSpan="2"/>
		<emp:select id="LmtFearn.ewrant_type" label="收入凭证类别" required="false" dictname="STD_ZB_EWRANT_TYPE" colSpan="2" cssFakeInputClass="emp_field_select_select1"/>
		<emp:text id="LmtFearn.mearn_score" label="月收入原值" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtFearn.ibank_mearn" label="我行认定月收入" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
		<emp:text id="LmtFearn.yearn_score" label="年收入原值" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtFearn.ibank_yearn" label="我行认定年收入" maxlength="18" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<emp:textarea id="LmtFearn.memo" label="备注" maxlength="60" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

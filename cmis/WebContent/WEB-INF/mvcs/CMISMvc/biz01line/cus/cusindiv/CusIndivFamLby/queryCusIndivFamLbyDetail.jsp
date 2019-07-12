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
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
text-align:left;
width:450px;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusIndivFamLbyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doReturnCusIndivFamLby(){
		goback();
	}
		
	function goback(){
		var paramStr="CusIndivFamLby.cus_id="+CusIndivFamLby.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivFamLbyList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivFamLbyGroup" title="负债情况" maxColumn="2">
		<emp:text id="CusIndivFamLby.indiv_debt_id" label="负债编号" maxlength="40" required="true" hidden="true"/>
		<emp:select id="CusIndivFamLby.indiv_debt_typ" label="负债类型" required="true" dictname="STD_ZB_INV_DE_TYP"/>
		<emp:text id="CusIndivFamLby.indiv_creditor" label="债权人" maxlength="80" required="true" />
		<emp:select id="CusIndivFamLby.indiv_debt_cur" label="负债币种" required="true" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="CusIndivFamLby.indiv_debt_amt" label="负债金额(元)" maxlength="18" colSpan="2" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
		<emp:date id="CusIndivFamLby.indiv_debt_str_dt" label="债务开始时间" required="true" />
		<emp:date id="CusIndivFamLby.indiv_debt_end_dt" label="债务到期时间" required="true" />
		<emp:text id="CusIndivFamLby.indiv_debt_desc" label="负债描述" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:textarea id="CusIndivFamLby.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusIndivFamLby.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
		<emp:text id="CusIndivFamLby.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
		<emp:date id="CusIndivFamLby.input_date" label="登记日期" required="false" hidden="true"/>
		<emp:text id="CusIndivFamLby.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
		<emp:date id="CusIndivFamLby.last_upd_date" label="更新日期" required="false" hidden="true"/>
		<emp:text id="CusIndivFamLby.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivFamLby" label="返回"/>
	</div>
</body>
</html>
</emp:page>
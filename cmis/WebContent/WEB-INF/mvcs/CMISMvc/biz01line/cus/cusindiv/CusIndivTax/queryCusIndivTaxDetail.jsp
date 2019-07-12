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

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturnCusIndivTax(){
		goback();
	}
	function goback(){
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivTax.cus_id="+CusIndivTax.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusIndivTaxList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivTaxGroup" title="税费缴纳情况" maxColumn="2">
			<emp:select id="CusIndivTax.indiv_taxes" label="税费种类" required="true" dictname="STD_ZB_INV_TAX_TYP" colSpan="2"/>
			<emp:text id="CusIndivTax.indiv_tax_amt" label="应缴纳/支付金额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndivTax.indiv_tax_pay_amt" label="已缴纳/支付金额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="CusIndivTax.indiv_tax_dt" label="缴纳/支付日期" required="false" />
			<emp:select id="CusIndivTax.indiv_tax_flg" label="是否正常缴纳/支付" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="CusIndivTax.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusIndivTax.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			<emp:text id="CusIndivTax.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="CusIndivTax.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
			<emp:text id="CusIndivTax.input_id_displayname" label="登记人"  hidden="true" />
            <emp:text id="CusIndivTax.input_br_id_displayname" label="登记机构"  hidden="true"/>
            <emp:date id="CusIndivTax.input_date" label="登记日期" hidden="true" />
            <emp:text id="CusIndivTax.last_upd_id_displayname" label="更新人"  hidden="true" />
			<emp:date id="CusIndivTax.last_upd_date" label="更新日期" hidden="true" />
			<emp:text id="CusIndivTax.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusIndivTax.serno" label="编号" maxlength="20" required="true" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivTax" label="返回"/>
	</div>
</body>
</html>
</emp:page>
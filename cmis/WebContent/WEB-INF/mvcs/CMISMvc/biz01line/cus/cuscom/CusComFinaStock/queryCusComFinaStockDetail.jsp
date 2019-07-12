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
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComFinaStock.cus_id._obj.element.value;
		var paramStr="CusComFinaStock.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComFinaStockList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusComFinaStockGroup" title="发行股票信息" maxColumn="2">
			
			<emp:text id="CusComFinaStock.com_stk_code" label="股票代码" maxlength="10" required="true" />
			<emp:text id="CusComFinaStock.com_stk_name" label="股票名称" maxlength="60" required="true" />
			<emp:date id="CusComFinaStock.com_stk_mrk_dt" label="上市日期" required="true" />
			<emp:select id="CusComFinaStock.com_stk_mrk_place" label="上市地" required="true" dictname="STD_ZX_LISTED" />
			<emp:text id="CusComFinaStock.com_stk_mrk_brs" label="交易所名称" maxlength="80" required="false" colSpan="2"/>
			<emp:text id="CusComFinaStock.com_stk_init_amr" label="首次发行价(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComFinaStock.com_stk_cur_amt" label="股票当前价(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComFinaStock.com_stk_eva_amt" label="股票评估价(元)" maxlength="18" required="false" colSpan="2" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComFinaStock.com_stk_cap_qnt" label="当前股本总量(万股)" required="true" dataType="Double" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComFinaStock.com_stk_cur_qnt" label="当前流通股量(万股)" required="false" dataType="Double" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="CusComFinaStock.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			
			<emp:text id="CusComFinaStock.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComFinaStock.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComFinaStock.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComFinaStock.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:date id="CusComFinaStock.last_upd_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="CusComFinaStock.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>

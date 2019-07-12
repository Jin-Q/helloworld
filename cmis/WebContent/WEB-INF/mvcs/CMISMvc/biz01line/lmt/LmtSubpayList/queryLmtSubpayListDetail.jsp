<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
	<html>
	<head>
	<title>详情查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<%
		request.setAttribute("canwrite", "");
		Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String type = "";
		if (context.containsKey("type")) {
			type = context.getDataValue("type").toString();
		}
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
	<emp:gridLayout id="LmtSubpayListGroup" title="代偿业务明细" maxColumn="2">
		<emp:text id="LmtSubpayList.subpay_bill_no" label="代偿业务借据号" required="true" />
		<emp:text id="LmtSubpayList.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" />
		<emp:select id="LmtSubpayList.guar_mode" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true" defvalue="100" hidden="true" />
		<emp:text id="LmtSubpayList.prd_id_displayname" label="业务品种"  required="true" readonly="true" />
		<emp:text id="LmtSubpayList.bill_amt" label="借据金额" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="LmtSubpayList.bill_bal" label="借据余额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="LmtSubpayList.int_cumu" label="欠息累计" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="LmtSubpayList.subpay_cap" label="代偿本金" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="LmtSubpayList.subpay_int" label="代偿利息" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
		<emp:text id="LmtSubpayList.serno" label="业务编号" maxlength="40" required="true" hidden="true" />
		<emp:text id="LmtSubpayList.guar_cus_id" label="担保公司客户码" maxlength="30" required="true" hidden="true" />
		<emp:select id="LmtSubpayList.subpay_status" label="代偿状态" required="false" dictname="STD_ZB_SUBPAY" hidden="true" />
		<emp:text id="LmtSubpayList.pk" label="主键" hidden="true" />
	</emp:gridLayout>
	<div align="center"><br>
	<emp:button id="return" label="关闭" /></div>
	</body>
	</html>
</emp:page>

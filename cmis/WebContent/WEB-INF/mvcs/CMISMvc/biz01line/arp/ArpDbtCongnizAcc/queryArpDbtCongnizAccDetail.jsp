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
		var url = '<emp:url action="queryArpDbtCongnizAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addCusForm(ArpDbtCongnizAcc);
		addContForm(ArpDbtCongnizAcc);
		addBillForm(ArpDbtCongnizAcc);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpDbtCongnizAccGroup" title="呆账认定台账" maxColumn="2">
			<emp:text id="ArpDbtCongnizAcc.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="ArpDbtCongnizAcc.pk_serno" label="流水号" maxlength="40" required="true" hidden="true" />
			<emp:text id="ArpDbtCongnizAcc.cus_id" label="客户码" maxlength="30" required="false" />
			<emp:text id="ArpDbtCongnizAcc.cus_name" label="客户名称" required="false" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpDbtCongnizAcc.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtCongnizAcc.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtCongnizAcc.cn_cont_no" label="中文合同编号"  readonly="true" />
			<emp:text id="ArpDbtCongnizAcc.prd_type" label="产品类别"  readonly="true" />
			<emp:date id="ArpDbtCongnizAcc.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpDbtCongnizAcc.end_date" label="到期日期" required="false" />
			<emp:select id="ArpDbtCongnizAcc.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="ArpDbtCongnizAcc.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtCongnizAcc.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtCongnizAcc.owe_int" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtCongnizAcc.rec_int_accum" label="应收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtCongnizAcc.recv_int_accum" label="实收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="ArpDbtCongnizAcc.four_class" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="ArpDbtCongnizAcc.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:textarea id="ArpDbtCongnizAcc.congniz_resn" label="认定理由" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtCongnizAcc.congniz_date" label="认定日期" required="false" />
			<emp:text id="ArpDbtCongnizAcc.input_id_displayname" label="经办人" readonly="true" required="false"  />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
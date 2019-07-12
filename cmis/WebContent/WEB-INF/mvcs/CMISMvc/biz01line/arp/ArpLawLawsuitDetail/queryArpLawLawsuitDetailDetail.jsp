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
		menuIds = '&subMenuId=ArpLawLawsuitDetail&op=${context.op}';
		var url = '<emp:url action="queryArpLawLawsuitDetailList.do"/>?serno='+ArpLawLawsuitDetail.serno._getValue()+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addContForm(ArpLawLawsuitDetail);
		addCusForm(ArpLawLawsuitDetail);
		addBillForm(ArpLawLawsuitDetail);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpLawLawsuitDetailGroup" title="诉讼明细信息" maxColumn="2">
			<emp:text id="ArpLawLawsuitDetail.pk_serno" label="流水号" maxlength="40"  hidden="true"/>
			<emp:text id="ArpLawLawsuitDetail.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpLawLawsuitDetail.bill_no" label="借据编号" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.cont_no" label="合同编号" required="true"  readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="ArpLawLawsuitDetail.cus_name" label="客户名称" colSpan="2"
			required="true" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:text id="ArpLawLawsuitDetail.prd_type" label="产品类型" readonly="true" required="true" colSpan="2"/>
			<emp:text id="ArpLawLawsuitDetail.loan_amt" label="贷款金额" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.loan_balance" label="贷款余额" dataType="Currency" required="true" readonly="true"/>			
			<emp:text id="ArpLawLawsuitDetail.rec_int_accum" label="应收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.recv_int_accum" label="实收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.distr_date" label="发放日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.end_date" label="到期日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpLawLawsuitDetail.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpLawLawsuitDetail.lawsuit_sub" label="诉讼标的" maxlength="16" required="true" dataType="Currency" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

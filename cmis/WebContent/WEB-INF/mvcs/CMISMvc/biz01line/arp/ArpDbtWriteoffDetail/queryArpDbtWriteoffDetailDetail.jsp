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
		menuIds = '&subMenuId=ARP_DBT_WRITEOFF_DETAIL&op=${context.op}';
		var url = '<emp:url action="queryArpDbtWriteoffDetailList.do"/>'+postStr+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
		addContForm(ArpDbtWriteoffDetail);
		addBillForm(ArpDbtWriteoffDetail);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
		<emp:gridLayout id="ArpDbtWriteoffDetailGroup" title="呆账核销明细" maxColumn="2">
			<emp:text id="ArpDbtWriteoffDetail.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:text id="ArpDbtWriteoffDetail.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpDbtWriteoffDetail.bill_no" label="借据编号" required="true" readonly="false" />
			<emp:text id="ArpDbtWriteoffDetail.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtWriteoffDetail.prd_type" label="产品类型" readonly="true"  colSpan="2"/>
			<emp:date id="ArpDbtWriteoffDetail.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpDbtWriteoffDetail.end_date" label="到期日期" required="false" />
			<emp:text id="ArpDbtWriteoffDetail.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.owe_int" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtWriteoffDetail.writeoff_cap" label="核销本金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.writeoff_int" label="核销利息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.writeoff_amt" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
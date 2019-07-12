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
		menuIds = '&subMenuId=ARP_BOND_REDUC_DETAIL&op=${context.op}';
		var url = '<emp:url action="queryArpBondReducDetailList.do"/>'+postStr+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
		addContForm(ArpBondReducDetail);
		addBillForm(ArpBondReducDetail);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpBondReducDetailGroup" title="债权减免明细" maxColumn="2">
			<emp:text id="ArpBondReducDetail.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpBondReducDetail.pk_serno" label="流水号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpBondReducDetail.bill_no" label="借据编号" maxlength="40" required="true" />
			<emp:text id="ArpBondReducDetail.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpBondReducDetail.prd_type" label="产品类型" readonly="true" colSpan="2"/>
			<emp:date id="ArpBondReducDetail.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpBondReducDetail.end_date" label="到期日期" required="false" />			
			<emp:text id="ArpBondReducDetail.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" colSpan="2" />
			<emp:text id="ArpBondReducDetail.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_cap" label="减免本金" maxlength="16" required="true" dataType="Currency" defvalue="0.00" />
			<emp:text id="ArpBondReducDetail.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_inner_owe_int" label="减免表内利息" maxlength="16" required="true" dataType="Currency" defvalue="0.00" />
			<emp:text id="ArpBondReducDetail.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_out_owe_int" label="减免表外利息" maxlength="16" required="true" dataType="Currency" defvalue="0.00" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
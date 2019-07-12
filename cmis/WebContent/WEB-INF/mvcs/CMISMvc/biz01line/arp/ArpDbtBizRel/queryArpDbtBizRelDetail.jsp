<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		menuIds = '&subMenuId=ARP_DBT_BIZ_REL&op=${context.op}';
		var url = '<emp:url action="queryArpDbtBizRelList.do"/>'+postStr+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
		addContForm(ArpDbtBizRel);
		addBillForm(ArpDbtBizRel);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="ArpDbtBizRelGroup" title="呆账业务关系" maxColumn="2">
			<emp:text id="ArpDbtBizRel.serno" label="业务编号" maxlength="40" hidden="true" defvalue="${context.serno}"/>
			<emp:text id="ArpDbtBizRel.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpDbtBizRel.bill_no" label="借据编号" required="true"  />
			<emp:text id="ArpDbtBizRel.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtBizRel.prd_type" label="产品类型" readonly="true"  colSpan="2"/>
			<emp:date id="ArpDbtBizRel.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpDbtBizRel.end_date" label="到期日期" required="false" />
			<emp:text id="ArpDbtBizRel.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.owe_int" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtBizRel.rec_int_accum" label="应收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.recv_int_accum" label="实收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="ArpDbtBizRel.four_class" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="ArpDbtBizRel.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="ArpDbtBizRel.twelve_class" label="十二级分类" required="false" dictname="STD_ZB_TWELVE_CLASS" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
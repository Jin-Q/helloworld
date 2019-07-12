<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		menuIds = '&subMenuId=ARP_BOND_REDUC_DETAIL&op=${context.op}';
		var url = '<emp:url action="queryArpBondReducDetailList.do"/>'+postStr+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){
		doPubUpdate(ArpBondReducDetail);
	};

	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
		addContForm(ArpBondReducDetail);
		addBillForm(ArpBondReducDetail);
	};

	/*** 各种减免金额校验begin ***/
	function checkAmt(obj){
		var notice;
		if(obj.name == 'ArpBondReducDetail.reduc_cap'){	//校验：借据余额 > 减免本金
			big_amt = ArpBondReducDetail.loan_balance._getValue();
			small_amt = ArpBondReducDetail.reduc_cap._getValue();
			notice = "[减免本金]应该小于[借据余额]";
		}
		if(obj.name == 'ArpBondReducDetail.reduc_inner_owe_int'){	//校验：表内欠息 > 减免表内利息
			big_amt = ArpBondReducDetail.inner_owe_int._getValue();
			small_amt = ArpBondReducDetail.reduc_inner_owe_int._getValue();
			notice = "[减免表内利息]应该小于[表内欠息]";
		}
		if(obj.name == 'ArpBondReducDetail.reduc_out_owe_int'){	//校验：表外欠息 > 减免表外利息
			big_amt = ArpBondReducDetail.out_owe_int._getValue();
			small_amt = ArpBondReducDetail.reduc_out_owe_int._getValue();
			notice = "[减免表外利息]应该小于[表外欠息]";
		}

		big_amt = parseFloat(big_amt);
		small_amt = parseFloat(small_amt);
		if(small_amt > big_amt){
			alert(notice);
			obj.value = '0.00';
		}
	};
	/*** 各种减免金额校验end ***/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateArpBondReducDetailRecord.do" method="POST">
		<emp:gridLayout id="ArpBondReducDetailGroup" maxColumn="2" title="债权减免明细">
			<emp:text id="ArpBondReducDetail.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpBondReducDetail.pk_serno" label="流水号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="ArpBondReducDetail.bill_no" label="借据编号" maxlength="40" required="true" />
			<emp:text id="ArpBondReducDetail.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpBondReducDetail.prd_type" label="产品类型" readonly="true" colSpan="2"/>
			<emp:date id="ArpBondReducDetail.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpBondReducDetail.end_date" label="到期日期" required="false" />
			<emp:text id="ArpBondReducDetail.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" colSpan="2" />
			
			<emp:text id="ArpBondReducDetail.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_cap" label="减免本金" maxlength="16" required="true" readonly="false"
			dataType="Currency" defvalue="0.00" onchange="checkAmt(this)"/>
			<emp:text id="ArpBondReducDetail.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_inner_owe_int" label="减免表内利息" maxlength="16" readonly="false"
			required="true" dataType="Currency" defvalue="0.00" onchange="checkAmt(this)"/>
			<emp:text id="ArpBondReducDetail.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpBondReducDetail.reduc_out_owe_int" label="减免表外利息" maxlength="16" readonly="false"
			required="true" dataType="Currency" defvalue="0.00" onchange="checkAmt(this)"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
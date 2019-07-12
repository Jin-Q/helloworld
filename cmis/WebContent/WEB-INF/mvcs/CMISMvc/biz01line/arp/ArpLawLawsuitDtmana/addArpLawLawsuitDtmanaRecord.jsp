<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String case_no= (String)request.getParameter("case_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	/*** 选择借据并校验begin ***/
	function selCusId(data){
		bill_no = data.bill_no._getValue();
		cont_no = data.cont_no._getValue();
		cus_id = data.cus_id._getValue();
		cus_id_displayname = data.cus_id_displayname._getValue();
		distr_date = data.distr_date._getValue();
		end_date = data.end_date._getValue();	
		loan_amt = data.loan_amt._getValue();
		loan_balance = data.loan_balance._getValue();
		rec_int_accum = data.rec_int_accum._getValue();
		recv_int_accum = data.recv_int_accum._getValue();
		prd_id_displayname = data.prd_id_displayname._getValue();

		var url="<emp:url action='checkAssetPreserve.do'/>&type="+subMenuId+"&value="+bill_no+"&serno=<%=case_no%>";
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpLawLawsuitDtmana.bill_no._setValue(bill_no);
			ArpLawLawsuitDtmana.cont_no._setValue(cont_no);
			ArpLawLawsuitDtmana.cus_id._setValue(cus_id);
			ArpLawLawsuitDtmana.cus_id_displayname._setValue(cus_id_displayname);
			ArpLawLawsuitDtmana.distr_date._setValue(distr_date);
			ArpLawLawsuitDtmana.end_date._setValue(end_date);	
			ArpLawLawsuitDtmana.loan_amt._setValue(loan_amt);
			ArpLawLawsuitDtmana.loan_balance._setValue(loan_balance);
			ArpLawLawsuitDtmana.rec_int_accum._setValue(rec_int_accum);
			ArpLawLawsuitDtmana.recv_int_accum._setValue(recv_int_accum);
			ArpLawLawsuitDtmana.prd_id_displayname._setValue(prd_id_displayname);
		}else{
			alert("此借据已存在于此次诉讼中!");
		}
	};
	/*** 选择借据并校验end ***/
	
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpLawLawsuitDtmana);
	};

	function doReturn() {
		var url = '<emp:url action="queryArpLawLawsuitDtmanaList.do"/>?case_no=<%=case_no%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doLoad(){
		subMenuId = "${context.subMenuId}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpLawLawsuitDtmanaRecord.do" method="POST">
		
		<emp:gridLayout id="ArpLawLawsuitDtmanaGroup" title="诉讼明细管理" maxColumn="2">
			<emp:text id="ArpLawLawsuitDtmana.pk_serno" label="流水号" maxlength="40"  hidden="true"/>
			<emp:text id="ArpLawLawsuitDtmana.case_no" label="案件编号" maxlength="40" required="true" hidden="true" defvalue="<%=case_no%>"  />
			<emp:pop id="ArpLawLawsuitDtmana.bill_no" label="借据编号" url="queryArpBadassetAccPop.do?returnMethod=selCusId" required="true" />
			<emp:text id="ArpLawLawsuitDtmana.cont_no" label="合同编号" required="true"  readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="ArpLawLawsuitDtmana.cus_id_displayname" label="客户名称" colSpan="2"
			required="true" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:text id="ArpLawLawsuitDtmana.prd_id_displayname" label="产品类型" readonly="true" required="true" colSpan="2"/>
			<emp:text id="ArpLawLawsuitDtmana.loan_amt" label="贷款金额" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.loan_balance" label="贷款余额" dataType="Currency" required="true" readonly="true"/>			
			<emp:text id="ArpLawLawsuitDtmana.rec_int_accum" label="应收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.recv_int_accum" label="实收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.distr_date" label="发放日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.end_date" label="到期日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDtmana.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
			<emp:text id="ArpLawLawsuitDtmana.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
			<emp:text id="ArpLawLawsuitDtmana.lawsuit_sub" label="诉讼标的" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>
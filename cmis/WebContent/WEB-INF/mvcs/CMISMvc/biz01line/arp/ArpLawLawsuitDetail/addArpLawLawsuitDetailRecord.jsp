<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String serno= (String)request.getParameter("serno");
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
		cus_id_displayname = data.cus_name._getValue();
		distr_date = data.start_date._getValue();
		end_date = data.end_date._getValue();	
		loan_amt = data.loan_amt._getValue();
		loan_balance = data.loan_balance._getValue();
		rec_int_accum = data.rec_int_accum._getValue();
		recv_int_accum = data.recv_int_accum._getValue();
		prd_id_displayname = data.prd_id_displayname._getValue();

		var url="<emp:url action='checkAssetPreserve.do'/>&type="+subMenuId+"&value="+bill_no+"&serno=<%=serno%>";
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpLawLawsuitDetail.bill_no._setValue(bill_no);
			ArpLawLawsuitDetail.cont_no._setValue(cont_no);
			ArpLawLawsuitDetail.cus_id._setValue(cus_id);
			ArpLawLawsuitDetail.cus_id_displayname._setValue(cus_id_displayname);
			ArpLawLawsuitDetail.distr_date._setValue(distr_date);
			ArpLawLawsuitDetail.end_date._setValue(end_date);	
			ArpLawLawsuitDetail.loan_amt._setValue(loan_amt);
			ArpLawLawsuitDetail.loan_balance._setValue(loan_balance);
			ArpLawLawsuitDetail.rec_int_accum._setValue(rec_int_accum);
			ArpLawLawsuitDetail.recv_int_accum._setValue(recv_int_accum);
			ArpLawLawsuitDetail.prd_id_displayname._setValue(prd_id_displayname);
		}else{
			alert("此借据已存在于此次诉讼中!");
		}
	};
	/*** 选择借据并校验end ***/
	
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpLawLawsuitDetail);
	};

	function doReturn() {
		var url = '<emp:url action="queryArpLawLawsuitDetailList.do"/>?serno=<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doLoad(){
		subMenuId = "${context.subMenuId}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()" >
	
	<emp:form id="submitForm" action="addArpLawLawsuitDetailRecord.do" method="POST">
		
		<emp:gridLayout id="ArpLawLawsuitDetailGroup" title="诉讼明细信息" maxColumn="2">
			<emp:text id="ArpLawLawsuitDetail.pk_serno" label="流水号" maxlength="40"  hidden="true"/>
			<emp:text id="ArpLawLawsuitDetail.serno" label="业务编号" maxlength="40" required="true" hidden="true" defvalue="<%=serno%>"  />
			<emp:pop id="ArpLawLawsuitDetail.bill_no" label="借据编号" url="queryBillNoPop.do?condition= and acc_status not in ('0','9','10','11') &moduleId=arp&returnMethod=selCusId&flag=2" required="true" />
			<emp:text id="ArpLawLawsuitDetail.cont_no" label="合同编号" required="true"  readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="ArpLawLawsuitDetail.cus_id_displayname" label="客户名称" colSpan="2"
			required="true" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:text id="ArpLawLawsuitDetail.prd_id_displayname" label="产品类型" readonly="true" required="true" colSpan="2"/>
			<emp:text id="ArpLawLawsuitDetail.loan_amt" label="贷款金额" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.loan_balance" label="贷款余额" dataType="Currency" required="true" readonly="true"/>			
			<emp:text id="ArpLawLawsuitDetail.rec_int_accum" label="应收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.recv_int_accum" label="实收利息累计" dataType="Currency" required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.distr_date" label="发放日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.end_date" label="到期日期"  required="true" readonly="true"/>
			<emp:text id="ArpLawLawsuitDetail.lawsuit_cap" label="诉讼本金" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
			<emp:text id="ArpLawLawsuitDetail.lawsuit_int" label="诉讼利息" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
			<emp:text id="ArpLawLawsuitDetail.lawsuit_sub" label="诉讼标的" maxlength="16" required="true" dataType="Currency" defvalue="0"/>
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
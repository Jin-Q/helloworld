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
		//var url = '<emp:url action="queryPspTaxDetailList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
	function checkPaidDate(){
		var paid_start_date = PspTaxDetail.paid_start_date._getValue();
		var paid_end_date = PspTaxDetail.paid_end_date._getValue();
		if(paid_start_date!=''&&paid_end_date!=''){
			if(CheckDate1BeforeDate2(paid_end_date,paid_start_date)){
				alert('税务缴纳结束日期不能小于等于税务缴纳起始日期！');
				PspTaxDetail.paid_end_date._setValue('');
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspTaxDetailGroup" title="税费明细" maxColumn="2">
			
			<emp:select id="PspTaxDetail.tax_type" label="税费类型" required="true" dictname="STD_PSP_TAX_TYPE"/>
			<emp:text id="PspTaxDetail.tax_amt" label="缴费金额" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:date id="PspTaxDetail.paid_start_date" label="税务缴纳起始日期" required="true" onblur="checkPaidDate()"/>
			<emp:date id="PspTaxDetail.paid_end_date" label="税务缴纳结束日期" required="true" onblur="checkPaidDate()"/>
			<emp:select id="PspTaxDetail.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2"/>
			<emp:textarea id="PspTaxDetail.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspTaxDetailGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspTaxDetail.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="PspTaxDetail.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
			<emp:date id="PspTaxDetail.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspTaxDetail.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspTaxDetail.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspTaxDetail.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspTaxDetail.task_id" label="任务编号" required="false" hidden="true"/>
			<emp:text id="PspTaxDetail.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

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
		//var url = '<emp:url action="queryPspFeeDetailList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspFeeDetailGroup" title="水电费明细" maxColumn="2">
			<emp:date id="PspFeeDetail.last_regi_date" label="缴纳起始日期" required="true" />
			<emp:date id="PspFeeDetail.regi_date" label="缴纳结束日期" required="true" />
			
			<emp:text id="PspFeeDetail.paid_acct_name" label="缴费账户名" maxlength="100" required="true" colSpan="2"/>
			<emp:select id="PspFeeDetail.paid_type" label="缴费类别" required="true" dictname="STD_PSP_PAID_TYPE"/>
			<emp:text id="PspFeeDetail.qnt" label="数量（吨/度）" maxlength="38" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PspFeeDetail.paid_amt" label="缴费金额（元）" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PspFeeDetail.breach_amt" label="违约金（元）" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="PspFeeDetail.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2"/>
			<emp:textarea id="PspFeeDetail.remarks" label="备注" maxlength="250" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="PspFeeDetailGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspFeeDetail.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspFeeDetail.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspFeeDetail.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspFeeDetail.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspFeeDetail.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspFeeDetail.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspFeeDetail.task_id" label="任务编号" required="true" hidden="true"/>
			<emp:text id="PspFeeDetail.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

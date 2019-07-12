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
		var url = '<emp:url action="queryIqpPackLoanList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpPackLoanGroup" title="打包贷款从表" maxColumn="2">
			<emp:text id="IqpPackLoan.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpPackLoan.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpPackLoan.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:text id="IqpPackLoan.cdt_cert_no" label="信用证编号" maxlength="40" required="false" />
			<emp:select id="IqpPackLoan.cdt_cert_cur_type" label="信用证币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpPackLoan.cdt_cert_amt" label="信用证金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpPackLoan.cdt_cert_bal" label="信用证余额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpPackLoan.is_internal_cert" label="是否国内证" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpPackLoan.cdt_cert_app_advice" label="国业部信用证审查意见" maxlength="250" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String cus_id=request.getParameter("CusHoldChrem.cus_id");
%>
<emp:page>
<html>
<head>
<title>持有理财信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var paramStr="CusHoldChrem.cus_id="+CusHoldChrem.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusHoldChremList.do"/>?'+ paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusHoldChremGroup" title="理财基本信息" maxColumn="2">
		<emp:text id="CusHoldChrem.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
		<emp:text id="CusHoldChrem.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
		<emp:text id="CusHoldChrem.prod_name" label="产品名称" maxlength="80" required="true" />
		<emp:select id="CusHoldChrem.chrem_type" label="理财类型" required="false" dictname="STD_CHREM_TYPE" defvalue="1"/>
		<emp:date id="CusHoldChrem.subscr_date" label="认购时间" required="true" />
		<emp:text id="CusHoldChrem.subscr_amt" label="认购金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:date id="CusHoldChrem.start_date" label="开始时间"  required="true" onblur="CheckRegDate(CusHoldChrem.start_date,CusHoldChrem.end_date);"/>
		<emp:date id="CusHoldChrem.end_date" label="到期时间" required="true" onblur="CheckRegDate(CusHoldChrem.start_date,CusHoldChrem.end_date);"/>
		<emp:text id="CusHoldChrem.expect_income_rate" label="预期收益率" maxlength="4" required="true" dataType="Percent" readonly="true" cssElementClass="emp_field_text_readonly"/>
	</emp:gridLayout>
	<emp:gridLayout id="CusHoldChremGroup" title="登记信息" maxColumn="2">
		<emp:text id="CusHoldChrem.input_id_displayname" label="登记人"  required="false" />
		<emp:text id="CusHoldChrem.input_br_id_displayname" label="登记机构"  required="false" />
		<emp:text id="CusHoldChrem.input_date" label="登记日期" maxlength="10" required="false" />
		<emp:text id="CusHoldChrem.serno" label="流水号" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
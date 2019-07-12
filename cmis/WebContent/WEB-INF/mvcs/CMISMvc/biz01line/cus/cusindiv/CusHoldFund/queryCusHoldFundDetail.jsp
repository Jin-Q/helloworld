<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String cusid = (String)request.getParameter("CusHoldFund.cus_id");
%>
<emp:page>
<html>
<head>
<title>持有资金信息</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	//返回列表页面
  	function doReturn(){
  		var editFlag = '${context.EditFlag}';
  		var paramStr="CusHoldFund.cus_id="+CusHoldFund.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusHoldFundList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusHoldFundGroup" title="持有基金信息" maxColumn="2">
			<emp:text id="CusHoldFund.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusHoldFund.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
			<emp:text id="CusHoldFund.prod_name" label="产品名称" maxlength="80" required="true" />
			<emp:select id="CusHoldFund.fund_type" label="理财类型" required="false" dictname="STD_FUND_TYPE" defvalue="1"/>
			<emp:date id="CusHoldFund.subscr_date" label="认购时间" required="true" />
			<emp:text id="CusHoldFund.hold_shr" label="持有份额" maxlength="16" required="true" />
			<emp:date id="CusHoldFund.start_date" label="开始时间"  required="true" onblur="CheckRegDate(CusHoldFund.start_date,CusHoldFund.end_date);"/>
			<emp:date id="CusHoldFund.end_date" label="到期时间" required="true" onblur="CheckRegDate(CusHoldFund.start_date,CusHoldFund.end_date);"/>
			<emp:text id="CusHoldFund.expect_income_rate" label="预期收益率" maxlength="4" required="true" dataType="Percent" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="CusHoldFund.acct_no" label="账号" maxlength="32" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="CusHoldFundGroup" title="登记信息" maxColumn="2">
			<emp:text id="CusHoldFund.input_id_displayname" label="登记人"  required="false" />
			<emp:text id="CusHoldFund.input_br_id_displayname" label="登记机构"  required="false" />
			<emp:text id="CusHoldFund.input_date" label="登记日期" maxlength="10" defvalue="$OPENDAY" required="false" />
			<emp:text id="CusHoldFund.serno" label="流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusHoldFund.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusHoldFund.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表信息"/>
	</div>
</body>
</html>
</emp:page>
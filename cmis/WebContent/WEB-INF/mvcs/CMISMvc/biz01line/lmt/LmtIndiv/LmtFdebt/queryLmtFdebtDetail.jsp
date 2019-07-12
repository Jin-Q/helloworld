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
	
	/*--user code begin--*/
	function doReturn() {
		window.close();
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtFdebtGroup" title="家庭负债" maxColumn="2">
			<emp:pop id="LmtFdebt.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=LmtIndiv&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" readonly="true" required="true"/>
			<emp:text id="LmtFdebt.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFdebt.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" />
			<emp:select id="LmtFdebt.debt_type" label="负债类型" required="true" dictname="STD_ZB_DEBT_TYPE"/>
			<emp:select id="LmtFdebt.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="LmtFdebt.debt_amt" label="负债金额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtFdebt.debt_bal" label="负债余额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="LmtFdebt.start_date" label="债务开始日期" required="false" />
			<emp:date id="LmtFdebt.over_date" label="债务结束日期" required="false" />
			<emp:textarea id="LmtFdebt.memo" label="备注" maxlength="60" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>

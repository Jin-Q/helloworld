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
		var fin_serno = LmtQuotaAdjustApp.fin_serno._getValue();
		var url = '<emp:url action="queryLmtQuotaAdjustAppList.do"/>?serno='+fin_serno;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtQuotaAdjustAppGroup" title="用信限额调整" maxColumn="2">
			<emp:text id="LmtQuotaAdjustApp.fin_agr_no" label="融资协议编号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="LmtQuotaAdjustApp.fin_totl_limit" label="融资总额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjustApp.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjustApp.single_quota_his" label="单户限额(存量)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjustApp.single_quota_new" label="单户限额(新增)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjustApp.inure_date" label="生效日期" maxlength="10" required="false" />
			<emp:text id="LmtQuotaAdjustApp.end_date" label="到期日期" maxlength="10" required="false" />
			<emp:text id="LmtQuotaAdjustApp.fin_serno" label="审批流水" required="false" hidden="false"/>
			<emp:select id="LmtQuotaAdjustApp.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:select id="LmtQuotaAdjustApp.serno" label="主键" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

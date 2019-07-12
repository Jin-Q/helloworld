<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String agr_no = request.getParameter("agr_no");
	String single_quota = request.getParameter("single_quota");
	String fin_totl_limit = request.getParameter("fin_totl_limit");
	String fin_totl_spac = request.getParameter("fin_totl_spac");
%>

<script type="text/javascript">
	
	function doReturn() {
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit = '<%=fin_totl_limit%>';
		var fin_totl_spac = '<%=fin_totl_spac%>';
		var url = '<emp:url action="queryLmtQuotaAdjustList.do"/>?agr_no='+agr_no+'&single_quota='+single_quota+'&fin_totl_limit='+fin_totl_limit+"&fin_totl_spac="+fin_totl_spac;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtQuotaAdjustGroup" title="用信限额调整" maxColumn="2">
			<emp:text id="LmtQuotaAdjust.fin_agr_no" label="融资协议编号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="LmtQuotaAdjust.fin_totl_limit" label="融资总额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjust.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjust.single_quota_his" label="单户限额(存量)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjust.single_quota_new" label="单户限额(新增)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtQuotaAdjust.inure_date" label="生效日期" maxlength="10" required="false" />
			<emp:text id="LmtQuotaAdjust.end_date" label="到期日期" maxlength="10" required="false" />
			<emp:select id="LmtQuotaAdjust.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:select id="LmtQuotaAdjust.serno" label="主键" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

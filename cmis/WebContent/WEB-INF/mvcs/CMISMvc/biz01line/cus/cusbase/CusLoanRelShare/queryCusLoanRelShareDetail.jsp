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
		var url = '<emp:url action="queryCusLoanRelShareList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusLoanRelShareGroup" title="共享客户" maxColumn="2">
			<emp:text id="CusLoanRelShare.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="CusLoanRelShare.br_id" label="机构" maxlength="20" required="true" />
			<emp:select id="CusLoanRelShare.bank_flg" label="是否主办机构" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="CusLoanRelShare.agri_flg" label="是否农户" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="CusLoanRelShare.cus_name" label="客户名称" maxlength="80" required="true" />
			<emp:select id="CusLoanRelShare.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" />
			<emp:select id="CusLoanRelShare.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusLoanRelShare.cert_code" label="证件号码" maxlength="20" required="true" />
			<emp:text id="CusLoanRelShare.main_cus_mgr" label="主管客户经理" maxlength="20" required="true" />
			<emp:text id="CusLoanRelShare.opt_cus_mgr" label="托管客户经理/共享客户经理" maxlength="20" required="true" />
			<emp:text id="CusLoanRelShare.area_code" label="区域编码" maxlength="12" required="false" />
			<emp:text id="CusLoanRelShare.area_name" label="区域编码" maxlength="100" required="false" />
			<emp:text id="CusLoanRelShare.out_cus_id" label="外部客户码" maxlength="30" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

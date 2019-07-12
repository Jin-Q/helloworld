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
		var url = '<emp:url action="queryArpLawDebtorManaList.do"/>?case_no='+ArpLawMemberMana.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="ArpLawMemberManaGroup" title="涉及人员管理" maxColumn="2">
			<emp:text id="ArpLawMemberMana.pk_serno" label="流水号" maxlength="40" required="true" />
			<emp:text id="ArpLawMemberMana.case_no" label="案件编号" maxlength="40" required="true" />
			<emp:select id="ArpLawMemberMana.member_type" label="人员类别" required="true" 
			defvalue="002" dictname="STD_ZB_MEMBER_TYPE" hidden="true" />
			<emp:text id="ArpLawMemberMana.cus_id" label="客户码" required="true"  colSpan="2" />
			<emp:text id="ArpLawMemberMana.cus_name" label="客户名" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="ArpLawMemberMana.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="ArpLawMemberMana.cert_code" label="证件号码" required="true" readonly="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

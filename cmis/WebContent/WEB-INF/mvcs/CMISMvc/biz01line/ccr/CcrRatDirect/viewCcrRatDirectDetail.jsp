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
		var url = '<emp:url action="queryCcrRatDirectList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CcrRatDirectGroup" title="评级直接认定" maxColumn="2">
			<emp:text id="CcrRatDirect.serno" label="业务申请编号" maxlength="40" required="true" colSpan="2" readonly="true"/>
			<emp:pop id="CcrRatDirect.cus_id" label="客户码" url="queryCusLoanRelList.do?cusTypCondition=&returnMethod=returnCus" required="false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="CcrRatDirect.cus_type" label="客户类型" required="false" dictname="STD_ZB_CUS_TYPE"/>
			<emp:text id="CcrRatDirect.cus_name" label="客户名称 " maxlength="60" required="false" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
			<emp:select id="CcrRatDirect.grade" label="信用等级" required="false" dictname="STD_ZB_CREDIT_GRADE"/>
			<emp:text id="CcrRatDirect.score" label="信用评分" maxlength="16" required="false" readonly="true" hidden="true"/>
			<emp:textarea id="CcrRatDirect.memo" label="认定理由" maxlength="1000" required="false" colSpan="2"/>
			<emp:text id="CcrRatDirect.input_date" label="认定日期" maxlength="10" required="false" readonly="true" defvalue=""/>
			<emp:date id="CcrRatDirect.end_date" label="到期日期" required="false" onblur="CheckDt()"/>
			<emp:text id="CcrRatDirect.cust_mgr_displayname" label="客户经理"  required="false"  readonly="true"/>
			<emp:text id="CcrRatDirect.input_br_id_displayname" label="经办机构"  required="false" readonly="true"/>
			<emp:text id="CcrRatDirect.cust_mgr" label="客户经理" maxlength="20" required="false"  readonly="true" hidden="true"/>
			<emp:text id="CcrRatDirect.input_br_id" label="经办机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CcrRatDirect.approve_status" label="审批状态" maxlength="20" required="false" defvalue="000" readonly="true" hidden="false"/>
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

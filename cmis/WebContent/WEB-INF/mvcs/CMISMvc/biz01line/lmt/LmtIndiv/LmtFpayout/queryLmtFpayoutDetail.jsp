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
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="LmtFpayoutGroup" title="家庭支出" maxColumn="2">
			<emp:pop id="LmtFpayout.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=Ind&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="LmtFpayout.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFpayout.cert_type" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="LmtFpayout.cert_code" label="证件号码" required="false"  readonly="true"/>
			<emp:select id="LmtFpayout.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true" colSpan="2"/>
			<emp:select id="LmtFpayout.fpayout_type" label="家庭支出类型" required="false" dictname="STD_ZB_FPAYOUT_TYPE"  colSpan="2"/>
			<emp:text id="LmtFpayout.mpayout" label="月支出" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtFpayout.ypayout" label="年支出" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="LmtFpayout.memo" label="备注" maxlength="60" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>

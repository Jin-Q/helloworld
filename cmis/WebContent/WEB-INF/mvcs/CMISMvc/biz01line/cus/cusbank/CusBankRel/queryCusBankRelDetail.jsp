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
		var url = '<emp:url action="queryCusBankRelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusBankRelGroup" title="客户与我行关系" maxColumn="2">
			<emp:text id="CusBankRel.cus_id" label="客户码" maxlength="20" required="true" />
			<emp:text id="CusBankRel.cus_name" label="客户名称" maxlength="60" required="false" />
			<emp:select id="CusBankRel.cus_bank_rel" label="客户与我行关系"  required="false" dictname="STD_ZB_CUS_BANK"/>
			<emp:text id="CusBankRel.reg_date" label="登记日期" maxlength="10" required="false" />
			<emp:text id="CusBankRel.reg_name" label="登记人" maxlength="60" required="false" hidden="true"/>
			<emp:text id="CusBankRel.reg_org" label="登记机构" maxlength="60" required="false" hidden="true"/>
			<emp:text id="CusBankRel.reg_name_displayname" label="登记人"  required="false" readonly="true"/>		
			<emp:text id="CusBankRel.reg_org_displayname" label="登记机构"  required="false" readonly="true" />	
			<emp:text id="CusBankRel.cus_bank_rel_name" label="客户与我行关系名称" maxlength="60" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

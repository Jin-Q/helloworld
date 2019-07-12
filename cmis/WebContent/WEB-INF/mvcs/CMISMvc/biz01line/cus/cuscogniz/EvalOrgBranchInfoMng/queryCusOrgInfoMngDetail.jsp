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
		var cus_id = CusOrgInfoMng.cus_id._getValue();
		var url = '<emp:url action="queryCusOrgInfoMngList.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
		<emp:gridLayout id="CusOrgInfoMngGroup" title="评估机构分部信息" maxColumn="2">
			<emp:text id="CusOrgInfoMng.serno" label="申请流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusOrgInfoMng.cus_id" label="客户码" maxlength="30" required="true" hidden="true" />
			<emp:select id="CusOrgInfoMng.branch_type" label="类型" required="true" dictname="STD_ZB_BRANCH_TYPE" />
			<emp:pop id="CusOrgInfoMng.branch_addr_displayname" label="地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgInfoMng.street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>			
			<emp:text id="CusOrgInfoMng.duty_man" label="负责人" maxlength="30" required="true" />
			<emp:text id="CusOrgInfoMng.phone" label="联系电话" maxlength="30" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

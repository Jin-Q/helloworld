<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%	
	String cus_id=request.getParameter("cus_id"); 
	String ops=request.getParameter("ops");
	String app_serno=request.getParameter("app_serno");
%>
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
		var url = '<emp:url action="queryCusOrgInfoList.do"/>?cus_id='+"<%=cus_id%>"+'&ops='+"<%=ops%>"+'&serno='+"<%=app_serno%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusOrgInfoGroup" title="评估机构分部信息" maxColumn="2">
			<emp:text id="CusOrgInfo.serno" label="申请流水号" maxlength="40" required="false" hidden="true" />
			<emp:text id="CusOrgInfo.cus_id" label="客户码" maxlength="30" required="true" hidden="true" />
			<emp:select id="CusOrgInfo.branch_type" label="类型" required="true" dictname="STD_ZB_BRANCH_TYPE" />
			<emp:pop id="CusOrgInfo.branch_addr_displayname" label="地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgInfo.street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusOrgInfo.duty_man" label="负责人" maxlength="30" required="true" />
			<emp:text id="CusOrgInfo.phone" label="联系电话" maxlength="30" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusOrgInfo.app_serno" label="业务流水号" maxlength="30" required="false" hidden="true" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<emp:tabGroup mainTab="mainTab" id="mainTabs">
			<emp:tab label="申请基本信息" id="mainTab" needFlush="true" url="getIqpLoanAppForDiscUpdatePage.do?menuId=queryIqpLoanApp&op=update&serno=${context.serno}&cus_id=${context.cus_id}"></emp:tab>
			<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>

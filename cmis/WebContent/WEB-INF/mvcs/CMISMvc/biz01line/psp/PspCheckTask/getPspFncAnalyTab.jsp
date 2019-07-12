<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
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
		<emp:tab label="财务指标分析" id="fnctab1" needFlush="true" url="subjectAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="杜邦财务分析" id="fnctab2" needFlush="true" url="dupontAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="财务比率分析" id="fnctab3" needFlush="true" url="finaRateAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="结构分析" id="fnctab14" needFlush="true" url="struAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
	<%if("view".equals(op)){ %>
		<emp:tab label="财务分析" id="mainTab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
	<%}else{ %>
		<emp:tab label="财务分析" id="mainTab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
	<%} %>
	</emp:tabGroup>
</body>
</html>
</emp:page>

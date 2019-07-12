<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String task_type = "";
	String op = "";
	if(context.containsKey("task_type")){
		task_type = (String)context.getDataValue("task_type");
	}
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
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
	<%if(!op.equals("view")){ %>
		<%if(!task_type.equals("03")){ %>
			<emp:tab label="现场检查表（公司、小微）" id="mainTab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckcom1.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（固定资产）" id="propertytab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckindivcon1.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（抵质押）" id="custab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckgrt1.raq&task_id=${context.task_id}"></emp:tab>
		<%}else{ %>
			<emp:tab label="现场检查表（个人经营性）" id="mainTab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckindivope1.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（个人消费）" id="custab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckpro1.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（抵质押）" id="propertytab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckgrt1.raq&task_id=${context.task_id}"></emp:tab>
		<%} %>
	<%}else{ %>
		<%if(!task_type.equals("03")){ %>
			<emp:tab label="现场检查表（公司、小微）" id="mainTab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckcom1View.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（固定资产）" id="propertytab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckindivcon1View.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（抵质押）" id="custab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckgrt1View.raq&task_id=${context.task_id}"></emp:tab>
		<%}else{ %>
			<emp:tab label="现场检查表（个人经营性）" id="mainTab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckindivope1View.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（个人消费）" id="custab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckpro1View.raq&task_id=${context.task_id}"></emp:tab>
			<emp:tab label="现场检查表（抵质押）" id="propertytab" needFlush="true" url="getReportShowPage.do?reportId=psp/curcheckgrt1View.raq&task_id=${context.task_id}"></emp:tab>
		<%} %>
	<%} %>

	</emp:tabGroup>
</body>
</html>
</emp:page>

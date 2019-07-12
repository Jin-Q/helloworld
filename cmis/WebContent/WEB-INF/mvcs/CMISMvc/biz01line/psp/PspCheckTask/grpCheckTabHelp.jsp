<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
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
			<emp:tab label="用信情况" id="mainTab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.cus_id}"></emp:tab>
			<emp:tab label="借款人分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
			<emp:tab label="固定资产贷款（项目融资）分析" id="propertytab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
			<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
		<%//	<emp:tab label="财务分析" id="finatab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab> %>
			<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.task_id}&cus_id=${context.cus_id}"></emp:tab>
			<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
			<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
			<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
			<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.task_type}"></emp:tab>
		<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab> %>
			<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
			<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.cus_id}&op=view"></emp:tab>
	<%}else{ %>
			<emp:tab label="用信情况" id="mainTab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.cus_id}"></emp:tab>
			<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
			<emp:tab label="固定资产贷款（项目融资）分析" id="propertytab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
			<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
		<%//	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab> %>
			<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.task_id}&cus_id=${context.cus_id}"></emp:tab>
			<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
			<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
			<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
			<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.task_type}"></emp:tab>
		<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab> %>
			<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
			<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.cus_id}&op=view"></emp:tab>
	<%} %>
	</emp:tabGroup>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kcoll = (KeyedCollection)context.getDataElement("PspBatchTaskRel");
	context.put("major_task_id",kcoll.getDataValue("major_task_id").toString());
	context.put("manager_id",kcoll.getDataValue("manager_id").toString());
	context.put("manager_br_id",kcoll.getDataValue("manager_br_id").toString());
	context.put("batch_task_type",kcoll.getDataValue("batch_task_type").toString());
	String batch_task_type="";
	if(kcoll.containsKey("batch_task_type")){
	 	batch_task_type = kcoll.getDataValue("batch_task_type").toString();
	 }
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryPspBatchTaskRelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content">
	<emp:form id="queryForm" action="#" method="POST">
		<emp:tabGroup mainTab="batch_task_tab" id="mainTab" >	
		<emp:tab label="贷后批量任务" id="batch_task_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="PspBatchTaskRelGroup" title="贷后批量任务" maxColumn="2">
			<emp:text id="PspBatchTaskRel.major_task_id" label="主任务编号" maxlength="40" required="true"  readonly="true"/>
			<emp:select id="PspBatchTaskRel.batch_task_type" label="批量任务类型" dictname="STD_BATCH_TASK_TYPE" required="true"  readonly="true"/>
			<emp:select id="PspBatchTaskRel.task_type" label="任务类型" dictname="STD_ZB_TASK_TYPE" required="true" defvalue="09" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_id_displayname" label="主管客户经理" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_br_id_displayname" label="主管机构" maxlength="20" required="false" cssElementClass="emp_currency_text_readonly"/>
						<emp:text id="PspBatchTaskRel.task_create_date" label="任务生成日期" readonly="true" hidden="true" />
			<emp:text id="PspBatchTaskRel.task_request_time" label="要求完成日期" readonly="true" hidden="true" />
			<emp:text id="PspBatchTaskRel.manager_id" label="主管客户经理" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspBatchTaskRel.manager_br_id" label="主管机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<%if(batch_task_type.equals("01")){ %>
		<emp:gridLayout id="batch_task_tab" maxColumn="2" title="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见">
			<emp:date id="PspBatchTaskRel.check_time" label="批量任务检查时间" required="true" readonly="true"/>
			<emp:textarea id="PspBatchTaskRel.check_view" label="意见" maxlength="1000" required="true" readonly="true" colSpan="2"/>
		</emp:gridLayout>
		<%}else if(batch_task_type.equals("02")){ %>
		<emp:gridLayout id="batch_task_tab" maxColumn="2" title=" ">
			<emp:date id="PspBatchTaskRel.check_time" label="批量任务检查时间" required="true" readonly="true"/>
			<emp:textarea id="PspBatchTaskRel.check_view" label="意见" maxlength="1000" required="false" hidden="true"/>
		</emp:gridLayout>
		<%} %>
		</emp:tab>
		<emp:tab label="贷后子任务明细" id="sub_task_tab" initial="false" needFlush="true" url="queryPspBatchTaskList.do?major_task_id=${context.major_task_id}&manager_id=${context.manager_id}&batch_task_type=${context.batch_task_type}&op=view " />
	   <%if((kcoll.getDataValue("batch_task_type").toString()).equals("01")) {%>
	    <emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcarandhouse_view.raq&task_id=${context.major_task_id}"/>
	    <%}else{ %>
	    <emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportpersonloan100_view.raq&task_id=${context.major_task_id}"/>
	    <%} %>
	   </emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="return" label="返回列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>
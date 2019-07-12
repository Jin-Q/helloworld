<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>

<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
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
		var task_set_id = IqpChkStoreSet.task_set_id._getValue();
		var url = '<emp:url action="queryIqpChkStoreSetList.do"/>&task_set_id='+task_set_id+"&menuId=IqpChkStoreSet";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="核/巡任务基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpChkStoreSetGroup" maxColumn="2" title="核/巡任务设定">
			<emp:text id="IqpChkStoreSet.task_set_id" label="任务编号" maxlength="32" required="true" readonly="true" />
			<emp:select id="IqpChkStoreSet.task_set_type" label="任务维度" required="false" dictname="STD_ZB_INSURE_MODE" readonly="true"/>
			<%
				String task_set_type = "";
				if(context.containsKey("task_set_type")){
					task_set_type = (String)context.getDataValue("task_set_type");
				}
				if(task_set_type.equals("01")){//出质人
			%>
			<emp:text id="IqpChkStoreSet.cus_id" label="出质人编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreSet.cus_id_displayname" label="出质人名称" required="false" readonly="true"/>
			<% }else{%>
			<emp:text id="IqpChkStoreSet.cus_id" label="监管企业编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreSet.cus_id_displayname" label="监管企业名称" required="false" readonly="true"/>
			<% }%>
			
			<emp:pop id="IqpChkStoreSet.task_exe_user_displayname" label="任务执行人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:date id="IqpChkStoreSet.task_request_time" label="要求完成时间" required="true" />
			<emp:textarea id="IqpChkStoreSet.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">	
			<emp:text id="IqpChkStoreSet.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpChkStoreSet.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpChkStoreSet.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="IqpChkStoreSet.task_exe_user" label="任务执行人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpChkStoreSet.input_id" label="登记人" maxlength="20" required="false" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="IqpChkStoreSet.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true"/>
		</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

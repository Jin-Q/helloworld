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
		var url = '<emp:url action="queryPspAltSignalList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspAltSignalGroup" title="预警信号" maxColumn="2">
			<emp:text id="PspAltSignal.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="PspAltSignal.cus_id_displayname" label="客户名称"  required="false" />
			<emp:textarea id="PspAltSignal.signal_info" label="风险预警信息内容及影响" maxlength="500" required="false" colSpan="2" />
			<emp:select id="PspAltSignal.signal_type" label="类型" required="false" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
			<emp:text id="PspAltSignal.last_date" label="预计持续时间（天）" maxlength="10" required="false" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="PspAltSignal.disp_mode" label="处置措施及进展情况" maxlength="50" required="false" colSpan="2" />
			<emp:select id="PspAltSignal.signal_status" label="信号状态" required="false" dictname="STD_ZB_COMM_STATUS" />
		</emp:gridLayout>
		<emp:gridLayout id="PspAltSignalGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspAltSignal.input_id_displayname" label="登记人"  required="false" />
			<emp:text id="PspAltSignal.input_br_id_displayname" label="登记机构"  required="false" />
			<emp:date id="PspAltSignal.input_date" label="登记日期" required="false" />
			
			<emp:text id="PspAltSignal.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAltSignal.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAltSignal.task_id" label="任务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="PspAltSignal.pk_id" label="主键" maxlength="32" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

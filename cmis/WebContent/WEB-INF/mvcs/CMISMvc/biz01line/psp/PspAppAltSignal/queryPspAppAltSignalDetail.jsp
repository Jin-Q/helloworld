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
	/*	var url = '<emp:url action="queryPspAppAltSignalList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;*/
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspAppAltSignalGroup" title="预警信号申请" maxColumn="2">
			<emp:textarea id="PspAppAltSignal.signal_info" label="风险预警信息内容及影响" maxlength="2000" required="true" colSpan="2" />
			<emp:select id="PspAppAltSignal.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE"/>
			<emp:text id="PspAppAltSignal.last_date" label="预计持续时间（天）" maxlength="10" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="PspAppAltSignal.disp_mode" label="处置措施及进展情况" maxlength="50" required="true" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspAppAltSignal.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspAppAltSignal.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspAppAltSignal.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspAppAltSignal.input_id" label="登记人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspAppAltSignal.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAppAltSignal.pk_id" label="主键" maxlength="32" required="true" hidden="true"/>
			<emp:text id="PspAppAltSignal.task_id" label="任务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="PspAppAltSignal.cus_id" label="客户编码" maxlength="40" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>

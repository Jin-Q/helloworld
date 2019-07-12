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
	
/*	function doReturn() {
		var url = '<emp:url action="queryPspRavelSignalListList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};*/
	
	/*--user code begin--*/
	function doClose(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspRavelSignalListGroup" title="解除预警信号申请关联表" maxColumn="2">
		<emp:text id="PspRavelSignalList.serno" label="申请编号" maxlength="32" required="true" />
		<emp:text id="PspRavelSignalList.pk_id" label="预警信号ID" maxlength="40" required="true" />
		<emp:text id="PspRavelSignalList.cus_id" label="客户码" maxlength="40" required="false" />
		<emp:text id="PspRavelSignalList.cus_id_displayname" label="客户名称"  required="false" />
		<emp:textarea id="PspRavelSignalList.signal_info" label="风险预警信息内容及影响" maxlength="500" required="true" colSpan="2" />
		<emp:select id="PspRavelSignalList.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="PspRavelSignalList.last_date" label="预计持续时间（天）" maxlength="10" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
		<emp:textarea id="PspRavelSignalList.disp_mode" label="处置措施及进展情况" maxlength="50" required="true" colSpan="2" />
	</emp:gridLayout>
	<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
		<emp:text id="PspRavelSignalList.input_id_displayname" label="登记人" required="false" />
		<emp:text id="PspRavelSignalList.input_br_id_displayname" label="登记机构" required="false" />
		<emp:date id="PspRavelSignalList.input_date" label="登记日期" required="false" />
		
		<emp:text id="PspRavelSignalList.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
		<emp:text id="PspRavelSignalList.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

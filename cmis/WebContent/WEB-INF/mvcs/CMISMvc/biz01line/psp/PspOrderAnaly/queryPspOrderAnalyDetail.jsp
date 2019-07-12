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
	/*	var url = '<emp:url action="queryPspOrderAnalyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;*/
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspOrderAnalyGroup" title="订单明细" maxColumn="2">
			<emp:text id="PspOrderAnaly.rcver_name" label="需方名称" maxlength="100" required="true" />
			<emp:date id="PspOrderAnaly.order_date" label="签订时间" required="false" />
			<emp:text id="PspOrderAnaly.prd_name" label="产品名称" maxlength="100" required="true" colSpan="2"/>
			
			<emp:text id="PspOrderAnaly.amt" label="金额" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:date id="PspOrderAnaly.provid_date" label="供货时间" required="false" />
			<emp:text id="PspOrderAnaly.qnt" label="供货数量" maxlength="38" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="PspOrderAnaly.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspOrderAnaly.input_id_displayname" label="登记人"  required="false" readonly="true" />
			<emp:text id="PspOrderAnaly.input_br_id_displayname" label="登记机构"  required="false" readonly="true" />
			<emp:text id="PspOrderAnaly.input_date" label="登记日期" maxlength="10" required="false" readonly="true" />
			
			<emp:text id="PspOrderAnaly.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspOrderAnaly.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspOrderAnaly.pk_id" label="主键" maxlength="32" readonly="true" hidden="true" />
			<emp:text id="PspOrderAnaly.task_id" label="任务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="PspOrderAnaly.cus_id" label="客户编码" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

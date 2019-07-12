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
	
	/*--user code begin--*/
	function doReturn() {
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpBuscontInfoGroup" title="贸易合同信息" maxColumn="2">
			<emp:text id="IqpBuscontInfo.tcont_no" label="贸易合同编号" maxlength="40" required="false" hidden="false" />
			<emp:text id="IqpBuscontInfo.tcont_amt" label="贸易合同金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpBuscontInfo.sup_mat_cprt" label="供货单位" maxlength="80" required="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/>
			<emp:date id="IqpBuscontInfo.start_date" label="贸易合同起始日"  required="true" />
			<emp:date id="IqpBuscontInfo.end_date" label="贸易合同到期日"  required="false" />
			<emp:textarea id="IqpBuscontInfo.trade_detail" label="贸易合同交易内容" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="IqpBuscontInfo.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			
			<emp:text id="IqpBuscontInfo.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpBuscontInfo.po_no" label="池编号" maxlength="30" required="false" hidden="true"/>
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>

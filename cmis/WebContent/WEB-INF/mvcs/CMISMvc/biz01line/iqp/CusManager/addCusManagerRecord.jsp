<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusManagerRecord.do" method="POST">
		
		<emp:gridLayout id="CusManagerGroup" title="客户经理登记" maxColumn="2">
			<emp:text id="CusManager.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="CusManager.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:select id="CusManager.biz_type" label="业务类型" required="false" />
			<emp:text id="CusManager.manager_id" label="客户经理" maxlength="20" required="false" />
			<emp:select id="CusManager.is_main_manager" label="是否主管客户经理" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="CusManager.ser_rate" label="业务比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="CusManager.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="CusManager.input_org" label="登记机构" maxlength="20" required="false" />
			<emp:date id="CusManager.input_date" label="登记日期" required="false" />
			<emp:date id="CusManager.update_date" label="变更日期" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


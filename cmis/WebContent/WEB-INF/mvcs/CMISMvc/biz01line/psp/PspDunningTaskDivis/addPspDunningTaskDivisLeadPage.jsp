<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doLoad(){
		
	}
	
	//选择客户POP框返回方法
	function returnCus(data){
		PspDunningTaskDivis.cus_id._setValue(data.cus_id._getValue());
		PspDunningTaskDivis.cus_id_displayname._setValue(data.cus_name._getValue());
	}

	//新增催收任务
	function doAddPspDun(){
		var form = document.getElementById("submitForm");
		PspDunningTaskDivis._toForm(form);
		form.submit();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="getPspDunningTaskDivisAddPage.do" method="POST">
		<emp:gridLayout id="PspDunningTaskDivisGroup" title="催收任务分配" maxColumn="2">
			<emp:pop id="PspDunningTaskDivis.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="true"/>
			<emp:text id="PspDunningTaskDivis.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addPspDun" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>


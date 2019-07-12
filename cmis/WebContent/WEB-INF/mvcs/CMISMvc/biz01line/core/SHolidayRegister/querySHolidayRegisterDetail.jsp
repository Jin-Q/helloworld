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
		var url = '<emp:url action="querySHolidayRegisterList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SHolidayRegisterGroup" title="用户休假登记" maxColumn="2">
			<emp:text id="SHolidayRegister.actorno" label="用户码" maxlength="8" required="true" />
			<emp:text id="SHolidayRegister.actorno_displayname" label="用户名称" required="true" />			
			<emp:date id="SHolidayRegister.begin_date" label="开始日期" required="true" />
			<emp:date id="SHolidayRegister.plan_end_date" label="预计到期日期" required="true" />
			<emp:date id="SHolidayRegister.real_end_date" label="实际到期日期" required="false" />
			<emp:textarea id="SHolidayRegister.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:select id="SHolidayRegister.status" label="状态" required="false" dictname="STD_DRFPO_STATUS"/>
	</emp:gridLayout>
	
	<emp:gridLayout id="SHolidayRegisterGroup" maxColumn="2" title="登记信息">
			<emp:text id="SHolidayRegister.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="SHolidayRegister.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="SHolidayRegister.input_id" label="登记人" required="true"  hidden="true"/>
			<emp:text id="SHolidayRegister.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="SHolidayRegister.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:text id="SHolidayRegister.serno" label="流水号" maxlength="40" required="false" hidden="true" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>

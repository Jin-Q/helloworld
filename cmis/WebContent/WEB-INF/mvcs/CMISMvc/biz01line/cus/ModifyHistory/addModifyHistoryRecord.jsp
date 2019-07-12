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
	
	<emp:form id="submitForm" action="addModifyHistoryRecord.do" method="POST">
		
		<emp:gridLayout id="ModifyHistoryGroup" title="MODIFY_HISTORY" maxColumn="2">
			<emp:text id="ModifyHistory.key_id" label="KEY_ID" maxlength="30" required="true" />
			<emp:text id="ModifyHistory.table_name" label="TABLE_NAME" maxlength="20" required="false" />
			<emp:text id="ModifyHistory.cus_id" label="CUS_ID" maxlength="30" required="false" />
			<emp:text id="ModifyHistory.modify_record" label="MODIFY_RECORD" maxlength="-1" required="false" />
			<emp:text id="ModifyHistory.modify_time" label="MODIFY_TIME" maxlength="30" required="false" />
			<emp:text id="ModifyHistory.modify_user_id" label="MODIFY_USER_ID" maxlength="8" required="false" />
			<emp:text id="ModifyHistory.modify_user_ip" label="MODIFY_USER_IP" maxlength="20" required="false" />
			<emp:text id="ModifyHistory.modify_status" label="MODIFY_STATUS" maxlength="1" required="false" />
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


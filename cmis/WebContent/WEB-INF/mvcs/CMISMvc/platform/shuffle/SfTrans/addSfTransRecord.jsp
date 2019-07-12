<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>����ҳ��</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSfTransRecord.do" method="POST">
		
		<emp:gridLayout id="SfTransGroup" title="交易定义" maxColumn="2">
			<emp:text id="SfTrans.trans_id" label="交易ID" maxlength="32" required="true" />
			<emp:text id="SfTrans.trans_name" label="交易名称" maxlength="50" required="true" />
			<emp:text id="SfTrans.trans_permission" label="交易授权" maxlength="50" required="false" />
			<emp:text id="SfTrans.trans_ext" label="交易预处理" maxlength="100" required="false" />
			<emp:textarea id="SfTrans.trans_desc" label="交易描述" maxlength="500" required="false" colSpan="2" />
			<emp:text id="SfTrans.sysid" label="sysid" maxlength="32" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="ȷ��" op="add"/>
			<emp:button id="reset" label="ȡ��"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


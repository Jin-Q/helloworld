<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updatePrdSubTabActionRecord.do" method="POST">
		<emp:gridLayout id="PrdSubTabActionGroup" maxColumn="2" title="从资源操作权限管理">
			<emp:text id="PrdSubTabAction.pkid" label="主键" maxlength="40" hidden="true" colSpan="2" required="true" readonly="true" />
			<emp:text id="PrdSubTabAction.main_id" label="主资源关联模块" maxlength="80" required="true" readonly="true"/>
			<emp:text id="PrdSubTabAction.main_act_id" label="主资源操作ID" maxlength="20" required="true" readonly="true"/>
			<emp:text id="PrdSubTabAction.sub_id" label="从资源关联模块" maxlength="80" required="true" readonly="true"/>
			<emp:text id="PrdSubTabAction.sub_act_id" label="从资源操作ID" maxlength="20" required="true" readonly="true"/>
			<emp:pop id="PrdSubTabAction.rule" label="过滤规则" url="rulespop.do?id=PrdSubTabAction.rule" required="false" buttonLabel="选择规则"  />
			<emp:textarea id="PrdSubTabAction.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PrdSubTabAction.input_id" label="登记人员" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:date id="PrdSubTabAction.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="PrdSubTabAction.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

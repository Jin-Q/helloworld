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
	
	<emp:form id="submitForm" action="updateWfiSpNameListRecord.do" method="POST">
		<emp:gridLayout id="WfiSpNameListGroup" maxColumn="2" title="授信特别权限授权名单表">
			<emp:text id="WfiSpNameList.pk_id" label="主键" maxlength="36" required="true" />
			<emp:text id="WfiSpNameList.cus_id" label="客户号" maxlength="30" required="false" />
			<emp:text id="WfiSpNameList.sp_right_type" label="特殊权限类型" maxlength="3" required="false" />
			<emp:text id="WfiSpNameList.memo" label="描述" maxlength="100" required="false" />
			<emp:text id="WfiSpNameList.manager_br_id" label="责任机构码" maxlength="20" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

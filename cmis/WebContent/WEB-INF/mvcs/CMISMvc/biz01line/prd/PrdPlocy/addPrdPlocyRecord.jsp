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
	
	<emp:form id="submitForm" action="addPrdPlocyRecord.do" method="POST">
		
		<emp:gridLayout id="PrdPlocyGroup" title="政策资料管理" maxColumn="2">
			<emp:text id="PrdPlocy.schemecode" label="政策资料代码" maxlength="30" required="true" />
			<emp:text id="PrdPlocy.schemedesc" label="政策资料描述" maxlength="200" required="false" />
			<emp:select id="PrdPlocy.ifwarrant" label="是否权证类" dictname="STD_ZX_YES_NO" required="false" />
			<emp:select id="PrdPlocy.schemetype" label="政策资料类型" required="false" dictname="STD_ZB_INFO_TYPE" />
			<emp:text id="PrdPlocy.outline" label="外部链接" maxlength="100" required="false" />
			<emp:text id="PrdPlocy.videocode" label="影像关联代码" maxlength="30" required="false" />
			
			<emp:text id="PrdPlocy.inputid" label="登记人员" maxlength="20" required="false" readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PrdPlocy.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="PrdPlocy.orgid" label="登记机构" maxlength="20" required="false" readonly="true" defvalue="${context.organNo}"/>
			<emp:textarea id="PrdPlocy.comments" label="备注" maxlength="200" required="false" colSpan="2"/>
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


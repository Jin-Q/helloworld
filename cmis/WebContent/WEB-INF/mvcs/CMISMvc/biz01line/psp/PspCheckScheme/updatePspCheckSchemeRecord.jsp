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
	
	<emp:form id="submitForm" action="updatePspCheckSchemeRecord.do" method="POST">
		<emp:tabGroup mainTab="catalogTab" id="mainTab">
			<emp:tab label="贷后检查方案" id="catalogTab">
				<emp:gridLayout id="PspCheckSchemeGroup" maxColumn="2" title="贷后检查项方案">
					<emp:text id="PspCheckScheme.scheme_id" label="方案编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="PspCheckScheme.scheme_name" label="方案名称" maxlength="100" required="false" />
					<emp:textarea id="PspCheckScheme.memo" label="备注" colSpan="2" maxlength="250" required="false" />
					<emp:text id="PspCheckScheme.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
					<emp:date id="PspCheckScheme.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
					<emp:text id="PspCheckScheme.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="submit" label="保存" op="update"/>
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:tab>
			<emp:tab label="检查方案目录配置" id="relTab" url="queryPspSchCatRelList.do" needFlush="true" reqParams="scheme_id=$PspCheckScheme.scheme_id;"/>
		</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>

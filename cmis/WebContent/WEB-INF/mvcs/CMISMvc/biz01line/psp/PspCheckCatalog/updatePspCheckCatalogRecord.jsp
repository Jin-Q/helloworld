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
	
	<emp:form id="submitForm" action="updatePspCheckCatalogRecord.do" method="POST">
		<emp:tabGroup mainTab="catalogTab" id="mainTab">
			<emp:tab label="检查目录信息" id="catalogTab">
				<emp:gridLayout id="PspCheckCatalogGroup" maxColumn="2" title="检查目录表">
					<emp:text id="PspCheckCatalog.catalog_id" label="目录编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="PspCheckCatalog.catalog_name" label="目录名称" maxlength="100" required="false" />
					<emp:text id="PspCheckCatalog.memo" label="备注" maxlength="250" required="false" />
					<emp:text id="PspCheckCatalog.input_id" label="登记人" maxlength="20" required="false" />
					<emp:date id="PspCheckCatalog.input_date" label="登记日期" required="false" />
					<emp:text id="PspCheckCatalog.input_br_id" label="登记机构" maxlength="20" required="false" />
				</emp:gridLayout>
				<div align="center">
					<br>
					<emp:button id="submit" label="保存" op="update"/>
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:tab>
			<emp:tab label="检查目录项目配置" id="relTab" url="queryPspCatItemRelList.do" needFlush="true" reqParams="catalog_id=$PspCheckCatalog.catalog_id;"/>
		</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:600px;

}
.emp_input1{
border:1px solid #b7b7b7;
width:600px;
height:100px;
}
</style>
<script type="text/javascript">
	/*--user code begin--*/
		function returnMsg(data){
		PrdCatalog.supcatalogid._setValue(data.id);
		PrdCatalog.cataloglevel._setValue(data.locate+","+PrdCatalog.catalogid._getValue());
	
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePrdCatalogRecord.do" method="POST">
		<emp:gridLayout id="PrdCatalogGroup" maxColumn="2" title="产品目录表">
			<emp:text id="PrdCatalog.catalogid" label="目录编号" maxlength="14" required="true" readonly="true" />
			<emp:text id="PrdCatalog.catalogname" label="目录名称" maxlength="40" required="true" />
			<emp:pop id="PrdCatalog.supcatalogid" label="上级目录编码"  required="false" url="showDicTree_PRD.do" returnMethod="returnMsg" buttonLabel="选择"/>	
			<emp:text id="PrdCatalog.supcatalogname" label="上级目录名称" maxlength="40" readonly="true" required="false" />
			<emp:text id="PrdCatalog.cataloglevel" label="目录层级" maxlength="200" readonly="true" required="true"  cssElementClass="emp_input"/>	
			<emp:text id="PrdCatalog.inputid" label="登记人员" maxlength="30" readonly="true" required="false" hidden="true"/>
			<emp:text id="PrdCatalog.inputdate" label="登记日期" maxlength="10" readonly="true" required="false" hidden="true"/>		
			<emp:text id="PrdCatalog.orgid" label="登记机构" maxlength="20" readonly="true" required="false" hidden="true"/>			
			<emp:textarea id="PrdCatalog.comments" label="备注" maxlength="200" required="false" colSpan="2" cssElementClass="emp_input1"/>
		    
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

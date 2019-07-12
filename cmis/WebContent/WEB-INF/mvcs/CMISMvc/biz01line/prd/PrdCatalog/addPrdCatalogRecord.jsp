<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

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
		if(data.locate==""){
			PrdCatalog.cataloglevel._setValue(data.id+","+PrdCatalog.catalogid._getValue());
		}else{
			PrdCatalog.cataloglevel._setValue(data.locate+","+PrdCatalog.catalogid._getValue());
		}
		
		PrdCatalog.supcatalogname._setValue(data.label);
	
	}

	function getCataloglevel(){
		var catalogId = PrdCatalog.catalogid._getValue();
		var supcatalogId = PrdCatalog.supcatalogid._getValue();
		PrdCatalog.cataloglevel._setValue(supcatalogId+","+catalogId);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdCatalogRecord.do" method="POST">
		<emp:gridLayout id="PrdCatalogGroup" title="产品目录表" maxColumn="2">
			<emp:text id="PrdCatalog.catalogid" label="目录编号" maxlength="14" required="false" hidden="true" />
			<emp:text id="PrdCatalog.catalogname" label="目录名称" maxlength="40" required="true" />
			 <!--emp:pop id="PrdCatalog.supcatalogid" label="上级目录编码"  required="false" url="showDicTree.do?dicTreeTypeId=STD_ZB_CATALOG_TYPE" returnMethod="returnMsg" -->
			<emp:pop id="PrdCatalog.supcatalogid" label="上级目录编码"  required="false" url="showDicTree_PRD.do" returnMethod="returnMsg" buttonLabel="选择"/>
			<emp:text id="PrdCatalog.supcatalogname" label="上级目录名称" maxlength="40" readonly="true" required="false" />								
			<emp:text id="PrdCatalog.cataloglevel" label="目录层级" maxlength="200" hidden="false" required="false" readonly="true" cssElementClass="emp_input"/>	
			<emp:textarea id="PrdCatalog.comments" label="备注" maxlength="200" required="false" colSpan="2" cssElementClass="emp_input1"/>	
			<emp:text id="PrdCatalog.inputid" label="登记人员" maxlength="30" required="false" readonly="true" hidden="true"/>
			<emp:text id="PrdCatalog.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" hidden="true"/>
			<emp:text id="PrdCatalog.orgid" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>			
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


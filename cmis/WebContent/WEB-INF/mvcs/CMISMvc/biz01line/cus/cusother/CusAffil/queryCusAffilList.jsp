<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusAffil._toForm(form);
		CusAffilList._obj.ajaxQuery(null,form);
	};
		
	function doReset(){
		page.dataGroups.CusAffilGroup.reset();
	};
	
	/*--user code begin--*/
	function doPrintTable(){
		var form = document.getElementById("queryForm");
		CusAffil._toForm(form);
		form.submit();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form method="POST" action="expCusAffilToExcel.do" id="queryForm">
		<emp:gridLayout id="CusAffilGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CusAffil.cus_id" label="客户码" />
			<emp:text id="CusAffil.cus_name" label="客户名称" />
			<emp:select id="CusAffil.affil_rela" label="关联关系" dictname="STD_ZB_AFFIL_RELA" />
		</emp:gridLayout>
	</emp:form>	
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="printTable" label="导出Excel" op="print"/>
	</div>
	<form method="POST" action="#" id="listForm">
	<emp:table icollName="CusAffilList" pageMode="true" url="pageCusAffilQuery.do">
		<emp:text id="affil_rela" label="关联关系" dictname="STD_ZB_AFFIL_RELA" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="auth_crd_balance" label="授信金额" dataType="Currency"/>
		<emp:text id="cus_affil_id" label="关联客户码" />
		<emp:text id="cus_affil_name" label="关联客户名称" />
		<emp:text id="affil_auth_crd_balance" label="关联授信金额" dataType="Currency"/>
	</emp:table>
	</form>
	</body>
</html>
</emp:page>
    
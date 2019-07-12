<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBankCusRel._toForm(form);
		resultSet._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusBankCusRelGroup.reset();
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusBankCusRelGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CusBankCusRel.cus_id" label="客户码" />
			<emp:text id="CusBankCusRel.cus_id_displayname" label="关联客户名称" />
			<emp:select id="CusBankCusRel.cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK" />
			<emp:select id="CusBankCusRel.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusBankCusRel.cert_code" label="证件号码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="resultSet" pageMode="true" url="pageCusBankCusRelQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="关联客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK" />
		<emp:text id="bank_duty" label="在我行职务" dictname="STD_ZB_BANK_DUTY" />
		<emp:text id="equi_no" label="股权证号码" />
		<emp:text id="bank_equi_amt" label="拥有我行股份金额(元)" dataType="Currency"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
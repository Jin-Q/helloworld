<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var catalog_no = IqpCommoProvider.mort_catalog_no._getValue();
		var url = '<emp:url action="queryIqpCommoProviderList.do"/>?catalog_no='+catalog_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpCommoProviderGroup" title="商品供应商管理" maxColumn="2">
		<emp:text id="IqpCommoProvider.mort_catalog_no" label="押品目录编号" maxlength="30" required="true" readonly="true" defvalue="${context.catalog_no}" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:pop id="IqpCommoProvider.provider_no" label="供应商编号" url="queryAllCusPop.do?cusTypCondition=Com&returnMethod=returnCus" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="IqpCommoProvider.provider_no_displayname" label="供应商名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" /> 
			<emp:select id="IqpCommoProvider.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="IqpCommoProvider.cert_code" label="证件号码" maxlength="20" readonly="true" required="true"/>
			<emp:text id="IqpCommoProvider.linkman" label="联系人" maxlength="20" required="true" />
			<emp:text id="IqpCommoProvider.link_phone" label="联系电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpCommoProvider.link_addr" label="联系地址" colSpan="2" hidden="true"/>
			<emp:pop id="IqpCommoProvider.link_addr_displayname" label="联系地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnLinkAddr" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="IqpCommoProvider.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="IqpCommoProvider.input_id_displayname" label="登记人" required="false" readonly="true" />
			<emp:text id="IqpCommoProvider.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:text id="IqpCommoProvider.input_id" label="登记人" maxlength="60" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpCommoProvider.input_br_id" label="登记机构" maxlength="60" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="IqpCommoProvider.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpCommoProvider.status" label="状态" maxlength="20" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>

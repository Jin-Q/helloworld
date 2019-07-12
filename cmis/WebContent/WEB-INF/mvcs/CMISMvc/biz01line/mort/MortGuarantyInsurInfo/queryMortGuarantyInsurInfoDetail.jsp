<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var guaranty_no = MortGuarantyInsurInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantyInsurInfoList.do"/>?menuIdTab=mort_maintain&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="MortGuarantyInsurInfoGroup" title="记录抵质押物保险信息" maxColumn="2">
			<emp:text id="MortGuarantyInsurInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="MortGuarantyInsurInfo.insu_org_type" label="保险机构类型" required="true" dictname="STD_INSU_ORG_TYPE" hidden="true"/>
			<emp:text id="MortGuarantyInsurInfo.insu_org_no" label="保险机构编号" maxlength="40" />
			<emp:text id="MortGuarantyInsurInfo.insu_org_name" label="保险机构名称" maxlength="100" required="true" />
			<emp:text id="MortGuarantyInsurInfo.insuarance_no" label="保险单编号" maxlength="40" required="true" />
			<emp:text id="MortGuarantyInsurInfo.insurant" label="被保险人" maxlength="100" required="true" />
			<emp:text id="MortGuarantyInsurInfo.beneficiar" label="受益人" maxlength="100" required="true" />
			<emp:select id="MortGuarantyInsurInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="MortGuarantyInsurInfo.insure_amt" label="投保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="MortGuarantyInsurInfo.insur_start_date" label="起始日期" required="true" />
			<emp:date id="MortGuarantyInsurInfo.inure_date" label="生效日期" required="true" />
			<emp:date id="MortGuarantyInsurInfo.insur_end_date" label="到期日期" required="true" />
			<emp:select id="MortGuarantyInsurInfo.insu_type" label="保险险种" required="true" dictname="STD_ZB_INSU_TYPE" />
			<emp:textarea id="MortGuarantyInsurInfo.other_memo" label="其他说明" maxlength="600" required="false" colSpan="2" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

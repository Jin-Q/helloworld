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
		var url = '<emp:url action="queryCusTrusteeInfoList.do"/>?&menuId=custrusteeinfo';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusTrusteeInfoGroup" title="委托托管信息" maxColumn="2">
		<emp:text id="CusTrusteeInfo.serno" label="业务流水号" maxlength="40" required="true" colSpan="2" readonly="true"/>
		<emp:select id="CusTrusteeInfo.consignor_type" label="委托类别" required="true" dictname="STD_CUS_CONSIG_TYPE" readonly="true"/>
		<emp:select id="CusTrusteeInfo.is_provid_accredit" label="是否提供书面授权书" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
		<emp:text id="CusTrusteeInfo.consignor_br_id_displayname" label="委托机构" required="true" readonly="true"/>
		<emp:text id="CusTrusteeInfo.consignor_id_displayname" label="委托人" required="true" readonly="true"/>
		<emp:text id="CusTrusteeInfo.trustee_br_id_displayname" label="托管机构" required="true" readonly="true"/>
		<emp:text id="CusTrusteeInfo.trustee_id_displayname" label="托管人" required="true" readonly="true"/>
		<emp:textarea id="CusTrusteeInfo.trustee_detail" label="托管详情" maxlength="250" required="true" colSpan="2" readonly="true"/>
			
		<emp:text id="CusTrusteeInfo.consignor_id" label="委托人"   required="true" hidden="true"/>
		<emp:text id="CusTrusteeInfo.consignor_br_id" label="委托机构" required="true" hidden="true"/>
		<emp:text id="CusTrusteeInfo.trustee_id" label="托管人" required="true" hidden="true"/>
		<emp:text id="CusTrusteeInfo.trustee_br_id" label="托管机构" required="true" hidden="true"/>
	</emp:gridLayout>
	<emp:gridLayout id="CusTrusteeInfoGroup2" title="登记信息" maxColumn="2">
		<emp:date id="CusTrusteeInfo.trustee_date" label="托管日期" readonly="true"/>
		<emp:date id="CusTrusteeInfo.retract_date" label="回收日期" readonly="true"/>
		<emp:text id="CusTrusteeInfo.input_id_displayname" label="登记人"  readonly="true"/>
		<emp:text id="CusTrusteeInfo.input_br_id_displayname" label="登记机构" readonly="true"/>
		<emp:text id="CusTrusteeInfo.input_id" label="登记人" maxlength="20" hidden="true" />
		<emp:text id="CusTrusteeInfo.input_br_id" label="登记机构" maxlength="20" hidden="true" />
		<emp:date id="CusTrusteeInfo.input_date" label="登记日期" readonly="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>

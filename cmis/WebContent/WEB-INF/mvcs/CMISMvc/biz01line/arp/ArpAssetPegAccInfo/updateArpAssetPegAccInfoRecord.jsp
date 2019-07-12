<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateArpAssetPegAccInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpAssetPegAccInfoGroup" maxColumn="2" title="资产转固信息">
			<emp:text id="ArpAssetPegAccInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="ArpAssetPegAccInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpAssetPegAccInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetPegAccInfo.guaranty_name" label="抵债资产名称" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetPegAccInfo.guaranty_type_displayname" label="抵债资产类型" required="true" readonly="true" />
			<emp:text id="ArpAssetPegAccInfo.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" defvalue="${context.debt_in_amt}" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpAssetPegAccInfo.to_prop_value" label="转固价值" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpAssetPegAccInfo.eval_amt" label="评估金额" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpAssetPegAccInfo.disp_resn" label="处置理由" maxlength="200" required="false" colSpan="2" />
			<emp:date id="ArpAssetPegAccInfo.asgn_date" label="入账日期" required="false" />
			<emp:textarea id="ArpAssetPegAccInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetPegAccInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" readonly="true" defvalue="00"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

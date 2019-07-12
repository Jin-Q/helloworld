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
	
	<emp:form id="submitForm" action="updateArpAssetSaleAccInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpAssetSaleAccInfoGroup" maxColumn="2" title="资产出售信息">
			<emp:text id="ArpAssetSaleAccInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="ArpAssetSaleAccInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpAssetSaleAccInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetSaleAccInfo.guaranty_name" label="抵债资产名称" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetSaleAccInfo.guaranty_type_displayname" label="抵债资产类型" required="true" readonly="true" />
			<emp:text id="ArpAssetSaleAccInfo.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" defvalue="${context.debt_in_amt}" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpAssetSaleAccInfo.eval_amt" label="评估金额" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpAssetSaleAccInfo.sale_amt" label="出售价格" maxlength="16" required="true" dataType="Currency" />
			<emp:date id="ArpAssetSaleAccInfo.sale_date" label="出售日期" required="true" />
			<emp:select id="ArpAssetSaleAccInfo.sale_mode" label="出售方式" required="true" dictname="STD_ZB_SALE_TYPE" />
			<emp:textarea id="ArpAssetSaleAccInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetSaleAccInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" defvalue="00" readonly="true"/>
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

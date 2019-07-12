<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addArpAssetRentAccInfoRecord.do" method="POST">
		
		<emp:gridLayout id="ArpAssetRentAccInfoGroup" title="资产出租信息(台账)" maxColumn="2">
			<emp:text id="ArpAssetRentAccInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="ArpAssetRentAccInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" />
			<emp:text id="ArpAssetRentAccInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" />
			<emp:text id="ArpAssetRentAccInfo.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpAssetRentAccInfo.renta_paid_mode" label="租金收缴方式" maxlength="5" required="false" />
			<emp:text id="ArpAssetRentAccInfo.rent_amt" label="租金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpAssetRentAccInfo.mort_amt" label="押金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpAssetRentAccInfo.lessee" label="承租人" maxlength="40" required="false" />
			<emp:text id="ArpAssetRentAccInfo.lessee_phone" label="承租人电话" maxlength="40" required="false" />
			<emp:text id="ArpAssetRentAccInfo.lessee_addr" label="承租人地址" maxlength="40" required="false" />
			<emp:select id="ArpAssetRentAccInfo.lessee_cert_type" label="承租人证件类型" required="false" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="ArpAssetRentAccInfo.lessee_cert_no" label="承租人证件号码" maxlength="40" required="false" />
			<emp:date id="ArpAssetRentAccInfo.lease_start_date" label="租约起始日期" required="false" />
			<emp:date id="ArpAssetRentAccInfo.lease_end_date" label="租约到期日期" required="false" />
			<emp:textarea id="ArpAssetRentAccInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetRentAccInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" />
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


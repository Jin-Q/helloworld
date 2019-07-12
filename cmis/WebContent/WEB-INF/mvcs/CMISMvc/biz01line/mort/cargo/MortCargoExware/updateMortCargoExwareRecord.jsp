<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateMortCargoExwareRecord.do" method="POST">
		<emp:gridLayout id="MortCargoExwareGroup" maxColumn="2" title="货物出库管理">
			<emp:text id="MortCargoExware.serno" label="业务编号" maxlength="60" required="true" readonly="true" />
			<emp:text id="MortCargoExware.cus_id" label="客户码" maxlength="40" required="true" />
			<emp:text id="MortCargoExware.guaranty_no" label="押品编号" maxlength="40" required="true" />
			<emp:text id="MortCargoExware.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" />
			<emp:text id="MortCargoExware.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortCargoExware.this_exware_total" label="此次出库总价值" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortCargoExware.exware_total" label="出库后总价值" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="MortCargoExware.tally_date" label="记账日期" required="false" />
			<emp:textarea id="MortCargoExware.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="MortCargoExware.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" />
			<emp:text id="MortCargoExware.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="MortCargoExware.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:date id="MortCargoExware.input_date" label="登记日期" required="false" />
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

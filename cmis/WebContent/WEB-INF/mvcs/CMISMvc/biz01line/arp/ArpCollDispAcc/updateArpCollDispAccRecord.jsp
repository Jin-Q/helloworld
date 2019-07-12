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
	
	<emp:form id="submitForm" action="updateArpCollDispAccRecord.do" method="POST">
		<emp:gridLayout id="ArpCollDispAccGroup" maxColumn="2" title="抵债物处置台账">
			<emp:text id="ArpCollDispAcc.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="ArpCollDispAcc.asset_disp_no" label="资产处置编号" maxlength="40" required="true" readonly="true" />
			<emp:date id="ArpCollDispAcc.app_date" label="申请日期" required="false" />
			<emp:date id="ArpCollDispAcc.end_date" label="办结日期" required="false" />
			<emp:text id="ArpCollDispAcc.guaranty_no" label="抵债资产编号" maxlength="40" required="true" />
			<emp:select id="ArpCollDispAcc.asset_disp_mode" label="资产处置方式" required="false" dictname="STD_ASSET_DISP_MODEL" />
			<emp:text id="ArpCollDispAcc.fore_disp_expense" label="预计处置费用" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpCollDispAcc.disp_amt" label="处置金额" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpCollDispAcc.disp_memo" label="处置说明" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="ArpCollDispAcc.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="ArpCollDispAcc.manager_id" label="责任人" maxlength="20" required="false" />
			<emp:text id="ArpCollDispAcc.manager_br_id" label="责任机构" maxlength="20" required="false" />
			<emp:text id="ArpCollDispAcc.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="ArpCollDispAcc.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:date id="ArpCollDispAcc.input_date" label="登记日期" required="false" />
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

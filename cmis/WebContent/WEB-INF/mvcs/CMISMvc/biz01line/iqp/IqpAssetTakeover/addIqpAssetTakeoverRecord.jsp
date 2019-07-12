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
	
	<emp:form id="submitForm" action="addIqpAssetTakeoverRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetTakeoverGroup" title="资产转让" maxColumn="2">
			<emp:text id="IqpAssetTakeover.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetTakeover.asset_no" label="资产包编号" maxlength="40" required="false" />
			<emp:text id="IqpAssetTakeover.prd_id" label="产品编码" maxlength="10" required="false" />
			<emp:select id="IqpAssetTakeover.takeover_type" label="转让方式" required="false" />
			<emp:text id="IqpAssetTakeover.toorg_no" label="交易对手行号" maxlength="20" required="false" />
			<emp:text id="IqpAssetTakeover.toorg_name" label="交易对手行名" maxlength="100" required="false" />
			<emp:text id="IqpAssetTakeover.toorg_acct_name" label="交易对手户名" maxlength="80" required="false" />
			<emp:text id="IqpAssetTakeover.tooorg_org_no" label="交易对手开户行行号" maxlength="20" required="false" />
			<emp:text id="IqpAssetTakeover.tooorg_org_name" label="交易对手开户行行名" maxlength="100" required="false" />
			<emp:select id="IqpAssetTakeover.topp_addr" label="交易对手所在城市" required="false" />
			<emp:text id="IqpAssetTakeover.this_acct" label="本行账户" maxlength="5" required="false" />
			<emp:text id="IqpAssetTakeover.this_acct_name" label="本行账户名" maxlength="80" required="false" />
			<emp:text id="IqpAssetTakeover.acctsvcr_no" label="开户行行号" maxlength="20" required="false" />
			<emp:text id="IqpAssetTakeover.acctsvcr_name" label="开户行行名" maxlength="100" required="false" />
			<emp:select id="IqpAssetTakeover.takeover_curr" label="转让币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetTakeover.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpAssetTakeover.takeover_amt" label="转让金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpAssetTakeover.takeover_rate" label="转让利息" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpAssetTakeover.int_type" label="收息方式" required="false" />
			<emp:date id="IqpAssetTakeover.takeover_date" label="转让日期" required="false" />
			<emp:text id="IqpAssetTakeover.csgn_rate" label="委托费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpAssetTakeover.takeover_times" label="转让笔数" maxlength="38" required="false" />
			<emp:select id="IqpAssetTakeover.risk_is_trans" label="风险是否转移" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="IqpAssetTakeover.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:pop id="IqpAssetTakeover.manager_br_id" label="管理机构" url="null" required="false" />
			<emp:text id="IqpAssetTakeover.input_id" label="登记人" maxlength="40" required="false" />
			<emp:text id="IqpAssetTakeover.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:date id="IqpAssetTakeover.input_date" label="登记日期" required="false" />
			<emp:select id="IqpAssetTakeover.approve_status" label="申请状态" required="false" />
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


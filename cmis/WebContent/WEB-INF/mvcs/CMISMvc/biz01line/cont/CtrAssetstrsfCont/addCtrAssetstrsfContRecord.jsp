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
	
	<emp:form id="submitForm" action="addCtrAssetstrsfContRecord.do" method="POST">
		
		<emp:gridLayout id="CtrAssetstrsfContGroup" title="资产转受让合同" maxColumn="2">
			<emp:text id="CtrAssetstrsfCont.serno" label="业务编号" maxlength="40" required="false" />
			<emp:text id="CtrAssetstrsfCont.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="CtrAssetstrsfCont.asset_no" label="资产包编号" maxlength="40" required="false" />
			<emp:text id="CtrAssetstrsfCont.prd_id" label="产品编码" maxlength="6" required="false" />
			<emp:select id="CtrAssetstrsfCont.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE" />
			<emp:pop id="CtrAssetstrsfCont.toorg_no" label="交易对手行号" url="null" required="false" />
			<emp:text id="CtrAssetstrsfCont.toorg_name" label="交易对手行名" maxlength="100" required="false" />
			<emp:text id="CtrAssetstrsfCont.topp_acct_name" label="交易对手户名" maxlength="40" required="false" />
			<emp:pop id="CtrAssetstrsfCont.tooorg_no" label="交易对手开户行行号" url="null" required="false" />
			<emp:text id="CtrAssetstrsfCont.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" />
			<emp:text id="CtrAssetstrsfCont.tooorg_city" label="交易对手所在城市" maxlength="50" required="false" />
			<emp:text id="CtrAssetstrsfCont.this_acct_no" label="本行账户" maxlength="40" required="false" />
			<emp:text id="CtrAssetstrsfCont.this_acct_name" label="本行账户名" maxlength="100" required="false" />
			<emp:pop id="CtrAssetstrsfCont.acctsvcr_no" label="开户行行号" url="null" required="false" />
			<emp:text id="CtrAssetstrsfCont.acctsvcr_name" label="开户行行名" maxlength="100" required="false" />
			<emp:select id="CtrAssetstrsfCont.acct_curr" label="转让币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CtrAssetstrsfCont.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrAssetstrsfCont.takeover_total_amt" label="转让金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CtrAssetstrsfCont.takeover_int" label="转让利息" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="CtrAssetstrsfCont.interest_type" label="收息方式" required="false" />
			<emp:date id="CtrAssetstrsfCont.takeover_date" label="转让日期" required="false" />
			<emp:text id="CtrAssetstrsfCont.trust_rate" label="委托费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="CtrAssetstrsfCont.takeover_qnt" label="转让笔数" maxlength="38" required="false" dataType="Int" />
			<emp:select id="CtrAssetstrsfCont.is_risk_takeover" label="风险是否转移" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="CtrAssetstrsfCont.memo" label="备注" maxlength="250" required="false" />
			<emp:pop id="CtrAssetstrsfCont.manager_br_id" label="管理机构" url="null" required="false" />
			<emp:pop id="CtrAssetstrsfCont.input_id" label="登记人" url="null" required="false" />
			<emp:pop id="CtrAssetstrsfCont.input_br_id" label="登记机构" url="null" required="false" />
			<emp:date id="CtrAssetstrsfCont.input_date" label="登记日期" required="false" />
			<emp:select id="CtrAssetstrsfCont.cont_status" label="合同状态" required="false" dictname="STD_ZB_CTRLOANCONT_TYPE" />
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


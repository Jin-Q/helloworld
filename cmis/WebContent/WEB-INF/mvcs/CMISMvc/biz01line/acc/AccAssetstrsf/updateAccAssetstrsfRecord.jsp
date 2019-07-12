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
	
	<emp:form id="submitForm" action="updateAccAssetstrsfRecord.do" method="POST">
		<emp:gridLayout id="AccAssetstrsfGroup" maxColumn="2" title="资产转让台账">
			<emp:text id="AccAssetstrsf.bill_no" label="借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="AccAssetstrsf.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.asset_no" label="资产包编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.takeover_type" label="转让方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.asset_type" label="资产类型" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.is_risk_takeover" label="风险是否转移" maxlength="1" required="false" />
			<emp:text id="AccAssetstrsf.takeover_date" label="转让日期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.cus_name" label="客户名称" maxlength="100" required="false" />
			<emp:text id="AccAssetstrsf.ori_cont_no" label="原合同编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.ori_bill_no" label="原借据编号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.guar_type" label="担保方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.guar_desc" label="担保品说明" maxlength="250" required="false" />
			<emp:text id="AccAssetstrsf.loan_amt" label="贷款金额" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.loan_bal" label="贷款余额" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.latest_repay" label="最近还款日" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.loan_start_date" label="原借款起始日期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.loan_end_date" label="原借款到期日期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.ir_accord_type" label="利率依据方式" maxlength="2" required="false" />
			<emp:text id="AccAssetstrsf.ir_type" label="利率种类" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.ir_adjust_type" label="利率调整方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.ruling_ir" label="基准利率（年）" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.ir_float_type" label="正常利率浮动方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.ir_float_rate" label="利率浮动比" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.ir_float_point" label="利率浮动点数" maxlength="38" required="false" />
			<emp:text id="AccAssetstrsf.reality_ir_y" label="正常利率（年）" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.overdue_float_type" label="逾期利率浮动方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.overdue_rate" label="逾期利率浮动比" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.overdue_point" label="逾期利率浮动点" maxlength="38" required="false" />
			<emp:text id="AccAssetstrsf.overdue_rate_y" label="逾期利率（年）" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.default_float_type" label="违约利率浮动方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.default_rate" label="违约利率浮动比" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.default_point" label="违约利率浮动点" maxlength="38" required="false" />
			<emp:text id="AccAssetstrsf.default_rate_y" label="违约利率（年）" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.pad_rate_y" label="垫款利率（年）" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.repay_type" label="还款方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.repay_term" label="还款间隔周期" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.repay_space" label="还款间隔" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.ei_type" label="结息方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.five_class" label="五级分类" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.takeover_amt" label="转让金额" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.takeover_rate" label="转让利率" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.takeover_int" label="转让利息" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.afee_type" label="安排计费方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.takeover_frate" label="转让费率" maxlength="10" required="false" />
			<emp:text id="AccAssetstrsf.afee" label="安排费" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.afee_pay_type" label="安排费支付方式" maxlength="5" required="false" />
			<emp:text id="AccAssetstrsf.mfee" label="管理费" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.int" label="应计利息" maxlength="16" required="false" />
			<emp:text id="AccAssetstrsf.agent_asset_acct" label="结算账号" maxlength="40" required="false" />
			<emp:text id="AccAssetstrsf.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="AccAssetstrsf.fina_br_id" label="账务机构" maxlength="20" required="false" />
			<emp:text id="AccAssetstrsf.acc_status" label="台账状态" maxlength="5" required="false" />
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

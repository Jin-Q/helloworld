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

<body  class="page_content">
	
	<emp:form id="submitForm" action="addCtrLoanContRecord.do" method="POST">
		<emp:gridLayout id="CtrLoanContGroup" title="合同主表" maxColumn="2">
		<emp:text id="CtrLoanCont.cont_no" label="合同编号" maxlength="40" required="true" />
		<emp:text id="CtrLoanCont.cn_cont_no" label="中文合同编号" maxlength="100" required="false" />
		<emp:text id="CtrLoanCont.cus_id" label="客户码" maxlength="40" required="false" />
		<emp:select id="CtrLoanCont.biz_type" label="业务模式" required="false" dictname="STD_BIZ_TYPE" />
		<emp:pop id="CtrLoanCont.belong_net" label="所属网络" url="null" required="false" />
		<emp:select id="CtrLoanCont.rent_type" label="租赁模式" required="false" dictname="STD_RENT_TYPE" />
		<emp:text id="CtrLoanCont.prd_id" label="产品编号" maxlength="6" required="false" />
		<emp:text id="CtrLoanCont.prd_details" label="产品细分" maxlength="10" required="false" />
		<emp:select id="CtrLoanCont.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" />
		<emp:text id="CtrLoanCont.promissory_note" label="承诺函" maxlength="80" required="false" />
		<emp:select id="CtrLoanCont.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:select id="CtrLoanCont.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" />
		<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整  -->
		<emp:text id="CtrLoanCont.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" readonly="true" />
		<emp:text id="CtrLoanCont.trust_company" label="信托公司" maxlength="100" required="false" />
		<emp:select id="CtrLoanCont.cont_cur_type" label="合同币种" required="false" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="CtrLoanCont.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" />
		<emp:date id="CtrLoanCont.cont_start_date" label="合同起始日期" required="false" />
		<emp:date id="CtrLoanCont.cont_end_date" label="合同到期日期" required="false" />
		<emp:text id="CtrLoanCont.exchange_rate" label="汇率" maxlength="10" required="false" dataType="Rate" />
		<emp:text id="CtrLoanCont.security_rate" label="保证金比例" maxlength="10" required="false" dataType="Percent" />
		<emp:text id="CtrLoanCont.security_amt" label="保证金金额" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="CtrLoanCont.same_security_amt" label="视同保证金" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="CtrLoanCont.ass_sec_multiple" label="担保放大倍数" maxlength="10" required="false" dataType="Double" />
		<emp:select id="CtrLoanCont.limit_ind" label="授信额度使用标志" required="false" dictname="STD_LIMIT_IND" />
		<emp:text id="CtrLoanCont.limit_acc_no" label="授信台账编号" maxlength="40" required="false" />
		<emp:text id="CtrLoanCont.limit_credit_no" label="第三方授信编号" maxlength="40" required="false" />
		<emp:textarea id="CtrLoanCont.remarks" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CtrLoanCont.cont_status" label="合同状态" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.manager_br_id" label="管理机构" maxlength="20" required="false" />
		<emp:text id="CtrLoanCont.input_id" label="登记人" maxlength="20" required="false" />
		<emp:text id="CtrLoanCont.input_br_id" label="登记机构" maxlength="20" required="false" />
		<emp:date id="CtrLoanCont.input_date" label="登记日期" required="false" />
		<emp:text id="CtrLoanCont.serno" label="业务编号" maxlength="40" required="true" />
		<emp:select id="CtrLoanCont.is_trust_loan" label="是否信托贷款" required="true" dictname="STD_ZX_YES_NO" />
		<emp:text id="CtrLoanCont.cont_amt" label="合同金额" maxlength="18" required="true" dataType="Currency" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.is_close_loan" label="是否无间贷" hidden="true" maxlength="1" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.repay_bill" label="偿还借据" maxlength="40" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_form" label="贷款形式" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_nature" label="贷款性质" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.is_conf_pay_type" label="是否确定支付方式" maxlength="1" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.pay_type" label="支付方式" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_accord_type" label="利率依据方式" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_type" label="利率种类" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_adjust_type" label="利率调整方式" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ruling_ir" label="基准利率（年）" maxlength="16" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_float_type" label="利率浮动方式" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_float_rate" label="利率浮动比" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.default_rate" label="违约利率浮动比" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.overdue_rate" label="逾期利率浮动比" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ir_float_point" label="利率浮动点数" maxlength="38" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.overdue_point" label="逾期利率浮动点" maxlength="38" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.default_point" label="违约利率浮动点" maxlength="38" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.reality_ir_y" label="执行利率（年）" maxlength="16" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.fir_repay_date" label="首次还款日" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.repay_type" label="还款方式" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.interest_term" label="计息周期" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.five_classfiy" label="五级分类" maxlength="10" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.spe_loan_type" label="特殊贷款类型" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.limit_useed_type" label="额度占用类型" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_type" label="贷款种类" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.estate_adjust_type" label="产业结构调整类型" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.com_up_indtify" label="工业转型升级标识" maxlength="40" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.strategy_new_type" label="战略新兴产业类型" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.new_prd_loan" label="新兴产业贷款" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.green_prd" label="绿色产品" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.agriculture_type" label="涉农贷款类型" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_direction" label="贷款投向" maxlength="100" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong1" label="贷款归属1" maxlength="100" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong2" label="贷款归属2" maxlength="100" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong3" label="贷款归属3" maxlength="100" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.ensure_project_loan" label="保障性安居工程贷款" maxlength="5" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.loan_use_type" label="借款用途" maxlength="250" required="false" />
		<emp:text id="CtrLoanCont.CtrLoanContSub.repay_src_des" label="还款来源" maxlength="250" required="false" />
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

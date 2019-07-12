<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doQuery(){
	var form = document.getElementById('queryForm');
	IqpLoanApp._toForm(form);
	IqpLoanAppList._obj.ajaxQuery(null,form);
};

   function doReset(){
	  page.dataGroups.IqpLoanAppGroup.reset();
   };
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <div  class='emp_gridlayout_title'>还款方案试算结果</div> 
	<emp:table icollName="RepayPlanList" pageMode="true" url="pageCreateRepayPlanOp.do" reqParams="serno=${context.iqpLoanApp.serno}&apply_amount=${context.iqpLoanApp.apply_amount}&repay_date=${context.iqpLoanApp.repay_date}&apply_date=${context.iqpLoanApp.apply_date}&CONTRACT_EXPIRY_DATE=${context.iqpLoanApp.CONTRACT_EXPIRY_DATE}&loan_type=${context.iqpLoanApp.loan_type}&reality_ir_y=${context.iqpLoanApp.reality_ir_y}&repay_term=${context.iqpLoanApp.repay_term}&repay_space=${context.iqpLoanApp.repay_space}&repay_type=${context.iqpLoanApp.repay_type}&repay_mode_type=${context.iqpLoanApp.repay_mode_type}&is_term=${context.iqpLoanApp.is_term}&interest_term=${context.interest_term}&term=${context.term}&termType=${context.termType}&prd_id=${context.prd_id}">
		<emp:text id="TrmTms" label="期次" />
		<emp:text id="NxtDealDt" label="下一处理日期" />
		<emp:text id="PnpAmt" label="本金金额" dataType="Currency"/>
		<emp:text id="IntAmt" label="利息金额" dataType="Currency"/>
		<emp:text id="RpyblPnyInt" label="应还罚息" dataType="Currency"/>
		<emp:text id="RpyblCmpdInt" label="应还复利" dataType="Currency"/>
		<emp:text id="PyblPnyIntCmpdInt" label="应付罚息的复利" dataType="Currency"/>
		<emp:text id="RpyblCmpdIntCmpdInt" label="应还复利的复利" dataType="Currency"/>
		<emp:text id="TotAmt" label="合计金额/总金额" dataType="Currency"/>
		<emp:text id="RcvblPnpAmt" label="应收未收本金" dataType="Currency"/>
		
		<%-- <emp:text id="PERIOD_NO" label="期号" />
		<emp:text id="OUGHT_REPAY_CORPUS" label="应还款本金"  dataType="Currency"/>
		<emp:text id="OUGHT_TERM_AMT" label="应还款分期金额"  dataType="Currency"/>
		<emp:text id="OUGHT_REPAY_INT" label="应还款利息"  dataType="Currency"/>
		<emp:text id="INT_RATE" label="利率"  dataType="Rate"/>
		<emp:text id="LOAN_OD_ACT_RATE" label="罚息执行利率"  dataType="Rate" hidden="true"/>
		<emp:text id="REPAY_DATE" label="还款日期" /> --%>
	</emp:table>  
	
	   <emp:text id="iqpLoanApp.serno" label="业务编号" hidden="true"/>
	   <emp:text id="iqpLoanApp.apply_amount" label="申请金额" hidden="true"/>
	   <emp:text id="iqpLoanApp.repay_date" label="还款日" hidden="true"/>
	   <emp:text id="iqpLoanApp.apply_date" label="申请日期" hidden="true"/>
	   <emp:text id="iqpLoanApp.CONTRACT_EXPIRY_DATE" label="合同到期日" hidden="true"/>
	   <emp:text id="iqpLoanApp.loan_type" label="贷款种类" hidden="true"/>
	   <emp:text id="iqpLoanApp.reality_ir_y" label="执行利率（年）" hidden="true"/>
	   <emp:text id="iqpLoanApp.repay_term" label="还款间隔周期" hidden="true"/>
	   <emp:text id="iqpLoanApp.repay_space" label="还款间隔" hidden="true"/>
	   <emp:text id="iqpLoanApp.repay_type" label="还款方式" hidden="true"/>
	   <emp:text id="iqpLoanApp.repay_mode_type" label="还款方式种类" hidden="true"/>
	   <emp:text id="iqpLoanApp.is_term" label="是否期供" hidden="true"/>
</body>
</html>
</emp:page>
    
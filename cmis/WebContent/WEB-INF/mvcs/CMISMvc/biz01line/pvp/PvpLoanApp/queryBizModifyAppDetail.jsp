<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String op = "";
    if(context.containsKey("op")){
    	op = (String)context.getDataValue("op");
    }
    String prd_id ="";
    if(context.containsKey("prd_id")){
    	prd_id = (String)context.getDataValue("prd_id");
    }
    String wf_flag="";
    if(context.containsKey("wf_flag")){
    	wf_flag = (String)context.getDataValue("wf_flag");
    }
    String repay_type ="";
    if(context.containsKey("repay_type")){
    	repay_type = (String)context.getDataValue("repay_type");
    }
    String pay_type ="";
    if(context.containsKey("pay_type")){
    	pay_type = (String)context.getDataValue("pay_type");
    }
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>	
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="jsBizModify.jsp" flush="true" />
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
function doOnLoad(){
	var repay_mode_type = CtrLoanContTmp.CtrLoanContSubTmp.repay_mode_type._getValue();
	var pay_type = CtrLoanContTmp.CtrLoanContSubTmp.is_conf_pay_type._getValue();
	var prd_id =CtrLoanContTmp.prd_id._getValue();
	//getHLByCurr();//--加载汇率--
	getRulMounth();//--加载利率--
	changeRmbAmt();//--加载折算金额--
	ir_accord_typeChange("init");
	changeIrFloatType();//根据利率浮动方式同比调整显示
	changeOverdueFloatType();
	changeDefaultFloatType();
	hiddenIr();
	hiddenBWIr();
	reality_ir_yChange();//通过年利率计算月利率
	setRepayTerm();
	setRepayType();
	getHLByCurr4Security();
	caculateContEndDate();
	//checkFromRepayType(repay_mode_type);
	checkRepayDate(repay_mode_type);
	readonly4RateInfo();
	readonly4ReturnType();
	checkIsDelay();
	is_off_busi();//表外业务字段隐藏js
    is_show_pad_rate();//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示
	//加载页面支付方式的判断
	if(pay_type==1){
		CtrLoanContTmp.CtrLoanContSubTmp.pay_type._obj._renderHidden(false);
		CtrLoanContTmp.CtrLoanContSubTmp.pay_type._obj._renderRequired(true);
	}
	CtrLoanContTmp.CtrLoanContSubTmp.repay_type_displayname._obj.addTheButton("repay_type","生成还款方案",getRepayForm);
	if(prd_id == "200024"){
    	CtrLoanContTmp.CtrLoanContSubTmp.actp_status._obj._renderHidden(false);
    }
	if(prd_id!="200024" && prd_id!="400020" && prd_id!="400021"){
		document.getElementById('amtInfo').style.display="none";
	}
	if('${context.belg_line}' == "BL100" ||'${context.belg_line}' == "BL200" || "${context.belg_line}" == "BL300"){
		CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan._obj._renderHidden(true);
	}else{
		CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan._obj._renderHidden(true);
		CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan._obj._renderRequired(false);
	}
	var sfgjd = CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan._getValue();//是否无间贷 1:是 2：否
	var dkxs = CtrLoanContTmp.CtrLoanContSubTmp.loan_form._getValue();//贷款形式
	if(sfgjd == "1" || dkxs == "3"){
		CtrLoanContTmp.CtrLoanContSubTmp.repay_bill._obj._renderHidden(false);
		CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan._obj._renderHidden(true);   
	}else{
		CtrLoanContTmp.CtrLoanContSubTmp.repay_bill._obj._renderHidden(true);
	}
};

function doReturn(){
	var op='<%=op%>';
	if(op!="his"){
		var url = '<emp:url action="queryBizModifyAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}else{
		var url = '<emp:url action="queryBizModifyAppHisList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
};

</script>
<jsp:include page="/include.jsp" flush="true"/>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="#" method="POST">
		<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  		<emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
	  		    <emp:gridLayout id="PvpBizModifyRelGroup" maxColumn="2" title="打回业务修改申请信息">
	  		    	<emp:text id="PvpBizModifyRel.modify_rel_serno" label="打回业务修改申请流水号" required="false" readonly="true" />
	  		    	<emp:select id="PvpBizModifyRel.biz_cate" label="业务申请类型" dictname="ZB_BIZ_CATE" required="false" readonly="true" />
	  		    </emp:gridLayout>
	  			<emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="合同基本信息">
			  		<emp:text id="CtrLoanContTmp.serno" label="业务编号" maxlength="40" required="true" readonly="false" hidden="true"/>
					<emp:text id="CtrLoanContTmp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"  />
					<emp:text id="CtrLoanContTmp.cn_cont_no" label="中文合同编号" maxlength="100" required="true" />
					<emp:text id="CtrLoanContTmp.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
					<emp:text id="CtrLoanContTmp.prd_id_displayname" label="产品名称"  required="true" readonly="true"/>
					<emp:text id="CtrLoanContTmp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" colSpan="2"/>
					<emp:text id="CtrLoanContTmp.cus_id_displayname" label="客户名称"  required="false" cssElementClass="emp_field_text_long_readonly" readonly="true" colSpan="2"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.loan_form" label="贷款形式" required="true" dictname="STD_LOAN_FORM" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.loan_nature" label="贷款性质" required="true" dictname="STD_LOAN_NATYRE" readonly="true"/>
					<emp:select id="CtrLoanContTmp.assure_main" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>
					<emp:select id="CtrLoanContTmp.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_close_loan" label="是否无间贷" hidden="true" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>
					<emp:pop id="CtrLoanContTmp.CtrLoanContSubTmp.repay_bill" label="偿还借据" url="queryAccPop.do?returnMethod=getBill&cus_id=${context.CtrLoanContTmp.cus_id}"  required="false" readonly="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
					<emp:select id="CtrLoanContTmp.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>			
					<emp:text id="CtrLoanContTmp.promissory_note" label="承诺函" maxlength="80" required="false"  hidden="true" readonly="true" />  
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_collect_stamp" label="是否收取印花税" required="true" dictname="STD_ZX_YES_NO" readonly="true" defvalue="1" hidden="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.stamp_collect_mode" label="印花税收取方式" required="true" readonly="true" dictname="STD_ZB_STAMP_MODE" hidden="true"/>
					<emp:select id="CtrLoanContTmp.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" readonly="true" hidden="true"/>						
					<emp:text id="CtrLoanContTmp.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" readonly="true" />
					<emp:text id="CtrLoanContTmp.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" readonly="true" />	
					<emp:select id="CtrLoanContTmp.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true"  readonly="true" dictname="STD_ZX_YES_NO" />
					<emp:pop id="CtrLoanContTmp.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true" readonly="true"/>
				    <emp:select id="CtrLoanContTmp.biz_type" label="业务模式" hidden="true" required="false" readonly="true" dictname="STD_BIZ_TYPE"/>
					<emp:select id="CtrLoanContTmp.rent_type" label="租赁模式" required="false" dictname="STD_RENT_TYPE" readonly="true" hidden="true"/>
					<emp:pop id="CtrLoanContTmp.belong_net" label="所属网络" url="" returnMethod="" required="false" readonly="true" hidden="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.collateral_type" label="抵质押类型" dictname="STD_COLLATERAL_TYPE" required="false" hidden="true" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.actp_status" label="税票提供状态" dictname="STD_ACTP_STATUS" required="true" defvalue="1" hidden="true" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_freeze" label="是否冻结" dictname="STD_ZX_YES_NO" required="false" hidden="true" readonly="true"/>
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.loan_type" label="贷款种类" hidden="true"/>
	  			</emp:gridLayout>
	  			<div id="payInfo" >
	  			<emp:gridLayout id="" maxColumn="2" title="支付信息">   		
			    	<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_conf_pay_type" label="是否确定支付方式" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.pay_type" label="支付方式" required="true" dictname="STD_IQP_PAY_TYPE"/>
			  	</emp:gridLayout>
			  	</div>
			  	<div id="amtInfo" > 
	  			 <emp:gridLayout id="" maxColumn="2" title="金额信息">
				    <emp:select id="CtrLoanContTmp.cont_cur_type" label="合同币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
				    <emp:text id="CtrLoanContTmp.exchange_rate" label="汇率" maxlength="10" required="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				    <emp:text id="CtrLoanContTmp.cont_amt" label="合同金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				    <emp:text id="CtrLoanContTmp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				    
				    <emp:select id="CtrLoanContTmp.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
				   	<emp:text id="CtrLoanContTmp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
				   	<emp:text id="CtrLoanContTmp.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				   	<emp:text id="CtrLoanContTmp.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				    
				    <emp:text id="CtrLoanContTmp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="CtrLoanContTmp.ass_sec_multiple" label="担保放大倍数" hidden="true" maxlength="10" required="false" dataType="Double" readonly="true" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="CtrLoanContTmp.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="CtrLoanContTmp.risk_open_amt" label="风险敞口金额（元）" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				    <emp:text id="CtrLoanContTmp.risk_open_rate" label="敞口比率" maxlength="10" required="true" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
				    <emp:text id="CtrLoanContTmp.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="true"  hidden="true"/>			
				</emp:gridLayout>
				</div>
			     <emp:gridLayout id="" maxColumn="2" title="期限信息">
				    <emp:date id="CtrLoanContTmp.cont_start_date" label="合同起始日期" required="true" readonly="true" />
				    <emp:date id="CtrLoanContTmp.cont_end_date" label="合同到期日期" required="true"  readonly="true"/>
				    <emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.term_type" label="期限类型" required="true"  dictname="STD_ZB_TERM_TYPE" />					
				    <emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.cont_term" label="合同期限" required="true" onblur="caculateContEndDate()"/>					
				    <emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.overdrawn_term" label="透支期限" required="false" dataType="Int" onchange="checkOverDrawn()" hidden="true"/>					
				    <emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.overdrawn_type" label="透支类型" required="false" hidden="true" readonly="true" dictname="STD_ZB_TERM_TYPE" />		    
				    <emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_delay" label="是否节假日顺延" required="true"  readonly="true" dictname="STD_ZX_YES_NO"/>					
				    <emp:date id="CtrLoanContTmp.cancel_date" label="合同注销日期" required="false"  readonly="true" hidden="true"/>					
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="额度信息">
				    <emp:select id="CtrLoanContTmp.limit_ind" label="授信额度使用标志" required="true" dictname="STD_LIMIT_IND" colSpan="2" readonly="true" />
				    <emp:text id="CtrLoanContTmp.limit_acc_no" label="授信台账编号" maxlength="40" required="true" readonly="true"/>
					<emp:text id="CtrLoanContTmp.limit_credit_no" label="第三方授信编号" maxlength="40" required="false" readonly="true"/>
				</emp:gridLayout>
				<div id="rateInfo" >
				<emp:gridLayout id="" maxColumn="2" title="利率信息">
				    <emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.ir_accord_type" label="利率依据方式"  onchange="ir_accord_typeChange('change');" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.ir_type" label="利率种类" hidden="true" required="false" onchange="ir_typeChange();" dictname="STD_ZB_RATE_TYPE" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.ruling_ir" cssElementClass="emp_currency_text_readonly" label="基准利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate"/>
					<emp:text id="ruling_mounth" cssElementClass="emp_currency_text_readonly" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>  
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.pad_rate_y" cssElementClass="emp_currency_text_readonly" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2" readonly="false" required="false" dataType="Rate"/>  
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.ir_adjust_type" label="利率调整方式" hidden="true" required="false" colSpan="2" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
					
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.ir_next_adjust_term" cssElementClass="emp_currency_text_readonly" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
					<emp:date id="CtrLoanContTmp.CtrLoanContSubTmp.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
					
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.ir_float_rate" cssElementClass="emp_currency_text_readonly" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getRelYM();" required="false" dataType="Percent2" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.ir_float_point" cssElementClass="emp_currency_text_readonly" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />
					
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.reality_ir_y" cssElementClass="emp_currency_text_readonly" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange()" readonly="true" maxlength="16" required="false" dataType="Rate"/>
					<emp:text id="reality_mounth" label="执行利率(月)" cssElementClass="emp_currency_text_readonly" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.overdue_float_type" label="逾期利率浮动方式" hidden="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.overdue_rate" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Percent2" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.overdue_point" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.overdue_rate_y" cssElementClass="emp_currency_text_readonly" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate"/>
					
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.default_rate" cssElementClass="emp_currency_text_readonly" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Percent2" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.default_point" cssElementClass="emp_currency_text_readonly" label="违约利率浮动点数" maxlength="38" hidden="true" onchange="getDefaultRateY();" required="false" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.default_rate_y" cssElementClass="emp_currency_text_readonly" label="违约利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate"/>
					
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.ruling_ir_code" cssElementClass="emp_currency_text_readonly" label="基准利率代码" hidden="true" maxlength="40"  required="false"/>
				</emp:gridLayout>
				</div>
				<div id="returnType" >   
				<emp:gridLayout id="" maxColumn="2" title="还款方式信息">
				    <emp:pop id="CtrLoanContTmp.CtrLoanContSubTmp.repay_type_displayname" label="还款方式" url="queryPrdRepayModeList.do?returnMethod=getRepayType" required="true" />
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.interest_term" label="计息周期" required="true" dictname="STD_IQP_RATE_CYCLE" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.repay_term" label="还款间隔周期" required="true" dictname="STD_BACK_CYCLE" onchange="cleanSpace()" readonly="true"/>
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.repay_space" cssElementClass="emp_currency_text_readonly" label="还款间隔" maxlength="10" required="true" dataType="Int" onblur="checkTerm()"  readonly="true"/>
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.repay_date" label="还款日" required="true" readonly="true"/>
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.is_term" label="是否期供" required="true" readonly="true" dictname="STD_ZX_YES_NO" />
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.repay_type" label="还款方式" required="false" hidden="true"/>  
					<emp:date id="CtrLoanContTmp.CtrLoanContSubTmp.fir_repay_date" label="首次还款日" required="false" hidden="true"/>
					<emp:text id="CtrLoanContTmp.CtrLoanContSubTmp.repay_mode_type" label="还款方式类型" required="false" hidden="true"/>	
				</emp:gridLayout>
				<%-- <div  class='emp_gridlayout_title'>还款方式策略信息</div>
					<emp:table icollName="PrdRepayPlanList" pageMode="true" url="getPrdRepayPlanUpdatePage.do" reqParams="repay_mode_id=${context.CtrLoanContSub.repay_type}" >	
						<emp:text id="serno" label="编号" hidden="true"/>
						<emp:text id="exe_times" label="执行期数" />
						<emp:text id="repay_mode" label="还款方式" dictname="STD_ZB_RPYM_OPT"/> 
						<emp:text id="repay_type" label="还款类型" dictname="STD_ZB_SETL_TYPE"/>
						<emp:text id="rate_sprd" label="利差" dataType="Rate"/>    
						<emp:text id="rate_pefloat" label="利率浮动比例" dataType="Rate"/>  
						<emp:text id="rate_cal_basic" label="利率计算基础" dictname="STD_ZB_RATE_BASE"/>
						<emp:text id="int_cal_basic" label="利息计算基础" dictname="STD_ZB_INT_BASE"/>
						<emp:text id="repay_interval" label="还款间隔" />
						<emp:text id="repay_trem" label="还款间隔周期" dictname="STD_BACK_CYCLE"/> 
					</emp:table> --%>
				</div>
				<%if(!"200024".equals(prd_id) && !"400020".equals(prd_id) && !"400021".equals(prd_id)){ %>
				<emp:gridLayout id="" maxColumn="2" title="其他信息"> 
					<emp:select id="CtrLoanContTmp.CtrLoanContSubTmp.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
				</emp:gridLayout>
			<%} %> 
	  		</emp:tab>
			<%if("A001".equals(repay_type)){ %>
	  		<emp:tab id="IqpFreedomPayInfo" label="还款计划登记" url="queryIqpFreedomPayInfoList.do" reqParams="modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&serno=${context.CtrLoanContTmp.serno}&op=view&menuIdTab=queryIqpLoanApp&subMenuId=IqpFreedomPayInfo&&modiflg=yes" initial="true"/>
	  		<%} %>
	  		<%if("1".equals(pay_type)){ %>
	  		<emp:tab id="IqpCusAcct" label="账户信息" url="queryIqpCusAcctList.do" reqParams="modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&serno=${context.CtrLoanContTmp.serno}&is_agent_disc=null&is_close_loan=1&prd_id=${context.CtrLoanContTmp.prd_id}&menuIdTab=queryIqpLoanApp&subMenuId=queryIqpCusAcctList&op=view&modiflg=yes" initial="true"/>
	  		<%} %>
	  		<%if("200024".equals(prd_id)){ %>
				<emp:tab id="IqpAccpDetail" label="银行承兑汇票" url="getIqpAccAccpUpdatePage.do" reqParams="op=view&serno=${context.CtrLoanContTmp.serno}&cont=modify&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}" initial="true"/>
				<emp:tab id="PubBailInfo" label="保证金信息" url="queryPubBailInfo_jointList.do" reqParams="serno=${context.CtrLoanContTmp.serno}&cus_id=${context.CtrLoanContTmp.cus_id}&menuIdTab=queryIqpLoanApp&subMenuId=queryPubBailInfo_jointList&op=view&modiflg=yes&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}" initial="true"/>
				<emp:tab id="IqpAppendTermsList" label="附加条款" url="queryIqpAppendTermsList.do" reqParams="modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&op=view&serno=${context.CtrLoanContTmp.serno}&apply_cur_type=${context.CtrLoanContTmp.cont_cur_type}&apply_amount=${context.CtrLoanContTmp.cont_amt}&prd_id=${context.CtrLoanContTmp.prd_id}&menuIdTab=queryIqpLoanApp&subMenuId=queryIqpAppendTermsList&modiflg=yes&wf_flag=${context.wf_flag}" initial="true"/>
				<emp:tab id="IqpGuarList" label="担保信息" url="queryGrtLoanRGurList.do" reqParams="op=view&serno=${context.CtrLoanContTmp.serno}&cus_id=${context.CtrLoanContTmp.cus_id}&assure_main=${context.CtrLoanContTmp.assure_main}&limit_acc_no=${context.CtrLoanContTmp.limit_acc_no}&limit_credit_no=${context.CtrLoanContTmp.limit_credit_no}&limit_ind=${context.CtrLoanContTmp.limit_ind}&menuIdTab=queryIqpLoanApp&subMenuId=queryGrtLoanRGurList&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&modiflg=yes" initial="true"/>
			<%} %>
			<%if("400020".equals(prd_id)||"400021".equals(prd_id)){ %>
				<emp:tab id="PubBailInfo" label="保证金信息" url="queryPubBailInfo_jointList.do" reqParams="serno=${context.CtrLoanContTmp.serno}&cus_id=${context.CtrLoanContTmp.cus_id}&menuIdTab=queryIqpLoanApp&subMenuId=queryPubBailInfo_jointList&op=view&modiflg=yes&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}" initial="true"/>
				<emp:tab id="IqpGuarList" label="担保信息" url="queryGrtLoanRGurList.do" reqParams="op=view&serno=${context.CtrLoanContTmp.serno}&cus_id=${context.CtrLoanContTmp.cus_id}&assure_main=${context.CtrLoanContTmp.assure_main}&limit_acc_no=${context.CtrLoanContTmp.limit_acc_no}&limit_credit_no=${context.CtrLoanContTmp.limit_credit_no}&limit_ind=${context.CtrLoanContTmp.limit_ind}&menuIdTab=queryIqpLoanApp&subMenuId=queryGrtLoanRGurList&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&modiflg=yes" initial="true"/>
			<%} %>
	    	<%if("1".equals(wf_flag)){ %>
			<emp:tab id="CompareHis" label="与上一次修改值对比" url="getModifyHisViewPage.do" reqParams="cont_no=${context.CtrLoanContTmp.cont_no}&prd_id=${context.CtrLoanContTmp.prd_id}&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&op=his&wf_flag=${context.wf_flag}"/>
			<%} %>
	  	</emp:tabGroup>
	  	<div align="center">
			<br>
			<%if(!"1".equals(wf_flag)){ %>
				<emp:button id="return" label="返回列表页面" />
			<%} %>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

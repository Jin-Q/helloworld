<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------***贷款出账申请调用JS***----------------------------
function doOnLoad(){
    /*
	doChangLimitInt();//额度使用标识 
	changeRmbAmt();//获取折合人民币金额、保证金比例、风险敞口比例
	showPromissory();//是否承诺函下
	showTrust();//是否信托贷款
	showPayType();//支付方式
	changeFloatType();//利率浮动方式
	getRulMounth();//--加载利率--
	show_net();//显示所属网络
	controlBizType();//业务模式控制
	*/
}
//-----------------------业务模式控制------------------------------------
function controlBizType(){
	var bizType = CtrLoanCont.biz_type._getValue();
	if(bizType == 8){
		CtrLoanCont.rent_type._obj._renderHidden(false);
	}else {
		CtrLoanCont.rent_type._setValue("");
		CtrLoanCont.rent_type._obj._renderHidden(true);
	}
}
//--------------------------是否承诺函下---------------------------------------
function showPromissory(){
	var promissory_note = CtrLoanCont.is_promissory_note._getValue();
    if(promissory_note==1){
  	  CtrLoanCont.promissory_note._obj._renderHidden(false);
    }
}
//--------------------------是否信托贷款---------------------------------------
function showTrust(){
	var is_trust = CtrLoanCont.is_trust_loan._getValue();
    if(is_trust==1){
  	  CtrLoanCont.trust_company._obj._renderHidden(false);
    }
}
//--------------------------支付方式---------------------------------------
function showPayType(){
	 var pay_type = CtrLoanCont.CtrLoanContSub.is_conf_pay_type._getValue();
     if(pay_type==1){
   	  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(false);
   	  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(true);
     }
}
//--------------------------额度使用标识---------------------------------------
function doChangLimitInt(){
	var limitInt = CtrLoanCont.limit_ind._getValue();
	if(limitInt == "1"){
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
		CtrLoanCont.limit_acc_no._obj._renderRequired(false);
		CtrLoanCont.limit_acc_no._setValue("");
		remain_amount._obj._renderHidden(true);
		remain_amount._obj._renderRequired(false);
		remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		CtrLoanCont.limit_credit_no._setValue("");
		together_remain_amount._obj._renderHidden(true);
		together_remain_amount._obj._renderRequired(false);
		together_remain_amount._setValue("");
	}else if(limitInt == "2" || limitInt == "3"){
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
		CtrLoanCont.limit_acc_no._obj._renderRequired(true);
		CtrLoanCont.limit_acc_no._setValue("");
		remain_amount._obj._renderHidden(false);
		remain_amount._obj._renderRequired(true);
		remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._setValue("");
		together_remain_amount._obj._renderHidden(true);
		together_remain_amount._obj._renderRequired(true);
		together_remain_amount._setValue("");
	}else if(limitInt == "4"){
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
		CtrLoanCont.limit_acc_no._obj._renderRequired(false);
		CtrLoanCont.limit_acc_no._setValue("");
		remain_amount._obj._renderHidden(true);
		remain_amount._obj._renderRequired(false);
		remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._setValue("");
		together_remain_amount._obj._renderHidden(false);
		together_remain_amount._obj._renderRequired(true);
		together_remain_amount._setValue("");
	}else if(limitInt == "5" || limitInt == "6"){
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
		CtrLoanCont.limit_acc_no._obj._renderRequired(true);
		CtrLoanCont.limit_acc_no._setValue("");
		remain_amount._obj._renderHidden(false);
		remain_amount._obj._renderRequired(true);
		remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._setValue("");
		together_remain_amount._obj._renderHidden(false);
		together_remain_amount._obj._renderRequired(true);
		together_remain_amount._setValue("");
	}
}
//-------------------获取折合人民币金额、保证金比例、风险敞口比例-----------------------
function changeRmbAmt(){
	var contAmt = PvpLoanApp.cont_amt._getValue();
	if(contAmt != null && contAmt != ""){
		var secAmt = CtrLoanCont.security_amt._getValue(); 
		var rate = CtrLoanCont.exchange_rate._getValue();
		var rmbValue = Math.round(contAmt*rate*100)/100;
		var secRate = Math.round(secAmt/contAmt*100)/100;
		var secRmbAmt = Math.round(secAmt*rate*100)/100;
		CtrLoanCont.apply_rmb_amount._setValue(''+rmbValue+'');//合同金额折算人民币
		CtrLoanCont.security_rate._setValue(''+secRate+'');//保证金比例
		//CtrLoanCont.security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		CtrLoanCont.security_rate._setValue(''+secRate+'');//保证金比例
		var sSecAmt = CtrLoanCont.same_security_amt._getValue();
		var riskAmt = Math.round((contAmt-secAmt-sSecAmt)*100)/100;
		IqpLoanApp.risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
		var riskRate = Math.round((riskAmt/contAmt)*100)/100;
		IqpLoanApp.risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
}

//主管机构
function getOrgID(data){
	PvpLoanApp.in_acct_br_id._setValue(data.organno._getValue());
	PvpLoanApp.in_acct_br_id_displayname._setValue(data.organname._getValue());
};

//-------------------获取合同可用余额-----------------------
function getContBalance(){
	var contAmt = PvpLoanApp.cont_amt._getValue();
	var pvpAmt = PvpLoanApp.pvp_amt._getValue();
	var contBal = Math.round((contAmt-pvpAmt)*100)/100;
	PvpLoanApp.cont_balance._setValue(''+contBal+'');
};
//-------------------根据利率浮动方式同比调整显示-----------------------
function changeFloatType(){
	var floatType = CtrLoanCont.CtrLoanContSub.ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");
		
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(true);
		
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);
	}else if(floatType=='1'){//加点
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");
		CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");
		CtrLoanCont.CtrLoanContSub.default_rate._setValue("");
		
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);
		
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(true);	
	}else if(floatType=='2'){//正常加点逾期加百分比
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");
		
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(true);
		
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(true);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);	
	}else {
	}
};
//-------------------根据年利率同比换算月利率-----------------------
function getRulMounth(){
	var rulY = CtrLoanCont.CtrLoanContSub.ruling_ir._getValue();
	if(rulY != null && rulY != ""){
		ruling_mounth._setValue(parseFloat(rulY)/12);
		getRelYM();
	}
};
//-------------------实时调整执行年、月利率-----------------------
function getRelYM(){
	var rulY = CtrLoanCont.CtrLoanContSub.ruling_ir._getValue();
	var rulM = ruling_mounth._getValue();
	var fRate = CtrLoanCont.CtrLoanContSub.ir_float_rate._getValue();
	var fPoint = CtrLoanCont.CtrLoanContSub.ir_float_point._getValue();
	if(fRate !=null && fRate != ""){
		var relY = (parseFloat(fRate)+parseFloat(rulY));
		var relM = parseFloat(relY/12);
		CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else if(fPoint !=null && fPoint != ""){
		var relY = (parseFloat(rulY)*10000+parseFloat(fPoint))/10000;
		var relM = Math.round(relY*10000)/120000;
		CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else {
		CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(rulY);
		reality_mounth._setValue(parseFloat(rulY)/12);
	}
};

//是否显示所属网络
function show_net(){
    var net = CtrLoanCont.biz_type._getValue();
    if(net == 7 || net == 8){
    	CtrLoanCont.belong_net._obj._renderHidden(true);
    	CtrLoanCont.belong_net._setValue("");
    }else{
    	CtrLoanCont.belong_net._obj._renderHidden(false);
    	CtrLoanCont.belong_net._setValue("");
    }
};
</script>
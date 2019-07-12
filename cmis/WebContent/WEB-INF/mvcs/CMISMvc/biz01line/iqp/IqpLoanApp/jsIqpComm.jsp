<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------普通贷款业务申请调用JS----------------------------
//-----------------------是否额度合同项下支用------------------------------
function isPay(){
	var isPay = IqpLoanApp.is_limit_cont_pay._getValue();
	if(isPay == 2){
		IqpLoanApp.limit_cont_no._obj._renderHidden(true);
		IqpLoanApp.limit_cont_no._obj._renderRequired(false);
		IqpLoanApp.limit_cont_no._setValue("");
		IqpLoanApp.in_acct_br_id_displayname._obj._renderHidden(true);
		IqpLoanApp.in_acct_br_id_displayname._obj._renderRequired(false);
	}else if(isPay == 1){
		IqpLoanApp.limit_cont_no._obj._renderHidden(false);
		IqpLoanApp.in_acct_br_id_displayname._obj._renderHidden(false);
		IqpLoanApp.limit_cont_no._obj._renderRequired(true);
		IqpLoanApp.in_acct_br_id_displayname._obj._renderRequired(true);
	}
};
//-----------------------是否额度合同项下支用判断授信额度使用标志------------------------------
function is_limit_cont_pay_change(){
    var value = IqpLoanApp.is_limit_cont_pay._getValue();
	var guar_type = IqpLoanApp.assure_main._getValue();
	var limitInt = IqpLoanApp.limit_ind._getValue();
	var risk_open_amt = IqpLoanApp.risk_open_amt._getValue();
	if(value==1){
		var cont_no = IqpLoanApp.limit_cont_no._getValue();
		if(cont_no == null || cont_no == ""){
			alert("请先选择需要录入的额度合同编号！");
			return;
		}
		if(limitInt == "2" || limitInt == "3"){//使用循环额度  || 使用一次性额度
			var limit_type = "2"==limitInt?"01":"02";   //如果额度使用类型为循环额度 将额度类型置为01-循环额度  否则置为02-一次性额度
			IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="queryLmtAgrListByLimitCodeOp.do"/>&returnMethod=getLimitContNoByIsLimitContPay&limit_cont_no='+cont_no+'&guar_type='+guar_type+'&limit_type='+limit_type+'&risk_open_amt='+risk_open_amt;
		}else if(limitInt == "5" || limitInt == "6"){//使用循环额度+合作方额度  || 使用一次性额度+合作方额度
			var limit_type = "5"==limitInt?"01":"02";   //如果额度使用类型为使用循环额度+合作方额度  将额度类型置为01-循环额度  否则置为02-一次性额度
			IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="queryLmtAgrListByLimitCodeOp.do"/>&returnMethod=getLimitContNoByIsLimitContPay&limit_cont_no='+cont_no+'&guar_type='+guar_type+'&limit_type='+limit_type+'&risk_open_amt='+risk_open_amt;
		}
	}else {
		IqpLoanApp.limit_cont_no._setValue("");
	}

};
//-----------------------额度合同pop框授信返回------------------------------------
function getLimitContNoByIsLimitContPay(data){
	IqpLoanApp.limit_acc_no._setValue(data.limit_code._getValue());
	//remain_amount._setValue(data.enable_amt._getValue());
};
//-----------------------入账机构js控制------------------------------------
function getAcctOrgID(data){
	IqpLoanApp.in_acct_br_id._setValue(data.organno._getValue());
	IqpLoanApp.in_acct_br_id_displayname._setValue(data.organname._getValue());
};
//-----------------------业务模式控制------------------------------------
function controlBizType(){
	var bizType = IqpLoanApp.biz_type._getValue();
	if(bizType == 8){
		IqpLoanApp.rent_type._obj._renderHidden(false);
		IqpLoanApp.rent_type._obj._renderRequired(true);
	}else if(bizType != 8 && bizType != 7 ){
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.biz_type._obj._renderRequired(true); 
		IqpLoanApp.rent_type._setValue("");
		IqpLoanApp.rent_type._obj._renderHidden(true);
		IqpLoanApp.rent_type._obj._renderRequired(false);  
	}else {
		IqpLoanApp.rent_type._setValue("");
		IqpLoanApp.rent_type._obj._renderHidden(true);
		IqpLoanApp.rent_type._obj._renderRequired(false); 
	}
};
//-----------------------贷款性质控制------------------------------------
function loan_nature_change(){
	var loanNature = IqpLoanApp.IqpLoanAppSub.loan_nature._getValue();
	if(loanNature == 2){
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderHidden(false);
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderRequired(true);
	}else {
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._setValue("");
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderHidden(true);
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderRequired(false);
	}
};

//-----------------------通过异步取利率(无使用品种取共用，共用代码9999)------------------------------------
function getRate(){
 	var currType = IqpLoanApp.apply_cur_type._getValue();//币种
	var prdId = IqpLoanApp.prd_id._getValue();//业务品种
	var termType = IqpLoanApp.IqpLoanAppSub.term_type._getValue();//期限类型
	var term = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();//期限
	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
	if(currType == null || currType == ""){
		alert("请选择币种！");
		IqpLoanApp.apply_cur_type._obj.element.focus();
		return;
	}
	if(termType == null || termType == ""){
		alert("请选择期限类型！");
		IqpLoanApp.IqpLoanAppSub.term_type._obj.element.focus();
		return;
	}
	if(term == null || term == ""){
		IqpLoanApp.IqpLoanAppSub.apply_term._obj.element.focus();
		//alert("请录入期限！");
		return;
	}
	if(term == "0"){
		if("700021" != prdId && "700020" != prdId){
			alert("期限不能为0");
			IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
			return;
		}
	}
	//表外不取
    if('${context.supcatalog}'=="PRD20120802563"){
         return;
    }
    //如果是贸易融资业务，且为外币，且选择了牌告利率依据方式或为空
    if(('${context.supcatalog}'=="PRD20120802669" || '${context.supcatalog}'=="PRD20120802563") && currType != "CNY" &&(ir_accord_type=="01" || ir_accord_type=="02" || ir_accord_type=="" || ir_accord_type=="03" || ir_accord_type == null)){
         return;
    }
	
	var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
	if(prdId != null && prdId != ""){
		var url = '<emp:url action="getRate.do"/>'+param;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var rate = jsonstr.rate;
				var code = jsonstr.code;
				if(flag == "success"){
					if(ir_accord_type=="04"){//当[利率依据方式]为"正常利率上浮动"时，才需要根据当前基准利率表中获取[基准利率代码]行自动赋值。
						IqpLoanApp.IqpLoanAppSub.ruling_ir_code._setValue(code);
					}else{
						IqpLoanApp.IqpLoanAppSub.ruling_ir_code._setValue("");
					}
					//if(IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue()==null || IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue() == ""){
						IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(rate);
						ruling_mounth._setValue(Math.round(rate*1000000/12)/1000000); 
					//}
					getRelYM();
				}else {
					alert(msg);
					IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
function getRateforIrAccordChange(){
 	var currType = IqpLoanApp.apply_cur_type._getValue();//币种
	var prdId = IqpLoanApp.prd_id._getValue();//业务品种
	var termType = IqpLoanApp.IqpLoanAppSub.term_type._getValue();//期限类型
	var term = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();//期限
	var llyjfs = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();//利率依据方式
	if(currType == null || currType == ""){
		alert("请选择币种！");
		IqpLoanApp.apply_cur_type._obj.element.focus();
		return;
	}
	if(termType == null || termType == ""){
		alert("请选择期限类型！");
		IqpLoanApp.IqpLoanAppSub.term_type._obj.element.focus();
		return;
	}
	if(term == null || term == ""){
		IqpLoanApp.IqpLoanAppSub.apply_term._obj.element.focus();
		//alert("请录入期限！");
		return;
	}
	if(term == "0"){
		alert("期限不能为0");
		IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
		return;
	}
	if(llyjfs == "04"){ 
		var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
		if(prdId != null && prdId != ""){
			var url = '<emp:url action="getRate.do"/>'+param;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					var rate = jsonstr.rate;
					var code = jsonstr.code;
					if(flag == "success"){
						if(llyjfs=="04"){//当[利率依据方式]为"正常利率上浮动"时，才需要根据当前基准利率表中获取[基准利率代码]行自动赋值。
							IqpLoanApp.IqpLoanAppSub.ruling_ir_code._setValue(code);
						}
						if(IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue()==null || IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue() == ""){
							IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(rate);
							ruling_mounth._setValue(Math.round(rate*1000000/12)/1000000);
						}
						getRelYM();
					}else {
						alert(msg);
						IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	}
};
//-----------------------通过异步取汇率------------------------------------
function getHLByCurr(){
	IqpLoanApp.exchange_rate._setValue("");
	var currType = IqpLoanApp.apply_cur_type._getValue();
	if(currType != null && currType != ""){
		var url = '<emp:url action="getHLByCurr.do"/>&currType='+currType;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var sld = jsonstr.sld;
				if(flag == "success"){
					IqpLoanApp.exchange_rate._setValue(sld);
					changeRmbAmt();
					var term_type = IqpLoanApp.IqpLoanAppSub.term_type._getValue();
					var apply_term = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();
					if((term_type != null && term_type != "")&&(apply_term != null && apply_term != "")){
						getRate();//切换币种时需获取利率 
					}
					selectIrType();
					changeRmbAmt4Security();//计算保证金金额
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};

//-----------------------保证金通过异步取汇率------------------------------------
function getHLByCurr4Security(){
	IqpLoanApp.security_exchange_rate._setValue("");
	var currType = IqpLoanApp.security_cur_type._getValue();
	if(currType != null && currType != ""){
		var url = '<emp:url action="getHLByCurr.do"/>&currType='+currType;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var sld = jsonstr.sld;
				if(flag == "success"){
					IqpLoanApp.security_exchange_rate._setValue(sld);
					changeRmbAmt4Security();
					changeRmbAmt();
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
//-----------------------贴现通过异步取汇率------------------------------------
function getHLByCurrDscnt(){
	IqpLoanApp.exchange_rate._setValue("");
	var currType = IqpLoanApp.apply_cur_type._getValue();
	if(currType != null && currType != ""){
		var url = '<emp:url action="getHLByCurr.do"/>&currType='+currType;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var sld = jsonstr.sld;
				if(flag == "success"){
					IqpLoanApp.exchange_rate._setValue(sld);
					changeRmbAmt();
					changeRmbAmt4Security();//计算保证金金额
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
function selectIrType(){
    var cur_type = IqpLoanApp.apply_cur_type._getValue();
    var options = IqpLoanApp.IqpLoanAppSub.ir_type._obj.element.options;
    var ir_type = IqpLoanApp.IqpLoanAppSub.ir_type._getValue();
    for(var i=options.length-1;i>=0;i--){
			 options.remove(i); 
	}
    if(cur_type == "CNY"){
        var option1 = new Option("短期贷款6个月","13");
        var option2 = new Option("短期贷款6-12个月","14");
        options.add(option1);
        options.add(option2);
        if(ir_type != ""){
        	IqpLoanApp.IqpLoanAppSub.ir_type._setValue(ir_type);
        }
    }else{
    	var option1 = new Option("外汇一个月LIBOR","01");
    	var option2 = new Option("外汇二个月LIBOR","02");
    	var option3 = new Option("外汇三个月LIBOR","03");
    	var option4 = new Option("外汇四个月LIBOR","04");
    	var option5 = new Option("外汇五个月LIBOR","05");
    	var option6 = new Option("外汇六个月LIBOR","06");
    	var option7 = new Option("外汇七个月LIBOR","07");
    	var option8 = new Option("外汇八个月LIBOR","08");
    	var option9 = new Option("外汇九个月LIBOR","09");
    	var option10 = new Option("外汇十个月LIBOR","10");
    	var option11 = new Option("外汇十一个月LIBOR","11");
    	var option12 = new Option("外汇一年LIBOR","12");
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        options.add(option5);
        options.add(option6);
        options.add(option7);
        options.add(option8);
        options.add(option9);
        options.add(option10);
        options.add(option11);
        options.add(option12);
        if(ir_type != ""){
        	IqpLoanApp.IqpLoanAppSub.ir_type._setValue(ir_type);
        }
    }
};
//通过客户码查询客户所属业务条线
function getBelglineByKhm(){
	var cus_id = IqpLoanApp.cus_id._getValue();
	if(cus_id != null && cus_id != ""){
		var url = '<emp:url action="getBelglineByKhm.do"/>&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var sld = jsonstr.sld;
				if(flag == "success"){
					if(sld != "BL300"){//对私
						IqpLoanApp.is_rfu._obj._renderHidden(true);
						IqpLoanApp.is_rfu._obj._renderRequired(false);
					    IqpLoanApp.is_spe_cus._obj._renderHidden(true);
					    IqpLoanApp.is_spe_cus._obj._renderRequired(false);
					   
					}else{
						IqpLoanApp.is_rfu._obj._renderHidden(false);
						IqpLoanApp.is_rfu._obj._renderRequired(true);
					    IqpLoanApp.is_spe_cus._obj._renderHidden(false);
					    IqpLoanApp.is_spe_cus._obj._renderRequired(true);
					    checkIsRufHis();
				    }
				}else {
					alert(msg);
				}
			}
		}; 
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};

//如果是个人客户则需要查询是否有被拒绝历史
function checkIsRufHis(){
	var cus_id = IqpLoanApp.cus_id._getValue();
	if(cus_id != null && cus_id != ""){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					IqpLoanApp.is_rfu._setValue("2");
				}else {
					IqpLoanApp.is_rfu._setValue("1");
					IqpLoanApp.is_rfu._obj._renderReadonly(true);
				}
			}
		}; 
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = "<emp:url action='checkCusIsRfuHis.do'/>?cus_id="+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};

function getIsRfuHis(){
	var cus_id = IqpLoanApp.cus_id._getValue();
	var url = "<emp:url action='getCusIsRfuListHis.do'/>&cus_id="+cus_id;
	url=EMPTools.encodeURI(url);
  	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
};

//--页面加载授信使用标志------
function doInitLimit(){
	var limitInt = IqpLoanApp.limit_ind._getValue();
	var cus_id = IqpLoanApp.cus_id._getValue();  
	var lmt_type = "01";//01-单一法人 
	var lmt_type2 = "03";//03-合作方
	var outstnd_amt = IqpLoanApp.risk_open_amt._getValue();
	var assure_main = IqpLoanApp.assure_main._getValue();
	var prd_id = IqpLoanApp.prd_id._getValue();
	if(limitInt == "1" || limitInt == ""){//不使用额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(true);
		IqpLoanApp.limit_acc_no._obj._renderRequired(false);
		IqpLoanApp.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		
		IqpLoanApp.limit_credit_no._obj._renderHidden(true);
		IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
		IqpLoanApp.limit_credit_no._obj.config.url='';
	}else if(limitInt == "2" || limitInt == "3"){//使用循环额度  || 使用一次性额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(false);
		IqpLoanApp.limit_acc_no._obj._renderRequired(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		
		IqpLoanApp.limit_credit_no._obj._renderHidden(true);
		IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);

		var limit_type = "2"==limitInt?"01":"02";   //如果额度使用类型为循环额度 将额度类型置为01-循环额度  否则置为02-一次性额度
		
		IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtAmt&limit_type="+limit_type;
		IqpLoanApp.limit_credit_no._obj.config.url='';
	}else if(limitInt == "4"){//合作方额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(true);
		IqpLoanApp.limit_acc_no._obj._renderRequired(false);
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		IqpLoanApp.limit_credit_no._obj._renderHidden(false);
		IqpLoanApp.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		IqpLoanApp.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtCoopAmt";
		IqpLoanApp.limit_acc_no._obj.config.url='';
		//银票贴现使用第三方时 2014-03-15wangs添加
		if(prd_id == "300021"){
			IqpLoanApp.limit_acc_no._obj._renderHidden(true);
			IqpLoanApp.limit_acc_no._obj._renderRequired(false);
			IqpLoanApp.limit_credit_no._obj._renderHidden(true);
			IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		}
	}else if(limitInt == "5" || limitInt == "6"){//使用循环额度+合作方额度  || 使用一次性额度+合作方额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(false);
		IqpLoanApp.limit_acc_no._obj._renderRequired(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		IqpLoanApp.limit_credit_no._obj._renderHidden(false);
		IqpLoanApp.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		var limit_type = "5"==limitInt?"01":"02";   //如果额度使用类型为使用循环额度+合作方额度  将额度类型置为01-循环额度  否则置为02-一次性额度

		//合作方
		IqpLoanApp.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtCoopAmt";
		//单一法人
		IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtAmt&limit_type="+limit_type;
	}
	is_limit_cont_pay_change();  
	//如果担保方式为100%保证金时则授信使用标识为不使用授信
	if('${context.op}' != "view"){
		if(assure_main == "500"){
			IqpLoanApp.limit_ind._setValue("1");
			IqpLoanApp.limit_ind._obj._renderReadonly(true);
		}else{
			IqpLoanApp.limit_ind._obj._renderReadonly(false);
			if(prd_id != "300021" && prd_id != "300020"){
				var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
				var dkxs = IqpLoanApp.IqpLoanAppSub.loan_form._getValue();//贷款形式
				/* modified by yangzy 2014/10/29 无间贷额度可选 */
				//if(sfgjd == "1" || dkxs == "3"){
				if(dkxs == "3"){
					IqpLoanApp.limit_ind._obj._renderReadonly(true);
					IqpLoanApp.limit_acc_no._obj._renderReadonly(true);
					IqpLoanApp.limit_credit_no._obj._renderReadonly(true);
				}else{
					IqpLoanApp.limit_ind._obj._renderReadonly(false);
					IqpLoanApp.limit_acc_no._obj._renderReadonly(false);
					IqpLoanApp.limit_credit_no._obj._renderReadonly(false);
				}
			}
		}
	}
	//银票贴现默认使用第三方授信
	if(prd_id == "300021"){
		IqpLoanApp.limit_ind._setValue("4");
		IqpLoanApp.limit_ind._obj._renderReadonly(true);
	}
};

 function cleanLimitInt(){
	 IqpLoanApp.limit_ind._setValue("");
	 doChangLimitInt();
 };
//--------------------------检查授信额度使用的必要条件---------------------------------------
function isUseLimt(){
	var assureMain = IqpLoanApp.assure_main._getValue();//担保方式
	var riskOpenAmt = IqpLoanApp.risk_open_amt._getValue();//风险敞口金额

	if(assureMain == "" || assureMain == null ){
		alert("担保方式不能为空！");
		IqpLoanApp.limit_ind._setValue("");
		return;
	}
	if(riskOpenAmt == "" || riskOpenAmt == null){
		alert("风险敞口金额不能为空！");
		IqpLoanApp.limit_ind._setValue("");
		return;
	}
};

//--------------------------额度使用标识---------------------------------------
function doChangLimitInt(){
	var limitInt = IqpLoanApp.limit_ind._getValue();
	var cus_id = IqpLoanApp.cus_id._getValue();
	var lmt_type = "01";//01-单一法人 
	var lmt_type2 = "03";//03-合作方
	var outstnd_amt = IqpLoanApp.risk_open_amt._getValue();
	if(outstnd_amt == null || outstnd_amt == ""){
	   //alert("风险敞口金额不能为空！");
       IqpLoanApp.limit_ind._setValue("");
       return;
	}
	var assure_main = IqpLoanApp.assure_main._getValue();
	var prd_id = IqpLoanApp.prd_id._getValue();
	//银票贴现默认使用第三方授信
	if(prd_id != "300021"){
	   if(assure_main == null || assure_main == ""){
	     //alert("担保方式不能为空！");
	     IqpLoanApp.limit_ind._setValue("");
	     return;
	   }
	}else{
		IqpLoanApp.limit_ind._setValue("4");
		IqpLoanApp.limit_ind._obj._renderReadonly(true);
	}
	var prd_id = IqpLoanApp.prd_id._getValue();
	if(limitInt == "1" || limitInt == ""){//不使用额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(true);
		IqpLoanApp.limit_acc_no._obj._renderRequired(false);
		IqpLoanApp.limit_acc_no._setValue("");
		IqpLoanApp.limit_credit_no._obj._renderHidden(true);
		IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		IqpLoanApp.limit_credit_no._setValue("");
	}else if(limitInt == "2" || limitInt == "3"){//使用循环额度  || 使用一次性额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(false);
		IqpLoanApp.limit_acc_no._obj._renderRequired(true);
		IqpLoanApp.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		IqpLoanApp.limit_credit_no._obj._renderHidden(true);
		IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		IqpLoanApp.limit_credit_no._setValue("");
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		//together_remain_amount._setValue("");

		var limit_type = "2"==limitInt?"01":"02";   //如果额度使用类型为循环额度 将额度类型置为01-循环额度  否则置为02-一次性额度
		
		IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtAmt&limit_type="+limit_type;
		IqpLoanApp.limit_credit_no._obj.config.url='';
	}else if(limitInt == "4"){//合作方额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(true);
		IqpLoanApp.limit_acc_no._obj._renderRequired(false);
		IqpLoanApp.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		//remain_amount._setValue("");
		IqpLoanApp.limit_credit_no._obj._renderHidden(false);
		IqpLoanApp.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		IqpLoanApp.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtCoopAmt";
		IqpLoanApp.limit_acc_no._obj.config.url='';
		//银票贴现,商票贴现使用第三方时 2014-03-15wangs添加
		if(prd_id == "300021"){
			IqpLoanApp.limit_acc_no._obj._renderHidden(true);
			IqpLoanApp.limit_acc_no._obj._renderRequired(false);
			IqpLoanApp.limit_credit_no._obj._renderHidden(true);
			IqpLoanApp.limit_credit_no._obj._renderRequired(false);
		}
	}else if(limitInt == "5" || limitInt == "6"){//使用循环额度+合作方额度  || 使用一次性额度+合作方额度
		IqpLoanApp.limit_acc_no._obj._renderHidden(false);
		IqpLoanApp.limit_acc_no._obj._renderRequired(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		IqpLoanApp.limit_credit_no._obj._renderHidden(false);
		IqpLoanApp.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		var limit_type = "5"==limitInt?"01":"02";   //如果额度使用类型为使用循环额度+合作方额度  将额度类型置为01-循环额度  否则置为02-一次性额度
		
		//合作方
		IqpLoanApp.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtCoopAmt";
		//单一法人
		IqpLoanApp.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&guar_type="+assure_main+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtAmt&limit_type="+limit_type;
	}
	is_limit_cont_pay_change();
	//如果担保方式为100%保证金时则授信使用标识为不使用授信
	if(assure_main == "500"){
		IqpLoanApp.limit_ind._setValue("1");
		IqpLoanApp.limit_ind._obj._renderReadonly(true);
	}else{
		IqpLoanApp.limit_ind._obj._renderReadonly(false);
	}
};

function cleanLimitInd(){
	var prd_id = IqpLoanApp.prd_id._getValue();
	//银票贴现,商票贴现 2014-03-15wangs添加
	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
	if(prd_id == "300021" || prd_id == "300020"){
		limit_ind = IqpLoanApp.limit_ind._getValue();
		var limitIndOptions = IqpLoanApp.limit_ind._obj.element.options;
		for(var i=limitIndOptions.length-1;i>=0;i--){	
			if(limitIndOptions[i].value=="4" || limitIndOptions[i].value=="5" || limitIndOptions[i].value=="6"){//
				limitIndOptions.remove(i);
			}
		}
		var varOption = new Option('使用承兑人额度','4');
		limitIndOptions.add(varOption);
		IqpLoanApp.limit_ind._setValue(limit_ind);
	}
};

//-------------------银行承兑汇票贴现，担保方式，担保方式细分隐藏-----------------------
function checkAssureMain(){
    var prd_id = IqpLoanApp.prd_id._getValue();
    if(prd_id == "300021"){
    	IqpLoanApp.assure_main._obj._renderHidden(true);
    	IqpLoanApp.assure_main_details._obj._renderHidden(true);

    	IqpLoanApp.assure_main._obj._renderRequired(false);
    	IqpLoanApp.assure_main_details._obj._renderRequired(false);
    }
}
//-------------------获取折合人民币金额、保证金比例、风险敞口比例-----------------------
function changeRmbAmt(){
	var prd_id=IqpLoanApp.prd_id._getValue();
	var appAmt = IqpLoanApp.apply_amount._getValue();//申请金额
	if(appAmt != null && appAmt != ""){
		//var secAmt = IqpLoanApp.security_amt._getValue();//保证金金额
		var rate = IqpLoanApp.exchange_rate._getValue();//汇率

		var setRate = IqpLoanApp.security_rate._getValue();//保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		var rmbValue = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round((parseFloat(rmbValue)*parseFloat(setRate))*100)/100;//保证金折算人民币金额
		IqpLoanApp.apply_rmb_amount._setValue(''+rmbValue+'');//申请金额折算人民币

		changeRmbAmt4Security();
		var security_amt = IqpLoanApp.security_amt._getValue();//保证金金额
		var security_exchange_rate = IqpLoanApp.security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate)*100)/100;
		IqpLoanApp.security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = IqpLoanApp.same_security_amt._getValue();//视同保证金
		if(sSecAmt == null || sSecAmt == ""){
			sSecAmt = 0;
		}
		if(prd_id == "700020" || prd_id =="700021"){
            var floodact_perc = '${context.floodact_perc}';
       	    if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
       	    	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
       	    }else{
       	    	var appAmt = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
           	}
		}else{
			var appAmt = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
	    }
		var a = Math.round(parseFloat(appAmt)*100)/100;
		var riskAmt = Math.round((parseFloat(a)-parseFloat(secRmbAmt)-parseFloat(sSecAmt))*100)/100;
		if(riskAmt<0){
			riskAmt = 0;
		}
		IqpLoanApp.risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
        //取风险敞口金额供授信Pop框使用
		//doChangLimitInt(); 
		doInitLimit();
		var riskRate;
		if(appAmt!=0){
			riskRate = Math.round((riskAmt/appAmt)*10000)/10000;
			
			if(riskRate < 0){   
				riskRate = 0;
			}else if(riskRate > 1){
				riskRate = 1;
			}
		}else{
			var serate  = IqpLoanApp.security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}
		IqpLoanApp.risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};
//--------------检查客户半年日均-------------------
function checkCusHalfAmt(){
	var rmb_amt = IqpLoanApp.apply_rmb_amount._getValue();
	var cus_total_amt = IqpLoanApp.cus_total_amt._getValue();
	var is_close_loan = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();
    if('${context.supcatalog}' == "PRD20120802659" && is_close_loan =="1"){////如果是个人经营性贷款
        if(parseFloat(cus_total_amt)/parseFloat(rmb_amt)<0.05){
             alert("客户及其配偶的半年日均合计占贷款金额占比不得低于5%");
        }
    }
};
//-------------------如果为信用证业务则需查询溢装比例,重新计算保证金金额及风险敞口-----------------------
function getCreditAmt4NewApplyAmt(serno,apply_cur_type,apply_amount,setRate,rate,security_exchange_rate){
	if(serno != null && serno != "" && apply_cur_type != null && apply_cur_type != "" && serno != null && serno != ""){
		var url = '<emp:url action="getCreditAmt4NewApplyAmt.do"/>&serno='+serno+'&apply_cur_type='+apply_cur_type+'&apply_amount='+apply_amount+'&exchange_rate='+setRate;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var rateValue = jsonstr.rateValue;
				if(flag == "success"){
					security_amt = Math.ceil((parseFloat(rateValue*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
	            	IqpLoanApp.security_amt._setValue(''+security_amt+'');
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
//-------------------计算保证金金额-----------------------
function changeRmbAmt4Security(){
	var prd_id=IqpLoanApp.prd_id._getValue();
	var appAmt = IqpLoanApp.apply_amount._getValue();//申请金额
	var security_cur_type = IqpLoanApp.security_cur_type._getValue();//保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = IqpLoanApp.exchange_rate._getValue();//汇率
		var setRate =IqpLoanApp.security_rate._getValue();//保证金比例
		var security_exchange_rate = IqpLoanApp.security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		//如果是贸易融资业务
		var security_amt;
		if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
            if(prd_id == "700020" || prd_id =="700021"){
                 var floodact_perc = '${context.floodact_perc}';
            	 if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
            		appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
            		security_amt = Math.ceil((parseFloat(appAmt*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
                 	IqpLoanApp.security_amt._setValue(''+security_amt+'');
                 }else{
                	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
                 	IqpLoanApp.security_amt._setValue(''+security_amt+'');
                 }
            }else{
            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
            	IqpLoanApp.security_amt._setValue(''+security_amt+'');
            }
		}else{
		    security_amt = Math.round(parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)*100)/100;//保证金金额
		    IqpLoanApp.security_amt._setValue(''+security_amt+'');
		}
	}
};

function _doKeypressDown() {
	try{
		if(IqpLoanApp.security_rate._obj.element.focus){
			IqpLoanApp.security_rate._obj.element.select();
	    }
	}catch(e){
		alert(e);
	}
}
//-------------------计算保证金金额(修改保证金比例的时候)-----------------------
function changeRmbAmt4SecurityChange(){
	var prd_id=IqpLoanApp.prd_id._getValue();
	var appAmt = IqpLoanApp.apply_amount._getValue();//申请金额
	var security_cur_type = IqpLoanApp.security_cur_type._getValue();//保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = IqpLoanApp.exchange_rate._getValue();//汇率
		var setRate =IqpLoanApp.security_rate._obj.element.value;//保证金比例
		//var setRate =IqpLoanApp.security_rate._getValue();//保证金比例
		var security_exchange_rate = IqpLoanApp.security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}else{
			if(setRate.indexOf("%")>-1){
				setRate = setRate.replace("%","");
				setRate = Number(setRate);
			}
		}
		//如果是贸易融资业务
		var security_amt;
		if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
			if(prd_id == "700020" || prd_id =="700021"){
				var floodact_perc = '${context.floodact_perc}';
           	    if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
           	    	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
           	    	security_amt = Math.ceil((parseFloat(appAmt*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
                	IqpLoanApp.security_amt._setValue(''+security_amt+'');
           	    }else{
           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
                	IqpLoanApp.security_amt._setValue(''+security_amt+'');
               	}
            }else{
            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
            	IqpLoanApp.security_amt._setValue(''+security_amt+'');
            }
			
		}else{
		    security_amt = Math.round((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)),2)/100;//保证金金额
		    IqpLoanApp.security_amt._setValue(''+security_amt+'');
	    }
	}
};
//-------------------获取保证金比例同比修改保证金折算人民币金额、敞口金额，敞口比率-----------------------
function changeSecRate(){
	var appAmt = IqpLoanApp.apply_amount._getValue();//申请金额
	var prd_id = IqpLoanApp.prd_id._getValue();//
	if(appAmt != null && appAmt != ""){
		//var secAmt = IqpLoanApp.security_amt._getValue();//保证金金额
		var rate = IqpLoanApp.exchange_rate._getValue();//汇率
		//var setRate = IqpLoanApp.security_rate._getValue();//保证金比例
		var setRate = IqpLoanApp.security_rate._obj.element.value;//保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}else{
			if(setRate.indexOf("%")>-1){
				setRate = setRate.replace("%","");
				setRate = Number(setRate);
			}
		}

		var rmbValue = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round(parseFloat(rmbValue)*parseFloat(setRate))/100;//保证金折算人民币金额
		IqpLoanApp.apply_rmb_amount._setValue(''+rmbValue+'');//申请金额折算人民币

		changeRmbAmt4SecurityChange();
		var security_amt = IqpLoanApp.security_amt._getValue();//保证金金额
		var security_exchange_rate = IqpLoanApp.security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate)*100)/100;

		IqpLoanApp.security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = IqpLoanApp.same_security_amt._getValue();//视同保证金
		if(sSecAmt == null || sSecAmt == ""){
			sSecAmt = 0;
		}
		var appAmt;
		if(prd_id == "700020" || prd_id =="700021"){
            var floodact_perc = '${context.floodact_perc}';
       	    if(floodact_perc !='0' && floodact_perc !='' && floodact_perc !=null){
       	    	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
       	    }else{
       	    	appAmt = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
           	}
		}else{
			appAmt = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		}
		var a = Math.round(parseFloat(appAmt)*100)/100;
		var riskAmt = Math.round((parseFloat(a)-parseFloat(secRmbAmt)-parseFloat(sSecAmt))*100)/100;
		if(riskAmt<0){
			riskAmt = 0;
		}
		IqpLoanApp.risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
		var riskRate;
		if(appAmt!=0){
			//riskRate = (parseFloat(riskAmt)/parseFloat(rmbValue));
			riskRate = Math.round((riskAmt/appAmt)*10000)/10000;
			if(riskRate < 0){
				riskRate = 0;
			}else if(riskRate > 1){
				riskRate = 1;
			}
		}else{
			var serate  = IqpLoanApp.security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}
		IqpLoanApp.risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};
//-------------------根据年利率同比换算月利率-----------------------
function getRulMounth(){
	var rulY = IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue();
	if(rulY != null && rulY != ""){
		ruling_mounth._setValue(parseFloat(rulY)/12);
		getRelYM();
	}
	var llyjfs = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();//利率依据方式
	if(llyjfs == "01"){//议价利率
        IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);
        IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);
    }
};
//-------------------通过libor利率类型获取牌告基准利率(curr 币种\irType 利率种类)-----------------------
function getLiborRate(curr,irType){
	if(curr != null && curr != "" && irType != null && irType != ""){
		var url = '<emp:url action="getLiborRate.do"/>&curr='+curr+'&irType='+irType;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var rateValue = jsonstr.rateValue;
				if(flag == "success"){
					IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(rateValue);
					ruling_mounth._setValue(parseFloat(rateValue)/12);
					getReality_ir_y();
					getRelYM();
					getOverdueRateY();
					getDefaultRateY();
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
//-------------------加点、加百分比实时调整执行年、月利率-----------------------
function getRelYM(){
	var rulY = IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue();
	var ir_accord_type  = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();//利率依据方式 
	if(rulY == null || rulY == ""){
		rulY = 0;
	}
	var rulM = ruling_mounth._getValue();
	var fRate = IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj.element.value;
	var fPoint = IqpLoanApp.IqpLoanAppSub.ir_float_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*rulY;
		var relM = parseFloat(relY)/12;
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");
	        IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(rulY);
			reality_mounth._setValue(parseFloat(rulY)/12);
	        return;
	    }
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");
		var relY = (parseFloat(rulY)*10000+parseFloat(fPoint))/10000;
		var relM = Math.round(relY*10000)/120000;
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(relY);
		reality_mounth._setValue(relM);
	}else {
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");
		//只有利率依据方式为牌告利率的时候,执行年利率为基准年利率
		if(ir_accord_type == "02"){
			IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(rulY);
			reality_mounth._setValue(parseFloat(rulY)/12);
		}
	}
	if(ir_accord_type == "02" || ir_accord_type == "04"){
		getOverdueRateY();//更新逾期利率 
	    getDefaultRateY();//更新违约利率
	}
};
//-------------------根据贷款利率浮动方式同比调整显示-----------------------
function changeIrFloatType(){
	var floatType = IqpLoanApp.IqpLoanAppSub.ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
		
	}else if(floatType=='1'){//加点
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//贷款利率浮动比
	}else {
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//贷款利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
	}
};
//-------------------根据逾期利率浮动方式同比调整显示-----------------------
function changeOverdueFloatType(){
	var overdueFloatType = IqpLoanApp.IqpLoanAppSub.overdue_float_type._getValue();
	if(overdueFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
	}else if(overdueFloatType=='1'){//加点
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
	}else {
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
	}
};
//-------------------根据违约利率浮动方式同比调整显示-----------------------
function changeDefaultFloatType(){
	var defaultFloatType = IqpLoanApp.IqpLoanAppSub.default_float_type._getValue();
	if(defaultFloatType=='0'){//加百分比
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
	}else if(defaultFloatType=='1'){//加点
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(false);//违约利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(true);//违约利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
	}else {
		/** 显示控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
	}
};
//----------------更新逾期利率-----------------
function getOverdueRateY(){
	var overdueY = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
	if(overdueY == null || overdueY == ""){
		overdueY = 0;
	}
	var fRate = IqpLoanApp.IqpLoanAppSub.overdue_rate._obj.element.value;
	var fPoint = IqpLoanApp.IqpLoanAppSub.overdue_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*overdueY; 
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");
	        IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue("");
	        return;
	    }
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");
		var relY = (parseFloat(overdueY)*10000+parseFloat(fPoint))/10000;
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue("");
	}
};

//---------------更新违约利率------------------
function getDefaultRateY(){
	var defaultY = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
	if(defaultY == null || defaultY == ""){
		defaultY = 0;
	}
	var fRate = IqpLoanApp.IqpLoanAppSub.default_rate._obj.element.value;
	var fPoint = IqpLoanApp.IqpLoanAppSub.default_point._getValue();
	if(fRate !=null && fRate != ""){//加百分比
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");
		var relY =parseFloat(1+(parseFloat(fRate)/100))*defaultY; 
		IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(relY);
	}else if(fPoint !=null && fPoint != ""){//加点
		if(fPoint.search("^[0-9|.|-]*$")!=0){
	        alert("请输入正确数据!");
	        IqpLoanApp.IqpLoanAppSub.default_point._setValue("");
	        IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue("");
	        return;
	    }
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");
		var relY = (parseFloat(defaultY)*10000+parseFloat(fPoint))/10000;
		IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(relY);
	}else {
		//alert("请输入正确数据!");
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");
		IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue("");
	}
};
function showDifRateType(){
	  var type = IqpLoanApp.IqpLoanAppSub.ir_float_type._getValue();
	  if(type==0){
		  IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");
		  IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");
		  IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);
		  IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);
		  IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
		  IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(false);
	  }else if(type==1){
		  IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");
		  IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");
		  IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);
		  IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);
		  IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(false);
		  IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(false);
	  }else if(type==2){

		  IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
		  IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(false);
		  IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");
		  IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");
		  IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);
		  IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);
	  }
	  
  };
  function getCusForm(){
		var cus_id = IqpLoanApp.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'cus_window','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	//生成还款策略
	function getRepayForm(){  debugger;
		var apply_amount = IqpLoanApp.apply_amount._getValue();
		if(apply_amount==""){
			alert("请输入申请金额!");
			return;
		}  
		var repay_mode_type = IqpLoanApp.IqpLoanAppSub.repay_mode_type._getValue();
		var repay_date = IqpLoanApp.IqpLoanAppSub.repay_date._getValue();
		var repay_term = IqpLoanApp.IqpLoanAppSub.repay_term._getValue();
		var repay_space = IqpLoanApp.IqpLoanAppSub.repay_space._getValue();
		var is_term = IqpLoanApp.IqpLoanAppSub.is_term._getValue();
		var interest_term = IqpLoanApp.IqpLoanAppSub.interest_term._getValue();
		var prd_id = IqpLoanApp.prd_id._getValue();
		if(repay_mode_type =="01" || repay_mode_type =="03" || repay_mode_type =="04"){
			if(repay_date==""){
				alert("请输入还款日");
				return;
			}
			if(repay_term==""){
	            alert("请输入还款间隔周期");
	            return;
			}
			if(repay_space==""){
				alert("请输入还款间隔");
				return;
			}
			if(is_term==""){
				alert("请输入是否期供");
				return;
			}
		}
		var apply_date = IqpLoanApp.apply_date._getValue();
		if(apply_date==""){
			alert("请输入申请日期");
			return;
		}
		var term_type = IqpLoanApp.IqpLoanAppSub.term_type._getValue();//期限类型
		if(term_type==""){
			alert("请输入期限类型");
			return;
		}
		var apply_term = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();//申请期限
		if(apply_term==""){
			alert("请输入申请期限");
			return;
		}
		var repay_type = IqpLoanApp.IqpLoanAppSub.repay_type._getValue();//还款方式
		if(repay_type==""){
			alert("请输入还款方式");
			return;
		}
		var loan_type = IqpLoanApp.IqpLoanAppSub.loan_type._getValue();
        if(loan_type==""){
        	alert("请输入贷款种类");
        	return;
        }
        var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
        //如果是不计息
        if(ir_accord_type == "03" ){
            var reality_ir_y = "0";
        }else{ 
        	var reality_ir_y = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
    		if(reality_ir_y==""){
    			alert("请输入执行利率（年）");
    			return;
    		}
        }
		if(apply_amount!="" && apply_date!="" && term_type!="" && apply_term!="" && repay_type!="" && loan_type!="" && reality_ir_y!="" ){
			var url = '<emp:url action="createRepayPlanOp.do"/>&apply_amount='+apply_amount+'&repay_date='+repay_date+'&apply_date='+apply_date+'&term_type='+term_type+'&apply_term='+apply_term+'&repay_type='+repay_type+'&loan_type='+loan_type+'&reality_ir_y='+reality_ir_y+'&repay_term='+repay_term+'&repay_space='+repay_space+'&is_term='+is_term+'&interest_term='+interest_term+'&prd_id='+prd_id;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param); 
		}else{
            alert("请输入相关信息");
            return;  
		}
	};
	//是否隐藏支付方式
	function isShow(){
		  var payType = IqpLoanApp.IqpLoanAppSub.conf_pay_type._getValue();
		  if(payType==1){
			  IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(false);
			  IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderRequired(true);
		  }else{
			  IqpLoanApp.IqpLoanAppSub.pay_type._setValue("");
			  IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderRequired(false);
			  IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(true);
		   }

	  };
	  function isShowNote(){
	     var note = IqpLoanApp.is_promissory_note._getValue();
	     if(note==1){
	    	 IqpLoanApp.promissory_note._obj._renderHidden(false);
	    	 IqpLoanApp.promissory_note._obj._renderRequired(true);
	     }else{
	    	 IqpLoanApp.promissory_note._setValue("");
	    	 IqpLoanApp.promissory_note._obj._renderRequired(false);
	    	 IqpLoanApp.promissory_note._obj._renderHidden(true);
	     }
	  };
	  /**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
	  function isShowCompany(){
		 var loan = IqpLoanApp.is_trust_loan._getValue();
		 if(loan=="1"){
			 IqpLoanApp.trust_company._obj._renderHidden(false);
			 IqpLoanApp.trust_company._obj._renderRequired(true);
			 IqpLoanApp.trust_pro_name._obj._renderHidden(false);
			 IqpLoanApp.trust_pro_name._obj._renderRequired(true);
		 }else{
			 IqpLoanApp.trust_company._setValue("");
			 IqpLoanApp.trust_pro_name._setValue("");
			 IqpLoanApp.trust_company._obj._renderRequired(false);
			 IqpLoanApp.trust_company._obj._renderHidden(true);
			 IqpLoanApp.trust_pro_name._obj._renderRequired(false);
			 IqpLoanApp.trust_pro_name._obj._renderHidden(true);
		 }
	  };
	  /**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
	  function cleanDate(){
		  IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
	  };

	  function setRulingMounth(){
		  var year = IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue();
		  ruling_mounth._setValue(year/12); 
	  }; 

	  function setreality_ir_yMounth(){
	      var reality_ir_y = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
	      reality_mounth._setValue(reality_ir_y/12);
	  };

	  
		function loantypeReturn(date){
			IqpLoanApp.IqpLoanAppSub.loan_type._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.loan_type_displayname._setValue(date.label);
		};
		function agricultureReturn(date){
			IqpLoanApp.IqpLoanAppSub.agriculture_type._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.agriculture_type_displayname._setValue(date.label);
		};
		function projectReturn(date){
			IqpLoanApp.IqpLoanAppSub.ensure_project_loan._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname._setValue(date.label);
		};
		function onReturn(date){
			IqpLoanApp.IqpLoanAppSub.estate_adjust_type._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.estate_adjust_type_displayname._setValue(date.label);
		};
		function strategyReturn(date){
			IqpLoanApp.IqpLoanAppSub.strategy_new_loan._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.strategy_new_loan_displayname._setValue(date.label);
		};
		function newPrdReturn(date){
			IqpLoanApp.IqpLoanAppSub.new_prd_loan._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.new_prd_loan_displayname._setValue(date.label);
		};
		function greenPrdReturn(date){
			IqpLoanApp.IqpLoanAppSub.green_prd._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.green_prd_displayname._setValue(date.label);
		};
		function loanBelong1Return(date){
			IqpLoanApp.IqpLoanAppSub.loan_belong1._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._setValue(date.label);
		};
		
		function loanDirectionReturn(date){
			IqpLoanApp.IqpLoanAppSub.loan_direction._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._setValue(date.label);
		};
		function loanBelong2Return(date){
			IqpLoanApp.IqpLoanAppSub.loan_belong2._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._setValue(date.label);
			
		};
		function loanBelong3Return(date){
			IqpLoanApp.IqpLoanAppSub.loan_belong3._obj.element.value=date.id;
			IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._setValue(date.label);
		};

		//主管机构
		function getOrgID(data){
			IqpLoanApp.manager_br_id._setValue(data.organno._getValue());
			IqpLoanApp.manager_br_id_displayname._setValue(data.organname._getValue());
		};

		function checkDate(){
	        var repayDate = IqpLoanApp.IqpLoanAppSub.repay_date._getValue();
		};
        //是否显示所属网络
		function show_net(){
            var net = IqpLoanApp.biz_type._getValue();
            if(net == 7 || net == 8){
            	IqpLoanApp.belong_net._obj._renderHidden(true);
            	IqpLoanApp.belong_net._obj._renderRequired(false);
            	IqpLoanApp.belong_net._setValue("");
            }else{
            	IqpLoanApp.belong_net._obj._renderHidden(false);
            	IqpLoanApp.belong_net._obj._renderRequired(false); 
            }   
	    };

//是否无间贷下拉框响应方法
function is_cloas_loan_change(){
	var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
	var dkxs = IqpLoanApp.IqpLoanAppSub.loan_form._getValue();//贷款形式
	var prd_id = IqpLoanApp.prd_id._getValue();
	if(sfgjd == "1" || dkxs == "3"){
		IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderHidden(false);
		IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderRequired(true); 

		IqpLoanApp.limit_ind._obj._renderReadonly(true);
		IqpLoanApp.limit_acc_no._obj._renderReadonly(true);
		IqpLoanApp.limit_credit_no._obj._renderReadonly(true);
	}else{
		IqpLoanApp.IqpLoanAppSub.repay_bill._setValue("");   
		IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderHidden(true);
		IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderRequired(false); 

		IqpLoanApp.limit_ind._obj._renderReadonly(false);
		/**modified by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造 begin**/
		if(prd_id!="100088"){
			IqpLoanApp.limit_acc_no._obj._renderReadonly(false);
			IqpLoanApp.limit_acc_no._setValue("");
		}
		/**modified by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造 end**/
		IqpLoanApp.limit_credit_no._obj._renderReadonly(false);
		IqpLoanApp.limit_credit_no._setValue("");
	}
	checkIsCloseLoan();
	if(sfgjd == "1" && "PRD20120802659"=="${context.supcatalog}"){
		getCusTotalAmt();
		IqpLoanApp.cus_total_amt._obj._renderHidden(false);
	}else{
		IqpLoanApp.cus_total_amt._obj._renderHidden(true);
	}
	/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 begin**/
	/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造 begin**/
	/**if(sfgjd == "1"){
		var cusId = IqpLoanApp.cus_id._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var bizline = jsonstr.bizline;
				if(flag == "success"){
					if(bizline!="BL300"){
						IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);
						//剔除不属于无间贷的借款用途
						var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;		
						for ( var i = options.length - 1; i >= 0; i--) {
							if(options[i].value != "10" && options[i].value != "13" && options[i].value != ""){
									options.remove(i);
							}
						}
					}else{
						IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(false);
					}
		       }else { 
	               alert(msg);
	           }
	    }
	  };
	  var handleFailure = function(o){
	    alert("异步回调失败！");  
	  };
	  var callback = {
	    success:handleSuccess,
	    failure:handleFailure
	  };
	  var url = '<emp:url action="getBizLineByCusId.do"/>&cusid='+cusId;
	  var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}else{
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(false);
	}*/
	/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造 end**/

	if(sfgjd=="1"){
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);//
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(false);
		IqpLoanApp.IqpLoanAppSub.conf_pay_type._setValue("1"); 
		IqpLoanApp.IqpLoanAppSub.pay_type._setValue("0"); 
		//剔除不属于无间贷的借款用途
		var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;		
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value != "10" && options[i].value != "13" && options[i].value != ""){
					options.remove(i);
			}
		}
	}else{
	 /**modified by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造 begin**/
		if(prd_id!="100088"){
			IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(false);
		}
	 /**modified by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造 begin**/
			var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;		
			for ( var i = options.length - 1; i >= 0; i--) {
				options.remove(i);
			}
		getAllLoanUseType();
	}
};

function getAllLoanUseType(){
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			if(flag == "success"){
				var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;
				options.add(new Option("-----请选择-----",""));	
				var ennameStr=jsonstr.ennameStr;
				var cnnameStr=jsonstr.cnnameStr;
				var ennames=ennameStr.split(",");
				var cnnames=cnnameStr.split(",");
				for ( var i=0;i<=ennames.length - 1;i++) {
					options.add(new Option(cnnames[i],ennames[i]));	
				}
	       }else { 
               alert(msg);
           }
    }
  };
  var handleFailure = function(o){
    alert("异步回调失败！");  
  };
  var callback = {
    success:handleSuccess,
    failure:handleFailure
  };
  var url = '<emp:url action="querySDicByOpttype.do"/>&opttype=STD_ZB_USE_TYPE';
  var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
};
/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 end**/
//无间贷，申请金额校验
function checkIsCloseLoan(){
	var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
	if(sfgjd == "1"){
		IqpLoanApp.apply_amount._obj._renderReadonly(true);
    }else{
    	IqpLoanApp.apply_amount._obj._renderReadonly(false);
    }
};

//主担保方式下拉框相应方法
function assure_mainChange(){
	var assureMainValue =IqpLoanApp.assure_main._getValue();
	var assmainDtsoptions = IqpLoanApp.assure_main_details._obj.element.options;
	var a = "0";
	var b = "0";
	var c = "0";
	var d = "0";
	var e = "0";
	for(var i=assmainDtsoptions.length-1;i>=0;i--){	
		if(assmainDtsoptions[i].value=="1"){//普通抵押
			a = "1";
		}
		if(assmainDtsoptions[i].value=="8"){//保证
			b = "1";
		}
		if(assmainDtsoptions[i].value=="9"){//信用
			c = "1";
		}
		if(assmainDtsoptions[i].value=="10"){//100%保证金
			d = "1";
		}
		if(assmainDtsoptions[i].value=="11"){//准全额保证金
			e = "1";
		}
	}
	if(a == "0"){
		var varOption = new Option('普通抵押','1');
		assmainDtsoptions.add(varOption);
	}
	if(b == "0"){
		var varOption = new Option('保证','8');
		assmainDtsoptions.add(varOption);
	}
	if(c == "0"){
		var varOption = new Option('信用','9');
		assmainDtsoptions.add(varOption);
	}
	if(d == "0"){
		var varOption = new Option('100%保证金','10');
		assmainDtsoptions.add(varOption);
	}
	if(e == "0"){
		var varOption = new Option('准全额保证金','11');
		assmainDtsoptions.add(varOption);
	}
	if(assureMainValue == ""){
		IqpLoanApp.assure_main_details._obj._renderReadonly(false);
		IqpLoanApp.assure_main_details._setValue("");
	}else if(assureMainValue =="100"){//主担保方式为抵押时，担保方式细分自动赋值为抵押
		IqpLoanApp.assure_main_details._setValue("1");
	    IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="300"){//保证
		IqpLoanApp.assure_main_details._setValue("8");
	    IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="400"){//信用
		IqpLoanApp.assure_main_details._setValue("9");
	    IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="500"){//100%保证金  
		IqpLoanApp.assure_main_details._setValue("10");
		IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="510"){//准全额保证金
		IqpLoanApp.assure_main_details._setValue("11");
		IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue.substring(0,1) == "2"){
		IqpLoanApp.assure_main_details._obj._renderReadonly(false);
		IqpLoanApp.assure_main_details._setValue("");
		var assmainDtsoptions = IqpLoanApp.assure_main_details._obj.element.options;
		for(var i=assmainDtsoptions.length-1;i>=0;i--){	
			if(assmainDtsoptions[i].value=="1" || assmainDtsoptions[i].value=="8" ||assmainDtsoptions[i].value=="9" ||assmainDtsoptions[i].value=="10" ||assmainDtsoptions[i].value=="11"){
				assmainDtsoptions.remove(i);
			}
		}
	}
};

//-------------------利率依据方式下拉框响应方法-----------------------
function ir_accord_typeChange(data){
	var llyjfs = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();//利率依据方式
	var llfdfs = IqpLoanApp.IqpLoanAppSub.ir_float_type._getValue();//利率浮动方式
	if(llyjfs == "01"){//议价利率
		//added by yangzy 20150923 议价利率相关提醒 start
		alert("请注意！逾期利率与违约利率不要录入浮动比！");
		//added by yangzy 20150923 议价利率相关提醒 end
		/** 显示控制 */
    	IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
			IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
			IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
			IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("0");//利率调整方式
			IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
			IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
			IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
			IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
			IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
			IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("");//违约利率浮动方式
			IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
			IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
			//IqpLoanApp.IqpLoanAppSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);//计息周期

		/** 只读控制 */
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderReadonly(false);//违约利率（年）
		
		/** 获取基准利率 */
		//getRate();
    }else if(llyjfs == "02"){//牌告利率依据
		 /** 显示控制 */
    	IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
			IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
			IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
			IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("");//利率调整方式
			IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
			IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
			IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
			IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
			IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
			IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("0");//违约利率浮动方式
			IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
			IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
			//IqpLoanApp.IqpLoanAppSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);//计息周期

		/** 只读控制 */
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
		//getRate(); 
		changeOverdueFloatType();
		changeDefaultFloatType();
    }else if(llyjfs == "03"){//不计息
        /** 显示控制 */
    	IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);//计息周期
		/** 赋值控制 */
		if(data != "init"){
			IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
			IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
			IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
			IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("");//利率调整方式
			IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
			IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
			IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
			IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("");//逾期利率浮动方式
			IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
			IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
			IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("");//违约利率浮动方式
			IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
			IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
		}
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);//计息周期

		/** 只读控制 */
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "04"){//正常利率
		 /** 显示控制 */
    	IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
			IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
			IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
			IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
			IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("");//利率调整方式
			IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
			IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
			IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
			IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("0");//逾期利率浮动方式
			IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
			IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
			IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("0");//违约利率浮动方式
			IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
			IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数
			//IqpLoanApp.IqpLoanAppSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(true); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);//计息周期

		/** 只读控制 */
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

		changeOverdueFloatType();
		changeDefaultFloatType();
		/** 获取基准利率 */
		getRateforIrAccordChange();
    }
	if(llyjfs!="04"){//当[利率依据方式]为"正常利率上浮动"时，才需要根据当前基准利率表中获取[基准利率代码]行自动赋值。
		IqpLoanApp.IqpLoanAppSub.ruling_ir_code._setValue("");
	}
	// 贸易融资贴现需隐藏还款方式 
	hiddenTradeFinanc();
};

//-------------------利率种类下拉框相应方法-----------------------
function ir_typeChange(){
	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
	if(ir_accord_type=="03" || ir_accord_type=="01" || ir_accord_type=="04" || ir_accord_type==""){
       return;
    }
	var llyjfs = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();//利率依据方式
    var rateType = IqpLoanApp.IqpLoanAppSub.ir_type._getValue();//利率种类
    var bz = IqpLoanApp.apply_cur_type._getValue();//币种
    if(bz=="" || bz == null){
         alert("请先选择【申请币种】！");
         IqpLoanApp.apply_cur_type._obj.element.focus();
         return;
    }
    if(llyjfs=="" || llyjfs == null){
        alert("请先选择【利率依据方式】！");
        IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj.element.focus();
        return;
   }
    if(rateType == null || rateType == ""){
        
		alert("请先选择【利率种类】");
		return;
    }
    
    if(rateType == "13" ){
        var term = "6"
    	getRate4IrTypeChange(term);
    }else if(rateType == "14"){
    	var term = "12"
        getRate4IrTypeChange(term);
    }else{
    	getLiborRate(bz,rateType);
    }
};
function getRate4IrTypeChange(term){
 	var currType = IqpLoanApp.apply_cur_type._getValue();//币种
	var prdId = IqpLoanApp.prd_id._getValue();//业务品种
	var termType = "002";//月
	
	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
	if(currType == null || currType == ""){
		alert("请选择币种！");
		IqpLoanApp.apply_cur_type._obj.element.focus();
		return;
	}
	if(termType == null || termType == ""){
		alert("请选择期限类型！");
		IqpLoanApp.IqpLoanAppSub.term_type._obj.element.focus();
		return;
	}
	if(term == null || term == ""){
		IqpLoanApp.IqpLoanAppSub.apply_term._obj.element.focus();
		//alert("请录入期限！");
		return;
	}
	if(term == "0"){
		if("700021" != prdId && "700020" != prdId){
			alert("期限不能为0");
			IqpLoanApp.IqpLoanAppSub.apply_term._setValue("");
			return;
		}
	}
	var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
	if(prdId != null && prdId != ""){
		var url = '<emp:url action="getRate.do"/>'+param;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				var rate = jsonstr.rate;
				var code = jsonstr.code;
				if(flag == "success"){
					IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(rate);
					ruling_mounth._setValue(parseFloat(rate)/12);
					getReality_ir_y();
					getRelYM();
					getOverdueRateY();
					getDefaultRateY();
				}else {
					alert(msg);
					IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
};
//-------------------年利率计算月利率-----------------------
function reality_ir_yChange(){
	//var reality_ir_y_Value = IqpLoanApp.IqpLoanAppSub.reality_ir_y._getValue();
	var reality_ir_y_Value = IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj.element.value;
	var yll = parseFloat(reality_ir_y_Value)/1200;
	reality_mounth._setValue(yll);
};
//-------------------通过基准利率（年）获得执行利率（年）-----------------------
function getReality_ir_y(){
	var ir_y = IqpLoanApp.IqpLoanAppSub.ruling_ir._getValue();
	IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(ir_y);
	reality_mounth._setValue(parseFloat(ir_y)/12);	
}; 

//-------------------判断是否为银票贴现，影藏所有利息要素-----------------------
function hiddenIr(){
	var prd = IqpLoanApp.prd_id._getValue();
	if(prd == "300021" || prd == "300020"){
		 /** 显示控制 */
		 IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj._renderHidden(true);//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._setValue("");//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
		ruling_mounth._setValue(""); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
		reality_mounth._setValue(""); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("");//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("");//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("");//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数

		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj._renderRequired(false);//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数
	}
};
//-----------------表外业务需要影藏利息要素-----------------------
function hiddenBWIr(){
	var prd = IqpLoanApp.prd_id._getValue();
	var fin_type = '${context.IqpLoanApp.fin_type}';//保理类型
	/** 400020(外汇保函)、400021(境内保函)、700020(信用证)、700021(进口开证) 500032(提货担保)、400022(贷款承诺)、400024(贷款意向)、400023(贷款证明)、200024(银行承兑汇票) */
	if(prd == "400020" || prd == "400021" || prd == "700020" || prd == "700021" || prd == "500032"
		 || prd == "400022" || prd == "400024" || prd == "400023" || prd == "200024" || (prd == "800021"&&fin_type=='2')){
		 /** 显示控制 */
		 IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj._renderHidden(true);//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderHidden(true);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(false); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._setValue("");//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._setValue("");//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._setValue(""); //基准利率（年）
		ruling_mounth._setValue(""); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue(""); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue(""); //违约利率（年）
		//IqpLoanApp.IqpLoanAppSub.pad_rate_y._setValue("");//垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._setValue(""); //执行利率（年）
		reality_mounth._setValue(""); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue("");//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("");//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("");//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._setValue("");//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("");//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue("");//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._setValue("");//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("");//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._setValue("");//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._setValue("");//违约利率浮动点数

		/** 必输控制 */
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj._renderRequired(false);//利率依据方式
		IqpLoanApp.IqpLoanAppSub.ir_type._obj._renderRequired(false);//利率种类
		IqpLoanApp.IqpLoanAppSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		IqpLoanApp.IqpLoanAppSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(true); //垫款利率（年）
		IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		IqpLoanApp.IqpLoanAppSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		IqpLoanApp.IqpLoanAppSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		IqpLoanApp.IqpLoanAppSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		IqpLoanApp.IqpLoanAppSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		IqpLoanApp.IqpLoanAppSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		IqpLoanApp.IqpLoanAppSub.default_point._obj._renderRequired(false);//违约利率浮动点数

        /**隐藏还款方式信息、还款方式策略信息 */
		hiddenRepay();  
 	} 
	hiddenTradeFinanc();
};
//贸易融资贴现品种 隐藏还款方式信息、还款方式策略信息
function hiddenTradeFinanc(){
	var prd = IqpLoanApp.prd_id._getValue();
	/** 500027(远期信用证项下汇票贴现 )、500028(延期信用证项下应收款买入 )、500029(福费廷)*/
	if(prd == "500027" || prd == "500028" || prd == "500029"){
		hiddenRepay();
	}
};
//贸易融资贴现品种 利率依据方式为牌告利率
function setIrAccordType(){
	var prd = IqpLoanApp.prd_id._getValue();
	/** 500027(远期信用证项下汇票贴现 )、500028(延期信用证项下应收款买入 )、500029(福费廷)*/
	if(prd == "500027" || prd == "500028" || prd == "500029"){
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._setValue("02");
		IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj._renderReadonly(true);
		ir_accord_typeChange("init");
	}
};

function hiddenRepay(){
	document.getElementById('returnType').style.display="none"; 
	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(false);
	IqpLoanApp.IqpLoanAppSub.repay_type._obj._renderRequired(false);
	IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj._renderRequired(false);  
	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(false);
	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(false);
	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(false);
	//IqpLoanApp.IqpLoanAppSub.fir_repay_date._obj._renderRequired(false);
	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(false);  
	IqpLoanApp.IqpLoanAppSub.fir_repay_date._obj._renderRequired(false);  
	IqpLoanApp.IqpLoanAppSub.repay_mode_type._obj._renderRequired(false);  
};

function getRepayType(data){
	var repay_mode_id = data.repay_mode_id._getValue();
	var repay_mode_type = data.repay_mode_type._getValue();
	var is_instm = data.is_instm._getValue();
	
	var is_term;
	if(is_instm == "Y"){
		is_term = "1";
	}else if(is_instm == "N"){
		is_term = "2";
	}
	IqpLoanApp.IqpLoanAppSub.repay_type._setValue(repay_mode_id);
	IqpLoanApp.IqpLoanAppSub.repay_type_displayname._setValue(data.repay_mode_dec._getValue());
	IqpLoanApp.IqpLoanAppSub.is_term._setValue(is_term);
	IqpLoanApp.IqpLoanAppSub.repay_mode_type._setValue(repay_mode_type);
	checkFromRepayType(repay_mode_type);
	var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?repay_mode_id='+repay_mode_id;
	url = EMPTools.encodeURI(url);
	//PrdRepayPlanList._obj.ajaxQuery(url,null);
};
//用于初始化还款方式，保理专用（HS141110017_保理业务改造）  add by zhaozq 20150202 start
function getRepayTypeForBl(repay_mode_id,repay_mode_type,is_instm,repay_mode_dec){
	var is_term;
	if(is_instm == "Y"){
		is_term = "1";
	}else if(is_instm == "N"){
		is_term = "2";
	}
	IqpLoanApp.IqpLoanAppSub.repay_type._setValue(repay_mode_id);
	IqpLoanApp.IqpLoanAppSub.repay_type_displayname._setValue(repay_mode_dec);
	IqpLoanApp.IqpLoanAppSub.is_term._setValue(is_term);
	IqpLoanApp.IqpLoanAppSub.repay_mode_type._setValue(repay_mode_type);
	checkFromRepayType(repay_mode_type);
	var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?repay_mode_id='+repay_mode_id;
	url = EMPTools.encodeURI(url);
	//PrdRepayPlanList._obj.ajaxQuery(url,null);
};
//用于初始化还款方式，保理专用（HS141110017_保理业务改造）  add by zhaozq 20150202 end
//通过还款方式判断还款方式信息 
function checkFromRepayType(repay_mode_type){
    if(repay_mode_type == "05"){//还款方式种类为利随本清	
    	var options = IqpLoanApp.IqpLoanAppSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	IqpLoanApp.IqpLoanAppSub.interest_term._setValue("4");
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._setValue("M");

    	
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(true);

    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._setValue("1");
    	IqpLoanApp.IqpLoanAppSub.repay_date._setValue("21");
    }else if(repay_mode_type == "04" || repay_mode_type == "00"){//还款方式种类为按期结息 自由还款法
    	removeInterestTerm();

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._setValue("1");
    	IqpLoanApp.IqpLoanAppSub.repay_date._setValue("21");
    }else if(repay_mode_type == "01" || repay_mode_type == "03"){//还款方式种类为 等额本息 等额本金
    	removeInterestTerm();

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

		IqpLoanApp.IqpLoanAppSub.repay_date._setValue("21");
    	/* var date = '${context.OPENDAY}';
    	var day = date.substr(8,2);
    	if(parseFloat(day)>=28){
    		IqpLoanApp.IqpLoanAppSub.repay_date._setValue("27");
        }else{
        	IqpLoanApp.IqpLoanAppSub.repay_date._setValue(day);
        } */
    	IqpLoanApp.IqpLoanAppSub.interest_term._setValue("2");
    	IqpLoanApp.IqpLoanAppSub.repay_term._setValue("M");
    	
    }else if(repay_mode_type == "07" || repay_mode_type == "08"){//还款方式种类为弹性还款 气球贷
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(false);  

    }else if(repay_mode_type == "09"){//还款方式种类为一次付息到期还本
    	var options = IqpLoanApp.IqpLoanAppSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	IqpLoanApp.IqpLoanAppSub.interest_term._setValue("5");
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(false);
    }
    checkRepayDate();
};

function checkRepayDate(repay_mode_type){
	if(repay_mode_type == "05"){//还款方式种类为利随本清	
    	var options = IqpLoanApp.IqpLoanAppSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(true);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(true);

    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

    }else if(repay_mode_type == "04" || repay_mode_type == "00"){//还款方式种类为按期结息 自由还款法
    	removeInterestTerm();

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

    }else if(repay_mode_type == "01" || repay_mode_type == "03"){//还款方式种类为 等额本息 等额本金
    	removeInterestTerm();

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);

    }else if(repay_mode_type == "07" || repay_mode_type == "08"){//还款方式种类为弹性还款 气球贷
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(false);  

    }else if(repay_mode_type == "09"){//还款方式种类为一次付息到期还本
    	var options = IqpLoanApp.IqpLoanAppSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderHidden(false);
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderHidden(false);

    	IqpLoanApp.IqpLoanAppSub.repay_term._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderRequired(true);   
    	IqpLoanApp.IqpLoanAppSub.is_term._obj._renderRequired(true);

    	IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(false);
    	IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(false);
    }
	var cusId = IqpLoanApp.cus_id._getValue();
	var handleSuccess = function(o) {
		EMPTools.unmask();
		if (o.responseText !== undefined) {
			try {
				var jsonstr = eval("(" + o.responseText + ")");
			} catch (e) {
				alert("Parse jsonstr define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			if("1" == flag){
				IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(false);
			}else {
				IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);
			}
		}
	};
	var handleFailure = function(o) {
		alert(o.responseText);
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var url = '<emp:url action="queryCusTypeByCusId.do"/>?cus_id='+cusId;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
}

function removeInterestTerm(){
	var options = IqpLoanApp.IqpLoanAppSub.interest_term._obj.element.options;
	for(var j=options.length-1;j>=0;j--){
        if(options[j].value=="4" || options[j].value=="5"){
        	options.remove(j);
        }
	}
};

function setRepayType(){
	var repay_mode_id = IqpLoanApp.IqpLoanAppSub.repay_type._getValue();
	if(repay_mode_id==""){
		return false;
	}
	var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?repay_mode_id='+repay_mode_id;
	url = EMPTools.encodeURI(url);
	//PrdRepayPlanList._obj.ajaxQuery(url,null); 
};
function setRepayTerm(){
	var prd = IqpLoanApp.prd_id._getValue(); 
	/**表外业务时，还款方式信息、还款方式策略信息需要隐藏,不需执行下列代码*/
	/** 400020(外汇保函)、400021(境内保函)、700020(信用证)、700021(进口开证) 500032(提货担保)、400022(贷款承诺)、400024(贷款意向)、400023(贷款证明)、200024(银行承兑汇票) */
	if(prd != "400020" && prd != "400021" && prd != "700020" && prd != "700021" && prd != "500032" && prd != "400022" && prd != "400024" && prd != "400023" && prd != "200024"){
	   var term = IqpLoanApp.IqpLoanAppSub.interest_term._getValue();
	   var term = IqpLoanApp.IqpLoanAppSub.interest_term._getValue();
       //计息周期为按年  季 月
       if(term == "0"){
    	   IqpLoanApp.IqpLoanAppSub.repay_term._setValue("Y");
       }else if(term == "1"){
    	   IqpLoanApp.IqpLoanAppSub.repay_term._setValue("Q");
       }else if(term == "2"){
    	   IqpLoanApp.IqpLoanAppSub.repay_term._setValue("M");
       }
  }
};

function getNetMagInfo(data){
	IqpLoanApp.belong_net._setValue(data.serno._getValue()); 
};   

function getContMsg(data){
	IqpLoanApp.limit_cont_no._setValue(data.cont_no._getValue());
};
function cleanSpace(){
    var repay_term = IqpLoanApp.IqpLoanAppSub.repay_term._getValue();
    if(repay_term == "Q"){
    	IqpLoanApp.IqpLoanAppSub.repay_space._setValue("");
    }
};


function checkTerm(){
	var repay_term = IqpLoanApp.IqpLoanAppSub.repay_term._getValue();
	var repay_space = IqpLoanApp.IqpLoanAppSub.repay_space._getValue();
    if(repay_term == "Q"){
        if(repay_space > 4){
            alert("还款间隔应小于等于4"); 
            IqpLoanApp.IqpLoanAppSub.repay_space._setValue("");  
        }    
    }
};

function is_limit_cont_pay(){
	var prd_id = IqpLoanApp.prd_id._getValue();
    //提货担保   500032  同业代付 500020 国际保理 800020 信托收据贷款 500021 打包贷款 500022 出口订单融资 500023 出口商业发票融资 500024 信用证项下出口押汇 500026 信用证 700020 国内保理 800021 短期信保融资 500031 远期信用证项下汇票贴现 500027 延期信用证项下应收款买入 500028 福费廷 500029 出口托收贷款 500025 进口开证  400020 外汇保函 700021
    if(prd_id == "500020" || prd_id == "800020"  || prd_id == "500032" || prd_id == "500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500026" || prd_id == "700020" || prd_id == "800021" || prd_id == "500031" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500025"  || prd_id == "400020"  || prd_id == "700021" ){
    	IqpLoanApp.is_limit_cont_pay._obj._renderRequired(true);
    	IqpLoanApp.is_limit_cont_pay._obj._renderHidden(false);
    }else{
    	IqpLoanApp.is_limit_cont_pay._obj._renderRequired(false);
    	IqpLoanApp.is_limit_cont_pay._obj._renderHidden(true);    
    }
};
//个人消费性贷款【贷款投向】隐藏且非必输，其他均显示且必输
function is_person_consume(){
	var prd_id = IqpLoanApp.prd_id._getValue();
    //住房贷款 100028 汽车贷款 100029 综合消费贷款 100030 黄金质押贷款 100031 珠宝质押贷款 100032 商用房贷款 100033 公积金贷款 100072  2014-10-09 modified by FCL 商用房属经营性贷款，贷款投向必录项 
    /* modified by yangzy 2014/12/20 增加生活贷A款、生活贷B款、生活贷C款、生活贷D款  start */
    if(prd_id == "100028" || prd_id == "100029" || prd_id == "100030" || prd_id == "100031" || prd_id == "100032" || prd_id == "100072" || prd_id == "100080" || prd_id == "100081" || prd_id == "100082" || prd_id == "100083" ){
    	IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderRequired(false);
    	IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderHidden(true);
    }else{
    	IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderRequired(true);
    	IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderHidden(false);     
    } 
    /* modified by yangzy 2014/12/20 增加生活贷A款、生活贷B款、生活贷C款、生活贷D款 end */
};

//表外业务以下字段不显示
function is_off_busi(){
	var menuId = '${context.menuId}';
	var prd_id = IqpLoanApp.prd_id._getValue();
    //    境内保函	400021  信用证	700020      进口开证	700021      贷款承诺	400022      信贷证明	400023      贷款意向	400024      外汇保函	400020      提货担保	500032        银行承兑汇票	200024
    if( prd_id == "400021" || prd_id == "700020" || prd_id == "700021" || prd_id == "400022" || prd_id == "400023" || prd_id == "400024" || prd_id == "400020" || prd_id == "500032" || prd_id == "200024" ){
    	IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderRequired(false);//是否无间贷
    	IqpLoanApp.IqpLoanAppSub.loan_form._obj._renderRequired(false);//贷款形式
    	IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderRequired(false);//贷款性质
    	
    	IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderHidden(true);//是否无间贷
    	IqpLoanApp.IqpLoanAppSub.loan_form._obj._renderHidden(true);//贷款形式
    	IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderHidden(true);//贷款性质
        //如果是信托贷款业务
    	if(menuId != "trustqueryIqpLoanApp"){
    		IqpLoanApp.is_promissory_note._obj._renderRequired(false);//是否承诺函下
        	IqpLoanApp.is_trust_loan._obj._renderRequired(false);//是否信托贷款
        	IqpLoanApp.is_promissory_note._obj._renderHidden(true);//是否承诺函下
        	IqpLoanApp.is_trust_loan._obj._renderHidden(true);//是否信托贷款
    	}
    	var is_trust_loan = IqpLoanApp.is_trust_loan._getValue();
    	if(is_trust_loan == "1"){
    		IqpLoanApp.is_promissory_note._obj._renderRequired(true);//是否承诺函下
        	IqpLoanApp.is_trust_loan._obj._renderRequired(true);//是否信托贷款
        	IqpLoanApp.is_promissory_note._obj._renderHidden(false);//是否承诺函下
        	IqpLoanApp.is_trust_loan._obj._renderHidden(false);//是否信托贷款
        }
    	//隐藏支付信息
    	document.getElementById('payInfo').style.display="none";
    	IqpLoanApp.IqpLoanAppSub.conf_pay_type._obj._renderRequired(false);
    	IqpLoanApp.IqpLoanAppSub.conf_pay_type._setValue("");
    	IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderRequired(false);
    	IqpLoanApp.IqpLoanAppSub.pay_type._setValue("");
    	//境内保函、银票确定支付方式，自主支付
    	if( prd_id == "400021" || prd_id == "200024" ){
    		IqpLoanApp.IqpLoanAppSub.conf_pay_type._setValue("1");
    		IqpLoanApp.IqpLoanAppSub.pay_type._setValue("0");
    	}
    }else {
    	//IqpLoanApp.IqpLoanAppSub.conf_pay_type._setValue("1");
    }
};

//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示
function is_show_pad_rate(){
	var prd_id = IqpLoanApp.prd_id._getValue();
	//贷款承诺	400022      信贷证明	400023      贷款意向	400024
    if(prd_id == "400022" || prd_id == "400023" || prd_id == "400024"){
    	//隐藏利率信息
    	document.getElementById('rateInfo').style.display="none";
    	IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderRequired(false);
    	IqpLoanApp.IqpLoanAppSub.pad_rate_y._obj._renderHidden(true);

    } 
};
function getLimitAccNo(){
	var limit_acc_no = IqpLoanApp.limit_acc_no._getValue();  
	if(limit_acc_no == null || limit_acc_no == ""){
       alert("请先选择授信协议");
       return;
	}
	var url = "<emp:url action='viewLmtAgrInfo.do'/>?showButton=N&op=view&agr_no="+limit_acc_no;
	url=EMPTools.encodeURI(url);  
	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
};
function getLimitCreditNO(){
	var limit_credit_no = IqpLoanApp.limit_credit_no._getValue();  
	if(limit_credit_no == null || limit_credit_no == ""){
	       alert("请先选择授信协议");
	       return;
		}
	var url = "<emp:url action='viewLmtAgrInfo.do'/>?showButton=N&op=view&agr_no="+limit_credit_no;
	url=EMPTools.encodeURI(url);
	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
};

//承诺函Pop框选取方法
function getPromissory(data){
	IqpLoanApp.promissory_note._setValue(data.cont_no._getValue());
};
/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
function getBizType(){
	var flg = '${context.flg}';
    if(flg == "trust"){
    	IqpLoanApp.is_trust_loan._setValue("1");
    	IqpLoanApp.is_trust_loan._obj._renderReadonly(true);
    	IqpLoanApp.trust_company._obj._renderRequired(true);
    	IqpLoanApp.trust_company._obj._renderHidden(false);
    	IqpLoanApp.trust_pro_name._obj._renderRequired(true);
    	IqpLoanApp.trust_pro_name._obj._renderHidden(false);
    }else{
    	IqpLoanApp.is_trust_loan._setValue("2");
    	IqpLoanApp.is_trust_loan._obj._renderReadonly(true);
    	IqpLoanApp.trust_company._obj._renderRequired(false);
    	IqpLoanApp.trust_company._obj._renderHidden(true);
    	IqpLoanApp.trust_pro_name._obj._renderRequired(false);
    	IqpLoanApp.trust_pro_name._obj._renderHidden(true);
   }
}
/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
function getFlg(){
	var flg = '${context.flg}';
	if(flg == "csgn"){
		//定向资管委托贷款
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._setValue("08");
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderReadonly(true);
	}else if(flg == "csgnClaimInvest"){
		//委托债权投资
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._setValue("14");
		IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderReadonly(true);
	}else{
		var options = IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj.element.options;
		for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="08" || options[i].value=="14"){
				options.remove(i);
			}
		}
	}
}

function readonly4ReturnType(){
 	  IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
	  IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
	  IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);
}
function readonly4RateInfo(){
	IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(true);
};

//通过客户ID异步查询客户所属条线
function getBizLineByCusId(){
	var cusId = IqpLoanApp.cus_id._getValue();
	var prd_id = IqpLoanApp.prd_id._getValue();
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			var bizline = jsonstr.bizline;
			if(flag == "success"){
				if(bizline == "BL100" || bizline == "BL200"){
					if(prd_id == "300020" || prd_id == "300021"){
						IqpLoanApp.IqpDiscApp.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE"/>';
					}else{
						IqpLoanApp.IqpLoanAppSub.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE"/>';
					}
					IqpLoanApp.IqpLoanAppSub.loan_belong4._setValue("0000");
					IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderHidden(true);
					IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderRequired(false);
				}else{
					if(prd_id == "300020" || prd_id == "300021"){
						IqpLoanApp.IqpDiscApp.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_PER_POSITIONTYPE"/>';
					}else{
						IqpLoanApp.IqpLoanAppSub.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_PER_POSITIONTYPE"/>';
					}
					IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderHidden(false);
					IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderRequired(true);
				}
				//是否收取印花税 小微条线发起的业务申请，【是否收取印花税】改为【否】并置灰，【印花税收取方式】字段隐藏
				//对公、个人条线发起的业务申请，【是否收取印花税】改为【是】并置灰，【印花税收取方式】不做变动
				//对公客户包含品种有：流贷业务、银团业务、固贷业务、定向资管委托贷款、委托债权投资、贸易融资、委托贷款，其他表内外业务均不收；
				//个人客户包含品种：表外业务：个人委托贷款；经营性：渔贷通、购置机械设备小额担保按揭贷款、
                //经营性贷款、农户生产经营性贷款、农村创业青年小额贷款、循环贷款、建筑工程项目经理个人经营性贷款；
                //消费性：汽车贷款、综合消费贷款、黄金质押贷款、住房贷款、珠宝质押贷款、商用房贷款、存单质押贷款、
                //动产质押贷款、定向资管委托贷款、委托债权投资
				if(bizline == "BL100" ){
					if( prd_id == "100063" ||'${context.menuId}' =="csgnqueryIqpLoanApp" || '${context.menuId}' =="csgnqueryIqpLoanAppHistory" || '${context.menuId}' =="csgnClaimInvestqueryIqpLoanApp" || '${context.menuId}' =="csgnClaimInvestIqpLoanAppHis" || '${context.supcatalog}' == "PRD20120802665" || '${context.supcatalog}' == "PRD20120802670" || '${context.supcatalog}' == "PRD20120802671" || '${context.supcatalog}' == "PRD20120802669" ){
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("1");
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
						/*modified by wangj XD150407026_贸易融资汇率问题 begin*/
						if(prd_id == "500029"||prd_id == "500028"||prd_id == "500027"){//福费廷不收取印花税
							IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
							IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
							IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
							IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
							IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
		                    IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
						}
						/*modified by wangj XD150407026_贸易融资汇率问题 end*/
					}else{
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
						IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
						IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
	                    IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
				    }
				}else if(bizline == "BL300"){
                    if(prd_id == "100065" ||'${context.menuId}' =="csgnqueryIqpLoanApp" || '${context.menuId}' =="csgnqueryIqpLoanAppHistory" || '${context.menuId}' =="csgnClaimInvestqueryIqpLoanApp" || '${context.menuId}' =="csgnClaimInvestIqpLoanAppHis" || '${context.supcatalog}' == "PRD20120802658" || '${context.supcatalog}' == "PRD20120802659"){
                    	IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("1");
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
                    }else{
                    	IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
						IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
						IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
						IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
	                    IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
                    }
				}else if(bizline == "BL200"){
					IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
					IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
					IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
					IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
					IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
                    IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
                }
                //资产业务-信托贷款  【是否收取印花税】改为默认为否，不可修改。
                if('${context.menuId}' =="trustqueryIqpLoanApp"){
                	IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
					IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(true);
					IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(false);
					IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
					IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
                    IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
                }
      }else { 
        alert(msg);
      }
    }
  };
  var handleFailure = function(o){
    alert("异步回调失败！");  
  };
  var callback = {
    success:handleSuccess,
    failure:handleFailure
  };
  var url = '<emp:url action="getBizLineByCusId.do"/>&cusid='+cusId;
  var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};

//通过客户ID异步查询客户所属条线---贴现
function getBizLineByCusIdDisc(){
	var cusId = IqpLoanApp.cus_id._getValue();
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			var bizline = jsonstr.bizline;
			if(flag == "success"){
				if(bizline == "BL100" || bizline == "BL200"){
					IqpLoanApp.IqpDiscApp.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE"/>';
				}else{
					IqpLoanApp.IqpDiscApp.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_PER_POSITIONTYPE"/>';
				}
			}else { 
				alert(msg);
			}
		}
	};
	var handleFailure = function(o){
		alert("异步回调失败！");	
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};
	var url = '<emp:url action="getBizLineByCusId.do"/>&cusid='+cusId;
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};
//-------------通过账号获取在我行的账号信息------------
function getAcctNo(){
		 var acctNo = IqpLoanApp.IqpDiscApp.agent_acct_no._getValue();
	        if(acctNo == null || acctNo == ""){
				alert("请先输入账号信息！");
				return;
	        }
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var retMsg = jsonstr.retMsg;
					var ACCT_NO = jsonstr.BODY.ACCT_NO;
					var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
					var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
					var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME;
					var ORG_NO = jsonstr.BODY.ORG_NO;
					if(flag == "success"){
						IqpLoanApp.IqpDiscApp.agent_acct_name._setValue(ACCT_NAME);
						IqpLoanApp.IqpDiscApp.agent_org_no._setValue(OPEN_ACCT_BRANCH_ID);
						IqpLoanApp.IqpDiscApp.agent_org_name._setValue(OPEN_ACCT_BRANCH_NAME);
					}else {
						alert(retMsg);
						IqpLoanApp.IqpDiscApp.agent_acct_no._setValue("");
						IqpLoanApp.IqpDiscApp.agent_acct_name._setValue("");
						IqpLoanApp.IqpDiscApp.agent_org_no._setValue("");
						IqpLoanApp.IqpDiscApp.agent_org_name._setValue("");
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = '<emp:url action="clientTrade4Esb.do"/>?acct_no='+acctNo+'&service_code=11003000007&sence_code=17';	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};

    function checkAgentDisc(){
          var is_agent_disc = IqpLoanApp.IqpDiscApp.is_agent_disc._getValue();
          if(is_agent_disc == "1"){
        	  IqpLoanApp.IqpDiscApp.agent_acct_no._obj._renderHidden(false);
        	  IqpLoanApp.IqpDiscApp.agent_acct_name._obj._renderHidden(false);
        	  IqpLoanApp.IqpDiscApp.agent_acct_no._obj._renderRequired(true);
        	  IqpLoanApp.IqpDiscApp.agent_acct_name._obj._renderRequired(true);
        	  
        	  IqpLoanApp.IqpDiscApp.agent_org_no._obj._renderHidden(false);
        	  IqpLoanApp.IqpDiscApp.agent_org_name._obj._renderHidden(false);
        	  IqpLoanApp.IqpDiscApp.agent_org_no._obj._renderRequired(true);
        	  IqpLoanApp.IqpDiscApp.agent_org_name._obj._renderRequired(true);
          }else if(is_agent_disc == "2"){
        	  IqpLoanApp.IqpDiscApp.agent_acct_no._obj._renderHidden(true);
        	  IqpLoanApp.IqpDiscApp.agent_acct_name._obj._renderHidden(true);
        	  IqpLoanApp.IqpDiscApp.agent_acct_no._obj._renderRequired(false);
        	  IqpLoanApp.IqpDiscApp.agent_acct_name._obj._renderRequired(false);
        	  IqpLoanApp.IqpDiscApp.agent_acct_no._setValue("");
        	  IqpLoanApp.IqpDiscApp.agent_acct_name._setValue("");
        	  
        	  IqpLoanApp.IqpDiscApp.agent_org_no._obj._renderHidden(true);
        	  IqpLoanApp.IqpDiscApp.agent_org_name._obj._renderHidden(true);
        	  IqpLoanApp.IqpDiscApp.agent_org_no._obj._renderRequired(false);
        	  IqpLoanApp.IqpDiscApp.agent_org_name._obj._renderRequired(false);
        	  IqpLoanApp.IqpDiscApp.agent_org_no._setValue("");
        	  IqpLoanApp.IqpDiscApp.agent_org_name._setValue("");
          }
    };
    function doClean(){ 
    	IqpLoanApp.IqpDiscApp.is_elec_bill._setValue("");
    	IqpLoanApp.IqpDiscApp.disc_type._setValue("");
    	IqpLoanApp.assure_main._setValue("");
    	IqpLoanApp.assure_main_details._setValue("");
    	IqpLoanApp.security_rate._setValue("");
    	//IqpLoanApp.same_security_amt._setValue("");
    	IqpLoanApp.limit_ind._setValue("");
    	IqpLoanApp.limit_acc_no._setValue("");
    	IqpLoanApp.limit_credit_no._setValue("");
    	IqpLoanApp.IqpDiscApp.is_agent_disc._setValue("");
    	IqpLoanApp.IqpDiscApp.agent_acct_no._setValue("");
    	IqpLoanApp.IqpDiscApp.agent_acct_name._setValue("");
    	IqpLoanApp.IqpDiscApp.disc_sett_acct_no._setValue("");
    	IqpLoanApp.IqpDiscApp.disc_sett_acct_name._setValue("");
    	IqpLoanApp.IqpDiscApp.pvp_pact_cond_memo._setValue("");
    	IqpLoanApp.IqpDiscApp.five_classfiy._setValue("");
    	IqpLoanApp.IqpDiscApp.spe_loan_type._setValue("");
    	IqpLoanApp.IqpDiscApp.limit_useed_type._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_use_type._setValue("");
    	IqpLoanApp.IqpDiscApp.com_up_indtify._setValue("");
    	IqpLoanApp.IqpDiscApp.agriculture_type_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.ensure_project_loan_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_type_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.estate_adjust_type_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.strategy_new_loan_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.new_prd_loan_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_direction_displayname._setValue("");
    	
    	IqpLoanApp.IqpDiscApp.loan_belong1_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_belong2_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_belong3_displayname._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_type._setValue("");
    	IqpLoanApp.IqpDiscApp.agriculture_type._setValue("");
    	IqpLoanApp.IqpDiscApp.ensure_project_loan._setValue("");
    	IqpLoanApp.IqpDiscApp.estate_adjust_type._setValue("");
    	IqpLoanApp.IqpDiscApp.strategy_new_loan._setValue("");
    	IqpLoanApp.IqpDiscApp.new_prd_loan._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_direction._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_belong1._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_belong2._setValue("");
    	IqpLoanApp.IqpDiscApp.loan_belong3._setValue("");
    	IqpLoanApp.IqpDiscApp.repay_src_des._setValue("");
    	IqpLoanApp.IqpDiscApp.biz_sour._setValue("");
    	IqpLoanApp.IqpDiscApp.sour_memo._setValue("");
    	IqpLoanApp.manager_br_id_displayname._setValue("");
    	IqpLoanApp.manager_br_id._setValue("");
    };
	function getOrgNo(data){
		IqpLoanApp.IqpDiscApp.agent_org_no._setValue(data.bank_no._getValue());
		IqpLoanApp.IqpDiscApp.agent_org_name._setValue(data.bank_name._getValue());
	};
    //是否收取印花税
	function changeStamp(){
        var is_collect_stamp = IqpLoanApp.IqpLoanAppSub.is_collect_stamp._getValue();
        if(is_collect_stamp == "1"){
        	IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(true);
        	IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(false);
        	checkstampCollectMode();
        }else if(is_collect_stamp == "2"){
        	IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
        	IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
        	IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
        }
    };

    function checkstampCollectMode(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款 
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("4");
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderReadonly(true);
		}
		if(prd_id == '700020' || prd_id == '700021' || prd_id == '400020' || prd_id == '500032'){//信用证业务,外汇保函，提货担保隐藏
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
		}
	};

    //是否收取印花税
	function changeStampForDis(){
        var is_collect_stamp = IqpLoanApp.IqpDiscApp.is_collect_stamp._getValue();
        alert(is_collect_stamp);
        if(is_collect_stamp == "1"){
        	IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderRequired(true);
        	IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderHidden(false);
        	checkstampCollectModeForDis();
        }else if(is_collect_stamp == "2"){
        	IqpLoanApp.IqpDiscApp.stamp_collect_mode._setValue("");
        	IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderRequired(false);
        	IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderHidden(true);
        }
    };

	function checkstampCollectModeForDis(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._setValue("2");
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderReadonly(true);
		}
	}
	//贷款归属的显示还是隐藏(2014-04-15需求变更)
	function showLoanBelong(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		//除(贴现，表外，贸易融资业务)外均展示
		if(prd_id == "100063" || prd_id =="100065" || prd_id == "200024" || prd_id == "300020" || prd_id == "300021" || prd_id == "400020" || prd_id == "400021" || prd_id == "400022" || prd_id == "400023" || prd_id == "400024" || prd_id == "500020" || prd_id == "500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026"  || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021" || prd_id == "800020" || prd_id == "800021"){
			IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderRequired(false);
			
			IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderHidden(true);
	    }else{
	    	IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderRequired(true);
			IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderRequired(true);
			IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderRequired(true);
			
			IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderHidden(false);
		}
    }
    //隐藏利率依据方式 （不计息）
    function removeIrAccordType(){
        var options = IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj.element.options;
    	for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="03"){
				options.remove(i);
			}
		}
    }
    //查询半年日均
    function getCusTotalAmt(){
    	var cus_id = IqpLoanApp.cus_id._getValue();
    	var prd_id = IqpLoanApp.prd_id._getValue();
        if(cus_id == null || cus_id == ""){
			alert("客户码为空!");
			return;
        }
        if(prd_id == null || prd_id == ""){
			alert("产品编号为空!");
			return;
        }
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var mes = jsonstr.mes;
				if(flag == "success"){
					//alert("查询成功!");
					var totalAmt = jsonstr.totalAmt;
			    	IqpLoanApp.cus_total_amt._setValue(totalAmt);
			    }else if(flag == "error"){
			       alert(mes);
			    }
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="getCusHalfYearAmt.do"/>?cus_id='+cus_id+'&prd_id='+prd_id;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    };
    //如果为贸易融资三种产品，则期限类型默认为日 
    function setTermType(){
    	var prd_id = IqpLoanApp.prd_id._getValue();
    	if(prd_id == "500029" || prd_id == "500027" || prd_id == "500028"){
    		IqpLoanApp.IqpLoanAppSub.term_type._setValue("003");
    		IqpLoanApp.IqpLoanAppSub.term_type._obj._renderReadonly(true);
        }
    };
    //是否节假日顺延,银承隐藏期限类型，期限
    function checkIsDelay(){
        var prd_id = IqpLoanApp.prd_id._getValue();
        //如果为银行承兑汇票、银票贴现、商票贴现
        if(prd_id == "200024" || prd_id == "300020" || prd_id == "300021"){
        	IqpLoanApp.IqpLoanAppSub.is_delay._obj._renderReadonly(true);   
        	IqpLoanApp.IqpLoanAppSub.is_delay._setValue("1");
        }else{
        	IqpLoanApp.IqpLoanAppSub.is_delay._obj._renderReadonly(true);
        	IqpLoanApp.IqpLoanAppSub.is_delay._setValue("2");
        }
        if(prd_id == "200024"){//如果为银行承兑汇票
        	IqpLoanApp.IqpLoanAppSub.term_type._obj._renderRequired(false);
        	IqpLoanApp.IqpLoanAppSub.apply_term._obj._renderRequired(false);
        	IqpLoanApp.IqpLoanAppSub.term_type._obj._renderHidden(true);
        	IqpLoanApp.IqpLoanAppSub.apply_term._obj._renderHidden(true);
        	IqpLoanApp.IqpLoanAppSub.term_type._setValue("003");
        	IqpLoanApp.IqpLoanAppSub.apply_term._setValue("1");
        }
    };
    //贸易融资表内业务支付方式
    function cleanPayType(){
    	var prd = IqpLoanApp.prd_id._getValue();
    	if(prd != "500020" && prd != "500021" && prd != "500022" && prd != "500023" && prd != "500024" && prd != "500025" && prd != "500026" && prd != "500027" && prd != "500028" && prd != "500029" && prd != "500031" && prd != "800020" && prd != "800021"){
    		var options = IqpLoanApp.IqpLoanAppSub.pay_type._obj.element.options;
    		for(var i=options.length-1;i>=0;i--){
    			if(options[i].value=="2"){
    				options.remove(i);
    			}
    		}
    	}
    };
	/**modified by lisj 2015-5-26 需求编号：XD150413030 关于无间贷业务申请改造 begin**/
    //无间贷，借新还旧 只读
    function readOnly4CloseLoan(){
    	var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
		var dkxs = IqpLoanApp.IqpLoanAppSub.loan_form._getValue();//贷款形式
		if(sfgjd == "1" || dkxs == "3"){
		/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 begin**/
			/*var cusId = IqpLoanApp.cus_id._getValue();
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					var bizline = jsonstr.bizline;
					if(flag == "success"){
						if(bizline!="BL300"){*/
							IqpLoanApp.is_rfu._obj._renderReadonly(true);//是否曾被拒绝" 	 
							IqpLoanApp.IqpLoanAppSub.loan_form._obj._renderReadonly(true); //贷款形式" 
							IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderReadonly(true); //贷款性质"
							//IqpLoanApp.assure_main._obj._renderReadonly(true); //担保方式" 
							IqpLoanApp.assure_main_details._obj._renderReadonly(true);//担保方式细分" 
							IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderReadonly(true); //是否无间贷"
							IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderReadonly(true); //偿还借据" 
							IqpLoanApp.is_promissory_note._obj._renderReadonly(true); //是否承诺函下" 
							IqpLoanApp.promissory_note._obj._renderReadonly(true); //承诺函" 
							IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderReadonly(true); //是否收取印花税" 
							IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderReadonly(true); //印花税收取方式" 
							IqpLoanApp.is_trust_loan._obj._renderReadonly(true); //是否信托贷款" 
							IqpLoanApp.trust_company._obj._renderReadonly(true); //信托公司" 
							/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
							IqpLoanApp.trust_pro_name._obj._renderReadonly(true); //信托项目名称
							/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
							IqpLoanApp.is_limit_cont_pay._obj._renderReadonly(true); //是否额度合同项下支用" 
							IqpLoanApp.limit_cont_no._obj._renderReadonly(true); //额度合同编号
							IqpLoanApp.rent_type._obj._renderReadonly(true); //租赁模式" 
							IqpLoanApp.belong_net._obj._renderReadonly(true);//所属网络" 

							IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);//支付方式
							IqpLoanApp.apply_cur_type._obj._renderReadonly(true);
							IqpLoanApp.apply_amount._obj._renderReadonly(true);
							IqpLoanApp.security_cur_type._obj._renderReadonly(true);
							IqpLoanApp.security_rate._obj._renderReadonly(true);

							IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.is_term._obj._renderReadonly(true);
							if(dkxs == "3"){
								
							IqpLoanApp.assure_main._obj._renderReadonly(true); //担保方式" 
							
							IqpLoanApp.IqpLoanAppSub.five_classfiy._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.spe_loan_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.limit_useed_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_use_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.com_up_indtify._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderReadonly(true);
							//IqpLoanApp.IqpLoanAppSub.loan_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.agriculture_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.estate_adjust_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.strategy_new_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.new_prd_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderReadonly(true);

							IqpLoanApp.manager_br_id_displayname._obj._renderReadonly(true);
							}
					/**	}else{
							IqpLoanApp.is_rfu._obj._renderReadonly(true);//是否曾被拒绝" 	 
							IqpLoanApp.IqpLoanAppSub.loan_form._obj._renderReadonly(true); //贷款形式" 
							IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderReadonly(true); //贷款性质"
							IqpLoanApp.assure_main._obj._renderReadonly(true); //担保方式" 
							IqpLoanApp.assure_main_details._obj._renderReadonly(true);//担保方式细分" 
							IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderReadonly(true); //是否无间贷"
							IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderReadonly(true); //偿还借据" 
							IqpLoanApp.is_promissory_note._obj._renderReadonly(true); //是否承诺函下" 
							IqpLoanApp.promissory_note._obj._renderReadonly(true); //承诺函" 
							IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderReadonly(true); //是否收取印花税" 
							IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderReadonly(true); //印花税收取方式" 
							IqpLoanApp.is_trust_loan._obj._renderReadonly(true); //是否信托贷款" 
							IqpLoanApp.trust_company._obj._renderReadonly(true); //信托公司" 
							/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
							/*IqpLoanApp.trust_pro_name._obj._renderReadonly(true); //信托项目名称 */
							/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
						/**	IqpLoanApp.is_limit_cont_pay._obj._renderReadonly(true); //是否额度合同项下支用" 
							IqpLoanApp.limit_cont_no._obj._renderReadonly(true); //额度合同编号
							IqpLoanApp.rent_type._obj._renderReadonly(true); //租赁模式" 
							IqpLoanApp.belong_net._obj._renderReadonly(true);//所属网络" 

							IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);//支付方式
							IqpLoanApp.apply_cur_type._obj._renderReadonly(true);
							IqpLoanApp.apply_amount._obj._renderReadonly(true);
							IqpLoanApp.security_cur_type._obj._renderReadonly(true);
							IqpLoanApp.security_rate._obj._renderReadonly(true);

							IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.repay_space._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.repay_date._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.is_term._obj._renderReadonly(true);
							
							IqpLoanApp.IqpLoanAppSub.five_classfiy._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.spe_loan_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.limit_useed_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_use_type._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.com_up_indtify._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.principal_loan_typ._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.agriculture_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.estate_adjust_type_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.strategy_new_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.new_prd_loan_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_direction_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname._obj._renderReadonly(true);
							IqpLoanApp.IqpLoanAppSub.loan_belong4._obj._renderReadonly(true);

							IqpLoanApp.manager_br_id_displayname._obj._renderReadonly(true);
						}
		            }else { 
		              alert(msg);
		            }
		    }
		  };
		  var handleFailure = function(o){
		    alert("异步回调失败！");  
		  };
		  var callback = {
		    success:handleSuccess,
		    failure:handleFailure
		  };
		  var url = '<emp:url action="getBizLineByCusId.do"/>&cusid='+cusId;
		  var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)*/
		  /**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 end**/
		}
    };
	/**modified by lisj 2015-5-26 需求编号：XD150413030 关于无间贷业务申请改造 end**/
    //反向计算利率浮动比
    //01':'议价利率依据', '02':'牌告利率依据', '03':'不计息', '04':'正常利率上浮动'
    function caculateOverdueRate(){
    	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		var reality_ir_y = IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj.element.value;//执行利率（年）
    		if(parseFloat(reality_ir_y)>=0){
    			var overdue_rate_y = IqpLoanApp.IqpLoanAppSub.overdue_rate_y._obj.element.value;//逾期利率（年）
    			var overdue_rate = parseFloat(overdue_rate_y)/parseFloat(reality_ir_y)-1;
    			IqpLoanApp.IqpLoanAppSub.overdue_rate._setValue(overdue_rate+"");
    			IqpLoanApp.IqpLoanAppSub.overdue_float_type._setValue("0");//加百分比
        	}else{
            	alert("请先输入执行利率!");
        		IqpLoanApp.IqpLoanAppSub.overdue_rate_y._setValue("");
            }
        }
    };
  //反向计算利率浮动比
    function caculateDefaultRate(){
    	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		var reality_ir_y = IqpLoanApp.IqpLoanAppSub.reality_ir_y._obj.element.value;//执行利率（年）
    		if(parseFloat(reality_ir_y)>=0){
    			var default_rate_y = IqpLoanApp.IqpLoanAppSub.default_rate_y._obj.element.value;//违约利率（年）
    			var default_rate = parseFloat(default_rate_y)/parseFloat(reality_ir_y)-1;
    			IqpLoanApp.IqpLoanAppSub.default_rate._setValue(default_rate+"");
    			IqpLoanApp.IqpLoanAppSub.default_float_type._setValue("0");//加百分比
        	}else{
            	alert("请先输入执行利率!");
            	IqpLoanApp.IqpLoanAppSub.default_rate_y._setValue("");
            }
        }
    };
    //反向计算利率浮动比 并隐藏
    function ifRrAccordType(){
    	var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
    	if(ir_accord_type == "01"){
    		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderHidden(true);
    		IqpLoanApp.IqpLoanAppSub.overdue_rate._obj._renderRequired(false);

    		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderHidden(true);
    		IqpLoanApp.IqpLoanAppSub.default_rate._obj._renderRequired(false);
    	}
    };
    
</script>
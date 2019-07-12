<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<script type="text/javascript">
//-----------------------普通贷款业务合同调用JS----------------------------
//-----------------------是否额度合同项下支用------------------------------
function isPay(){
	var isPay = CtrLoanCont.is_limit_cont_pay._getValue();
	if(isPay == 2){ 
		CtrLoanCont.limit_cont_no._obj._renderHidden(true);
		CtrLoanCont.limit_cont_no._obj._renderRequired(false);
		CtrLoanCont.limit_cont_no._setValue("");
		CtrLoanCont.in_acct_br_id_displayname._obj._renderHidden(true);
		CtrLoanCont.in_acct_br_id_displayname._obj._renderRequired(false);
	}else if(isPay == 1){
		CtrLoanCont.limit_cont_no._obj._renderHidden(false);
		CtrLoanCont.in_acct_br_id_displayname._obj._renderHidden(false);
		CtrLoanCont.limit_cont_no._obj._renderRequired(true);
		CtrLoanCont.in_acct_br_id_displayname._obj._renderRequired(true);
	}
};
function is_limit_cont_pay(){
	var prd_id = CtrLoanCont.prd_id._getValue();
    //提货担保   500032 同业代付 500020 国际保理 800020 信托收据贷款 500021 打包贷款 500022 出口订单融资 500023 出口商业发票融资 500024 信用证项下出口押汇 500026 信用证 700020 国内保理 800021 短期信保融资 500031 远期信用证项下汇票贴现 500027 延期信用证项下应收款买入 500028 福费廷 500029 出口托收贷款 500025  进口开证  400020 外汇保函 700021
    if(prd_id == "500020"|| prd_id == "500032" || prd_id == "800020" || prd_id == "500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500026" || prd_id == "700020" || prd_id == "800021" || prd_id == "500031" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500025"  || prd_id == "400020"  || prd_id == "700021" ){
    	CtrLoanCont.is_limit_cont_pay._obj._renderRequired(true);
    	CtrLoanCont.is_limit_cont_pay._obj._renderHidden(false);
    }else{
    	CtrLoanCont.is_limit_cont_pay._obj._renderRequired(false);
    	CtrLoanCont.is_limit_cont_pay._obj._renderHidden(true);    
    }
};
//-----------------------利率调整方式js控制------------------------------------
function ir_adjust_type_change(){
	var irAdjType = CtrLoanCont.CtrLoanContSub.ir_adjust_type._getValue();
	if(irAdjType == 0){
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderHidden(true);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderHidden(true);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderHidden(true);//第一次调整日
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderRequired(false);//第一次调整日
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._setValue("");//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._setValue("");//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._setValue("");//第一次调整日
	}else if(irAdjType == "FIX"){
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderHidden(false);//第一次调整日
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderRequired(true);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderRequired(true);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderRequired(true);//第一次调整日
		
	}else {
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderHidden(false);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderHidden(false);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderHidden(false);//第一次调整日
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_term._obj._renderRequired(false);//下一次利率调整间隔
		CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit._obj._renderRequired(false);//下一次利率调整单位
		CtrLoanCont.CtrLoanContSub.fir_adjust_day._obj._renderRequired(false);//第一次调整日
	}
};
//-------------------年利率计算月利率-----------------------
function reality_ir_yChange(){
	var reality_ir_y_Value = CtrLoanCont.CtrLoanContSub.reality_ir_y._obj.element.value;
	var yll = parseFloat(reality_ir_y_Value)/1200;
	reality_mounth._setValue(yll);
};
//-----------------------业务模式控制------------------------------------
function controlBizType(){
	var bizType = CtrLoanCont.biz_type._getValue();
	if(bizType == 8){
		CtrLoanCont.rent_type._obj._renderHidden(false);
		CtrLoanCont.rent_type._obj._renderRequired(true);
	}else if(bizType != 8 && bizType != 7 ){
		CtrLoanCont.biz_type._obj._renderHidden(false);
		CtrLoanCont.biz_type._obj._renderRequired(true); 
		CtrLoanCont.rent_type._setValue("");
		CtrLoanCont.rent_type._obj._renderHidden(true);
		CtrLoanCont.rent_type._obj._renderRequired(false);  
	}else {
		CtrLoanCont.rent_type._setValue("");
		CtrLoanCont.rent_type._obj._renderHidden(true);
		CtrLoanCont.rent_type._obj._renderRequired(false); 
	}    
};


//-----------------------通过异步取汇率------------------------------------
function getHLByCurr(){
	CtrLoanCont.exchange_rate._setValue("");
	var currType = CtrLoanCont.cont_cur_type._getValue();
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
					CtrLoanCont.exchange_rate._setValue(sld);
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

function doAnsyRequest(form,formUrl){
	var ranParam = new Date();
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
				alert("修改成功!");
			}else {
				alert("发生异常!");
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
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',formUrl, callback,postData)
}

//--页面加载授信使用标志------
function doInitLimit(){
	var limitInt = CtrLoanCont.limit_ind._getValue();
	var cus_id = CtrLoanCont.cus_id._getValue();//5000132513
	var lmt_type = "01";//01-单一法人 
	var lmt_type2 = "03";//03-合作方
	var outstnd_amt = CtrLoanCont.risk_open_amt._getValue();
	var prd_id = CtrLoanCont.prd_id._getValue();
	if(limitInt == "1"){//不使用额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
	    CtrLoanCont.limit_acc_no._obj._renderRequired(false);
	    CtrLoanCont.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_credit_no._obj.config.url='';
	}else if(limitInt == "2" || limitInt == "3"){//使用循环额度  || 使用一次性额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
	    CtrLoanCont.limit_acc_no._obj._renderRequired(true);
	    CtrLoanCont.limit_acc_no._obj._renderReadonly(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		//remain_amount._obj._renderReadonly(true);
		
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_credit_no._obj.config.url='';
	}else if(limitInt == "4"){//合作方额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
	    CtrLoanCont.limit_acc_no._obj._renderRequired(false);
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._obj._renderReadonly(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);
		//together_remain_amount._obj._renderReadonly(true);

		CtrLoanCont.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_acc_no._obj.config.url='';
		//银票贴现使用第三方时 2014-03-15wangs添加
		if(prd_id == "300021"){
			CtrLoanCont.limit_acc_no._obj._renderHidden(true);
			CtrLoanCont.limit_acc_no._obj._renderRequired(false);
			CtrLoanCont.limit_credit_no._obj._renderHidden(true);
			CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		}
	}else if(limitInt == "5" || limitInt == "6"){//使用循环额度+合作方额度  || 使用一次性额度+合作方额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
	    CtrLoanCont.limit_acc_no._obj._renderRequired(true);
	    CtrLoanCont.limit_acc_no._obj._renderReadonly(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		//remain_amount._obj._renderReadonly(true);
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._obj._renderReadonly(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);
		//together_remain_amount._obj._renderReadonly(true);

		CtrLoanCont.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
	}
	//银票贴现默认使用第三方授信
	if(prd_id == "300021"){
		CtrLoanCont.limit_ind._setValue("4");
		CtrLoanCont.limit_ind._obj._renderReadonly(true);
	}
}
//-------------------银行承兑汇票贴现，担保方式，担保方式细分隐藏-----------------------
function checkAssureMain(){
    var prd_id = CtrLoanCont.prd_id._getValue();
    if(prd_id == "300021"){
    	CtrLoanCont.assure_main._obj._renderHidden(true);
    	CtrLoanCont.assure_main_details._obj._renderHidden(true);

    	CtrLoanCont.assure_main._obj._renderRequired(false);
    	CtrLoanCont.assure_main_details._obj._renderRequired(false);
    }
}
//--------------------------额度使用标识---------------------------------------
function doChangLimitInt(){
	var limitInt = CtrLoanCont.limit_ind._getValue();
	var cus_id = CtrLoanCont.cus_id._getValue();
	var lmt_type = "01";//01-单一法人 
	var lmt_type2 = "03";//03-合作方
	var outstnd_amt = CtrLoanCont.risk_open_amt._getValue();
	if(limitInt == "1"|| limitInt == ""){//不使用额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
		CtrLoanCont.limit_acc_no._obj._renderRequired(false);
		CtrLoanCont.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		//remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		CtrLoanCont.limit_credit_no._setValue("");
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		//together_remain_amount._setValue("");
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_credit_no._obj.config.url='';
	}else if(limitInt == "2" || limitInt == "3"){//使用循环额度  || 使用一次性额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
		CtrLoanCont.limit_acc_no._obj._renderRequired(true);
		CtrLoanCont.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._obj._renderHidden(true);
		CtrLoanCont.limit_credit_no._obj._renderRequired(false);
		CtrLoanCont.limit_credit_no._setValue("");
		//together_remain_amount._obj._renderHidden(true);
		//together_remain_amount._obj._renderRequired(false);
		//together_remain_amount._setValue("");
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_credit_no._obj.config.url='';
	}else if(limitInt == "4"){//合作方额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(true);
		CtrLoanCont.limit_acc_no._obj._renderRequired(false);
		CtrLoanCont.limit_acc_no._setValue("");
		//remain_amount._obj._renderHidden(true);
		//remain_amount._obj._renderRequired(false);
		//remain_amount._setValue("");
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		CtrLoanCont.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_acc_no._obj.config.url='';
	}else if(limitInt == "5" || limitInt == "6"){//使用循环额度+合作方额度  || 使用一次性额度+合作方额度
		CtrLoanCont.limit_acc_no._obj._renderHidden(false);
		CtrLoanCont.limit_acc_no._obj._renderRequired(true);
		//remain_amount._obj._renderHidden(false);
		//remain_amount._obj._renderRequired(true);
		CtrLoanCont.limit_credit_no._obj._renderHidden(false);
		CtrLoanCont.limit_credit_no._obj._renderRequired(true);
		//together_remain_amount._obj._renderHidden(false);
		//together_remain_amount._obj._renderRequired(true);

		CtrLoanCont.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type2+"&outstnd_amt="+outstnd_amt;
		CtrLoanCont.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+'&lmt_type='+lmt_type+"&outstnd_amt="+outstnd_amt;
	}
};

function cleanLimitInd(){
	var prd_id = CtrLoanCont.prd_id._getValue();
	//银票贴现,商票贴现 2014-03-15wangs添加
	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度', '5':'使用循环额度+第三方额度', '6':'使用一次性额度+第三方额度'
	if(prd_id == "300021" || prd_id == "300020"){
		limit_ind = CtrLoanCont.limit_ind._getValue();
		var limitIndOptions = CtrLoanCont.limit_ind._obj.element.options;
		for(var i=limitIndOptions.length-1;i>=0;i--){
			if(limitIndOptions[i].value=="4" || limitIndOptions[i].value=="5" || limitIndOptions[i].value=="6"){//
				limitIndOptions.remove(i);
			}
		}
		var varOption = new Option('使用承兑人额度','4');
		limitIndOptions.add(varOption);
		CtrLoanCont.limit_ind._setValue(limit_ind);
	}
};
//-----------------------保证金通过异步取汇率------------------------------------
function getHLByCurr4Security(){
	CtrLoanCont.security_exchange_rate._setValue("");
	var currType = CtrLoanCont.security_cur_type._getValue();
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
					CtrLoanCont.security_exchange_rate._setValue(sld);
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

//-------------------计算保证金金额-----------------------
function changeRmbAmt4Security(){
	var prd_id = CtrLoanCont.prd_id._getValue();
	var appAmt = CtrLoanCont.cont_amt._getValue();//申请金额
	var security_cur_type = CtrLoanCont.security_cur_type._getValue();//保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = CtrLoanCont.exchange_rate._getValue();//汇率
		var setRate =CtrLoanCont.security_rate._getValue();//保证金比例
		var security_exchange_rate = CtrLoanCont.security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		//如果是贸易融资业务
		var security_amt;
		if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
			if(prd_id == "700020" || prd_id =="700021"){
				var floodact_perc = '${context.floodact_perc}';
           	    if(floodact_perc !='0'){
           	    	appAmt = parseFloat(appAmt *  (1+ parseFloat(floodact_perc)));
           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
           	    	CtrLoanCont.security_amt._setValue(''+security_amt+'');
           	    }else{
           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
           	    	CtrLoanCont.security_amt._setValue(''+security_amt+'');
               	}
            }else{
            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
            	CtrLoanCont.security_amt._setValue(''+security_amt+'');
            }
		}else{
		    security_amt = Math.round(parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)*100)/100;//保证金金额
		    CtrLoanCont.security_amt._setValue(''+security_amt+'');
		}
	}
};

//-------------------获取折合人民币金额、保证金比例、风险敞口比例-----------------------
function changeRmbAmt(){
	var appAmt = CtrLoanCont.cont_amt._getValue();//合同金额
	var prd_id = CtrLoanCont.prd_id._getValue();
	if(appAmt != null && appAmt != ""){
		//var secAmt = CtrLoanCont.security_amt._getValue();//保证金金额
		var rate = CtrLoanCont.exchange_rate._getValue();//汇率
		var setRate = CtrLoanCont.security_rate._getValue();//保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		var rmbValue = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round((parseFloat(rmbValue)*parseFloat(setRate))*100)/100;//保证金折算人民币金额
		CtrLoanCont.apply_rmb_amount._setValue(''+rmbValue+'');//申请金额折算人民币

		changeRmbAmt4Security();
		var security_amt = CtrLoanCont.security_amt._getValue();//保证金金额
		var security_exchange_rate = CtrLoanCont.security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate)*100)/100;
		
		CtrLoanCont.security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = CtrLoanCont.same_security_amt._getValue();//视同保证金
		if(sSecAmt == null || sSecAmt == ""){
			sSecAmt = 0;
		}
		if(prd_id == "700020" || prd_id =="700021"){
            var floodact_perc = '${context.floodact_perc}';
       	    if(floodact_perc !='0'){
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
		CtrLoanCont.risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
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
			var serate  = CtrLoanCont.security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}
		
		CtrLoanCont.risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};

//是否无间贷下拉框响应方法
function is_cloas_loan_change(){
	var sfgjd = CtrLoanCont.CtrLoanContSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
	var dkxs = CtrLoanCont.CtrLoanContSub.loan_form._getValue();//贷款形式
	if(sfgjd == "1" || dkxs == "3"){
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderRequired(true);   
	}else{
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderRequired(false); 
	}
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
	function getCusForm(){
		var cus_id = CtrLoanCont.cus_id._getValue();  
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
  		window.open(url,'cuswindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function getLimitAccNo(){
		var limit_acc_no = CtrLoanCont.limit_acc_no._getValue();  
		var url = "<emp:url action='viewLmtAgrInfo.do'/>?op=view&showButton=N&agr_no="+limit_acc_no;
		url=EMPTools.encodeURI(url);  
  		window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function getLimitCreditNO(){
		var limit_credit_no = CtrLoanCont.limit_credit_no._getValue();  
		var url = "<emp:url action='viewLmtAgrInfo.do'/>?op=view&showButton=N&agr_no="+limit_credit_no;
		url=EMPTools.encodeURI(url); 
  		window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	//生成还款策略
	function getRepayForm(){  
		var apply_amount = CtrLoanCont.cont_amt._getValue();
		if(apply_amount==""){
			alert("请输入合同金额!");
			return;
		}  
		var repay_mode_type = CtrLoanCont.CtrLoanContSub.repay_mode_type._getValue();
		var repay_date = CtrLoanCont.CtrLoanContSub.repay_date._getValue();
		var repay_term = CtrLoanCont.CtrLoanContSub.repay_term._getValue();
		var repay_space = CtrLoanCont.CtrLoanContSub.repay_space._getValue();
		var is_term = CtrLoanCont.CtrLoanContSub.is_term._getValue();
		var interest_term = CtrLoanCont.CtrLoanContSub.interest_term._getValue();
		var prd_id = CtrLoanCont.prd_id._getValue();
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
		var apply_date = CtrLoanCont.cont_start_date._getValue();
		if(apply_date==""){
			alert("请输入合同起始日期");
			return;
		}
		var term_type = CtrLoanCont.CtrLoanContSub.term_type._getValue();//期限类型
		if(term_type==""){
			alert("请输入期限类型");
			return;
		}
		var apply_term = CtrLoanCont.CtrLoanContSub.cont_term._getValue();//申请期限
		if(apply_term==""){
			alert("请输入申请期限");
			return;
		}
		var repay_type = CtrLoanCont.CtrLoanContSub.repay_type._getValue();//还款方式
		if(repay_type==""){
			alert("请输入还款方式");
			return;
		}
		var loan_type = CtrLoanCont.CtrLoanContSub.loan_type._getValue();
        if(loan_type==""){
        	alert("请输入贷款种类");
        	return;
        }
        var ir_accord_type = CtrLoanCont.CtrLoanContSub.ir_accord_type._getValue();
        //如果是不计息
        if(ir_accord_type == "03" ){
            var reality_ir_y = "0";
        }else{ 
        	var reality_ir_y = CtrLoanCont.CtrLoanContSub.reality_ir_y._getValue();
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
	
	function isShow(){
		  var payType = CtrLoanCont.CtrLoanContSub.conf_pay_type._getValue();
		  if(payType==1){
			  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(false);
			  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(true);
		  }else{
			  CtrLoanCont.CtrLoanContSub.pay_type._setValue("");
			  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(false);
			  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(true);
		   }

	  };
	  function cleanDate(){
		  CtrLoanCont.CtrLoanContSub.apply_term._setValue("");
	  };

	  function agricultureReturn(date){
			CtrLoanCont.CtrLoanContSub.agriculture_type._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.agriculture_type_displayname._setValue(date.label);
		};
		function projectReturn(date){
			CtrLoanCont.CtrLoanContSub.ensure_project_loan._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.ensure_project_loan_displayname._setValue(date.label);
		};
		function onReturn(date){
			CtrLoanCont.CtrLoanContSub.estate_adjust_type._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.estate_adjust_type_display._setValue(date.label);
		};
		function strategyReturn(date){
			CtrLoanCont.CtrLoanContSub.strategy_new_loan._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.strategy_new_loan_display._setValue(date.label);
		};
		function newPrdReturn(date){
			CtrLoanCont.CtrLoanContSub.new_prd_loan._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.new_prd_loan_display._setValue(date.label);
		};
		function greenPrdReturn(date){
			CtrLoanCont.CtrLoanContSub.green_prd._obj.element.value=date.id;
			CtrLoanCont.CtrLoanContSub.green_prd_display._setValue(date.label);
		};
		function loanBelong1Return(date){
			CtrLoanCont.CtrLoanContSub.loan_belong1._obj.element.value=date.id;
			
		};
		
		function loanDirectionReturn(date){
			CtrLoanCont.CtrLoanContSub.loan_direction._obj.element.value=date.id;
			loan_dirname._setValue(date.label);
		};
		function loanBelong2Return(date){
			CtrLoanCont.CtrLoanContSub.loan_belong2._obj.element.value=date.id;
			
		};
		function loanBelong3Return(date){
			CtrLoanCont.CtrLoanContSub.loan_belong3._obj.element.value=date.id;
		};
		function ensureProjectReturn(date){
			CtrLoanCont.CtrLoanContSub.loan_use_type._obj.element.value=date.id;
		};

		//主管机构
		function getOrgID(data){
			CtrLoanCont.manager_br_id._setValue(data.organno._getValue());
		};

		function checkDate(){
	        var repayDate = CtrLoanCont.CtrLoanContSub.repay_date._getValue();
	        alert(repayDate);
		};

		//是否显示所属网络
		function show_net(){
            var net = CtrLoanCont.biz_type._getValue();
            if(net == 7 || net == 8){
            	CtrLoanCont.belong_net._obj._renderHidden(true);
            	CtrLoanCont.belong_net._obj._renderRequired(false);
            	CtrLoanCont.belong_net._setValue("");
            }else{
            	CtrLoanCont.belong_net._obj._renderHidden(false);
            	CtrLoanCont.belong_net._obj._renderRequired(false);  
            }
	    };

	  //-------------------根据年利率同比换算月利率-----------------------
	    function getRulMounth(){
	    	var rulY = CtrLoanCont.CtrLoanContSub.ruling_ir._getValue();
	    	if(rulY != null && rulY != ""){
	    		ruling_mounth._setValue(parseFloat(rulY)/12);
	    		//getRelYM();
	    		
	    	}
	    	var llyjfs = CtrLoanCont.CtrLoanContSub.ir_accord_type._getValue();//利率依据方式
	    	if(llyjfs == "01"){//议价利率
	    		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);
	    		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);
	    		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);
	    		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);
	        }
	    };

	    function isShowNote(){
		     var note = CtrLoanCont.is_promissory_note._getValue();
		     if(note==1){
		    	 CtrLoanCont.promissory_note._obj._renderHidden(false);
		    	 CtrLoanCont.promissory_note._obj._renderRequired(true);
		     }else{
		    	 CtrLoanCont.promissory_note._setValue("");
		    	 CtrLoanCont.promissory_note._obj._renderRequired(false);
		    	 CtrLoanCont.promissory_note._obj._renderHidden(true);
		     }
		  };
		/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
	    function isShowCompany(){
			 var loan = CtrLoanCont.is_trust_loan._getValue();
			 if(loan=="1"){
				 CtrLoanCont.trust_company._obj._renderHidden(false);
				 CtrLoanCont.trust_company._obj._renderRequired(true);
				 CtrLoanCont.trust_pro_name._obj._renderHidden(false);
				 CtrLoanCont.trust_pro_name._obj._renderRequired(true);
			 }else{
				 CtrLoanCont.trust_company._setValue("");
				 CtrLoanCont.trust_pro_name._setValue("");
				 CtrLoanCont.trust_company._obj._renderRequired(false);
				 CtrLoanCont.trust_company._obj._renderHidden(true);
				 CtrLoanCont.trust_pro_name._obj._renderRequired(false);
				 CtrLoanCont.trust_pro_name._obj._renderHidden(true);
			 }
		};
		/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
function ir_accord_typeChange(data){  
	var llyjfs = CtrLoanCont.CtrLoanContSub.ir_accord_type._getValue();//利率依据方式
	var llfdfs = CtrLoanCont.CtrLoanContSub.ir_float_type._getValue();//利率浮动方式
	if(llyjfs == "01"){//议价利率
		/** 显示控制 */
    	CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);//计息周期
		/** 赋值控制 */
		if(data != "init"){
			CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
			CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
			CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
			CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
			CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("0");//利率调整方式
			CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
			CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
			CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
			CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
			CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
			CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
			CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
			CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
			CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
			CtrLoanCont.CtrLoanContSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);//计息周期
		/** 只读控制 */
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		//CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderReadonly(false);//执行利率（年）
		//CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderReadonly(false);//逾期利率（年）
		//CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderReadonly(false);//违约利率（年）
		
		
		/** 获取基准利率 */
		//getRate();
    }else if(llyjfs == "02"){//牌告利率依据
		 /** 显示控制 */
    	CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
			CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
			CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
			CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
			CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("");//利率调整方式
			CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
			CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
			CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
			CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
			CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
			CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
			CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
			CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
			CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
			CtrLoanCont.CtrLoanContSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);//计息周期
		/** 只读控制 */
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "03"){//不计息
        /** 显示控制 */
    	CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(true);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
			CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
			CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
			CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
			CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("");//利率调整方式
			CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
			CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
			CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
			CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
			CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
			CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
			CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
			CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
			CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
			CtrLoanCont.CtrLoanContSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(false);//计息周期

		/** 只读控制 */
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）
    }else if(llyjfs == "04"){//正常利率
		 /** 显示控制 */
    	CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(false); //基准利率（年）
		ruling_mounth._obj._renderHidden(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(false); //执行利率（年）
		reality_mounth._obj._renderHidden(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);//计息周期

		/** 赋值控制 */
		if(data != "init"){
			CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
			CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
			ruling_mounth._setValue(""); //对应基准利率（月）
			CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
			CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
			CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
			CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
			reality_mounth._setValue(""); //执行利率（月）
			CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("");//利率调整方式
			CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
			CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
			CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
			CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
			CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
			CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
			CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
			CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
			CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
			CtrLoanCont.CtrLoanContSub.interest_term._setValue("");//计息周期
		}
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(true); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(true); //执行利率（年）
		reality_mounth._obj._renderRequired(true); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);//计息周期

		/** 只读控制 */
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderReadonly(true);//执行利率（年）

		/** 获取基准利率 */
		//getRate();
    }
	// 贸易融资贴现需隐藏还款方式 
	hiddenTradeFinanc();
};

//-------------------根据贷款利率浮动方式同比调整显示-----------------------
function changeIrFloatType(){
	var floatType = CtrLoanCont.CtrLoanContSub.ir_float_type._getValue();
	if(floatType=='0'){//加百分比
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(false);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(true);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
		
	}else if(floatType=='1'){//加点
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(false);//贷款利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(true);//贷款利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//贷款利率浮动比
	}else {
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//贷款利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
	}
};


//-------------------根据逾期利率浮动方式同比调整显示-----------------------
function changeOverdueFloatType(){
	var overdueFloatType = CtrLoanCont.CtrLoanContSub.overdue_float_type._getValue();
	if(overdueFloatType=='0'){//加百分比
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
	}else if(overdueFloatType=='1'){//加点
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(false);//逾期利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(true);//逾期利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
	}else {
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
	}
};


//-------------------根据违约利率浮动方式同比调整显示-----------------------
function changeDefaultFloatType(){
	var defaultFloatType = CtrLoanCont.CtrLoanContSub.default_float_type._getValue();
	if(defaultFloatType=='0'){//加百分比
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
	}else if(defaultFloatType=='1'){//加点
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(false);//违约利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(true);//违约利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
	}else {
		/** 显示控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数
		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
		/** 值域控制 */
		CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数
	}
};

//-------------------判断是否为银票贴现，影藏所有利息要素-----------------------
function hiddenIr(){
	var prd = CtrLoanCont.prd_id._getValue();
	if(prd == "300021" || prd == "300020"){
		 /** 显示控制 */
		 CtrLoanCont.CtrLoanContSub.ir_accord_type._obj._renderHidden(true);//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		CtrLoanCont.CtrLoanContSub.ir_accord_type._setValue("");//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
		ruling_mounth._setValue(""); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
		reality_mounth._setValue(""); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("");//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数

		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_accord_type._obj._renderRequired(false);//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数
	}
};

//-----------------表外业务需要影藏利息要素-----------------------
function hiddenBWIr(){
	var prd = CtrLoanCont.prd_id._getValue();
	/** 400020(外汇保函)、400021(境内保函)、700020(信用证)、700021(进口开证) 500032(提货担保)、400022(贷款承诺)、400024(贷款意向)、400023(贷款证明)、200024(银行承兑汇票) */
	if(prd == "400020" || prd == "400021" || prd == "700020" || prd == "700021" || prd == "500032" || prd == "400022" || prd == "400024" || prd == "400023" || prd == "200024"){
		 /** 显示控制 */
		CtrLoanCont.CtrLoanContSub.ir_accord_type._obj._renderHidden(true);//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderHidden(true);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderHidden(true); //基准利率（年）
		ruling_mounth._obj._renderHidden(true); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderHidden(true); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderHidden(true); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(false); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderHidden(true); //执行利率（年）
		reality_mounth._obj._renderHidden(true); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderHidden(true);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderHidden(true);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderHidden(true);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderHidden(true);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderHidden(true);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderHidden(true);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderHidden(true);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderHidden(true);//违约利率浮动点数

		/** 赋值控制 */
		CtrLoanCont.CtrLoanContSub.ir_accord_type._setValue("");//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._setValue("");//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._setValue(""); //基准利率（年）
		ruling_mounth._setValue(""); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._setValue(""); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._setValue(""); //违约利率（年）
		//CtrLoanCont.CtrLoanContSub.pad_rate_y._setValue("");//垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._setValue(""); //执行利率（年）
		reality_mounth._setValue(""); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._setValue("");//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._setValue("");//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._setValue("");//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._setValue("");//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._setValue("");//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._setValue("");//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._setValue("");//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._setValue("");//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._setValue("");//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._setValue("");//违约利率浮动点数

		/** 必输控制 */
		CtrLoanCont.CtrLoanContSub.ir_accord_type._obj._renderRequired(false);//利率依据方式
		CtrLoanCont.CtrLoanContSub.ir_type._obj._renderRequired(false);//利率种类
		CtrLoanCont.CtrLoanContSub.ruling_ir._obj._renderRequired(false); //基准利率（年）
		ruling_mounth._obj._renderRequired(false); //对应基准利率（月）
		CtrLoanCont.CtrLoanContSub.overdue_rate_y._obj._renderRequired(false); //逾期利率（年）
		CtrLoanCont.CtrLoanContSub.default_rate_y._obj._renderRequired(false); //违约利率（年）
		CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(true); //垫款利率（年）
		CtrLoanCont.CtrLoanContSub.reality_ir_y._obj._renderRequired(false); //执行利率（年）
		reality_mounth._obj._renderRequired(false); //执行利率（月）
		CtrLoanCont.CtrLoanContSub.ir_adjust_type._obj._renderRequired(false);//利率调整方式
		CtrLoanCont.CtrLoanContSub.ir_float_type._obj._renderRequired(false);//利率浮动方式
		CtrLoanCont.CtrLoanContSub.ir_float_rate._obj._renderRequired(false);//利率浮动比
		CtrLoanCont.CtrLoanContSub.ir_float_point._obj._renderRequired(false);//贷款利率浮动点数
		CtrLoanCont.CtrLoanContSub.overdue_float_type._obj._renderRequired(false);//逾期利率浮动方式
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);//逾期利率浮动比
		CtrLoanCont.CtrLoanContSub.overdue_point._obj._renderRequired(false);//逾期利率浮动点数
		CtrLoanCont.CtrLoanContSub.default_float_type._obj._renderRequired(false);//违约利率浮动方式
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);//违约利率浮动比
		CtrLoanCont.CtrLoanContSub.default_point._obj._renderRequired(false);//违约利率浮动点数

		 /**隐藏还款方式信息、还款方式策略信息 */      
		hiddenRepay();
	}    
	hiddenTradeFinanc();
};

//贸易融资贴现品种 隐藏还款方式信息、还款方式策略信息
function hiddenTradeFinanc(){
	var prd = CtrLoanCont.prd_id._getValue();
	/** 500027(远期信用证项下汇票贴现 )、500028(延期信用证项下应收款买入 )、500029(福费廷)*/
	if(prd == "500027" || prd == "500028" || prd == "500029" ){
		hiddenRepay();
	}
};

function hiddenRepay(){
	document.getElementById('returnType').style.display="none"; 
	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(false);
	CtrLoanCont.CtrLoanContSub.repay_type._obj._renderRequired(false);
	CtrLoanCont.CtrLoanContSub.repay_type_displayname._obj._renderRequired(false);  
	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(false);
	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(false);
	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(false);
	//CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderRequired(false);
	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(false);  
}

function setRepayType(){
	var repay_mode_id = CtrLoanCont.CtrLoanContSub.repay_type._getValue(); 
	if(repay_mode_id==""){
		return false;
	}
	var url = '<emp:url action="getPrdRepayPlanUpdatePage.do"/>?repay_mode_id='+repay_mode_id;
	url = EMPTools.encodeURI(url);
	//PrdRepayPlanList._obj.ajaxQuery(url,null);
}; 

function setRepayTerm(){
	var prd = CtrLoanCont.prd_id._getValue();
	/**表外业务时，还款方式信息、还款方式策略信息需要隐藏,不需执行下列代码*/
	/** 400020(外汇保函)、400021(境内保函)、700020(信用证)、700021(进口开证) 500032(提货担保)、400022(贷款承诺)、400024(贷款意向)、400023(贷款证明)、200024(银行承兑汇票) */
    if(prd != "400020" && prd != "400021" && prd != "700020" && prd != "700021" && prd != "500032" && prd != "400022" && prd != "400024" && prd != "400023" && prd != "200024"){
    var term = CtrLoanCont.CtrLoanContSub.interest_term._getValue();
    //计息周期为利随本清 
    if(term == "4"){
    	//CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderHidden(false); 
    	//CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderRequired(true);
    	//计息周期为放款日结息 	
    }else if(term == "5"){
        //CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderHidden(true); 
        //CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderRequired(false); 
    }else{ 
    	//CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderHidden(false);
    	//CtrLoanCont.CtrLoanContSub.fir_repay_date._obj._renderRequired(true);
    }  
   }
};
//个人消费性贷款【贷款投向】隐藏且非必输，其他均显示且必输
function is_person_consume(){
	var prd_id = CtrLoanCont.prd_id._getValue();
    //住房贷款 100028 汽车贷款 100029 综合消费贷款 100030 黄金质押贷款 100031 珠宝质押贷款 100032 商用房贷款 100033 公积金贷款 100072 2014-10-09 modified by FCL 商用房属经营性贷款，贷款投向必录项  
    /* modified by yangzy 2014/12/29 增加生活贷A款、生活贷B款、生活贷C款、生活贷D款  start */
    if(prd_id == "100028" || prd_id == "100029" || prd_id == "100030" || prd_id == "100031" || prd_id == "100032" || prd_id == "100072" || prd_id == "100080" || prd_id == "100081" || prd_id == "100082" || prd_id == "100083" ){
    /* modified by yangzy 2014/12/29 增加生活贷A款、生活贷B款、生活贷C款、生活贷D款  end */
    	CtrLoanCont.CtrLoanContSub.loan_direction_displayname._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.loan_direction_displayname._obj._renderHidden(true);
    }else{
    	CtrLoanCont.CtrLoanContSub.loan_direction_displayname._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.loan_direction_displayname._obj._renderHidden(false);     
    } 
};
function getContMsg(data){
	CtrLoanCont.limit_cont_no._setValue(data.cont_no._getValue());
};
//表外业务以下字段不显示
function is_off_busi(){
	var menuId = '${context.menuId}';
	var prd_id = CtrLoanCont.prd_id._getValue();
    //  境内保函	400021  信用证	700020      进口开证	700021      贷款承诺	400022      信贷证明	400023      贷款意向	400024      外汇保函	400020      提货担保	500032        银行承兑汇票	200024
    if(prd_id == "400021" || prd_id == "700020" || prd_id == "700021" || prd_id == "400022" || prd_id == "400023" || prd_id == "400024" || prd_id == "400020" || prd_id == "500032" || prd_id == "200024" ){
    	CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderRequired(false);//是否无间贷
    	CtrLoanCont.CtrLoanContSub.loan_form._obj._renderRequired(false);//贷款形式
    	CtrLoanCont.CtrLoanContSub.loan_nature._obj._renderRequired(false);//贷款性质
    	
    	CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderHidden(true);//是否无间贷
    	CtrLoanCont.CtrLoanContSub.loan_form._obj._renderHidden(true);//贷款形式
    	CtrLoanCont.CtrLoanContSub.loan_nature._obj._renderHidden(true);//贷款性质

		if(prd_id == "700020" || prd_id == "700021"){ //贸易融资相关问题2014.05.07
	    	CtrLoanCont.cont_start_date._obj._renderHidden(true);//合同起始日期
	    	CtrLoanCont.cont_end_date._obj._renderHidden(true);//合同到期日期
		}
    	/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin **/
    	//如果是信托贷款业务
    	if(menuId != "trustqueryCtrLoanCont"){
    		CtrLoanCont.is_promissory_note._obj._renderRequired(false);//是否承诺函下
        	CtrLoanCont.is_trust_loan._obj._renderRequired(false);//是否信托贷款
        	CtrLoanCont.is_promissory_note._obj._renderHidden(true);//是否承诺函下
        	CtrLoanCont.is_trust_loan._obj._renderHidden(true);//是否信托贷款
    	}
    	var is_trust_loan = CtrLoanCont.is_trust_loan._getValue();
    	if(is_trust_loan == "1"){
    		CtrLoanCont.is_promissory_note._obj._renderRequired(true);//是否承诺函下
    		CtrLoanCont.is_trust_loan._obj._renderRequired(true);//是否信托贷款
    		CtrLoanCont.is_promissory_note._obj._renderHidden(false);//是否承诺函下
    		CtrLoanCont.is_trust_loan._obj._renderHidden(false);//是否信托贷款
        }
    	//隐藏支付信息
    	document.getElementById('payInfo').style.display="none";
    	CtrLoanCont.CtrLoanContSub.is_conf_pay_type._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(false);
    }else { 
          //不作处理
    }
    /** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end **/
};

//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示
function is_show_pad_rate(){
	var prd_id = CtrLoanCont.prd_id._getValue();
	//贷款承诺	400022      信贷证明	400023      贷款意向	400024
    if(prd_id == "400022" || prd_id == "400023" || prd_id == "400024"){
    	//隐藏利率信息
    	document.getElementById('rateInfo').style.display="none";
    	CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.pad_rate_y._obj._renderHidden(true);
    } 
};

//-----------------------贷款性质控制------------------------------------
function loan_nature_change(){
	var loanNature = CtrLoanCont.CtrLoanContSub.loan_nature._getValue();
	if(loanNature == 2){
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderRequired(true);
	}else {
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._setValue("");
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderRequired(false);
	}
};
/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
function getBizType(){
	var flg = '${context.flg}';
    if(flg == "trust"){
    	CtrLoanCont.is_trust_loan._obj._renderReadonly(false);
    	CtrLoanCont.trust_company._obj._renderRequired(true);
    	CtrLoanCont.trust_pro_name._obj._renderRequired(true);
    }else{
    	CtrLoanCont.is_trust_loan._setValue("2");
    	CtrLoanCont.is_trust_loan._obj._renderReadonly(true);
    	CtrLoanCont.trust_company._obj._renderRequired(false);
    	CtrLoanCont.trust_pro_name._obj._renderRequired(false);
   }
};
/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
function getFlg(){
	var flg = '${context.flg}';
	if(flg == "csgn"){
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._setValue("08");
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderReadonly(true);
	}else if(flg == "csgnClaimInvest"){
		//委托债权投资
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._setValue("14");
		CtrLoanCont.CtrLoanContSub.principal_loan_typ._obj._renderReadonly(true);
	}
}


//通过还款方式判断还款方式信息 
function checkFromRepayType(repay_mode_type){
    if(repay_mode_type == "05"){//还款方式种类为利随本清
    	var options = CtrLoanCont.CtrLoanContSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	CtrLoanCont.CtrLoanContSub.interest_term._setValue("4");
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(true);
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);
    }else if(repay_mode_type == "04" || repay_mode_type == "01" || repay_mode_type == "03"){//还款方式种类为按期结息 等额本息 等额本金
    	removeInterestTerm();

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);

    	//CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(false);
    	//CtrLoanCont.CtrLoanContSub.repay_space._obj._renderReadonly(false);
    	//CtrLoanCont.CtrLoanContSub.repay_date._obj._renderReadonly(false);
    }else if(repay_mode_type == "07" || repay_mode_type == "08"){//还款方式种类为弹性还款 气球贷
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);

    }else if(repay_mode_type == "09"){//还款方式种类为一次付息到期还本
    	var options = CtrLoanCont.CtrLoanContSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	CtrLoanCont.CtrLoanContSub.interest_term._setValue("5");
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(true);
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);
    }
    checkRepayDate();
};

function checkRepayDate(repay_mode_type){
	if(repay_mode_type == "05"){//还款方式种类为利随本清
    	var options = CtrLoanCont.CtrLoanContSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(true);
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);
    }else if(repay_mode_type == "04" || repay_mode_type == "01" || repay_mode_type == "03"){//还款方式种类为按期结息 等额本息 等额本金
    	removeInterestTerm();

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);

    	//CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(false);
    	//CtrLoanCont.CtrLoanContSub.repay_space._obj._renderReadonly(false);
    	//CtrLoanCont.CtrLoanContSub.repay_date._obj._renderReadonly(false);
    }else if(repay_mode_type == "07" || repay_mode_type == "08"){//还款方式种类为弹性还款 气球贷
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);

    }else if(repay_mode_type == "09"){//还款方式种类为一次付息到期还本
    	var options = CtrLoanCont.CtrLoanContSub.interest_term._obj.element.options;
    	var option1 = new Option('利随本清','4');
    	var option2 = new Option('放款日结息','5');
    	options.add(option1);
    	options.add(option2);
        
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderReadonly(true);
    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderHidden(false);

    	CtrLoanCont.CtrLoanContSub.interest_term._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.repay_term._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_space._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.repay_date._obj._renderRequired(true);   
    	CtrLoanCont.CtrLoanContSub.is_term._obj._renderRequired(true);
    }
	var cusId = CtrLoanCont.cus_id._getValue();
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
	var options = CtrLoanCont.CtrLoanContSub.interest_term._obj.element.options;
	for(var j=options.length-1;j>=0;j--){
        if(options[j].value=="4" || options[j].value=="5"){
        	options.remove(j);
        }
	}
};

function caculateContEndDate(){
    var startDate = CtrLoanCont.cont_start_date._getValue();
    var type = CtrLoanCont.CtrLoanContSub.term_type._getValue();
    var term = CtrLoanCont.CtrLoanContSub.cont_term._getValue();
    if(startDate == "" || startDate == null){
       alert("请检查合同起始日期!");
       return;
    }
    if(type == "" || type == null){
       alert("请检查期限类型!");
       return;
    }
    if(term == "" || term == null){
       alert("请检查合同期限!");
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
			var endDate = jsonstr.endDate;
			if(flag == "success"){
				CtrLoanCont.cont_end_date._setValue(endDate);
			}else {
				alert("计算合同到期日失败！");
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
	var url = '<emp:url action="caculateContEndDate.do"/>?startDate='+startDate+'&type='+type+'&term='+term;
	url = EMPTools.encodeURI(url);   
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};
//通过客户ID异步查询客户所属条线
function getBizLineByCusId(){
	var cusId = CtrLoanCont.cus_id._getValue();
	var prd_id = CtrLoanCont.prd_id._getValue();
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
						CtrLoanCont.CtrDiscCont.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE"/>';
					}else{
						CtrLoanCont.CtrLoanContSub.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE"/>';
						CtrLoanCont.CtrLoanContSub.loan_belong4._obj._renderHidden(true);
						CtrLoanCont.CtrLoanContSub.loan_belong4._obj._renderRequired(false);
					}
					
				}else{
					if(prd_id == "300020" || prd_id == "300021"){
						CtrLoanCont.CtrDiscCont.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_PER_POSITIONTYPE"/>';
					}else{
						CtrLoanCont.CtrLoanContSub.loan_type_displayname._obj.config.url='<emp:url action="showDicTree.do?dicTreeTypeId=STD_PER_POSITIONTYPE"/>';
						CtrLoanCont.CtrLoanContSub.loan_belong4._obj._renderHidden(false);
						CtrLoanCont.CtrLoanContSub.loan_belong4._obj._renderRequired(true);
					}
					
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

function checkAgentDisc(){
    var is_agent_disc = CtrLoanCont.CtrDiscCont.is_agent_disc._getValue();
    if(is_agent_disc == "1"){
    	CtrLoanCont.CtrDiscCont.agent_acct_no._obj._renderHidden(false);
    	CtrLoanCont.CtrDiscCont.agent_acct_name._obj._renderHidden(false);
    	CtrLoanCont.CtrDiscCont.agent_acct_no._obj._renderRequired(true);
    	CtrLoanCont.CtrDiscCont.agent_acct_name._obj._renderRequired(true);
    	
    	CtrLoanCont.CtrDiscCont.agent_org_no._obj._renderHidden(false);
    	CtrLoanCont.CtrDiscCont.agent_org_name._obj._renderHidden(false);
    	CtrLoanCont.CtrDiscCont.agent_org_no._obj._renderRequired(true);
    	CtrLoanCont.CtrDiscCont.agent_org_name._obj._renderRequired(true);
    }else if(is_agent_disc == "2"){
    	CtrLoanCont.CtrDiscCont.agent_acct_no._obj._renderHidden(true);
    	CtrLoanCont.CtrDiscCont.agent_acct_name._obj._renderHidden(true);
    	CtrLoanCont.CtrDiscCont.agent_acct_no._obj._renderRequired(false);
    	CtrLoanCont.CtrDiscCont.agent_acct_name._obj._renderRequired(false);
    	CtrLoanCont.CtrDiscCont.agent_acct_no._setValue("");
    	CtrLoanCont.CtrDiscCont.agent_acct_name._setValue("");
    	
    	CtrLoanCont.CtrDiscCont.agent_org_no._obj._renderHidden(true);
    	CtrLoanCont.CtrDiscCont.agent_org_name._obj._renderHidden(true);
    	CtrLoanCont.CtrDiscCont.agent_org_no._obj._renderRequired(false);
    	CtrLoanCont.CtrDiscCont.agent_org_name._obj._renderRequired(false);
    	CtrLoanCont.CtrDiscCont.agent_org_no._setValue("");
    	CtrLoanCont.CtrDiscCont.agent_org_name._setValue("");
    }
};

//是否收取印花税
function changeStamp(){
    var is_collect_stamp = CtrLoanCont.CtrLoanContSub.is_collect_stamp._getValue();
    if(is_collect_stamp == "1"){
    	CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderHidden(false);
    	checkstampCollectMode();
    }else if(is_collect_stamp == "2"){
    	CtrLoanCont.CtrLoanContSub.stamp_collect_mode._setValue("");
    	CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderHidden(true);
    }
};

function checkstampCollectMode(){
	var prd_id = CtrLoanCont.prd_id._getValue();//产品编号
	if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
		CtrLoanCont.CtrLoanContSub.stamp_collect_mode._setValue("4");
		CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderReadonly(true);
	}
	if(prd_id == '700020' || prd_id == '700021' || prd_id == '400020' || prd_id == '500032'){//信用证业务,外汇保函，提货担保隐藏
		CtrLoanCont.CtrLoanContSub.is_collect_stamp._setValue("2");
		CtrLoanCont.CtrLoanContSub.is_collect_stamp._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.is_collect_stamp._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.stamp_collect_mode._setValue("");
		CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderHidden(true);
	}
};

//是否收取印花税
function changeStampForDis(){
    var is_collect_stamp = CtrLoanCont.CtrDiscCont.is_collect_stamp._getValue();
    if(is_collect_stamp == "1"){
    	CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderRequired(true);
    	CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderHidden(false);
    	checkstampCollectModeForDis();
    }else if(is_collect_stamp == "2"){
    	CtrLoanCont.CtrDiscCont.stamp_collect_mode._setValue("");
    	CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderRequired(false);
    	CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderHidden(true);
    }
};

function checkstampCollectModeForDis(){
	var prd_id = CtrLoanCont.prd_id._getValue();//产品编号
	if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
		CtrLoanCont.CtrDiscCont.stamp_collect_mode._setValue("2");
		CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderReadonly(true);
	}
};
//贷款归属的显示还是隐藏(2014-04-15需求变更)
function showLoanBelong(){
	var prd_id = CtrLoanCont.prd_id._getValue();//产品编号
	//除(贴现，表外，贸易融资业务)外均展示
	if(prd_id == "100063" || prd_id =="100065" || prd_id == "200024" || prd_id == "300020" || prd_id == "300021" || prd_id == "400020" || prd_id == "400021" || prd_id == "400022" || prd_id == "400023" || prd_id == "400024" || prd_id == "500020" || prd_id == "500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026"  || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021" || prd_id == "800020" || prd_id == "800021"){
		CtrLoanCont.CtrLoanContSub.loan_belong1_displayname._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.loan_belong2_displayname._obj._renderRequired(false);
		CtrLoanCont.CtrLoanContSub.loan_belong3_displayname._obj._renderRequired(false);
		
		CtrLoanCont.CtrLoanContSub.loan_belong1_displayname._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.loan_belong2_displayname._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.loan_belong3_displayname._obj._renderHidden(true);
    }else{
    	CtrLoanCont.CtrLoanContSub.loan_belong1_displayname._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.loan_belong2_displayname._obj._renderRequired(true);
    	CtrLoanCont.CtrLoanContSub.loan_belong3_displayname._obj._renderRequired(true);
		
    	CtrLoanCont.CtrLoanContSub.loan_belong1_displayname._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.loan_belong2_displayname._obj._renderHidden(false);
    	CtrLoanCont.CtrLoanContSub.loan_belong3_displayname._obj._renderHidden(false);
	}
};
//是否节假日顺延,银承隐藏期限类型，期限
function checkIsDelay(){
    var prd_id = CtrLoanCont.prd_id._getValue();
    //如果为银行承兑汇票、银票贴现、商票贴现
    if(prd_id == "200024" || prd_id == "300020" || prd_id == "300021"){
    	CtrLoanCont.CtrLoanContSub.is_delay._obj._renderReadonly(true);   
    	CtrLoanCont.CtrLoanContSub.is_delay._setValue("1");
    }else{
    	CtrLoanCont.CtrLoanContSub.is_delay._obj._renderReadonly(true);
    	CtrLoanCont.CtrLoanContSub.is_delay._setValue("2");
    }
    if(prd_id == "200024"){//如果为银行承兑汇票
    	CtrLoanCont.cont_start_date._obj._renderRequired(false);
    	CtrLoanCont.cont_end_date._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.cont_term._obj._renderRequired(false);
    	CtrLoanCont.CtrLoanContSub.term_type._obj._renderRequired(false);
    	
    	CtrLoanCont.cont_start_date._obj._renderHidden(true);
    	CtrLoanCont.cont_end_date._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.cont_term._obj._renderHidden(true);
    	CtrLoanCont.CtrLoanContSub.term_type._obj._renderHidden(true);
    }
};
//反向计算利率浮动比 并隐藏
function ifRrAccordType(){
	var ir_accord_type = CtrLoanCont.CtrLoanContSub.ir_accord_type._getValue();
	if(ir_accord_type == "01"){
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.overdue_rate._obj._renderRequired(false);

		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.default_rate._obj._renderRequired(false);
	}
};
</script>
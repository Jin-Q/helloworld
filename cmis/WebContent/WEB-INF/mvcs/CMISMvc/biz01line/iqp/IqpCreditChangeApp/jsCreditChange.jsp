<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------通过异步取汇率------------------------------------
function getHLByCurr(){
	IqpCreditChangeApp.new_exchange_rate._setValue("");
	var currType = IqpCreditChangeApp.new_cur_type._getValue();
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
					IqpCreditChangeApp.new_exchange_rate._setValue(sld);
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

//-------------------获取修改后折合成人民币金额、修改后最大开证金额、保证金比例、风险敞口比例-----------------------
function changeRmbAmt(){
	var prd_id = IqpCreditChangeApp.prd_id._getValue();
	var appAmt = IqpCreditChangeApp.new_apply_amt._getValue();//修改后信用证金额
	var floodact_perc = IqpCreditChangeApp.new_floodact_perc._obj.element.value;
    if(floodact_perc =='' || floodact_perc ==null){
    	floodact_perc = 0;
    }
    floodact_perc = parseFloat(floodact_perc)/100;
	if(appAmt != null && appAmt != ""){
		var rate = IqpCreditChangeApp.new_exchange_rate._getValue();//汇率
		var setRate = IqpCreditChangeApp.new_security_rate._getValue();//修改后保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		var new_credit_apply_amt = parseFloat(appAmt * (1+ parseFloat(floodact_perc)));
        IqpCreditChangeApp.new_credit_apply_amt._setValue(''+new_credit_apply_amt+'');
        
		var rmbValue = Math.round((parseFloat(appAmt* (1+ parseFloat(floodact_perc)))*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round((parseFloat(rmbValue)*parseFloat(setRate))*100)/100;//保证金折算人民币金额
		IqpCreditChangeApp.new_rmb_amount._setValue(''+rmbValue+'');//申请金额折算人民币
		var new_credit_apply_amt = Math.round((parseFloat(appAmt* (1+ parseFloat(floodact_perc))))*100)/100;//修改后最大开证金额
		IqpCreditChangeApp.new_credit_apply_amt._setValue(''+new_credit_apply_amt+'');

		changeRmbAmt4Security();
		var security_amt = IqpCreditChangeApp.new_security_amt._getValue();//保证金金额
		var security_exchange_rate = IqpCreditChangeApp.new_security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate)*100)/100;
		IqpCreditChangeApp.new_security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = 0;

   	    appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
		var a = Math.round(parseFloat(appAmt)*100)/100;
		var riskAmt = Math.round((parseFloat(a)-parseFloat(secRmbAmt)-parseFloat(sSecAmt))*100)/100;
		if(riskAmt<0){
			riskAmt = 0;
		}
		IqpCreditChangeApp.new_risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
		var riskRate;
		if(appAmt!=0){
			riskRate = Math.round((riskAmt/appAmt)*10000)/10000;
			
			if(riskRate < 0){   
				riskRate = 0;
			}else if(riskRate > 1){
				riskRate = 1;
			}
		}else{
			var serate  = IqpCreditChangeApp.new_security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}
		IqpCreditChangeApp.new_risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};
//-------------------计算保证金金额-----------------------
function changeRmbAmt4Security(){
	var prd_id = IqpCreditChangeApp.prd_id._getValue();
	var appAmt = IqpCreditChangeApp.new_apply_amt._getValue();//修改后信用证金额
	var security_cur_type = IqpCreditChangeApp.new_security_cur_type._getValue();//修改后保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = IqpCreditChangeApp.new_exchange_rate._getValue();//汇率
		var setRate =IqpCreditChangeApp.new_security_rate._getValue();//修改后保证金比例
		var security_exchange_rate = IqpCreditChangeApp.new_security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		//如果是贸易融资业务
		var security_amt;
		var floodact_perc = IqpCreditChangeApp.new_floodact_perc._obj.element.value;
	    if(floodact_perc =='' || floodact_perc ==null){
	    	floodact_perc = 0;
	    }
	    floodact_perc = parseFloat(floodact_perc)/100;
        appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
        security_amt = Math.ceil((parseFloat(appAmt*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
        IqpCreditChangeApp.new_security_amt._setValue(''+security_amt+'');
        var security_amt_old = IqpCreditChangeApp.security_amt._getValue();
        IqpCreditChangeApp.new_add_security_rmb_rate._setValue(''+Math.round(parseFloat(security_amt)-parseFloat(security_amt_old))+'');
	}
};

//-----------------------保证金通过异步取汇率------------------------------------
function getHLByCurr4Security(){
	IqpCreditChangeApp.new_security_exchange_rate._setValue("");
	var currType = IqpCreditChangeApp.security_cur_type._getValue();
	IqpCreditChangeApp.new_security_cur_type._setValue(currType);
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
					IqpCreditChangeApp.new_security_exchange_rate._setValue(sld);
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


function _doKeypressDown() {
	try{
		if(IqpCreditChangeApp.new_security_rate._obj.element.focus){
			IqpCreditChangeApp.new_security_rate._obj.element.select();
	    }
	}catch(e){
		alert(e);
	}
}
//-------------------计算保证金金额(修改保证金比例的时候)-----------------------
function changeRmbAmt4SecurityChange(){
	var prd_id=IqpCreditChangeApp.prd_id._getValue();
	var appAmt = IqpCreditChangeApp.new_apply_amt._getValue();//申请金额
	var security_cur_type = IqpCreditChangeApp.new_security_cur_type._getValue();//保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = IqpCreditChangeApp.new_exchange_rate._getValue();//汇率
		//var setRate =IqpLoanApp.security_rate._obj.element.value;//保证金比例
		var setRate =IqpCreditChangeApp.new_security_rate._getValue();//保证金比例
		var security_exchange_rate = IqpCreditChangeApp.new_security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		//如果是贸易融资业务
		var security_amt;
		var floodact_perc = IqpCreditChangeApp.new_floodact_perc._obj.element.value;
	    if(floodact_perc =='' || floodact_perc ==null){
	    	floodact_perc = 0;
	    }
	    floodact_perc = parseFloat(floodact_perc)/100;
        appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
        security_amt = Math.ceil((parseFloat(appAmt*setRate/100)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
        IqpCreditChangeApp.new_security_amt._setValue(''+security_amt+'');
			
	}
};
//-------------------获取保证金比例同比修改保证金折算人民币金额、敞口金额，敞口比率-----------------------
function changeSecRate(){
	var appAmt = IqpCreditChangeApp.new_apply_amt._getValue();//申请金额
	var prd_id = IqpCreditChangeApp.prd_id._getValue();//申请金额
	if(appAmt != null && appAmt != ""){
		var floodact_perc = IqpCreditChangeApp.new_floodact_perc._obj.element.value;
	    if(floodact_perc =='' || floodact_perc ==null){
	    	floodact_perc = 0;
	    }
	    floodact_perc = parseFloat(floodact_perc)/100;
		//var secAmt = IqpLoanApp.security_amt._getValue();//保证金金额
		var rate = IqpCreditChangeApp.new_exchange_rate._getValue();//汇率
		var setRate = IqpCreditChangeApp.new_security_rate._getValue();//保证金比例
		//var setRate = IqpLoanApp.security_rate._obj.element.value;//保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}

		var rmbValue = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round(parseFloat(rmbValue)*parseFloat(setRate))/100;//保证金折算人民币金额

		changeRmbAmt4SecurityChange();
		var security_amt = IqpCreditChangeApp.new_security_amt._getValue();//保证金金额
		var security_exchange_rate = IqpCreditChangeApp.new_security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate)*100)/100;

		IqpCreditChangeApp.new_security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = 0;//视同保证金
		
       	appAmt = parseFloat(appAmt * rate *  (1+ parseFloat(floodact_perc)));
       	
		var a = Math.round(parseFloat(appAmt)*100)/100;
		var riskAmt = Math.round((parseFloat(a)-parseFloat(secRmbAmt)-parseFloat(sSecAmt))*100)/100;
		if(riskAmt<0){
			riskAmt = 0;
		}
		IqpCreditChangeApp.new_risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
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
			var serate  = IqpCreditChangeApp.new_security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}
		IqpCreditChangeApp.new_risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};
//主担保方式下拉框相应方法
function assure_mainChange(){
	var assureMainValue =IqpCreditChangeApp.new_assure_main._getValue();
	var assmainDtsoptions = IqpCreditChangeApp.new_assure_main_details._obj.element.options;
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
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(false);
		IqpCreditChangeApp.new_assure_main_details._setValue("");
	}else if(assureMainValue =="100"){//主担保方式为抵押时，担保方式细分自动赋值为抵押
		IqpCreditChangeApp.new_assure_main_details._setValue("1");
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="300"){//保证
		IqpCreditChangeApp.new_assure_main_details._setValue("8");
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="400"){//信用
		IqpCreditChangeApp.new_assure_main_details._setValue("9");
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="500"){//100%保证金  
		IqpCreditChangeApp.new_assure_main_details._setValue("10");
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue =="510"){//准全额保证金
		IqpCreditChangeApp.new_assure_main_details._setValue("11");
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(true);
	}else if(assureMainValue.substring(0,1) == "2"){
		IqpCreditChangeApp.new_assure_main_details._obj._renderReadonly(false);
		IqpCreditChangeApp.new_assure_main_details._setValue("");
		var assmainDtsoptions = IqpCreditChangeApp.new_assure_main_details._obj.element.options;
		for(var i=assmainDtsoptions.length-1;i>=0;i--){	
			if(assmainDtsoptions[i].value=="1" || assmainDtsoptions[i].value=="8" ||assmainDtsoptions[i].value=="9" ||assmainDtsoptions[i].value=="10" ||assmainDtsoptions[i].value=="11"){
				assmainDtsoptions.remove(i);
			}
		}
	}
};
// for old
function getHLByCurr4Old(){
	IqpCreditChangeApp.exchange_rate._setValue("");
	var currType = IqpCreditChangeApp.cont_cur_type._getValue();
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
					IqpCreditChangeApp.exchange_rate._setValue(sld);
					changeRmbAmt4Old();
					changeRmbAmt4Security4Old();//计算保证金金额
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
//-----------------------保证金通过异步取汇率forold------------------------------------
function getHLByCurr4Security4Old(){
	IqpCreditChangeApp.security_exchange_rate._setValue("");
	var currType = IqpCreditChangeApp.security_cur_type._getValue();
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
					IqpCreditChangeApp.security_exchange_rate._setValue(sld);
					changeRmbAmt4Security4Old();
					changeRmbAmt4Old();
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
function changeRmbAmt4Security4Old(){
	var prd_id = IqpCreditChangeApp.prd_id._getValue();
	var appAmt = IqpCreditChangeApp.cont_amt._getValue();//申请金额
	var security_cur_type = IqpCreditChangeApp.security_cur_type._getValue();//保证金币种
	if(appAmt != null && appAmt != "" && security_cur_type!=null && security_cur_type !=""){
		var rate = IqpCreditChangeApp.exchange_rate._getValue();//汇率
		var setRate =IqpCreditChangeApp.security_rate._getValue();//保证金比例
		var security_exchange_rate = IqpCreditChangeApp.security_exchange_rate._getValue();//保证金汇率
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		//如果是贸易融资业务
		var security_amt;
		if(prd_id == "500020" || prd_id =="500021" || prd_id == "500022" || prd_id == "500023" || prd_id == "500024" || prd_id == "500025" || prd_id == "500026" || prd_id == "500027" || prd_id == "500028" || prd_id == "500029" || prd_id == "500031" || prd_id == "800020" || prd_id == "800021" || prd_id == "400020" || prd_id == "500032" || prd_id == "700020" || prd_id == "700021"){
			if(prd_id == "700020" || prd_id =="700021"){
				var floodact_perc = IqpCreditChangeApp.floodact_perc._obj.element.value;
			    if(floodact_perc =='' || floodact_perc ==null){
			    	floodact_perc = 0;
			    }
			    floodact_perc = parseFloat(floodact_perc)/100;
           	    if(floodact_perc !='0'){
           	    	appAmt = parseFloat(appAmt *  (1+ parseFloat(floodact_perc)));
           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
           	    	IqpCreditChangeApp.security_amt._setValue(''+security_amt+'');
           	    }else{
           	    	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
           	    	IqpCreditChangeApp.security_amt._setValue(''+security_amt+'');
               	}
            }else{
            	security_amt = Math.ceil((parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate))/100)*100;//保证金金额
            	IqpCreditChangeApp.security_amt._setValue(''+security_amt+'');
            }
		}else{
		    security_amt = Math.round(parseFloat(appAmt*rate*setRate)/parseFloat(security_exchange_rate)*100)/100;//保证金金额
		    IqpCreditChangeApp.security_amt._setValue(''+security_amt+'');
		}
	}
};
//-------------------获取折合人民币金额、保证金比例、风险敞口比例-----------------------
function changeRmbAmt4Old(){
	var appAmt = IqpCreditChangeApp.cont_amt._getValue();//合同金额
	var prd_id = IqpCreditChangeApp.prd_id._getValue();
	if(appAmt != null && appAmt != ""){
		//var secAmt = IqpCreditChangeApp.security_amt._getValue();//保证金金额
		var rate = IqpCreditChangeApp.exchange_rate._getValue();//汇率
		var setRate = IqpCreditChangeApp.security_rate._getValue();//保证金比例
		if(setRate == null || setRate == ""){
			setRate = 0;
		}
		var rmbValue = Math.round((parseFloat(appAmt)*parseFloat(rate))*100)/100;//折合人民币申请金额
		var secRmbAmt = Math.round((parseFloat(rmbValue)*parseFloat(setRate))*100)/100;//保证金折算人民币金额
		//IqpCreditChangeApp.apply_rmb_amount._setValue(''+rmbValue+'');//申请金额折算人民币

		changeRmbAmt4Security4Old();
		var security_amt = IqpCreditChangeApp.security_amt._getValue();//保证金金额
		var security_exchange_rate = IqpCreditChangeApp.security_exchange_rate._getValue();//保证金汇率
		secRmbAmt = Math.round(parseFloat(security_amt)*parseFloat(security_exchange_rate));
		
		//IqpCreditChangeApp.security_rmb_rate._setValue(''+secRmbAmt+'');//保证金折算人民币
		var sSecAmt = IqpCreditChangeApp.same_security_amt._getValue();//视同保证金
		if(sSecAmt == null || sSecAmt == ""){
			sSecAmt = 0;
		}
		if(prd_id == "700020" || prd_id =="700021"){
			var floodact_perc = IqpCreditChangeApp.floodact_perc._obj.element.value;
		    if(floodact_perc =='' || floodact_perc ==null){
		    	floodact_perc = 0;
		    }
		    floodact_perc = parseFloat(floodact_perc)/100;
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
		IqpCreditChangeApp.risk_open_amt._setValue(''+riskAmt+'');//风险敞口金额
        //取风险敞口金额供授信Pop框使用
		//doChangLimitInt();
		var riskRate;
		if(appAmt!=0){
			riskRate = Math.round((riskAmt/appAmt)*10000)/10000;
			if(riskRate < 0){   
				riskRate = 0;
			}else if(riskRate > 1){
				riskRate = 1;
			}
		}else{
			var serate  = IqpCreditChangeApp.security_rate._obj.element.value;
			if(serate!=''){
				riskRate = 1-parseFloat(serate)/100;
			}else{
				riskRate = 0;
			}
		}		
		IqpCreditChangeApp.risk_open_rate._setValue(''+riskRate+'');//风险敞口比例
	}
};
</script>
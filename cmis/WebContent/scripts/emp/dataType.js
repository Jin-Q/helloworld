


/*********************用于系统内部调用的缺省类(用于调用具体的数据类型实现)*******************************/
if (!EMP.type.Base) {

	EMP.type.Base = {
		
		//校验输入值，并将输入值转变成真实值返回(返回值为EMPTools.message对象，其中的message为真实值或校验出错信息)
		check : function(displayValue, dataType_validateJS, formatErrorMsg, rangeErrorMsg) {
		var isInput=false;
		var msgObj = eval(dataType_validateJS);
			return msgObj;
		},
		
		//将真实值转变成显示值并返回(返回值即为显示值)
		display : function(realValue, dataType_convertorJS) {
		
			var isInput=false;
			var displayValue = eval(dataType_convertorJS);
			return displayValue;
		},
		
		//校验输入值，并将输入值转变成显示值返回(返回值为EMPTools.message对象，其中的message为显示值或校验出错信息)
		checkAndDisplay : function(displayValue, dataType_validateJS, dataType_convertorJS, formatErrorMsg, rangeErrorMsg) {
			if(dataType_convertorJS.indexOf("EMP.type.Rate4Month.display")!=-1||dataType_convertorJS.indexOf("EMP.type.Percent.display")!=-1){
				var isInput = true;
			}
			
			var msgObj = eval(dataType_validateJS);
			
			if(msgObj.result == false)//校验不通过的情况
				return msgObj;
			
			//var isInput = true
			var realValue = msgObj.message;
			if(dataType_convertorJS.indexOf("EMP.type.Rate4Month.display")!=-1||dataType_convertorJS.indexOf("EMP.type.Percent.display")!=-1){
				var isInput = true;
			}
			displayValue = eval(dataType_convertorJS);
			msgObj.message = displayValue;
			return msgObj;
		}
	};
}; 


/****************************数值类型的输入域**********************************/
if (!EMP.type.Decimal) {

	EMP.type.Decimal = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, scale, showSymbol, max, min, formatMsg, rangeMsg) {
			
			if(value == "" || value == null){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			//alert(min);
			var isNegative = false;
			if(value.charAt(0) == '-'){//判断当前是否带负号
				isNegative = true;
				value = value.substring(1);
			}
			if(showSymbol && value.charAt(0) == '￥'){//判断当前是否带金额符号
				value = value.substring(1);
			}
			
			//检查是否为科学计数法
		    var tempValueStr = new String(value); 
		    if ((tempValueStr.indexOf('E') != -1) || (tempValueStr.indexOf('e') != -1)){
		    	//alert('科学记数法Decimal' + tempValueStr);
		    	value = parseFloat(tempValueStr);
		    	value = new String(value);
		    }
			
			//检查格式
			var reg = /^[\d\,]*\.?\d*$/;
			var checkres = reg.exec(value);
			if (!checkres) {
				return EMPTools.message(false, formatMsg);
			}
			
			//去掉逗号
			var value = value.replace(/\,/g,"");
			if (value == "" || value == ".") //为空或只包含"."，则表示值为0
				value = "0";
			
			var valueD = parseFloat(value);
			
			//去除多余的小数点位数
			valueD *= Math.pow(10, scale);
			valueD = Math.round(valueD);
			valueD /= Math.pow(10, scale);
 
			if(valueD < min || valueD > max){//校验最大值和最小值
				
				return EMPTools.message(false, rangeMsg);
			}
			
			value = valueD.toString();
			if(isNegative){
				value = "-" + value;
				var navalueD = -1 * valueD;
				if(navalueD < min || navalueD > max){//校验最大值和最小值
					
					return EMPTools.message(false, rangeMsg);
				}				
			}
			return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回
		display : function(value, scale, showComma, showSymbol) {
			if (value == null || value == ""){
				return "";//如果未输入任何数据，则不作格式转换
			}
			
			var isNegative = false;
			if(value.charAt(0) == '-'){//判断当前是否带负号
				isNegative = true;
				value = value.substring(1);
			}
			
			//判断是否为科学计数法
		    var tempValueStr = new String(value); 
		    if ((tempValueStr.indexOf('E') != -1) || (tempValueStr.indexOf('e') != -1)){
		    	
		    	value = parseFloat(tempValueStr);
		    	value = new String(value);
		    }
			
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			
			//整数部分每3位添加逗号
			if(showComma){
				integer = integer.split("").reverse().join("");
				var reg = /(\d{3})\B/g;
				integer = integer.replace(reg,"$1,");
				integer = integer.split("").reverse().join("");
			}
			
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}
			if(showSymbol)//需要显示金额符号的情况
				value = "￥" + value;
			if(isNegative)
				value = "-" + value;
			return value;
		}	
	};
};



/************日期类型的输入域(缺省用于Date、DateSpace标签的校验)**************/
if (!EMP.type.Date) {

	EMP.type.Date = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, formatMsg, rangeMsg) {
		    
		if(value == null || value == ""){
			return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
		}
		var partten = /^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$/;
		if(!partten.exec(value)){
			return EMPTools.message(false, "输入日期格式必须为yyyy-MM-dd");
		}
		var idx = value.indexOf("-");
		if(idx != -1){
			var _arr = value.split('-');
			var year = _arr[0];
			var month = _arr[1];
			var date = _arr[2];
		}else{
			if(value.length != 8){
				return EMPTools.message(false, formatMsg);
			}
			var year = value.substring(0,4)
			var month = value.substring(4,6);
			var date = value.substring(6,8);
		}
		
		//月份是从0-11，因此new Date时需要将月份减1
		var dateObj = new Date(year,month-1,date);
		if(isNaN(dateObj)){
			return EMPTools.message(false, formatMsg);
		}
		
		//modify by liubq
		//修改成：如果输入的日期大于本月的最后一个日期，则提示错误，不再自动转成下个月的某个日期
		if(year != dateObj.getFullYear() || month != (dateObj.getMonth()+1) || date != dateObj.getDate()){
			return EMPTools.message(false, rangeMsg);
		}
		
		year = "" + dateObj.getFullYear();
		month = "" + (dateObj.getMonth()+1);
		if(month.length == 1)
			month = "0" + month;
		date = "" + dateObj.getDate();
		if(date.length == 1)
			date = "0" + date;
		
		var value = year + "-" + month + "-" + date;
		return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回:yyyy-MM-dd
		display : function(value) {
			if (value == null || value == "") {
				return "";//无真实值或真实值为空串，则直接返回空串
			}
			return value;
		},	
		
		//校验月-日格式
		checkMonDay : function(value, formatMsg, rangeMsg) {
			if(value.length==10)
				value = value.substring(5);
			else if(value.length == 8)
				value = value.substring(4);
			
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			var partten = /^[0-9]{1,2}[-]?[0-9]{1,2}$/;
			if(!partten.exec(value)){
				return EMPTools.message(false, "输入日期格式必须为MM-dd");
			}
			var idx = value.indexOf("-");
			if(idx != -1 ){
				var _arr = value.split('-');
				var month = _arr[0];
				var date = _arr[1];
			}else{
				if(value.length != 4){
					return EMPTools.message(false, formatMsg);
				}
				var month = value.substring(0,2);
				var date = value.substring(2,4);
			}
			
			//校验月-日是否在合理范围内
			var md = new Array(new Array(1,31),new Array(2,29),new Array(3,31),new Array(4,30),new Array(5,31),new Array(6,30),new Array(7,31),new Array(8,31),new Array(9,30),new Array(10,31),new Array(11,30),new Array(12,31));
			month = parseInt(month,10);
			date = parseInt(date,10);
			
			if(month>12 ||md[month-1][1] < date)
				return EMPTools.message(false, rangeMsg); 
			
			month = "" + month;
			if(month.length == 1)
				month = "0" + month;
			date = "" + date;
			if(date.length == 1)
				date = "0" + date;
			
			var value = month + "-" + date;
			return EMPTools.message(true, value);
		},
		
		//校验年-月格式
		checkYearMon : function(value, formatMsg, rangeMsg) {
			if(value.length==10)
				value = value.substring(0,7);
			else if(value.length == 8)
				value = value.substring(0,6);
			
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			var partten = /^[0-9]{4}[-]?[0-9]{2}$/;
			if(!partten.exec(value)){
				return EMPTools.message(false, "输入日期格式必须为yyyy-MM");
			}
			var idx = value.indexOf("-");
			if(idx != -1 ){
				var _arr = value.split('-');
				var year = _arr[0];
				var month = _arr[1];
			}else{
				if(value.length != 6){
					return EMPTools.message(false, formatMsg);
				}
				var year = value.substring(0,4);
				var month = value.substring(4,6);
			}
			
			//校验年-月是否在合理范围内
			month = parseInt(month,10);
			
			if(month>12)
				return EMPTools.message(false, rangeMsg); 
			
			month = "" + month;
			if(month.length == 1)
				month = "0" + month;
			
			var value = year + "-" + month;
			return EMPTools.message(true, value);
		}
	
	};
};



/************************整数类型的输入域******************************/
if (!EMP.type.Number) {

	EMP.type.Number = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, max, min, formatMsg, rangeMsg) {
			
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			
		    var tempValueStr = new String(value); 
		    if ((tempValueStr.indexOf('E') != -1) || (tempValueStr.indexOf('e') != -1)){ 
		    	//alert('科学记数法Number' + tempValueStr);
		    	value = parseFloat(tempValueStr);
		    	value = new String(value);
		    }			
			
			//检查格式
			var reg = /^[\d]*$/;
			var checkres = reg.exec(value);
			if (!checkres) {
				return EMPTools.message(false, formatMsg);
			}
			var valueD = parseInt(value);
			if(valueD < min || valueD > max){//校验最小值、最小值
				return EMPTools.message(false, rangeMsg);
			}
			
			return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回
		display : function(value) {
			
			if (value == null || value == "") {
				return "";//无真实值或真实值为空串，则直接返回空串
			}
			
		    var tempValueStr = new String(value); 
		    if ((tempValueStr.indexOf('E') != -1) || (tempValueStr.indexOf('e') != -1)){
		    	//alert('科学记数法Number' + tempValueStr);
		    	value = parseFloat(tempValueStr);
		    }			
			
			return value;
		}
	}
};

if (!EMP.type.Acct) {

	EMP.type.Acct = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, formatMsg) {
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			//检查格式
			//var reg = /^[\d]*$/;
			var reg = /^[0-9-_]+$/;
			var checkres = reg.exec(value);
			if (!checkres) {
				return EMPTools.message(false, "您输入的数据格式不正确，请输入数值或 _ 或 -");
			}
			
			return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回
		display : function(value) {
			
			if (value == null || value == "") {
				return "";//无真实值或真实值为空串，则直接返回空串
			}
			
		    return value;
		}
	}
};

/***********************简单的、可使用正则表达式进行判断的输入域******************************/
if (!EMP.type.Pattern) {

	EMP.type.Pattern = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, regFormat, formatMsg, rangeMsg) {
			
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			var reg = new RegExp(regFormat);
			var checkres = reg.test(value);
			if (!checkres) {
				return EMPTools.message(false, formatMsg);
			}
			
			return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回
		display : function(value) {
			
			if (value == null || value == "") {//无真实值或真实值为空串，则直接返回空串
				return "";
			}
			return value;
		}
	}
};


/************************身份证号类型的输入域******************************/
if (!EMP.type.Identity) {

	EMP.type.Identity = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, formatMsg, rangeMsg) {
			
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			
			var length = value.length;
		    if (length != 15 && length != 18){
		    	return EMPTools.message(false, formatMsg);
		    }
		    
		    var year = null;
		    var month = null;
		    var day = null;
		    if (length == 15){//15位的身份证号
		    	var reg = /^([0-9]{15})$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				year = "19" + value.substring(6,8);
				month = value.substring(8,10);
				day = value.substring(10,12);
		    }else if (length == 18){
		        var reg = /^([0-9]{17})([0-9|x|X])$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				year = value.substring(6,10);
				month = value.substring(10,12);
				day = value.substring(12,14);
		    }
		    var dateObj = new Date(year,month-1,day);
			if(isNaN(dateObj)){
				return EMPTools.message(false, formatMsg);
			}
			if((parseInt(day) != parseInt(dateObj.getDate())) || (parseInt(month)!= parseInt((dateObj.getMonth()+1)))){
				return EMPTools.message(false, formatMsg);
			}
			if (length == 18){
				var wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
				var verifyCodeList = [ "1", "0", "x", "9", "8", "7", "6", "5", "4",
					"3", "2" ];
				var totalCode = 0;
				for (var k = 0; k < 17; k++) {
					totalCode += value.substring(k,k+1) * wi[k];
				}
				var code = verifyCodeList[totalCode % 11 ];
				
				if(code != value.substring(17,18)){
					return EMPTools.message(false, formatMsg);
				}
			}
			return EMPTools.message(true, value);
		},
		
		//将真实值转变成显示值并返回
		display : function(value) {
			
			if (value == null || value == "") {//无真实值或真实值为空串，则直接返回空串
				return "";
			}
			return value;
		}
	}
};

/************************百分比\千分比类型的输入域（加入percent2支持负数 edit by zhangpu）******************************/
if (!EMP.type.Percent) {

	EMP.type.Percent = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, scale, isPercent, max, min, formatMsg, rangeMsg, isInput) {
		//alert("百分比 校验函数="+value);
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}

			var isNegative = false;
			if(value.charAt(0) == '-'){//判断当前是否带负号
				isNegative = true;
				value = value.substring(1);
			}
			
			//检查格式
			if(isPercent){//是否是百分率(缺省是百分率)
				var reg = /^[\d\,]*\.?\d*[\%]?$/;
				var checkres = reg.exec(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				//去掉逗号
				var value = value.replace(/\,/g,"");
				if (value == "" || value == ".") //为空或只包含"."，则表示值为0
					value = "0";
				
				var idx = value.indexOf("%");
			}else{
				var reg = /^[\d\,]*\.?\d*[\‰]?$/;
				var checkres = reg.exec(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				
				//去掉逗号
				var value = value.replace(/\,/g,"");
				if (value == "" || value == ".") //为空或只包含"."，则表示值为0
					value = "0";
				
				var idx = value.indexOf("‰");
			}
			
			var valueD = null;

			if(idx != -1){//输入值带有百分号\千分号	一定是前台值，做真实值转换
				value = value.substring(0,idx)
				valueD = parseFloat(value);
				
				if(isPercent){//是否是百分率
					valueD /= 100;
				} else {
					valueD /= 1000;
				}
			}else{
				if(isInput==true){//前台输入且不带百分号转换为真实值
					
					valueD = parseFloat(value);
					
					if(isPercent){//是否是百分率
						valueD /= 100;
					} else {
						valueD /= 1000;
					}
				}else{//后台的值为真实值不需要做转换
					valueD = parseFloat(value);
				}	
				
				
			}
			//去除多余的小数点位数
			valueD *= Math.pow(10, scale);
			valueD = Math.round(valueD);
			valueD /= Math.pow(10, scale);
			
			if(isNegative){
				valueD = -valueD;
			}
		
			
			if(valueD < min || valueD > max){//校验最大值和最小值
				return EMPTools.message(false, rangeMsg);
			}

			value = valueD.toString();
			//补足小数位数
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}

			return EMPTools.message(true, value);
		},
		
		//将真实值转变成页面上最终显示给用户的显示值。
		display : function(value, scale, isPercent, showComma, isInput) {
			//alert("百分比  展现函数value="+value+";scale="+scale+";ispercent="+isPercent+";showComma="+showComma+";isInput"+isInput);
			if (value == null || value == "") {//无真实值或真实值为空串，则直接返回空串
				return "";
			}
			
			var valueD = parseFloat(value);
			scale = parseFloat(scale);
			
			if(isPercent){//是否是百分率
				scale -= 2;
				valueD *= 100;
			}else{
				scale -= 3;
				valueD *= 1000;
			}
			
			valueD = round(valueD,scale);
			
			value = valueD.toString();
			
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			
			if(showComma){
				//整数部分每3位添加逗号
				integer = integer.split("").reverse().join("");
				var reg = /(\d{3})\B/g;
				integer = integer.replace(reg,"$1,");
				integer = integer.split("").reverse().join("");
			}
				
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}
			
			if(isPercent){ //是否是百分率
				value += "%";
			}else{
				value += "‰";
			}
			
			return value;
		}
	}
};

/************************月利率类型的输入域******************************/
if (!EMP.type.Rate4Month) {

	EMP.type.Rate4Month = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, scale, isPercent, max, min, formatMsg, rangeMsg, isInput) {
		//alert("百分比 校验函数="+value);
			if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}

			var isNegative = false;
			if(value.charAt(0) == '-'){//判断当前是否带负号
				isNegative = true;
				value = value.substring(1);
			}
			
			//检查格式
			if(isPercent){//是否是百分率(缺省是百分率)
				var reg = /^[\d\,]*\.?\d*[\%]?$/;
				var checkres = reg.exec(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				//去掉逗号
				var value = value.replace(/\,/g,"");
				if (value == "" || value == ".") //为空或只包含"."，则表示值为0
					value = "0";
				
				var idx = value.indexOf("%");
			}else{
				var reg = /^[\d\,]*\.?\d*[\‰]?$/;
				var checkres = reg.exec(value);
				if (!checkres) {
					return EMPTools.message(false, formatMsg);
				}
				
				//去掉逗号
				var value = value.replace(/\,/g,"");
				if (value == "" || value == ".") //为空或只包含"."，则表示值为0
					value = "0";
				
				var idx = value.indexOf("‰");
			}
			
			var valueD = null;

			if(idx != -1){//输入值带有百分号\千分号	一定是前台值，做真实值转换
				value = value.substring(0,idx)
				valueD = parseFloat(value);
				
				if(isPercent){//是否是百分率
					valueD /= 100;
				} else {
					valueD /= 1000;
				}
			}else{
				if(isInput==true){//前台输入且不带百分号转换为真实值
					
					valueD = parseFloat(value);
					
					if(isPercent){//是否是百分率
						valueD /= 100;
					} else {
						valueD /= 1000;
					}
				}else{//后台的值为真实值不需要做转换
					valueD = parseFloat(value);
				}	
				
				
			}
			//去除多余的小数点位数
			valueD *= Math.pow(10, scale);
			valueD = Math.round(valueD);
			valueD /= Math.pow(10, scale);
			
			if(isNegative){
				valueD = -valueD;
			}
		
			
			if(valueD < min || valueD > max){//校验最大值和最小值
				return EMPTools.message(false, rangeMsg);
			}

			value = valueD.toString();
			//补足小数位数
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}

			return EMPTools.message(true, value);
		},
		
		//将真实值转变成页面上最终显示给用户的显示值。
		display : function(value, scale, isPercent, showComma, isInput) {
			//alert("百分比  展现函数value="+value+";scale="+scale+";ispercent="+isPercent+";showComma="+showComma+";isInput"+isInput);
			if (value == null || value == "") {//无真实值或真实值为空串，则直接返回空串
				return "";
			}
			
			var valueD = parseFloat(value);
			scale = parseFloat(scale);
			
			if(isPercent){//是否是百分率
				scale -= 2;
				valueD *= 100;
			}else{
				scale -= 3;
				valueD *= 1000;
			}
			
			valueD = round(valueD,scale);
			
			value = valueD.toString();
			
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			
			if(showComma){
				//整数部分每3位添加逗号
				integer = integer.split("").reverse().join("");
				var reg = /(\d{3})\B/g;
				integer = integer.replace(reg,"$1,");
				integer = integer.split("").reverse().join("");
			}
				
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}
			
			if(isPercent){ //是否是百分率
				value += "%";
			}else{
				value += "‰";
			}
			
			return value;
		}
	}
};





function round(a,b){
	 var valueD = a;
	 var scale = b;
	 valueD *= Math.pow(10, scale);
	 valueD = Math.round(valueD);
	 valueD /= Math.pow(10, scale);
	 return valueD;
 }

/*
 if (!EMP.type.Rate4Month) {

	EMP.type.Rate4Month = {
		
		//校验输入值，并将输入值转变成真实值返回
		check : function(value, scale, isPercent, max, min, formatMsg, rangeMsg) {
				if(value == null || value == ""){
				return EMPTools.message(true, "");//如果未输入任何数据，则不作格式检查和转换。
			}
			   //alert("月利率 校验函数="+value);
			var isNegative = false;
			if(value.charAt(0) == '-'){//判断当前是否带负号
				isNegative = true;
				value = value.substring(1);
			}
			
			
			//检查格式

			var reg = /^[\d\,]*\.?\d*[\‰]?$/;
			var checkres = reg.exec(value);
			if (!checkres) {
				return EMPTools.message(false, formatMsg);
			}
			
			//去掉逗号
			var value = value.replace(/\,/g,"");
			if (value == "" || value == ".") //为空或只包含"."，则表示值为0
				value = "0";
				
			var idx = value.indexOf("‰");
			
			var valueD = null;
			if(idx != -1){//输入值带有千分号
				value = value.substring(0,idx)
				valueD = parseFloat(value);
				//valueD = valueD * 12 / 10;
				valueD = round(valueD,scale);
			 
			}else{
				valueD = parseFloat(value);
				//valueD *= 1000; 
			}
			//后台值 = 前台显示值 × 12 ÷ 10 /
			
			//去除多余的小数点位数
			valueD *= Math.pow(10, scale);
			valueD = Math.round(valueD);
			valueD /= Math.pow(10, scale);
			
			if(isNegative){
				valueD = -valueD;
			}
		
			if(valueD < min || valueD > max){//校验最大值和最小值
				//return EMPTools.message(false, rangeMsg);
			}
			
			value = valueD.toString();
			//补足小数位数
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}

			return EMPTools.message(true, value);
		},
		
		//将真实值转变成页面上最终显示给用户的显示值。
		display : function(value, scale, isPercent, showComma, isInput) {
			
			//alert("月利率 展现函数value="+value+"scale="+scale+"ispercent="+isPercent+"showComma="+showComma+"isInput="+isInput);
			
			if(isInput==true){
				alert("呵呵，true！"+value);
			}else{
				alert("呜呜，flase"+value);
			}	
			if (value == null || value == "") {//无真实值或真实值为空串，则直接返回空串
				return "";
			}
			
			var values=value.split(".");
			
			var valueD = parseFloat(value);
			
			scale = parseFloat(scale);
			
			scale -= 3;
			// 前台值 = 后台值 ÷ 12 × 10   
			
			//valueD = valueD / 12 * 10;
			valueD = round(valueD,scale);
			 
			value = valueD.toString();
			
			var d = value.indexOf(".");
			var integer = value;
			var decimal = "";
			if (d != -1){
				integer = value.substring(0, d);
				decimal = value.substring(d+1);
			}
			
			if(showComma){
				//整数部分每3位添加逗号
				integer = integer.split("").reverse().join("");
				var reg = /(\d{3})\B/g;
				integer = integer.replace(reg,"$1,");
				integer = integer.split("").reverse().join("");
			}

			if (scale>0) {
				while (decimal.length < scale) {
					decimal += "0";
				}
				value = integer + "." + decimal;
			}else{
				value = integer;
			}
			
			value += "‰";
			return value;
		}
	}
};
 */

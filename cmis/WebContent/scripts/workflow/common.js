function JHashMap()
{
    this.length = 0;
    this.prefix = "hashmap_";
};
/**
 * 向HashMap中添加键值对
 */
JHashMap.prototype.put = function (key, value)
{
    this[this.prefix + key] = value;
    this.length ++;
};
/**
 * 从HashMap中获取value值
 */
JHashMap.prototype.get = function(key)
{
    return typeof this[this.prefix + key] == "undefined" 
            ? null : this[this.prefix + key];
};
/**
 * 从HashMap中获取所有key的集合，以数组形式返回
 */
JHashMap.prototype.keySet = function()
{
    var arrKeySet = new Array();
    var index = 0;
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            arrKeySet[index ++] = strKey.substring(this.prefix.length);
    }
    return arrKeySet.length == 0 ? null : arrKeySet;
};
/**
 * 从HashMap中获取value的集合，以数组形式返回
 */
JHashMap.prototype.values = function()
{
    var arrValues = new Array();
    var index = 0;
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            arrValues[index ++] = this[strKey];
    }
    return arrValues.length == 0 ? null : arrValues;
};
/**
 * 获取HashMap的value值数量
 */
JHashMap.prototype.size = function()
{
    return this.length;
};
/**
 * 删除指定的值
 */
JHashMap.prototype.remove = function(key)
{
    delete this[this.prefix + key];
    this.length --;
};
/**
 * 清空HashMap
 */
JHashMap.prototype.clear = function()
{
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
            delete this[strKey];   
    }
    this.length = 0;
};
/**
 * 判断HashMap是否为空
 */
JHashMap.prototype.isEmpty = function()
{
    return this.length == 0;
};
/**
 * 判断HashMap是否存在某个key
 */
JHashMap.prototype.containsKey = function(key)
{
    for(var strKey in this)
    {
       if(strKey == this.prefix + key)
          return true;  
    }
    return false;
};
/**
 * 判断HashMap是否存在某个value
 */
JHashMap.prototype.containsValue = function(value)
{
    for(var strKey in this)
    {
       if(this[strKey] == value)
          return true;  
    }
    return false;
};
/**
 * 把一个HashMap的值加入到另一个HashMap中，参数必须是HashMap
 */
JHashMap.prototype.putAll = function(map)
{
    if(map == null)
        return;
    if(map.constructor != JHashMap)
        return;
    var arrKey = map.keySet();
    var arrValue = map.values();
    for(var i in arrKey)
       this.put(arrKey[i],arrValue[i]);
};
//toString
JHashMap.prototype.toString = function()
{
    var str = "";
    for(var strKey in this)
    {
        if(strKey.substring(0,this.prefix.length) == this.prefix)
              str += strKey.substring(this.prefix.length) 
                  + " : " + this[strKey] + "\r\n";
    }
    return str;
};

//文本变化时触发 解决失去焦点才触发事件的问题
function changecheck(obj, maxLength,checkSpan)
{
	var sOldValue = "" ;
	var vNewValue = obj.value ;
    if (sOldValue != vNewValue)
    {
        sOldValue = vNewValue;
        checkstr(obj, maxLength,checkSpan) ;
    }
}

// 字符长度校验
//obj: 当前调用该方法的对象
//maxLength：字符串长度英文为一个字符串长度、中文占两个个字符串长度
//checkSpan: 提示标签快块的ID
function checkstr(obj, maxLength,checkSpan) {
	var flag = true ;
	var str = obj.value;
	charLength = str.length ;
	var arr = str.match(/[^\x00-\x80]/ig);
	//如果存在中文占两个字符串长度
	if (arr != null)
		charLength += arr.length;
	var oInput = document.getElementById(obj.id);
	var msgChick = document.getElementById(checkSpan);
	var submitbtn = document.getElementById("submitbtn");
	if (charLength > maxLength) {
		var overLength = charLength-maxLength;
		msgChick.innerHTML = '最大字符串长度：' + maxLength+' |超过长度：'+ overLength;
		flag = true;
		if (oInput == null) {
			oInput = event.srcElement;
		}
		var rtextRange = oInput.createTextRange();
		rtextRange.moveStart('character', oInput.value.length);
		rtextRange.collapse(true);
		rtextRange.select();
	} else {
		msgChick.innerHTML="";
		flag = false;
	}
	if(submitbtn!=null&& submitbtn!= "undefined"){
		submitbtn.disabled = flag ; 
	}

}

		/**
		 * <b>禁用提交按钮</b>
		 */
		function stopSubmit(flag) {
			var submitbtn = document.getElementById("submitbtn");
			if (submitbtn != null && submitbtn != "undefined") {
				submitbtn.disabled = flag;
			}
		}
		
		function sel(oInput)
		{
			if (oInput == null) {
				oInput = event.srcElement;
			}
			var rtextRange = oInput.createTextRange();
			rtextRange.moveStart('character', oInput.value.length);
			rtextRange.collapse(true);
			rtextRange.select();
		}
	

	// 是否为金额
function ismoney(obj, checkspan) {
	var span = checkspan;
	var flag = true;
	var msgChick = document.getElementById(span);
	var oInput = obj.value;
	var regu = /^[0-9][\d,]{0,11}\.?\d{0,11}$/; //;金额
	var re = new RegExp(regu);
	if (re.test(oInput)) {
		if (oInput.length < 15) {
			msgChick.innerHTML = "";
			obj.value = formatCurrency(obj.value);
			flag = false;
		} else {
			msgChick.innerHTML = "整数部分不能超过12个字符";
			flag=true ;
			obj.value = "" ;
		}
	} else {
		msgChick.innerHTML = '请输入正确的金额格式';
		flag=true ;
		obj.value = "" ;
	}
	stopSubmit(flag);
}

//是否为金额可以为空
function ismoneynull(obj, checkspan) {
	var span = checkspan;
	var flag = true;
	var msgChick = document.getElementById(span);
	var oInput = obj.value;
	var regu = /^[0-9][\d,]{0,11}\.?\d{0,11}$|^\s*\s*$/; //;金额
	var re = new RegExp(regu);
	if (re.test(oInput)) {
		if (oInput.length < 15) {
			msgChick.innerHTML = "";
			obj.value = formatCurrency(obj.value);
			flag = false;
		} else {
			msgChick.innerHTML = "整数部分不能超过12个字符";
			flag=true ;
			obj.value = "" ;
		}
	} else {
		msgChick.innerHTML = '请输入正确的金额格式';
		flag=true ;
		obj.value = "" ;
	}
	stopSubmit(flag);
}

/**
 * 授信金额必须大于敞口金额
 */
function lowMoney(oInput, checkspan) {
	var inputEl = document.getElementsByTagName("input");
	for ( var i = 0; i < inputEl.length; i++) {
		if (inputEl[i].className == 'money') {
			var inputObj = inputEl[i];
			if (oInput == inputObj) {
				var obj = inputEl[i - 1]; // 敞口金额对象
				var aValue = inputObj.value; // 授信金额
				var bValue = obj.value;
				if ((aValue != null && aValue.length >= 0)
						&& (bValue != null && bValue.length >= 0)) {
					if (aValue.replace(",", "") < bValue.replace(",", "")) {
						var msgChick = document.getElementById(span);
						msgChick.innerHTML = "授信金额必须大于敞口金额";
					}
				}
			}
		}
	}
}

/*
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
    num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
    cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
    num = num.substring(0,num.length-(4*i+3))+','+
    num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + num + '.' + cents);
}

	 

	// 是否为利率
function ispay(obj, checkspan) {
	var span = checkspan;
	var flag = true;
	var msgChick = document.getElementById(span);
	var oInput = obj.value;
	var regu = /^[0-9]\d{0,11}\.?\d{0,6}$|^\s*\s*$/; //整数或者小数可以为空
	var re = new RegExp(regu);
	if (re.test(oInput)) {
		if (oInput.length > 12) {
			msgChick.innerHTML = "利率值异常";
			obj.value = "" ;
			flag=true ;
		} else {
			msgChick.innerHTML = "";
			flag = false;
		}
	} else {
		var msgChick = document.getElementById(span);
		msgChick.innerHTML = '请输入正确的利率格式';
		flag = true;
		obj.value = "" ;
	}
	stopSubmit(flag);
}
	

	// 月份
function monthCheck(obj, checkspan) {
	var span = checkspan;
	var flag = true;
	var msgChick = document.getElementById(span);
	var oInput = obj.value;
	var regu = /^[0-9]\d{0,4}\.?\d{0,6}$|^\s*\s*$/;  // 月份;
	var re = new RegExp(regu);
	if (re.test(oInput)) {
		if (oInput.length < 7) {
			msgChick.innerHTML = "";
			flag = false;
		} else {
			msgChick.innerHTML = "整数部分不能超过5个字符";
			flag=true ;
			obj.value = "" ;
		}
	} else {
		msgChick.innerHTML = '输入期限(月)格式';
		flag=true ;
		obj.value = "" ;
	}
	stopSubmit(flag);
    }

	//日期
	function dayCheck(obj, checkspan) {
	var span = checkspan;
	var flag = true;
	var msgChick = document.getElementById(span);
	var oInput = obj.value;
	var regu = /^[0-9]\d*$|^\s*\s*$/; // 月份
	var re = new RegExp(regu);
	if (re.test(oInput)) {
		if (oInput.length < 6) {
			msgChick.innerHTML = "";
			flag = false;
		} else {
			msgChick.innerHTML = "整数6个字符";
			flag = true;
			obj.value = "" ;
		};
	} else {
		msgChick.innerHTML = '正确的期限(月)格式';
		flag = true;
		obj.value = "" ;
	}
	stopSubmit(flag);
};

    /**
 *  加载后自动格式化金额
 */
function loadmoney() {
	var inputEl = document.getElementsByTagName("input");
	for ( var i = 0; i < inputEl.length; i++) {
		if (inputEl[i].className == 'money') {
			var inputObj = inputEl[i];
			var inputValue = inputObj.value;
			if (inputValue != null && inputValue.length > 0) {
				inputObj.value = formatCurrency(inputValue);
			}
		}
	}
}
	


 
// 获取当前格式化后的时间
	function getCurrentDate(){
		
		var day = new Date();
		
		var Year = day.getFullYear();
		var Month = day.getMonth()+1;
		var Day = day.getDate();
		var CurrentDate = "";
		
		CurrentDate += Year + "-";
   
		if(Month >= 10 ){
			CurrentDate += Month + "-";
		}else{
			CurrentDate += "0" + Month + "-";
		}
		if(Day >= 10 ){
			CurrentDate += Day ;
		}else{
			CurrentDate += "0" + Day ;
		}

   		return CurrentDate;
	}

	function CheckYearBeforeToday(yearIn)
	{
    /*
     * 检查日期是否小于当日函数
     * 返回值为true是输入日期小于当日 为false输入日期大于或等于当日
     */		
		var   exp=/^\d{4}$/;  
  		var   x=exp.test(yearIn);   
  		if (!x)
  		{
  			alert("请输入4位数字");
  			return false;
  		}
		var date=new Date();
	//	if(yearIn-1900<0)
		if(date.getFullYear()-60>yearIn)
		{
			alert("输入年份过早");
			return false;
		}else if(yearIn-date.getFullYear()>0){
			alert("输入年份不应晚于当前年份");
			return false;
		}
		return true;
		
	}
	
	function AddEventBeginEndDate(tablename,begin,end){
		var str="cmistaggroup_"+tablename+".fields."+begin;
		
		var	startField = eval("cmistaggroup_"+tablename+".fields."+begin);
		var endField = eval("cmistaggroup_"+tablename+".fields."+end);
		EMP.util.PageUtil.addEvent(startField.element, "blur", CheckBeginAndEndDate,"",startField,endField,"begin");
		EMP.util.PageUtil.addEvent(endField.element, "blur", CheckBeginAndEndDate,"",startField,endField,"end");
	}
	
	
	function CheckBeginAndEndDate(beginField,endField,flag){
	  var begin=beginField.getValue();
	  var end=endField.getValue();
	  if(begin==""||end==""){
	  	return false;
	  }
	  var bocheck=CheckDate1BeforeDate2(begin,end);
	  
	  if(!bocheck){
	   alert('开始日期不能晚于结束日期');
	   if(flag=="begin"){
	   		beginField.setValue("");
	   }else if(flag=="end"){
	   		endField.setValue("");
	   }
	   return false;
	  }
	  return true;
	}
	
	function CheckDateBeforeTodayByTips(field,tips)
	{
         dateIn = eval(field+".getValue();");
         if(!CheckDateBeforeToday(dateIn)){
             alert(tips+"必须小于当前日期!");
             return false;
         }
         return true;
	}
	    /*
     * 检查日期是否小于等于当日函数
     * 返回值为true是输入日期小于当日 为false输入日期大于或等于当日
     */	
/*
 * function CheckDateBeforeToday(dateIn) {
 * 
 * 
 * var date=new Date(); dateInYear=dateIn.substring(0,4);
 * dateInMonth=dateIn.substring(5,7); dateInDate=dateIn.substring(8,10);
 * dateStr=date.getFullYear(); if (dateStr-dateInYear<0) {
 * 
 * return false; }else if(dateStr-dateInYear>0) { return true; }
 * 
 * dateStr=date.getMonth()+1; if (dateStr-dateInMonth<0) {
 * 
 * return false; }else if (dateStr-dateInMonth>0) { return true; }
 * dateStr=date.getDate(); if (dateStr-dateInDate<0) {
 * 
 * return false; }
 * 
 * return true;
 *  }
 */	
	
	

	function CheckDate1BeforeDate2(date1,date2)
	{
    /*
     * 检查日期2是否小于等于日期1
     * 返回值为true是输入日期1小于输入日期2 为false输入日期1早于或等于输入日期2
     * 如果其中一个值为空的时候 直接返回TRUE
     */		
        if(""==date1){
        	return  true; 
        }
        if(""==date2){
        	return  true; 
        }
		var date=new Date();
		date1Year=date1.substring(0,4);
		date1Month=date1.substring(5,7);
		date1Date=date1.substring(8,10);
		date2Year=date2.substring(0,4);
		date2Month=date2.substring(5,7);
		date2Date=date2.substring(8,10);
		
		if (date2Year-date1Year<0)
		{
			return false;
		}
		else if(date2Year-date1Year>0)
		{
			return true;
		}
		
			
		if (date2Month-date1Month<0)
		{
			return false;
		}
		else if (date2Month-date1Month>0)
		{
			return true;
		}

		if (date2Date-date1Date<0)
		{
			return false;
		}
		
		return true;	

	}


	function CheckDateAfterToday(dateIn)
	{
    /*
     * 检查日期是否大于当日函数
     * 返回值为true是输入日期晚于当日 为false输入日期大于或等于当日
     */			
 
		var date=new Date();
		if(dateIn.length==8){
			dateIn = dateIn.substring(0,4)+'-'+dateIn.substring(4,6)+'-'+dateIn.substring(6,8);
		}
		var date=new Date();
		dateInYear=dateIn.substring(0,4);
		dateInMonth=dateIn.substring(5,7);
		dateInDate=dateIn.substring(8,10);
		dateStr=date.getFullYear();
		if (dateStr-dateInYear>0)
		{

			return false;
		}else if(dateStr-dateInYear<0)
		{
			return true;
		}
		
			dateStr=date.getMonth()+1;
		if (dateStr-dateInMonth>0)
		{

			return false;
		}else if (dateStr-dateInMonth<0)
		{
			return true;
		}
			dateStr=date.getDate();	
		if (dateStr-dateInDate>=0)
		{

			return false;
		}
		
		return true;	
	}

	function CheckDKK(financecodeStr)
	{
    /*
     * 检查贷款卡号函数
     * 返回值为true是贷款卡号符合规则 为false贷款卡号不符合规则
     */
		var financecode=new Array();
		financecode[0]=financecodeStr.charCodeAt(0);
		financecode[1]=financecodeStr.charCodeAt(1);
		financecode[2]=financecodeStr.charCodeAt(2);
		financecode[3]=financecodeStr.charCodeAt(3);
		financecode[4]=financecodeStr.charCodeAt(4);
		financecode[5]=financecodeStr.charCodeAt(5);
		financecode[6]=financecodeStr.charCodeAt(6);
		financecode[7]=financecodeStr.charCodeAt(7);
		financecode[8]=financecodeStr.charCodeAt(8);
		financecode[9]=financecodeStr.charCodeAt(9);
		financecode[10]=financecodeStr.charCodeAt(10);
		financecode[11]=financecodeStr.charCodeAt(11);
		financecode[12]=financecodeStr.charCodeAt(12);
		financecode[13]=financecodeStr.charCodeAt(13);
		financecode[14]=financecodeStr.charCodeAt(14);
		financecode[15]=financecodeStr.charCodeAt(15);
		var weightValue = new Array();
		var checkValue = new Array();
		var totalValue = 0;
		var c = 0;
		weightValue[0] = 1;
		weightValue[1] = 3;
		weightValue[2] = 5;
		weightValue[3] = 7;
		weightValue[4] = 11;
		weightValue[5] = 2;
		weightValue[6] = 13;
		weightValue[7] = 1;
		weightValue[8] = 1;
		weightValue[9] = 17;
		weightValue[10] = 19;
		weightValue[11] = 97;
		weightValue[12] = 23;
		weightValue[13] = 29;
		for (var j = 0; j < 14; j++)
		{
			if (financecode[j] >= 65 && financecode[j] <= 90){
			
				checkValue[j] = (financecode[j] - 65) + 10;
				}
			else
			if (financecode[j] >= 48 && financecode[j] <= 57)
			{

			checkValue[j] = financecode[j] - 48;

				}
			else{
			return false;
				}

			totalValue += weightValue[j] * checkValue[j];
			
		}

		c = 1 + totalValue % 97;
		var val = (financecode[14] - 48) * 10 + (financecode[15] - 48);
		if(val==c)
		{
		}
		return val == c;
	}

	
	function CheckJRJG(financecodeStr)
	{
    /*
     * 检查金融机构号函数
     * 返回值为true是金融机构号符合规则 为false金融机构号不符合规则
     */	
		var financecode=new Array();	
		var M = 10;
		var s = M;
		var k = 9;
		financecode[0]=financecodeStr.charCodeAt(0);
		financecode[1]=financecodeStr.charCodeAt(1);
		financecode[2]=financecodeStr.charCodeAt(2);
		financecode[3]=financecodeStr.charCodeAt(3);
		financecode[4]=financecodeStr.charCodeAt(4);
		financecode[5]=financecodeStr.charCodeAt(5);
		financecode[6]=financecodeStr.charCodeAt(6);
		financecode[7]=financecodeStr.charCodeAt(7);
		financecode[8]=financecodeStr.charCodeAt(8);
		financecode[9]=financecodeStr.charCodeAt(9);
		financecode[10]=financecodeStr.charCodeAt(10);
			
		for (var i = k; i >= 0; i--)
		{
			var temp = financecode[k - i];
			if (temp >= 48 && temp <= 57)
				temp -= 48;
			else
			if (temp >= 65 && temp <= 90 || temp >= 97 && temp <= 122)
				temp = 0;
			else
				return false;
			if ((s + temp) % M == 0)
				s = 9;
			else
				s = (((s + temp) % M) * 2) % (M + 1);
		}

		s = (M + 1) - s;
		if (s == 10)
			s = 0;
		return s == 11 && financecode[10] == 88 || s == financecode[10] - 48;
	}


function CheckIdValue(idCard){

    /*
     * 检查身份证号函数
     * 返回值为true是身份证号符合规则 为false身份证号不符合规则
     */	
    var id=idCard;
    var id_length=id.length;

    if (id_length==0){
        alert("请输入身份证号码!");
        return false;
    }

    if (id_length!=15 && id_length!=18){
        alert("身份证号长度应为15位或18位！");
        return false;
    }

    if (id_length==15){
		var re = /^\d{15}$/;
		if(!re.test(id)){
			alert("15位身份证号必须为数字");
			return false;
		}
        yyyy="19"+id.substring(6,8);
        mm=id.substring(8,10);
        dd=id.substring(10,12);

        if (mm>12 || mm<=0){
            alert("输入身份证号,月份非法！");
            return false;
        }

        if (dd>31 || dd<=0){
            alert("输入身份证号,日期非法！");
            return false;
        }

        birthday=yyyy+ "-" +mm+ "-" +dd;

        if ("13579".indexOf(id.substring(14,15))!=-1){
            sex="1";
        }else{
            sex="2";
        }
    }else if (id_length==18){
		var re = /^\d{17}[\dXx]$/;
		if(!re.test(id)){
			alert("身份证前17位必须为数字,第18位为数字或X");
		  	return false;
		}

        yyyy=id.substring(6,10);
        
        if(yyyy>new Date().getFullYear() || yyyy<1800){
            alert("输入身份证号,年度非法！");
            return false;
        }

        mm=id.substring(10,12);
        if (mm>12 || mm<=0){
            alert("输入身份证号,月份非法！");
            return false;
        }

        dd=id.substring(12,14);
        if (dd>31 || dd<=0){
            alert("输入身份证号,日期非法！");
            return false;
        }

        if (id.charAt(17)=="x" || id.charAt(17)=="X")
        {
            if ("x"!=GetIdVerifyBit(id) && "X"!=GetIdVerifyBit(id)){
                alert("身份证校验错误，请检查最后一位！");
                return false;
            }

        }else{
            if (id.charAt(17)!=GetIdVerifyBit(id)){
                alert("身份证校验错误，请检查最后一位！");
                return false;
            }
        }

        birthday=id.substring(6,10) + "-" + id.substring(10,12) + "-" + id.substring(12,14);
        if ("13579".indexOf(id.substring(16,17)) > -1){
            sex="1";
        }else{
            sex="2";
        }
    }

    return true;
}
//通过身份证号计算校验位的数值
function GetIdVerifyBit(id){
    var result;
    var nNum=eval(id.charAt(0)*7+id.charAt(1)*9+id.charAt(2)*10+id.charAt(3)*5+id.charAt(4)*8+id.charAt(5)*4+id.charAt(6)*2+id.charAt(7)*1+id.charAt(8)*6+id.charAt(9)*3+id.charAt(10)*7+id.charAt(11)*9+id.charAt(12)*10+id.charAt(13)*5+id.charAt(14)*8+id.charAt(15)*4+id.charAt(16)*2);
    nNum=nNum%11;
    switch (nNum) {
       case 0 :
          result="1";
          break;
       case 1 :
          result="0";
          break;
       case 2 :
          result="X";
          break;
       case 3 :
          result="9";
          break;
       case 4 :
          result="8";
          break;
       case 5 :
          result="7";
          break;
       case 6 :
          result="6";
          break;
       case 7 :
          result="5";
          break;
       case 8 :
          result="4";
          break;
       case 9 :
          result="3";
          break;
       case 10 :
          result="2";
          break;
    }
    //document.write(result);
    return result;
}

	/*
     * 检查组织机构号函数
     * 返回值为true是组织机构号符合规则 为false组织机构号不符合规则
     */
function CheckOrganFormat(code)
	{
		//旧的组织机构代码证规则
		if(code.length!=10){
			alert("组织机构代码必须为10位！");
			return false;
		}
		var old_style = /^X\d{9}$/;
		if(old_style.test(code)){
			return true;
		}
		//现有的组织机构代码证规则
		var w_i = new Array();
		var c_i = new Array();
		var s = 0;
		var financecode =new Array();
		
		financecode[0]=code.charCodeAt(0);
		financecode[1]=code.charCodeAt(1);
		financecode[2]=code.charCodeAt(2);
		financecode[3]=code.charCodeAt(3);
		financecode[4]=code.charCodeAt(4);
		financecode[5]=code.charCodeAt(5);
		financecode[6]=code.charCodeAt(6);
		financecode[7]=code.charCodeAt(7);
		financecode[8]=code.charCodeAt(8);
		financecode[9]=code.charCodeAt(9);
		financecode[10]=code.charCodeAt(10);
		

		if (code=="00000000-0"){
			alert("请输入正确的组织机构代码");
			return false;
		}
		w_i[0] = 3;
		w_i[1] = 7;
		w_i[2] = 9;
		w_i[3] = 10;
		w_i[4] = 5;
		w_i[5] = 8;
		w_i[6] = 4;
		w_i[7] = 2;
		if (financecode[8] != 45){
			alert("请输入正确的组织机构代码");	
			return false;
			}
		var c;
		for (var i = 0; i < 10; i++)
		{
			c = financecode[i];
			if (c <= 122 && c >= 97)
			{
				alert("请输入正确的组织机构代码");
				return false;
			}
		}

		fir_value = financecode[0];
		sec_value = financecode[1];
		if (fir_value >= 65 && fir_value <= 90)
			c_i[0] = (fir_value + 32) - 87;
		else
		if (fir_value >= 48 && fir_value <= 57)
			c_i[0] = fir_value - 48;
		else
		{
			alert("请输入正确的组织机构代码");
			return false;
		}
		s += w_i[0] * c_i[0];
		if (sec_value >= 65 && sec_value <= 90)
			c_i[1] = (sec_value - 65) + 10;
		else
		if (sec_value >= 48 && sec_value <= 57)
			c_i[1] = sec_value - 48;
		else
		{
			alert("请输入正确的组织机构代码");
			return false;
		}
		s += w_i[1] * c_i[1];
		for (var j = 2; j < 8; j++)
		{
			if (financecode[j] < 48 || financecode[j] > 57){
				alert("请输入正确的组织机构代码");
				return false;
				}
				
			c_i[j] = financecode[j] - 48;
			s += w_i[j] * c_i[j];
		}

		c = 11 - s % 11;
		if(financecode[9] == 88 && c == 10 || c == 11 && financecode[9] == 48 || c == financecode[9] - 48)
		{
			return true;
		}else{
			alert("请输入正确的组织机构代码");			
			return false;
		}
	}
	
function DateAddMonth(strDate,iMonths){
  
   var thisYear = parseFloat(strDate.substring(0,4));
   var thisMonth = parseFloat(strDate.substring(5,7));
   var thisDate = parseFloat(strDate.substring(8,10));
   var d =thisYear * 12 + thisMonth + parseFloat(iMonths);
      
   var newMonth = d % 12;
   if (newMonth==0) {
       newMonth=12;
   }
    
   var newYear = (d - newMonth) /12;
    
   var newDate = thisDate;
   var iMonthLastDate=getMonthLastDate(newYear,newMonth)
   
   if (newDate>iMonthLastDate) newDate=iMonthLastDate;
   var newDay="";

   newDay += newYear;
    
   if (newMonth<10) {
    newDay +="-0" + newMonth;
   }else{
    newDay +="-" + newMonth;
   }

   if (newDate<10) {
    newDay +="-0" + newDate;
   }else{
    newDay +="-" + newDate;
   }
   return(newDay);                                // 返回日期。
}

function getMonthLastDate(iYear,iMonth){
	 var dateArray= new Array(31,28,31,30,31,30,31,31,30,31,30,31);
	 var days=dateArray[iMonth-1];
	 if ((((iYear % 4 == 0) && (iYear % 100 != 0)) || (iYear % 400 == 0)) && iMonth==2){
	    days=29;
	 }
	 return days;
}

//是否是正整型 
function IsInteger(str) 
{ 
    var re = new RegExp(/^[0-9]*[1-9][0-9]*$/); 
    return re.test(str); 
} 
/*gaozh code begin 链接客户信息*/

function doGetCusInfo(cus_id,cus_type){
		
	   var sessionId = document.getElementsByName("EMP_SID")[0].value;
	   var url='';
	  // if(cus_type=='indiv')//对私
	  // url = contextPath +
		// '/getCusIndivViewPage.do?EMP_SID='+sessionId+'&cus_id='+cus_id+'&info=tree';
	 // else if(cus_type=='com')//对公
	// url = contextPath +
	// '/getCusComViewPage.do?EMP_SID='+sessionId+'&cus_id='+cus_id+'&info=tree';
	   url = contextPath + '/getCusViewPage.do?EMP_SID='+sessionId+'&cusId='+cus_id+'&info=tree';

	   window.open(url,'newwindow','height=480, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
}
/*gaozh code end 2009.04.23*/

function showCertTyp(elementObj, comOrIndiv){//added by echow 显示公司类/个人类证件类型   comOrIndiv:com/indiv
	var options = elementObj._obj.element.options;
	if(comOrIndiv == 'com'){//公司类
		for ( var i = options.length - 1; i >= 0; i--) {
			if((options[i].value.substring(0,1) == '1'  || options[i] && options[i].value == '280'
				|| options[i] && options[i].value == '222'|| options[i] && options[i].value == '232'
				|| options[i] && options[i].value == '242'|| options[i] && options[i].value == '270') && options[i].value != '1X'){
				//去掉个人及“农民专业合作社”
					options.remove(i);
			}
		}
	}else if(comOrIndiv == 'indiv'){//个人类
		for (var i = options.length - 1; i >= 0; i--) {
		/*	if(options[i].value.substring(0,1) == '2'){
				options.remove(i);
			}
			if(options[i] && options[i].value == '130'){
			 
				options.remove(i);
			}
			//以下程序专为证件类型服务
			if(options[i] && options[i].value == '11'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '14'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '17'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '18'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '19'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '1X'){
				options.remove(i);
			}
			else if(options[i] && options[i].value == '20'){
				options.remove(i);
			}*/
			if(options[i].value=='a'||options[i].value=='b'||options[i].value=='c'||options[i].value=='X'){
				options.remove(i);
			}
		}
	}
}


 
//把金额的"‰"过滤掉
function percentFilterPub(amt) {
	if (amt == null) {
		return null;
	}
	
	if(amt.indexOf("‰")==-1){
		return amt;// *1000
	}
	
	
	var amtStr = amt;
	var amtStrTmp = amt;
	
	
	while (amtStrTmp.indexOf("‰") != -1) {
		amtStr = amtStr.replace("‰", "");
		amtStrTmp=amtStr;
	}
	return amtStr;
}

function percentFilterPubH(amt) {
	if (amt == null) {
		return 0;
	}
	
	if(amt.indexOf("%")==-1){
		return amt;// *100
	}
	
	var amtStr = amt;
	var amtStrTmp = amt;
	
	
	while (amtStrTmp.indexOf("%") != -1) {
		amtStr = amtStr.replace("%", "");
		amtStrTmp=amtStr;
	}
	return amtStr;
}

 

//将toObj中的options选项用fromObj中的替换，并根据selValue设置选择状态(add by gaozh 2009.05.18)
function setObjOptByObj(toObj,fromObj,selValue){
    if(toObj._obj.element.options && fromObj._obj.element.options){
    	var toObjOpts = toObj._obj.element.options;
    	var fromObjOpts = fromObj._obj.element.options;
    	toObjOpts.length=0;// 清空
    	// for(var j=toObjOpts.length-1;j>=0;j--)toObjOpts.remove(j);
    	var isSel=false;
		for ( var i = 0; i<fromObjOpts.length; i++) {
			 
			var newOpt=new Option(fromObjOpts[i].innerText,fromObjOpts[i].value); 
			toObjOpts.add(newOpt);
			if(!isSel && selValue==fromObjOpts[i].value){
				newOpt.selected=true;
				isSel=true;
			}	
		}
   
    }
	
}

//判断日期输入控件值是否小于指定参数OPENDAY的值，提示errMsg。
function checkLeftValue(inputObj,_value,errMsg){
     if(inputObj.value!=''&&_value!=''){
	   if(inputObj.value>=_value){
		 alert(errMsg);
		 inputObj.value="";
		 return false;
	   }
     } 
	 return true;
}

//使用授信明细
function doGetLMT(serno,guar_type,biz_type,crd_lmt_type,cus_id,viewFlag){
	
	   if(crd_lmt_type==""){
		   alert("请选择授信额度使用标识!");
		   return;
	   }
	   if(crd_lmt_type=="1"){
		   alert("不使用授信不需要维护额度明细!");
		   return;
	   }
	   if(guar_type==""){
		   alert("请选择主担保方式!");
		   return;
	   }
	   if(crd_lmt_type=="2"){
		   crd_lmt_type="10";
	   }
	   if(crd_lmt_type=="3"){
		   crd_lmt_type="20";
	   }
	   var sessionId = document.getElementsByName("EMP_SID")[0].value;
	   var url;
	   if(viewFlag=='query'){
		    url='queryLmtContRelPopListView.do?EMP_SID=' + sessionId;
	   }else{
		    url='queryLmtContRelPopList.do?EMP_SID=' + sessionId;
	   }
	   url = url + "&serno=" + serno
	         + "&guar_type=" + guar_type
	         + "&biz_type=" + biz_type
	         + "&crd_lmt_type=" + crd_lmt_type
	         + "&cus_id=" + cus_id;
	   url = EMPTools.encodeURI(url);
	   window.open(url,'newwindow','height=480, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
}
//查看合作方
function doGetPrjCopAcc(acc_no){
	   
	   if(acc_no==""){
		   alert("没有选择合作方,无法查看!");
		   return;
	   }
	   var sessionId = document.getElementsByName("EMP_SID")[0].value;
	   var url='getPrjCopAccViewPage.do?EMP_SID=' + sessionId;
	   url = url + "&acc_no=" + acc_no;
	   url = EMPTools.encodeURI(url);
	   window.open(url,'newwindow','height=480, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
}
//
function checkBeforeToday(before,after,now,befEsg,aftEsg){
	 if(before.value!=''&&now!=''){
		 if(before.value>now){
			 alert(befEsg+"不能大于当前日期！");
			 before.value="";
		 }
		 if(after.value!=''){
			 if(now<=after.value){
				 alert(aftEsg+"应大于当前日期");
				 after.value="";
			 }
			 if(before.value>=after.value){
				 alert(aftEsg+"应大于"+befEsg);
				 after.value="";
			 }
		 }
	 }
}
function checkAfterToday(before,after,now,befEsg,aftEsg){
	 if(after.value!=''&&now!=''){
		 if(after.value<=now){
			 alert(aftEsg+"应大于当前日期！");
			 after.value="";
		 }
		 if(before.value!=''){
			 if(before.value>=after.value){
				 alert(aftEsg+"应大于"+befEsg);
				 before.value="";
			 }
		 }
	 }
}


//自定义页面标签后打印错误信息add by gaozh090611
function printMsg(obj,msg){
	   var thisTag=obj._obj.tag;
	   if(!this.msgSpan){
		   this.msgSpan=document.createElement('SPAN'); 
	   }else{
		   this.msgSpan.innerHTML = "  ";
	   }
	   
	   this.msgSpan.innerHTML=msg;
	    if(obj._obj.config.csserrorclass){
			EMPTools.addClass(this.msgSpan, obj._obj.config.csserrorclass);
		}
	    thisTag.appendChild(this.msgSpan);
}

//标签校验重载的业务校验add by gaozh090611
function pageBussinessCheck(obj){
  try{ 
	 var retMsg="";
	 var retIs=true;  
	if(window._pageBussinessCheck){
	  var colId=obj.dataName; 
	  var colObj=eval(colId); 
	  
	    retMsg=_pageBussinessCheck(colId,colObj); // 页面重载方法,colId是icoll字段名,colObj是icoll字段对象
	  
	  
	}
	 if(retMsg!=null&&retMsg!='')retIs=false;
	   return  EMPTools.message(retIs, retMsg) ;
  }catch(e){
	  
  }
} 


/* 页面重载例子
function _pageBussinessCheck(colId,colObj){
  var retMsg="";
	if(colId=='CusIndivTax.indiv_tax_amt'){
		retMsg= cheakAmt(colObj);  
	}   
	 if(colId=='CusIndivTax.indiv_tax_pay_amt'){
		    var colValue=parseFloat(colObj._getValue());
		    if(colValue>10){
		  	  retMsg="输入>10 -->:"+colValue;
		  	 
		    }   
	 }    
	 
	return retMsg;        

}	 
* */


// 将选中的icoll加入到form中 如：iCollSel2Form(CusLoanRelList,form,['cus_id','cus_name']);
// op中接收： iColl = (IndexedCollection)context.getDataElement("CusLoanRelList");
function iCollSel2Form(icoll,form,keys,prefix){
	
	var idx = icoll._obj.getSelectedIdx(); //这个是获取选中的索引号!=icoll.size
	if (idx.length==0) return null;
	var paramIdx = 0;
  var id="";
  var idValue="";
	for (var i=0;i<idx.length;i++) {
		for (var k=0;k<keys.length;k++) {
			//sb += "&";
			id="";
			idValue="";
			id +=icoll._obj.dataName+"["+paramIdx+"].";
			idValue = icoll._obj.data[idx[i]][keys[k]]._getValue();//keys[k]是列表中字段名，赞没找到抽象的写法
			if(prefix)
				id += prefix+"."+keys[k];
			else
				id += keys[k];
			
			id2Form(form,id,idValue);
			
		}
		if(icoll._obj.selectType == 2 && icoll._obj.dataName != null && icoll._obj.dataName != ""){
			paramIdx ++;
		}
	}

	 	
	
}

//iColl2FormAll(form,CusLoanRelList,selectIColl);
function iColl2FormAll(form,FiColl,ToiColl){
	var idx = FiColl._obj.getSelectedIdx();
	if (idx.length==0) return null;
	var preK=0;
	 for(var k=0;k<idx.length;k++){
		 ToiColl._addKColl(FiColl._obj.data[idx[k]]);		
		 
	 }
     ToiColl._toForm(form);

}


//将id加入form 如:id2Form(form,'cus_id','1234');
function id2Form(form,id,value){
	 
	var input = form[id];
	if(input == null){
		input = form.document.createElement("input");
		input.type="hidden";
		input.name = id;
		form.appendChild(input);	
		form[id] = input;// 强制在form对象中将新建的隐藏域放入
	}
	if(value != null && value != "")
		input.value = value;
	}	


/*add by zhoujf 2009-07-13
 * 根据参数表模型ID，传递担保方式，产品编号，客户号，额度使用标识获取授信额度编号和金额
 */
function getUrlToNo(modelId){
	
    var sessionId = document.getElementsByName("EMP_SID")[0].value;
    var cusId = modelId.cus_id._obj.element.value;
	var bizType = modelId.biz_type._obj.element.value;
	var dbfs = modelId.assure_means_main._obj.element.value;
	var apply_amount = modelId.apply_amount._getValue();
	
	var lmt_type = '';
	if( modelId.limit_ind._obj.element.value == '2'){
		lmt_type = '10';
	}else
	if(  modelId.limit_ind._obj.element.value == '4'){
		lmt_type = '20';
	}
	if(lmt_type!=''&&(dbfs==""||dbfs==null)){
		alert("请先选择'担保方式'");
		 modelId.limit_ind._obj.element.value="";
		return false;
	}
	 
	if(modelId.limit_ind._obj.element.value == '1'||
	   modelId.limit_ind._obj.element.value =='')//不使用授信
	{
		modelId.limit_ma_no._obj.config.required=false;
		modelId.limit_ma_no._obj.config.hidden=true;
		modelId.limit_ma_no._obj.element.value="";
		modelId.limit_ma_no._obj._renderStatus();
		
		modelId.limit_amt._obj.config.required=false
		modelId.limit_amt._obj.config.hidden=true;
		modelId.limit_amt._obj.element.value="";
		modelId.limit_amt._obj._renderStatus();
		
		modelId.limit_acc_no._obj.config.required=false
		modelId.limit_acc_no._obj.config.hidden=true;
		modelId.limit_acc_no._obj.element.value="";
		modelId.limit_acc_no._obj._renderStatus();
		
	}else{
		modelId.limit_ma_no._obj.config.required=true
		modelId.limit_ma_no._obj.config.hidden=false;
		modelId.limit_ma_no._obj.element.value="";
		modelId.limit_ma_no._obj._renderStatus();
			
		modelId.limit_amt._obj.config.required=true
		modelId.limit_amt._obj.config.hidden=false;
		modelId.limit_amt._obj.element.value="";
		modelId.limit_amt._obj._renderStatus();
		
		
		modelId.limit_acc_no._obj.config.required=true
		modelId.limit_acc_no._obj.config.hidden=false;
		modelId.limit_acc_no._obj.element.value="";
		modelId.limit_acc_no._obj._renderStatus();
	}
	
	
	
	modelId.limit_ma_no._obj.config.url = "";
	//modelId.limit_ma_no._obj.config.url = contextPath+'/queryLmtContDetailsListForLoan.do?EMP_SID='+sessionId
	//+'&returnMethod=getLmtNo' +'&cus_Id='+cusId+"&biz_type="+bizType+"&guar_type="+dbfs+"&crd_lmt_type="+lmt_type;
	//modelId.limit_ma_no._obj.config.url = EMPTools.encodeURI(modelId.limit_ma_no._obj.config.url);

	modelId.limit_ma_no._obj.config.url = contextPath+'/queryLmtContForLoanPopList.do?EMP_SID='+sessionId
	+'&returnMethod=getLmtNo' +'&cus_Id='+cusId+"&biz_type="+bizType+"&guar_type="+dbfs+"&crd_lmt_type="+lmt_type+"&apply_amount="+apply_amount;
	modelId.limit_ma_no._obj.config.url = EMPTools.encodeURI(modelId.limit_ma_no._obj.config.url);
	

	
}

function checkLimitYoN(modelId){
	   if(modelId.limit_ind._obj.element.value == '1'||
			   modelId.limit_ind._obj.element.value =='')//不使用授信
			{
				modelId.limit_ma_no._obj.config.required=false;
				modelId.limit_ma_no._obj.config.hidden=true;
				modelId.limit_ma_no._obj.element.value="";
				modelId.limit_ma_no._obj._renderStatus();
				
				modelId.limit_amt._obj.config.required=false
				modelId.limit_amt._obj.config.hidden=true;
				modelId.limit_amt._obj.element.value="";
				modelId.limit_amt._obj._renderStatus();
				
				modelId.limit_acc_no._obj.config.required=false
				modelId.limit_acc_no._obj.config.hidden=true;
				modelId.limit_acc_no._obj.element.value="";
				modelId.limit_acc_no._obj._renderStatus();
				
			}else{
				modelId.limit_ma_no._obj.config.required=true
				modelId.limit_ma_no._obj.config.hidden=false;
				modelId.limit_ma_no._obj._renderStatus();
					
				modelId.limit_amt._obj.config.required=true
				modelId.limit_amt._obj.config.hidden=false;
				modelId.limit_amt._obj._renderStatus();
				
				
				modelId.limit_acc_no._obj.config.required=true
				modelId.limit_acc_no._obj.config.hidden=false;
				modelId.limit_acc_no._obj._renderStatus();
			}
			

}
//end by zhoujf 2009-07-13
 
//动态过滤下拉框  elementObj:要过滤的下拉框对象,separator：分隔符,removeStr:用分隔符拼成的要过滤的字符串
function removeOpts(elementObj,removeStr,separator){
  var options = elementObj._obj.element.options;
  for ( var i = options.length - 1; i >= 0; i--) {
	    if(options[i].value!=''){//保留 '请选择'
		    var optI=options[i].value+separator;
			if(removeStr.indexOf(optI)!=-1){			
			  options.remove(i);
			}
	    }		
	}
  
  
} 


/**
 * 判断是否为有效年份
 * @param str
 * @return
 */
function IsYear(str) 
{ 
       var re = new RegExp(/\d{4}((0[1-9])|(1[0-2]))/); 
             if (re.test(str)==false){
                        alert("您输入的年份有误！")
                        eval('b.'+'focus()');
                        eval('b.value=0');
             }  
} 

/**
 * 判断是否闰年
 * @param rq
 * @return
 */
function isLeapYear(rq) 
{ 

return (0==this.getYear()%4&&((this.getYear()%100!=0)||(this.getYear()%400==0))); 
} 

/**
 * 将string转换为date  yyyy-mm-dd
 * @param DateStr
 * @return
 */
function StringToDate(DateStr) 
{ 
var converted = Date.parse(DateStr); 
var myDate = new Date(converted); 
if (isNaN(myDate)) 
{ 
//var delimCahar = DateStr.indexOf('/')!=-1?'/':'-'; 
var arys= DateStr.split('-'); 
myDate = new Date(arys[0],--arys[1],arys[2]); 
} 
return myDate; 
}


/**
 * 获取将日期加某类时间后的日期类型
 * @param date
 * @param strInterval s-秒；n
 * @param Number
 * @return
 */

function DateAdd(date,strInterval, Number) { 
var dtTmp = StringToDate(date); 
switch (strInterval) { 
case 's' :return new Date(Date.parse(dtTmp) + (1000 * Number)); 
case 'n' :return new Date(Date.parse(dtTmp) + (60000 * Number)); 
case 'h' :return new Date(Date.parse(dtTmp) + (3600000 * Number)); 
case 'd' :return new Date(Date.parse(dtTmp) + (86400000 * Number)); 
case 'w' :return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number)); 
case 'q' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number*3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
case 'm' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
case 'y' :return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
} 
} 


/**
 * 
 * @param date
 * @param strInterval
 * @param Number
 * @return
 */
function getDate(date,strInterval, Number){
return getYYYYMMDD(DateToString(DateAdd(date,strInterval, Number),'-'),'-');
}

/**
 * 获取日期的前一天日期
 * @param date
 * @return
 */
function getYD(date){
return getYYYYMMDD(DateToString(DateAdd(date,'d', -1),'-'),'-');        
}

/**
 * 获取六个月后的前一天日期
 * @param d
 * @return
 */
function getSMYD(d){
return getYD(getDate(d,'m',6));
}

/**
 * 将date转换为字符串
 * @param d
 * @param sep
 * @return
 */
function DateToString(d,sep){
var s;
s = d.getYear()+sep;                        
s += (d.getMonth() + 1) + sep;           
s += d.getDate() ;                  
return s;     

}

/**
 * 将yyyy？m？d 转换为yyy？mm？dd格式
 * @param d
 * @param sep
 * @return
 */
function getYYYYMMDD(d,sep){
 var da=d.split(sep);

 if(da[1].length==1){
 da[1]="0"+da[1];
 }
 
  if(da[2].length==1){
 da[2]="0"+da[2];
 }
 
 return da[0]+sep+da[1]+sep+da[2];
}



//计算两个日期之间的天数
function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
  
    var  aDate,  oDate1,  oDate2,  iDays  
    aDate  =  sDate1.split("-")  
    oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])    //转换为12-18-2006格式  
    aDate  =  sDate2.split("-")  
    oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])  
    iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
    return  iDays  
}


//pop open前添加单击事件 如 iqpAccpAppDetail.jsp  popClickFun(IqpAccpApp.limit_ma_no,'getAccpUrl') gaozh
function popClickFun(popObj,clickFun){
	 if(popObj){
	  var popBtn=popObj._obj.tag.getElementsByTagName("BUTTON")[0];
	  if(window[clickFun]){
	     EMPTools.addEvent(popBtn,"click",window[clickFun], window);
	  }
	 } 
	
}

//select设置 gaozh	
/*
 * optObj要设置的select emp对象
 * id ,name option属性
 * selValue 选中值
 * append 是否添加 false 则先清空
 */
function  addOpt(optObj,id,name,selValue,append){
	var options = optObj._obj.element.options;
	if(!append) options.length=0;
    var newOpt=new Option(name,id); 
    options.add(newOpt);
    if(selValue==id){
		newOpt.selected=true;
		sel=true;
	}
}

/**
 * 检查数字域是否大于0
 * @param amt    标签id
 * @param msg    标签lable
 * @return
 */
function cheakGtZero(amt,msg){
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<=0){
		  alert(msg+"值应大于零！");
		  amt._obj.element.value="0";
		  return amt._obj.element.focus();
	  }
	  
}
/**
 * 检查数字域是否大于等于0
 * @param amt    标签id
 * @param msg    标签lable
 * @return
 */
function cheakGtOrEqZero(amt,msg){
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<0){
		  alert(msg+"值不能为负数！");
		  amt._obj.element.value="0";
		  return amt._obj.element.focus();
	   }
	}

/**
 * 提交时使用
 * @param amt
 * @param msg
 * @return
 */
function cheakGtZero4Submit(amt,msg){
	  var getAmt = parseFloat(amt._getValue());
	  var rv=false;
	  if(getAmt<=0){
		  alert(msg+"值应大于零！");
		  amt._obj.element.value="";
		 
	  }else{
		   rv=true;
	   }  
	  return rv;
	  
}
/**
* 检查数字域是否大于等于0
* @param amt    标签id
* @param msg    标签lable
* @return
*/
function cheakGtOrEqZero4Submit(amt,msg){
	  var rv=false;
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<0){
		  alert(msg+"值不能为负数！");
		  amt._obj.element.value="";
		 
	   }else{
		   rv=true;
	   }  
	  return rv;
	}

/**
 * 检查数值域
 * @param amt 标签id
 * @param msg 标签lable
 * @param falg
 *        "gt"   >
 *        "lt"   <
 *        "eq"   =
 *        "neq"  ≠
 *        "le"   ≤
 *        "ge"   ≥
 * @param num 要比较的数值
 * @return
 */
function cheakNumThanNum(amt,msg,falg,num){
	  var getAmt = parseFloat(amt._getValue());
	  if(num==null||isNaN(num)){
			alert("您传入的比较值不是数值型！");
			amt._obj.element.value="";
			amt._obj.element.focus();
		}
	  if(flag=="gt"){
		  if(getAmt>num){
			  alert(msg+"值不能大于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus();
		   }
	  } else if(flag=="lt"){
		  if(getAmt<num){
			  alert(msg+"值不能小于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus();
		   }
	  }	else if(flag=="eq"){
		  if(getAmt=num){
			  alert(msg+"值不能等于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus();
		   }  
	  }else if(flag=="neq"){
		  if(getAmt!=num){
			  alert(msg+"值应等于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus();
		   }  
	  }else if(flag=="le"){
		  if(getAmt<= num){
			  alert(msg+"值不能小于或等于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus()
		   }  
	  }else if(flag=="ge"){
		  if(getAmt>=num){
			  alert(msg+"值不能大于或等于"+num+"！");
			  amt._obj.element.value="";
			  return amt._obj.element.focus()
		   }  
	  }
	}

function CheckDateFor2(date1,date2){
	var start = date1._obj.element.value;
	var end = date2._obj.element.value;
	if(end!=null && end!="" ){
		var flag = CheckDateAfterToday(end);
		if(!flag){
			alert("您输入的日期应大于当前日期！");
			date2._obj.element.value="";
		}else{
			if(start!=null && start!=""){
				var ff = CheckDate1BeforeDate2(end,start);
				if(ff){
					alert("到期日期不小于起始日期！");
					date2._obj.element.value="";
				}
			}
	    }
	}
}

//格式化数字  pattern 为 0000.00 或##.00
function format(number,pattern){
    var str            = number.toString();
    var strInt;
    var strFloat;
    var formatInt;
    var formatFloat;
    if(/\./g.test(pattern)){
        formatInt        = pattern.split('.')[0];
        formatFloat        = pattern.split('.')[1];
    }else{
        formatInt        = pattern;
        formatFloat        = null;
    }
    if(/\./g.test(str)){
        if(formatFloat!=null){
            var tempFloat    = Math.round(parseFloat('0.'+str.split('.')[1])*Math.pow(10,formatFloat.length))/Math.pow(10,formatFloat.length);
            /*modified by gaozh 负数处理*/
            if(number.toString().indexOf('e-')!=-1){
            	var eInt=(number.toString().substr(number.toString().indexOf('e-')+2,number.toString().length));
            	if(parseInt(eInt)>0)number=0;
            } 
            if(Math.floor(number)<0)
              strInt        = (parseInt(number)+Math.floor(tempFloat)).toString(); 
            else    

	        //end     
              strInt        = (Math.floor(number)+Math.floor(tempFloat)).toString();                
            strFloat    = /\./g.test(tempFloat.toString())?tempFloat.toString().split('.')[1]:'0';            
        }else{
            strInt        = Math.round(number).toString();
            strFloat    = '0';
        }
    }else{
        strInt        = str;
        strFloat    = '0';
    }
    if(formatInt!=null){
        var outputInt    = '';
        var zero        = formatInt.match(/0*$/)[0].length;
        var comma        = null;
        if(/,/g.test(formatInt)){
            comma        = formatInt.match(/,[^,]*/)[0].length-1;
        }
        var newReg        = new RegExp('(\\d{'+comma+'})','g');

        if(strInt.length<zero){
            outputInt        = new Array(zero+1).join('0')+strInt;
            outputInt        = outputInt.substr(outputInt.length-zero,zero)
        }else{
            outputInt        = strInt;
        }

        var 
        outputInt            = outputInt.substr(0,outputInt.length%comma)+outputInt.substring(outputInt.length%comma).replace(newReg,(comma!=null?',':'')+'$1')
        outputInt            = outputInt.replace(/^,/,'');

        strInt    = outputInt;
    }

    if(formatFloat!=null){
        var outputFloat    = '';
        var zero        = formatFloat.match(/^0*/)[0].length;

        if(strFloat.length<zero){
            outputFloat        = strFloat+new Array(zero+1).join('0');
            //outputFloat        = outputFloat.substring(0,formatFloat.length);
            var outputFloat1    = outputFloat.substring(0,zero);
            var outputFloat2    = outputFloat.substring(zero,formatFloat.length);
            outputFloat        = outputFloat1+outputFloat2.replace(/0*$/,'');
        }else{
            outputFloat        = strFloat.substring(0,formatFloat.length);
        }

        strFloat    = outputFloat;
    }else{
        if(pattern!='' || (pattern=='' && strFloat=='0')){
            strFloat    = '';
        }
    }

    return strInt+(strFloat==''?'':'.'+strFloat);
}



//异步方法获取基准年利率
function getRateBasePub(term,modelId) {
	if(!IsInteger(term)){
		alert("请您输入正确的期限类型！");
		eval(modleId+".apply_term._obj.element.value=''");
		return eval(modelId+'.apply_term._obj.element.focus()');
	}
	var url = '<emp:url action="getRateBaseFromPrd.do?apply_term=' + term + '&ir_type=10"/>'
	var handleSuccess = function(o) {
		setRateBasePub(o,modelId);
	};
	var handleFailure = function(o) {
		alert("获取基准利率失败！");
	};
	var callback = {
		success :handleSuccess,
		failure :handleFailure
	};
	var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
}


//设置基准年利率和月利率
function setRateBasePub(o,modelId) {
	
	if (o.responseText !== undefined) {
		try {
			var jsonstr = eval("(" + o.responseText + ")");
		} catch (e) {
			alert("Parse jsonstr define error!" + e.message);
			return;
		}
		var jsonStr = jsonstr.rateBase; //从产品中获取基准年利率
		
		if (jsonStr != null && jsonStr != '') {
			//alert(modelId+".ruling_ir._obj.element.value=format(jsonStr*100,'#.000000')+'%'");
			eval(modelId+".ruling_ir._obj.element.value=format(jsonStr*100,'#.000000')+'%';");        //基准年利率
			eval(modelId+".sug_ruling_ir._obj.element.value=format(jsonStr*1000/12,'#.000000')+'‰';"); //基准月利率
			//设置基准利率
			//eval(modelId+".reality_ir_y._obj.element.value=format(jsonStr,'#.000000')*100+'‰'");  //执行月利率
			//eval(modelId+".reality_ir_m._obj.element.value=format(jsonStr/12,'#.000000')*100+)'‰'";  //执行月利率

			//设置执行利率
			var floatRate=eval(modelId+".floating_rate._obj.element");
			setRealIRPub(floatRate.value,floatRate,'auto',modelId);
			

			//设置逾期利率
			var overRate= eval(modelId+".overdue_rate._obj.element");
			setOverIRPub(overRate.value,overRate,'over',modelId);
			//设置违约利率
			var defaultRate=eval(modelId+".default_rate._obj.element");
			setOverIRPub(defaultRate.value,defaultRate,'default',modelId);
			//设置其他利率
			var ciRate=eval(modelId+".ci_rate._obj.element");
			setOverIRPub(ciRate.value,ciRate,'ci',modelId);
			/*eval(modelId+".reality_ir_m._obj.element.value=jsonStr/12");  //执行月利率
			eval(modelId+".overdue_ir._obj.element.value=jsonStr/12");      //逾期月利率
			eval(modelId+".default_ir._obj.element.value=jsonStr/12");       //违约月利率
			*/
		}
	}
}

//设置执行利率
function setRealIRPub(value,obj,type,modelId) {
	//var floatingRate=percentFilterPubH(value);
	if(isNaN(percentFilterPubH(value))){
		alert("您输入的利率浮动值有误！");
		obj.focus();
		return;
	}
	var floatingRate=parseFloat(percentFilterPubH(value));

	//检查基准利率是否合法
	checkRateBasePub(modelId);

	var realIR=0;
	var realIRM=0;
	var rulingIR=percentFilterPubH(eval(modelId+".ruling_ir._obj.element.value"));

	realIR=(rulingIR*(100+floatingRate))/100;
	realIRM=realIR*10/12;
	eval(modelId+".reality_ir_y._obj.element.value=format(realIR,'#.000000')+'%'");
	eval(modelId+".reality_ir_m._obj.element.value=format(realIRM,'#.000000')+'‰'");

	if(type!='auto'){
		
		//设置逾期利率
		var overRate= eval(modelId+".overdue_rate._obj.element");
		setOverIRPub(overRate.value,overRate,'over',modelId);
		//设置违约利率
		var defaultRate=eval(modelId+".default_rate._obj.element");
		setOverIRPub(defaultRate.value,defaultRate,'default',modelId);
		//设置其他利率
		var ciRate=eval(modelId+".ci_rate._obj.element");
		setOverIRPub(ciRate.value,ciRate,'ci',modelId);
	}

}

	//设置逾期或违约利率
	function setOverIRPub(value,obj,type,modelId){
	
	if(isNaN(percentFilterPubH(value))){
		alert("您输入的值有误！");
		obj.focus();
		return;
	}

	var val=parseFloat(percentFilterPubH(value));
	
	//检查基准利率是否合法
	checkRateBasePub(modelId);
	//检查执行利率是否合法
	checkRealRatePub(modelId);

	
	var rulingIR=percentFilterPub(eval(modelId+".reality_ir_m._obj.element.value"));

	
	var realIR=rulingIR*(100+val)/100;
	if(type=='over'){	
		eval(modelId+".overdue_ir._obj.element.value=format(realIR,'#.000000')+'‰'");
	}else if(type=='default'){
		eval(modelId+".default_ir._obj.element.value=format(realIR,'#.000000')+'‰'");
	}else{
		eval(modelId+".ci_ir._obj.element.value=format(realIR,'#.000000')+'‰'");
	}	
	}

//检查基准利率是否获取
function checkRateBasePub(modelId){

	var term=percentFilterPub(eval(modelId+".apply_term._obj.element.value"));
	var rulingIR=percentFilterPubH(eval(modelId+".ruling_ir._obj.element.value"));
	var rulingIRM=percentFilterPub(eval(modelId+".sug_ruling_ir._obj.element.value"));
	if(term==null||isNaN(term)){
		alert("您还没有输入期限并获取基准利率！");
		eval(modelId+".apply_term._obj.element.focus()");
	}
	
	if(rulingIR==null||rulingIRM==null||isNaN(rulingIR)||isNaN(rulingIRM)){
		alert("您还没有获取基准利率！");
		
	}		
}

//检查基准利率是否获取
function checkRealRatePub(modelId){
	var floating=percentFilterPubH(eval(modelId+".floating_rate._obj.element.value"));
	var IR=percentFilterPubH(eval(modelId+".ruling_ir._obj.element.value"));
	var IRM=percentFilterPub(eval(modelId+".sug_ruling_ir._obj.element.value"));

	if(floating==null||isNaN(floating)){
		alert("您还没有输入利率浮动比！");
		eval(modelId+".floating_rate_obj.element.focus()");
		}

	if(IR==null||IRM==null||isNaN(IR)||isNaN(IRM)){
		alert("您还没有获取执行利率！");
	}
}

//把金额的"‰"过滤掉
function percentFilterPub(amt) {
	if (amt == null) {
		return null;
	}
	
	if(amt.indexOf("‰")==-1){
		return amt;//*1000
	}
	
	
	var amtStr = amt;
	var amtStrTmp = amt;
	
	
	while (amtStrTmp.indexOf("‰") != -1) {
		amtStr = amtStr.replace("‰", "");
		amtStrTmp=amtStr;
	}
	return amtStr;
}

function percentFilterPubH(amt) {
	if (amt == null) {
		return null;
	}
	
	if(amt.indexOf("%")==-1){
		return amt;//*100
	}
	
	var amtStr = amt;
	var amtStrTmp = amt;
	
	
	while (amtStrTmp.indexOf("%") != -1) {
		amtStr = amtStr.replace("%", "");
		amtStrTmp=amtStr;
	}
	return amtStr;
}

//重置执行利率信息
function reSetRealIRPub(modelId){
	eval(modelId+".floating_rate_obj.element.value=0");   //利率浮动值
	eval(modelId+".ruling_ir._obj.element.value=0");		 //执行年利率
	eval(modelId+".sug_ruling_ir._obj.element.value=0");  //执行月利率
}

//重置违约及加罚信息
function reSetOtherIRPub(modelId){
	eval(modelId+".overdue_rate._obj.element.value=0");	//逾期浮动比例
	eval(modelId+".overdue_ir._obj.element.value=0");    //逾期月利率
	eval(modelId+".default_rate._obj.element.value=0");  //转移用途浮动比例
	eval(modelId+".default_ir._obj.element.value=0");    //违约月利率
}

//将toObj中的options选项用fromObj中的替换，并根据selValue设置选择状态(add by gaozh 2009.05.18)
function setObjOptByObj(toObj,fromObj,selValue){
  if(toObj._obj.element.options && fromObj._obj.element.options){
  	var toObjOpts = toObj._obj.element.options;
  	var fromObjOpts = fromObj._obj.element.options;
  	toObjOpts.length=0;//清空
  	//for(var j=toObjOpts.length-1;j>=0;j--)toObjOpts.remove(j);
  	var isSel=false;
		for ( var i = 0; i<fromObjOpts.length; i++) {
			 
			var newOpt=new Option(fromObjOpts[i].innerText,fromObjOpts[i].value); 
			toObjOpts.add(newOpt);
			if(!isSel && selValue==fromObjOpts[i].value){
				newOpt.selected=true;
				isSel=true;
			}	
		}
 
  }
	
}

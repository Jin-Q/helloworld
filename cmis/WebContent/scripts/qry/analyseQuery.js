
function doSelect__Oper(_obj,inputname){
	var opername=inputname+"_OPER";
	var valuename2=inputname+"_VALUE2";
	var buttonname2=inputname+"_BUTTON2";
	document.getElementById(opername).value=_obj.value;
	if (_obj.value!='6'){
		try{
			document.getElementById(valuename2).disabled=true;
			document.getElementById(valuename2).value="";   //日期不为区间时 清空结束日期值   2014-06-12 唐顺岩
		}catch(e){}			 
		try{
			document.getElementById(buttonname2).disabled=true;
		}catch(e){}
	}else{
		try{
			document.getElementById(valuename2).disabled="";
		}catch(e){}	
		try{
			document.getElementById(buttonname2).disabled="";	
		}catch(e){}	
	}
	
}

function openCalendar(obj){
	if (page.calendarObj == null) {
		page.calendarObj = new Calendar("page.calendarObj");
		var tempDiv = document.createElement("DIV");
		document.body.appendChild(tempDiv);
		tempDiv.innerHTML += page.calendarObj;
	}
	page.calendarObj.show(obj);
	
}

function onblurPopCalendar(inputname){
//验证格式&转换样式
		if (inputname==""){
			return "";
		}	
		var strDate = document.getElementById(inputname).value;
		if (strDate==""){
			return "";
		}	
		
		var regDate = /^(\d{8})$/;
		result = new String(strDate.match(regDate));				
		if(result!="null"){
			//如果输入为8位数字
			strDate= result.substring(0,4)+"-"+result.substring(4,6)+"-"+result.substring(6,8);
		}
		else{
			//如果不是8位数字				
			//"-",".","/","\"
			var reg = /[-|\\|\.|\/|\s]/g;
			strDate = strDate.replace(reg, "-");
			//将上述截断符统一更换成"-"
			//"dddd-dd-dd"
		}	
			regDate = /^(\d{1,4})(-)(\d{1,2})\2(\d{1,2})$/;
			var result = strDate.match(regDate);

			if ( result == null ){
				alert("不符合日期格式!请输入yyyymmdd，yyyy-mm-dd格式");
				return null;
			}
			var month = ((""+result[3]).length < 2)?("0" + result[3]):("" + result[3]);
			
			var day = ((""+result[4]).length < 2)? ("0" + result[4]):("" + result[4]);
		
		strDate = result[1] + result[2] + month + result[2] + day;
		var date = new Date(result[1], result[3]-1,result[4]);
		
		month = ((date.getMonth() + 1) < 10)?("0" + (date.getMonth() + 1)):("" + (date.getMonth() + 
		
		1));
		day = (date.getDate() < 10)?("0" + date.getDate()):("" + date.getDate());
		var newStr=date.getFullYear() + result[2] + month + result[2] + day;
		if(newStr == strDate){
			document.getElementById(inputname).value=newStr;
		}else
		{
			oConfig.errorMsg="不符合日期格式!";
			return null;
		}

	
	//其他操作在格式修改之后执行
		if(window.onblurPopCalendar_after){
			onblurPopCalendar_after(inputname);
		}
}

function onblurDecimalInput(inputname){
//验证格式，不再四舍五入
		var input =document.getElementById(inputname).value;
		if(input==""){
			return "";//如果未输入任何数据，则不作格式检查和转换。
		}
		if(typeof(input)!='string'){
			input=input.toString();
		}

		//检查格式
		var reg = /^\-?[\d\,]*\.?\d*$/;
		var checkres = reg.exec(input);
		if (!checkres) {
			alert("不是金额数据格式!");
			return null;
		}
//验证通过开始进行客户自定义函数
		if(window.onblurDecimalInput_after){
			onblurDecimalInput_after(inputname);
		}
}


function onblurTextInput(inputname){
//不需要验证格式

//开始进行客户自定义函数
		if(window.onblurTextInput_after){
			onblurTextInput_after(inputname);
		}
}



function onblurCombobox(inputname){
//修改onblur
	
	//其他操作在格式修改之后执行
		if(window.onblurCombobox_after){
			onblurCombobox_after(inputname);
		}
}

//选择显示字段和排序字段所需js

  function add2List(fromList,toList) {
	var i = 0;


    var nLen;
	var oOption = document.createElement("OPTION");

 
	nLen = document.getElementById(fromList).options.length;
	for(i=0;i<nLen;i++){
	  if(document.getElementById(fromList).options[i].selected){
 
		    oOption.text = document.getElementById(fromList).options[i].text ;
		    oOption.value = document.getElementById(fromList).options[i].value;
 
		i=-1;
		break;
	  }
	}
	if(i > -1){
	   alert("请选择一个要加入列表的字段！");
	   return;
	}


	nLen = document.getElementById(toList).options.length;
	for(i=0;i<nLen;i++){
	  //alert(oOption.value+"\n"+document.all.toList.options[i].value);
	  if(oOption.value==document.getElementById(toList).options[i].value
		   || oOption.value==(document.getElementById(toList).options[i].value + " desc") || (oOption.value + " desc") == document.getElementById(toList).options[i].value ){
		alert("在列表已经存在该字段，请重新选择!");
		return;
	  }
	}

	document.getElementById(toList).options.add(oOption);
	return;
  }

  function del2List(toList) {
	var i,j=0;
	var nLen;
	nLen = document.getElementById(toList).options.length;
	for(i=0;i<nLen;i++){
	  if(document.getElementById(toList).options[i].selected){
		document.getElementById(toList).options.remove(i);
		i--;
		nLen--;
		j=-1;
	  }
	}
	if(j > -1){
	   alert("请先选择一个要删除的字段！");
	   return;
	}	
	return;
  }
  


function doPopWindow(tablename,selectname,enname){
	//如果自定义函数存在则直接调用自定义函数
	if(doPopWindow_modify){
		if(doPopWindow_modify(tablename,selectname,enname)){
		//如果在doPopWindow_modify中返回true,则不执行下方的默认函数
		//不修改doPopWindow_modify的话会默认返回false;
			return;
		}
	}
	//默认pop函数
	
	var url =getDefaltUrl();
	//将url中的xxxxxxx换成所需要的
	//注意，由于有可能在url里夹带参数。所以下边会有根据tablename里是否"?"有来进行判断
	if(tablename.indexOf("?")>-1){
		url=url.replace("?","&")
		//去掉"?",变成"&"
	}
	
	
	url=url.replace('xxxxxxx',tablename);
	url=url+"&popReturnMethod=onReturn__"+enname+"&returnMethod=onReturn__"+enname;
	window.open(url,'popwin_'+enname,'height=650, width=1000, top=100, left=100, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');

}


function doSubmit2(){
	//验证查询数量是否在1000-50000之间的整数。
	var INT_COUNT=document.getElementById("INT_COUNT").value;
	if(INT_COUNT==""){
		alert("请输入查询条数");
		return "";//如果未输入任何数据，则不作格式检查和转换。
	}
	var reg =/^[0-9]{1,20}$/;
	var checkres = reg.exec(INT_COUNT);
	if (!checkres) {
		oConfig.errorMsg="只能包含数字!";
		return null;
	}
	INT_COUNT=parseInt(INT_COUNT);
	if(50000<INT_COUNT){
		alert("查询条数应小于50000条");
		return;
	}
	
	if( window.beforeSubmit ){
          if( !beforeSubmit() ){
		      return;
		  }
    }

	//获取选择字段
	var nLen=document.getElementById('toListToSelect').options.length;
	var ShowColumns;
	ShowColumns = (0 == nLen)?'':'|';
	for(i=0;i<nLen;i++){
		ShowColumns += document.getElementById('toListToSelect').options[i].value+'|';
	}	
	document.getElementById('ShowColumns').value=ShowColumns;
	
	//获取排序字段
	nLen=document.getElementById('toListToOrder').options.length;
	var OrderByColumns='';
	for(i=0;i<nLen;i++){
		OrderByColumns += document.getElementById('toListToOrder').options[i].value+',';
	}
	OrderByColumns=OrderByColumns.substring(0,OrderByColumns.length-1)
	document.getElementById('OrderByColumns').value=OrderByColumns;
	var submitForm = document.getElementById("submitForm");
	submitForm.submit();
}
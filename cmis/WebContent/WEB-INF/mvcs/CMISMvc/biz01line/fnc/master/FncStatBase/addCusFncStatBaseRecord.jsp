<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
//登录人
String currentUserId=(String)context.getDataValue("currentUserId");
//机构
String organno=(String)context.getDataValue("organNo");
//是否是财务简表
String isSmp = request.getParameter("isSmp");
%>
<emp:page>
<html>
<head>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b37c33;
	text-align: left;
	width: 450px;
}
</style>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		//原来的做法
		EMPTools.addEvent(FncStatBase.stat_prd_style._obj.element, "change", doChange, FncStatBase.stat_prd_style);
	};
	
	function doAddFncStatBase(){
		var form = document.getElementById('submitForm');
		var result = FncStatBase._checkAll();
		if(result){
			var style_value = FncStatBase.stat_prd_style._obj.element.value;
			var prd_value = FncStatBase.stat_prd._obj.element.value;
			var yyyy = prd_value.substring(0,4);//截取年
			CheckYearBeforeToday(yyyy);//先校验年
			var mm = prd_value.substring(4);
			if(style_value=="1"){//月
				if(mm == 01 || mm == 02 || mm == 03 || mm == 04 
					|| mm == 05 || mm == 06 || mm == 07 || mm == 08 
						|| mm == 09 || mm == 10 || mm == 11 || mm == 12){
					var flag = CheckDateAfterToday(prd_value);
					if(flag){
						return 	alert("报表期间要小于当前日期！");
					}
					
				}else{
					return 	alert("请输入正常的月份！");
				}
			}else if(style_value=="2"){//季
				if(mm == 03 || mm == 06 || mm == 09 || mm == 12){
					var flag = CheckDateAfterToday(prd_value);
					if(flag){
						return 	alert("报表期间要小于当前日期！");
					}
					
				}else{
					return alert("当报表财周期类型为季报时，月份只能输入０３、０６、０９或１２");
				}
			}else if(style_value=="3"){//半年
				if(mm == 06 || mm == 12){
					var flag = CheckDateAfterToday(prd_value);
					if(flag){
						return 	alert("报表期间要小于当前日期！");
					}
					
				}else{
					return 	alert("当报表财周期类型为半年报时，月份只能输入０６或１２");
				}	
			}else if(style_value=="4"){//年
				if(mm == 12){
					var flag = CheckDateAfterToday(prd_value);
					if(flag){
						return 	alert("报表期间要小于当前日期！");
					}
					
				}else{
					return 	alert("当报表财周期类型为年报时，月份只能输入１２");
				}
			}else{
				return alert("请选择报表周期类型！");
			}
			FncStatBase._toForm(form)
			//form.submit();
			toSubmitForm(form);
		}else{
			closeLock();
			alert("请输入必填项！");
		}
	};
	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				var cus_id = jsonstr.cus_id;
				var stat_prd_style = jsonstr.stat_prd_style;
				var stat_prd = jsonstr.stat_prd;
				var state_flg = jsonstr.state_flg;
				var stat_style = jsonstr.stat_style;
				var fnc_type = jsonstr.fnc_type;
				if(flag=="exist"){
					alert("该客户财报信息已经存在,无法进行新增操作！");
					return;
			    }else if(flag=="noexist"){
			    	var paramStr="cus_id="+cus_id+"&stat_prd_style="+stat_prd_style
					+"&stat_prd="+stat_prd+"&state_flg="+state_flg
					+"&stat_style="+stat_style+"&fnc_type="+fnc_type+"&isSmp=<%=isSmp%>";				    		
		    		var url = '<emp:url action="cusFncStat.do"/>&'+paramStr;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert(flag);
					return;
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	
	function doChange(){
		var curYear = getCurYear();
		var yearLimit = parseFloat(curYear)+5;
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var select = FncStatBase.stat_prd._obj.element;	
		var y = FncStatBase.stat_prd_year._obj.element;	
			select.length=1;
			
			if(style_value=="4"){
				FncStatBase.stat_prd_year._obj.config.hidden=true;
				FncStatBase.stat_prd_year._obj._renderStatus();
			}else{
				FncStatBase.stat_prd_year._obj.config.hidden=false;
				FncStatBase.stat_prd_year._obj._renderStatus();
				var m=0;
				for(var n=1999;n<yearLimit;n++){
					y.options[m+1] = new Option();
					y.options[m+1].value = n+1;
					y.options[m+1].text =  n+1+"年";
					
					//alert(curYear+"---"+(n+1));
					if(curYear==n+1){
						y.options[m+1].selected =  true;
					}	
					m++;
				}
			}		
			
		 if(style_value=="4"){
		  	var j=0;
			for(var i=1999;i<yearLimit;i++){ 
				select.options[j+1] = new Option();
				select.options[j+1].value = i+1+'12';
				select.options[j+1].text =  i+1+"年";
				j++;
			}
		}else if(style_value=="3"){
			for(var i=1;i<=2;i++){
				select.options[i] = new Option();	
				if("1"==i){
				select.options[i].value = ""+curYear+"0"+(i+5);
				select.options[i].text ="上半年";
				}else{
				select.options[i].value = ""+curYear+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
					select.options[i].value = ""+curYear+"0"+(i*3);
				}else{
					select.options[i].value = ""+curYear+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
					select.options[i+1].value = ""+curYear+"0"+(i+1);
				}else{
					select.options[i+1].value = ""+curYear+(i+1);
				}
				select.options[i+1].text =  i+1+"月";
			}
		}
	};

	function doChangey(){
		var y = FncStatBase.stat_prd_year._obj.element;	
		var curYear = y.value;
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var select = FncStatBase.stat_prd._obj.element;	
			select.length=1;
		 if(style_value=="4"){
		  	var j=0;
			for(var i=1999;i<2015;i++){ 
				select.options[j+1] = new Option();
				select.options[j+1].value = i+1+'12';
				select.options[j+1].text =  i+1+"年";
				j++;
			}
		}else if(style_value=="3"){
			for(var i=1;i<=2;i++){
				select.options[i] = new Option();	
				if("1"==i){
				select.options[i].value = ""+curYear+"0"+(i+5);
				select.options[i].text ="上半年";
				}else{
				select.options[i].value = ""+curYear+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
					select.options[i].value = ""+curYear+"0"+(i*3);
				}else{
					select.options[i].value = ""+curYear+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
					select.options[i+1].value = ""+curYear+"0"+(i+1);
				}else{
					select.options[i+1].value = ""+curYear+(i+1);
				}
				select.options[i+1].text =  i+1+"月";
			}
		}
	};

	function doChange3(){
		var curYear = getCurYear();
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var select = FncStatBase.stat_prd._obj.element;	
			select.length=1;
			
		 if(style_value=="4"){
		  	var j=0;
			for(var i=1999;i<2015;i++){ 
				select.options[j+1] = new Option();
				select.options[j+1].value = i+1+'12';
				select.options[j+1].text =  i+1+"年";
				j++;
			}
		}else if(style_value=="3"){
			for(var i=1;i<=2;i++){
				select.options[i] = new Option();	
				if("1"==i){
				select.options[i].value = ""+curYear+"0"+(i+5);
				select.options[i].text ="上半年";
				}else{
				select.options[i].value = ""+curYear+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
					select.options[i].value = ""+curYear+"0"+(i*3);
				}else{
					select.options[i].value = ""+curYear+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
					select.options[i+1].value = ""+curYear+"0"+(i+1);
				}else{
					select.options[i+1].value = ""+curYear+(i+1);
				}
				select.options[i+1].text =  i+1+"月";
			}
		}
	};
	
	function getCurYear(){
	//	var now=new Date();
	//	var year=now.getFullYear();
		var openDay = '${context.OPENDAY}';
		year = openDay.substring(0,4);
		return year;
	};
	function getMonth(){
		var now=new Date();
		var month=now.getMonth()+1
		return month;
	};
	function getDate(){
		var now=new Date();
		var date=now.getDate();
		return date;
	};
	
	function doChangeId(){
		var url = '<emp:url action="queryFncConfStylesListByCon.do"/>';
		var handleSuccess = function(o){	
			setCustomer(o);	
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);	
	};
		
	function setCustomer(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr define error!"+e.message);
				return;
			}
            var FncConfStylesList=jsonstr.FncConfStylesList;
            
			var bsStyleId = FncStatBase.stat_bs_style_id._obj.element;	
			var plStyleId = FncStatBase.stat_pl_style_id._obj.element;	
			var cfStyleId = FncStatBase.stat_cf_style_id._obj.element;	
			var fiStyleId = FncStatBase.stat_fi_style_id._obj.element;	
			var soeStyleId = FncStatBase.stat_soe_style_id.element;	
			var slStyleId = FncStatBase.stat_sl_style_id._obj.element;	
			//bsStyleId.length=1;
			var bsId = 1;
			var plId = 1;
			var cfId = 1;
			var fiId = 1;
			var soeId = 1;
			var slId = 1;
			for(var i=0;i<FncConfStylesList.length;i++){
			var fncConfTyp=FncConfStylesList[i].fnc_conf_typ;
				
			if(fncConfTyp=="01"){
				bsStyleId.options[bsId] = new Option();
				bsStyleId.options[bsId].value = FncConfStylesList[i].style_id;
				bsStyleId.options[bsId].text =  FncConfStylesList[i].fnc_conf_dis_name;
				bsId++;
			}else if(fncConfTyp=="02"){
				plStyleId.options[plId] = new Option();
				plStyleId.options[plId].value = FncConfStylesList[i].style_id;
				plStyleId.options[plId].text =  FncConfStylesList[i].fnc_conf_dis_name;
				plId++;
			
			}else if(fncConfTyp=="03"){
				cfStyleId.options[cfId] = new Option();
				cfStyleId.options[cfId].value = FncConfStylesList[i].style_id;
				cfStyleId.options[cfId].text =  FncConfStylesList[i].fnc_conf_dis_name;
				cfId++;
			
			}else if(fncConfTyp=="04"){
				fiStyleId.options[fiId] = new Option();
				fiStyleId.options[fiId].value = FncConfStylesList[i].style_id;
				fiStyleId.options[fiId].text =  FncConfStylesList[i].fnc_conf_dis_name;
				fiId++;
			
			}else if(fncConfTyp=="05"){
				soeStyleId.options[soeId] = new Option();
				soeStyleId.options[soeId].value = FncConfStylesList[i].style_id;
				soeStyleId.options[soeId].text =  FncConfStylesList[i].fnc_conf_dis_name;
				soeId++;
			
			}else if(fncConfTyp=="06"){
				slStyleId.options[slId] = new Option();
				slStyleId.options[slId].value = FncConfStylesList[i].style_id;
				slStyleId.options[slId].text = FncConfStylesList[i].fnc_conf_dis_name;
				slId++;
			}
		}
	 }
	};

	function doReturn() {
		if(window.parent && window.parent.refreshFnc){
			window.parent.refreshFnc();
		}else{
			var isSmp = '<%=isSmp%>';
			/**modified by lisj 2015-2-9 修复生产无效跳窗，于2015-2-9上线 begin**/
			//alert(isSmp);
			/**modified by lisj 2015-2-9 修复生产无效跳窗，于2015-2-9上线 end**/
			var url = '<emp:url action="queryCusFncStatBaseSmpList.do"/>&isSmp=isSmp';
			if(isSmp==null||isSmp==''||isSmp=='null'){
				url = '<emp:url action="queryCusFncStatBaseList.do"/>';
			}
			url = EMP.util.Tools.encodeURI(url);
			window.location=url;
		}
	};

	function doSelectedStatPrd(){
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var prd_value = FncStatBase.stat_prd._obj.element.value;
		var yyyy = prd_value.substring(0,4);//截取年
		CheckYearBeforeToday(yyyy);//先校验年
		var mm = prd_value.substring(4);
		if(style_value=="1"){//月
			if(mm == 01 || mm == 02 || mm == 03 || mm == 04 
				|| mm == 05 || mm == 06 || mm == 07 || mm == 08 
					|| mm == 09 || mm == 10 || mm == 11 || mm == 12){
				var flag = CheckDateAfterToday(prd_value);
				if(flag){
					alert("报表期间要小于当前日期！");
				}
				
			}else{
				alert("请输入正常的月份！");
			}
		}else if(style_value=="2"){//季
			if(mm == 03 || mm == 06 || mm == 09 || mm == 12){
				var flag = CheckDateAfterToday(prd_value);
				if(flag){
					alert("报表期间要小于当前日期！");
				}
				
			}else{
				alert("当报表财周期类型为季报时，月份只能输入０３、０６、０９或１２");
			}
		}else if(style_value=="3"){//半年
			if(mm == 06 || mm == 12){
				var flag = CheckDateAfterToday(prd_value);
				if(flag){
					alert("报表期间要小于当前日期！");
				}
				
			}else{
				alert("当报表财周期类型为半年报时，月份只能输入０６或１２");
			}	
		}else if(style_value=="4"){//年
			if(mm == 12){
				var flag = CheckDateAfterToday(prd_value);
				if(flag){
					alert("报表期间要小于当前日期！");
				}
				
			}else{
				alert("当报表财周期类型为年报时，月份只能输入１２");
			}
		}else{
			alert("请选择报表周期类型！");
		}
	};

	function onlyNum(){ 
		if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105))){
			//考虑小键盘上的数字键 
			event.returnvalue=false; 
			alert("请输入数字！");
		}else{
			doSelectedStatPrd();
		}
	};

	function CheckYearBeforeToday(yearIn){
    /*
     * 检查日期是否小于当日函数
     * 返回值为true是输入日期小于当日 为false输入日期大于或等于当日
     */		
		var   exp=/^\d{4}$/;  
  		var   x=exp.test(yearIn);   
  		if (!x){
  			alert("请输入6位数字");
  			return false;
  		}
		var date = '${context.OPENDAY}';
		//var str = date.getFullYear();
		var str = date.substring(0,4);
		if(yearIn-1900<0){
			alert("输入年份过早");
			return false;
		}else if(yearIn-str>0){
			alert("输入年份不应晚于当前年份");
			return false;
		}
		return true;
	};

	function dealAudit(){
		//是否经过调整
		//var adjt = FncStatBase.stat_is_adjt._obj.element.value;
		var audit = FncStatBase.stat_is_audit._getValue();
		
		if(audit=="1"){//经过审计
			FncStatBase.stat_adt_entr._obj.config.hidden=false;
			FncStatBase.stat_adt_entr._obj._renderStatus();
			
			FncStatBase.stat_adt_conc._obj.config.hidden=false;
			FncStatBase.stat_adt_conc._obj._renderStatus();
		}else{
			FncStatBase.stat_adt_entr._obj.config.hidden=true;
			FncStatBase.stat_adt_entr._obj._renderStatus();

			FncStatBase.stat_adt_conc._obj.config.hidden=true;
			FncStatBase.stat_adt_conc._obj._renderStatus();
		}		
	};
	
	function dealAdjt(){
		//是否经过调整
		//var adjt = FncStatBase.stat_is_adjt._obj.element.value;
		var adjt = FncStatBase.stat_is_adjt._getValue();
		
		if(adjt=="1"){//经过调整
			FncStatBase.stat_adj_rsn._obj.config.hidden=false;
			FncStatBase.stat_adj_rsn._obj._renderStatus();
		}else{
			FncStatBase.stat_adj_rsn._obj.config.hidden=true;
			FncStatBase.stat_adj_rsn._obj._renderStatus();
		}		
	};
	
	function CheckDateAfterToday(dateIn){
    /*
     * 检查日期是否大于当日函数
     * 返回值为true是输入日期晚于当日 为false输入日期大于或等于当日
     */			
 
		//var date=new Date();
		var date = '${context.OPENDAY}';
		dateInYear=dateIn.substring(0,4);
		dateInMonth=dateIn.substring(4);
		//dateStr=date.getFullYear();
		dateStr=date.substring(0,4);
		if (dateStr-dateInYear>0){
			return false;
		}else if(dateStr-dateInYear<0){
			return true;
		}
		
		//dateStr=date.getMonth()+1;
		dateStr=date.substring(5,7);
		if (dateStr-dateInMonth>=0){
			return false;
		}else if (dateStr-dateInMonth<0){
			return true;
		}
		return true;	
	};

	function linkChangeStatPrd(){
	    var stat_prd_style = FncStatBase.stat_prd_style._getValue();
	    if(stat_prd_style=='4'){
	        var stat_prd = FncStatBase.stat_prd._getValue();
	        FncStatBase.stat_prd_year._setValue(stat_prd.substr(0,4));
		}
	};

	function checkPrdStyle(){
		//检验是否为年报，如果不是年报则将年份设置为必填项。4为年报的代号。
		var prdStyle=FncStatBase.stat_prd_style._getValue();
		if(prdStyle!=4){
			FncStatBase.stat_prd_year._obj._renderRequired(true);
		}else{
			FncStatBase.stat_prd_year._obj._renderRequired(false);
		}
	};
	function returnCus(data){
		FncStatBase.cus_id._setValue(data.cus_id._getValue());
		FncStatBase.cus_id_displayname._setValue(data.cus_name._getValue());
    };
</script>
<style type="text/css">
.emp_field_text_input_0 {
width:300px;
}
</style>
</head>
<body  class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="<emp:url action='addFncStatBaseRecord.do'/>" method="POST">
	</form>
	
<div id="FncStatBaseGroup" class="emp_group_div">
	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="公司客户报表">
	        <emp:pop id="FncStatBase.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
			<emp:text id="FncStatBase.cus_id" label="客户码"  required="true" readonly="true"/>
			<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型"  required="true" dictname="STD_ZB_FNC_STAT" onchange="checkPrdStyle()"/>
			<emp:select id="FncStatBase.stat_prd_year" label="年份"   hidden="false" onchange="doChangey();"/>
			<!-- emp:date id="FncStatBase.stat_prd_date" label="报表期间" required="true" onblur="getYYYYMM(this)"/ -->
			<emp:select id="FncStatBase.stat_prd" label="报表期间"  required="true" onchange="linkChangeStatPrd()"/>
			<!-- emp:text id="FncStatBase.stat_prd" label="报表期间"  maxlength="6" required="true"  onblur="doSelectedStatPrd();"/ -->
			<emp:select id="FncStatBase.stat_bs_style_id" label="资产样式编号" required="false" hidden="true"/>
			<emp:select id="FncStatBase.stat_pl_style_id" label="损益表编号"  required="false" hidden="true"/>
			<emp:select id="FncStatBase.stat_cf_style_id" label="现金流量表编号" required="false" hidden="true"/>
			<emp:select id="FncStatBase.stat_fi_style_id" label="财务指标表编号"  required="false" hidden="true"/>
			<emp:select id="FncStatBase.stat_soe_style_id" label="所有者权益变动表编号"  required="false" hidden="true"/>
			<emp:select id="FncStatBase.stat_sl_style_id" label="财务简表编号"  required="false" hidden="true"/>
			<emp:text id="FncStatBase.style_id1" label="保留" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.style_id2" label="保留1" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.state_flg" label="状态" maxlength="9" required="false" hidden="true" defvalue="000099990"/>
			<emp:select id="FncStatBase.stat_is_nrpt" label="是否新准则报表"  required="false" dictname="STD_ZX_YES_NO" defvalue="1" hidden="true"/>
			<emp:select id="FncStatBase.stat_style" label="报表口径"  required="true" dictname="STD_ZB_FNC_STYLE?enname not in (9)" defvalue="1" readonly="false"/>
			<emp:select id="FncStatBase.stat_is_audit" label="是否经过审计"  required="true" dictname="STD_ZX_YES_NO" onchange="dealAudit()"/>
			<emp:select id="FncStatBase.stat_is_adjt" label="是否经过调整"  required="true" dictname="STD_ZX_YES_NO" onchange="dealAdjt()" defvalue="2"/>
			<emp:text id="FncStatBase.stat_adt_entr" label="审计单位" maxlength="60" required="false" cssElementClass="emp_field_text_input_0" hidden="true" colSpan="2"/>
			<emp:text id="FncStatBase.input_date" label="登记日期" maxlength="10" required="false" hidden="true" />
			<emp:textarea id="FncStatBase.stat_adt_conc" label="审计结论" maxlength="200" required="false" hidden="true"/>	
			<emp:text id="FncStatBase.last_upd_date" label="更新时间" maxlength="0" required="false" hidden="true" />
			<emp:textarea id="FncStatBase.stat_adj_rsn" label="财务报表调整原因" maxlength="200" required="false" hidden="true"/>
			<emp:text id="FncStatBase.last_upd_id" label="更新人" maxlength="16" required="false" hidden="true" />
			<emp:text id="FncStatBase.input_br_id" label="登记机构" maxlength="10" required="false" hidden="true"/>
			<emp:text id="FncStatBase.input_id" label="登记人" maxlength="16" required="false" hidden="true" />
			
			<emp:text id="FncStatBase.isSmp" label="是否简表标志" maxlength="16" defvalue="<%=isSmp %>" hidden="true" />
		</emp:gridLayout>
		</div>
	
	<div align="center">
			<br>
		<emp:button id="addFncStatBase" label="保存" />
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
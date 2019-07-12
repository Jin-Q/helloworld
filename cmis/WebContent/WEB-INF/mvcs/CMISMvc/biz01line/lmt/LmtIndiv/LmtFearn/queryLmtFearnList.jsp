<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<style type="text/css">
 .table_td_show{
	border: 1px solid  #BCD7E2;
	padding: 0px 3px;
	cursor: pointer;
	cursor: hand;
	white-space: nowrap;
	overflow: visible;
}

.table_show{
	width: 100%;
	border-collapse: collapse;
	border-spacing: 0;
	border: 1px solid #7BAFC5;  
}

</style>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtFearn._toForm(form);
		LmtFearnList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtFearnPage() {//家庭收入修改
		var paramStr = LmtFearnList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFearnUpdatePage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
		    window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFearn() {//家庭收入查看
		var paramStr = LmtFearnList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFearnViewPage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFearnPage() {//家庭收入新增
		var lmt_serno='${context.serno}';
		var url = '<emp:url action="getLmtFearnAddPage.do"/>&lmt_serno='+lmt_serno+"&cus_id="+'${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function doDeleteLmtFearn() {
		var paramStr = LmtFearnList._obj.getParamStr(['cus_id','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFearnRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
								}
								var flag = jsonstr.flag;
								if(flag=="success"){
									alert("删除成功!");
										var lmt_serno='${context.serno}';
										var url = '<emp:url action="queryLmtFearnList.do"/>&'+paramStr+"&serno="+lmt_serno;
										url = EMPTools.encodeURI(url);
										window.location = url;
						 	  	}else {
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};		
	
	function doReset(){
		page.dataGroups.LmtFearnGroup.reset();
	};
	
	/*--user code begin--*/
	function doGetUpdateLmtFpayoutPage() {//家庭支出修改
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFpayoutUpdatePage.do"/>?'+paramStr+'';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFpayout() {//家庭支出查看
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFpayoutViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFpayoutPage() {//家庭支出新增
		var lmt_serno='${context.serno}';
		var url = '<emp:url action="getLmtFpayoutAddPage.do"/>&lmt_serno='+lmt_serno+"&cus_id="+'${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		window.location.reload();
	};

	function doDeleteLmtFpayout() {
		var paramStr = LmtFpayoutList._obj.getParamStr(['cus_id','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFpayoutRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
								}
								var flag = jsonstr.flag;
								if(flag=="success"){
									alert("删除成功!");
										var lmt_serno='${context.serno}';
										var url = '<emp:url action="queryLmtFearnList.do"/>&'+paramStr+"&serno="+lmt_serno;
										url = EMPTools.encodeURI(url);
										window.location = url;
						 	  	}else {
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};	

	function sumFearn(){
		var recordCount = LmtFearnList._obj.recordCount;
		var mearn_score =0;
		var ibank_mearn =0;
		var yearn_score =0;
		var ibank_yearn =0;
		var cus_attr =0;
		var number1 =0;
		var number2 =0;
		var number3 =0;
		var number4 =0;
		var number5 =0;
		var number6 =0;
		var number7 =0;
		var number8 =0;
		var number9 =0;
		var number10 =0;
		var number11 =0;
		var number12 =0;
		for(var i=0;i<recordCount;i++){
			cus_attr = LmtFearnList._obj.data[i].cus_attr._getValue()
			mearn_score = eval(LmtFearnList._obj.data[i].mearn_score._getValue());
			ibank_mearn = eval(LmtFearnList._obj.data[i].ibank_mearn._getValue());
			yearn_score = eval(LmtFearnList._obj.data[i].yearn_score._getValue());
			ibank_yearn = eval(LmtFearnList._obj.data[i].ibank_yearn._getValue());
			if(cus_attr=="01" || cus_attr=="02"){
				number1 += mearn_score;
				number2 += ibank_mearn;
				number3 += yearn_score;
				number4 += ibank_yearn;
			}else if(cus_attr=="03" || cus_attr=="04"){
				number5 += mearn_score;
				number6 += ibank_mearn;
				number7 += yearn_score;
				number8 += ibank_yearn;
			}else if(cus_attr=="05" || cus_attr=="06"){
				number9 += mearn_score;
				number10 += ibank_mearn;
				number11 += yearn_score;
				number12 += ibank_yearn;
			}
		}
		
		document.getElementById("ZJKR1").innerHTML= number1.toFixed(2);
		document.getElementById("ZJKR2").innerHTML= number2.toFixed(2);
		document.getElementById("ZJKR3").innerHTML= number3.toFixed(2);
		document.getElementById("ZJKR4").innerHTML= number4.toFixed(2);
		
		document.getElementById("GTZWR1").innerHTML= number5.toFixed(2);
		document.getElementById("GTZWR2").innerHTML= number6.toFixed(2);
		document.getElementById("GTZWR3").innerHTML= number7.toFixed(2);
		document.getElementById("GTZWR4").innerHTML= number8.toFixed(2);
		
		document.getElementById("BZR1").innerHTML= number9.toFixed(2);
		document.getElementById("BZR2").innerHTML= number10.toFixed(2);
		document.getElementById("BZR3").innerHTML= number11.toFixed(2);
		document.getElementById("BZR4").innerHTML= number12.toFixed(2);

		document.getElementById("SUM1").innerHTML= (number1+number5+number9).toFixed(2);
		document.getElementById("SUM2").innerHTML= (number2+number6+number10).toFixed(2);
		document.getElementById("SUM3").innerHTML= (number3+number7+number11).toFixed(2);
		document.getElementById("SUM4").innerHTML= (number4+number8+number12).toFixed(2);
	}

	function sumFpayout(){
		var recordCount = LmtFpayoutList._obj.recordCount;
		var mpayout =0;
		var ypayout =0;
		var cus_attr =0;
		var numberPay1 =0;
		var numberPay2 =0;
		var numberPay3 =0;
		var numberPay4 =0;
		var numberPay5 =0;
		var numberPay6 =0;
		for(var i=0;i<recordCount;i++){
			cus_attr = LmtFpayoutList._obj.data[i].cus_attr._getValue()
			mpayout = eval(LmtFpayoutList._obj.data[i].mpayout._getValue());//月支出
			ypayout = eval(LmtFpayoutList._obj.data[i].ypayout._getValue());//年支出
			if(cus_attr=="01" || cus_attr=="02"){
				numberPay1 += mpayout;
				numberPay2 += ypayout;
			}else if(cus_attr=="03" || cus_attr=="04"){
				numberPay3 += mpayout;
				numberPay4 += ypayout;
			}else if(cus_attr=="05" || cus_attr=="06"){
				numberPay5 += mpayout;
				numberPay6 += ypayout;
			}
		}
		document.getElementById("ZJKRPAY1").innerHTML= numberPay1.toFixed(2);
		document.getElementById("ZJKRPAY2").innerHTML= numberPay2.toFixed(2);
		
		document.getElementById("GTZWRPAY1").innerHTML= numberPay3.toFixed(2);
		document.getElementById("GTZWRPAY2").innerHTML= numberPay4.toFixed(2);
		
		document.getElementById("BZRPAY1").innerHTML= numberPay5.toFixed(2);
		document.getElementById("BZRPAY2").innerHTML= numberPay6.toFixed(2);
		
		document.getElementById("SUMPAY1").innerHTML= (numberPay1+numberPay3+numberPay5).toFixed(2);
		document.getElementById("SUMPAY2").innerHTML= (numberPay2+numberPay4+numberPay6).toFixed(2);
	}

	function doLoad(){
		sumFearn();
		sumFpayout();
		doCountRateLoad();
	}

	function doCountRateLoad(){
		var month = document.getElementById("SUM2").innerHTML;//我行认定月收入（合计）
		var year = document.getElementById("SUM4").innerHTML;//我行认定年收入（合计）
		var monthPay = document.getElementById("SUMPAY1").innerHTML;//我行认定月支出（合计）
		var yearPay = document.getElementById("SUMPAY2").innerHTML;//我行认定年支出（合计）
		var countNumMonth = eval(monthPay)/eval(month);
		var countNumYear = eval(yearPay)/eval(year);
		if(countNumMonth.toFixed(2)== "NaN" || countNumMonth.toFixed(2)== "Infinity"){
			document.getElementById("month_rate").value = "0.00";	
		}else{
			document.getElementById("month_rate").value = countNumMonth.toFixed(2);
		}
		if(countNumMonth.toFixed(2)== "NaN" || countNumMonth.toFixed(2)== "Infinity"){
			document.getElementById("year_rate").value = "0.00";	
		}else{
			document.getElementById("year_rate").value = countNumYear.toFixed(2);
		}
	}

	function doCountRate(){
		var month = document.getElementById("SUM2").innerHTML;//我行认定月收入（合计）
		var year = document.getElementById("SUM4").innerHTML;//我行认定年收入（合计）
		var monthPay = document.getElementById("SUMPAY1").innerHTML;//我行认定月支出（合计）
		var yearPay = document.getElementById("SUMPAY2").innerHTML;//我行认定年支出（合计）
		if((month !="0.00" && monthPay =="0.00") || (monthPay !="0.00" && month =="0.00") || (year !="0.00" && yearPay =="0.00") ||(yearPay !="0.00" && year =="0.00") ||(yearPay =="0.00" && year =="0.00")){
			alert("【收入】和【支出】必须都得填写完整！");
			return false;
		}else{
			var countNumMonth = eval(monthPay)/eval(month);
			var countNumYear = eval(yearPay)/eval(year);
			if(countNumMonth.toFixed(2)== "NaN"){
				document.getElementById("month_rate").value = "0.00";	
			}else{
				document.getElementById("month_rate").value = countNumMonth.toFixed(2);
			}
			if(countNumMonth.toFixed(2)== "NaN"){
				document.getElementById("year_rate").value = "0.00";	
			}else{
				document.getElementById("year_rate").value = countNumYear.toFixed(2);
			}
		}
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div class='emp_gridlayout_title'>家庭收入&nbsp;</div>	
	<div align="left">
		<emp:actButton id="getAddLmtFearnPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtFearnPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtFearn" label="删除" op="remove"/>
		<emp:actButton id="viewLmtFearn" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtFearnList" pageMode="false" url="">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" /> 
		<emp:text id="earning_sour" label="收入来源" dictname="STD_ZB_EARNING_SOUR" /> 
		<emp:text id="mearn_score" label="月收入原值" dataType="Currency"/>
		<emp:text id="ibank_mearn" label="我行认定月收入" dataType="Currency"/>
		<emp:text id="yearn_score" label="年收入原值" dataType="Currency" />
		<emp:text id="ibank_yearn" label="我行认定年收入" dataType="Currency" />
		<emp:text id="serno" label="流水号" defvalue="${context.serno}" hidden="true"/>
	</emp:table>
	<br/>
	<table class="table_show">
		<tr>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">收入</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">主借款人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">共同债务人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">保证人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">合计</td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">月收入原值合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKR1"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWR1"></td>
			<td height="25" align="right" class="table_td_show" id="BZR1"></td>
			<td height="25" align="right" class="table_td_show" id="SUM1"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show" >我行认定月收入合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKR2"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWR2"></td>
			<td height="25" align="right" class="table_td_show" id="BZR2"></td>
			<td height="25" align="right" class="table_td_show" id="SUM2"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">年收入原值合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKR3"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWR3"></td>
			<td height="25" align="right" class="table_td_show" id="BZR3"></td>
			<td height="25" align="right" class="table_td_show" id="SUM3"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">我行认定年收入合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKR4"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWR4"></td>
			<td height="25" align="right" class="table_td_show" id="BZR4"></td>
			<td height="25" align="right" class="table_td_show" id="SUM4"></td>
		</tr>
	</table>
	<br> 
	<div class='emp_gridlayout_title'>家庭支出&nbsp;</div>	
	<div align="left">
		<emp:actButton id="getAddLmtFpayoutPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtFpayoutPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtFpayout" label="删除" op="remove"/>
		<emp:actButton id="viewLmtFpayout" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtFpayoutList" pageMode="false" url="">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" />
		<emp:text id="fpayout_type" label="家庭支出类型" dictname="STD_ZB_FPAYOUT_TYPE" />
		<emp:text id="mpayout" label="月支出" dataType="Currency"/>
		<emp:text id="ypayout" label="年支出" dataType="Currency"/>
		<emp:text id="serno" label="流水号" defvalue="${context.serno}" hidden="true"/>
	</emp:table>
	<br>
	<table  class="table_show">
		<tr>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">支出</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">主借款人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">共同债务人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">保证人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">合计</td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">月支出合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRPAY1"></td>
			<td height="25" align="right" class="table_td_show" id="GTZWRPAY1"></td>
			<td height="25" align="right" class="table_td_show" id="BZRPAY1"></td>
			<td height="25" align="right" class="table_td_show" id="SUMPAY1"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">年支出合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRPAY2"></td>
			<td height="25" align="right" class="table_td_show" id="GTZWRPAY2"></td>
			<td height="25" align="right" class="table_td_show" id="BZRPAY2"></td>
			<td height="25" align="right" class="table_td_show" id="SUMPAY2"></td>
		</tr>
	</table>
	<br>
	<div class='emp_gridlayout_title'>我行认定月付比和年付比&nbsp;</div>	
	<div align="left">
		<!--<emp:actButton id="countRate" label="计算" op="add"/>-->
		<br>
		我行认定月付比:<input type="text" id="month_rate" value=""/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		我行认定年付比:<input type="text" id="year_rate" value=""/>
	</div>
</body>
</html>
</emp:page>
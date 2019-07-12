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
		LmtFdebt._toForm(form);
		LmtFdebtList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtFdebtPage() {
		var paramStr = LmtFdebtList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFdebtUpdatePage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFdebt() {
		var lmt_serno='${context.serno}';
		var paramStr = LmtFdebtList._obj.getParamStr(['cus_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFdebtViewPage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFdebtPage() { 
		var lmt_serno='${context.serno}';
		var url = '<emp:url action="getLmtFdebtAddPage.do"/>&lmt_serno='+lmt_serno+"&cus_id="+'${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		window.location.reload();
	};
	
	function doDeleteLmtFdebt() {
		var paramStr = LmtFdebtList._obj.getParamStr(['cus_id','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFdebtRecord.do"/>?'+paramStr;
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
										var url = '<emp:url action="queryLmtFdebtList.do"/>&'+paramStr+"&serno="+lmt_serno;
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
		page.dataGroups.LmtFdebtGroup.reset();
	};
	
	/*--user code begin--*/
	function doGetUpdateLmtFassetPage() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno = '${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFassetUpdatePage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtFasset() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id','serno']);
		var lmt_serno='${context.serno}';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtFassetViewPage.do"/>?'+paramStr+'&lmt_serno='+lmt_serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtFassetPage() {
		var lmt_serno='${context.serno}';
		var url = '<emp:url action="getLmtFassetAddPage.do"/>&lmt_serno='+lmt_serno+"&cus_id="+'${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=500,width=800,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		window.location.reload();
	};

	function doDeleteLmtFasset() {
		var paramStr = LmtFassetList._obj.getParamStr(['cus_id','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtFassetRecord.do"/>?'+paramStr;
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
										var url = '<emp:url action="queryLmtFdebtList.do"/>&'+paramStr+"&serno="+lmt_serno;
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

	function doLoad(){
		sumFdebt();
		sumFasset();
		doCountRateLoad();
	}

	function sumFdebt(){
		var recordCount = LmtFdebtList._obj.recordCount;
		var debt_amt =0;
		var debt_bal =0;
		var cus_attr =0;
		var number1 =0;
		var number2 =0;
		var number3 =0;
		var number4 =0;
		var number5 =0;
		var number6 =0;
		for(var i=0;i<recordCount;i++){
			cus_attr = LmtFdebtList._obj.data[i].cus_attr._getValue()
			debt_amt = eval(LmtFdebtList._obj.data[i].debt_amt._getValue());//金额
			debt_bal = eval(LmtFdebtList._obj.data[i].debt_bal._getValue());//余额
			if(cus_attr=="01" || cus_attr=="02"){
				number1 += debt_amt;
				number2 += debt_bal;
			}else if(cus_attr=="03" || cus_attr=="04"){ 
				number3 += debt_amt;
				number4 += debt_bal;
			}else if(cus_attr=="05" || cus_attr=="06"){
				number5 += debt_amt;
				number6 += debt_bal;
			}
		}
		document.getElementById("ZJKRAMT").innerHTML= number1.toFixed(2);
		document.getElementById("ZJKRBAL").innerHTML= number2.toFixed(2);
		
		document.getElementById("GTZWRAMT").innerHTML= number3.toFixed(2);
		document.getElementById("GTZWRBAL").innerHTML= number4.toFixed(2);
		
		document.getElementById("BZRAMT").innerHTML= number5.toFixed(2);
		document.getElementById("BZRBAL").innerHTML= number6.toFixed(2);
		
		document.getElementById("SUMAMT").innerHTML= (number1+number3+number5).toFixed(2);
		document.getElementById("SUMBAL").innerHTML= (number2+number4+number6).toFixed(2);
	}

	function sumFasset(){
		var recordCount = LmtFassetList._obj.recordCount;
		var debt_amt =0;
		var debt_bal =0;
		var cus_attr =0;
		var number7 =0;
		var number8 =0;
		var number9 =0;
		var number10 =0;
		var number11 =0;
		var number12 =0;
		for(var i=0;i<recordCount;i++){
			cus_attr = LmtFassetList._obj.data[i].cus_attr._getValue()
			asset_seval = eval(LmtFassetList._obj.data[i].asset_seval._getValue());//原估值
			asset_ivalue = eval(LmtFassetList._obj.data[i].asset_ivalue._getValue());//认定值
			if(cus_attr=="01" || cus_attr=="02"){
				number7 += asset_seval;
				number8 += asset_ivalue;
			}else if(cus_attr=="03" || cus_attr=="04"){ 
				number9 += asset_seval;
				number10 += asset_ivalue;
			}else if(cus_attr=="05" || cus_attr=="06"){
				number11 += asset_seval;
				number12 += asset_ivalue;
			}
		}
		document.getElementById("ZJKRSEVAL").innerHTML= number7.toFixed(2);
		document.getElementById("ZJKRIVALUE").innerHTML= number8.toFixed(2);
		
		document.getElementById("GTZWRSEVAL").innerHTML= number9.toFixed(2);
		document.getElementById("GTZWRIVALUE").innerHTML= number10.toFixed(2);
		
		document.getElementById("BZRSEVAL").innerHTML= number11.toFixed(2);
		document.getElementById("BZRIVALUE").innerHTML= number12.toFixed(2);
		
		document.getElementById("SUMSEVAL").innerHTML= (number7+number9+number11).toFixed(2);
		document.getElementById("SUMIVALUE").innerHTML= (number8+number10+number12).toFixed(2);
	}

	function doCountRateLoad(){
		var IVALUE = document.getElementById("SUMIVALUE").innerHTML;//资产认定值（合计）
		var BAL = document.getElementById("SUMBAL").innerHTML;//负债余额（合计）
		var rate = eval(IVALUE)/eval(BAL);
		if(rate.toFixed(2)=="NaN" || rate.toFixed(2)=="Infinity"){
			document.getElementById("asset_payout").value = "0.00";
		}else{
			document.getElementById("asset_payout").value = rate.toFixed(2);
		}
	}

	function doCountRate(){
		var IVALUE = document.getElementById("SUMIVALUE").innerHTML;//资产认定值（合计）
		var BAL = document.getElementById("SUMBAL").innerHTML;//负债余额（合计）
		if((IVALUE=="0.00"&&BAL!="0.00") || (BAL=="0.00"&&IVALUE!="0.00") || (BAL=="0.00"&&IVALUE=="0.00")){
			alert("【资产认定值】和【负债余额】都得填写完整！");
		}else{
			var rate = eval(IVALUE)/eval(BAL);
			if(rate.toFixed(2)=="NaN"){
				document.getElementById("asset_payout").value = "0.00";
			}else{
				document.getElementById("asset_payout").value = rate.toFixed(2);
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm"></form>
	<div class='emp_gridlayout_title'>家庭主要负债&nbsp;</div>	
	<div align="left">
		<emp:actButton id="getAddLmtFdebtPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtFdebtPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtFdebt" label="删除" op="remove"/>
		<emp:actButton id="viewLmtFdebt" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtFdebtList" pageMode="false" url="pageLmtFdebtQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" />
		<emp:text id="debt_type" label="负债类型" dictname="STD_ZB_DEBT_TYPE" />
		<emp:text id="debt_amt" label="负债金额" dataType="Currency"/>
		<emp:text id="debt_bal" label="负债余额" dataType="Currency"/>
		<emp:text id="serno" label="流水号" defvalue="${context.serno}" hidden="true"/>
	</emp:table>
	<br/>
	<table class="table_show">
		<tr>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">负债</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">主借款人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">共同债务人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">保证人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">合计</td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">负债金额合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRAMT" ></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWRAMT"></td>
			<td height="25" align="right" class="table_td_show" id="BZRAMT"></td>
			<td height="25" align="right" class="table_td_show" id="SUMAMT"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show" >负债余额合计(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRBAL"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWRBAL"></td>
			<td height="25" align="right" class="table_td_show" id="BZRBAL"></td>
			<td height="25" align="right" class="table_td_show" id="SUMBAL"></td>
		</tr>
	</table>
	<br>
	<div class='emp_gridlayout_title'>家庭主要资产&nbsp;</div>	
	<div align="left">
		<emp:actButton id="getAddLmtFassetPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtFassetPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtFasset" label="删除" op="remove"/>
		<emp:actButton id="viewLmtFasset" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtFassetList" pageMode="false" url="pageLmtFassetQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_attr" label="客户属性" dictname="STD_ZB_CUS_ATTR" />
		<emp:text id="fasset_type" label="家庭资产类型" dictname="STD_ZB_FASSET_TYPE"/>
		<emp:text id="asset_seval" label="资产原估值" dataType="Currency"/>
		<emp:text id="asset_ivalue" label="资产认定值" dataType="Currency"/>
		<emp:text id="serno" label="流水号" defvalue="${context.serno}" hidden="true"/>
	</emp:table>
	<br/>
	<table class="table_show">
		<tr>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">资产</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">主借款人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">共同债务人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">保证人家庭</td>
			<td height="25" align="center" style="border: none; background-color: #99CCFF">合计</td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show">资产原估值(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRSEVAL"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWRSEVAL"></td>
			<td height="25" align="right" class="table_td_show" id="BZRSEVAL"></td>
			<td height="25" align="right" class="table_td_show" id="SUMSEVAL"></td>
		</tr>
		<tr>
			<td height="25" align="center" class="table_td_show" >资产认定值(元)</td>
			<td height="25" align="right" class="table_td_show" id="ZJKRIVALUE"></td>   
			<td height="25" align="right" class="table_td_show" id="GTZWRIVALUE"></td>
			<td height="25" align="right" class="table_td_show" id="BZRIVALUE"></td>
			<td height="25" align="right" class="table_td_show" id="SUMIVALUE"></td>
		</tr>
	</table>
	<br>
	<div class='emp_gridlayout_title'>资产负债比&nbsp;</div>
	<div align="left">
		<!--<emp:actButton id="countRate" label="计算" op="add"/>-->
		<br>
		资产负债比:<input type="text" id="asset_payout" value=""/>
	</div>
</body>
</html>
</emp:page>
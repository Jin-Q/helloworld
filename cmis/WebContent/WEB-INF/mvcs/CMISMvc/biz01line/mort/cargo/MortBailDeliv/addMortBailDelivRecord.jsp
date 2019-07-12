<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
	String oversee_agr_no = "";
	if(context.containsKey("oversee_agr_no")){
		oversee_agr_no = (String)context.getDataValue("oversee_agr_no");
	}
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<style type="text/css">

.emp_text2{
border:1px solid #b7b7b7;
width:100px;
background-color:#eee;
}
.emp_text{
border:1px solid #b7b7b7;
width:150px;
background-color:#eee;
}
.emp_text3{
border:1px solid #b7b7b7;
width:100px;
}  
</style>
<script type="text/javascript">
	var totalPr=0;//提货总价值
	function doLoad(){
		MortCargoPledgeNewList._obj.selectAll();
		doAdd();
		doCacul();

		var agr_type = MortBailDeliv.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(3)").text(labelName);
		 });
	};
	
	//质押物清单下查看按钮方法
	function doViewMortCargoPledge() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//质押物清单中保存按钮方法
	function doSave(){
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		//alert(data.length);
		if(data.length>0){
			for(var i=0;i<data.length;i++){
				var deliv_qnt = data[i].deliv_qnt._getValue();//出库数量
				cargo_id = data[i].cargo_id._getValue();
				if(parseFloat(deliv_qnt)==0){
					alert("货物编号为："+cargo_id+"的置换数量为“0”，不能做提货操作！");
					return;
				}
			}
		}else{
			alert("请选取待提货的货物！");
			return;
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("已保存成功！");
					window.location.reload();
				}else{
					alert("提货信息保存失败！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var guaranty_no = MortBailDeliv.guaranty_no._getValue();
		var form = document.getElementById("submitForm");
		MortCargoPledgeNewList._toForm(form);
		page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = '<emp:url action="saveCargoPledge.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);		
	}
	//提货记账按钮方法
	function doTally(){
		if(!MortBailDeliv._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortBailDeliv.storage_total._getValue();
			var surplus_total = MortBailDeliv.surplus_total._getValue();
			if(storage_total==surplus_total){
				alert("没有需要提货的货物，不能做“提货记账”操作！");
				return;
			}
			var repay_amt = MortBailDeliv.repay_amt._getValue();//还款金额
			var deliv_total = MortBailDeliv.deliv_total._getValue();//提货金额
			var pldimn_rate = MortBailDeliv.pldimn_rate._getValue();//抵质押率
			var a = parseFloat(repay_amt);
			var b = parseFloat(deliv_total);
			if(a>=b*parseFloat(pldimn_rate)){
			}else{
				alert("还款金额÷质押率≥提货总价值！");
				return;
			}
			var amt = MortBailDeliv.deliv_total._getValue();//提货金额
			if(parseFloat(amt)>0){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("记账失败！");
							return;
						}
						var flag = jsonstr.flag;
						if(flag=='success'){	
							alert("已成功记账！");
							var url = '<emp:url action="queryMortBailDelivList.do"/>?menuId=bzjth';
							url = EMPTools.encodeURI(url);
							location.href(url);
						}else{
							alert("记账失败！");
						}   
					}	
				};
				var handleFailure = function(o) {
					alert("记账失败!");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var form = document.getElementById('submitForm');
				MortBailDeliv._toForm(form);
				MortCargoPledgeNewList._toForm(form);
				var url = '<emp:url action="tallyMortBailDelivRecord.do"/>';
				url = EMPTools.encodeURI(url);
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
			}else{
				alert("提货总价值为“0”，请检查是否货物清单信息输入后未进行保存操作，保存之后再进行“提货记账”操作！");
			}
	    }
	}
	//保存按钮方法
	function doNext(){
		if(!MortBailDeliv._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortBailDeliv.storage_total._getValue();
			var surplus_total = MortBailDeliv.surplus_total._getValue();
			if(storage_total==surplus_total){
				alert("没有需要提货的货物，不能做“保存”操作！");
				return;
			}
			var repay_amt = MortBailDeliv.repay_amt._getValue();//还款金额
			var deliv_total = MortBailDeliv.deliv_total._getValue();//提货金额
			var pldimn_rate = MortBailDeliv.pldimn_rate._getValue();//抵质押率
			var a = parseFloat(repay_amt);
			var b = parseFloat(deliv_total);
			if(a>=b*parseFloat(pldimn_rate)){
			}else{
				alert("还款金额÷质押率≥提货总价值！");
				return;
			}
			var amt = MortBailDeliv.deliv_total._getValue();//提货金额
			if(parseFloat(amt)>0){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("保存失败！");
							return;
						}
						var flag = jsonstr.flag;
						if(flag=='success'){	
							alert("保存成功");
							var url = '<emp:url action="queryMortBailDelivList.do"/>?menuId=bzjth';
							url = EMPTools.encodeURI(url);
							location.href(url);
						}else{
							alert("保存失败");
						}   
					}	
				};
				var handleFailure = function(o) {
					alert("保存失败!");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var form = document.getElementById('submitForm');
				MortBailDeliv._toForm(form);
				var url = '<emp:url action="addMortBailDelivRecord.do"/>';
				url = EMPTools.encodeURI(url);
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		    }else{
				alert("提货总价值为“0”，请检查是否货物清单信息输入后未进行保存操作，保存之后再进行“保存”操作！");
			}
	    }
	}
	//返回按钮方法
	function doReturn(){
		<%if(!"".equals(oversee_agr_no)){%>
		var url = '<emp:url action="queryMortInfo4AccOverseeRemindList.do"/>?oversee_agr_no=${context.oversee_agr_no}';
		<%}else if("hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
		<%}else{%>
		var url = '<emp:url action="queryMortBailDelivList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	
	}
	//校验还款金额需不小于提货总价值
	function doChange(){
		var repay_amt = MortBailDeliv.repay_amt._getValue();//还款金额
		var deliv_total = MortBailDeliv.deliv_total._getValue();//提货金额
		var pldimn_rate = MortBailDeliv.pldimn_rate._getValue();//抵质押率
		var a = parseFloat(repay_amt);
		var b = parseFloat(deliv_total);
		if(a>=b*parseFloat(pldimn_rate)){
		}else{
			alert("还款金额÷质押率≥提货总价值！");
			MortBailDeliv.repay_amt._setValue();
		}
	}
	//根据提货数量计算提货价值、剩余数量、剩余价值
	function doCacul(){
		MortCargoPledgeNewList._obj.selectAll();
	  	var data = MortCargoPledgeNewList._obj.getSelectedData();
		if(data.length>0){
	     	for(var i=0;i<data.length;i++){       
				var qnt=data[i].qnt._getValue();//在库数量
				var deliv_qnt=data[i].deliv_qnt._getValue();//提货数量  
				var identy_unit_price = data[i].identy_unit_price._getValue();//银行认定单价
				var identy_total  = data[i].identy_total._getValue();//银行认定总价
				if(parseFloat(deliv_qnt)>parseFloat(qnt)){
					alert("提货数量需不大于在库数量");
					data[i].deliv_qnt._setValue("0");
					return false;
					//window.location.reload();
				}else{
					data[i].deliv_value._setValue((parseFloat(deliv_qnt)*parseFloat(identy_unit_price)).toString());//提货总价
					data[i].surplus_qnt._setValue((parseFloat(qnt)-parseFloat(deliv_qnt)).toString());//剩余数量
				 	data[i].surplus_value._setValue((parseFloat(identy_total)-(parseFloat(deliv_qnt)*parseFloat(identy_unit_price))).toString());//剩余总价值
				}
	   	  	 }
		}else{
			alert("至少选择一条质押物记录！");
			var num = MortCargoPledgeNewList._getSize();
			for(var i=0;i<num;i++){
			   MortCargoPledgeNewList[i].deliv_qnt._setValue("0");//重置提货数量为零
			}
		}
	};
	//计算提货总价值
	function doAdd(){
		var num = MortCargoPledgeNewList._getSize();
		var total=0;
		for(var i=0;i<num;i++){
			var amt = MortCargoPledgeNewList[i].deliv_value._getValue();
			total = parseFloat(total)+parseFloat(amt);
		}
		MortBailDeliv.deliv_total._setValue(total.toString());//提货总价值
		var storage_total = MortBailDeliv.storage_total._getValue();//库存总价值
		MortBailDeliv.surplus_total._setValue((parseFloat(storage_total)-parseFloat(total)).toString());//剩余总价值
		
	}
</script>
</head>

<body class="page_content" onload="doLoad()">
 <emp:tabGroup id="MortCargoReplTab" mainTab="appinf">
   <emp:tab label="基本信息" id="appinf"  needFlush="true" initial="true" >
	<emp:form id="submitForm" action="" method="POST">
		<emp:gridLayout id="MortBailDelivGroup" title="保证金提货" maxColumn="2">
			<emp:text id="MortBailDeliv.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortBailDeliv.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortBailDeliv.cus_id" label="出质人客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.cus_id_displayname" label="出质人客户名称" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="MortBailDeliv.repay_receipt_type" label="还款凭证类型" required="true" dictname="STD_REPAY_LIST_TYPE" />
			<emp:text id="MortBailDeliv.receipt_serno" label="凭证流水号" maxlength="40" required="true" />
			<emp:text id="MortBailDeliv.repay_amt" label="还款金额" maxlength="20" required="true" dataType="Double" onchange="doChange()"/>
			<emp:text id="MortBailDeliv.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortBailDeliv.deliv_total" label="提货总价值" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortBailDeliv.surplus_total" label="剩余总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortBailDeliv.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortBailDeliv.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortBailDeliv.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="MortBailDeliv.pldimn_rate" label="抵质押率" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortBailDelivGroup" title="登记信息" maxColumn="2">		
			<emp:text id="MortBailDeliv.input_id" label="登记人" maxlength="20" required="false" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortBailDeliv.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" defvalue="$organNo" hidden="true"/>
			<emp:text id="MortBailDeliv.input_id_displayname" label="登记人" required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortBailDeliv.input_br_id_displayname" label="登记机构" required="false" readonly="true" defvalue="$organName"/>
			<emp:date id="MortBailDeliv.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
			<emp:text id="MortBailDeliv.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>

		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存"/>
			<emp:button id="tally" label="提货记账"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>货物清单</div>
		<div align="left">
		    <emp:button id="save" label="确认" op="view"/>
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		</div>
	<emp:table icollName="MortCargoPledgeNewList" pageMode="false" url="" selectType="2">
		<emp:text id="cargo_id" label="货物编号" readonly="true"/>
		<emp:text id="guaranty_catalog" label="押品所处目录" readonly="true"/>
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录名称" readonly="true"/>
		<emp:text id="qnt" label="在库数量" readonly="true"/>
		<emp:text id="identy_unit_price" label="单价" readonly="true" dataType="Currency"/>
		<emp:text id="identy_total" label="在库总价" readonly="true" dataType="Currency"/>
		<emp:text id="deliv_qnt" label="提货数量" dataType="Double" cssElementClass="emp_text3" onchange="doCacul()" defvalue="0" required="true" flat="true"/>
		<emp:text id="deliv_value" label="提货价值" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
		<emp:text id="serno" label="流水" hidden="true"/>
	</emp:table>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
</body>
</html>
</emp:page>


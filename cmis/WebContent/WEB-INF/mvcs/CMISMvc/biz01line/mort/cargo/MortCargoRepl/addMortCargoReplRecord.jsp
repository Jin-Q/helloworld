<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
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
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
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
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	var totalPr=0;//置换总价值
	var arr = new Array();//存储需要出库的货物编号集
	var arrNew = new Array();//存储需要入库的货物编号集
	function doReturn(){
		<%if(!"".equals(oversee_agr_no)){%>
		var url = '<emp:url action="queryMortInfo4AccOverseeRemindList.do"/>?oversee_agr_no=${context.oversee_agr_no}';
		<%}else if("hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
		<%}else{%>
		var url = '<emp:url action="queryMortCargoReplList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		//alert(url);
		window.location=url;
	}
	function doLoad(){
		//MortCargoPledgeNewList._obj.selectAll();
		//doAdd();
		doCacul();
		var agr_type = MortCargoRepl.agr_type._getValue();
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
	function returnGuaranty(data){
		MortCargoRepl.guaranty_no._setValue(data.guaranty_no._getValue());
		MortCargoRepl.cus_id._setValue(data.cus_id._getValue());
		MortCargoRepl.oversee_agr_no._setValue(data.agr_no._getValue());
		MortCargoRepl.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
	}	
	//货物记录新增事件
	function doGetAddMortCargoPledgePage() {
	//	var guaranty_no = '${context.guaranty_no}';
		var guaranty_no = MortCargoRepl.guaranty_no._getValue();
		var serno = MortCargoRepl.serno._getValue();
	//	var url = '<emp:url action="getMortCargoPledgeAddPage.do"/>?flag=tab&action=zh&guaranty_no='+guaranty_no;//flag标志非押品维护时的货物操作（tab）
		var url = '<emp:url action="getMortCargoReplListAddPage.do"/>?flag=tab&action=zh&guaranty_no='+guaranty_no+'&serno='+serno;
		url = EMPTools.encodeURI(url);
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	//货物记录删除事件
	function doDeleteMortCargoPledge() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length==1) {
			var cargo_status = data[0].cargo_status._getValue();
			if("01"!=cargo_status){
				alert("非登记状态的货物记录不能进行“删除”操作！");
				return;
			}
			if(confirm("是否确认要删除？")){
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
							alert("货物信息删除成功！");
							window.location.reload();
						}else{
							alert("货物信息删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var cargo_id = data[0].cargo_id._getValue();
				var serno = MortCargoRepl.serno._getValue();
			//	var url = '<emp:url action="deleteMortCargoPledgeRecord.do"/>?action=zh&cargo_id='+cargo_id;//action=zh,表示操作货物置换清单表
				var url = '<emp:url action="deleteMortCargoReplListRecord.do"/>?serno='+serno+'&cargo_id='+cargo_id;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//货物记录修改事件
	function doGetUpdateMortCargoPledgePage() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length==1) {
			var cargo_status = data[0].cargo_status._getValue();
			if("01"!=cargo_status){
				alert("非登记状态的货物记录不能进行“修改”操作！");
				return;
			}
			var cargo_id = data[0].cargo_id._getValue();
			var serno = MortCargoRepl.serno._getValue();
		//	var url = '<emp:url action="getMortCargoPledgeUpdatePage.do"/>?flag=tab&action=zh&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			var url = '<emp:url action="getMortCargoReplListUpdatePage.do"/>?cargo_id='+cargo_id+'&serno='+serno;
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//货物记录查看事件
	function doViewMortCargoPledge() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var serno = MortCargoRepl.serno._getValue();
		//	var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&action=zh&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			var url = '<emp:url action="getMortCargoReplListViewPage.do"/>?cargo_id='+cargo_id+'&serno='+serno;
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//确定按钮事件（选中一条或多条货物记录，并累加其置换价值）
	function doSure(){
		var totalAmt = MortCargoRepl.not_out_total._getValue();//本次操作之前的出库待记账货物
		var totalNewAmt = MortCargoRepl.not_to_total._getValue();//本次操作之前的入库待记账货物
		var data = MortCargoPledgeList._obj.getSelectedData();
		var dataNew = MortCargoPledgeNewList._obj.getSelectedData();
		var status="";
		var total=0;//选定的入库货物的置换价值的累加和
		var totalNew=0;//选定的新增待登记货物的银行认定总价的累加和
		if(data.length>0||dataNew.length>0){
			var cargo_id;//已选定的货物编号
			var unit;//单个货物的银行认定总价
			var count=0;//置换货物总数量
			var deliv_qnt;
			arr=[];//每次重新选择前，押品编号进行清空
			arrNew=[];
			for(var i=0;i<data.length;i++){
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				unit = data[i].deliv_value._getValue();//置换价值
				deliv_qnt = data[i].deliv_qnt._getValue();//置换数量
				if(status!="02")
				{
					alert("货物编号为："+cargo_id+"的货物处于非入库状态不能添加出库！");
					return;
				}else{
					arr.push(cargo_id);
					total = parseFloat(total)+parseFloat(unit);
					deliv_qnt = parseFloat(count)+parseFloat(deliv_qnt);
				}
				if(parseFloat(deliv_qnt)==0){
					alert("货物编号为："+cargo_id+"的置换数量为“0”，不能做货物置换操作！");
					return;
				}
			}
			
			for(var i=0;i<dataNew.length;i++){
				cargo_id = dataNew[i].cargo_id._getValue();
				status = dataNew[i].cargo_status._getValue();
				unit = dataNew[i].identy_total._getValue();
				if(status!="01")
				{
					alert("货物编号为："+cargo_id+"的货物处于非登记状态不能添加置换！");
					return;
				}else{
					arrNew.push(cargo_id);
					totalNew = parseFloat(totalNew)+parseFloat(unit);
				}
			}
			if((parseFloat(totalNew)+parseFloat(totalNewAmt))>=(parseFloat(total)+parseFloat(totalAmt))){
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
							window.location.reload();
						}else{
							alert("押品状态修改失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
		 		var guaranty_no = MortCargoRepl.guaranty_no._getValue();
		 		var serno = MortCargoRepl.serno._getValue();
				var form = document.getElementById("submitForm");
				MortCargoPledgeList._toForm(form);
				page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
				var postData = YAHOO.util.Connect.setForm(form);
				var url = '<emp:url action="changeGaurantyStatus.do"/>?flgzh=5&cargo_id_str_new='+arrNew+'&cargo_id_str='+arr+'&guaranty_no='+guaranty_no+'&serno='+serno;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);	
			}else{
				alert("置换货物的总金额小于被置换货物的总金额，请重新选择！");
				return;
			}
		}else{
			alert("至少选中一条在库货物或者置换货物记录！");
		}
	}
			
	function doCancle(){
		var totalAmt = MortCargoRepl.not_out_total._getValue();//本次操作之前的出库待记账货物
		var totalNewAmt = MortCargoRepl.not_to_total._getValue();//本次操作之前的入库待记账货物
		var data = MortCargoPledgeList._obj.getSelectedData();
		var dataNew = MortCargoPledgeNewList._obj.getSelectedData();
		var status="";
		var total=0;//选定的入库货物的银行认定总价的累加和
		var totalNew=0;//选定的新增待登记货物的银行认定总价的累加和
		if(data.length>0||dataNew.length>0){
			var cargo_id;//已选定的
			var unit;//单个货物的银行认定价值
			arr=[];//重新操作时，对押品编号集进行清空（在库货物）
			arrNew=[];//重新操作时，对押品编号集进行清空（置换货物）
			for(var i=0;i<data.length;i++){
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				unit = data[i].deliv_value._getValue();
				if(status!="05")
				{
					alert("货物编号为："+cargo_id+"的货物处于非出库待记账状态不能进行撤销！");
					return;
				}else{
					arr.push(cargo_id);
					total = parseFloat(total)-parseFloat(unit);
				}
			}
			for(var i=0;i<dataNew.length;i++){
				cargo_id = dataNew[i].cargo_id._getValue();
				status = dataNew[i].cargo_status._getValue();
				unit = dataNew[i].identy_total._getValue();
				
				if(status!="04")
				{
					alert("货物编号为："+cargo_id+"的货物处于非入库待记账状态不能进行撤销！");
					return;
				}else{
					arrNew.push(cargo_id);
					totalNew = parseFloat(totalNew)-parseFloat(unit);
				}
			}
			if((parseFloat(totalNew)+parseFloat(totalNewAmt))>=(parseFloat(total)+parseFloat(totalAmt))){
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
							window.location.reload();
						}else{
							alert("押品状态修改失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var guaranty_no = MortCargoRepl.guaranty_no._getValue();
				var serno = MortCargoRepl.serno._getValue();
				var form = document.getElementById("submitForm");
				MortCargoPledgeList._toForm(form);
				page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
				var postData = YAHOO.util.Connect.setForm(form);
				var url = '<emp:url action="changeGaurantyStatus.do"/>?flgzh=6&cargo_id_str_new='+arrNew+'&cargo_id_str='+arr+'&guaranty_no='+guaranty_no+'&serno='+serno;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);	
			}else{
				alert("置换货物的总金额小于被置换货物的总金额，请重新选择！");
			}
			
		}else{
			alert("至少选中一条货物记录！");
		}
	}
	function doNext(){
		if(!MortCargoRepl._checkAll()){
			alert("请输入必填项！");
		}else{
		/*	var storage_total = MortCargoRepl.storage_total._getValue();
			var after_repl_total = MortCargoRepl.after_repl_total._getValue();
			if(storage_total==after_repl_total){
				alert("没有需要置换的货物，不能进行“保存”操作");
				return;
			}*/
			var totalIn;//入库待记账货物总价值
			var totalOut;//出库待记账货物总价值
			totalIn = MortCargoRepl.not_to_total._getValue();//入库待记账货物总价值
			totalOut = MortCargoRepl.not_out_total._getValue();//出库待记账货物总价值
			if(parseFloat(totalOut)==0){
				alert("出库待记账状态的货物价值为0,不能进行‘保存’操作");
				return;
			}
			if((parseFloat(totalIn)>=parseFloat(totalOut))){//待入库总价值大于待出库总价值
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
							var url = '<emp:url action="queryMortCargoReplList.do"/>?menuId=hwzh';
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
				MortCargoRepl._toForm(form);
				var serno = MortCargoRepl.serno._getValue();
				var url = '<emp:url action="addMortCargoReplRecord.do"/>?serno='+serno;
				url = EMPTools.encodeURI(url);
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
			}else{
				alert("置换货物的总金额小于被置换货物的总金额，请重新选择！");
			}
		}
	}

	function doTally(){
		if(!MortCargoRepl._checkAll()){
			alert("请输入必填项！");
		}else{
		/*	var storage_total = MortCargoRepl.storage_total._getValue();
			var after_repl_total = MortCargoRepl.after_repl_total._getValue();
			if(storage_total==after_repl_total){
				alert("没有需要置换的货物，不能进行“置换记账”操作");
				return;
			}*/
			var totalIn;//入库待记账货物总价值
			var totalOut;//出库待记账货物总价值
			totalIn = MortCargoRepl.not_to_total._getValue();//入库待记账货物总价值
			totalOut = MortCargoRepl.not_out_total._getValue();//出库待记账货物总价值
			if(parseFloat(totalOut)==0){
				alert("出库待记账状态的货物价值为0,不能进行‘置换记账’操作");
				return;
			}
			if((parseFloat(totalIn)>=parseFloat(totalOut))){//待入库总价值大于待出库总价值
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
						var url = '<emp:url action="queryMortCargoReplList.do"/>?menuId=hwzh';
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("记账失败");
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
			var serno = MortCargoRepl.serno._getValue();
			MortCargoRepl._toForm(form);
			var guaranty_no = MortCargoRepl.guaranty_no._getValue();
			var url = '<emp:url action="addMortCargoReplRecord.do"/>?flg=2&guaranty_no='+guaranty_no+'&serno='+serno;;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
			}else{
				alert("置换货物的总金额小于被置换货物的总金额，请重新选择！");
			}
		}
	}
	//根据置换数量计算置换价值、剩余数量、剩余价值
	function doCacul(){
		MortCargoPledgeList._obj.selectAll();
	  	var data = MortCargoPledgeList._obj.getSelectedData();
     	for(var i=0;i<data.length;i++){       
			var qnt=data[i].qnt._getValue();//在库数量
			var deliv_qnt=data[i].deliv_qnt._getValue();//置换数量  
			var identy_unit_price = data[i].identy_unit_price._getValue();//银行认定单价
			var identy_total  = data[i].identy_total._getValue();//银行认定总价
			if(parseFloat(deliv_qnt)>parseFloat(qnt)){
				alert("置换数量需不大于在库数量");
			//	data[i].deliv_qnt._obj.element.value='0.00';
			//	data[i].deliv_value._setValue('0.00');
				window.location.reload();
			}else{
				data[i].deliv_value._setValue((parseFloat(deliv_qnt)*parseFloat(identy_unit_price)).toString());//提货总价
				data[i].surplus_qnt._setValue((parseFloat(qnt)-parseFloat(deliv_qnt)).toString());//剩余数量
			 	data[i].surplus_value._setValue((parseFloat(identy_total)-(parseFloat(deliv_qnt)*parseFloat(identy_unit_price))).toString());//剩余总价值
			}
   	  	 }
     	//doAdd();
	};
	//计算提货总价值
	function doAdd(){
		var num = MortCargoPledgeList._getSize();
		var total=0;
		for(var i=0;i<num;i++){
			var amt = MortCargoPledgeList[i].deliv_value._getValue();
			total = parseFloat(total)+parseFloat(amt);
		}
	//	MortCargoRepl.after_repl_total._setValue(total.toString());//提货总价值
	//	var storage_total = MortCargoRepl.storage_total._getValue();//库存总价值
	//	MortCargoRepl.after_repl_total._setValue((parseFloat(storage_total)-parseFloat(total)).toString());//剩余总价值

		//此次置换总价值
		MortCargoRepl.this_repl_total._setValue(total);
		
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
 <emp:tabGroup id="MortCargoReplTab" mainTab="appinf">
   <emp:tab label="基本信息" id="appinf"  needFlush="true" initial="true" >
	<emp:form id="submitForm" action="addMortCargoReplRecord.do" method="POST">
		
		<emp:gridLayout id="MortCargoReplGroup" title="货物置换" maxColumn="2">
			<emp:text id="MortCargoRepl.serno" label="业务编号" maxlength="60" required="true" readonly="true"/>
			<emp:text id="MortCargoRepl.guaranty_no" label="押品编号" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoRepl.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id_displayname" label="出质人客户名称" required="false" cssElementClass="emp_field_text_readonly" readonly="true" colSpan="2"/>
			<emp:text id="MortCargoRepl.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.this_repl_total" label="此次置换总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" defvalue="0"/>
			<emp:text id="MortCargoRepl.not_out_total" label="出库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.not_to_total" label="入库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoRepl.after_repl_total" label="置换后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortCargoRepl.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoRepl.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" defvalue="00" readonly="true"/>
			<emp:textarea id="MortCargoRepl.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoReplGroup" title="登记信息" maxColumn="2">	
			<emp:text id="MortCargoRepl.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoRepl.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="MortCargoRepl.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortCargoRepl.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:date id="MortCargoRepl.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoRepl.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存" op="view"/>
			<emp:button id="tally" label="置换记账" op="view"/>
			<emp:button id="return" label="返回"/>
			<br>
		</div>
		</emp:form>
		<div class='emp_gridlayout_title'>在库货物信息</div>
		<div align="left">
			<emp:button id="sure" label="确定置换" />
			<emp:button id="cancle" label="撤销" />
		</div>
		<emp:table icollName="MortCargoPledgeList" pageMode="false" url="pageMortCargoPledgeQuery.do?status=ck&guarantyNo=${context.MortCargoRepl.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" readonly="true"/>
			<emp:text id="guaranty_catalog" label="押品所处目录" readonly="true" hidden="true"/>
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" readonly="true"  />
			<emp:text id="qnt" label="在库数量" readonly="true" />
			<emp:text id="identy_unit_price" label="单价" readonly="true" dataType="Currency" />
			<emp:text id="identy_total" label="在库总价" readonly="true" dataType="Currency"/>
			<emp:text id="deliv_qnt" label="置换数量" dataType="Double" onblur="doCacul()" defvalue="0" required="false" flat="true" cssElementClass="emp_text3" />
			<emp:text id="deliv_value" label="置换价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" hidden="false"/>
			<emp:text id="serno" label="流水" hidden="true"/>
		</emp:table>
		<div class='emp_gridlayout_title'>置换货物信息</div>
		<div align="left">
			<emp:button id="getAddMortCargoPledgePage" label="新增" op="view"/>
			<emp:button id="getUpdateMortCargoPledgePage" label="修改" op="view"/>
			<emp:button id="deleteMortCargoPledge" label="删除" op="view"/>
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		</div>
		
		<emp:table icollName="MortCargoPledgeNewList" pageMode="false" url="pageMortCargoPledgeNewQuery.do?status=zh&guarantyNo=${context.MortCargoRepl.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="qnt" label="数量" dataType="Double"/>
			<emp:text id="identy_unit_price" label="银行认定单价" dataType="Currency"/>
			<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
			<emp:text id="reg_date" label="登记日期" />
		</emp:table>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
</body>
</html>
</emp:page>


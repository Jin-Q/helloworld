<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String bizType = "";
	
	KeyedCollection kc = (KeyedCollection)context.getDataElement("IqpBillIncome");
	if(kc.containsKey("biz_type")){
		bizType = (String)kc.getDataValue("biz_type");
	}
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			//request.setAttribute("canwrite","");
		}
	}
	
	
%>

<%@page import="com.ecc.emp.data.KeyedCollection"%><emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
background-color:#eee;
width:100px;
}
.emp_input1{
border:1px solid #b7b7b7;
background-color:#eee;
width:60px;
}
.emp_input_60{
background-color:#eee;
width:60px;
}

.emp_field_select_longinput { 
	display: inline;
	border-width: 1px;
	width: 450px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_readonly .emp_field_select_longinput {
	display: none;
}
.emp_field_readonly .emp_field_select_longinput {
	display: inline;
	width: 80px;
	border-color: #b7b7b7;
}
</style>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	/*--user code begin--*/
	function doOnLoad(){
		var is_ebill = '${context.is_ebill}';
		if(is_ebill == '1'){
			if('<%=bizType%>'=='07'){
				IqpBillIncome.dscnt_int_pay_mode._obj._renderReadonly(true);
			}
			IqpBillIncome.disc_rate._obj._renderReadonly(true);
			IqpBillIncome.adj_days._obj._renderReadonly(true);
		}
		var bizType = '${context.IqpBillIncome.biz_type}';
		if(bizType == '04'||bizType == '02'){//卖出回购 买入
			//隐藏 转/贴现利率、贴现天数、利息
			IqpBillIncome.fore_rebuy_date._obj._renderRequired(true);
			//IqpBillIncome.rebuy_rate._obj._renderRequired(true);
			//IqpBillIncome.due_rebuy_rate._obj._renderRequired(true);
			IqpBillIncome.fore_rebuy_date._obj._renderReadonly(true);
			IqpBillIncome.rebuy_days._obj._renderRequired(true);
			IqpBillIncome.rebuy_int._obj._renderRequired(true);
			IqpBillIncome.rebuy_days._obj._renderReadonly(true);
			IqpBillIncome.rebuy_int._obj._renderReadonly(true);
			IqpBillIncome.disc_rate._obj._renderReadonly(true);
			//IqpBillIncome.disc_rate._obj._renderHidden(true);
			//IqpBillIncome.disc_days._obj._renderHidden(true);
			//IqpBillIncome.disc_days._obj._renderRequired(false);
			//IqpBillIncome.int._obj._renderHidden(true);
			//IqpBillIncome.realAmt._obj._renderHidden(true);
			//IqpBillIncome.realAmt._obj._renderRequired(false);
			IqpBillIncome.adj_days._obj._renderHidden(false);
			IqpBillIncome.adj_days._obj._renderRequired(true);
			getDiscRate();

			$(".emp_field_label:eq(3)").text("回购起始日");
			$(".emp_field_label:eq(5)").text("回购金额");
			$(".emp_field_label:eq(6)").text("回购利率");
			$(".emp_field_label:eq(7)").text("回购天数");
		}else if(bizType == '07'){//直贴
			IqpBillIncome.adj_days._obj._renderHidden(false);
			//直贴需要控制付息信息显示
			initPintInfo();
			getDiscRate();
			
		}else if(bizType == '05'){//内部转贴现
			IqpBillIncome.adj_days._obj._renderHidden(false);
			IqpBillIncome.adj_days._obj._renderRequired(true);
			IqpBillIncome.disc_rate._obj._renderReadonly(true);//非直贴都不能修改利率，统一为批次利率
			getDiscRate();
		}else{
			if(bizType == '01' || bizType == '03'){//买入买断/卖出买断
				IqpBillIncome.adj_days._obj._renderHidden(false);
			}
			IqpBillIncome.disc_rate._obj._renderReadonly(true);//非直贴都不能修改利率，统一为批次利率
			getDiscRate();
		}

		//锁定按钮
		if('<%=op%>' == "view"){
			var bizType = '${context.IqpBillIncome.biz_type}';//业务类型
			if(bizType=='07'){
				document.getElementById("button_addIqpBillPintDetail").disabled="disabled";
				document.getElementById("button_delIqpBillPintDetail").disabled="disabled";
			}
			document.getElementById("button_save").disabled="disabled";
		}
	};
	
	//-------异步保存主表单页面信息-------
	function doSave(){
		if(!IqpBillIncome._checkAll()){
			return;
		}

		var bizType = '${context.IqpBillIncome.biz_type}';//业务类型
		if(bizType=='07'){//直贴，需要校验付息支付方式
			var dscnt_int_pay_mode = IqpBillIncome.dscnt_int_pay_mode._getValue();
			if(dscnt_int_pay_mode=='3'||dscnt_int_pay_mode=='2'){//协议付息需判断付息比例之和
				var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
				/*  检查有效记录的字段否为空 */
				var count = 0;
				var pint_perc_all = 0;
				for(var i=0;i<recordCount;i++){
					var optType = IqpBillPintDetailList._obj.data[i].optType._getValue();
					if(optType == "" || optType == "add" ){
						count++;
						var acctsvcr = IqpBillPintDetailList._obj.data[i].acctsvcr._getValue();
						var pint_no = IqpBillPintDetailList._obj.data[i].pint_no._getValue();
						var pint_perc = IqpBillPintDetailList._obj.data[i].pint_perc._getValue();
						var pint_amt = IqpBillPintDetailList._obj.data[i].pint_amt._getValue();
						var pint_acct_name = IqpBillPintDetailList._obj.data[i].pint_acct_name._getValue();
						if( acctsvcr == "" ){
							alert("第"+ count + "条记录开户行为空！");
							return;
						}
						if( pint_no == "" ){
							alert("第"+ count +"条记录付息账户为空！");
							return;
						}
						if( pint_perc == "" ){
							alert("第"+ count +"条记录付息比例为空！");
							return;
						}
						if( pint_acct_name == "" ){
							alert("第"+ count +"条记录付息账户开户名为空！");
							return;
						}
						//if( pint_amt == "" ){
							//alert("第"+ count +"条记录付息金额为空！");
							//return;
						//}
						pint_perc_all = parseFloat(pint_perc_all)+parseFloat(pint_perc);
					}
				}
				/*modified by wangj XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求 begin
				                  变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
				*/
				if(count>3){
					alert("付息明细不能超过3条！");
					return;
				}
				/*modified by wangj XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求 
				   	        变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求end
				*/
				if(pint_perc_all!=1){
					alert("付息比例之和应该为100%！");
					return;
				}
			}
		}
		
		var form = document.getElementById("submitForm");
		IqpBillIncome._toForm(form);
		if(bizType=='07'){
			IqpBillPintDetailList._toForm(form);
		}
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
					window.location.reload();
					doOnLoad();
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpBillIncomeRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	//-------计算利息信息-------
	function getDiscRate(){
		var amt = IqpBillIncome.drft_amt._getValue();
		var days = '${context.IqpBillIncome.disc_days}';
		var adays = IqpBillIncome.adj_days._getValue();
		var rate = IqpBillIncome.disc_rate._obj.element.value;
		if(rate==''){
			rate=0;
		}else{
			rate=parseFloat(rate)/100;
		}
		if(adays ==0){
			var intAmt = parseFloat(amt)*(parseInt(days)+parseInt(adays))*rate/360;
		}else{
			var intAmt = parseFloat(amt)*(parseInt(days)+parseInt(adays))*rate/360;
		}
		
		if(amt == null || amt == ""){
		}else {
		    /* modified by yangzy 2015/02/10 票据利息计算 start */
			IqpBillIncome.int._setValue(''+round(round(intAmt,6),2)+'');
			IqpBillIncome.realAmt._setValue(''+round(amt-round(round(intAmt,6),2),2)+'');
			/* modified by yangzy 2015/02/10 票据利息计算 end */
			if(adays ==0){
				IqpBillIncome.disc_days._setValue(parseInt(days));
			}else{
				IqpBillIncome.disc_days._setValue(parseInt(days));
			}
			
		}
	};	
	//-------计算回购利息信息-------
	function getRebuyRate(){
		/*var amt = IqpBillIncome.drft_amt._getValue();
		var days = IqpBillIncome.rebuy_days._getValue();
		var rate = IqpBillIncome.rebuy_rate._getValue();
		var intAmt = parseFloat(amt)*(parseInt(days))*rate/360;
		IqpBillIncome.rebuy_int._setValue(''+round(intAmt,2)+'');*/
	};
	//-------回购利息计算设置-------
	function changeRebuy(){
		var bizType = IqpBillIncome.biz_type._getValue();
		if(bizType == 04||bizType == 02){
			IqpBillIncome.fore_rebuy_date._obj._renderRequired(true);
			IqpBillIncome.rebuy_rate._obj._renderRequired(true);
			IqpBillIncome.due_rebuy_rate._obj._renderRequired(true);
			IqpBillIncome.rebuy_days._obj._renderRequired(true);
			IqpBillIncome.rebuy_int._obj._renderRequired(true);
		}else {
			IqpBillIncome.fore_rebuy_date._obj._renderRequired(false);
			IqpBillIncome.rebuy_rate._obj._renderRequired(false);
			IqpBillIncome.due_rebuy_rate._obj._renderRequired(false);
			IqpBillIncome.rebuy_days._obj._renderRequired(false);
			IqpBillIncome.rebuy_int._obj._renderRequired(false);
		}
	};

	function doReturn(){
		var url = '';
		var menuID = '${context.menuIdFather}';
		if(menuID == ''){
		//	url = '<emp:url action="queryIqpBillDetailList.do"/>&menuId=queryIqpRpddscnt&subMenuId=queryIqpBillList4Rpd&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
		}else{
		//	url = '<emp:url action="queryIqpBillDetailList.do"/>&menuId=${context.menuIdFather}&subMenuId=${context.subMenuIdFather}&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
		}
	    //url = EMPTools.encodeURI(url); 
	    //window.parent.location=url;
		//var url = '<emp:url action="queryIqpBillDetailList.do"/>&menuId=${context.menuIdFather}&subMenuId=${context.subMenuIdFather}&batch_no=${context.batch_no}&status=${context.status}&bill_type=${context.bill_type}&serno=${context.serno}&op=update';
	    //url = EMPTools.encodeURI(url); 
	   // window.parent.location=url;
	    window.history.go(-1);
	}

	//付息情况记录控制（放在所有付息js最前）
	rowIndex = 0;
	
	//增加一条记录
	function doAddIqpBillPintDetail(){
		var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
		IqpBillPintDetailList._obj._addRow();
		IqpBillPintDetailList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
		IqpBillPintDetailList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var row = recordCount-1;
		var id1 = row + '_view1';//每一个按钮id都是固定的
		var id = id1;
		IqpBillPintDetailList._obj.data[recordCount-1].acctsvcr._obj.addOneButton(id,'选择',querySOrg);
		var id2 = row + '_view2';//每一个按钮id都是固定的
		var id = id2;
		IqpBillPintDetailList._obj.data[recordCount-1].pint_no._obj.addOneButton(id,'获取',getAccountInfo);
		IqpBillPintDetailList._obj.data[recordCount-1].is_local_bank._setValue("1");
		document.getElementById(id1).disabled="disabled";
	}
	function doLoadSetAcctInfo(){
		var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
		for(var idx=0;idx<recordCount;idx++){
			if('<%=op%>' == "view"){
				
			}else{
				var id1 = idx + '_view1';//每一个按钮id都是固定的
				var id = id1;
				IqpBillPintDetailList._obj.data[idx].acctsvcr._obj.addOneButton(id,'选择',querySOrg);
				var id2 = idx + '_view2';//每一个按钮id都是固定的
				var id = id2;
				IqpBillPintDetailList._obj.data[idx].pint_no._obj.addOneButton(id,'获取',getAccountInfo);
				var is_local_bank = IqpBillPintDetailList._obj.data[idx].is_local_bank._getValue();
				if(is_local_bank == '2'){
					IqpBillPintDetailList._obj.data[idx].pint_acct_name._obj._renderReadonly(false) ;
					var id1 = idx + '_view1';//每一个按钮id都是固定的
					var id2 = idx + '_view2';
					document.getElementById(id2).disabled="disabled";
					document.getElementById(id1).disabled="";
				}else{
					IqpBillPintDetailList._obj.data[idx].pint_acct_name._obj._renderReadonly(true) ;
					var id1 = idx + '_view1';//每一个按钮id都是固定的
					var id2 = idx + '_view2';
					document.getElementById(id1).disabled="disabled";
					document.getElementById(id2).disabled="";
				}
			}
		}
    };
	function doSetAcctInfo(){
		//付息账号确认为本行账号，已设置为隐藏  modify by zhaozq 2014-08-07 
		var idx = IqpBillPintDetailList._obj.getSelectedIdx();
		var is_local_bank = IqpBillPintDetailList._obj.data[idx].is_local_bank._getValue();
		if(is_local_bank == '1'){
			IqpBillPintDetailList._obj.data[idx].is_local_bank._setValue("2");
			IqpBillPintDetailList._obj.data[idx].acctsvcr._setValue("");
			IqpBillPintDetailList._obj.data[idx].acctsvcrnm._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_no._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_acct_name._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_acct_name._obj._renderReadonly(false) ;
			var id1 = idx + '_view1';//每一个按钮id都是固定的
			var id2 = idx + '_view2';
			document.getElementById(id2).disabled="disabled";
			document.getElementById(id1).disabled="";
		}else{
			IqpBillPintDetailList._obj.data[idx].is_local_bank._setValue("1");
			IqpBillPintDetailList._obj.data[idx].acctsvcr._setValue("");
			IqpBillPintDetailList._obj.data[idx].acctsvcrnm._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_no._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_acct_name._setValue("");
			IqpBillPintDetailList._obj.data[idx].pint_acct_name._obj._renderReadonly(true) ;
			var id1 = idx + '_view1';//每一个按钮id都是固定的
			var id2 = idx + '_view2';
			document.getElementById(id1).disabled="disabled";
			document.getElementById(id2).disabled="";
		}
    };
	function querySOrg(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		/*modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin*/
		var url = '<emp:url action="getPrdBankInfoPopList.do"/>&popReturnMethod=getOrgNo&status=1';
		/*modified by wangj 需求编号：ED150612003 ODS系统取数需求  end */
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	
	function getOrgNo(data){
		rowIndexStr=rowIndex;
		IqpBillPintDetailList._obj.data[rowIndexStr].acctsvcr._setValue(data.bank_no._getValue());
		IqpBillPintDetailList._obj.data[rowIndexStr].acctsvcrnm._setValue(data.bank_name._getValue());
    };
	//联机获取付息账号
	function getAccountInfo(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var acctNo = IqpBillPintDetailList._obj.data[rowIndex].pint_no._getValue();
        if(acctNo == null || acctNo == ""){
			alert("请先输入账号信息！");
			return;
        }
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var retMsg = jsonstr.retMsg;
				if(flag == "success"){
					var ACCT_NO = jsonstr.BODY.ACCT_NO;
					var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
					var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
					var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME;
					var ORG_NO = jsonstr.BODY.ORG_NO;
					var ACCT_GL_CODE = jsonstr.BODY.GL_CODE;//增加科目号
					var CCY=jsonstr.BODY.CCY;//增加币种
					var C_INTERBANK_ID=jsonstr.BODY.C_INTERBANK_ID;
					IqpBillPintDetailList._obj.data[rowIndex].pint_acct_name._setValue(ACCT_NAME);
					IqpBillPintDetailList._obj.data[rowIndex].acct_gl_code._setValue(ACCT_GL_CODE);
					IqpBillPintDetailList._obj.data[rowIndex].cur_type._setValue(CCY);
					IqpBillPintDetailList._obj.data[rowIndex].acc_type._setValue(ACCT_TYPE);
					IqpBillPintDetailList._obj.data[rowIndex].acctsvcr._setValue(OPEN_ACCT_BRANCH_ID);
					IqpBillPintDetailList._obj.data[rowIndex].acctsvcrnm._setValue(OPEN_ACCT_BRANCH_NAME);
					IqpBillPintDetailList._obj.data[rowIndex].interbank_id._setValue(C_INTERBANK_ID);
				}else {
					alert(retMsg); 
					IqpBillPintDetailList._obj.data[rowIndex].pint_no._setValue("");
					IqpBillPintDetailList._obj.data[rowIndex].pint_acct_name._setValue("");
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
		var url = '<emp:url action="clientTrade4Esb.do"/>?acct_no='+acctNo+'&service_code=11003000007&sence_code=17';	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)	 
	};

	//删除一条记录
	function doDelIqpBillPintDetail(){
		var dataRow =  IqpBillPintDetailList._obj.getSelectedData()[0];
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var optType = dataRow.optType._getValue() ;

				dataRow.acctsvcr._obj._renderHidden(true) ;
				dataRow.acctsvcrnm._obj._renderHidden(true) ;
				dataRow.pint_no._obj._renderHidden(true) ;
				dataRow.pint_acct_name._obj._renderHidden(true) ;
				dataRow.pint_perc._obj._renderHidden(true) ;
				dataRow.pint_amt._obj._renderHidden(true) ;
				dataRow.is_local_bank._obj._renderHidden(true) ;
				dataRow.doit._obj._renderHidden(true) ;
				dataRow.displayid._obj._renderHidden(true) ;
				if(optType == 'add'){
					dataRow.optType._setValue("none") ;
				}else if(optType == 'del'){
					dataRow.optType._setValue("del") ;
				}else if(optType == ''){
					dataRow.optType._setValue("del");
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	//删除所有付息信息
	function doDeleteAll(){
		var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
		for(i=0;i<recordCount;i++){
			var dataRow =  IqpBillPintDetailList._obj.data[i];
			//alert(dataRow);
			var optType = dataRow.optType._getValue() ;

			dataRow.acctsvcr._obj._renderHidden(true) ;
			dataRow.acctsvcrnm._obj._renderHidden(true) ;
			dataRow.pint_no._obj._renderHidden(true) ;
			dataRow.pint_acct_name._obj._renderHidden(true) ;
			dataRow.pint_perc._obj._renderHidden(true) ;
			dataRow.pint_amt._obj._renderHidden(true) ;
			
			if(optType == 'add'){
				dataRow.optType._setValue("none") ;
			}else if(optType == 'del'){
				dataRow.optType._setValue("del") ;
			}else if(optType == ''){
				dataRow.optType._setValue("del");
			}
			IqpBillPintDetailList._obj._deleteRow(recordCount-i-1);   //删除行
			ImpCfForecastList._obj.recordCount -=1;
		}
	}

	function setPintInfo(){
		var dscnt_int_pay_mode = IqpBillIncome.dscnt_int_pay_mode._getValue();
		if(dscnt_int_pay_mode=='2'){//买方付息
			//锁定按钮                                     
			document.getElementById("button_addIqpBillPintDetail").disabled="disabled";
			document.getElementById("button_delIqpBillPintDetail").disabled="disabled";

			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = '';

			var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
			for(var i=parseInt(recordCount)-1;i>=0;i--){
			    IqpBillPintDetailList._obj.data[i].optType._setValue("del");//判断操作方式
				IqpBillPintDetailList._obj._deleteRow(i);   //删除行
				IqpBillPintDetailList._obj.recordCount -=1;
			}
			
			//增加一条记录，并只读
			IqpBillPintDetailList._obj._addRow();
			IqpBillPintDetailList._obj.recordCount +=1; 	//增加总记录数
			var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
			IqpBillPintDetailList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
			var row = recordCount-1;
			var id1 = row + '_view1';
			var id=id1;
			IqpBillPintDetailList._obj.data[recordCount-1].acctsvcr._obj.addOneButton(id,'选择',querySOrg);
			var id2 = row + '_view2';
			var id=id2;
			IqpBillPintDetailList._obj.data[recordCount-1].pint_no._obj.addOneButton(id,'获取',getAccountInfo);
			IqpBillPintDetailList._obj.data[recordCount-1].pint_perc._setValue(1);
			IqpBillPintDetailList._obj.data[recordCount-1].pint_perc._obj._renderReadonly(true)
			IqpBillPintDetailList._obj.data[recordCount-1].pint_amt._setValue(IqpBillIncome.int._getValue());
			IqpBillPintDetailList._obj.data[recordCount-1].is_local_bank._setValue("1");
			document.getElementById(id1).disabled="disabled";
		}else if(dscnt_int_pay_mode=='3'){//协议付息
			//锁定按钮
			document.getElementById("button_addIqpBillPintDetail").disabled="";
			document.getElementById("button_delIqpBillPintDetail").disabled="";
			
			var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
			for(var i=parseInt(recordCount)-1;i>=0;i--){
				IqpBillPintDetailList._obj.data[i].optType._setValue("del");//判断操作方式
				IqpBillPintDetailList._obj._deleteRow(i);   //删除行
				IqpBillPintDetailList._obj.recordCount -=1;
			}

			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = '';
		}else{//卖方付息、不选择
			//锁定按钮
			document.getElementById("button_addIqpBillPintDetail").disabled="";
			document.getElementById("button_delIqpBillPintDetail").disabled="";
			
			var recordCount = IqpBillPintDetailList._obj.recordCount;//取总记录数
			for(var i=parseInt(recordCount)-1;i>=0;i--){
				IqpBillPintDetailList._obj.data[i].optType._setValue("del");//判断操作方式
				IqpBillPintDetailList._obj._deleteRow(i);   //删除行
				IqpBillPintDetailList._obj.recordCount -=1;
			}

			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = 'none';
		}
	}

	function calPintAmt(id){
		//alert(this.);
		alert(id);
		rowIndex = parseInt(id.split('_')[0]);
		alert(rowIndex);
		var dataRow =  IqpBillPintDetailList._obj.data[rowIndex];
		var pint_perc = dataRow.pint_amt._getValue();
		var int_amt = IqpBillIncome.int._getValue();
		dataRow.pint_amt._setValue(int_amt*pint_perc);
	}

	function initPintInfo(){
		var dscnt_int_pay_mode = IqpBillIncome.dscnt_int_pay_mode._getValue();
		if(dscnt_int_pay_mode=='2'){//买方付息
			doLoadSetAcctInfo();
			//锁定按钮
			document.getElementById("button_addIqpBillPintDetail").disabled="disabled";
			document.getElementById("button_delIqpBillPintDetail").disabled="disabled";

			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = '';
		}else if(dscnt_int_pay_mode=='3'){//协议付息
			doLoadSetAcctInfo();
			//锁定按钮
			document.getElementById("button_addIqpBillPintDetail").disabled="";
			document.getElementById("button_delIqpBillPintDetail").disabled="";
			
			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = '';
		}else{//卖方付息、不选择
			//锁定按钮
			document.getElementById("button_addIqpBillPintDetail").disabled="";
			document.getElementById("button_delIqpBillPintDetail").disabled="";
			
			var pintDetail = document.getElementById("pintDetail");
			pintDetail.style.display = 'none';
		}
	};

	function checkForeRebuyDate(){
		var biz_type = IqpBillIncome.biz_type._getValue();
		//卖出回购
        if(biz_type == "04"){
        	var porder_no = IqpBillIncome.porder_no._getValue();
        	var handleSuccess = function(o){
    			if(o.responseText !== undefined) {
    				try {
    					var jsonstr = eval("("+o.responseText+")");
    				} catch(e) {
    					alert("Parse jsonstr1 define error!" + e.message);
    					return;
    				}
    				var flag = jsonstr.flag;
    				var date = jsonstr.date;
    				if(flag == "success"){
    					if(date != ""){
    					   var fore_rebuy_date = IqpBillIncome.fore_rebuy_date
                           if(fore_rebuy_date >date){
                               alert("该票据买入返售的到期日为："+date+",回购到期日不能超过该日期!");
                           }
        				}
    				}else {
    					alert("查询失败！");
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

    		var url = '<emp:url action="checkForeRebuyDate.do"/>?porder_no='+porder_no;
    		url = EMPTools.encodeURI(url);
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
        }
	};

	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updateIqpBillIncomeRecord.do" method="POST">
		<emp:gridLayout id="IqpBillIncomeGroup" maxColumn="2" title="利息计算(利息=票面金额*计算天数*执行年利率/360)">
		<emp:text id="IqpBillIncome.batch_no" label="批次号" maxlength="40" defvalue="${context.batch_no}" required="false" hidden="true" readonly="true" />
			<emp:text id="IqpBillIncome.porder_no" label="汇票号码" maxlength="40" required="true" defvalue="${context.porder_no}" readonly="true" />
			<emp:select id="IqpBillIncome.biz_type" label="业务类型" required="true" readonly="true" dictname="STD_ZB_BUSI_TYPE" onblur="changeRebuy();" colSpan="2"/>
			<emp:date id="IqpBillIncome.fore_disc_date" label="转/贴现日期" required="true"  readonly="true"/>
			<% 
			if(bizType.equals("04")||bizType.equals("02")){
		    %>
		    <emp:date id="IqpBillIncome.fore_rebuy_date" label="回购到期日" required="false" />
		    <%
			}
		    %>
			<emp:text id="IqpBillIncome.drft_amt" label="票面金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBillIncome.disc_rate" label="转/贴现利率" maxlength="10" required="true" dataType="Rate" onblur="getDiscRate();"/>
			<emp:text id="IqpBillIncome.disc_days" label="贴现天数" maxlength="38" required="true"  readonly="true" />
			
			<emp:text id="IqpBillIncome.adj_days" label="调整天数"  maxlength="38" hidden="true" defvalue="0" required="true" onblur="getDiscRate();"/>
			<emp:text id="IqpBillIncome.int" label="利息" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpBillIncome.realAmt" label="实付金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpBillIncome.rebuy_days" label="回购天数" maxlength="38" required="false" hidden="true" onblur="getRebuyRate();"/>
			<emp:text id="IqpBillIncome.rebuy_int" label="回购利息" maxlength="18" required="false" hidden="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		
		<% 
			if(bizType.equals("07")){
		%>
		<emp:gridLayout id="IqpBillPintDetailGroup" maxColumn="2" title="贴现付息情况">
			<emp:select id="IqpBillIncome.dscnt_int_pay_mode" label="贴现利息支付方式" required="true" dictname="STD_ZB_DSCNT_DEFRAY_MODE" colSpan="2" onchange="setPintInfo()"/>
		</emp:gridLayout>
		<div id="pintDetail" style="display:none">
		    <div class='emp_gridlayout_title' id="commen">付息明细&nbsp;</div>
			<div align="left">
			
				<emp:button id="addIqpBillPintDetail" label="新增" />
				<emp:button id="delIqpBillPintDetail" label="删除" />
			
			</div>
			<% 
			if(op.equals("view")){
		    %>
		    <emp:table icollName="IqpBillPintDetailList" pageMode="false" editable="true" url="">
			    <emp:text id="optType" label="操作方式" hidden="true" />
			    <emp:link id="doit" label="设置" imageFile="images/default/logo_setting.gif" opName="设置是否本行账号" operation="setAcctInfo" hidden="true"/>  
				<emp:select id="is_local_bank" label="是否本行账号" flat="false" required="false" dictname="STD_ZX_YES_NO" readonly="true" cssFakeInputClass="emp_field_select_longinput"/>
				<emp:text id="pint_no" label="付息账号" flat="false" required="false" dataType="Acct"/>
				<emp:text id="pint_acct_name" label="付息账户开户名" flat="false" readonly="true" required="false"/>
				<emp:text id="acctsvcr" label="开户行行号" flat="false" readonly="true" required="false" cssElementClass="emp_input"/>
				<emp:text id="acctsvcrnm" label="开户行行名" flat="false" readonly="true" required="false"/>
				<emp:text id="pint_perc" label="付息比例" flat="false" dataType="Percent" cssElementClass="emp_input1"/>
				<emp:text id="pint_amt" label="付息金额" flat="false" readonly="true" cssElementClass="emp_input1"/>
				<emp:text id="batch_no" label="批次号" hidden="true" defvalue="${context.batch_no}"/>
				<emp:text id="porder_no" label="汇票号码" hidden="true" defvalue="${context.porder_no}"/>
				<emp:text id="pk" label="物理主键" hidden="true"/>
				<emp:text id="acct_gl_code" label="科目号" hidden="true"/>
				<emp:text id="cur_type" label="币种" hidden="true"/>
				<emp:text id="acc_type" label="账户类型" hidden="true"/>
				<emp:text id="interbank_id" label="银联行号" hidden="true" />
			</emp:table>
		    <% 
			}else{
		    %>
		    <emp:table icollName="IqpBillPintDetailList" pageMode="false" editable="true" url="">
			    <emp:text id="optType" label="操作方式" hidden="true" />
			    <emp:link id="doit" label="设置" imageFile="images/default/logo_setting.gif" opName="设置是否本行账号" operation="setAcctInfo" hidden="true"/>  
				<emp:select id="is_local_bank" label="是否本行账号" flat="false" required="false" dictname="STD_ZX_YES_NO" readonly="true" cssFakeInputClass="emp_field_select_longinput"/>
				<emp:text id="pint_no" label="付息账号" flat="false" required="false" dataType="Acct"/>
				<emp:text id="pint_acct_name" label="付息账户开户名" flat="false" readonly="true" required="false"/>
				<emp:text id="acctsvcr" label="开户行行号/机构号" flat="false" readonly="true" required="false" cssElementClass="emp_input"/>
				<emp:text id="acctsvcrnm" label="开户行行名/机构名" flat="false" readonly="true" required="false"/>
				<emp:text id="pint_perc" label="付息比例" flat="false" dataType="Percent" cssElementClass="emp_input1"/>
				<emp:text id="pint_amt" label="付息金额" flat="false" readonly="true" cssElementClass="emp_input1"/>
				<emp:text id="batch_no" label="批次号" hidden="true" defvalue="${context.batch_no}"/>
				<emp:text id="porder_no" label="汇票号码" hidden="true" defvalue="${context.porder_no}"/>
				<emp:text id="pk" label="物理主键" hidden="true"/>
				<emp:text id="acct_gl_code" label="科目号" hidden="true"/>
				<emp:text id="cur_type" label="币种" hidden="true"/>
				<emp:text id="acc_type" label="账户类型" hidden="true"/>
				<emp:text id="interbank_id" label="银联行号" hidden="true" />
			</emp:table>
		     <% 
			}
		    %>
			
		</div>
		<% 
			}
		%>
		<div align="center">
			<br>
			
			<emp:button id="save" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

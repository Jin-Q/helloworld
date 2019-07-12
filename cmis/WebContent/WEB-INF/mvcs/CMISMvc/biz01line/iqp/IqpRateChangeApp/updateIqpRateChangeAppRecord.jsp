<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsIqpRateChangeApp.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function onload(){
		ir_accord_typeChange("init");
		reality_ir_yChange();
		selectIrType();
		new_ir_accord_typeChange("init");
		new_reality_ir_yChange();
		getRulMounth();
		newGetRulMounth();
		changeIrFloatType();
		changeIrFloatTypeForOld();
		changeOverdueFloatTypeForOld();
		changeDefaultFloatTypeForOld();
		ifRrAccordType();
		ifRrAccordType4New();

		//利率调整不可修改，利率调整方式  add by zhaozq 20150127 start
		IqpRateChangeApp.new_ir_adjust_type._obj._renderReadonly(true);//利率调整方式
		//利率调整不可修改，利率调整方式  add by zhaozq 20150127 end
	};

	//-----------------提交---------------------
	function doSub(data){
		var form = document.getElementById("submitForm");
		if(IqpRateChangeApp._checkAll()){
			IqpRateChangeApp._toForm(form); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						if(data=="doSubWF"){
							doSubmitWF();
						}else{
							alert("保存成功!");
							window.location.reload();
						}
					}else {
						alert(msg);
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};

	function doSubWF(){
		doSub("doSubWF");
	};

	//-----------提交流程----------
	function doSubmitWF(apply_type){
		var serno = IqpRateChangeApp.serno._getValue();
		var cus_id = IqpRateChangeApp.cus_id._getValue();
		var cus_name = IqpRateChangeApp.cus_id_displayname._getValue();
		var approve_status = IqpRateChangeApp.approve_status._getValue();
		WfiJoin.table_name._setValue("IqpRateChangeApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.prd_name._setValue("利率调整申请");
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
	    WfiJoin.appl_type._setValue("0023");//0023利率调整申请
		initWFSubmit(false);
	};

	function doBack(){
		var url = '<emp:url action="queryIqpRateChangeAppList.do"/>'; 
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpRateChangeAppRecord.do" method="POST">
	
		<emp:gridLayout id="IqpRateChangeAppGroup" title="借据基本信息" maxColumn="2">
			<emp:text id="IqpRateChangeApp.serno" label="业务流水号" maxlength="40" required="false" hidden="true" />
			<!--XD150520037_信贷系统利率调整修改优化 start-->
			<emp:pop id="IqpRateChangeApp.bill_no" label="借据编号" url="queryAccLoanPop.do?returnMethod=selAccInfo&condition=a.acc_status='1' and a.prd_id not in('400020','400021','700020','700021','500032','400022','400024','400023','200024') and a.manager_br_id='${context.organNo}' and a.cont_no in (select cont_no from cus_manager where manager_id = '${context.currentUserId}' and is_main_manager = '1')"  required="true" />
			<!--XD150520037_信贷系统利率调整修改优化 end-->
			<emp:text id="IqpRateChangeApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpRateChangeApp.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpRateChangeApp.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpRateChangeApp.loan_amt" label="贷款金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpRateChangeApp.loan_balance" label="贷款余额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpRateChangeApp.distr_date" label="发放日期" maxlength="10" required="false" readonly="true"/>
			<emp:text id="IqpRateChangeApp.end_date" label="到期日期" maxlength="10" required="false" readonly="true"/>
		</emp:gridLayout>
		
		<div id="rateInfo" >
		<emp:gridLayout id="" maxColumn="2" title="原利率信息">
			<emp:select id="IqpRateChangeApp.ir_accord_type" label="利率依据方式 " required="false" dictname="STD_ZB_IR_ACCORD_TYPE" readonly="true"/>
			<emp:select id="IqpRateChangeApp.ir_type" label="利率种类 " required="false" dictname="STD_ZB_RATE_TYPE" readonly="true"/>
			<emp:text id="IqpRateChangeApp.ruling_ir" label="基准利率（年）" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="ruling_mounth" label="对应基准利率(月)" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate4Month" readonly="true"/>		
			<emp:text id="IqpRateChangeApp.pad_rate_y" label="垫款利率（年）" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:select id="IqpRateChangeApp.ir_adjust_type" label="利率调整方式" required="false" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
			<emp:select id="IqpRateChangeApp.ir_float_type" label="利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.ir_float_rate" label="利率浮动比" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpRateChangeApp.ir_float_point" label="贷款利率浮动点数" maxlength="10" required="false" colSpan="2" readonly="true"/>
			<emp:text id="IqpRateChangeApp.reality_ir_y" label="执行利率（年）" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="reality_mounth" cssElementClass="emp_currency_text_readonly" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			
			<emp:select id="IqpRateChangeApp.overdue_float_type" label="逾期利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.overdue_rate" label="逾期利率浮动比" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpRateChangeApp.overdue_point" label="逾期利率浮动点" cssElementClass="emp_currency_text_readonly" maxlength="10" required="false" readonly="true"/>
			<emp:text id="IqpRateChangeApp.overdue_rate_y" label="逾期利率（年）" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			
			<emp:select id="IqpRateChangeApp.default_float_type" label="违约利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.default_rate" label="违约利率浮动比" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpRateChangeApp.default_point" label="违约利率浮动点" maxlength="10" required="false" readonly="true"/>
			<emp:text id="IqpRateChangeApp.default_rate_y" label="违约利率（年）" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>
		    <emp:text id="IqpRateChangeApp.ruling_ir_code" label="基准利率代码" hidden="true" maxlength="40"  required="false"/>
		 </emp:gridLayout>
		 </div>
		 
		 <emp:gridLayout id="" maxColumn="2" title="调整后利率信息">
			<emp:select id="IqpRateChangeApp.new_ir_accord_type" label="利率依据方式 " onchange="new_ir_accord_typeChange('change');" required="false" dictname="STD_ZB_IR_ACCORD_TYPE" readonly="true"/>
			<emp:select id="IqpRateChangeApp.new_ir_type" label="利率种类 " required="false" dictname="STD_ZB_RATE_TYPE" onchange="ir_typeChange()"/>
			<emp:text id="IqpRateChangeApp.new_ruling_ir" label="基准利率（年）"  maxlength="16" required="false" readonly="true" dataType="Rate" />
			<emp:text id="new_ruling_mounth" label="基准利率(月)"  maxlength="16" required="false" readonly="true" dataType="Rate4Month" cssElementClass="emp_currency_text_readonly" />	
			<emp:text id="IqpRateChangeApp.new_pad_rate_y" label="垫款利率（年）"  maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpRateChangeApp.new_ir_adjust_type" label="利率调整方式" required="false" dictname="STD_IR_ADJUST_TYPE" />
			<emp:select id="IqpRateChangeApp.new_ir_float_type" label="利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType()" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.new_ir_float_rate" label="利率浮动比"  maxlength="16" required="false" dataType="Rate" onchange="getRelYM();"/>
			<emp:text id="IqpRateChangeApp.new_ir_float_point" label="贷款利率浮动点数" maxlength="10" required="false" colSpan="2" onchange="getRelYM();"/>
			<emp:text id="IqpRateChangeApp.new_reality_ir_y" label="执行利率（年）"  maxlength="16" required="false" dataType="Rate" onchange="new_reality_ir_yChange();caculateOverdueRate();caculateDefaultRate();"/>
			<emp:text id="new_reality_mounth"  label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true" cssElementClass="emp_currency_text_readonly"/>	
			
			<emp:select id="IqpRateChangeApp.new_overdue_float_type" label="逾期利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.new_overdue_rate" label="逾期利率浮动比"  maxlength="16" required="false" dataType="Rate" onchange="getOverdueRateY();" />
			<emp:text id="IqpRateChangeApp.new_overdue_point" label="逾期利率浮动点"  maxlength="10" required="false" />
			<emp:text id="IqpRateChangeApp.new_overdue_rate_y" label="逾期利率（年）"  maxlength="16" onchange="caculateOverdueRate();" required="false" dataType="Rate" colSpan="2" readonly="true"/>
			
			<emp:select id="IqpRateChangeApp.new_default_float_type" label="违约利率浮动方式 " required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true" onchange="changeDefaultFloatType();" colSpan="2"/>
			<emp:text id="IqpRateChangeApp.new_default_rate" label="违约利率浮动比"  maxlength="16" required="false" dataType="Rate" onchange="getDefaultRateY();"/>
			<emp:text id="IqpRateChangeApp.new_default_point" label="违约利率浮动点" maxlength="10" required="false" onchange="getDefaultRateY();"/>
			<emp:text id="IqpRateChangeApp.new_default_rate_y" label="违约利率（年）"  maxlength="16" onchange="caculateDefaultRate();" required="false" dataType="Rate" readonly="true"/>
			<!-- 利率生效日期为当天，不可改  zhaozq 20141211 start -->
			<emp:date id="IqpRateChangeApp.new_inure_date" label="调整后利率生效日期" required="true" readonly="true" defvalue="${context.OPENDAY}" onblur="checkDate()"/>
			<!-- 利率生效日期为当天，不可改  zhaozq 20141211 end -->
			<emp:select id="IqpRateChangeApp.new_rate_change_reason" label="申请利率调整事由" required="true" dictname="STD_RATE_CHANGE_REASON" colSpan="2"/>
			<emp:textarea id="IqpRateChangeApp.change_rate_res" label="利率变更原因" maxlength="250" required="false" colSpan="2" />
		    <emp:text id="IqpRateChangeApp.new_ruling_ir_code" label="调整后基准利率代码" hidden="true" maxlength="40"  required="false"/>
		 </emp:gridLayout>
		 <emp:gridLayout id="" maxColumn="3" title="登记信息">		
			<emp:text id="IqpRateChangeApp.manager_br_id_displayname" label="主管机构"  required="true" readonly="true" colSpan="3"/>
		    <emp:text id="IqpRateChangeApp.input_id_displayname" label="登记人" required="true"  readonly="true"/>
			<emp:text id="IqpRateChangeApp.input_br_id_displayname" label="登记机构"  required="true"  readonly="true"/>
			<emp:date id="IqpRateChangeApp.input_date" label="登记日期" required="true" readonly="true"  />
			
			<emp:text id="IqpRateChangeApp.manager_br_id" label="主管机构" maxlength="20" required="false" readonly="true" hidden="true"/> 
			<emp:text id="IqpRateChangeApp.input_id" label="登记人"  hidden="true" maxlength="20" required="false"  readonly="true"/>
			<emp:text id="IqpRateChangeApp.input_br_id" label="登记机构"  hidden="true" maxlength="20" required="false"  readonly="true"/>
		    <emp:select id="IqpRateChangeApp.approve_status" label="申请状态" required="false" hidden="true" dictname="WF_APP_STATUS" />
		
		    <emp:text id="IqpRateChangeApp.prd_id" label="产品编号" maxlength="40" required="false" hidden="true" />
		    <emp:text id="IqpRateChangeApp.term_type" label="期限类型" maxlength="40" required="false" hidden="true" />
		    <emp:text id="IqpRateChangeApp.cont_term" label="期限" maxlength="40" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="subWF" label="提交" op="update"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

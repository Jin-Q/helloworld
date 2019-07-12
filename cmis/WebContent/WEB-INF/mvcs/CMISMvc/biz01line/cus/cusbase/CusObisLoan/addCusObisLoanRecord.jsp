<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_field_text_input2 {width: 350px;}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<% 
	String action = "addCusObisLoanRecord.do";
	String oper = request.getParameter("oper");
	if (oper != null && oper.equals("update")) {
		action = "updateCusObisLoanRecord.do";
	}
%>
<script type="text/javascript">
	function doAddCusObisLoan(){
		var result = CusObisLoan._checkAll();
		if(result){
				var form = document.getElementById("submitForm");
				CusObisLoan._toForm(form)
				toSubmitFormAdd(form);
		}//else alert("请输入必填项！");
	}
	function toSubmitFormAdd(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusObisLoan.cus_id="+CusObisLoan.cus_id._obj.element.value;
						var EditFlag  ='${context.EditFlag}';
						var url = '<emp:url action="getCusObisLoanAddPage.do"/>&'+paramStr+"&oper=add&EditFlag="+EditFlag;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
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
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	}

	function goback(){
		var paramStr="CusObisLoan.cus_id="+CusObisLoan.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusObisLoanList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};

	function checkStrData(){
		var strDt = CusObisLoan.loan_str_dt._getValue();
		var endDt = CusObisLoan.loan_end_dt._getValue();
		var openDay = '${context.OPENDAY}';
		if(strDt!=null && strDt!="" ){
			if(strDt>openDay){
				alert("起始日期不能大于当前日期！");
				CusObisLoan.loan_str_dt._setValue("");
				return;
			}
			if(endDt!=null && endDt!="" ){
				if(strDt>=endDt){
					alert("起始日期要小于 到期日期！");
					CusObisLoan.loan_end_dt._setValue("");
				}
			}
		}
	}
	
	function doReturn(){
		goback();
	}

	/*--user code begin--*/
			
	/*--user code end--*/
	function cheakContAmt(){
		var cont_amt = CusObisLoan.cont_amt._getValue(); //合同金额
		var loan_blc = CusObisLoan.loan_blc._getValue(); //余额
		if(parseFloat(cont_amt)<parseFloat(loan_blc)){
			alert("合同金额应大于等于余额！");
			CusObisLoan.loan_blc._setValue("");
		}
	}

	function doUpdateCusObisLoan() {
		var result = CusObisLoan._checkAll();
		if(result){
			var form = document.getElementById("submitForm");
			CusObisLoan._toForm(form)
			toSubmitFormUpd(form);
		}//else alert("请输入必填项！");
	};
	
	function toSubmitFormUpd(form){
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="修改成功"){
						alert("修改成功!");
						doReturn();
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
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};
	
	
	//选币种
	function getchangeCur() {
	    var curType = CusObisLoan.cont_cur_typ._getValue();
	    if(curType==null||curType==''){
	    	  alert("请您选择币种！");
	          return;
		}
	    if(curType=="CNY"){
	    	CusObisLoan.exchange_rate._setValue("1");
	    	CusObisLoan.exchange_rate._obj._renderReadonly(true);
		}
	    else{
	    	CusObisLoan.exchange_rate._obj._renderReadonly(false);
	    }
	    var loan_typ = CusObisLoan.loan_typ._getValue();
		if(loan_typ=="08"){
	    	CusObisLoan.exchange_rate._obj._renderHidden(false);
		}
	    var cont_amt = CusObisLoan.cont_amt._getValue();
	    cont_amt = toFixed2(cont_amt);

		var exchange_rate = CusObisLoan.exchange_rate._getValue();
	    exchange_rate = toFixed2(exchange_rate);

	    var loan_blc = CusObisLoan.loan_blc._getValue();
	    loan_blc = toFixed2(loan_blc);

		var interest_blc1 = CusObisLoan.interest_blc1._getValue();
		interest_blc1 = toFixed2(interest_blc1);

		var interest_blc2 = CusObisLoan.interest_blc2._getValue();
		interest_blc2 = toFixed2(interest_blc2);
		
		var int_overdue_amt = CusObisLoan.int_overdue_amt._getValue();
		int_overdue_amt = toFixed2(int_overdue_amt);
		
	    var cont_amt_cny =  cont_amt*exchange_rate;//合同金额折算成人民币
	    var loan_blc_cny =loan_blc*exchange_rate;//余额折算成人民币
	    var interest_blc1_cny =interest_blc1*exchange_rate;//表内利息余额折算成人民币
	    var interest_blc2_cny =interest_blc2*exchange_rate;//表外利息余额折算成人民币
	    var int_overdue_amt_cny =int_overdue_amt*exchange_rate;//逾期金额折算成人民币
		var minValue = -99999999999;
		var maxValue = 99999999999;
		if(parseFloat(cont_amt_cny) >= minValue && parseFloat(cont_amt_cny) <= maxValue){
			CusObisLoan.cont_amt_cny._setValue(cont_amt_cny+"");
		}else{
			alert("【合同金额折算成人民币】取值范围应在（"+minValue+"~"+maxValue+"）之间！");
			CusObisLoan.cont_amt._setValue("");
			CusObisLoan.cont_amt_cny._setValue("");
		}
		
		if(parseFloat(loan_blc_cny) >= minValue && parseFloat(loan_blc_cny) <= maxValue){
			CusObisLoan.loan_blc_cny._setValue(loan_blc_cny+"");
		}else{
			alert("【余额折算成人民币】取值范围应在（"+minValue+"~"+maxValue+"）之间！");
			CusObisLoan.loan_blc._setValue("");
			CusObisLoan.loan_blc_cny._setValue("");
		}

		if(parseFloat(interest_blc1_cny) >= minValue && parseFloat(interest_blc1_cny) <= maxValue){
			CusObisLoan.interest_blc1_cny._setValue(interest_blc1_cny+"");
		}else{
			alert("【表内利息余额折算成人民币】取值范围应在（"+minValue+"~"+maxValue+"）之间！");
			CusObisLoan.interest_blc1._setValue("");
			CusObisLoan.interest_blc1_cny._setValue("");
		}

		if(parseFloat(interest_blc2_cny) >= minValue && parseFloat(interest_blc2_cny) <= maxValue){
			CusObisLoan.interest_blc2_cny._setValue(interest_blc2_cny+"");
		}else{
			alert("【表外利息余额折算成人民币】取值范围应在（"+minValue+"~"+maxValue+"）之间！");
			CusObisLoan.interest_blc2._setValue("");
			CusObisLoan.interest_blc2_cny._setValue("");
		}

		if(parseFloat(int_overdue_amt_cny) >= minValue && parseFloat(int_overdue_amt_cny) <= maxValue){
			CusObisLoan.int_overdue_amt_cny._setValue(int_overdue_amt_cny+"");
		}else{
			alert("【逾期金额折算成人民币】取值范围应在（"+minValue+"~"+maxValue+"）之间！");
			CusObisLoan.int_overdue_amt._setValue("");
			CusObisLoan.int_overdue_amt_cny._setValue("");
		}
		
	}
	 //转换FLOAT 并保留2位有效小数
	function toFixed2(value,fix){
	   if(fix==null)fix=2;
	   var _fix=parseInt(fix);
	   var _value=parseFloat(value);
	   if(isNaN(_value))_value=0;
       return (_value.toFixed(_fix));
	}

	//getchangeCur();
	function changeBizType(){
	    var loan_typ = CusObisLoan.loan_typ._getValue();
		if(loan_typ=="08"){
	    	CusObisLoan.int_overdue_times._obj._renderHidden(false);
		}
	}

	function checkRate(){
		var exchange_rate = CusObisLoan.exchange_rate._getValue();
		if(parseFloat(exchange_rate)<=0){
			alert("汇率应大于零！");
			CusObisLoan.exchange_rate._setValue("");
		}
	}

</script>
</head>
<body class="page_content"  >
	
	<emp:form id="submitForm" action="<%=action%>" method="POST">
		<emp:gridLayout id="CusObisLoanGroup" title="他行交易－他行贷款" maxColumn="2">
			<emp:text id="CusObisLoan.seq" label="序号" maxlength="38" readonly="true" hidden="true"/>
			<emp:text id="CusObisLoan.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:select id="CusObisLoan.loan_typ" label="业务品种" required="true" dictname="STD_ZB_OTHERPRO_TYPE" onblur="changeBizType()"/>
			<emp:text id="CusObisLoan.org_name" label="开户机构名称" maxlength="80" required="true" />
			<emp:text id="CusObisLoan.cont_no" label="合同号" maxlength="40" required="false" />
			<emp:text id="CusObisLoan.loan_no" label="借据号" maxlength="40" required="false" />
			<emp:select id="CusObisLoan.cont_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.exchange_rate" label="汇率" required="true" colSpan="2" onblur="getchangeCur();checkRate()" dataType="Double"/>
			<emp:text id="CusObisLoan.cont_amt" label="合同金额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakContAmt()" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.cont_amt_cny" label="合同金额折算成人民币(元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.loan_blc" label="余额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakContAmt()" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.loan_blc_cny" label="余额折算成人民币(元)" maxlength="18" required="true" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.cont_rate" label="月利率" required="true" dataType="Rate4Month" colSpan="2" />
			<emp:text id="CusObisLoan.interest_blc1" label="表内利息余额(元)" maxlength="18" required="false" dataType="Currency" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.interest_blc1_cny" label="表内利息余额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.interest_blc2" label="表外利息余额(元)" maxlength="18" required="false" dataType="Currency" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.interest_blc2_cny" label="表外利息余额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.int_overdue_amt" label="逾期金额(元)" maxlength="18" required="false" dataType="Currency" onchange="getchangeCur()"/>
			<emp:text id="CusObisLoan.int_overdue_amt_cny" label="逾期金额折算成人民币(元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusObisLoan.int_overdue_times" label="逾期期数" maxlength="16" required="false" dataType="Int" />
			<emp:text id="CusObisLoan.gty_perc" label="保证金比例" maxlength="15" required="false" dataType="Percent"/>
			<emp:select id="CusObisLoan.gty_main_typ" label="主要担保方式" required="true" dictname="G_GUIDE_TYPE" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisLoan.dbwlx" label="担保物(人)类型" required="true" dictname="STD_ZB_DBWLX" colSpan="2"/>
			<emp:date id="CusObisLoan.loan_str_dt" label="起始日期" required="false" onblur="checkStrData()"/>
			<emp:date id="CusObisLoan.loan_end_dt" label="到期日期" required="false" onblur="checkStrData()"/>
			<emp:text id="CusObisLoan.extend_tm" label="展期次数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="CusObisLoan.refinance_tm" label="借新还旧次数" maxlength="38" required="false" dataType="Int" />
			<emp:select id="CusObisLoan.loan_form4" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="CusObisLoan.loan_form5" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="CusObisLoan.law_suit_flg" label="诉讼状态" required="false" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="CusObisLoan.valid_flg" label="有效标志" required="true" dictname="STD_ZB_STATUS" defvalue="1"/>
			<emp:textarea id="CusObisLoan.remarks" label="备注" maxlength="200" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 200)"/>
			<emp:select id="CusObisLoan.cus_typ" label="客户类型" required="false" dictname="STD_ZB_INVESTOR2" hidden="true"/>
			<emp:text id="CusObisLoan.cus_bch_id" label="开户机构代码" maxlength="20" required="false" hidden="true" />
			<emp:text id="CusObisLoan.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusObisLoan.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusObisLoan.input_br_id" label="登记机构" required="false" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusObisLoan.last_upd_id" label="更新人" required="false" hidden="true" />
			<emp:date id="CusObisLoan.last_upd_date" label="更新日期" required="false" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>	
			<%if(oper.equals("add")){ %>
			<emp:button id="addCusObisLoan" label="保存" />
			<%} 
				
			  if(oper.equals("update")){%>
			<emp:button id="updateCusObisLoan" label="保存" />
			<%} %>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
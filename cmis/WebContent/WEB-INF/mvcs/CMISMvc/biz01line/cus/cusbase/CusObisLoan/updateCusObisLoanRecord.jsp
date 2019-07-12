<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_field_text_input2 {width: 350px;}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doUpdateCusObisLoan() {
	var result = CusObisLoan._checkAll();
	if(result){
		var form = document.getElementById("submitForm");
		CusObisLoan._toForm(form)
		toSubmitForm(form);
	}else alert("请输入必填项！");
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
function doReturn() {
			var cus_id  =CusObisLoan.cus_id._obj.element.value;
			var paramStr="CusObisLoan.cus_id="+cus_id;
			var url = '<emp:url action="queryCusObisLoanList.do"/>&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location=url;
};
function checkStrData(strDate,endDate){
	var strDt = strDate._obj.element.value;
	var endDt = endDate._obj.element.value;
	var openDay = '${context.OPENDAY}';
	if(strDt!=null && strDt!="" ){
		if(strDt>openDay){
			alert("起始日期不能大于当前日期！");
			strDate._obj.element.value="";
			return;
		}
		if(endDt!=null && endDt!="" ){
			if(strDt>=endDt){
				alert("起始日期要小于 到期日期！");
				endDate._obj.element.value="";
			}
		}
	}
}
function cheakAmt(amt){
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<0){
		  alert("金额值不能为负数！");
		  amt._obj.element.value="";
	   }
}
function cheakContAmt(amt){
    //合同金额
	var contAmt=CusObisLoan.cont_amt._getValue();
	var loanBlc=CusObisLoan.loan_blc._getValue();
	if(contAmt==""||contAmt==null){
		contAmt=0;
	}
	if(loanBlc==""||loanBlc==null){
		loanBlc=0;
	}
	contAmt=parseFloat(contAmt);
	loanBlc=parseFloat(loanBlc);
	if(isNaN(contAmt)){
		alert("合同金额输入有误！");
		CusObisLoan.cont_amt._obj.element.value="";
		return;
	}
	if(contAmt<=0){
		alert("合同金额应大于零！");
		CusObisLoan.cont_amt._obj.element.value="";
		return ;
	}
	if(isNaN(loanBlc)){
		alert("余额输入有误！");
		CusObisLoan.loan_blc._obj.element.value="";
		return;
	}
	if(loanBlc<0){
		alert("金额不能为负数！");
		CusObisLoan.loan_blc._obj.element.value="";
		return ;
	}
	if(contAmt<loanBlc){
        alert("[合同金额]不能小于[余额] ");
        amt._obj.element.value="";
        return ;
    }
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusObisLoanRecord.do" method="POST">
		<emp:gridLayout id="CusObisLoanGroup" maxColumn="2" title="他行交易－他行贷款">
			<emp:text id="CusObisLoan.seq" label="序号" maxlength="38" required="true"  readonly="true" hidden="true"/>
			<emp:text id="CusObisLoan.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusObisLoan.loan_typ" label="业务品种" required="true" dictname="STD_ZB_OTHERPRO_TYPE"/>
			<emp:text id="CusObisLoan.org_name" label="开户机构名称" maxlength="80" required="true" />
			<emp:text id="CusObisLoan.cont_no" label="合同号" maxlength="40" required="false" />
			<emp:text id="CusObisLoan.loan_no" label="借据号" maxlength="40" required="false" />
			<emp:select id="CusObisLoan.cont_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="CusObisLoan.cont_amt" label="合同金额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakContAmt(CusObisLoan.cont_amt)"/>
			<emp:text id="CusObisLoan.loan_blc" label="余额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakContAmt(CusObisLoan.loan_blc)"/>
			<emp:text id="CusObisLoan.cont_rate" label="月利率" required="true" dataType="Rate4Month" />
			<emp:text id="CusObisLoan.interest_blc1" label="表内利息余额(元)" maxlength="18" required="false" dataType="Currency" onblur="cheakAmt(CusObisLoan.interest_blc1)"/>
			<emp:text id="CusObisLoan.interest_blc2" label="表外利息余额(元)" maxlength="18" required="false" dataType="Currency" onblur="cheakAmt(CusObisLoan.interest_blc2)"/>
			<emp:text id="CusObisLoan.gty_perc" label="保证金比例" maxlength="10" required="false" dataType="Rate" />
			<emp:select id="CusObisLoan.gty_main_typ" label="主要担保方式" required="true" dictname="G_GUIDE_TYPE" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisLoan.dbwlx" label="担保物(人)类型" required="true" dictname="STD_ZB_DBWLX" colSpan="2"/>
			<emp:date id="CusObisLoan.loan_str_dt" label="起始日期" required="false" onblur="checkStrData(CusObisLoan.loan_str_dt,CusObisLoan.loan_end_dt)"/>
			<emp:date id="CusObisLoan.loan_end_dt" label="到期日期" required="false" onblur="checkStrData(CusObisLoan.loan_str_dt,CusObisLoan.loan_end_dt)"/>
			<emp:text id="CusObisLoan.extend_tm" label="展期次数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="CusObisLoan.refinance_tm" label="借新还旧次数" maxlength="38" required="false" dataType="Int" />
			<emp:select id="CusObisLoan.loan_form4" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="CusObisLoan.loan_form5" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="CusObisLoan.law_suit_flg" label="诉讼状态" required="false" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="CusObisLoan.valid_flg" label="有效标志" required="true" dictname="STD_ZB_STATUS" />
			<emp:textarea id="CusObisLoan.remarks" label="备注" maxlength="200" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 200)"/>
			<emp:text id="CusObisLoan.cus_bch_id" label="开户机构代码" maxlength="20" required="false" hidden="true" />
			<emp:select id="CusObisLoan.cus_typ" label="客户类型" required="false" dictname="STD_ZB_INVESTOR2" hidden="true"/>
			<emp:text id="CusObisLoan.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusObisLoan.input_date" label="登记日期" required="false"  readonly="true" hidden="true"/>
			<emp:text id="CusObisLoan.input_br_id" label="登记机构" required="false"  readonly="true" hidden="true"/>
			<emp:text id="CusObisLoan.last_upd_id" label="更新人" required="false" hidden="true" />
			<emp:date id="CusObisLoan.last_upd_date" label="更新日期" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateCusObisLoan" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

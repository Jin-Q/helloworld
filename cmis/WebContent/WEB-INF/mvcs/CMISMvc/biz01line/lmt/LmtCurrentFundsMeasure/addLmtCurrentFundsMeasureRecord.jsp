<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.EMPConstance" %>

<%@page import="com.ecc.emp.core.Context"%><emp:page>
<html>
<head>
<title>修改页面</title>
<% 	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String is_exist = (String)context.getDataValue("is_exist");
%>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
	background-color:#eee;
}

</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doNextstep(){
		var form = document.getElementById("submitForm");
	    var result1 = LmtCurrentFundsMeasure.current_funds_loan._checkAll();
	    var result2 = LmtCurrentFundsMeasure.funds_other_channels._checkAll();
	    var result3 = LmtCurrentFundsMeasure.pre_income_rise_rate._checkAll();
	    var result4 = LmtCurrentFundsMeasure.operation_funds._checkAll();
	    LmtCurrentFundsMeasure._toForm(form);
	  	if(!result1||!result2||!result3||!result4) return false;
	  	asyncFormSubmit("calculate");
	};

	function doGetoper(){
		var form = document.getElementById("submitForm");
	    var result = LmtCurrentFundsMeasure.pre_income_rise_rate._checkAll();
	    LmtCurrentFundsMeasure._toForm(form);
	  	if(!result){
		  	return false;
	  	}
	  	asyncFormSubmit("operation");
	};

	function asyncFormSubmit(flag){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					//alert(" function(o) ");
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
					if(flag != null && flag == "calculate"){
						LmtCurrentFundsMeasure.current_funds_amt._setValue(jsonstr.currentFundsAmt);
						doGetLmtAmt()
					//	alert("测算成功！");
					}else if(flag != null && flag == "operation"){           
						LmtCurrentFundsMeasure.operation_funds._setValue(jsonstr.currentFundsAmt);
						doNextstep();
					//	alert("测算成功！");
						return true;
					}else if(flag != null && flag == "saveitem"){
						alert("测算成功！");
					}
				}else{
					alert(jsonstr.errMsg);
					return false;
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
		var form = document.getElementById("submitForm");
	 	if(flag != null){
			form.action="calculateCurrentFundsAmt.do?LmtCurrentFundsMeasure.oper="+flag;
	 	}
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	function doSelect(){
		var form = document.getElementById("submitForm");
		
	    var result = LmtCurrentFundsMeasure._checkAll();
	    LmtCurrentFundsMeasure._toForm(form);
	  	if(!result) return false;
	  	asyncFormSubmit("saveitem");
		
		/*var data = LmtCurrentFundsMeasure.current_funds_amt._getValue();
		if (data != null && data != "") {
			//alert("${context.returnMethod}");
			window.opener["${context.returnMethod}"](data);
			window.close();

		} else {
			alert('流动资金贷款额度为空,请先点击测算按钮，再确定！');
		}*/
	};

	function loadPage(){
		if("<%=is_exist%>"=="0"){
			alert('该客户没有最近两年的年报！');
		}
		//{
			//alert("该客户没有去年12月份的报表，请手工填数！");
			//LmtCurrentFundsMeasure.last_year_income._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.last_year_profit_rate._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.operation_turnover_count._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.pre_receive_turnover_time._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.goods_turnover_time._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.need_receive_turnover_time._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.pre_pay_turnover_time._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.borrower_monetary_fund._obj._renderReadonly(false);
			//LmtCurrentFundsMeasure.need_pay_turnover_time._obj._renderReadonly(false);
			
		//}
	/*	if(LmtCurrentFundsMeasure.last_year_profit_rate._getValue()=="")
		{
			LmtCurrentFundsMeasure.last_year_profit_rate._setValue("0.00");
		}
		LmtCurrentFundsMeasure.margin_deposits._setValue("0.00");
		LmtCurrentFundsMeasure.own_accp_risk_amt._setValue("0.00");
		LmtCurrentFundsMeasure.other_accp_risk_amt._setValue("0.00");
		*/
		var cllType = LmtCurrentFundsMeasure.com_cll_type._getValue();
		if(cllType!=null&&cllType!=''){
			if(cllType.substr(0,1)=='F'){//批发零售业
			//	rate = 0.7;
				LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('0.7');
				LmtCurrentFundsMeasure.cll_asset_debt_rate._obj._renderReadonly(true);
			}else if(cllType.substr(0,3)=='H61'){//住宿业
			//	rate = 0.68;
				LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('0.68');
				LmtCurrentFundsMeasure.cll_asset_debt_rate._obj._renderReadonly(true);
			}else if(cllType.substr(0,3)=='H62'){//餐饮业
			//	rate = 0.65;
				LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('0.65');
				LmtCurrentFundsMeasure.cll_asset_debt_rate._obj._renderReadonly(true);
			}else if(cllType.substr(0,1)=='E'){//建筑业
			//	rate = 0.66;
				LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('0.66');
				LmtCurrentFundsMeasure.cll_asset_debt_rate._obj._renderReadonly(true);
			}else if(cllType.substr(0,1)=='K'){//房地产
			//	rate = 0.71;
				LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('0.71');
				LmtCurrentFundsMeasure.cll_asset_debt_rate._obj._renderReadonly(true);
			}
		}
	}

	function doGetLmtAmt(){
		var result = LmtCurrentFundsMeasure.liab_fund._checkAll();
	    var result1 = LmtCurrentFundsMeasure.com_cll_type_displayname._checkAll();
	    var result2 = LmtCurrentFundsMeasure.cll_asset_debt_rate._checkAll();
		if(!result||!result1||!result2){
			return false;
		}
		var liab = LmtCurrentFundsMeasure.liab_fund._getValue();
		var cllType = LmtCurrentFundsMeasure.com_cll_type._getValue();
		var realLiab = LmtCurrentFundsMeasure.real_liab_fund._getValue();
		var rate =LmtCurrentFundsMeasure.cll_asset_debt_rate._getValue();
		if(parseFloat(rate)<0.6||parseFloat(rate)>0.71){//校验资产负债率范围
			alert('行业资产负债率介于[60%~71%]之间！');
			LmtCurrentFundsMeasure.cll_asset_debt_rate._setValue('');
			return;
		}
		
		var lmtAmt = (parseFloat(liab)*parseFloat(rate) - parseFloat(realLiab)).toFixed(2);
		if(lmtAmt<0){
			lmtAmt = 0;
		}
		LmtCurrentFundsMeasure.lmt_amt._setValue(lmtAmt+'');

		var form = document.getElementById("submitForm");//
	    var result1 = LmtCurrentFundsMeasure.lmt_amt._checkAll();
	    var result2 = LmtCurrentFundsMeasure.com_cll_type_displayname._checkAll();
	    var result3 = LmtCurrentFundsMeasure.cll_asset_debt_rate._checkAll();
	    LmtCurrentFundsMeasure._toForm(form);
	  	if(!result1||!result2||!result3) return false;
	  	
	  	asyncFormSubmit("saveitem");
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="loadPage();">
	
	<emp:form id="submitForm" action="updateLmtCurrentFundsMeasureRecord.do" method="POST">
		<emp:gridLayout id="LmtCurrentFundsMeasureGroup" maxColumn="2" title="流动资金额度测算">
		<emp:text id="LmtCurrentFundsMeasure.serno" label="业务流水号" hidden="true" />
		<emp:text id="LmtCurrentFundsMeasure.cus_id" label="客户码" hidden="true"/>
		<emp:text id="LmtCurrentFundsMeasure.item_id" label="授信台账编号" hidden="true" />
		
		<emp:text id="LmtCurrentFundsMeasure.last_year_income" label="上年度销售收入" maxlength="18" dataType="Currency" required="true" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtCurrentFundsMeasure.last_year_profit_rate" label="上年度销售利润率(%)" dataType="Percent" required="true" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtCurrentFundsMeasure.pre_income_rise_rate" label="预计销售收入年增长率(%)" dataType="Percent" required="true" readonly="false" defvalue="0"/>
		
		<emp:text id="LmtCurrentFundsMeasure.operation_turnover_count" label="营运资金周转次数" required="true" readonly="true" defvalue="0"/>
		<emp:text id="LmtCurrentFundsMeasure.pre_receive_turnover_time" label="预收账款周转天数" required="true" readonly="true" defvalue="0"/>
		<emp:text id="LmtCurrentFundsMeasure.goods_turnover_time" label="存货周转天数" required="true" readonly="true" defvalue="0"/>
		<emp:text id="LmtCurrentFundsMeasure.need_receive_turnover_time" label="应收账款周转天数" required="true" readonly="true" defvalue="0"/>
		<emp:text id="LmtCurrentFundsMeasure.need_pay_turnover_time" label="应付账款周转天数" required="true" readonly="true" defvalue="0"/>
		<emp:text id="LmtCurrentFundsMeasure.pre_pay_turnover_time" label="预付账款周转天数" required="true" readonly="true" defvalue="0"/>
		
		<emp:text id="LmtCurrentFundsMeasure.borrower_monetary_fund" label="借款人自有资金" maxlength="18" dataType="Currency" required="true" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtCurrentFundsMeasure.current_funds_loan" label="现有流动资金贷款" maxlength="18" dataType="Currency" required="true" />
		<emp:text id="LmtCurrentFundsMeasure.funds_other_channels" label="其他渠道提供的营运资金" colSpan="2" maxlength="18" dataType="Currency" required="true"/>
		<emp:text id="LmtCurrentFundsMeasure.operation_funds" label="营运资金量" maxlength="18" dataType="Currency" required="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtCurrentFundsMeasure.current_funds_amt" label="流动资金贷款额度" maxlength="18" dataType="Currency" readonly="true" required="true" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtCurrentFundsMeasureGroup1" maxColumn="2" title="授信限额测算">
		<emp:text id="LmtCurrentFundsMeasure.liab_fund" label="可负债额资产" required="true" readonly="false" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.real_liab_fund" label="实有负债额" required="true" readonly="true" defvalue="0" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.com_cll_type" label="所属行业" required="true" readonly="true" hidden="true"/>
		<emp:text id="LmtCurrentFundsMeasure.com_cll_type_displayname" label="所属行业" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2" />
		<emp:text id="LmtCurrentFundsMeasure.cll_asset_debt_rate" label="行业资产负债率(%)" dataType="Percent" required="true" />
		<emp:text id="LmtCurrentFundsMeasure.owner_int" label="所有者权益" required="true" readonly="true" defvalue="0" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.invisible_fund" label="无形资产" required="true" readonly="true" defvalue="0" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.defer_fund" label="递延资产" required="true" readonly="true" defvalue="0" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.visible_fund" label="有形净资产" required="true" readonly="true" defvalue="0" maxlength="18" dataType="Currency"/>
		<emp:text id="LmtCurrentFundsMeasure.lmt_amt" label="授信额度" maxlength="18" dataType="Currency" required="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
<!--			<emp:actButton id="getoper" label="测算营运资金量" op="update" mouseoutCss="button100" mousedownCss="button100" mouseoverCss="button100" mouseupCss="button100"/>-->
<!--			<emp:actButton id="nextstep" label="测算额度" op="update" />-->
			<emp:actButton id="getoper" label="测算授信额度" op="update" mouseoutCss="button100" mousedownCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

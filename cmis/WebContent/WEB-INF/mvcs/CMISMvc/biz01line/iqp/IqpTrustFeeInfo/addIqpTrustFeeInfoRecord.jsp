<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doOnLoad(){
		var optionJosn = "0,1,2";
		var options4ACT =IqpTrustFeeInfo.am_charge_term._obj.element.options;
		var options4LCT =IqpTrustFeeInfo.lm_charge_term._obj.element.options;
		/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin **/
		var options4MCT =IqpTrustFeeInfo.mm_charge_term._obj.element.options;
		for ( var i = options4ACT.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options4ACT[i].value)<0){
				options4ACT.remove(i);
			}
		} 
		for ( var i = options4LCT.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options4LCT[i].value)<0){
				options4LCT.remove(i);
			}
		}
		for ( var i = options4MCT.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options4MCT[i].value)<0){
				options4MCT.remove(i);
			}
		}
		/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end **/
	};

	function doSave(){
		var form = document.getElementById("submitForm");
		if(IqpTrustFeeInfo._checkAll()){
			IqpTrustFeeInfo._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var msg = jsonstr.msg;
					var serno = jsonstr.sernoStr;
					if(msg == "success"){
						alert("保存成功!");
						url = '<emp:url action="getIqpTrustFeeInfoAddPage.do"/>?serno='+serno;
						url = EMPTools.encodeURI(url);  
						window.location = url;
					}else {
						alert("保存失败!");
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
			var url = '<emp:url action="addIqpTrustFeeInfoRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}else {
			return;
		}
	};
	
	/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
	function doCalBaileePayAmt(){
		var serno = IqpTrustFeeInfo.serno._getValue();
		var mm_fee_rate = IqpTrustFeeInfo.mm_fee_rate._getValue();
		var mm_charge_term = IqpTrustFeeInfo.mm_charge_term._getValue();
		var bailee_pay_ratio = IqpTrustFeeInfo.bailee_pay_ratio._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
					var baileePayAmt = jsonstr.baileePayAmt;
					var msg = jsonstr.msg;
					if(msg ="success"){
						IqpTrustFeeInfo.bailee_pay_amt._setValue(baileePayAmt);
						doSave();
					}else{
						alert("自动计算失败！");
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
		var url = '<emp:url action="calBaileePayAmt.do"/>?serno='+serno+"&mm_fee_rate="+mm_fee_rate+"&mm_charge_term="+mm_charge_term+"&bailee_pay_ratio="+bailee_pay_ratio;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);	
	};
	/** add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
</script>
</head>
<body class="page_content" onload="doOnLoad()">	
	<emp:form id="submitForm" action="#" method="POST">	
		<emp:gridLayout id="IqpTrustFeeInfoGroup" title="信托贷款费用信息" maxColumn="2">
			<emp:text id="IqpTrustFeeInfo.serno" label="业务流水号" maxlength="40" required="true" defvalue="${context.sernoStr}" readonly="true" colSpan="2"/>
			<emp:text id="IqpTrustFeeInfo.am_fee_rate" label="安排撮合费率（年）" maxlength="16" required="true" dataType="Rate"/>
			<emp:select id="IqpTrustFeeInfo.am_charge_term" label="安排撮合费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" />
			<emp:text id="IqpTrustFeeInfo.lm_fee_rate" label="贷款管理费率（年）" maxlength="16" required="true" dataType="Rate"/>
			<emp:select id="IqpTrustFeeInfo.lm_charge_term" label="贷款管理费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
			<emp:text id="IqpTrustFeeInfo.mm_fee_rate" label="委托管理费率（年）" maxlength="16" required="true" dataType="Rate"/>
			<emp:select id="IqpTrustFeeInfo.mm_charge_term" label="委托管理费计费周期" dictname="STD_IQP_RATE_CYCLE" required="true" />
			<emp:text id="IqpTrustFeeInfo.bailee_pay_ratio" label="信托计划受托人支付比例" maxlength="16" required="true" dataType="Rate"/>
			<emp:text id="IqpTrustFeeInfo.bailee_pay_amt" label="信托计划受托人支付金额" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
		</emp:gridLayout>		
		<div align="center">
			<br>
			<emp:button id="calBaileePayAmt" label="保存"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
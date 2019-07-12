<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
	<html>
	<head>
	<title>修改页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<%
		String serno = request.getParameter("serno");
	%>
	<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtSubpayList._checkAll();
		if(result){
			LmtSubpayList._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	}
	
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
					if(flag=="success"){
						alert("修改成功！");
						window.close();
						window.opener.location.reload();
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	function doReturn(){
		window.close();
	}

	/*** 各种金额校验begin ***/
	function checkAmt(obj){
		var notice;
		if(obj.name == 'LmtSubpayList.subpay_cap'){
			big_amt = LmtSubpayList.bill_bal._getValue();
			small_amt = LmtSubpayList.subpay_cap._getValue();
			notice = "代偿本金不能大于借据余额！";
		}
		if(obj.name == 'LmtSubpayList.subpay_int'){
			big_amt = LmtSubpayList.int_cumu._getValue();
			small_amt = LmtSubpayList.subpay_int._getValue();
			notice = "代偿利息不能大于欠息累计！";
		}

		big_amt = parseFloat(big_amt);
		small_amt = parseFloat(small_amt);
		if(small_amt > big_amt){
			alert(notice);
			obj.value = '0.00';
		}
	};
	/*** 各种金额校验end ***/
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content">
	<emp:form id="submitForm" action="updateLmtSubpayListRecord.do"
		method="POST">
		<emp:gridLayout id="LmtSubpayListGroup" maxColumn="2" title="代偿业务明细">
			<emp:text id="LmtSubpayList.subpay_bill_no" label="代偿业务借据号" required="true" readonly="true" />
			<emp:text id="LmtSubpayList.cont_no" label="合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:select id="LmtSubpayList.guar_mode" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true" hidden="true"/> 
			<emp:text id="LmtSubpayList.prd_id_displayname" label="业务品种"  required="true" readonly="true" />
			<emp:text id="LmtSubpayList.bill_amt" label="借据金额" maxlength="18" required="false" dataType="Currency" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtSubpayList.bill_bal" label="借据余额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtSubpayList.int_cumu" label="欠息累计" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtSubpayList.subpay_cap" label="代偿本金" maxlength="18" required="false" dataType="Currency" onchange="checkAmt(this)"/>
			<emp:text id="LmtSubpayList.subpay_int" label="代偿利息" maxlength="18" required="false" dataType="Currency" onchange="checkAmt(this)"/>
			<emp:text id="LmtSubpayList.serno" label="业务编号" maxlength="40" required="true" hidden="true" />
			<emp:text id="LmtSubpayList.guar_cus_id" label="担保公司客户码" maxlength="30" required="true" hidden="true" />
			<emp:select id="LmtSubpayList.subpay_status" label="代偿状态" required="false" dictname="STD_ZB_SUBPAY" hidden="true" />
			<emp:text id="LmtSubpayList.pk" label="主键" hidden="true" />
		</emp:gridLayout>
		<div align="center"><br>
			<emp:button id="updatee" label="修改" /> 
			<emp:button id="return" label="关闭" />
		</div>
	</emp:form>
	</body>
	</html>
</emp:page>

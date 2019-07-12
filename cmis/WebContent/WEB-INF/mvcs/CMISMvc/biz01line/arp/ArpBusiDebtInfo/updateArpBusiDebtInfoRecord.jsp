<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	//金额校验
	function doCheck(data1,data2,type){
		var msg="";
		//贷款余额
		var num1 = data1._getValue();
		//抵偿本金
		var num2 = data2._getValue();
		//抵偿本金
		if(type=="1"){
			msg="抵偿本金不得大于贷款余额";
			
		}
		//抵偿表内利息
		if(type=="2"){
			msg="抵偿表内利息不得大于表内利息";
			
		}
		//抵偿表外利息
		if(type=="3"){
			msg="抵偿表外利息不得大于表外利息";
		}
		if(parseFloat(num1)<parseFloat(num2)){
	       alert(msg); 
	       if(type=="1"){
	    	   ArpBusiDebtInfo.debt_cap._setValue();
	       }else if(type=="2"){
	    	   ArpBusiDebtInfo.debt_inner_int._setValue();
	       }else if(type=="3"){
	     	   ArpBusiDebtInfo.debt_out_int._setValue();
	       }
		}
	};
	
	function doNext(){
		if(!ArpBusiDebtInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		ArpBusiDebtInfo._toForm(form);
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
					alert("修改成功!"); 
					window.location.reload();
				}else {
					alert("修改失败!"); 
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	function doReturn() {
		menuIds = '&subMenuId=Coll_Debt&op=${context.op}';
		var serno = ArpBusiDebtInfo.serno._getValue();
		var url = '<emp:url action="queryArpBusiDebtInfoList.do"/>?serno='+serno+menuIds;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};	

	function doLoad(){
		addContForm(ArpBusiDebtInfo);
		addBillForm(ArpBusiDebtInfo);
	};
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateArpBusiDebtInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpBusiDebtInfoGroup" maxColumn="2" title="业务抵债信息表">
			<emp:text id="ArpBusiDebtInfo.bill_no" label="借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpBusiDebtInfo.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.loan_amt" label="贷款金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.loan_balance" label="贷款余额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.debt_cap" label="抵偿本金" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.loan_balance,ArpBusiDebtInfo.debt_cap,1)" readonly="false"/>
			<emp:text id="ArpBusiDebtInfo.debt_inner_int" label="抵偿表内利息" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.inner_owe_int,ArpBusiDebtInfo.debt_inner_int,2)" readonly="false"/>
			<emp:text id="ArpBusiDebtInfo.debt_out_int" label="抵偿表外利息" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.out_owe_int,ArpBusiDebtInfo.debt_out_int,3)" readonly="false"/>
			<emp:text id="ArpBusiDebtInfo.debt_other_expense" label="抵偿其他发生费用" maxlength="16" required="true" dataType="Currency" readonly="false"/>
			<emp:text id="ArpBusiDebtInfo.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

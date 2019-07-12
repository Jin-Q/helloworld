<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String serno = request.getParameter("serno");
    String cus_id = request.getParameter("cus_id");
    if(context.containsKey("cus_id")){
    	context.setDataValue("cus_id",cus_id);
    }else{
    	context.addDataField("cus_id",cus_id);
    }
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">

function doLoad(){
	var serno = '<%=serno%>';
	ArpBusiDebtInfo.serno._setValue(serno);
}
function doReturn() {
	menuIds = '&subMenuId=Coll_Debt&op=${context.op}';
	var serno = '<%=serno%>';
	var url = '<emp:url action="queryArpBusiDebtInfoList.do"/>?serno='+serno+menuIds;
	url = EMPTools.encodeURI(url);
	window.location=url;
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
				alert("新增成功!"); 
				var serno = '<%=serno%>';
				var url = '<emp:url action="queryArpBusiDebtInfoList.do"/>?serno='+serno;
				url = EMPTools.encodeURI(url);
				window.location=url;
			}else {
				alert("新增失败!"); 
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
}

	/*** 选择借据并校验begin ***/
	function selCusId(data){
		bill_no = data.bill_no._getValue();
		cont_no=data.cont_no._getValue();
		loan_amt=data.loan_amt._getValue();
		loan_balance=data.loan_balance._getValue();
		inner_owe_int=data.inner_owe_int._getValue();
		out_owe_int=data.out_owe_int._getValue();

		var url="<emp:url action='checkAssetPreserve.do'/>&type=BusiDebtDetail&value="+bill_no;
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpBusiDebtInfo.bill_no._setValue(bill_no);
			ArpBusiDebtInfo.cont_no._setValue(cont_no);
			ArpBusiDebtInfo.loan_amt._setValue(loan_amt);
			ArpBusiDebtInfo.loan_balance._setValue(loan_balance);
			ArpBusiDebtInfo.inner_owe_int._setValue(inner_owe_int);
			ArpBusiDebtInfo.out_owe_int._setValue(out_owe_int);
			doReadOnly();
		}else{
			alert("此借据已发起过以物抵债申请!");
		}
	};
	/*** 选择借据并校验end ***/
	
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
}
function doReadOnly(){
	ArpBusiDebtInfo.debt_cap._obj._renderReadonly(false);
	ArpBusiDebtInfo.debt_inner_int._obj._renderReadonly(false);
	ArpBusiDebtInfo.debt_out_int._obj._renderReadonly(false);
	ArpBusiDebtInfo.debt_other_expense._obj._renderReadonly(false);
}
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addArpBusiDebtInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpBusiDebtInfoGroup" title="业务抵债信息表" maxColumn="2">
			<emp:pop id="ArpBusiDebtInfo.bill_no" label="借据编号" required="true" 
			url="queryBillNoPop.do?condition= and cus_id = '${context.cus_id}' and acc_status = '1' &moduleId=arp&returnMethod=selCusId&flag=1" />
			<emp:text id="ArpBusiDebtInfo.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.loan_amt" label="贷款金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.loan_balance" label="贷款余额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="ArpBusiDebtInfo.debt_cap" label="抵偿本金" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.loan_balance,ArpBusiDebtInfo.debt_cap,1)" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.debt_inner_int" label="抵偿表内利息" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.inner_owe_int,ArpBusiDebtInfo.debt_inner_int,2)" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.debt_out_int" label="抵偿表外利息" maxlength="16" required="true" dataType="Currency" onchange="doCheck(ArpBusiDebtInfo.out_owe_int,ArpBusiDebtInfo.debt_out_int,3)" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.debt_other_expense" label="抵偿其他发生费用" maxlength="16" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="ArpBusiDebtInfo.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


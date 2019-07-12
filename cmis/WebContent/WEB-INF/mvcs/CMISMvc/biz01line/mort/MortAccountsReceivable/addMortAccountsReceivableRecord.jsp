<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 550px;
}
</style>
<script type="text/javascript">
//保存按钮事件
function doNext(){
	if(!MortAccountsReceivable._checkAll()){
		alert("请输入必填项！");
	}else{
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert("保存成功!");
					var guaranty_no = MortAccountsReceivable.guaranty_no._getValue();
					var url = '<emp:url action="queryMortAccountsReceivableList.do"/>?guaranty_no='+guaranty_no;
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else{
					alert("保存失败!");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById('submitForm');
		MortAccountsReceivable._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);	
		var url = '<emp:url action="addMortAccountsReceivableRecord.do"/>';
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
	}
}

function doReturn(){
	var guaranty_no = MortAccountsReceivable.guaranty_no._getValue();
	var url = '<emp:url action="queryMortAccountsReceivableList.do"/>?guaranty_no='+guaranty_no;
	url = EMPTools.encodeURI(url);
	window.location=url;
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortAccountsReceivableRecord.do" method="POST">
		
		<emp:gridLayout id="MortAccountsReceivableGroup" title="应收账款" maxColumn="2">
			<emp:text id="MortAccountsReceivable.debt_id" label="应收账款编号" maxlength="60" required="true" hidden="true"/>
			<emp:text id="MortAccountsReceivable.guaranty_no" label="押品编号" maxlength="60" required="true" hidden="true"/>
			<emp:text id="MortAccountsReceivable.guaranty_name" label="押品名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortAccountsReceivable.buy_cus_name" label="买方客户名称" maxlength="40" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="MortAccountsReceivable.sel_cus_name" label="卖方客户名称" maxlength="40" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:select id="MortAccountsReceivable.bond_mode" label="债权类型" required="true" dictname="STD_ACTRECPO_BOND_TYPE" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="MortAccountsReceivable.bond_amt" label="债权金额" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="MortAccountsReceivable.invc_no" label="发票号" maxlength="40" required="true" />
			<emp:select id="MortAccountsReceivable.invc_ccy" label="发票币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortAccountsReceivable.invc_amt" label="发票金额" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="MortAccountsReceivable.cont_no" label="商业合同编号" maxlength="40" required="true" colSpan="2"/>
			
			<emp:date id="MortAccountsReceivable.invc_date" label="开票日期" required="true" />
			<emp:date id="MortAccountsReceivable.bond_pay_date" label="付款日期" required="true" />
			<emp:text id="MortAccountsReceivable.status" label="状态" maxlength="5" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="MortAccountsReceivableGroup" title="登记信息" maxColumn="2">
			<emp:text id="MortAccountsReceivable.input_id_displayname" label="登记人" required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="MortAccountsReceivable.input_br_id_displayname" label="登记机构" required="false" defvalue="$organName" readonly="true"/>
			<emp:date id="MortAccountsReceivable.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		    <emp:text id="MortAccountsReceivable.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="MortAccountsReceivable.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="确定" op="add"/>
			<emp:button id="return" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


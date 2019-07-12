<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String op= "";
if(context.containsKey("op")){
	op = (String)context.getDataValue("op");
}
if("view".equals(op)||"to_storage".equals(op)){
	request.setAttribute("canwrite","");
}
String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			function doAdd(){
		var form = document.getElementById('submitForm');
		MortDepositReceipt._toForm(form);
		if(!MortDepositReceipt._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var guaranty_no = '${context.guaranty_no}';
						var collateral_type_cd = '${context.collateral_type_cd}';
						var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no+'&collateral_type_cd='+collateral_type_cd;
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
				document.getElementById("button_add").disabled="";
				document.getElementById("button_reset").disabled="";
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	function doReset(){
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		location.href(url);
		page.dataGroups.MortDepositReceiptGroup.reset();
	};		
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortDepositReceipt.deposit_date._obj.element.value!=''){
			var e = MortDepositReceipt.deposit_mature_date._obj.element.value;
			var s = MortDepositReceipt.deposit_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('存入日期必须小于或等于当前日期！');
        		MortDepositReceipt.deposit_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('存入日期必须小于或等于到期日期！');
            		MortDepositReceipt.deposit_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortDepositReceipt.deposit_mature_date._obj.element.value!=''){
			var e = MortDepositReceipt.deposit_mature_date._obj.element.value;
			var s = MortDepositReceipt.deposit_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于存入日期！');
            		MortDepositReceipt.deposit_mature_date._obj.element.value="";
            		return;
            	}
			}
		}
	}				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortDepositReceiptRecord.do" method="POST">
		
		<emp:gridLayout id="MortDepositReceiptGroup" title="存单明细" maxColumn="2">
			<emp:text id="MortDepositReceipt.deposit_id" label="存单质押编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortDepositReceipt.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortDepositReceipt.receipt_type" label="存单类型" required="true" dictname="STD_DEPOSIT_TYPE" />
			<emp:text id="MortDepositReceipt.receipt_no" label="存单号码" maxlength="100" required="true"/>
			<emp:text id="MortDepositReceipt.account_bank_name" label="开户行名" maxlength="200" required="true" />
			<emp:text id="MortDepositReceipt.account_no" label="账号" maxlength="200" required="true" dataType="Acct"/>
			<emp:text id="MortDepositReceipt.account_name" label="账户账名" maxlength="40" required="true" />
			<emp:select id="MortDepositReceipt.is_bank_receipt" label="是否为我行存单" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="MortDepositReceipt.currency_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortDepositReceipt.receipt_amt" label="存单金额（万元）" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortDepositReceipt.riling_y" label="年利率（%）" maxlength="16" required="true" dataType="Rate" colSpan="2"/>
			<emp:text id="MortDepositReceipt.book_no" label="册号" maxlength="200" required="false" />
			<emp:text id="MortDepositReceipt.list_no" label="笔号" maxlength="200" required="false" />
			<emp:date id="MortDepositReceipt.deposit_date" label="存入日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortDepositReceipt.deposit_mature_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:textarea id="MortDepositReceipt.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortDepositReceipt.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="add" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<% } %>			
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


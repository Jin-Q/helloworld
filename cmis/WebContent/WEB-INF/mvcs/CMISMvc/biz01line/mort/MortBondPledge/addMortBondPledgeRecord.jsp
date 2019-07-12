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
		MortBondPledge._toForm(form);
		if(!MortBondPledge._checkAll()){
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
		page.dataGroups.MortBondPledgeGroup.reset();
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortBondPledgeRecord.do" method="POST">
		
		<emp:gridLayout id="MortBondPledgeGroup" title="债券明细" maxColumn="2">
			<emp:text id="MortBondPledge.bond_id" label="债券抵押编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortBondPledge.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortBondPledge.bond_code" label="债券代码" maxlength="100" required="true" />
			<emp:date id="MortBondPledge.debt_buy_date" label="债券购买日期" required="true" />
			<emp:text id="MortBondPledge.debt_account_name" label="债券资金账户户名" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.debt_account_bank_no" label="债券资金账户开户行" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.debt_account_no" label="债券资金账户账号" maxlength="40" required="false" />
			<emp:text id="MortBondPledge.debt_deposit_account_name" label="债券托管账户户名" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.debt_deposit_account_bank_no" label="债券托管账户开户行" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.debt_deposit_account_no" label="债券托管账户账号" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.receipt_proof_no" label="收款凭证编码" maxlength="40" required="false" />
			<emp:text id="MortBondPledge.settle_accounts_name" label="结算账户开户名" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.settle_accounts_bank" label="结算账户开户行" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.settle_accounts_no" label="结算账户账号" maxlength="40" required="false" />
			<emp:text id="MortBondPledge.book_value" label="账面价值（元）" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortBondPledge.bond_value" label="票面价值（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="MortBondPledge.bond_value_currency_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:date id="MortBondPledge.issu_date" label="债券发行日期" required="true" />
			<emp:date id="MortBondPledge.bond_maturity" label="到期日期" required="true" />
			<emp:text id="MortBondPledge.issue_org_name" label="发行机构名称" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.issue_org_level" label="发行机构等级" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.register_org_name" label="登记机构" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.agency_org_name" label="代理机构" maxlength="100" required="false" />
			<emp:text id="MortBondPledge.bond_term" label="债券期限" maxlength="6" required="false" dataType="Double" hidden="true"/>
			<emp:select id="MortBondPledge.bond_interest_pay_type_cd" label="债券利息支付类型" required="false" dictname="STD_BOND_PINT_TYPE" />
			<emp:select id="MortBondPledge.bond_interest_pay_cd" label="付息方式" required="false" dictname="STD_PINT_TYPE" />
			<emp:select id="MortBondPledge.ind_sign_bond" label="是否记名债券" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortBondPledge.bond_annual_rate" label="票面利率" maxlength="15" required="true" dataType="Rate" />
			<emp:textarea id="MortBondPledge.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortBondPledge.sys_update_time" label="系统更新日期" required="false" hidden="true"/>
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


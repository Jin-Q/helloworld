<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_text2{
border:1px solid #b7b7b7;
width:100px;
background-color:#eee;
}
.emp_text{
border:1px solid #b7b7b7;
width:150px;
background-color:#eee;
}
.emp_text3{
border:1px solid #b7b7b7;
width:100px;
}  
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
<%
	request.setAttribute("canwrite","");
%>
	function doReturn(){
		var url = '<emp:url action="queryMortBailDelivList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
		
	}
	function doTally(){
		if(!MortBailDeliv._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("记账失败！");
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("已成功记账！");
						var url = '<emp:url action="queryMortBailDelivList.do"/>?menuId=bzjth';
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("记账失败！");
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("记账失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			MortBailDeliv._toForm(form);
			var url = '<emp:url action="tallyMortBailDelivRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	    }
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="" method="POST">
		<emp:gridLayout id="MortBailDelivGroup" title="保证金提货" maxColumn="2">
			<emp:text id="MortBailDeliv.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortBailDeliv.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortBailDeliv.cus_id" label="出质人客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.cus_id_displayname" label="出质人客户名称" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="MortBailDeliv.repay_receipt_type" label="还款凭证类型" required="true" dictname="STD_REPAY_LIST_TYPE" />
			<emp:text id="MortBailDeliv.receipt_serno" label="凭证流水号" maxlength="40" required="false" />
			<emp:text id="MortBailDeliv.repay_amt" label="还款金额" maxlength="20" required="true" dataType="Double" onchange="doChange()"/>
			<emp:text id="MortBailDeliv.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortBailDeliv.deliv_total" label="提货总价值" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0"/>
			<emp:text id="MortBailDeliv.surplus_total" label="剩余总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:date id="MortBailDeliv.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortBailDeliv.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortBailDeliv.memo" label="备注" maxlength="200" required="false" colSpan="2" />	
		</emp:gridLayout>
		<emp:gridLayout id="MortBailDelivGroup" title="登记信息" maxColumn="2">		
			<emp:text id="MortBailDeliv.input_id" label="登记人" maxlength="20" required="false" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortBailDeliv.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" defvalue="$organNo" hidden="true"/>
			<emp:text id="MortBailDeliv.input_id_displayname" label="登记人" required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortBailDeliv.input_br_id_displayname" label="登记机构" required="false" readonly="true" defvalue="$organName"/>
			<emp:date id="MortBailDeliv.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>

		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="tally" label="提货记账" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
	<div class='emp_gridlayout_title'>质押物清单</div>
		<div align="left">
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		</div>
	<emp:table icollName="MortCargoPledgeNewList" pageMode="false" url="" selectType="2" editable="true">
		<emp:text id="cargo_id" label="货物编号" readonly="true" cssElementClass="emp_text"/>
		<emp:text id="guaranty_catalog" label="押品所处目录" readonly="true" cssElementClass="emp_text2"/>
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录名称" readonly="true" cssElementClass="emp_text2"/>
		<emp:text id="qnt" label="在库数量" readonly="true" cssElementClass="emp_text2"/>
		<emp:text id="identy_unit_price" label="单价" readonly="true" dataType="Currency" cssElementClass="emp_text2"/>
		<emp:text id="identy_total" label="在库总价" readonly="true" dataType="Currency" cssElementClass="emp_text2"/>
		<emp:text id="deliv_qnt" label="提货数量" dataType="Double" cssElementClass="emp_text3" onchange="doCacul()" defvalue="0" required="true"/>
		<emp:text id="deliv_value" label="提货价值" readonly="true" dataType="Currency" cssElementClass="emp_text2" defvalue="0"/>
		<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency" cssElementClass="emp_text2" defvalue="0"/>
		<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency" cssElementClass="emp_text2" defvalue="0"/>
	</emp:table>
</body>
</html>
</emp:page>

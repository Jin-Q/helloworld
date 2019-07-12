<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doUpdateCusObisDeposit() {
		var form = document.getElementById("submitForm");
		var result = CusObisDeposit._checkAll();
		if(result){
			CusObisDeposit._toForm(form)
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
		var cus_id  =CusObisDeposit.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusObisDeposit.cus_id="+cus_id;
		var url = '<emp:url action="queryCusObisDepositList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function cheakAmt(amt){
		var getAmt = parseFloat(amt._getValue());
		if(getAmt<0){
		alert("金额值不能为负数！");
		amt._obj.element.value="";
		}
	}
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusObisDepositRecord.do" method="POST">
		<emp:gridLayout id="CusObisDepositGroup" maxColumn="2" title="他行交易－他行存款">
			<emp:text id="CusObisDeposit.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusObisDeposit.seq" label="序号" maxlength="38" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusObisDeposit.org_name" label="开户机构名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:select id="CusObisDeposit.dep_typ" label="存款类型" required="true" dictname="STD_ZB_DEP_TYP" />
			<emp:text id="CusObisDeposit.dep_per" label="存款期限(月)" maxlength="38" required="false" dataType="Int" />
			<emp:text id="CusObisDeposit.acc_no" label="帐号" maxlength="40" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:select id="CusObisDeposit.acc_typ" label="账户类型" required="false" dictname="STD_ZB_CUS_ACC_TYP" />
			<emp:select id="CusObisDeposit.acc_st" label="账户状态" required="false" dictname="STD_ZB_CUS_ACC_ST" />
			<emp:select id="CusObisDeposit.acc_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusObisDeposit.acc_blc" label="存款余额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusObisDeposit.acc_blc)"/>
			<emp:text id="CusObisDeposit.crt_usr_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusObisDeposit.input_date" label="登记日期" required="false"  readonly="true" hidden="true"/>
			<emp:text id="CusObisDeposit.input_br_id" label="登记机构" required="false"  readonly="true" hidden="true"/>
			<emp:text id="CusObisDeposit.last_upd_id" label="更新人" required="false" hidden="true" />
			<emp:date id="CusObisDeposit.last_upd_date" label="更新日期" required="false" hidden="true" />
			<emp:text id="CusObisDeposit.acc_cls" label="科目" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateCusObisDeposit" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
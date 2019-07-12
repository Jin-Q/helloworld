<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsIqpAssetTrans.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function onload(){
		IqpAssetTransList.bill_no._obj.addOneButton("bill_no","选择",getBillForm);
		IqpAssetTransList.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpAssetTransList.cont_no._obj.addOneButton("cont_no","查看",getContForm);
		IqpAssetTransList.bill_no._obj.addOneButton("bill_no","查看",getBillNoForm);
		IqpAssetTransList.agent_asset_acct._obj.addOneButton("acctNo","获取",getAcctNo);
		getAcctNo();
		calTransAmt();
    };

    //-------------通过账号获取在我行的账号信息------------
    function getAcctNo(){
    	var agent_asset_acct = IqpAssetTransList.agent_asset_acct._getValue();
        if(agent_asset_acct == null || agent_asset_acct == ""){
			alert("请先输入账号信息！");
			return;
        }
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var retMsg = jsonstr.mes;
				var ACCT_NO = jsonstr.BODY.ACCT_NO;
				var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
				if(flag == "success"){
					IqpAssetTransList.agent_asset_acct_name._setValue(ACCT_NAME);
				}else {
					alert(retMsg); 
					IqpAssetTransList.agent_asset_acct._setValue("");
					IqpAssetTransList.agent_asset_acct_name._setValue("");
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
		var url = '<emp:url action="getIqpCusAcctForEsb.do"/>?acct_no='+agent_asset_acct;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    };

	function getBillForm(){
		var cus_id = IqpAssetTransList.cus_id._getValue();
		var url = "<emp:url action='queryIqpAssetRegiPop.do'/>&returnMethod=getBill&restrictUsed=flase&regi_type=01";
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
	};

	function getBill(data){
		IqpAssetTransList.bill_no._setValue(data.bill_no._getValue());
		IqpAssetTransList.cont_no._setValue(data.cont_no._getValue());
		IqpAssetTransList.cus_id._setValue(data.cus_id._getValue());
		IqpAssetTransList.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpAssetTransList.cur_type._setValue(data.cur_type._getValue());
		IqpAssetTransList.loan_amt._setValue(data.loan_amt._getValue());
		IqpAssetTransList.loan_balance._setValue(data.loan_balance._getValue());
		IqpAssetTransList.inner_owe_int._setValue(data.inner_owe_int._getValue());
		IqpAssetTransList.out_owe_int._setValue(data.out_owe_int._getValue());
		IqpAssetTransList.five_class._setValue(data.five_class._getValue());
		IqpAssetTransList.twelve_cls_flg._setValue(data.twelve_cls_flg._getValue());
		IqpAssetTransList.trans_rate._setValue('');
		IqpAssetTransList.trans_amt._setValue('');
		
		IqpAssetTransList.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpAssetTransList.fina_br_id_displayname._setValue(data.fina_br_id_displayname._getValue());
		IqpAssetTransList.manager_br_id._setValue(data.manager_br_id._getValue());
		IqpAssetTransList.fina_br_id._setValue(data.fina_br_id._getValue());
	};
    
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetTransList._checkAll()){
			IqpAssetTransList._toForm(form);
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
						var url = '<emp:url action="queryIqpAssetTransListList.do"/>?serno='+IqpAssetTransList.serno._getValue();
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改异常!");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};

	function calTransAmt(){
		var loan_balance = IqpAssetTransList.loan_balance._getValue();//贷款余额
		var trans_rate = IqpAssetTransList.trans_rate._obj.element.value;//转让比率
		if(trans_rate==''){
			trans_rate=0;
		}else{
			trans_rate=parseFloat(trans_rate)/100;
		}
		if(loan_balance != null && loan_balance != ""){
			var trans_amt = Math.round((parseFloat(loan_balance)*parseFloat(trans_rate))*100)/100;
			IqpAssetTransList.trans_amt._setValue(''+trans_amt+'');
		}
	}; 

	function calTransRate(){
		var loan_balance = IqpAssetTransList.loan_balance._getValue();//贷款余额
		var trans_amt = IqpAssetTransList.trans_amt._getValue();//转让金额
		if(loan_balance != null && loan_balance != ""){
			var trans_rate = parseFloat(trans_amt)/parseFloat(loan_balance);
			IqpAssetTransList.trans_rate._setValue(''+trans_rate+'');
		}
	}

	function getAcctOrgID(data){
		IqpAssetTransList.fina_br_id._setValue(data.organno._getValue());
		IqpAssetTransList.fina_br_id_displayname._setValue(data.organname._getValue());
	};
	function getOrgID(data){
		IqpAssetTransList.manager_br_id._setValue(data.organno._getValue());
		IqpAssetTransList.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpAssetTransListRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetTransListGroup" title="资产清单" maxColumn="2">
			<emp:text id="IqpAssetTransList.serno" label="业务编号" maxlength="40" required="true" defvalue="${context.serno}" readonly="true" colSpan="2"/>
			<emp:text id="IqpAssetTransList.bill_no" label="借据编号" maxlength="40" required="true" />
			
			<emp:text id="IqpAssetTransList.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransList.cus_id" label="客户编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransList.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetTransList.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetTransList.loan_amt" label="贷款金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.loan_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.inner_owe_int" label="表内欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.out_owe_int" label="表外欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetTransList.five_class" label="五级分类" required="false" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="IqpAssetTransList.twelve_cls_flg" label="十二级分类" required="false" readonly="true" dictname="STD_ZB_TWELVE_CLASS"/>
			
			<emp:text id="IqpAssetTransList.trans_rate" label="转让比率" maxlength="16" required="true" dataType="Percent" onchange="calTransAmt()"/>
			<emp:text id="IqpAssetTransList.trans_amt" label="转让金额" maxlength="16" required="true" dataType="Currency" onchange="calTransRate()"/>
			<emp:text id="IqpAssetTransList.agent_asset_acct" label="代理资产资金账号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="IqpAssetTransList.agent_asset_acct_name" label="代理资产资金账号名" maxlength="80" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			
			<emp:pop id="IqpAssetTransList.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpAssetTransList.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetTransList.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetTransList.fina_br_id" label="账务机构" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

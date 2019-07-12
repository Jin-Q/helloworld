<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	/*XD140718027：贷后检查返回按钮添加*/
	function doSub(){
    	var task_id = PspBankContacc.task_id._getValue();
    	var cus_id = PspBankContacc.cus_id._getValue();
		var form = document.getElementById("submitForm");
		PspBankContacc._checkAll();
		if(PspBankContacc._checkAll()){
			PspBankContacc._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id;
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

	function getAcctsvcrNo(data){
		PspBankContacc.acctsvcr_no._setValue(data.bank_no._getValue());
		PspBankContacc.acctsvcr_name._setValue(data.bank_name._getValue());
    };

    function doReturn(){
    	var task_id = PspBankContacc.task_id._getValue();
		var cus_id = PspBankContacc.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url; 
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePspBankContaccRecord.do" method="POST">
		<emp:gridLayout id="PspBankContaccGroup" title="银行对账单" maxColumn="2">
			<emp:text id="PspBankContacc.acct_no" label="账号" maxlength="40" required="true" />
			<emp:text id="PspBankContacc.acct_name" label="账户名" maxlength="100" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="PspBankContacc.acctsvcr_no" label="开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAcctsvcrNo"  buttonLabel="选择" hidden="true"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="PspBankContacc.acctsvcr_name" label="开户行行名" maxlength="100" required="true" />
			<emp:select id="PspBankContacc.orie_type" label="业务方向" required="true" dictname="STD_PSP_ORIE_TYPE"/>
			<emp:text id="PspBankContacc.qnt" label="笔数" maxlength="38" required="true" dataType="Int"/>
			<emp:text id="PspBankContacc.amt" label="金额" maxlength="16" required="true" dataType="Currency"/>
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspBankContacc.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2" readonly="true"/>
			<emp:textarea id="PspBankContacc.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspBankContaccGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspBankContacc.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspBankContacc.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspBankContacc.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspBankContacc.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspBankContacc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspBankContacc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspBankContacc.task_id" label="任务编号" required="false" hidden="true"/>
			<emp:text id="PspBankContacc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" />
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<!--<emp:button id="reset" label="重置"/> -->
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>

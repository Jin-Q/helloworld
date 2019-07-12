<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
    	var task_id = PspAccountsReceivableAnaly.task_id._getValue();
    	var cus_id = PspAccountsReceivableAnaly.cus_id._getValue();
		var form = document.getElementById("submitForm");
		PspAccountsReceivableAnaly._checkAll();
		if(PspAccountsReceivableAnaly._checkAll()){
			PspAccountsReceivableAnaly._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id;
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePspAccountsReceivableAnalyRecord.do" method="POST">
		<emp:gridLayout id="PspAccountsReceivableAnalyGroup" title="应收账款分析" maxColumn="2">
			<emp:text id="PspAccountsReceivableAnaly.pyee" label="对象名称" maxlength="100" required="true" />
			<emp:text id="PspAccountsReceivableAnaly.rec_amt" label="原始金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="PspAccountsReceivableAnaly.hpp_date" label="发生日期" required="true" />
			<emp:date id="PspAccountsReceivableAnaly.agreed_end_date" label="约定到期日期" required="true" />
			<emp:text id="PspAccountsReceivableAnaly.paid_amt" label="现值" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="PspAccountsReceivableAnaly.avg_acc_day" label="平均账期（日）" maxlength="8" required="true" dataType="Int"/>
			<emp:text id="PspAccountsReceivableAnaly.tran_freq" label="交易频率（月）" maxlength="8" required="true" dataType="Int"/>
			<emp:text id="PspAccountsReceivableAnaly.analy_tran_amt" label="分析期内的交易金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:select id="PspAccountsReceivableAnaly.pay_type" label="支付方式" required="true" dictname="STD_IQP_PAY_TYPE"/>
			<emp:textarea id="PspAccountsReceivableAnaly.info_sour" label="信息来源" maxlength="250" required="true" colSpan="2"/>
			<emp:textarea id="PspAccountsReceivableAnaly.memo" label="其他信息" maxlength="250" required="true" colSpan="2"/>
			
			<emp:text id="PspAccountsReceivableAnaly.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspAccountsReceivableAnaly.task_id" label="任务编号" required="true" hidden="true"/>
			<emp:text id="PspAccountsReceivableAnaly.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspAccountsReceivableAnalyGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspAccountsReceivableAnaly.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspAccountsReceivableAnaly.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspAccountsReceivableAnaly.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspAccountsReceivableAnaly.input_id" label="登记人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspAccountsReceivableAnaly.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String cus_id = request.getParameter("cus_id"); 
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
/************************ 下拉框(empext:select)的样式 **************************/
	/************ 下拉框(select)普通状态下的样式 ****************/
.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

.emp_field_select_input { /***** 隐藏 *****/
	display: none;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
}

/************ 下拉框(select)只读状态下的样式 ****************/
.emp_field_readonly .emp_field_select_input {
	display: inline;
	width: 200px;
	border-color: #b7b7b7;
	background-color: #e3e3e3;
}

/************ 下拉框(select)不可用状态下的样式 ****************/
.emp_field_disabled .emp_field_select_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		var cus_id = '<%=cus_id%>';
		PspRepaySrc.task_id._setValue(task_id);
		PspRepaySrc.cus_id._setValue(cus_id);

		var urls = "&condition=a.cus_id = '"+cus_id+"' and acc_status = '1'";
		PspRepaySrc.bill_no._obj.config.url=PspRepaySrc.bill_no._obj.config.url+urls;
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspRepaySrc.task_id._getValue();
		var cus_id = PspRepaySrc.cus_id._getValue();
		if(PspRepaySrc._checkAll()){
			PspRepaySrc._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyListForMon.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
						url = EMPTools.encodeURI(url);
						window.location = url; 
					}else {
						alert("新增异常!"); 
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
		}
	};	

	function getAcctsvcrNo(data){
		PspRepaySrc.asgn_acctsvcr_no._setValue(data.bank_no._getValue());
		PspRepaySrc.asgn_acctsvcr_name._setValue(data.bank_name._getValue());
    };

    function selAccInfo(data){
		var cus_id = '<%=cus_id%>';
		var bill_no = data.bill_no._getValue();
		
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
					PspRepaySrc.bill_no._setValue(bill_no);
				}else {
					alert("该借据已存在！");
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

		var url = '<emp:url action="checkAccLoanForPspRe.do"/>?cus_id='+cus_id+'&bill_no='+bill_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}

    function doReturn() {
		history.go(-1);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspRepaySrcRecord.do" method="POST">
		
		<emp:gridLayout id="PspRepaySrcGroup" title="还款来源信息" maxColumn="2">
			<emp:pop id="PspRepaySrc.bill_no" label="借据编号" url="queryAccLoanPop.do?returnMethod=selAccInfo" />
			<emp:date id="PspRepaySrc.asgn_date" label="转入日期" required="true" />
			<emp:text id="PspRepaySrc.asgn_amt" label="转入金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:select id="PspRepaySrc.sour" label="来源" required="true" dictname="STD_PSP_REPAY_SOUR" cssElementClass="emp_field_select_select1" colSpan="2"/>
			<emp:text id="PspRepaySrc.asgn_acct_no" label="转入方账号" maxlength="40" required="true" />
			<emp:text id="PspRepaySrc.asgn_acct_name" label="转入方账户名" maxlength="100" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="PspRepaySrc.asgn_acctsvcr_no" label="转入方开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAcctsvcrNo" required="true" buttonLabel="选择" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="PspRepaySrc.asgn_acctsvcr_name" label="转入方开户行行名" maxlength="100" required="true" readonly="true" cssElementClass="emp_field_select_select1" colSpan="2"/>
			<emp:text id="PspRepaySrc.proof_type" label="凭证种类和编号" maxlength="40" required="true" />
			<emp:text id="PspRepaySrc.asgn_acct_bal" label="转入后账户余额" maxlength="16" required="true" dataType="Currency"/>
			<emp:select id="PspRepaySrc.cus_rela" label="转入方和借款人关系" required="true" dictname="STD_OPP_DEBIT_RELA"/>
			<emp:textarea id="PspRepaySrc.memo" label="补充说明" maxlength="250" required="false" colSpan="2"/>
			
			<emp:text id="PspRepaySrc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspRepaySrc.task_id" label="任务编号" required="true" hidden="true"/>
			<emp:text id="PspRepaySrc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspRepaySrc.remarks" label="备注" maxlength="250" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="" title="登记信息" maxColumn="2">
			<emp:text id="PspRepaySrc.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspRepaySrc.input_br_id_displayname" label="登记机构"   required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspRepaySrc.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspRepaySrc.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspRepaySrc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


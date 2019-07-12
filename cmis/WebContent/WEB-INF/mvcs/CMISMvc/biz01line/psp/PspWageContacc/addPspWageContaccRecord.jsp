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

<script type="text/javascript">

	/*--user code begin--*/
	/*XD140718027：检查页面添加返回按钮*/
	function doload(){
		var task_id = '<%=task_id%>';
		var cus_id = '<%=cus_id%>';
		PspWageContacc.task_id._setValue(task_id);
		PspWageContacc.cus_id._setValue(cus_id);
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspWageContacc.task_id._getValue();
		var cus_id = PspWageContacc.cus_id._getValue();
		if(PspWageContacc._checkAll()){
			PspWageContacc._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
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
		}else {
			return false;
		}
		
	};	

	function getAcctsvcrNo(data){
		PspWageContacc.acctsvcr_no._setValue(data.bank_no._getValue());
		PspWageContacc.acctsvcr_name._setValue(data.bank_name._getValue());
    };

    function checkPaidDate(){
		var settl_start_date = PspWageContacc.settl_start_date._getValue();
		var settl_end_date = PspWageContacc.settl_end_date._getValue();
		if(settl_start_date!=''&&settl_end_date!=''){
			if(CheckDate1BeforeDate2(settl_end_date,settl_start_date)){
				alert('工资阶段结束日期不能小于等于工资结算起始日期！');
				PspWageContacc.settl_end_date._setValue('');
			}
		}
	};

	function doReturn(){
		var task_id = PspWageContacc.task_id._getValue();
		var cus_id = PspWageContacc.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url; 
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspWageContaccRecord.do" method="POST">
		
		<emp:gridLayout id="PspWageContaccGroup" title="工资对账单" maxColumn="2">
			<emp:text id="PspWageContacc.con_acct_no" label="企业账户" maxlength="40"  hidden="true"/>
			<emp:text id="PspWageContacc.con_acct_name" label="企业账户名" maxlength="100"   hidden="true"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="PspWageContacc.acctsvcr_no" label="开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAcctsvcrNo"  buttonLabel="选择"  hidden="true"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="PspWageContacc.acctsvcr_name" label="开户行行名" maxlength="40"  readonly="true"  hidden="true"/>
			<emp:text id="PspWageContacc.person_qnt" label="人数" maxlength="38" required="true" dataType="Int" colSpan="2"/>
			<emp:text id="PspWageContacc.the_amt" label="应发金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="PspWageContacc.act_amt" label="实发金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="PspWageContacc.settl_start_date" label="工资结算起始日期" required="true" onblur="checkPaidDate()"/>
			<emp:date id="PspWageContacc.settl_end_date" label="工资阶段结束日期" required="true" onblur="checkPaidDate()"/>
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspWageContacc.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" defvalue="${context.check_freq}" readonly="true" colSpan="2"/>
			<emp:textarea id="PspWageContacc.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspWageContaccGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspWageContacc.input_id_displayname" label="登记人"   required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspWageContacc.input_br_id_displayname" label="登记机构"   required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspWageContacc.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspWageContacc.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspWageContacc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="PspWageContacc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspWageContacc.task_id" label="任务编号" required="false" hidden="true"/>
			<emp:text id="PspWageContacc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


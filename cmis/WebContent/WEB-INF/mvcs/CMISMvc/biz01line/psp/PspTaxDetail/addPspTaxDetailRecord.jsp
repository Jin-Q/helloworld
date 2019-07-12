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
		PspTaxDetail.task_id._setValue(task_id);
		PspTaxDetail.cus_id._setValue(cus_id);
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspTaxDetail.task_id._getValue();
		var cus_id = PspTaxDetail.cus_id._getValue();
		if(PspTaxDetail._checkAll()){
			PspTaxDetail._toForm(form); 
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

	function checkPaidDate(){
		var paid_start_date = PspTaxDetail.paid_start_date._getValue();
		var paid_end_date = PspTaxDetail.paid_end_date._getValue();
		if(paid_start_date!=''&&paid_end_date!=''){
			if(CheckDate1BeforeDate2(paid_end_date,paid_start_date)){
				alert('税务缴纳结束日期不能小于等于税务缴纳起始日期！');
				PspTaxDetail.paid_end_date._setValue('');
			}
		}
	}

	function doReturn(){
		var task_id = PspTaxDetail.task_id._getValue();
		var cus_id = PspTaxDetail.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url; 
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspTaxDetailRecord.do" method="POST">
		
		<emp:gridLayout id="PspTaxDetailGroup" title="税费明细" maxColumn="2">
			<emp:text id="PspTaxDetail.tax_regi_no" label="税务登记证号" maxlength="40"  colSpan="2" hidden="true"/>
			<emp:select id="PspTaxDetail.tax_type" label="税费类型" required="true" dictname="STD_PSP_TAX_TYPE"/>
			<emp:text id="PspTaxDetail.tax_amt" label="缴费金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="PspTaxDetail.paid_start_date" label="税务缴纳起始日期" required="true" onblur="checkPaidDate()"/>
			<emp:date id="PspTaxDetail.paid_end_date" label="税务缴纳结束日期" required="true" onblur="checkPaidDate()"/>
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspTaxDetail.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" defvalue="${context.check_freq}" readonly="true" colSpan="2"/>
			<emp:textarea id="PspTaxDetail.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspTaxDetailGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspTaxDetail.input_id_displayname" label="登记人"  required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspTaxDetail.input_br_id_displayname" label="登记机构"  required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspTaxDetail.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspTaxDetail.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspTaxDetail.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="PspTaxDetail.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspTaxDetail.task_id" label="任务编号" required="false" hidden="true"/>
			<emp:text id="PspTaxDetail.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


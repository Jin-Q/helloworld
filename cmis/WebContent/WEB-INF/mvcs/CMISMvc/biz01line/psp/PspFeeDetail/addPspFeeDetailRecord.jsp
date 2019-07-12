<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
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
		PspFeeDetail.task_id._setValue(task_id);
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspFeeDetail._checkAll()){
			PspFeeDetail._toForm(form); 
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
						doReturn();
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};	

	function checkDate(){
		var last_regi_date = PspFeeDetail.last_regi_date._getValue();
		var regi_date = PspFeeDetail.regi_date._getValue();
		//alert(CheckDate1BeforeDate2(last_regi_date,regi_date));
		if(last_regi_date!=''&&regi_date!=''){
			if(CheckDate1BeforeDate2(regi_date,last_regi_date)){
				alert('缴纳结束日期不能小于等于缴纳起始日期！');
				PspFeeDetail.regi_date._setValue('');
			}
		}
	}

	function doReturn(){
		var dataFrom = '<%=dataFrom%>';
		var task_id = PspFeeDetail.task_id._getValue();
		var cus_id = PspFeeDetail.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		if(dataFrom=='indiv'){
			url = '<emp:url action="queryPspPropertyAnalyListForWater.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspFeeDetailRecord.do" method="POST">
		
		<emp:gridLayout id="PspFeeDetailGroup" title="水电费明细" maxColumn="2">
			<emp:date id="PspFeeDetail.last_regi_date" label="缴纳起始日期" defvalue="${context.regi_date}" />
			<emp:date id="PspFeeDetail.regi_date" label="缴纳结束日期" required="true" onblur="checkDate()"/>
			<emp:text id="PspFeeDetail.paid_acct_no" label="缴费账号" maxlength="40"  hidden="true"/>
			<emp:text id="PspFeeDetail.paid_acct_name" label="缴费账户名" maxlength="100" required="true" />
			<emp:select id="PspFeeDetail.paid_type" label="缴费类别" required="true" dictname="STD_PSP_PAID_TYPE"/>
			<emp:text id="PspFeeDetail.qnt" label="数量（吨/度）" maxlength="38" required="true" dataType="Int"/>
			<emp:text id="PspFeeDetail.paid_amt" label="缴费金额（元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="PspFeeDetail.breach_amt" label="违约金（元）" maxlength="16" required="true" dataType="Currency"/>
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspFeeDetail.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" defvalue="${context.check_freq}" readonly="true" colSpan="2"/>
			<emp:textarea id="PspFeeDetail.remarks" label="备注" maxlength="250" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="PspFeeDetailGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspFeeDetail.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspFeeDetail.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspFeeDetail.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspFeeDetail.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspFeeDetail.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="PspFeeDetail.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspFeeDetail.task_id" label="任务编号" required="false" hidden="true" defvalue="${context.task_id}"/>
			<emp:text id="PspFeeDetail.cus_id" label="客户码" maxlength="40" required="false" hidden="true" defvalue="${context.cus_id}"/>
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


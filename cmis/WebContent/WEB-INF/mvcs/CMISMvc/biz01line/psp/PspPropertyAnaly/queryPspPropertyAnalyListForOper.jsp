<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*XD140718027：公司客户和小微客户经营佐证信息的修改*/
	function doload(){
		var task_id = '${context.task_id}';
		this_task_id = '${context.task_id}';
		PspCheckAnaly.task_id._setValue(task_id);
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		PspPropertyAnaly._toForm(form);
		PspPropertyAnalyList._obj.ajaxQuery(null,form);
	};

	function doGetUpdatePspFeeDetailPage() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspFeeDetailList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspFeeDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspFeeDetail() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspFeeDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspFeeDetailPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspFeeDetailAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspFeeDetail() {
		var paramStr = PspFeeDetailList._obj.getParamStr(['pk_id']);
		var task_id = PspFeeDetailList._obj.getParamValue(['task_id']);
		var cus_id = PspFeeDetailList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspFeeDetailList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspFeeDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doResetvalue(){
		PspCheckAnaly.water_charge_analy._obj.reset();
		PspCheckAnaly.tax_charge_analy._obj.reset();
		PspCheckAnaly.paylist_analy._obj.reset();
		PspCheckAnaly.iostorelist_analy._obj.reset();
	};

	function doGetUpdatePspTaxDetailPage() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspTaxDetailList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspTaxDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspTaxDetail() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspTaxDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspTaxDetailPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspTaxDetailAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspTaxDetail() {
		var paramStr = PspTaxDetailList._obj.getParamStr(['pk_id']);
		var task_id = PspTaxDetailList._obj.getParamValue(['task_id']);
		var cus_id = PspTaxDetailList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspTaxDetailList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspTaxDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};

	function doSaveanaly(){
		var form = document.getElementById("submitForm");
		if(PspCheckAnaly._checkAll()){
			PspCheckAnaly._toForm(form);
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
						alert("保存成功!");
					}else {
						alert("保存失败!");
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
		}else {
			return false;
		}
	}

	function doGetUpdatePspBankContaccPage() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspBankContaccList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspBankContaccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspBankContacc() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspBankContaccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspBankContaccPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspBankContaccAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspBankContacc() {
		var paramStr = PspBankContaccList._obj.getParamStr(['pk_id']);
		var task_id = PspBankContaccList._obj.getParamValue(['task_id']);
		var cus_id = PspBankContaccList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspBankContaccList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspBankContaccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdatePspWageContaccPage() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspWageContaccList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspWageContaccUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspWageContacc() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspWageContaccViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspWageContaccPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspWageContaccAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspWageContacc() {
		var paramStr = PspWageContaccList._obj.getParamStr(['pk_id']);
		var task_id = PspWageContaccList._obj.getParamValue(['task_id']);
		var cus_id = PspWageContaccList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspWageContaccList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspWageContaccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdatePspIostoreDocPage() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspIostoreDocList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspIostoreDocUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspIostoreDoc() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspIostoreDocViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspIostoreDocPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspIostoreDocAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspIostoreDoc() {
		var paramStr = PspIostoreDocList._obj.getParamStr(['pk_id']);
		var task_id = PspIostoreDocList._obj.getParamValue(['task_id']);
		var cus_id = PspIostoreDocList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspIostoreDocList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspIostoreDocRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
	function doGetUpdatePspOrderAnalyPage() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspOrderAnalyList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspOrderAnalyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspOrderAnaly() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspOrderAnalyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspOrderAnalyPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspOrderAnalyAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspOrderAnaly() {
		var paramStr = PspOrderAnalyList._obj.getParamStr(['pk_id']);
		var task_id = PspOrderAnalyList._obj.getParamValue(['task_id']);
		var cus_id = PspOrderAnalyList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspOrderAnalyList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspOrderAnalyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReturn() {
		var menuId = "${context.menuId}" ;
		if(menuId == 'expert_check_task_his' || menuId == 'period_check_task_his' || menuId == 'first_check_task_his'){
			url = '<emp:url action="queryPspCheckTaskHistoryList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}';
		}else{
			url = '<emp:url action="queryPspCheckTaskList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	
	
	<div class="emp_gridLayout_title">水电费明细 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspFeeDetailPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspFeeDetailPage" label="修改" op="update"/>
		<emp:button id="deletePspFeeDetail" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspFeeDetail" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspFeeDetailList" pageMode="false" url="pagePspFeeDetailQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="last_regi_date" label="缴纳起始日期" />
		<emp:text id="regi_date" label="缴纳结束日期" />
		<emp:text id="paid_acct_no" label="缴费账号" hidden="true"/>
		<emp:text id="paid_acct_name" label="缴费账户名" />
		<emp:text id="paid_type" label="缴费类别" dictname="STD_PSP_PAID_TYPE"/>
		<emp:text id="qnt" label="数量（吨/度）" dataType="Int"/>
		<emp:text id="paid_amt" label="缴费金额（元）" dataType="Currency"/>
		<emp:text id="breach_amt" label="违约金（元）" dataType="Currency"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	<emp:gridLayout id="PspGroup" title="水电费分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.water_charge_analy" label="水电费分析说明：" maxlength="250" required="false" colSpan="2" />
	<emp:text id="PspCheckAnaly.task_id" label="任务编号"  required="false" hidden="true"/>
	</emp:gridLayout>
	<br>
	<br>
	
	<div class="emp_gridLayout_title">税费</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspTaxDetailPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspTaxDetailPage" label="修改" op="update"/>
		<emp:button id="deletePspTaxDetail" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspTaxDetail" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspTaxDetailList" pageMode="false" url="pagePspTaxDetailQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="tax_regi_no" label="税务登记证号" hidden="true"/>
		<emp:text id="tax_type" label="税费类型" dictname="STD_PSP_TAX_TYPE"/>
		<emp:text id="tax_amt" label="缴费金额" dataType="Currency"/>
		<emp:text id="paid_start_date" label="税务缴纳起始日期" />
		<emp:text id="paid_end_date" label="税务缴纳结束日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	<emp:gridLayout id="PspGroup" title="税费分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.tax_charge_analy" label="税费分析说明：" maxlength="250" required="false" colSpan="2"/>
	</emp:gridLayout>
	<br>
	<br>
	
	<div class="emp_gridLayout_title">银行对账单</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspBankContaccPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspBankContaccPage" label="修改" op="update"/>
		<emp:button id="deletePspBankContacc" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspBankContacc" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspBankContaccList" pageMode="false" url="pagePspBankContaccQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="acct_no" label="账号" />
		<emp:text id="acct_name" label="账户名" />
		<emp:text id="acctsvcr_no" label="开户行行号" hidden="true"/>
		<emp:text id="acctsvcr_name" label="开户行行名" hidden="true"/>
		<emp:text id="orie_type" label="业务方向" dictname="STD_PSP_ORIE_TYPE"/>
		<emp:text id="qnt" label="笔数" dataType="Int"/>
		<emp:text id="amt" label="金额" dataType="Currency"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	<emp:gridLayout id="PspGroup" title="银行对账单分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.bank_contacclist_analy" label="银行对账单分析说明：" maxlength="250" required="true" colSpan="2"/>
	</emp:gridLayout>
	<br>
	<br>
	
	<div class="emp_gridLayout_title">工资单</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspWageContaccPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspWageContaccPage" label="修改" op="update"/>
		<emp:button id="deletePspWageContacc" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspWageContacc" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspWageContaccList" pageMode="false" url="pagePspWageContaccQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="con_acct_no" label="企业账户" hidden="true"/>
		<emp:text id="con_acct_name" label="企业账户名" hidden="true"/>
		<emp:text id="acctsvcr_no" label="开户行行号" hidden="true"/>
		<emp:text id="acctsvcr_name" label="开户行行名" hidden="true"/>
		<emp:text id="person_qnt" label="人数" dataType="Int"/>
		<emp:text id="the_amt" label="应发金额" dataType="Currency"/>
		<emp:text id="act_amt" label="实发金额" dataType="Currency"/>
		<emp:text id="settl_start_date" label="工资结算起始日期" />
		<emp:text id="settl_end_date" label="工资阶段结束日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	<emp:gridLayout id="PspGroup" title="工资单分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.paylist_analy" label="工资单分析说明：" maxlength="250" required="false" colSpan="2"/>
	</emp:gridLayout>
	<br>
	<br>
	
	<div class="emp_gridLayout_title">出入库单据</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspIostoreDocPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspIostoreDocPage" label="修改" op="update"/>
		<emp:button id="deletePspIostoreDoc" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspIostoreDoc" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspIostoreDocList" pageMode="false" url="pagePspIostoreDocQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="goods_name" label="货物名称" />
		<emp:text id="qnt_unit" label="数量单位" dictname="STD_ZB_UNIT"/>
		<emp:text id="qnt" label="数量" dataType="Int"/>
		<emp:text id="total_price" label="买进总价值" dataType="Currency"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	<emp:gridLayout id="PspGroup" title="出入库单据分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.iostorelist_analy" label="出入库单据分析说明：" maxlength="250" required="false" colSpan="2"/>
	</emp:gridLayout>
	<br>
	<br>
	
	<div class="emp_gridLayout_title">订单明细</div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspOrderAnalyPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspOrderAnalyPage" label="修改" op="update"/>
		<emp:button id="deletePspOrderAnaly" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspOrderAnaly" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspOrderAnalyList" pageMode="false" url="pagePspOrderAnalyQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户编码" hidden="true"/>
		<emp:text id="rcver_name" label="需方名称" />
		<emp:text id="order_date" label="签订时间" />
		<emp:text id="prd_name" label="产品名称" />
		<emp:text id="model_no" label="型号" hidden="true"/>
		<emp:text id="unit_price" label="单价" dataType="Currency" hidden="true"/>
		<emp:text id="amt" label="金额" dataType="Currency" />
		<emp:text id="provid_date" label="供货时间" />
		<emp:text id="qnt" label="供货数量" />
	</emp:table>
	<emp:gridLayout id="PspGroup" title="订单明细分析说明" maxColumn="2">
	<emp:textarea id="PspCheckAnaly.order_analy" label="订单明细分析说明：" maxlength="250" required="false" colSpan="2"/>
	</emp:gridLayout>
	<br>
	<br>
	<emp:form id="submitForm" action="updatePspCheckAnalyRecord.do" method="POST">
	<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="saveanaly" label="确定" />
			<emp:button id="resetvalue" label="重置"/>
			<emp:button id="return" label="返回列表"/>
			<%} %>
	</div>
	</emp:form>
</body>
</html>
</emp:page>
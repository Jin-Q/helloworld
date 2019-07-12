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

	function doload(){
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		//PspCheckAnaly.task_id._setValue(task_id);
		//PspCheckAnaly.cus_id._setValue(cus_id);
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		PspPropertyAnaly._toForm(form);
		PspPropertyAnalyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspPropertyAnalyPage() {
		var paramStr = PspPropertyAnalyList._obj.getParamStr(['property_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspPropertyAnalyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspPropertyAnaly() {
		var paramStr = PspPropertyAnalyList._obj.getParamStr(['property_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspPropertyAnalyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspPropertyAnalyPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspPropertyAnalyAddPage.do"/>?'+'task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeletePspPropertyAnaly() {
		var paramStr = PspPropertyAnalyList._obj.getParamStr(['property_id']);
		var task_id = PspPropertyAnalyList._obj.getParamValue(['task_id']);
		var cus_id = PspPropertyAnalyList._obj.getParamValue(['cus_id']);
		var property_type = PspPropertyAnalyList._obj.getParamValue(['property_type']);
		if (paramStr != null) {
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
							var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
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
				var url = '<emp:url action="deletePspPropertyAnalyRecord.do"/>?'+paramStr+'&property_type='+property_type;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doResetvalue(){
		PspCheckAnaly.property_analy._obj.reset();
		PspCheckAnaly.accounts_receivable_analy._obj.reset();
	};

	function doGetUpdatePspAccountsReceivableAnalyPage() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAccountsReceivableAnalyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspAccountsReceivableAnaly() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspAccountsReceivableAnalyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspAccountsReceivableAnalyPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspAccountsReceivableAnalyAddPage.do"/>?'+'task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspAccountsReceivableAnaly() {
		var paramStr = PspAccountsReceivableAnalyList._obj.getParamStr(['pk_id']);
		var task_id = PspAccountsReceivableAnalyList._obj.getParamValue(['task_id']);
		var cus_id = PspAccountsReceivableAnalyList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
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
							var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
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
				var url = '<emp:url action="deletePspAccountsReceivableAnalyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	
	
	<div class="emp_gridLayout_title">固定资产分析 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspPropertyAnalyPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspPropertyAnalyPage" label="修改" op="update"/>
		<emp:button id="deletePspPropertyAnaly" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspPropertyAnaly" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspPropertyAnalyList" pageMode="false" url="pagePspPropertyAnalyQuery.do">
		<emp:text id="property_id" label="资产编号" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:select id="property_type" label="类型" dictname="STD_ZB_PROPERTY_TYPE"/>
		<emp:text id="owner" label="所有权人"/>
		<emp:text id="owner_cert_type" label="所有权人证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="owner_cert_code" label="所有权人证件号码" />
		<emp:text id="warrant_type" label="权证类型" dictname="STD_WRR_PROVE_TYPE" />
		<emp:text id="warrant_no" label="权证号码" />
	</emp:table>
	<!--  
	固定资产分析说明：<emp:textarea id="PspCheckAnaly.property_analy" label="固定资产分析说明" maxlength="250" hidden="true" colSpan="2"/>
	-->
	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">
	<%if(op.equals("view")){ %>
		<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA2787019371B94A4ADA7DD8B181C1&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
	<%}else{ %>
		<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA2787019371B94A4ADA7DD8B181C1&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update" initial="true"/>
	<%} %>
	</emp:tabGroup>
	
	<!-- 
	<div class="emp_gridLayout_title">应收账款分析 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspAccountsReceivableAnalyPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspAccountsReceivableAnalyPage" label="修改" op="update"/>
		<emp:button id="deletePspAccountsReceivableAnaly" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspAccountsReceivableAnaly" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspAccountsReceivableAnalyList" pageMode="false" url="pagePspAccountsReceivableAnalyQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="pyee" label="收款对象" />
		<emp:text id="rec_amt" label="原始金额" dataType="Currency"/>
		<emp:text id="hpp_date" label="发生日期" />
		<emp:text id="agreed_end_date" label="约定到期日期" />
		<emp:text id="paid_amt" label="现值" dataType="Currency"/>
		<emp:text id="tran_freq" label="交易频率（月）" />
		<emp:text id="analy_tran_amt" label="分析期内的交易金额" dataType="Currency"/>
		<emp:select id="pay_type" label="支付方式" dictname="STD_IQP_PAY_TYPE"/>
	</emp:table>
	
	应收账款分析说明：<emp:textarea id="PspCheckAnaly.accounts_receivable_analy" label="固定资产分析说明" maxlength="250" hidden="true" colSpan="2"/>
	<emp:text id="PspCheckAnaly.task_id" label="任务编号"  required="false" hidden="true"/>
	<emp:text id="PspCheckAnaly.cus_id" label="客户码"  required="false" hidden="true"/>
	<br>
	<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="saveanaly" label="确定" />
			<emp:button id="resetvalue" label="重置"/>
			<%} %>
	</div>
	<emp:form id="submitForm" action="updatePspCheckAnalyRecord.do" method="POST">
	</emp:form>
	-->
	
</body>
</html>
</emp:page>
    
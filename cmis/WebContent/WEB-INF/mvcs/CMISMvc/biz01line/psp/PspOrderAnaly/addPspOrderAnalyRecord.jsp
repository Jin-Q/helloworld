<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
		PspOrderAnaly.task_id._setValue(task_id);
		PspOrderAnaly.cus_id._setValue(cus_id);
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspOrderAnaly.task_id._getValue();
		var cus_id = PspOrderAnaly.cus_id._getValue();
		if(PspOrderAnaly._checkAll()){
			PspOrderAnaly._toForm(form); 
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	function doReturn(){
		var task_id = PspOrderAnaly.task_id._getValue();
		var cus_id = PspOrderAnaly.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForOper.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspOrderAnalyRecord.do" method="POST">
		
		<emp:gridLayout id="PspOrderAnalyGroup" title="订单明细" maxColumn="2">
			<emp:text id="PspOrderAnaly.rcver_name" label="需方名称" maxlength="100" required="true" />
			<emp:date id="PspOrderAnaly.order_date" label="签订时间" required="false" />
			<emp:text id="PspOrderAnaly.prd_name" label="产品名称" maxlength="100" required="true" />
			<emp:text id="PspOrderAnaly.model_no" label="型号" maxlength="40"  hidden="true"/>
			<emp:text id="PspOrderAnaly.unit_price" label="单价" maxlength="16"  dataType="Currency" hidden="true"/>
			<emp:text id="PspOrderAnaly.amt" label="金额" maxlength="16" required="true" dataType="Currency" />
			<emp:date id="PspOrderAnaly.provid_date" label="供货时间" required="false" />
			<emp:text id="PspOrderAnaly.qnt" label="供货数量" maxlength="38" required="true" dataType="Int" />
			<!-- modified by lisj 2015-1-21 需求编号：【XD141230092】 贷后管理需求（首次检查、零售常规） -->
			<emp:select id="PspOrderAnaly.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" defvalue="${context.check_freq}" readonly="true" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspOrderAnaly.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" readonly="true" />
			<emp:text id="PspOrderAnaly.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="PspOrderAnaly.input_date" label="登记日期" maxlength="10" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspOrderAnaly.input_id" label="登记人" maxlength="40" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspOrderAnaly.input_br_id" label="登记机构" maxlength="20" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="PspOrderAnaly.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspOrderAnaly.task_id" label="任务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="PspOrderAnaly.cus_id" label="客户编码" maxlength="40" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" />
			<emp:button id="reset" label="取消"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


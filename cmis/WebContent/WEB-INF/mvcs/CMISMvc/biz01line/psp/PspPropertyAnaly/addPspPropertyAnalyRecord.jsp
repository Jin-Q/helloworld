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
<jsp:include page="jsPspProperty.jsp" flush="true" />
<script type="text/javascript"><!--

	/*--user code begin--*/
	/*XD140718027：检查页面添加返回按钮*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspPropertyAnaly.task_id._setValue(task_id);
		var cus_id = '<%=cus_id%>';
		PspPropertyAnaly.cus_id._setValue(cus_id);
	}

	function doReturn(){
		var task_id = PspPropertyAnaly.task_id._getValue();
		var cus_id = PspPropertyAnaly.cus_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
		url = EMPTools.encodeURI(url);
		window.location = url; 
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var task_id = PspPropertyAnaly.task_id._getValue();
		var cus_id = PspPropertyAnaly.cus_id._getValue();
		if(PspPropertyAnaly._checkAll()){
			PspPropertyAnaly._toForm(form); 
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
						var url = '<emp:url action="queryPspPropertyAnalyList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
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
	/*--user code end--*/
	
--></script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspPropertyAnalyRecord.do" method="POST">
		
		<emp:gridLayout id="PspPropertyAnalyGroup" title="固定资产分析" maxColumn="2">
			<emp:text id="PspPropertyAnaly.owner" label="所有权人" required="true" maxlength="40" cssElementClass="emp_field_text_input1" colSpan="2"/>
			<emp:select id="PspPropertyAnaly.property_type" label="资产类型" required="true" dictname="STD_ZB_PROPERTY_TYPE" />
			<emp:select id="PspPropertyAnaly.rela_type" label="客户关系" required="true" dictname="STD_ZB_PSP_RELA_TYPE" />
			<emp:select id="PspPropertyAnaly.owner_cert_type" label="所有权人证件类型" required="true" dictname="STD_ZB_CERT_TYP" onchange="checkCertCode()"/>
			<emp:text id="PspPropertyAnaly.owner_cert_code" label="所有权人证件号码" required="true" maxlength="40" onblur="checkCertCode()"/>
			<emp:select id="PspPropertyAnaly.warrant_type" label="权证类型" required="true" dictname="STD_WRR_PROVE_TYPE" />
			<emp:text id="PspPropertyAnaly.warrant_no" label="权证号码" required="true" maxlength="40" />
			
			<emp:text id="PspPropertyAnaly.task_id" label="任务编号" required="true" hidden="true"/>
			<emp:text id="PspPropertyAnaly.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspPropertyAnalyGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspPropertyAnaly.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspPropertyAnaly.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspPropertyAnaly.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspPropertyAnaly.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspPropertyAnaly.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表"></emp:button>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


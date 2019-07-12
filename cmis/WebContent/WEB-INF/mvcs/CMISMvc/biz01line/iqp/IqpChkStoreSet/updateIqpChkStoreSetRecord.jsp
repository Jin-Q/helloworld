<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function setconId(data){
		IqpChkStoreSet.task_exe_user._setValue(data.actorno._getValue());
		IqpChkStoreSet.task_exe_user_displayname._setValue(data.actorname._getValue());
	};

	function doCretask(){
		checkIqpChkStoreTask();
	}
	
	//生成任务前校验当天是否已存在任务
	function checkIqpChkStoreTask(){
		var cus_id = IqpChkStoreSet.cus_id._getValue();
		var task_set_id = IqpChkStoreSet.task_set_id._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					addTask();
				}else {
					alert(msg);
					return;
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

		var url="<emp:url action='checkIqpChkStoreTask.do'/>?cus_id="+cus_id+"&task_set_id="+task_set_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	}
	
	//根据监管协议生成任务
	function addTask(){
		var form = document.getElementById("submitForm");
		if(IqpChkStoreSet._checkAll()){
			IqpChkStoreSet._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert(o.responseText);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						var url = '<emp:url  action="queryIqpChkStoreSetList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == "failure"){
						alert("监管协议不存在，生成任务失败!");
					}else {
						alert("生成任务失败!");
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
			var url = '<emp:url action="addIqpChkStoreTaskRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		}else {
			return;
		}
	}

	function doGenerate(){
		if(IqpChkStoreSet._checkAll()){
			var form = document.getElementById("submitForm");
			IqpChkStoreSet._toForm(form);
			var handleSuccess = function(o){ EMPTools.unmask();
				if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("程序异常！");
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("保存成功!");
						}else{
							alert("程序异常!");
						}	
				}
			};
			var handleFailure = function(o){ EMPTools.unmask();	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
		};
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpChkStoreSetRecord.do" method="POST">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="核/巡任务基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpChkStoreSetGroup" maxColumn="2" title="核/巡任务设定">
			<emp:text id="IqpChkStoreSet.task_set_id" label="任务编号" maxlength="32" required="true" readonly="true" />
			<emp:select id="IqpChkStoreSet.task_set_type" label="任务维度" required="false" dictname="STD_ZB_INSURE_MODE" readonly="true"/>
			<%
				String task_set_type = "";
				if(context.containsKey("task_set_type")){
					task_set_type = (String)context.getDataValue("task_set_type");
				}
				if(task_set_type.equals("01")){//出质人
			%>
			<emp:text id="IqpChkStoreSet.cus_id" label="出质人编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreSet.cus_id_displayname" label="出质人名称" required="false" readonly="true"/>
			<% }else{%>
			<emp:text id="IqpChkStoreSet.cus_id" label="监管企业编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreSet.cus_id_displayname" label="监管企业名称" required="false" readonly="true"/>
			<% }%>
			
			<emp:pop id="IqpChkStoreSet.task_exe_user_displayname" label="任务执行人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:date id="IqpChkStoreSet.task_request_time" label="要求完成时间" required="true" />
			<emp:textarea id="IqpChkStoreSet.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">	
			<emp:text id="IqpChkStoreSet.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpChkStoreSet.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpChkStoreSet.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="IqpChkStoreSet.is_task" label="是否生成任务" dictname="STD_ZX_YES_NO" required="false" defvalue="2" hidden="true"/>
			<emp:text id="IqpChkStoreSet.task_exe_user" label="任务执行人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpChkStoreSet.input_id" label="登记人" maxlength="20" required="false" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="IqpChkStoreSet.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="generate" label="保存" op="update"/>
			<emp:button id="cretask" label="生成任务" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
		
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
		
	</emp:form>
</body>
</html>
</emp:page>

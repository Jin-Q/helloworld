<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	//POP框返回方法
	function setconId(data){
		IqpChkStoreTask.manager_id._setValue(data.actorno._getValue());
		IqpChkStoreTask.manager_id_displayname._setValue(data.actorname._getValue());
		IqpChkStoreTask.manager_br_id._setValue(data.orgid._getValue());
		IqpChkStoreTask.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		IqpChkStoreTask.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					IqpChkStoreTask.manager_br_id._setValue(jsonstr.org);
					IqpChkStoreTask.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpChkStoreTask.manager_br_id._setValue("");
					IqpChkStoreTask.manager_br_id_displayname._setValue("");
					IqpChkStoreTask.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpChkStoreTask.manager_id._getValue();
					IqpChkStoreTask.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpChkStoreTask.manager_br_id._setValue("");
					IqpChkStoreTask.manager_br_id_displayname._setValue("");
					IqpChkStoreTask.manager_br_id_displayname._obj._renderReadonly(false);
					IqpChkStoreTask.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpChkStoreTask.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		IqpChkStoreTask.manager_br_id._setValue(data.organno._getValue());
		IqpChkStoreTask.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function doSave(){
		var form = document.getElementById("submitForm");
		if(IqpChkStoreTask._checkAll()){
           return;
		}
		IqpChkStoreTask._toForm(form);
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("noteam" == flag){//客户经理为非团队成员时
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action,callback);
	}	

	function doSubWF(){ 
		var form = document.getElementById("submitForm");
		var result = IqpChkStoreTask._checkAll();
		if(!result){
			return;
		}

		var task_id = IqpChkStoreTask.task_id._getValue();
		var cus_id = IqpChkStoreTask.cus_id._getValue();
		var cus_name = IqpChkStoreTask.cus_id_displayname._getValue();
		var approve_status = IqpChkStoreTask.approve_status._getValue();
		WfiJoin.table_name._setValue("IqpChkStoreTask");
		WfiJoin.pk_col._setValue("task_id");
		WfiJoin.pk_value._setValue(task_id);
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("033");
		initWFSubmit(false);
	}
    function doSave(){
        var form = document.getElementById("submitForm");
	    IqpChkStoreTask._toForm(form);
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
    };
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpChkStoreTaskRecord.do" method="POST">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="核/巡待办任务基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpChkStoreTaskGroup" maxColumn="2" title="核/巡库待办任务信息">
			<emp:text id="IqpChkStoreTask.task_id" label="任务执行编号" maxlength="32" required="true" readonly="true" />
			<emp:select id="IqpChkStoreTask.task_set_type" label="任务维度" required="false" dictname="STD_ZB_INSURE_MODE" readonly="true"/>
			<emp:text id="IqpChkStoreTask.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.oversee_agr_no" label="监管协议编号" maxlength="32" required="false" readonly="true"/>
			<emp:text id="IqpChkStoreTask.task_request_time" label="要求完成时间" maxlength="10" required="false" readonly="true"/>
			<emp:date id="IqpChkStoreTask.act_complete_time" label="实际完成时间" readonly="true" />
			<emp:select id="IqpChkStoreTask.prc_status" label="处理状态" required="false" readonly="true" dictname="STD_TASK_PRC_STATUS" />
			<emp:textarea id="IqpChkStoreTask.remarks" label="备注" maxlength="250" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">
			<emp:pop id="IqpChkStoreTask.manager_id_displayname" label="责任人 " url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" readonly="true" buttonLabel="选择"/>
			<emp:pop id="IqpChkStoreTask.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" hidden="true" buttonLabel="选择" readonly="true"/>
			<emp:text id="IqpChkStoreTask.input_id_displayname" label="登记人" required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpChkStoreTask.input_br_id_displayname" label="登记机构" required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpChkStoreTask.input_date" label="登记日期" maxlength="10" required="false" readonly="true" />
			<emp:select id="IqpChkStoreTask.approve_status" label="审批状态" required="false" readonly="true" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpChkStoreTask.input_id" label="登记人" maxlength="20" required="false" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="IqpChkStoreTask.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true"/>
			<emp:text id="IqpChkStoreTask.manager_id" label="责任人 " maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpChkStoreTask.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<br>
		<div align="center">
			<br>
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="subWF" label="放入流程" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		
		</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>

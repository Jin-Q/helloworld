<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kcoll = (KeyedCollection)context.getDataElement("PspBatchTaskRel");
	context.put("major_task_id",kcoll.getDataValue("major_task_id").toString());
	context.put("manager_id",kcoll.getDataValue("manager_id").toString());
	context.put("manager_br_id",kcoll.getDataValue("manager_br_id").toString());
	context.put("batch_task_type",kcoll.getDataValue("batch_task_type").toString());
	String batch_task_type="";
	if(kcoll.containsKey("batch_task_type")){
	 	batch_task_type = kcoll.getDataValue("batch_task_type").toString();
	 }
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryPspBatchTaskRelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//返回主管客户经理	
	function setconId(data){
		PspBatchTaskRel.manager_id._setValue(data.actorno._getValue());
		PspBatchTaskRel.manager_id_displayname._setValue(data.actorname._getValue());
		PspBatchTaskRel.manager_br_id._setValue(data.orgid._getValue());
		PspBatchTaskRel.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
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
					PspBatchTaskRel.manager_br_id._setValue(jsonstr.org);
					PspBatchTaskRel.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					PspBatchTaskRel.manager_br_id._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspBatchTaskRel.manager_id._getValue();
					PspBatchTaskRel.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspBatchTaskRel.manager_br_id._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._setValue("");
					PspBatchTaskRel.manager_br_id_displayname._obj._renderReadonly(false);
					PspBatchTaskRel.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspBatchTaskRel.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	
	//返回主管机构
	function getOrganName(data){
		PspBatchTaskRel.manager_br_id._setValue(data.organno._getValue());
		PspBatchTaskRel.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function doSave(){
		var form = document.getElementById("submitForm");
		if(!PspBatchTaskRel._checkAll()){
			return;
		}else{
			PspBatchTaskRel._toForm(form);
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag =="success"){
						alert("保存成功!");
					}else{
						alert("保存失败!");
					}
				}
			};
			var handleFailure = function(o) {
				alert("异步请求出错！");	
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action,callback,postData);
		}
	};

	//提交流程
	function doSubmitWF(){
		var task_id = PspBatchTaskRel.major_task_id._getValue();
		if (task_id == null) {
			alert('请先选择一条记录！');
			return;
		}
		
		var point_manager_id = PspBatchTaskRel.manager_id._getValue();
		var input_id = "${context.currentUserId}";
		if(point_manager_id != input_id){
			alert("当前用户不是主管客户经理!");
			return;
		}		
		var task_type = "09";
		var app_type = "059";
		var approve_status = PspBatchTaskRel.approve_status._getValue();
		WfiJoin.table_name._setValue("PspCheckTask");
		WfiJoin.pk_col._setValue("task_id");
		WfiJoin.pk_value._setValue(task_id);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue(app_type);  //流程申请类型，对应字典项[ZB_BIZ_CATE]
		WfiJoin.cus_id._setValue("");//客户码
		WfiJoin.cus_name._setValue("");//客户名称
		WfiJoin.prd_name._setValue("贷后检查任务审批");//产品名称
		initWFSubmit(false);
	};
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="savePspBatchTask.do" method="POST">
		<emp:tabGroup mainTab="batch_task_tab" id="mainTab" >
		<emp:tab label="贷后批量任务" id="batch_task_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="PspBatchTaskRelGroup" title="贷后批量任务" maxColumn="2">
			<emp:text id="PspBatchTaskRel.major_task_id" label="主任务编号" maxlength="40" required="true"  readonly="true"/>
			<emp:select id="PspBatchTaskRel.batch_task_type" label="批量任务类型" dictname="STD_BATCH_TASK_TYPE" required="true"  readonly="true"/>
			<emp:select id="PspBatchTaskRel.task_type" label="任务类型" dictname="STD_ZB_TASK_TYPE" required="true" defvalue="09" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_id_displayname" label="主管客户经理" maxlength="40" required="true" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_br_id_displayname" label="主管机构" required="true" readonly="true"/>
			<emp:date id="PspBatchTaskRel.task_create_date" label="批量任务起始日" required="false" readonly="true" hidden="true"/>
			<emp:date id="PspBatchTaskRel.task_request_time" label="批量任务完成日" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspBatchTaskRel.manager_id" label="主管客户经理" maxlength="40" hidden="true" readonly="true"/>
			<emp:text id="PspBatchTaskRel.manager_br_id" label="主管机构" maxlength="20" hidden="true" readonly="true"/>
			<emp:text id="PspBatchTaskRel.approve_status" label="审批状态" maxlength="20" hidden="true" readonly="true"/>
		</emp:gridLayout>
		<%if(batch_task_type.equals("01")){ %>
		<emp:gridLayout id="batch_task_tab" maxColumn="2" title="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见">
			<emp:date id="PspBatchTaskRel.check_time" label="批量任务检查时间" required="true" />
			<emp:textarea id="PspBatchTaskRel.check_view" label="意见" maxlength="1000" required="true" colSpan="2"/>
		</emp:gridLayout>
		<%}else if(batch_task_type.equals("02")){ %>
		<emp:gridLayout id="batch_task_tab" maxColumn="2" title=" ">
			<emp:date id="PspBatchTaskRel.check_time" label="批量任务检查时间" required="true" />
			<emp:textarea id="PspBatchTaskRel.check_view" label="意见" maxlength="1000" required="false" hidden="true"/>
		</emp:gridLayout>
		<%} %>
		</emp:tab>
		<emp:tab label="贷后子任务明细" id="sub_task_tab" initial="false" needFlush="true" url="queryPspBatchTaskList.do?major_task_id=${context.major_task_id}&manager_id=${context.manager_id}&batch_task_type=${context.batch_task_type}&op=update" />
	    <%if((kcoll.getDataValue("batch_task_type").toString()).equals("01")) {%>
	    <emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcarandhouse.raq&task_id=${context.major_task_id}"/>
	    <%}else{ %>
	    <emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportpersonloan100.raq&task_id=${context.major_task_id}"/>
	    <%} %>
	   </emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="save" label="保存"/>
			<emp:button id="submitWF" label="提交"/>
			<emp:button id="return" label="返回列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>


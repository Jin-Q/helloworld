<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspBatchTaskRel._toForm(form);
		PspBatchTaskList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PspBatchTaskRelGroup.reset();
	};
	
	/**function returnCus(data){
		PspBatchTaskRel.cus_id._setValue(data.cus_id._getValue());
		PspBatchTaskRel.cus_id_displayname._setValue(data.cus_name._getValue());
	};**/
	
	function setconId(data){
		PspBatchTaskRel.manager_id._setValue(data.actorno._getValue());
		PspBatchTaskRel.manager_id_displayname._setValue(data.actorname._getValue());
	};
	
	function getOrgID(data){
		PspBatchTaskRel.manager_br_id._setValue(data.organno._getValue());
		PspBatchTaskRel.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function doAddPspBatchTask(){
		var url = '<emp:url action="getPreparePspBatchTaskRelPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function doViewPspBatchTask(){
		var data = PspBatchTaskList._obj.getSelectedData();
		if(data.length == 0){
			alert("请选择批量任务项！");
			return;
		}else{
			var task_id = PspBatchTaskList._obj.getParamValue(['task_id']);
			var url = '<emp:url action="getPspBatchTaskRelViewPage.do"/>&task_id='+task_id;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};
	function doUpdatePspBatchTask(){
		var data = PspBatchTaskList._obj.getSelectedData();
		if(data.length == 0){
			alert("请选择批量任务项！");
			return;
		}else{
			var point_manager_id = PspBatchTaskList._obj.getParamValue('manager_id');
			var input_id = "${context.currentUserId}";
			if(point_manager_id != input_id){
				alert("当前用户不是主管客户经理!");
				return;
			}
			
			var approve_status = PspBatchTaskList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var task_id = PspBatchTaskList._obj.getParamValue(['task_id']);
				var url = '<emp:url action="getPspBatchTaskRelUpdatePage.do"/>&task_id='+task_id;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的批量任务不能进行修改操作！");
			}
		}
	};
	function doDeletePspBatchTask(){
		var data = PspBatchTaskList._obj.getSelectedData();
		if(data.length == 0){
			alert("请选择批量任务项！");
			return false;
		}else {
			var point_manager_id = PspBatchTaskList._obj.getParamValue('manager_id');
			var input_id = "${context.currentUserId}";
			if(point_manager_id != input_id){
				alert("当前用户不是主管客户经理!");
				return;
			}
			var approve_status = PspBatchTaskList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("是否确认删除该批量任务？")){
					var task_id = PspBatchTaskList._obj.getParamValue(['task_id']);
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
								var url = '<emp:url action="queryPspBatchTaskRelList.do"/>';
								url = EMPTools.encodeURI(url);
								window.location=url;
							}else {
								alert("删除失败!");
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
					var url = '<emp:url action="deletePspBatchTaskRecord.do"/>&task_id='+task_id;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
				}
			}else{
				alert("只有状态为【待发起】的批量任务才可以进行删除！");
			}
		}
	};
	
	//提交流程
	function doSubmitWF(){
		var task_id = PspBatchTaskList._obj.getParamValue(['task_id']);
		if (task_id == null) {
			alert('请先选择一条记录！');
			return;
		}
		
		var point_manager_id = PspBatchTaskList._obj.getParamValue('manager_id');
		var input_id = "${context.currentUserId}";
		if(point_manager_id != input_id){
			alert("当前用户不是主管客户经理!");
			return;
		}		
		var task_type = "09";
		var app_type = "059";
		var approve_status = PspBatchTaskList._obj.getParamValue(['approve_status']);
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
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="PspBatchTaskRelGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspBatchTaskRel.task_id" label="任务编号"  />
			<emp:select id="PspBatchTaskRel.approve_status" label="审批状态" dictname="WF_APP_STATUS"/> 
			<emp:pop id="PspBatchTaskRel.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspBatchTaskRel.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择"/>
			<emp:datespace id="PspBatchTaskRel.task_create_date" label="任务生成日期" />
			<emp:datespace id="PspBatchTaskRel.task_request_time" label="要求完成时间" />
			<emp:text id="PspBatchTaskRel.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="PspBatchTaskRel.manager_id" label="主管客户经理" hidden="true"/>
			<emp:text id="PspBatchTaskRel.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<br>
	<div align="left">
		<emp:button id="addPspBatchTask" label="新增批量任务" op="add" />
		<emp:button id="updatePspBatchTask" label="修改批量任务" op="update"/>
		<emp:button id="deletePspBatchTask" label="删除批量任务" op="remove"/>
		<emp:button id="viewPspBatchTask" label="查看批量任务" op="view"/>
		<emp:button id="submitWF" label="提交" op="submit"/>
	</div>
	<emp:table icollName="PspBatchTaskList" pageMode="true" url="pagePspBatchTaskRelQuery.do">
		<emp:text id="task_id" label="主任务编号"  />
		<emp:text id="qnt" label="贷款总笔数"/>
		<emp:text id="loan_totl_amt" label="贷款总金额"  dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款总余额"  dataType="Currency"/>
		<emp:text id="task_create_date" label="任务生成日期"  />
		<emp:text id="task_request_time" label="要求完成时间"  />
		<emp:text id="batch_task_type" label="批量任务类型"  dictname="STD_BATCH_TASK_TYPE"/>
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_id" label="主管客户经理"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="主管机构" />
		<emp:text id="manager_br_id" label="主管机构" hidden="true"/>	
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
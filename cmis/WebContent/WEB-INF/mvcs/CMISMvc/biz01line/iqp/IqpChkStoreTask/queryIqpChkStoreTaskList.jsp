<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpChkStoreTask._toForm(form);
		IqpChkStoreTaskList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpChkStoreTaskPage(){
		var paramStr = IqpChkStoreTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var approve_status = IqpChkStoreTaskList._obj.getParamValue(['approve_status']);
			if(approve_status=='000'||approve_status=='991'||approve_status=='992'||approve_status=='993'){
				var prc_status = IqpChkStoreTaskList._obj.getParamValue(['prc_status']);
				if(prc_status=='2'){
					alert("该任务已处理完成！");
					return;
				}else{
					var url = '<emp:url action="getIqpChkStoreTaskUpdatePage.do"/>?'+paramStr+"&op=update";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}else{
				alert("该任务已在审批中或已审批结束！");
				return;
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpChkStoreTask() {
		var paramStr = IqpChkStoreTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpChkStoreTaskViewPage.do"/>?'+paramStr+"&op=view&showButton=Y";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpChkStoreTaskPage() {
		var url = '<emp:url action="getIqpChkStoreTaskAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpChkStoreTask() {
		var paramStr = IqpChkStoreTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var approve_status = IqpChkStoreTaskList._obj.getParamValue(['approve_status']);
			if (approve_status == "000"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o){
						EMPTools.unmask();
						if(o.responseText !== undefined){
							try{
								var jsonstr = eval("("+o.responseText+")");
							}catch(e) {
								alert("删除失败!");
								return;
							}
							var flag=jsonstr.flag;	
							if(flag=="success"){
								alert('删除成功！');
								window.location.reload();				
							}
						}
					};
					var handleFailure = function(o){ 
						alert("删除失败，请联系管理员");
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}; 
					var url = '<emp:url action="deleteIqpChkStoreTaskRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert("只有状态为'待发起'的数据才能删除！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpChkStoreTaskGroup.reset();
	};

	function returnCus(data){
		IqpChkStoreTask.cus_id._setValue(data.cus_id._getValue());
		IqpChkStoreTask.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpChkStoreTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="IqpChkStoreTask.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus&cusTypCondition=belg_line in ('BL100','BL200') and cus_status='20'" />
			<emp:select id="IqpChkStoreTask.task_set_type" label="任务维度" dictname="STD_ZB_INSURE_MODE"/>
			<emp:select id="IqpChkStoreTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpChkStoreTask.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getUpdateIqpChkStoreTaskPage" label="处理" op="update"/>
		<emp:button id="viewIqpChkStoreTask" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpChkStoreTaskList" pageMode="true" url="pageIqpChkStoreTaskQuery.do">
		<emp:text id="task_id" label="任务执行编号" hidden="true"/>
		<emp:text id="task_set_type" label="任务维度" dictname="STD_ZB_INSURE_MODE"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="oversee_agr_no" label="监管协议编号" />
		<emp:text id="task_request_time" label="要求完成时间" />
		<emp:text id="act_complete_time" label="实际完成时间" />
		<emp:text id="manager_id_displayname" label="责任人 " />
		<emp:text id="manager_id" label="责任人 " hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="prc_status" label="处理状态" dictname="STD_TASK_PRC_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
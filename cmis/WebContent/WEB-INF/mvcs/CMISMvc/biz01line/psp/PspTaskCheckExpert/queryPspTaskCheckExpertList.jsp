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
		PspTaskCheckExpert._toForm(form);
		PspTaskCheckExpertList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspTaskCheckExpertPage() {
		var paramStr = PspTaskCheckExpertList._obj.getParamStr(['serno','task_create_mode']);
		if (paramStr != null) {
			var task_status = PspTaskCheckExpertList._obj.getParamValue(['task_status']);
			if("01"==task_status){
				alert("该条记录已经生成过检查任务，不能修改！");
				return;
			}
			var url = '<emp:url action="getPspTaskCheckExpertUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspTaskCheckExpert() {
		var paramStr = PspTaskCheckExpertList._obj.getParamStr(['serno','task_create_mode']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspTaskCheckExpertViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspTaskCheckExpertPage() {
		var url = '<emp:url action="getPspTaskCheckExpertAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspTaskCheckExpert() {
		var paramStr = PspTaskCheckExpertList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var task_status = PspTaskCheckExpertList._obj.getParamValue(['task_status']);
			if("01"==task_status){
				alert("该条记录已经生成过检查任务，不能删除！");
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已成功删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deletePspTaskCheckExpertRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspTaskCheckExpertGroup.reset();
	};

	function doCreatePspTaskCheckExpert(){
		var paramStr = PspTaskCheckExpertList._obj.getParamStr(['serno','task_create_mode']);
		if (paramStr != null) {
			var task_status = PspTaskCheckExpertList._obj.getParamValue(['task_status']);
			if("01"==task_status){
				alert("该条记录已经生成过检查任务，不能重复生成！");
				return;
			}
			if(confirm("是否确认要生成检查任务？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已成功生成检查任务！");
							window.location.reload();
						}else{
							alert("生成检查任务失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="createPspTaskCheckExpertRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspTaskCheckExpertGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspTaskCheckExpert.serno" label="任务编号" />
			<emp:text id="PspTaskCheckExpert.task_name" label="任务名称" />
			<emp:select id="PspTaskCheckExpert.task_create_mode" label="任务生成方式" dictname="STD_ZB_TASK_CREATE" />
			<emp:date id="PspTaskCheckExpert.need_finish_date" label="要求完成时间" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspTaskCheckExpertPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspTaskCheckExpertPage" label="修改" op="update"/>
		<emp:button id="deletePspTaskCheckExpert" label="删除" op="remove"/>
		<emp:button id="viewPspTaskCheckExpert" label="查看" op="view"/>
		<emp:button id="createPspTaskCheckExpert" label="生成检查任务" op="createTask"  mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>

	<emp:table icollName="PspTaskCheckExpertList" pageMode="true" url="pagePspTaskCheckExpertQuery.do">
		<emp:text id="serno" label="任务编号" />
		<emp:text id="task_name" label="任务名称" />
		<emp:text id="task_create_mode" label="任务生成方式" dictname="STD_ZB_TASK_CREATE" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="need_finish_date" label="要求完成时间" />
		<emp:text id="belg_manager_id_displayname" label="主管客户经理" />
		<emp:text id="belg_branch_displayname" label="主管机构" />
		<emp:text id="belg_manager_id" label="主管客户客户经理" hidden="true"/>
		<emp:text id="belg_branch" label="主管机构" hidden="true"/>
		<emp:text id="task_status" label="任务状态" dictname="STD_ZB_TASK_CHECK"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
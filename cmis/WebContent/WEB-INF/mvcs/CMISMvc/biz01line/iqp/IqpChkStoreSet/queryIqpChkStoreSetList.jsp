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
		IqpChkStoreSet._toForm(form);
		IqpChkStoreSetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpChkStoreSetPage() {
		var paramStr = IqpChkStoreSetList._obj.getParamStr(['task_set_id']);
		var is_task = IqpChkStoreSetList._obj.getParamValue(['is_task']);
		if (paramStr != null) {
			if(is_task == "2" || is_task == ""){
				var url = '<emp:url action="getIqpChkStoreSetUpdatePage.do"/>?'+paramStr+"&op=update";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert('已生成任务，不能修改！');
			}
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpChkStoreSet() {
		var paramStr = IqpChkStoreSetList._obj.getParamStr(['task_set_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpChkStoreSetViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpChkStoreSetPage() {
		var url = '<emp:url action="getIqpChkStoreSetAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteIqpChkStoreSet(){
		var paramStr = IqpChkStoreSetList._obj.getParamStr(['task_set_id']);
		var is_task = IqpChkStoreSetList._obj.getParamValue(['is_task']);
		if(paramStr != null){
			if(confirm("是否确认要删除？")){
				if(is_task == "2" || is_task == ""){
					var handleSuccess = function(o){
						if(o.responseText !== undefined){
							try{
								var jsonstr = eval("("+o.responseText+")");
							}catch(e){
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("删除成功！");
								window.location.reload();
							}else {
								alert("删除失败！");
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
					var url = '<emp:url action="deleteIqpChkStoreSetRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				}else{
					alert('已生成任务，不能删除！');
				}
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpChkStoreSetGroup.reset();
	};

	function returnCus(data){
		IqpChkStoreSet.cus_id._setValue(data.cus_id._getValue());
		IqpChkStoreSet.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpChkStoreSetGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="IqpChkStoreSet.cus_id_displayname" label="客户码" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus&cusTypCondition=belg_line in ('BL100','BL200') and cus_status='20'" />
			<emp:select id="IqpChkStoreSet.task_set_type" label="任务维度" dictname="STD_ZB_INSURE_MODE"/>
			<emp:text id="IqpChkStoreSet.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpChkStoreSetPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpChkStoreSetPage" label="修改" op="update"/>
		<emp:button id="deleteIqpChkStoreSet" label="删除" op="remove"/>
		<emp:button id="viewIqpChkStoreSet" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpChkStoreSetList" pageMode="true" url="pageIqpChkStoreSetQuery.do">
		<emp:text id="task_set_id" label="任务编号" hidden="true"/>
		<emp:select id="task_set_type" label="任务维度" dictname="STD_ZB_INSURE_MODE"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="task_exe_user" label="任务执行人" />
		<emp:text id="task_request_time" label="要求完成时间" />
		<emp:select id="is_task" label="是否生成任务" dictname="STD_ZX_YES_NO"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
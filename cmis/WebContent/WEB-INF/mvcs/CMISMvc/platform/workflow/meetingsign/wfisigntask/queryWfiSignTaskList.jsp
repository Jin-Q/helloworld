<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddWfiSignTaskPage(){
		var url = '<emp:url action="getWfiSignTaskAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiSignTask(){		
		var paramStr = WfiSignTaskList._obj.getParamStr(['st_task_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiSignTaskRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateWfiSignTaskPage(){
		var paramStr = WfiSignTaskList._obj.getParamStr(['st_task_id']);
		if (paramStr != null) {
			var status = WfiSignTaskList._obj.getSelectedData();
			var task_status = status[0].st_task_status._getValue();
			if(task_status==217||task_status==214||task_status==216){
				//217为会议结束在数据字典中的enname
					alert('会议已经结束，不能进行修改');
			}else{
				var url = '<emp:url action="getWfiSignTaskUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiSignTask(){
		var paramStr = WfiSignTaskList._obj.getParamStr(['st_task_id']);
		if (paramStr != null) {
			var url = '<emp:url action="queryWfiSignTaskDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiSignTask._toForm(form);
		WfiSignTaskList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.WfiSignTaskGroup.reset();
	};

	function doPrint(){
		var data = WfiSignTaskList._obj.getSelectedData();
		if (data[0] == null) {
			alert("请选择一条记录！");
			return;
		}
		var serno = data[0].serno._getValue();
    	var url = '<emp:url action="PrintReport4WfiSignTask.do"/>&serno='+serno;
    	url = EMPTools.encodeURI(url);
    	EMPTools.mask();
    	var handleSuccess = function(o){ 
    		EMPTools.unmask();
    		if(o.responseText !== undefined) {
    			try {
    					var jsonstr = eval("("+o.responseText+")");
    				} catch(e) {
    					alert("打印失败!");
    					return;
    				}
    				var flag=jsonstr.flag;					
    				if(flag=="true"){
    					doreport4wfisingn();							
    				}else{
    					alert("只有状态为【会议结束】的申请才能打印!");		
    				}
    		}	
    	};
    	var handleFailure = function(o){ 
    		alert("打印失败，请联系管理员");
    	};
    	var callback = {
    		success:handleSuccess,
    		failure:handleFailure
    	}; 
    	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	
	function doreport4wfisingn(){
		var data = WfiSignTaskList._obj.getSelectedData();
		var st_result = data[0].st_result._getValue() ;
		var st_task_id = data[0].st_task_id._getValue();
		var Sys_ReportName = "";
		Sys_ReportName = "hqjy";
		var url = '<emp:url action="printConfirmTable.do"/>&st_task_id='+st_task_id+'&Sys_ReportName='+Sys_ReportName;
      		window.location = url;
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiSignTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WfiSignTask.serno" label="业务流水号" />
			<emp:select id="WfiSignTask.biz_type" label="业务类型" dictname="ZB_BIZ_CATE" />
			<emp:select id="WfiSignTask.st_result" label="会议结果" dictname="WF_SIGN_RESULT" />
			<emp:text id="WfiSignTask.st_task_name" label="任务名称" hidden="true" />
			<emp:select id="WfiSignTask.is_end" label="是否已办结" dictname="SYS_YES_NO" colSpan="2" defvalue="2" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getUpdateWfiSignTaskPage" label="处理" op="deal"/>
		<emp:button id="viewWfiSignTask" label="查看" op="view"/>
	</div>
	<emp:table icollName="WfiSignTaskList" pageMode="true" url="pageWfiSignTaskQuery.do">
		<emp:text id="st_task_id" label="会签任务ID" hidden="true" />
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="biz_type" label="业务类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="st_task_name" label="任务名称" />
		<emp:text id="st_exe_user" label="会议安排人" hidden="true" />
		<emp:text id="st_exe_user_displayname" label="会议安排人"  />
		<emp:text id="st_exe_org" label="执行机构" hidden="true"/>
		<emp:text id="st_exe_org_displayname" label="执行机构" />
		<emp:text id="st_task_status" label="会议状态" dictname="WF_SIGN_STATUS" />
		<emp:text id="st_start_time" label="开始时间" />
		<emp:text id="st_end_time" label="结束时间" />
		<emp:text id="st_result" label="会签结果" dictname="WF_SIGN_RESULT" />
	</emp:table>
</body>
</html>
</emp:page>
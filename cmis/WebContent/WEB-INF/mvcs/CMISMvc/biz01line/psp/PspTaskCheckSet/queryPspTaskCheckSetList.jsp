<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String check_mode = "";
	if(context.containsKey("check_mode")){
		check_mode = (String)context.getDataValue("check_mode");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspTaskCheckSet._toForm(form);
		PspTaskCheckSetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspTaskCheckSetPage() {
		var paramStr = PspTaskCheckSetList._obj.getParamStr(['serno','check_mode']);
		if (paramStr != null) {
			var status = PspTaskCheckSetList._obj.getParamStr(['task_status']);
			if(status!="task_status=00"){
				alert("非登记状态的记录不允许进行修改操作！");
				return;
			}
			var url = '<emp:url action="getPspTaskCheckSetUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspTaskCheckSet() {
		var paramStr = PspTaskCheckSetList._obj.getParamStr(['serno','check_mode']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspTaskCheckSetViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspTaskCheckSetPage() {
		var url = '<emp:url action="getPspTaskCheckSetAddPage.do"/>?check_mode=<%=check_mode%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspTaskCheckSet() {
		var paramStr = PspTaskCheckSetList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = PspTaskCheckSetList._obj.getParamStr(['task_status']);
			if(status=="task_status=00"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("删除失败！");
								return;
							}
							var flag = jsonstr.flag;
							if(flag=='success'){	
								alert("已成功删除！");
								window.location.reload();
							}else{
								alert("删除失败");
							}   
						}	
					};
					var handleFailure = function(o) {
						alert("删除失败!");
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deletePspTaskCheckSetRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert('非登记状态的记录不能进行删除操作！');
			}
		}else {
			alert('请先选择一条记录！');
		}
		
	};
	
	function doReset(){
		page.dataGroups.PspTaskCheckSetGroup.reset();
	};
	//启用
	function doStartPspTaskCheckSet(){
		var paramStr = PspTaskCheckSetList._obj.getParamStr(['serno','check_mode','cus_type']);
		var task_status = PspTaskCheckSetList._obj.getParamStr(['task_status']);
		if(task_status=="task_status=01"){
			alert("此任务已经处于启用状态，不能重复进行启用操作！");
			return;
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("启用失败！");
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){	
					alert("已成功启用！");
					window.location.reload();
				}else if(flag=='exist'){
					alert("该客户类型存在已启用的的首次检查任务！");
				}else{
					alert("启用失败");
				}   
			}	
		};
		var handleFailure = function(o) {
			alert("启用失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="startOrStopTsk.do"/>?'+paramStr+'&brands=start';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	//停用
	function doStopPspTaskCheckSet(){
		var paramStr = PspTaskCheckSetList._obj.getParamStr(['serno']);
		var task_status = PspTaskCheckSetList._obj.getParamStr(['task_status']);
		if(task_status=="task_status=02" || task_status=="task_status=00"){
			alert("启用状态的任务，才能进行停用操作！");
			return;
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("停用失败！");
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){	
					alert("已成功停用！");
					window.location.reload();
				}else{
					alert("停用失败");
				}   
			}	
		};
		var handleFailure = function(o) {
			alert("停用失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="startOrStopTsk.do"/>?'+paramStr+'&brands=stop';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspTaskCheckSetGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspTaskCheckSet.serno" label="任务编号" />
			<emp:text id="PspTaskCheckSet.task_name" label="任务名称" />
			<emp:select id="PspTaskCheckSet.task_status" label="任务状态" dictname="STD_ZB_TASK_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspTaskCheckSetPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspTaskCheckSetPage" label="修改" op="update"/>
		<emp:button id="deletePspTaskCheckSet" label="删除" op="remove"/>
		<emp:button id="viewPspTaskCheckSet" label="查看" op="view"/>
		<emp:button id="startPspTaskCheckSet" label="启用" op="start"/>
		<emp:button id="stopPspTaskCheckSet" label="停用" op="stop"/>
	</div>

	<emp:table icollName="PspTaskCheckSetList" pageMode="true" url="pagePspTaskCheckSetQuery.do?check_mode=${context.check_mode}">
		<emp:text id="serno" label="任务编号" />
		<emp:text id="task_name" label="任务名称" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_PSP_CUS_TYPE" />
		<emp:text id="check_mode" label="检查方式" dictname="STD_ZB_CHECK_TYPE" />
		<%if(check_mode.equals("00")){%>
		<emp:text id="finish_term" label="完成期限(日)" />
		<%}%>
		<emp:text id="task_status" label="任务状态" dictname="STD_ZB_TASK_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
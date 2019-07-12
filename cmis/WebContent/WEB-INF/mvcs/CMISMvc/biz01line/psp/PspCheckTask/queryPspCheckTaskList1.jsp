<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kColl = (KeyedCollection)context.getDataElement("PspCheckTask");
	String check_type = "";//检查类型
	if(kColl.containsKey("check_type")){
		check_type = (String)kColl.getDataValue("check_type");
	}
	String task_type = "";//任务类型
	if(kColl.containsKey("task_type")){
		task_type = (String)kColl.getDataValue("task_type");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspCheckTask._toForm(form);
		PspCheckTaskList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspCheckTaskPage() {
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var point_manager_id = PspCheckTaskList._obj.getParamValue('manager_id');
			var input_id = "${context.currentUserId}";
			if(point_manager_id != input_id){
				alert("当前用户不是主管客户经理!");
				return;
			}
			
			var approve_status = PspCheckTaskList._obj.getParamValue(['approve_status']);
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getPspCheckTaskUpdatePage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspCheckTask() {
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspCheckTaskPage() {
		var url = '<emp:url action="getPspCheckTaskAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspCheckTask() {
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspCheckTaskRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCheckTaskGroup.reset();
	};

	function returnCus(data){
		PspCheckTask.cus_id._setValue(data.cus_id._getValue());
		PspCheckTask.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	function returnCusGrp(data){
		PspCheckTask.grp_no._setValue(data.grp_no._getValue());
		PspCheckTask.grp_no_displayname._setValue(data.grp_name._getValue());
	}
	function returnCoopAgrNo(data){
		PspCheckTask.cus_id._setValue(data.cus_id._getValue());
		PspCheckTask.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
	}
	function setconId(data){
		PspCheckTask.manager_id._setValue(data.actorno._getValue());
		PspCheckTask.manager_id_displayname._setValue(data.actorname._getValue());
	};
	
	function getOrgID(data){
		PspCheckTask.manager_br_id._setValue(data.organno._getValue());
		PspCheckTask.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doSubwf(){
		var task_id = PspCheckTaskList._obj.getParamValue(['task_id']);
		if (task_id == null) {
			alert('请先选择一条记录！');
			return;
		}
		
		var point_manager_id = PspCheckTaskList._obj.getParamValue('manager_id');
		var input_id = "${context.currentUserId}";
		if(point_manager_id != input_id){
			alert("当前用户不是主管客户经理!");
			return;
		}
		
		var task_type = '<%=task_type%>';
		var cus_id;
		var cus_id_displayname;
		var app_type;//集团客户与其他类型风险拦截不同，所以申请类型特殊处理
		if(task_type=='04'){
			cus_id = PspCheckTaskList._obj.getSelectedData()[0].grp_no._getValue();
			cus_id_displayname = PspCheckTaskList._obj.getSelectedData()[0].grp_no_displayname._getValue();
			app_type = "058";
		}else{
			cus_id = PspCheckTaskList._obj.getSelectedData()[0].cus_id._getValue();
			cus_id_displayname = PspCheckTaskList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			app_type = "059";
		}
		var approve_status = PspCheckTaskList._obj.getParamValue(['approve_status']);
		WfiJoin.table_name._setValue("PspCheckTask");
		WfiJoin.pk_col._setValue("task_id");
		WfiJoin.pk_value._setValue(task_id);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue(app_type);  //流程申请类型，对应字典项[ZB_BIZ_CATE]
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		//WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("贷后检查任务审批");//产品名称
		initWFSubmit(false);
	};

</script>
</head>
<body class="page_content">
	<emp:table icollName="PspCheckTaskList" pageMode="true" url="pagePspCheckTaskQuery.do?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}">
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="qnt" label="业务笔数" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="task_request_time" label="要求完成时间" />
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_id" label="主管客户经理" hidden="true"/>
		<emp:text id="check_type" label="检查类型" dictname="STD_PSP_CHECK_TYPE"/>
		<emp:text id="task_type" label="任务类型" dictname="STD_ZB_TASK_TYPE"/>
	</emp:table>
</body>
</html>
</emp:page>
    
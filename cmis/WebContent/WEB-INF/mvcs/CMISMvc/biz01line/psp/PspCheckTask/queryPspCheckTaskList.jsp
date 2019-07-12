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
	
	function load(){
		<%if("05".equals(task_type)){%>
			 var url="<emp:url action='queryLmtAgrJointPop.do'/>&returnMethod=returnCoopAgrNo&flag=3";
			 PspCheckTask.cus_id_displayname._obj.config.url = EMPTools.encodeURI(url);
		<%}else if("06".equals(task_type)){%>
			 var url="<emp:url action='queryLmtAgrJointPop.do'/>&returnMethod=returnCoopAgrNo&flag=3";
			 PspCheckTask.cus_id_displayname._obj.config.url = EMPTools.encodeURI(url);
		<%}%>
	}; 
	
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
			if(point_manager_id != input_id&&point_manager_id!="OCS01"){
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
			// modified by yangzy 2015/05/04 需求：XD150325024，集中作业扫描岗权限改造 start
			var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?op=view&viewfrom=nohis&'+paramStr;
			// modified by yangzy 2015/05/04 需求：XD150325024，集中作业扫描岗权限改造 end
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

	/*** 影像部分操作按钮begin ***/
	function doImageScan(){
		// modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start 
		var point_manager_id = PspCheckTaskList._obj.getParamValue('manager_id');
		var input_id = "${context.currentUserId}";
		if(point_manager_id != input_id){
			alert("当前用户不是主管客户经理!");
			return;
		}
		// modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end 
		var data = PspCheckTaskList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan26');	//贷后资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = PspCheckTaskList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View27');	//贷后资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};

	function doImagePrint(){
		var data = PspCheckTaskList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = PspCheckTaskList._obj.getParamValue(['task_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = PspCheckTaskList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'POSTLOAN';	//业务品种
		data['prd_stage'] = 'DHTZ' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/**add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
	//无条件否决
    function doReject(){
    	var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
    	if (paramStr != null) {
    		var approve_status = PspCheckTaskList._obj.getSelectedData()[0].approve_status._getValue();
    		//审批状态为待发起，追回，打回可做否决操作
    		if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
    		if(confirm("该操作会对贷后任务进行无条件否决，是否确认继续？")){
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
    						alert("否决成功!");
    						window.location.reload();
        				}else{
    						alert("否决操作异常!"); 
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
    			var url = '<emp:url action="rejectPspCheckTaskRecord.do"/>?'+paramStr;
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    		}  
    		/**add by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造 begin**/
    		//非流程上的否决恢复   
    		}else if(approve_status == "998"){
        		if(confirm("该操作会对贷后任务已经否决的再重新启用，是否确认继续？")){
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
        						alert("启用成功!");
        						window.location.reload();
            				}else if(flag == "warning"){
            					alert("流程上的否决不能再启用!");
            				}else{
        						alert("启用操作异常!"); 
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
        			var url = '<emp:url action="batchPspCheckTaskRecord.do"/>?'+paramStr;
        			url = EMPTools.encodeURI(url);
        			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
        		}     
        		}else{
    			alert("只有状态为【待发起,追回,打回,否决】的贷后任务才可以做否决/启用操作！");
    		}
    	}else {
    		alert('请先选择一条记录！');
    	}
    };
    /**add by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造（否决恢复操作） end**/  
    //任务展期
    function doDefer(){
    	var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var approve_status = PspCheckTaskList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getDeferPspCheckTaskRecordPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'viewTaskInfo','height=280,width=540,top=300,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				alert("只有状态为【待发起,追回,打回】的贷后任务才可以做任务展期操作！");
			}		
		} else {
			alert('请先选择一条记录！');
		}
    };
	/**add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = PspCheckTaskList._obj.getParamValue(['task_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=08&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" onload="load()">
	<form  method="POST" action="#" id="queryForm">
	
	<emp:gridLayout id="PspCheckTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckTask.task_id" label="任务编号"  /> 
			<%if(task_type.equals("04")){ %>
		    <emp:pop id="PspCheckTask.grp_no_displayname" label="集团客户名称" buttonLabel="选择" url="queryCusGrpInfoPopList.do?returnMethod=returnCusGrp" />
		    <emp:pop id="PspCheckTask.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择"/>
			<emp:datespace id="PspCheckTask.task_create_date" label="任务生成日期" />
			<emp:datespace id="PspCheckTask.task_request_time" label="要求完成时间" />
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="PspCheckTask.grp_no" label="集团客户码" hidden="true"/>
		    <emp:text id="PspCheckTask.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="PspCheckTask.manager_id" label="主管客户经理" hidden="true"/>
			
			<%}else if(task_type.equals("05")){ %>
			<emp:pop id="PspCheckTask.cus_id_displayname" label="合作方客户名称" buttonLabel="选择" url="queryLmtAgrJointPop.do?returnMethod=returnCoopAgrNo&flag=3" />
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="PspCheckTask.cus_id" label="合作方客户码" hidden="true"/>
			<%}else if(task_type.equals("06")){ %>
			<emp:pop id="PspCheckTask.cus_id_displayname" label="担保公司客户名称" buttonLabel="选择" url="queryLmtAgrJointPop.do?returnMethod=returnCoopAgrNo&flag=3" />
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="PspCheckTask.cus_id" label="担保公司客户码" hidden="true"/>
			<%}else{ %>
			<emp:pop id="PspCheckTask.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:pop id="PspCheckTask.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择"/>
			<emp:datespace id="PspCheckTask.task_create_date" label="任务生成日期" />
			<emp:datespace id="PspCheckTask.task_request_time" label="要求完成时间" />
			<emp:text id="PspCheckTask.cus_id" label="客户码" hidden="true"/>
			<emp:text id="PspCheckTask.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="PspCheckTask.manager_id" label="主管客户经理" hidden="true"/>
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<%} %>
			
	</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getUpdatePspCheckTaskPage" label="检查" op="update"/>
		<emp:button id="viewPspCheckTask" label="查看" op="view"/>
		<emp:button id="subwf" label="提交" op="submit"/>
		<%if(!task_type.equals("04")){ %>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<%} %>
		<!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin-->
		<!-- modified by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造 begin-->
		<emp:button id="reject" label="否决/启用" op="reject"/>  
		<!-- modified by yezm 2015-7-23 需求编号：XD150625045 贷后管理常规检查任务改造 begin-->    
		<emp:button id="defer" label="任务展期" op="defer"/>
		<!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end-->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="PspCheckTaskList" pageMode="true" url="pagePspCheckTaskQuery.do?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}">
		<emp:text id="task_id" label="任务编号" />
		<%if(task_type.equals("04")){ %>
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />
		<%}else if(task_type.equals("05")){ %>
		<emp:text id="cus_id" label="合作方客户码" />
		<emp:text id="cus_id_displayname" label="合作方客户名称" />
		<%}else if(task_type.equals("06")){ %>
		<emp:text id="cus_id" label="担保公司客户码" />
		<emp:text id="cus_id_displayname" label="担保公司客户名称" />
		<%}else{ %>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<%} %>
		<emp:text id="qnt" label="业务笔数" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="task_request_time" label="要求完成时间" />
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_id" label="主管客户经理" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		
	</emp:table>
	
</body>
</html>
</emp:page>
    
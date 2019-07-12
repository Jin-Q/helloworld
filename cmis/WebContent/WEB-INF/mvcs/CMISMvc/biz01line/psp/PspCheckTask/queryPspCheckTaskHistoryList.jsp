<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspCheckTask._toForm(form);
		PspCheckTaskList._obj.ajaxQuery(null,form);
	};
	
	function doViewPspCheckTask() {
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
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
	
	function setconId(data){
		PspCheckTask.manager_id._setValue(data.actorno._getValue());
		PspCheckTask.manager_id_displayname._setValue(data.actorname._getValue());
	};
	
	function getOrgID(data){
		PspCheckTask.manager_br_id._setValue(data.organno._getValue());
		PspCheckTask.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		var data = PspCheckTaskList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View27');	//贷后资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	
	/**add by lisj 2014年10月29日  增加影像核对功能  begin**/
	function doImageCheck(){
		var data = PspCheckTaskList._obj.getSelectedData();
		if (data != null && data !=0) {
			if( confirm("影像信息将直接归档，请确认!") ){
				ImageAction('Check3134');	//贷后资料归档
			}
		} else {
			alert('请先选择一条记录！');
		}		
	};
	/**add by lisj 2014年10月29日  增加影像核对功能  end**/
	
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
	
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = PspCheckTaskList._obj.getParamStr(['task_id']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("异步调用通讯发生异常！");
						return;
					}
					var flag=jsonstr.flag;
					if(flag =="success"){
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+'&print_type=06';
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4PCTH',param);
					}else{
						alert("清空原表报数据失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("清空原表报数据发生异常，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=06";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
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
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="PspCheckTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckTask.task_id" label="任务编号"  /> 
			<emp:pop id="PspCheckTask.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:pop id="PspCheckTask.manager_id_displayname" label="客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择"/>
			<emp:select id="PspCheckTask.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:datespace id="PspCheckTask.task_create_date" label="任务生成日期" />
			<emp:datespace id="PspCheckTask.task_request_time" label="要求完成时间" />
			
			<emp:text id="PspCheckTask.manager_br_id" label="主管机构" hidden="true"/>
			<emp:text id="PspCheckTask.manager_id" label="主管客户经理" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<%//<emp:button id="getUpdatePspCheckTaskPage" label="检查" op="update"/>%>
		<emp:button id="viewPspCheckTask" label="查看" op="view"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="println" label="封面打印" op="println"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="PspCheckTaskList" pageMode="true" url="pagePspCheckTaskHistoryQuery.do?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}">
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_name" label="集团名称" />
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
    
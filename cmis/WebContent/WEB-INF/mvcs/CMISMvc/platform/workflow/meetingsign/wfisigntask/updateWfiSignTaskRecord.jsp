<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
	<html>
	<head>
	<title>修改页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<style type="text/css">
.text_field_width {
	width: 500px;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
}

.textarea_field_width {
	width: 500px;
	height: 50px;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
}
</style>
	<script type="text/javascript">
	
	function refreshWfiSignVote() {
		WfiSignTask_tabs.tabs.WfiSignVote_tab.refresh();
	};
	//保存会议成员
	function doSave(callbackFn){
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("保存失败,请联系管理员!");
					return;
				}
				var flag = jsonstr.success;
				if(flag=="true"){
					if(typeof(callbackFn)=="function"){
						callbackFn.call(this);
					}else{
						alert("保存成功!");
					}
			    }else {
			     alert("保存失败!");
			  }      
			}
		};
		var handleFailure = function(o){ 
			EMPTools.unmask(); 
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var form = document.getElementById("submitForm");
		WfiSignTask._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);  
		EMPTools.mask();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	/*--user code begin--*/
	function buildMember(data){
		var values="";
		if(data != null) {
			values = data[1];
		} else {
			return;
		}
		var len = values.split(";").length;
		WfiSignTask.st_members._setValue(values);
		WfiSignTask.st_total_count._setValue(len);
		buildLeader(values);
		
	}
	function buildLeader(values){
		var leaderSelect = WfiSignTask.st_leader._obj.element.options;
		for(var n=leaderSelect.length-1;n>=0;n--){ 
			leaderSelect.remove(n); 
		} 
		if(!values)
			values="${context.WfiSignTask.st_members}";
		leaderSelect.add(new Option("-----  无  -----",""));
		var list=values.split(";");
		for(var i=0;i< list.length;i++){
			var v=list[i];
			if(v!="")
				leaderSelect.add(new Option(v, v));
		}
		
	}
	function init(){
		buildLeader();
		WfiSignTask.st_leader._setValue("${context.WfiSignTask.st_leader}");
		var status="${context.WfiSignTask.st_task_status}";
		configButtonAuth(status);
		var task_status = WfiSignTask.st_task_status._getValue();
		if(task_status=='217'){
			WfiSignTask.st_agree_count._obj._renderHidden(false);
			WfiSignTask.st_reject_count._obj._renderHidden(false);
			WfiSignTask.st_noidea_count._obj._renderHidden(false);
		}
	}
	function configButtonAuth(status){
		var configButtons=function(btnObjs){
			for(var i=0;i<btnObjs.length;i++){
				var btn=btnObjs[i].btn;
				var hidden=btnObjs[i].hidden;
				if(hidden){
					btn.style.display="none";
				}else{
					btn.style.display="inline";
				}
			}
		}
		var readOnlyField=function(readOnly){
			WfiSignTask.st_members._obj._renderReadonly(readOnly);
			WfiSignTask.st_leader._obj._renderReadonly(readOnly);
			WfiSignTask.st_advice._obj._renderReadonly(readOnly);
		}
		var hiddenField=function(hidden) {
			WfiSignTask.st_end_time._obj._renderHidden(hidden);
			WfiSignTask.st_advice._obj._renderHidden(hidden);
		}
		var saveBtn=document.getElementById("button_save");
		var beginBtn=document.getElementById("button_begin");
		var finishBtn=document.getElementById("button_finish");
		var rebeginBtn=document.getElementById("button_rebegin");
		var cancelBtn=document.getElementById("button_cancel");
		var dealBtn=document.getElementById("button_deal");
		var buttons=[];
		readOnlyField(true);
		var result=WfiSignTask.st_result._getValue();
		var hiddenDealBtn=true;
		if(result=="110"||result=="111"){
			hiddenDealBtn=false;
		}
		var hiddenBtn=false;
		if(result=="110"||result=="111"){
			hiddenBtn=true;
		}
		switch(status){
			//尚未开始
			case "210":buttons=[{btn:saveBtn,hidden:false},{btn:beginBtn,hidden:false},{btn:finishBtn,hidden:true},{btn:rebeginBtn,hidden:true},{btn:cancelBtn,hidden:false},{btn:dealBtn,hidden:hiddenDealBtn}];
			readOnlyField(false);
			hiddenField(true);
			break;
			//正在进行
			case "212":buttons=[{btn:saveBtn,hidden:true},{btn:beginBtn,hidden:true},{btn:finishBtn,hidden:false},{btn:rebeginBtn,hidden:hiddenBtn},{btn:cancelBtn,hidden:hiddenBtn},{btn:dealBtn,hidden:hiddenDealBtn}];break;
			//投票结束
			case "213":buttons=[{btn:saveBtn,hidden:true},{btn:beginBtn,hidden:true},{btn:finishBtn,hidden:true},{btn:rebeginBtn,hidden:hiddenBtn},{btn:cancelBtn,hidden:hiddenBtn},{btn:dealBtn,hidden:hiddenDealBtn}];break;
			//正在进行
			case "214":buttons=[{btn:saveBtn,hidden:true},{btn:beginBtn,hidden:true},{btn:finishBtn,hidden:true},{btn:rebeginBtn,hidden:hiddenBtn},{btn:cancelBtn,hidden:hiddenBtn},{btn:dealBtn,hidden:hiddenDealBtn}];break;
			//会议重置
			case "216":buttons=[{btn:saveBtn,hidden:true},{btn:beginBtn,hidden:true},{btn:finishBtn,hidden:true},{btn:rebeginBtn,hidden:hiddenBtn},{btn:cancelBtn,hidden:hiddenBtn},{btn:dealBtn,hidden:true}];break;
			//会议结束
			case "217":buttons=[{btn:saveBtn,hidden:true},{btn:beginBtn,hidden:true},{btn:finishBtn,hidden:true},{btn:rebeginBtn,hidden:hiddenBtn},{btn:cancelBtn,hidden:hiddenBtn},{btn:dealBtn,hidden:true}];break;
		}
		configButtons(buttons);
	}
	function doBegin(){
		var totl_count = WfiSignTask.st_total_count._getValue();
		if(parseFloat(totl_count)<3){
			alert('会签人数至少3人！');
			return;
		}
		if(!WfiSignTask._checkAll() || !confirm("会议即将开始是否继续?注意:会议开始后不能变更会议成员,除非会议重开!")){
			return ;
		}
		var callbackFn=function(){
			var url = "<emp:url action='beginSignTask.do'/>&taskId=${context.WfiSignTask.st_task_id}";
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("会议开始失败,请联系管理员!");
						return;
					}
					var flag = jsonstr.success;
					if(flag=="true"){
							alert("会议开始成功!");
							reload();
				    }else if(flag=="noDuty"){
						alert("缺少专审岗人员!"); 
				    }else{
				     		alert("会议开始失败！原因："+flag);
				  	}      
				}
			};
			var handleFailure = function(o){ 
				EMPTools.unmask(); 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			EMPTools.mask();
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
		doSave(callbackFn);
	}
	
	function doSaveBegin() {
		if(!WfiSignTask._checkAll() || !confirm("会议即将开始是否继续?注意:会议开始后不能变更会议成员,除非会议重开!")){
			return ;
		}
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("会议开始失败,请联系管理员!");
					return;
				}
				var flag = jsonstr.success;
				if(flag=="true"){
					alert("会议开始成功!");
					//reload();
					var urln = "<emp:url action='queryWfiSignTaskList.do'/>";
					window.location=urln;
			    }else {
			    	alert("会议开始失败！原因："+flag);
			  }      
			}
		};
		var handleFailure = function(o){ 
			EMPTools.unmask(); 
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var form = document.getElementById("submitForm");
		WfiSignTask._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);  
		EMPTools.mask();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST','beginSignTask.do', callback,postData);
	}
	
 	function reload(){
		var urln = "<emp:url action='getWfiSignTaskUpdatePage.do'/>&st_task_id=${context.WfiSignTask.st_task_id}";
		window.location=urln;
	}
	//结束会议
	function doFinish(){
			if(!confirm("本次会议即将结束是否继续?注意:本次会议结束不会影响会议结果,你可以重新安排会议或者重置会议!")){
				return ;
			}
			var url = "<emp:url action='finishSignTask.do'/>&taskId=${context.WfiSignTask.st_task_id}";
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("会议结束失败,请联系管理员!");
						return;
					}
					var flag = jsonstr.success;
					if(flag=="true"){
							alert("会议结束成功!");
							reload();
				    }else {
				     		alert("会议结束失败!");
				  	}      
				}
			};
			var handleFailure = function(o){ 
				EMPTools.unmask(); 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			EMPTools.mask();
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	//会议重开
	function doRebegin(){
			if(!confirm("本次会议即将结束并重新安排新的会议是否继续?注意:会议重开后可以安排新的会议成员参与!")){
				return ;
			}
			var url = "<emp:url action='rebeginSignTask.do'/>&taskId=${context.WfiSignTask.st_task_id}";
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("会议重开失败,请联系管理员!");
						return;
					}
					var flag = jsonstr.success;
					if(flag=="true"){
							alert("会议重开成功!");
							var taskId=jsonstr.taskId;
							var urln = "<emp:url action='getWfiSignTaskUpdatePage.do'/>&st_task_id="+taskId;
							window.location=urln;
				    }else {
				     		alert("会议重开失败!");
				  	}      
				}
			};
			var handleFailure = function(o){ 
				EMPTools.unmask(); 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			EMPTools.mask();
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	//会议重置
	function doCancel(){
		if(!confirm("会议即将重置是否继续?注意:会议重置后流程将跳回上一节点,你可以到待办事项中重新办理该流程!并把会议信息作废")){
			return ;
		}
			var url = "<emp:url action='cancelSignTask.do'/>&taskId=${context.WfiSignTask.st_task_id}";
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("会议重置失败,请联系管理员!");
						return;
					}
					var flag = jsonstr.success;
					if(flag=="true"){
							alert("会议重置成功,流程已经返回你的待办事项中,系统将返回会议列表页面!");
							doReturn();
				    }else {
				     		alert("会议重置失败!");
				  	}      
				}
			};
			var handleFailure = function(o){ 
				EMPTools.unmask(); 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			EMPTools.mask();
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	//会议处理 结束会议
	function doDeal(){
			if(!confirm("会议即将结束是否继续?注意:会议结束后 系统将会把会议结果和意见汇总到你的待办事项任务中!")){
				return ;
			}
			var url = "<emp:url action='submitSignTask.do'/>&taskId=${context.WfiSignTask.st_task_id}";
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("会议处理失败,请联系管理员!");
						return;
					}
					var flag = jsonstr.success;
					if(flag=="true"){
							alert("会议处理成功!系统将跳转到流程任务处理界面!");
							var instanceid="${context.WfiSignTask.wfi_instance_id}";
							var nodeid="${context.WfiSignTask.wfi_node_id}";
							var urln= "<emp:url action='getInstanceInfo.do'/>&applType=${context.WfiSignTask.biz_type}&instanceId="+instanceid+"&nodeId="+nodeid;
							url = EMPTools.encodeURI(urln);
							window.location=urln;
				    }else {
				     		alert("会议处理失败!");
				  	}      
				}
			};
			var handleFailure = function(o){ 
				EMPTools.unmask(); 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			EMPTools.mask();
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	/*--user code end--*/
	function doReturn() {
		var url = '<emp:url action="queryWfiSignTaskList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
</script>
	</head>
	<body class="page_content" onload="init()">
	<emp:form id="submitForm" action="updateWfiSignTaskRecord.do"
		method="POST">
<emp:tabGroup id="WfiSignTaskTab_tabs" mainTab="task_main_tab">
		<emp:tab id="app_main_tab" label="申请信息"  url="${context.WfiSignTask.wfi_biz_page}" />
		<emp:tab id="task_main_tab" label="会签详细信息">
		
		<emp:gridLayout id="WfiSignTaskGroup" title="会签任务" maxColumn="2">
			<emp:text id="WfiSignTask.st_task_name" label="任务名称" maxlength="60"
				required="false" readonly="true" colSpan="2"
				cssElementClass="text_field_width" />
			<emp:text id="WfiSignTask.st_config" label="会签策略" maxlength="40"
				required="false" hidden="true" />
			<emp:text id="WfiSignTask.st_config_displayname" label="会签策略" 
				required="false" readonly="true" />
			<emp:text id="WfiSignTask.serno" label="业务流水号" maxlength="40"
				required="false" readonly="true" />
			<emp:select id="WfiSignTask.biz_type" label="业务类型" required="false"
				dictname="ZB_BIZ_CATE" readonly="true" />
			<emp:text id="WfiSignTask.st_start_time" label="开始时间" maxlength="10"
				required="false" dataType="Date" readonly="true" />
			<emp:text id="WfiSignTask.st_end_time" label="结束时间" maxlength="10" required="false" dataType="Date" readonly="true" />
			<emp:text id="WfiSignTask.st_exe_user" label="会议安排人" maxlength="10"
				required="false" readonly="true" hidden="true"/>
			<emp:text id="WfiSignTask.st_exe_user_displayname" label="会议安排人" 
				required="false" readonly="true" />
			<emp:text id="WfiSignTask.st_exe_org" label="执行机构" maxlength="10"
				required="false" readonly="true" hidden="true"/>
			<emp:text id="WfiSignTask.st_exe_org_displayname" label="执行机构" 
				required="false" readonly="true" />
			

			<emp:pop id="WfiSignTask.st_members" label="本次会议成员" readonly="true"
				colSpan="2" required="true" url="queryWfiSignTaskUserPop.do"
				reqParams="returnMethod=buildMember&selectType=2&duty=${context.WfiSignTask.st_duty }"
				cssElementClass="text_field_width" />
			<emp:select id="WfiSignTask.st_leader" label="会议牵头人"
				required="true" readonly="true" />

			<emp:text id="WfiSignTask.st_total_count" label="会签人数"
				maxlength="100" required="false" dataType="Int" readonly="true"
				defvalue="0" />
			<emp:text id="WfiSignTask.st_vote_count" label="参与投票人数"
				maxlength="100" required="false" dataType="Int" readonly="true"
				defvalue="0" />
			<emp:text id="WfiSignTask.st_agree_count" label="同意票数"
				maxlength="100" required="false" dataType="Int" readonly="true"
				defvalue="0" hidden='true'/>
			<emp:text id="WfiSignTask.st_reject_count" label="否决票数"
				maxlength="100" required="false" dataType="Int" readonly="true"
				defvalue="0" hidden='true'/>
			<emp:text id="WfiSignTask.st_noidea_count" label="复议票数"
				maxlength="100" required="false" dataType="Int" readonly="true"
				defvalue="0" hidden='true'/>
			<emp:select id="WfiSignTask.st_result" label="会议结果" required="false"
				dictname="WF_SIGN_RESULT" readonly="true"  />
			<emp:select id="WfiSignTask.st_task_status" label="会议状态"
				required="false" dictname="WF_SIGN_STATUS" readonly="true" />
			<emp:textarea id="WfiSignTask.st_advice" label="会议意见"
				maxlength="1000" required="false" colSpan="2"
				cssElementClass="textarea_field_width" hidden="true"/>
				<emp:text id="WfiSignTask.st_task_times" label="会签次数"
				maxlength="40" dataType="Int" readonly="true" hidden="true" />
			<emp:text id="WfiSignTask.st_task_id" label="会签任务ID" maxlength="40"
				hidden="true" required="true" readonly="true" />
			<emp:text id="WfiSignTask.st_task_times" label="会签次数" maxlength="3"
				required="false" dataType="Int" hidden="true" />
			<emp:text id="WfiSignTask.wfi_node_id" label="流程节点ID" maxlength="40"
				required="false" hidden="true" />
			<emp:text id="WfiSignTask.wfi_instance_id" label="流程实例号"
				maxlength="40" required="false" hidden="true" />
			<emp:text id="WfiSignTask.wfi_advice_id" label="审批意见ID"
				maxlength="40" required="false" hidden="true" />
			<emp:text id="WfiSignTask.st_request_day" label="要求办理天数"
				maxlength="10" required="false" dataType="Int" hidden="true" />
			<emp:text id="WfiSignTask.st_duty" label="会签成员岗位" 
				required="true" hidden="true" />
		</emp:gridLayout>
		
			<emp:tabGroup id="WfiSignTask_tabs" mainTab="WfiSignVote_tab">
				<emp:tab id="WfiSignVote_tab" label="会议成员"
					url="queryWfiSignTaskWfiSignVoteList.do"
					reqParams="WfiSignTask.st_task_id=$WfiSignTask.st_task_id;&status=$WfiSignTask.st_task_status;"
					initial="true" needFlush="true"/>
		
			</emp:tabGroup>
		</emp:tab>
		
		<emp:tab id="app_history_tab" label="审批历史" url="getWfApproveHis.do" 
				reqParams="instanceId=${context.WfiSignTask.wfi_instance_id}&nodeId=${context.WfiSignTask.wfi_node_id}" />
	</emp:tabGroup>
	<div align=center>
			<emp:button id="save" label="保存"  /> 
			<emp:button id="begin" label="开始本次会议" />
			<%/* <emp:button id="saveBegin" label="开始本次会议" /> */%>
			<%/* <emp:button id="finish" label="结束本次会议" /> */%>
			<emp:button id="rebegin" label="重开本次会议" /> 
			<emp:button id="deal" label="结束会议" />
			<emp:button id="cancel" label="重置会议" /> 
			<emp:button id="return" label="返回会议列表" />
		</div>
	</emp:form>
	</body>
	</html>
</emp:page>

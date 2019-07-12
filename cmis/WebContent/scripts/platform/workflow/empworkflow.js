/**
 * 流程独立部署,规范化后操作URL
 */
var START_WF_URL = "wfStart.do"; //流程发起
var SAVE_WF_URL = "wfSave.do"; //流程保存
var SUBMIT_WF_URL = "wfSubmit.do"; //流程提交（同意/否决）

var SUBMIT_WF_AUTO_URL = "wfSubmitAuto.do"; //流程提交（同意/否决）


var RETURNBACK_WF_URL = "wfRetrunBack.do"; //流程退回
var CALLBACK_WF_URL = "wfCallBack.do"; //流程打回
var ASSIST_WF_URL = "wfAssist.do"; //流程协办
var CHANGE_WF_URL = "wfChange.do"; //流程转办
var JUMP_WF_URL = "wfJump.do"; //流程跳转
var SIGNIN_WF_URL = "wfSignIn.do"; //流程签收
var SIGNOFF_WF_URL = "wfSignOff.do"; //流程撤销签收
var HANG_WF_URL = "wfHang.do"; //流程挂起
var WAKE_WF_URL = "wfWake.do"; //流程唤醒
var URGE_WF_URL = "wfUrge.do"; //流程催办
var AGAIN_WF_URL = "wfTakeBack.do";//流程拿回/收回
var AGAINFIRST_WF_URL = "wfRecover.do";//流程追回
var START_GATHER_URL = "wfGather.do"; //实例化会办
var SIGNIN_TASK_URL = "wfTaskSignIn.do";  //项目池认领
var SIGNOFF_TASK_URL = "wfTaskSignOff.do";//项目池撤销认领
var CHECK_SUBWF_URL = "wfCheckAsynSubSubmit.do";  //子流程检查（当前节点是否发起过异步子流程）
var SUBMIT_SUBWF_URL = "wfSubSubmit.do";  //子流程发起

var SELECT_NEXTNODE_URL = "getNextNodeList.do"; //流程提交【获取一下节点列表】
var SELECT_WFNODE_URL = "getWFNodeList.do"; //流程跳转【获取流程节点列表】
var GET_STARTGATHER_URL = "getGatherPage.do"; //发起会办页面
var SELECT_ALLUSER_URL = "selectAllUser.do"; //流程协办【获取所有用户】
var SELECT_CHANGUSER_URL = "getWfChangeUser.do"; //流程转办【获取转办人员】
var SELECT_TREATEDNODE_URL = "getTreatedNodeList.do"; //流程打回【获取已经办理过的节点及人员列表】

/**
 * 流程操作请求URL定义
 */
//var SAVE_WF_URL = "saveWorkFlow.do";  //保存流程
//var SUBMIT_WF_URL = "submitWorkFlow.do"; //提交
//var SELECT_NEXTNODE_URL = "getNextNodeList.do"; //获取一下节点列表
//var ASSIST_WF_URL = "assistWorkFlow.do"; //协助办理
//var SELECT_ALLUSER_URL = "selectAllUser.do"; //协助办理选择用户
//var SELECT_CHANGUSER_URL = "getWfChangeUser.do"; //获取转办人员
//var CHANGE_WF_URL = "changeWorkFlow.do";  //转办
//var RETURNBACK_WF_URL = "returnBackWorkFlow.do";  //退回
//var CALLBACK_WF_URL = "callBackWorkFlow.do"; //打回
//var SELECT_TREATEDNODE_URL = "getTreatedNodeList.do"; //获取已经办理过的节点及人员列表
//var JUMP_WF_URL = "jumpWorkFlow.do";  //跳转
//var SELECT_WFNODE_URL = "getWFNodeList.do";  //获取流程所有节点列表
//var HANG_WF_URL = "hangWorkFlow.do";  //流程挂起
//var GET_STARTGATHER_URL = "getStartGatherPage.do";  //发起会办页面
//var START_GATHER_URL = "startGather.do";  //实例化会办
//var SUBMIT_SUBWF_URL = "submitSubWorkFlow.do";  //发起子流程
//var CHECK_SUBWF_URL = "checkAsynSubWorkFlow.do";  //检查当前节点是否发起过异步子流程
//var SIGNIN_WF_URL = "signInWorkFlow.do";  //签收实例
//var SIGNOFF_WF_URL = "signOffWorkFlow.do";  //实例撤销签收
//var SIGNIN_TASK_URL = "taskSignIn.do";  //实例放回项目池
//var SIGNOFF_TASK_URL = "taskSignOff.do";  //实例放回项目池
//var WAKE_WF_URL = "wakeWorkFlow.do";  //唤醒实例
//var AGAIN_WF_URL = "againWorkFlow.do";  //收回重办
//var AGAINFIRST_WF_URL = "againFirstWorkFlow.do";  //流程发起人追回重办
//var URGE_WF_URL = "urgeWorkFlow.do";  //实例催办

var DELETE_WF_URL = "delWorkFlow.do";  //删除实例
var RESET_WF_URL = "resetNodeUser.do";  //管理员重置节点办理人
var CUSTOM_DISAGREE_URL = "customDisagree.do";  //消费信贷特殊业务否决
var CUSTOM_DISAGREE_ACTIVATE_URL = "customDisagreeActivate.do"; //消费信贷特殊业务否决后再激活
var jFormObj;  //暂存表单

//显示流程操作返回结果消息，并返回到指定的页面
var showWfMsg = function(data) {
	var wfivo = data.WFI_RetObj;
	var sign = wfivo.sign;
	var nextNodeName = wfivo.nextNodeName;
	var msg = wfivo.message;
	var wfReturnUrl = wfivo.wfReturnUrl;
	if(sign==0&&nextNodeName!=null&&nextNodeName!='') {
		msg=msg+'<br/>下一办理节点：'+wfivo.nextNodeName+'&nbsp;&nbsp;&nbsp;&nbsp;下一办理人：'+wfivo.nextNodeUserName;
	}
	if(sign==0||sign==5){//成功或结束
		if(wfReturnUrl!=null && wfReturnUrl!='null' && wfReturnUrl!=''){
			$('#wfReturnUrl').setValue(wfReturnUrl);//设置特定指定的返回页面（如会签秘书提交）
		}
		top.$.messager.alert('',msg,'info',function(){
			var retUrl=$('#wfReturnUrl').getValue();
			if(retUrl=='F5'){
				window.location.reload();
			}else if(retUrl!=undefined&&retUrl!=null&&retUrl!=''&&retUrl!='#'){
				retUrl=EMPTools.encodeURI(retUrl);
				if(retUrl.indexOf('EMP_SID') == -1) {
					if(retUrl.indexOf('?') == -1) {
						retUrl = retUrl + '?EMP_SID='+$('#EMP_SID').getValue();
					} else {
						retUrl = retUrl + '&EMP_SID='+$('#EMP_SID').getValue();
					}
				}
			
				if(window.name == 'contentFrame') { //弹出窗口，最多只考虑在infoframe上弹出的第一层窗口发起流程
					        window.top.opener.location.reload();
							window.open('','_top');
							window.top.close();
						
				}else if(window.name != 'infoframe') { //弹出窗口，最多只考虑在infoframe上弹出的第一层窗口发起流程
					window.parent.location=retUrl;
					window.parent.EMP.closewin(); //关闭弹出窗口
				} else {
					window.location=retUrl;
				}
			}
		});
	}else{
		top.$.messager.alert('系统提示',msg,'info');
	}
	unmask();
};

//流程保存
var saveWorkFlow = function(jForm) {
	mask();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SAVE_WF_URL,
		data: jForm.toJsonData(),
		success: function(data) {
			showWfMsg(data);
			$('#wfCommentId').setValue(data.wfCommentId); //始终回写最新的流程意见id，修复无刷新再次保存时重复产生id
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			unmask();
		//	top.$.messager.alert('系统提示','保存出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);

		}
	});
};

//流程保存
var saveWorkFlow2 = function(jForm) {
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SAVE_WF_URL,
		data: jForm.toJsonData(),
		success: function(data) {
			$('#wfCommentId').setValue(data.wfCommentId); //始终回写最新的流程意见id，修复无刷新再次保存时重复产生id
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
		//	top.$.messager.alert('系统提示','保存出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);

		}
	});
};

//在执行流程操作前先保存
var doWorkFlowAgent = function(callBackFunc, jForm) {
	mask();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SAVE_WF_URL,
		data: jForm.toJsonData(),
		success: function(data) {
			unmask();
			var flag = data.WFI_RetObj.sign;
			$('#wfCommentId').setValue(data.wfCommentId); //始终回写最新的流程意见id，修复无刷新再次保存时重复产生id
			if(flag == 0) {
				callBackFunc(jForm);
			} else {
				top.$.messager.alert('系统提示','流程保存失败！'+flag,'error');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			unmask();
			//top.$.messager.alert('系统提示','保存出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);
		}
	});
};

//消费信贷特殊业务否决
var customDisagree = function(jForm) {
	top.$.messager.confirm('确定对话框','<b style="color:red">是否确定执行否决操作？</b>',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: CUSTOM_DISAGREE_URL,
				data: jForm.toJsonData(), 
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};
//消费信贷特殊业务否决后再激活
var customDisagreeActivate = function(jForm) {
	top.$.messager.confirm('确定对话框','<b style="color:red">是否确定执行激活操作？</b>',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: CUSTOM_DISAGREE_ACTIVATE_URL,
				data: jForm.toJsonData(), 
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//正常业务否决
var customDisagreeCommon = function(jForm) {
	top.$.messager.confirm('确定对话框','<b style="color:red">是否确定执行否决操作？</b>',function(r){
		if(r) {
			mask();
			document.getElementById("nextNodeId").value = "e0000"; //e标识为结束节点
			document.getElementById("nextNodeUser").value = "wfiSysNodeUser";
			$.ajax({
				type: "POST",
				dataType: "json",
				url: SUBMIT_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
}
var isOnClick =false;
//流程提交
var submitWorkFlow = function(jForm) {
	isOnClick = false;
	jFormObj = jForm;
	var instanceId = $('#instanceId').getValue();
	var nodeId = $('#nodeId').getValue();
	var sessionId = $('#EMP_SID').getValue();
	var applType = $('#applType').getValue();
	var commentSign = $('#commentSign').getValue();
	//打开选择下一节点及处理人的界面
	var url = SELECT_NEXTNODE_URL +"?instanceId="+instanceId+"&nodeId="+nodeId+"&commentSign="+commentSign+"&applType="+applType+"&EMP_SID="+sessionId+"&rd="+Math.random();
	var dialog=top.EMP.createwin({title:'选择下一步骤&办理人',url:url,maximized:false,closable:false,resizable:true,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',
	    handler:function (){
	    	if(isOnClick){
	    		top.$.messager.alert('系统提示',"请不要多次点击！",'info');
	    		return ;
	    	}
	    	//打开进度条
			top.$.messager.progress();
	    	
	    	
			var retObj =dialog.find('iframe').get(0).contentWindow.doRetSuc(window);
			if(retObj == null || retObj == "" || retObj == undefined || retObj == "null" || retObj == "undefined"){
				return ;
			}
			if(retObj[0]) {
				//检查是否有代办
				isOnClick =true;
			   	mask();
				var urlEn = "checkIsEntrust.do?userList="+retObj[2]+"&applType="+applType+"&EMP_SID="+sessionId+"&rd="+Math.random();
				isOnClick =false;
				$.ajax({
					type: "get",
					dataType: "json",
					url: urlEn,
					data: null, 
					success: function(data) {
						unmask();
						top.$.messager.progress('close');
						var isEntrust = data.isEntrust;
						if('true'==isEntrust){ //存在代办设置
							retObj[4]=true;
							var urlEntrust='selectEntrust.do?EMP_SID='+sessionId;
							urlEntrust=EMPTools.encodeURI(urlEntrust);
							var dialogEntrust=top.EMP.createwin({title:'代办模式',url:urlEntrust,maximized:false,closable:false,width:668,height:413,buttons: [{
								text:'确定',
							    iconCls:'icon-ok',    
							    handler:function (){
							    	top.$.messager.progress();
									var entrustModel=dialogEntrust.find('iframe').get(0).contentWindow.doSubmitWfAgentSet();
									if(entrustModel==''||entrustModel==null)return;
									dialogEntrust.dialog('close');
									retObj[5]=entrustModel;
									submitWorkFlowCallBack(retObj, dialog);
								}
							  },{
								text:'取消',
							    iconCls:'icon-cancel',    
							    handler:function (){
							    	dialogEntrust.dialog('close');
							    }
							}]});
						}else{
							top.$.messager.progress();
							retObj[4]=false;
							submitWorkFlowCallBack(retObj, dialog);
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						unmask();
						//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
						top.EMP.alertException(XMLHttpRequest.responseText);
						top.$.messager.progress('close');
					}
				});
			}
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){
	    	isOnClick = false;
	    	dialog.dialog('close');
	    	//还原主流程表单数据
			retsetFormValue(jForm);
	    }
	}],onLoad:function(){
		dialog.find('dialog-button').css('text-align','center');
	}});
};

//提交选择步骤&办理人后回调
var submitWorkFlowCallBack = function(retObj, dialog){
	mask();
	jForm = jFormObj;
	var applType = $('#applType').getValue();
	var sessionId = $('#EMP_SID').getValue();
	if(retObj!=null && retObj[0]==true) {
		var nextNode = retObj[1];
		var nextUser = retObj[2];
		var announceUser = retObj[3];
		var isEntrust = retObj[4];
		if(isEntrust){
			$('#entrustModel').setValue(retObj[5]);
		}
		$('#nextNodeId').setValue(nextNode);
		$('#nextNodeUser').setValue(nextUser);
		$('#nextAnnouceUser').setValue(announceUser);
		$.ajax({
			type: "POST",
			dataType: "json",
			url: SUBMIT_WF_URL,
			data: jForm.toJsonData(),
			success: function(data) {
				//还原主流程表单数据
				retsetFormValue(jForm);
				showWfMsg(data);
				dialog.dialog('close');
				top.$.messager.progress('close');
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				unmask();
				//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
				top.EMP.alertException(XMLHttpRequest.responseText);
				top.$.messager.progress('close');
			}
		});
	}
};

//发起子流程
var doSubWorkFlow = function(jForm) {
	//子流程类型0.不允许;1.用户选择同步子流;2.用户选择异步子流;3.系统指定同步子流;4.系统指定异步子流
	var subWfType = $('#subWfType').getValue();
	if(subWfType!='1' && subWfType!='2') {
		//alert('当前子流程类型为['+subWfType+']，非用户选择子流程，不能发起！');
		top.$.messager.alert('系统提示','当前子流程类型为['+subWfType+']，非用户选择子流程，不能发起！','error');
		return;
	}
	if(subWfType == '1') {
		top.$.messager.confirm('确定对话框','是否确定要发起同步子流程？',function(r){
			if(r) {
				subWorkFlowInitSubmit(jForm);
			}
		});
	} else { //检查在当前节点是否已经发起过子流程，进行提示
		mask();
		$.ajax({
			type: "POST",
			dataType: "json",
			url: CHECK_SUBWF_URL,
			data: jForm.toJsonData(), 
			success: function(data) {
				unmask();
				var flag = data.flag;
				if(flag == 0) {
					//不存在在途的子流程，直接继续执行
					subWorkFlowInitSubmit(jForm);					
				} else if(flag == 1){
					top.$.messager.confirm('确定对话框','当前流程已经存在审批中的子流程，确定继续？',function(r){
						if(r) {
							subWorkFlowInitSubmit(jForm);
						}
					});
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				unmask();
				//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
				top.EMP.alertException(XMLHttpRequest.responseText);
			}
		});
	}
};

//提交子流程
var subWorkFlowInitSubmit = function(jForm) {
	mask();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SUBMIT_SUBWF_URL,
		data: jForm.toJsonData(), 
		success: function(data) {
			unmask();
			var subInstanceId = data.instanceId;
			var subNodeId = data.nodeId;
			if(subInstanceId!==null && subInstanceId!='') {
				//发起成功，执行提交
				$('#instanceId').setValue(subInstanceId);
				$('#nodeId').setValue(subNodeId);
				var subWfType = $('#subWfType').getValue();
				if(subWfType == '1') {
					$('#callSubFlow').css('disabled',true);
				} else {
					$('#wfReturnUrl').setValue('#'); //异步子流程提交后，继续停留在审批页面
				}
				submitWorkFlow(jForm);
			} else {
				top.$.messager.alert('系统提示','发起子流程失败！'+data,'error');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			unmask();
			//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);
		}
	});
};

//协办
var assistWorkFlow = function(jForm) {
	var url = SELECT_ALLUSER_URL+"?count=1&EMP_SID="+$('#EMP_SID').getValue();
	var dialog=top.EMP.createwin({title:'选择协助办理人',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',    
	    handler:function (){
			var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
			if(retObj){
				mask();
				$('#nextNodeUser').setValue(retObj[1]);
				$.ajax({
					type: "POST",
					dataType: "json",
					url: ASSIST_WF_URL,
					data: jForm.toJsonData(),
					success: function(data) {
						dialog.dialog('close');
						showWfMsg(data);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						unmask();
						//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
						top.EMP.alertException(XMLHttpRequest.responseText);
					}
				});
			}else{
				top.$.messager.alert('系统提示','您没有选择协助办理用户！','warning');
			}
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){dialog.dialog('close');}
	}]});
};

//转办
var changeWorkFlow = function(jForm) {
	var url = SELECT_CHANGUSER_URL+'?instanceId='+$('#instanceId').getValue()+'&nodeId='+$('#nodeId').getValue()+'&EMP_SID='+$('#EMP_SID').getValue()+'&rd='+Math.random();
	var dialog=top.EMP.createwin({title:'流程转办',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',    
	    handler:function (){
			var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
			if(retObj){
				var user=retObj[1];
				if(user==null||user.length==0){
					top.$.messager.alert('系统提示','您没有选择可转办用户！','warning');
					return ;
				}
				$('#nextNodeId').setValue($('#nodeId').getValue());
				if(user != null)
					$('#nextNodeUser').setValue(user);
				mask();
				$.ajax({
					type: "POST",
					dataType: "json",
					url: CHANGE_WF_URL,
					data: jForm.toJsonData(), 
					success: function(data) {
						dialog.dialog('close');
						showWfMsg(data);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						unmask();
						//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
						top.EMP.alertException(XMLHttpRequest.responseText);
					}
				});
			}else{
				top.$.messager.alert('系统提示','您没有选择可转办用户！','warning');
			}
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){dialog.dialog('close');}
	}]});
};

//退回
var returnBackWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您确定要执行退回操作吗？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: RETURNBACK_WF_URL,
				data: jForm.toJsonData(), 
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//打回
var callBackWorkFlow = function(jForm) {
	var instanceId = $('#instanceId').getValue();
	var nodeId = $('#nodeId').getValue();
	//打开选择打回点及处理人的界面
	var url = SELECT_TREATEDNODE_URL+'?instanceId='+instanceId+'&nodeId='+nodeId+'&EMP_SID='+$('#EMP_SID').getValue()+'&rd='+Math.random();
	var dialog=top.EMP.createwin({title:'选择退回步骤',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',    
	    handler:function (){
			var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
			if(retObj[0]){
				var node=retObj[1];
				if(node==null||node==""){
					top.$.messager.alert('系统提示','请选择好处理过的步骤！','error');
					return;
				}
				var usr=retObj[2];
				if(usr==null||usr==""){
					top.$.messager.alert('系统提示','请选择好处理过的人员！','error');
					return;
				}
				var callBackModel=retObj[3];
				if(callBackModel==null||callBackModel==""){
					top.$.messager.alert('系统提示','请选择好退回后提交方式！','error');
					return;
				}
				$('#nextNodeId').setValue(node);
				$('#nextNodeUser').setValue(usr);
				$('#callBackModel').setValue(callBackModel);
				mask();
				$.ajax({
					type: "POST",
					dataType: "json",
					url: CALLBACK_WF_URL,
					data: jForm.toJsonData(), 
					success: function(data) {
						dialog.dialog('close');
						showWfMsg(data);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						unmask();
						//top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
						top.EMP.alertException(XMLHttpRequest.responseText);
					}
				});
			}else{
				top.$.messager.alert('系统提示','系统错误，没有选择好退回步骤及办理人！','error');
			}
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){dialog.dialog('close');}
	}]});
};

//跳转。
//由于可以跳转到流程任意节点，如果严格按照节点配置的办理人员计算办理人，将可能导致人员计算出错或失败的情况；
//如,跳转到一个办理人员配置为同一机构关系的节点。所以放开权限，跳转可以选择所有用户。
var jumpWorkFlow = function(jForm) {
	var instanceId = $('#instanceId').getValue();
	//打开选择打回点及处理人的界面
	var url = SELECT_WFNODE_URL+'?instanceId='+instanceId+'&EMP_SID='+$('#EMP_SID').getValue()+'&rd='+Math.random();
	var dialog=top.EMP.createwin({title:'跳转',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',    
	    handler:function (){
			var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
			if(retObj[0]){
				var node=retObj[1];
				if(node==null||node==""){
					top.$.messager.alert('系统提示','请选择好下一处理步骤！','error');
					return;
				}
				var usr=retObj[2];
				var ispool=retObj[3];
				//是否项目池
				if(ispool=="n"&&(usr==null||usr==""))
				{
					top.$.messager.alert('系统提示','请选择好下一办理人！','error');
					return;		
				}
				$('#nextNodeId').setValue(node);
				if(usr!=null) {
					$('#nextNodeUser').setValue(usr);
				}
				if(ispool=="y") {
					$('#nextNodeUser').setValue(""); //项目池人员为空
				}
				mask();
				$.ajax({
					type: "POST",
					dataType: "json",
					url: JUMP_WF_URL,
					data: jForm.toJsonData(), 
					success: function(data) {
						dialog.dialog('close');
						showWfMsg(data);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						unmask();
						top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
						top.EMP.alertException(XMLHttpRequest.responseText);
					}
				});
			}else{
				top.$.messager.alert('系统提示','请选择好跳转步骤及办理人！','error');
			}
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){dialog.dialog('close');}
	}]});
};

//挂起
var hangWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您是否确定执行流程挂起操作？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: HANG_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//唤醒流程实例
var doWakeWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您是否确定执行流程唤醒操作？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: WAKE_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//收回重办
var doAgainWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您是否确定执行流程收回操作？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: AGAIN_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//流程发起人追回重办（暂不提供）
var doAgainFisrtWorkFlow = function(jForm) {
	
};

//实例催办
var doUrgeWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您是否确定执行流程催办操作？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: URGE_WF_URL,
				data: jForm.toJsonData(), 
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//会办
var doStartGather = function(jForm) {
	var instanceId = $('#instanceId').getValue();
	var nodeId = $('#nodeId').getValue();
	var bizSeqNo = $('#pkVal').getValue();
	//发起会办
	var url = GET_STARTGATHER_URL+'?instanceId='+instanceId+'&nodeId='+nodeId+'&bizSeqNo='+bizSeqNo+'&EMP_SID='+$('#EMP_SID').getValue()+'&rd='+Math.random();
	var dialog=top.EMP.createwin({title:'发起会办',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
		text:'确定',
	    iconCls:'icon-ok',    
	    handler:function (){
	    	var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
		}
	  },{
		text:'取消',
	    iconCls:'icon-cancel',    
	    handler:function (){dialog.dialog('close');}
	}]});
};

//流程实例签收
var doSignInWorkFlow = function(jForm) {
	/*由于可以通过撤销签收操作来撤销，所以不弹出确定对话框，减少用户操作
	top.$.messager.confirm('确定对话框','您确定要执行签收操作吗？',function(r){
		if(r){
		}
	});
	*/
	mask();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SIGNIN_WF_URL,
		data: jForm.toJsonData(),
		success: function(data) {
			showWfMsg(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			unmask();
			top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);
		}
	});
};

//流程实例撤销签收
var doSignOffWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您确定要执行撤销签收操作吗？',function(r){
		if(r){
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: SIGNOFF_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//流程实例放回项目池
var doTaskSignOff = function(jForm) {
	top.$.messager.confirm('确定对话框','您确定执行此操作？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: SIGNOFF_TASK_URL,
				data: jForm.toJsonData(), 
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//项目池流程实例认领
var doTaskSignIn = function(jForm) {
	mask();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: SIGNIN_TASK_URL,
		data: jForm.toJsonData(), 
		success: function(data) {
			showWfMsg(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			unmask();
			top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
			top.EMP.alertException(XMLHttpRequest.responseText);
		}
	});
};

//管理员删除流程实例（不可恢复）
var doDelWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您确定执行流程实例删除操作（此操作数据不可恢复）？',function(r){
		if(r) {
			mask();
			$.ajax({
				type: "POST",
				dataType: "json",
				url: DELETE_WF_URL,
				data: jForm.toJsonData(),
				success: function(data) {
					showWfMsg(data);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					unmask();
					top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
					top.EMP.alertException(XMLHttpRequest.responseText);
				}
			});
		}
	});
};

//管理员重置流程当前办理人
var doResetWorkFlow = function(jForm) {
	top.$.messager.confirm('确定对话框','您确定执行重置流程当前办理人操作？',function(r){
		if(r) {
			var url = SELECT_ALLUSER_URL+"?count=1&EMP_SID="+$('#EMP_SID').getValue();
			var dialog=top.EMP.createwin({title:'重置办理人',url:url,maximized:false,closable:false,width:668,height:413,buttons: [{
				text:'确定',
			    iconCls:'icon-ok',    
			    handler:function (){
					var retObj=dialog.find('iframe').get(0).contentWindow.retSuc();
					if(retObj && retObj[0]){
						$('#ext').setValue(retObj[1]);
						mask();
						$.ajax({
							type: "POST",
							dataType: "json",
							url: RESET_WF_URL,
							data: jForm.toJsonData(), 
							success: function(data) {
								dialog.dialog('close');
								showWfMsg(data);
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								unmask();
								top.$.messager.alert('系统提示','操作出错！原因：'+errorThrown,'error');
								top.EMP.alertException(XMLHttpRequest.responseText);
							}
						});
					}else{
						top.$.messager.alert('系统提示','您没有选择重置办理人！','warning');
					}
				}
			  },{
				text:'取消',
			    iconCls:'icon-cancel',    
			    handler:function (){dialog.dialog('close');}
			}]});
		}
	});
};

var mask = function() {
	var bd = EMPTools.topPage.$('body');
	//IE不支持appentTo方法，所以替换成append
	$(bd).append("<div id='cmisMaskDiv' class='window-mask' style='display:block;z-index:998'></div>");
	$(bd).append("<div id='cmisMaskMsgDiv' class='datagrid-mask-msg' style='display:block;left:50%;z-index:999'>正在处理，请稍待。。。</div>");
//	$("<div id=\"cmisMaskDiv\" class=\"window-mask\" style=\"display:block;z-index:99998\"></div>").appendTo(bd);
//	var msg=$("<div id=\"cmisMaskMsgDiv\" class=\"datagrid-mask-msg\" style=\"display:block;left:50%;z-index:99999\"></div>").html("正在处理，请稍待。。。").appendTo(bd);
	var msg = $('#cmisMaskMsgDiv');
	msg.css("marginLeft",-msg.outerWidth()/2);
};

var unmask = function() {
	try{
		EMPTools.topPage.$('#cmisMaskMsgDiv').remove();
		EMPTools.topPage.$('#cmisMaskDiv').remove();
	}catch(e){}
};

var doTrack = function(form) {
	echain_track(form,"echain/studio/eChainMonitor.jsp");
};

function echain_track(form,trackUrl){
	var instanceid = form.instanceid.value;
	var currentuserid = form.currentuserid.value;
	var sessionId = document.getElementsByName("EMP_SID")[0].value;
	var url = trackUrl + "?EMP_SID=" + sessionId+"&instanceid="+instanceid+"&currentuserid="+currentuserid+"&rd="+Math.random();
	window.open(url,"流程跟踪", "height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no");
};

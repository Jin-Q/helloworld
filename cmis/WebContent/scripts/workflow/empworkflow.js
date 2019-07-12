	/**
	 * 流程操作请求URL定义
	 */
	var SAVE_WF_URL = "saveWorkFlow.do";  //保存流程
	var SUBMIT_WF_URL = "submitWorkFlow.do"; //提交
	var SELECT_NEXTNODE_URL = "getNextNodeList.do"; //获取一下节点列表
	var ASSIST_WF_URL = "assistWorkFlow.do"; //协助办理
	var SELECT_ALLUSER_URL = "selectAllUser.do"; //协助办理选择用户
	var SELECT_CHANGUSER_URL = "getWfChangeUser.do"; //获取转办人员
	var CHANGE_WF_URL = "changeWorkFlow.do";  //转办
	var RETURNBACK_WF_URL = "returnBackWorkFlow.do";  //退回
	var CALLBACK_WF_URL = "callBackWorkFlow.do"; //打回
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 start */
	var UNLIMIT_CALLBACK_WF_URL = "callBackWorkFlowUnlimit.do"; //管理员无条件打回
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 end */
	var SELECT_TREATEDNODE_URL = "getTreatedNodeList.do"; //获取已经办理过的节点及人员列表
	var JUMP_WF_URL = "jumpWorkFlow.do";  //跳转
	var SELECT_WFNODE_URL = "getWFNodeList.do";  //获取流程所有节点列表
	var HANG_WF_URL = "hangWorkFlow.do";  //流程挂起
	var GET_STARTGATHER_URL = "getStartGatherPage.do";  //发起会办页面
	var START_GATHER_URL = "startGather.do";  //实例化会办
	var SUBMIT_SUBWF_URL = "submitSubWorkFlow.do";  //发起子流程
	var CHECK_SUBWF_URL = "checkAsynSubWorkFlow.do";  //检查当前节点是否发起过异步子流程
	var SIGNIN_WF_URL = "signInWorkFlow.do";  //签收实例
	var SIGNOFF_WF_URL = "signOffWorkFlow.do";  //实例撤销签收
	var SIGNOFF_TASK_URL = "taskSignOff.do";  //实例放回项目池
	var WAKE_WF_URL = "wakeWorkFlow.do";  //唤醒实例
	var AGAIN_WF_URL = "againWorkFlow.do";  //收回重办
	var AGAINFIRST_WF_URL = "againFirstWorkFlow.do";  //流程发起人追回重办
	var URGE_WF_URL = "urgeWorkFlow.do";  //实例催办
	var DELETE_WF_URL = "delWorkFlow.do";  //删除实例
	var RESET_WF_URL = "resetNodeUser.do";  //管理员重置节点办理人
	
	//流程保存
	var saveWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('保存成功！');
				} else {
					alert('流程保存失败！'+flag);
				}
			}catch(e) {
				alert('流程保存异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', SAVE_WF_URL, callback,postData);
	}
	
	//在执行流程操作前先保存
	var doWorkFlowAgent = function(callBackFunc, form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					callBackFunc(form);
				} else {
					alert('流程保存失败！'+flag);
				}
			}catch(e) {
				alert('流程保存异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', SAVE_WF_URL, callback, postData);
	}
	
	//业务否决
	var customDisagree = function(form) {
		if(confirm('是否确定执行否决操作？')) {
			document.getElementById("nextNodeId").value = "e0000"; //e标识为结束节点
			document.getElementById("nextNodeUser").value = "wfiSysNodeUser";
			form.action = SUBMIT_WF_URL;
			form.submit();
		}
	}
	
	 // 流程取消条件，删除初始化的流程信息。
	 function doCancel(form) {
	    	var DELETE_WF_URL = "delWorkFlow.do";  //删除实例
	   		var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 0) {
					//	alert('流程发起取消,删除流程初始化数据成功！');
					} else {
						alert('流程发起取消,删除流程初始化数据失败！'+flag);
					}
				}catch(e) {
					alert('删除流程实例异常！'+o.responseText);
					return;
				}
			};		
			var handleFailure = function(o){	
				alert(o.responseText);
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', DELETE_WF_URL, callback, postData);
	    }
	 
	//流程提交
	var submitWorkFlow = function(form) {
		var instanceId = document.getElementById("instanceId").value;
		var nodeId = document.getElementById("nodeId").value;
		var currentUserId = document.getElementById("currentUserId").value;
		var sessionId = document.getElementById("EMP_SID").value;
		var applType = document.getElementById("applType").value;
		var commentSign = document.getElementById("commentSign").value;
		//流程是否在拟稿状态，用于流程发起后不选人，删除流程初审化信息。
		var isDraft = document.getElementById("isDraft").value;
		
		/** 风险拦截此处进行，先保存完，由于审批中变更所以先进行保存操作后再进行风险拦截 edit by tangzf 2014-03-12 */
		//检查是否需要执行风险拦截
		var _preventIdLst = document.getElementById("preventList").value;
		if(_preventIdLst!=null && _preventIdLst!='null' && _preventIdLst!='') {
			var pkValue = document.getElementById("pkVal").value;
			var modelId = document.getElementById("modelId").value;
			var wfSign = document.getElementById("wfSign").value;
			var _urlPrv = "procRiskInspect4WF.do?applType="+applType+"&pkVal="+pkValue+"&modelId="+modelId+"&wfSign=" +wfSign +"&pvId="+_preventIdLst+"&rd="+Math.random()+"&nodeId="+nodeId+"&EMP_SID="+sessionId;
	        var _retObj = window.showModalDialog(_urlPrv,"preventPage","dialogHeight=500px;dialogWidth=850px;");
			if(!_retObj || _retObj == '2' || _retObj == '5'){
				if( _retObj == '5'){
					alert("执行风险拦截有错误，请检查！");
				}
				return;
			}
		}
		
		//打开选择下一节点及处理人的界面
		var url = SELECT_NEXTNODE_URL +"?instanceId="+instanceId+"&nodeId="+nodeId+"&commentSign="+commentSign+"&EMP_SID="+sessionId+"&rd="+Math.random();
		var retObj = window.showModalDialog(url,'selectSubmitPage','dialogHeight:400px;dialogWidth:600px;help:no;resizable:no;status:no;location:no;');
		if(retObj!=null && retObj[0]==true) {
			var nextNode = retObj[1];
			var nextUser = retObj[2];
			var announceUser = retObj[3];
			document.getElementById("nextNodeId").value = nextNode;
			document.getElementById("nextNodeUser").value = nextUser;
			document.getElementById("nextAnnouceUser").value = announceUser;
			if(nextNode.indexOf('e')==-1) {
				//检查是否有委托设置，如果有则需选择提交代办模式0代办人办理，1原办理人代人都可以办理，2原办理人办理
				var urlEn = "checkIsEntrust.do?userList="+nextUser+"&applType="+applType+"&EMP_SID="+sessionId+"&rd="+Math.random();
				var retObjEn = window.showModalDialog(urlEn,'selectEnPage','dialogHeight:300px;dialogWidth:600px;help:no;resizable:no;status:no;');
				if(retObjEn==null) {
					//alert("检查代办模式失败，操作终止！");  取消或直接关闭不提示，正常终止操作
					return;				
				}
				if(retObjEn[0]=="true"){
					//给entrustModel赋
					var entrustModel = retObjEn[1];
					if(entrustModel!=null && entrustModel!="")
						document.getElementById("entrustModel").value=entrustModel;
					else{
						alert("请选择代办模式");
						return;
					}
				}
			}
			form.action = SUBMIT_WF_URL;
			form.submit();
		}else {
			if('1'==isDraft) {
			doCancel(form); 
		    } 
		}
	}
	
	//协办
	var assistWorkFlow = function(form) {
		var url = SELECT_ALLUSER_URL+"?count=1&EMP_SID="+document.getElementById("EMP_SID").value;
		var retObj = window.showModalDialog(url,'selectAssistPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;location:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		if(retObj[1] != null) {
			document.getElementById("nextNodeUser").value = retObj[1];
		}
		form.action = ASSIST_WF_URL;
		form.submit();
	}
	
	//转办
	var changeWorkFlow = function(form) {
		var url = SELECT_CHANGUSER_URL+'?instanceId='+document.getElementById("instanceId").value+'&nodeId='+document.getElementById("nodeId").value+'&EMP_SID='+document.getElementById("EMP_SID").value+'&rd='+Math.random();
		var retObj = window.showModalDialog(url,'selectChangePage','dialogHeight:300px;dialogWidth:600px;location:no;help:no;resizable:no;status:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		document.getElementById("nextNodeId").value = document.getElementById("nodeId").value;
		if(retObj[1] != null)
			document.getElementById("nextNodeUser").value = retObj[1];
		form.action = CHANGE_WF_URL;
		form.submit();
	}
	
	//退回
	var returnBackWorkFlow = function(form) {
		if(confirm('确定要执行退回操作？')) {
			form.action = RETURNBACK_WF_URL;
			form.submit();
		}
		
	}
	
	//打回
	var callBackWorkFlow = function(form) {
		var instanceId = document.getElementById("instanceId").value;
		var nodeId = document.getElementById("nodeId").value;
		/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
		var wfSign = document.getElementById("wfSign").value;
		//打开选择打回点及处理人的界面
		var url = SELECT_TREATEDNODE_URL+'?instanceId='+instanceId+'&nodeId='+nodeId+'&wfSign='+wfSign+'&EMP_SID='+document.getElementById("EMP_SID").value+'&rd='+Math.random();
		/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
		var retObj = window.showModalDialog(url,'selectCallBackPage','dialogHeight:450px;dialogWidth:750px;help:no;resizable:no;status:no;');
		//window.open(url);
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		document.getElementById("nextNodeId").value = retObj[1];
		if(retObj[2] != null)
			document.getElementById("nextNodeUser").value = retObj[2];
		if(retObj[3] != null)
			document.getElementById("callBackModel").value = retObj[3];
		/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
		if(retObj[4] != null)
			document.getElementById("callBackDiscs").value = retObj[4];
		/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
		/** add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin*/
		if(retObj[5] != null)
			document.getElementById("approveOpModel").value = retObj[5];
		/** add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end*/
		form.action = CALLBACK_WF_URL;
		form.submit();
	}
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 start */
	//无条件打回（非节点人打回）
	var callBackWorkFlowUnlimit = function(form) {
		var instanceId = document.getElementById("instanceId").value;
		var nodeId = document.getElementById("nodeId").value;
		//打开选择打回点及处理人的界面
		var url = SELECT_TREATEDNODE_URL+'?instanceId='+instanceId+'&nodeId='+nodeId+'&EMP_SID='+document.getElementById("EMP_SID").value+'&rd='+Math.random();
		var retObj = window.showModalDialog(url,'selectCallBackPage','dialogHeight:450px;dialogWidth:750px;help:no;resizable:no;status:no;');
		//window.open(url);
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		document.getElementById("nextNodeId").value = retObj[1];
		if(retObj[2] != null)
			document.getElementById("nextNodeUser").value = retObj[2];
		if(retObj[3] != null)
			document.getElementById("callBackModel").value = retObj[3];
		form.action = UNLIMIT_CALLBACK_WF_URL;
		form.submit();
	}
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 end */
	//跳转。
	//由于可以跳转到流程任意节点，如果严格按照节点配置的办理人员计算办理人，将可能导致人员计算出错或失败的情况；
	//如,跳转到一个办理人员配置为同一机构关系的节点。所以放开权限，跳转可以选择所有用户。
	var jumpWorkFlow = function(form) {
		var instanceId = document.getElementById("instanceId").value;
		//打开选择打回点及处理人的界面
		var url = SELECT_WFNODE_URL+'?instanceId='+instanceId+'&EMP_SID='+document.getElementById("EMP_SID").value+'&rd='+Math.random();
		var retObj = window.showModalDialog(url,'selectWfNodePage','dialogHeight:450px;dialogWidth:750px;help:no;resizable:no;status:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		document.getElementById("nextNodeId").value = retObj[1];
		if(retObj[2] != null)
			document.getElementById("nextNodeUser").value = retObj[2];
		else
			document.getElementById("nextNodeUser").value = ""; //项目池人员为空
		form.action = JUMP_WF_URL;
		form.submit();
	}
	
	//挂起
	var hangWorkFlow = function(form) {
		if(confirm('是否确定执行流程挂起操作？')) {
			form.action = HANG_WF_URL;
			form.submit();
		}
	}
	
	//唤醒流程实例
	var doWakeWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('唤醒流程实例成功！');
					//var url = 'getHangWorkList.do?EMP_SID='+document.getElementById("EMP_SID").value;
					//window.location = url;
					var url = form.ext.value;
					url = EMPTools.encodeURI(url);
					window.location = url;
				} else {
					alert('唤醒流程实例失败！'+flag);
				}
			}catch(e) {
				alert('唤醒流程实例异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', WAKE_WF_URL, callback, postData);
	}
	
	//收回重办
	var doAgainWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('流程收回成功！');
					var url = 'getToDoWorkList.do?EMP_SID='+document.getElementById("EMP_SID").value;
					window.location = url;					
				} else {
					alert('流程收回失败！'+flag);
				}
			}catch(e) {
				alert('流程收回异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', AGAIN_WF_URL, callback, postData);
	}
	
	//流程发起人追回重办
	var doAgainFisrtWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('流程追回成功！');
					var url = 'getToDoWorkList.do?EMP_SID='+document.getElementById("EMP_SID").value;
					window.location = url;					
				} else {
					alert('流程追回失败！'+flag);
				}
			}catch(e) {
				alert('流程追回异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', AGAINFIRST_WF_URL, callback, postData);
	}
	
	//实例催办
	var doUrgeWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('流程催办成功！');
				} else {
					alert('流程催办失败！'+flag);
				}
			}catch(e) {
				alert('流程催办异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', URGE_WF_URL, callback, postData);
	}
	
	//会办
	var doStartGather = function(form) {
		var instanceId = document.getElementById("instanceId").value;
		var nodeId = document.getElementById("nodeId").value;
		var bizSeqNo = document.getElementById("pkVal").value;
		//发起会办
		//打开选择下一节点及处理人的界面
		var url = GET_STARTGATHER_URL+'?instanceId='+instanceId+'&nodeId='+nodeId+'&bizSeqNo='+bizSeqNo+'&EMP_SID='+document.getElementById("EMP_SID").value+'&rd='+Math.random();
		var retValue=window.showModalDialog(url, "selectGatherPage", 'dialogHeight:360px;dialogWidth:660px;help:no;resizable:no;status:no;');
	}
	
	//发起子流程
	var doSubWorkFlow = function(form) {
		//子流程类型0.不允许;1.用户选择同步子流;2.用户选择异步子流;3.系统指定同步子流;4.系统指定异步子流
		var subWfType = form.subWfType.value;
		if(subWfType!='1' && subWfType!='2') {
			alert('当前子流程类型为['+subWfType+']，非用户选择子流程，不能发起！');
			return;
		}
		if(subWfType == '1') {
			if(!confirm("是否确定要发起同步子流程？")) {
				return;
			}
			subWorkFlowInitSubmit(form);
		} else { //检查在当前节点是否已经发起过子流程，进行提示
			var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 0) {
						//不存在在途的子流程，直接继续执行
						subWorkFlowInitSubmit(form);					
					} else if(flag == 1){
						if(confirm('系统检查到当前流程已经存在审批中的子流程，确定继续？')) {
							subWorkFlowInitSubmit(form);
						}
					}
				}catch(e) {
					alert(o.responseText);
					return;
				}
			};		
			var handleFailure = function(o){	
				alert(o.responseText);
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', CHECK_SUBWF_URL, callback, postData);
		}
	}
	var subWorkFlowInitSubmit = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var subInstanceId = jsonstr.instanceId;
				var subNodeId = jsonstr.nodeId;
				if(subInstanceId!==null && subInstanceId!='') {
					//发起成功，执行提交
					//考虑到异步子流程操作成功后,还需停留在流程审批页面可能做主流程其他操作，所以异常子流程提交需采用异步操作
					form.instanceId.value = subInstanceId;
					form.nodeId.value = subNodeId;
					var subWfType = form.subWfType.value;
					if(subWfType == '1') {
						document.getElementById("callSubFlow").disabled = true;
						submitWorkFlow(form);
					} else {
						var currentUserId = document.getElementById("currentUserId").value;
						var sessionId = document.getElementById("EMP_SID").value;
						//打开选择下一节点及处理人的界面
						var url = SELECT_NEXTNODE_URL +"?instanceId="+subInstanceId+"&nodeId="+subNodeId+"&EMP_SID="+sessionId+"&rd="+Math.random();
						var retObj = window.showModalDialog(url,'selectSubmitPage','dialogHeight:450px;dialogWidth:700px;help:no;resizable:no;status:no;location:no;');
						if(retObj!=null && retObj[0]==true) {
							var nextNode = retObj[1];
							var nextUser = retObj[2];
							var announceUser = retObj[3];
							document.getElementById("nextNodeId").value = nextNode;
							document.getElementById("nextNodeUser").value = nextUser;
							document.getElementById("nextAnnouceUser").value = announceUser;
							if(nextNode.indexOf('e')==-1) {
								//检查是否有委托设置，如果有则需选择提交代办模式0代办人办理，1原办理人代人都可以办理，2原办理人办理
								var urlEn = "checkIsEntrust.do?userList="+nextUser+"&applType="+document.getElementById("applType").value+"&EMP_SID="+sessionId+"&rd="+Math.random();
								var retObjEn = window.showModalDialog(urlEn,'selectEnPage','dialogHeight:300px;dialogWidth:600px;help:no;resizable:no;status:no;');
								if(retObjEn==null) {
									//alert("检查代办模式失败，操作终止！");  取消或直接关闭不提示，正常终止操作
									return;				
								}
								if(retObjEn[0]=="true"){
									//给entrustModel赋
									var entrustModel = retObjEn[1];
									if(entrustModel!=null && entrustModel!="")
										document.getElementById("entrustModel").value=entrustModel;
									else{
										alert("请选择代办模式");
										return;
									}
								}
							}
							form.action = SUBMIT_WF_URL;
							var handleSuccessSub = function(o){
								try {
									var jsonstr = eval("("+o.responseText+")");
									var flag = jsonstr.flag;
									if(flag == 0) {
										alert('子流程提交成功！');
									} else {
										alert('子流程提交失败！'+flag);
									}
								}catch(e) {
									alert('子流程提交异常！'+o.responseText);
									return;
								}
								//还原主流程表单数据
								retsetFormValue(form);
							};		
							var handleFailureSub = function(o){
								//还原主流程表单数据
								retsetFormValue(form);
								alert(o.responseText);
							};		
							var callbackSub = {
								success:handleSuccessSub,
								failure:handleFailureSub
							};
							var postDataSub = YAHOO.util.Connect.setForm(form);			
							var obj1Sub = YAHOO.util.Connect.asyncRequest('POST', form.action, callbackSub,postDataSub);
						} else {
							//还原主流程表单数据
							retsetFormValue(form);
						}
					}
				} else {
					alert('发起子流程失败！'+jsonstr);
				}
			}catch(e) {
				alert('发起子流程异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);		
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', SUBMIT_SUBWF_URL, callback,postData);
	}
	
	//流程实例签收
	var doSignInWorkFlow = function(form) {
		if(confirm("确定执行此操作？")) {
			var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 0) {
						alert('流程实例签收成功！');
						window.location.reload();					
					} else {
						alert('流程实例签收失败！'+flag);
					}
				}catch(e) {
					alert('流程实例签收异常！'+o.responseText);
					return;
				}
			};		
			var handleFailure = function(o){	
				alert(o.responseText);
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', SIGNIN_WF_URL, callback,postData);
		}
	}
	
	//流程实例撤销签收
	var doSignOffWorkFlow = function(form) {
		if(confirm("确定执行此操作？")) {
			var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 0) {
						alert('流程实例撤销签收成功！');
						var url = 'getToDoWorkList.do?EMP_SID='+document.getElementById("EMP_SID").value;
						window.location = url;					
					} else {
						alert('流程实例撤销签收失败！'+flag);
					}
				}catch(e) {
					alert('流程实例撤销签收异常！'+o.responseText);
					return;
				}
			};		
			var handleFailure = function(o){	
				alert(o.responseText);
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', SIGNOFF_WF_URL, callback,postData);
		}
	}
	
	//流程实例放回项目池
	var doTaskSignOff = function(form) {
		if(confirm("确定执行此操作？")) {
			var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 0) {
						alert('流程实例放回项目池成功！');
						var url = 'getToDoWorkList.do?EMP_SID='+document.getElementById("EMP_SID").value;
						window.location = url;					
					} else {
						alert('流程实例放回项目池失败！'+flag);
					}
				}catch(e) {
					alert('流程实例放回项目池异常！'+o.responseText);
					return;
				}
			};		
			var handleFailure = function(o){	
				alert(o.responseText);
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', SIGNOFF_TASK_URL, callback,postData);
		}
	}
	
	//管理员删除流程实例（不可恢复）
	var doDelWorkFlow = function(form) {
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('删除流程实例成功。将返回列表页面！');
					var url = 'getWfInstanceList.do?flag=1&EMP_SID='+document.getElementById("EMP_SID").value;
					window.location = url;
				} else {
					alert('删除流程实例失败！'+flag);
				}
			}catch(e) {
				alert('删除流程实例异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', DELETE_WF_URL, callback, postData);
	}
	
	//管理员重置流程当前办理人
	var doResetWorkFlow = function(form) {
		var url = SELECT_ALLUSER_URL+"?count=1&EMP_SID="+document.getElementById("EMP_SID").value;
		var retObj = window.showModalDialog(url,'selectResetPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;location:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		if(retObj[1] != null) {
			document.getElementById("ext").value = retObj[1];
		} else {
			return;
		}
		
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('重置流程当前办理人成功！');
					window.location.reload();
				} else {
					alert('重置流程当前办理人失败！'+flag);
				}
			}catch(e) {
				alert('重置流程当前办理人异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', RESET_WF_URL, callback, postData);
	}
	
	var doTrack = function(form) {
		echain_track(form,"echain/studio/eChainMonitor.jsp");
	}
	
	function echain_track(form,trackUrl){
		var instanceid = form.instanceid.value;
		var currentuserid = form.currentuserid.value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		var url = trackUrl + "?EMP_SID=" + sessionId+"&instanceid="+instanceid+"&currentuserid="+currentuserid+"&rd="+Math.random();
		window.open(url,"流程跟踪", "height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no");
	};
	
	function disabledAllButtons() {
		var bts = document.getElementsByTagName("BUTTON");
		for(var i=0; i<bts.length; i++) {
			bts[i].disabled=true;
		}
	};
	
	var abledAllButtons = function() {
		var bts = document.getElementsByTagName("BUTTON");
		for(var i=0; i<bts.length; i++) {
			bts[i].disabled=true;
		}
	};
	

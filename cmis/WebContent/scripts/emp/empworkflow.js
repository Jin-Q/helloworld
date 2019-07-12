	var contextPath = "/cmis-train";    //应用名称
	
	//提交地址
	var WF_SUBMIT_URL = "submitWorkFlow.do";
	//选择一下节点页面地址
	var WF_SELECT_NEXT_NODE_PAGE = "getSelectNextNodePage.do";
	//挂起的地址
	var WF_HANG_URL = "hangWorkFlow.do";
	//唤醒的地址
	var WF_WAKE_URL = "wakeWorkFlow.do";
	//挂起的地址
	var WF_Custom_HANG_URL = "customHangWorkFlow.do";
	//唤醒的地址
	var WF_Custom_WAKE_URL = "customWakeWorkFlow.do";
	//否决的地址
	var WF_Custom_Disagree_URL = "customDisagreeWorkFlow.do";
	//临时ACTION存放变量
	var WF_SUBMIT_FORM_ACTION;
	//提交表单变量
	var WF_SUBMIT_FORM;
	
	
	// 自定义否决的方法，（用于否决操作）
	var doCustomDisagree = function(form) {
		
		echain_common_op(form,WF_Custom_Disagree_URL,"getSuggestContentPage.do");
	}
		
	// 自定义挂起的方法，（用于取消操作）
	var doCustomHang = function(form) {
		
		echain_common_op(form,WF_Custom_HANG_URL,"getSuggestContentPage.do");
	}
	
	//自定义唤醒的方法
	var doCustomWake = function(form) {
		echain_common_op(form,WF_Custom_WAKE_URL,"getSuggestContentPage.do");
	}
	
	// 挂起的方法
	var doHang = function(form) {
		
		echain_common_op(form,WF_HANG_URL,"getSuggestContentPage.do");
	}
	//唤醒的方法
	var doWake = function(form) {
		echain_common_op(form,WF_WAKE_URL,"getSuggestContentPage.do");
	}
	
	var doWithdraw = function(form) {
		echain_withdraw(form,"withdrawUser.do","getSelectWithdrawUserPage.do");	
	}
	
	var doSave = function(form) {
		try{
			if(!canSave()) return;
		}catch(e){};
		echain_save(form,"saveWorkFlow.do");
		//echain_save(button,"saveWorkFlow2Blank.do");
	}
	
	var _doSubmitWorkFlow =function(form){
		 
		var wfsign = "";
		var scene =  "";
		var pkCol = "";
		var seq = "";
		
		try{
			wfsign = top.infoFrame.document.all("wfsign").value;
			scene =  top.infoFrame.document.all("scene").value;
			pkCol =  top.infoFrame.document.all("pkCol").value;
			seq   =  top.infoFrame.document.all("seq").value;
			sid   =  top.infoFrame.document.all("EMP_SID").value;
		}catch(e){
			/** 不在流程中，不作检查处理 */
			/**TODO 最好能有固定的域表明正在审批过程中 会不会死循环？？ */
 	
			_doSubmitWorkFlow(form);
		    return ;
		}
		var url = "CheckAllAuthExtWF.do?&EMP_SID=" + sid + "&wfsign=" + wfsign
                + "&scene=" + scene + "&pkCol=" + pkCol + "&seq=" + seq;

		var handleSuccess = function(o){
			
			if(o.responseText != undefined){
				try {
					var jsonstr = new Array();
					jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("检查审批扩展权限失败："+o.responseText);
					return;
				}
				var msg = jsonstr["ERRINFO"];
				
				if(msg.MSG != ''){
					alert("申请单填写有误：\n\r" + msg.MSG);
					return ;
				} else {
					_doSubmitWorkFlow(form);
				}
			}
		};
		var handleFailure = function(o){
			alert("申请单检查失败");
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('post', url, callback);
   }
	
	
	
	var doSubmitWorkFlow = function(form) {
		var formObj = form;
//		try{
//			if(!formObj.onsubmit())return;
//		} catch(e){};
//		try{
//			if(!canSubmit()) return;
//		}catch(e){};
		//&&document.getElementById("savebeforesubmit").value=='000' 暂时去掉
		if(document.getElementById("savebeforesubmit")){
			//提交前先保存，目的为了计算后续路由条件
		 
				var handleSuccess = function(o){
					try {
						var jsonstr = eval("("+o.responseText+")");
							
					}catch(e) {
						alert(o.responseText);
						return;
					}
				 	echain_submit(form,WF_SUBMIT_URL,WF_SELECT_NEXT_NODE_PAGE);	
				};		
				var handleFailure = function(o){	
					alert(o.responseText);
				};		
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var postData = YAHOO.util.Connect.setForm(formObj);			
				url = contextPath + "/saveWorkFlow2Blank.do";
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
			 
			if(document.getElementById("button_save"))
				document.getElementById("button_save").disabled = true;
			if(document.getElementById("button_submit"))
				document.getElementById("button_submit").disabled = true;
			if(document.getElementById("button_jump"))
				document.getElementById("button_jump").disabled = true;
		}
		else{
			alert("你页面中没有savebeforesubmit对象，确定提交时不需要保存？");
			echain_submit(form,"submitWorkFlow.do","getSelectNextNodePage.do");
		}		
	}
	
	var doBatchSubmit = function(form) {
		echain_submit(form,"batchsubmitWorkFlow.do","getSelectNextNodePage.do");
	}
	
	var doJump = function(form) {
		var formObj = form;
//		try {
//			if(!formObj.onsubmit())return;
//		}catch(e){}
//		try{
//			if(!canSubmit()) return;
//		}catch(e){};
		//&&document.getElementById("savebeforesubmit").value=='000' 暂时去掉
		if(document.getElementById("savebeforesubmit")) {
			//提交前先保存，目的为了计算后续路由条件
			var handleSuccess = function(o){				
				echain_submit(form, "jumpWorkFlow.do","getSelectWFNodePage.do");				
			};		
			var handleFailure = function(o){	
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};			
			
			var postData = YAHOO.util.Connect.setForm(formObj);			
			url = contextPath + "/saveWorkFlow2Blank.do";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
			
			if(document.getElementById("button_save"))
				document.getElementById("button_save").disabled = true;
			if(document.getElementById("button_submit"))
				document.getElementById("button_submit").disabled = true;
			if(document.getElementById("button_jump"))
				document.getElementById("button_jump").disabled = true;
		}
		else{
			echain_submit(form, "jumpWorkFlow.do","getSelectWFNodePage.do");
		}		
	}
	
	/** 打回后，被打回人直接提交回打回人 */
	var doCallback = function(form) {
		var formObj = form;
//		try {
//			if(!formObj.onsubmit())return;
//		}catch(e) {}
//		try{
//			if(!canSubmit()) return;
//		}catch(e){};
		//&&document.getElementById("savebeforesubmit").value=='000' 暂时去掉
		if(document.getElementById("savebeforesubmit")){
			//提交前先保存，目的为了计算后续路由条件
			var handleSuccess = function(o){				
				echain_submit(form,"callBackWorkFlow.do","getSelectWFTreatedNodePage.do");				
			};		
			var handleFailure = function(o){	
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};			
			
			var postData = YAHOO.util.Connect.setForm(formObj);			
			url = contextPath + "/saveWorkFlow2Blank.do";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
		else{
			echain_submit(form,"callBackWorkFlow.do","getSelectWFTreatedNodePage.do");
		}		
	}
	
	/** 打回后,被打回人重新一步步提交 */
	var doCallback4ReSubmit = function(form) {
		var formObj = form;
//		try {
//			if(!formObj.onsubmit())return;
//		}catch(e) {}
//		try{
//			if(!canSubmit()) return;
//		}catch(e){};
		//&&document.getElementById("savebeforesubmit").value=='000' 暂时去掉
		if(document.getElementById("savebeforesubmit")){
			//提交前先保存，目的为了计算后续路由条件
			var handleSuccess = function(o){				
				echain_submit(form,"jumpWorkFlow.do","getSelectWFTreatedNodePage2.do");				
			};		
			var handleFailure = function(o){	
			};		
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};			
			
			var postData = YAHOO.util.Connect.setForm(formObj);			
			url = contextPath + "/saveWorkFlow2Blank.do";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
		else{
			echain_submit(form,"jumpWorkFlow.do","getSelectWFTreatedNodePage2.do");
		}
	}
	
	var doCancel = function(form) {
		try{
			if(!canCancel()) return;
		}catch(e){};
		echain_common_op(form,"cancelWorkFlow.do","getSuggestContentPage.do");
	}	
	
	var doAgain = function(form) {
		try{
			if(!canAgain()) return;
		}catch(e){};
		echain_common_op(form,"requestAgainWorkFlow.do","getSuggestContentPage.do");
	}
	
	var doReturnback = function(form) {
		try{
			if(!canReturnback()) return;
		}catch(e){};
		echain_common_op(form,"returnBackWorkFlow.do","getSuggestContentPage.do");
	}	
	
	var doTrack = function(form) {
		echain_track(form,"echain/studio/eChainMonitor.jsp");
	}
	
	var doViewcomment = function(form) {
		echain_viewcomment(form,"viewSuggestContent.do");
	}
	
	//-----------------------------------------------------------------------------
	//流程通用提交的动作（如提交、批量提交、跳转、打回）
	//属性:element(按钮对象);submitUrl(提交的url);selectUrl(打开选择下一节点及处理人界面的url)
	//-----------------------------------------------------------------------------
	function echain_submit(form,submitUrl,selectUrl){
		var formObj = form;
		var instanceid = document.getElementById("instanceid").value;
		var nodeid = document.getElementById("nodeid").value;
		var currentuserid = document.getElementById("currentuserid").value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		
		
		
		try {
			//主表单提交
			if(document.getElementById("button_save"))
				document.getElementById("button_save").disabled = true;
			if(document.getElementById("button_submit"))
				document.getElementById("button_submit").disabled = true;
			if(document.getElementById("button_jump"))
				document.getElementById("button_jump").disabled = true;
			//也可能是子表单提交
			if(window.parent.document.getElementById("button_save"))
				window.parent.document.getElementById("button_save").disabled = true;
			if(window.parent.document.getElementById("button_submit"))
				window.parent.document.getElementById("button_submit").disabled = true;
			if(window.parent.document.getElementById("button_jump"))
				window.parent.document.getElementById("button_jump").disabled = true;
		} catch(e){}
		
		//打开选择下一节点及处理人的界面
		var url = contextPath + "/" + selectUrl +"?instanceid="+instanceid+"&nodeid="+nodeid+"&currentuserid="+currentuserid+"&EMP_SID="+sessionId;
		//var retObj = window.showModalDialog(url,'selectPage','dialogHeight:400px;dialogWidth:600px;help:no;resizable:no;status:no;');
		//
		
		//将最终提交的URL传递过去----------------
		if(formObj.submitUrl){///已有，则直接赋值
			formObj.submitUrl.value = submitUrl;
		} else {
			input = document.createElement("input");
			input.type="hidden";
			input.name = "submitUrl";
			input.id = "submitUrl";
			input.value = submitUrl;
			
			formObj.appendChild(input);
		}
		
		//将表单的action属性与表单本身保存下来，为了选择节点后，回调时不用再去取
		
		WF_SUBMIT_FORM_ACTION = formObj.action;
		WF_SUBMIT_FORM = formObj;
		//--------------------------------------
		
		//formObj.target="_blank";
		formObj.target="dialogiF";
		formObj.action = url;
		formObj.submit();
	};
		
	
	//配合表单提交式节点选择的回调函数
	//回调函数的表单是之前调用函保存下来的
	function echain_submit_callback(retObj,submitUrl) {
		//alert("echain_submit_callback");
		//var formObj = document.forms[0];
		
		var formObj = WF_SUBMIT_FORM;
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null){
			try {
				//主表单提交
				if(document.getElementById("button_save"))
					document.getElementById("button_save").disabled = false;
				if(document.getElementById("button_submit"))
					document.getElementById("button_submit").disabled = false;
				if(document.getElementById("button_jump"))
					document.getElementById("button_jump").disabled = false;
				//也可能是子表单提交
				if(window.parent.document.getElementById("button_save"))
					window.parent.document.getElementById("button_save").disabled = false;
				if(window.parent.document.getElementById("button_submit"))
					window.parent.document.getElementById("button_submit").disabled = false;
				if(window.parent.document.getElementById("button_jump"))
					window.parent.document.getElementById("button_jump").disabled = false;
			} catch(e){}
			return;
		}
		var status = retObj[0];
		if(status != true){
			try {
			//主表单提交
				if(document.getElementById("button_save"))
					document.getElementById("button_save").disabled = false;
				if(document.getElementById("button_submit"))
					document.getElementById("button_submit").disabled = false;
				if(document.getElementById("button_jump"))
					document.getElementById("button_jump").disabled = false;
				//也可能是子表单提交
				if(window.parent.document.getElementById("button_save"))
					window.parent.document.getElementById("button_save").disabled = false;
				if(window.parent.document.getElementById("button_submit"))
					window.parent.document.getElementById("button_submit").disabled = false;
				if(window.parent.document.getElementById("button_jump"))
					window.parent.document.getElementById("button_jump").disabled = false;
			} catch(e) {}
			return;
		}
		document.getElementById("nextnodeid").value = retObj[2];
		if(retObj[3] != null)
			document.getElementById("nextnodeuser").value = retObj[3];
		
		//提交节点
		//alert(formObj);
		url = contextPath + "/" +submitUrl;
		formObj.target="infoframe";
		formObj.action = url;
		//formObj.submitbutton.click();
		formObj.submit();
	};
	
	//-----------------------------------------------------------------------------
	//流程撤销办理人的动作
	//属性:element(按钮对象);submitUrl(提交的url);selectUrl(打开选择下一节点及处理人界面的url)
	//-----------------------------------------------------------------------------
	function echain_withdraw(form,submitUrl,selectUrl){			
		var formObj = form;
		var instanceid = document.getElementById("instanceid").value;
		var nodeid = document.getElementById("nodeid").value;
		var currentuserid = document.getElementById("currentuserid").value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;

		//打开选择下一节点及处理人的界面
		var url = contextPath + "/" + selectUrl +"?instanceid="+instanceid+"&nodeid="+nodeid+"&currentuserid="+currentuserid+"&EMP_SID="+sessionId;
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:400px;dialogWidth:600px;help:no;resizable:no;status:no;');
		
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null){
			return;
		}
		var status = retObj[0];
		if(status != true){
			return;
		}
		document.getElementById("nextnodeuser").value = retObj[2];
		//提交节点
		url = contextPath + "/" +submitUrl;
		formObj.action = url;
		//formObj.submitbutton.click();
		formObj.submit();
	};
	
	//-----------------------------------------------------------------------------
	//流程保存的动作
	//属性:element(按钮对象);saveUrl(保存的url)
	//-----------------------------------------------------------------------------
	function echain_save(form,saveUrl){
		var formObj = form;
		url = contextPath + "/" + saveUrl;
		formObj.action = url;
		//formObj.submitbutton.click();
		formObj.submit();
	};
	
	//-----------------------------------------------------------------------------
	//流程通用操作（如撤办、退回、拿回、手工催办等）
	//属性:element(按钮对象);opUrl(通用操作的url);contentUrl(打开输入意见页面的url)
	//-----------------------------------------------------------------------------
	function echain_common_op(form,opUrl,contentUrl){
		var formObj = form;
		var instanceid = document.getElementById("instanceid").value;
		var nodeid = document.getElementById("nodeid").value;
		var currentuserid = document.getElementById("currentuserid").value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		//打开输入意见的页面
		var url = contextPath + "/" + contentUrl + "?EMP_SID="+sessionId;
		var retObj = window.showModalDialog(url,'setContentPage','dialogHeight:400px;dialogWidth:600px;help:no;resizable:no;status:no;');
		//返回数组:[状态:true/false;意见];若没有返回值,或返回状态不为true,则表示取消
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		//设置action并submit
		url = contextPath + "/" + opUrl + "?instanceid="+instanceid+"&nodeid="+nodeid+"&currentuserid="+currentuserid+"&EMP_SID="+sessionId;
		formObj.target="infoframe";
		formObj.action = url;
		//formObj.submitbutton.click();
		formObj.submit();
	};
	
	//-----------------------------------------------------------------------------
	//流程跟踪的动作
	//属性:element(按钮对象);trackUrl(流程跟踪的url)
	//-----------------------------------------------------------------------------
	function echain_track(form,trackUrl){
		var instanceid = document.getElementById("instanceid").value;
		var currentuserid = document.getElementById("currentuserid").value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		var url = contextPath + "/" + trackUrl + "?EMP_SID=" + sessionId+"&instanceid="+instanceid+"&currentuserid="+currentuserid;
		window.open(url,"流程跟踪", "height=768, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no");
		//window.showModalDialog(url,'流程跟踪','dialogHeight:400px;dialogWidth:750px;help:no;resizable:no;status:no;location:no');
	};
	
	//-----------------------------------------------------------------------------
	//查看意见的动作
	//属性:element(按钮对象);viewcommentUrl(查看意见的url)
	//-----------------------------------------------------------------------------
	function echain_viewcomment(form,viewcommentUrl){
		var instanceid = document.getElementById("instanceid").value;
		var currentuserid = document.getElementById("currentuserid").value;
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		var url = contextPath + "/" + viewcommentUrl + "?EMP_SID=" + sessionId+"&instanceid="+instanceid+"&currentuserid="+currentuserid;
		window.showModalDialog(url,'viewCommentPage','dialogHeight:400px;dialogWidth:710px;help:no;resizable:no;status:no;location:no');
	};
	
	
	function getExpressInfo(pageurl){
 
		var wfsign = "";
		var scene =  "";

		try{
			wfsign = top.infoFrame.document.all("wfsign").value;
			scene =  top.infoFrame.document.all("scene").value;
		}catch(e){
			/** 不在流程中，不作检查处理 */
			/**TODO 最好能有固定的域表明正在审批过程中  */
			//alert("不在流程中，不作检查处理");
			return ;
		}
		var sessionId = document.getElementsByName("EMP_SID")[0].value;
		var url = "getCheckExpression.do?&EMP_SID=" + sessionId + "&wfsign=" + wfsign
		                 + "&scene=" + scene
		                 + "&pageurl=" + pageurl;
		                 
		
		var handleSuccess = function(o){
			
			if(o.responseText != undefined){
				try {
					var jsonstr = new Array();
					jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("检查审批扩展权限失败："+o.responseText);
					return;
				}
				var iColl = jsonstr["ExpList"];
				
				for(var n=0; n<iColl.length; n++){
					//alert(iColl[n].field_typ + "  " + iColl[n].field_nm + " " + iColl[n].expr);
					eval(iColl[n].expr);
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('post', url, callback);	
	};
	
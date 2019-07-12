<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%
/**
业务提交，发起流程说明：
1. 设置emp表单元素
	必须有：
		WfiJoin.table_name._setValue('XXX');
		WfiJoin.pk_col._setValue('XXX');
		WfiJoin.pk_value._setValue('XXX');
		WfiJoin.wfi_status._setValue('XXX');  //提交流程时，业务当前的审批状态
		WfiJoin.status_name._setValue('XXX'); //缺省为approve_status
		WfiJoin.appl_type._setValue('XXX');   //通过申请类型发起流程
	其他的表单元素可根据需求设置

2. 如果流程发起人需要填写提交流程意见，则调用initWFSubmit方法时传入true,即initWFSubmit(true)
*/

%>
<script src="<emp:file fileName='scripts/workflow/empworkflow.js'/>" type="text/javascript"></script>
<script type="text/javascript">

	//发起流程。isSuggest传入true表示第一个节点需要保存意见。
	function initWFSubmit(isSuggest) {
		//1.检查基本表单数据完整性
		var modelId = WfiJoin.table_name._getValue();
		var pkCol = WfiJoin.pk_col._getValue();
		var pkValue = WfiJoin.pk_value._getValue();
		var wfiStatus = WfiJoin.wfi_status._getValue();
		var statusName = WfiJoin.status_name._getValue();
		var applType = WfiJoin.appl_type._getValue();
		if(modelId==''||pkCol==''||pkValue==''||statusName==''||applType=='') {
			alert('流程接入表单信息没有设置好，请检查！');
			return;
		}
		if(wfiStatus!='' && wfiStatus!='000' && wfiStatus!='991' && wfiStatus!='992' && wfiStatus!='993'){
		//	alert('当前流程处于审批中状态或已经结束,不能再次提交审批!');
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请！');
			return;
		}
		//2.执行风险拦截检查
		var handleSuccess1 = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var _preventIdLst = jsonstr.preventList;
				var _wfSign = jsonstr.wfSign;
				WfiJoin.prevent_list._setValue(_preventIdLst); //设置到中间表，方便审批中可能调用
				if(_preventIdLst!=null && _preventIdLst!='null' && _preventIdLst!='') {
					var _urlPrv = "<emp:url action='procRiskInspect4WF.do'/>&applType="+applType+"&pkVal="+pkValue + "&modelId=" + modelId + "&pvId=" + _preventIdLst+"&wfSign=" +_wfSign +"&rd=" + Math.random();
			        var _retObj = window.showModalDialog(_urlPrv,"preventPage","dialogHeight=500px;dialogWidth=850px;");
					if(!_retObj || _retObj == '2' || _retObj == '5'){
						if( _retObj == '5'){
							alert("执行风险拦截有错误，请检查！");
						}
						return;
					}
				}
				//3.检查通过，开始发起流程
				doStart(isSuggest);
			}
		};
		var handleFailure1 = function(o){
		};
		var callback1 = {
			success:handleSuccess1,
			failure:handleFailure1
		};
		var postData = YAHOO.util.Connect.setForm(document.getElementById('submitForm4WF'));
		var url = "<emp:url action='getWfPreventList.do'/>";
		var obj1 = YAHOO.util.Connect.asyncRequest("POST", url, callback1, postData);
	}
    //单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 start
	function initWFSubmitSub(isSuggest) {
		//1.检查基本表单数据完整性
		var modelId = WfiJoin.table_name._getValue();
		var pkCol = WfiJoin.pk_col._getValue();
		var pkValue = WfiJoin.pk_value._getValue();
		var wfiStatus = WfiJoin.wfi_status._getValue();
		var statusName = WfiJoin.status_name._getValue();
		var applType = WfiJoin.appl_type._getValue();
		if(modelId==''||pkCol==''||pkValue==''||statusName==''||applType=='') {
			alert('流程接入表单信息没有设置好，请检查！');
			return;
		}
		if(wfiStatus!='' && wfiStatus!='000' && wfiStatus!='991' && wfiStatus!='992' && wfiStatus!='993'){
		//	alert('当前流程处于审批中状态或已经结束,不能再次提交审批!');
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请！');
			return;
		}
		WfiJoin.prevent_list._setValue(""); 
		//2.检查通过，开始发起流程
		doStart(isSuggest);
		
	}
	//单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 end
	
    function doStart(isSuggest) {
		var form = document.getElementById('submitForm4WF');
		var url = form.action;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var wfResult = jsonstr.wfResult;
				var wfState = wfResult.wfState;
				//如果流程状态为发起，则继续
				if(wfState == "1") {
					//为表单元素赋值
					document.getElementById("instanceId").value = wfResult.instanceId;
					document.getElementById("nodeId").value = wfResult.nodeId;
					document.getElementById("wfSign").value = wfResult.wfSign;
					document.getElementById("scene").value = wfResult.scene;
					document.getElementById("isDraft").value = wfResult.isdraft;
					document.getElementById("pkVal").value = WfiJoin.pk_value._getValue();
					document.getElementById("applType").value = WfiJoin.appl_type._getValue();
					document.getElementById("modelId").value = WfiJoin.table_name._getValue();
					document.getElementById("pkCol").value = WfiJoin.pk_col._getValue();
					document.getElementById("statusName").value = WfiJoin.status_name._getValue();
					document.getElementById("commentSign").value = '10';
					document.getElementById("commentContent").value = '【提交审批】';
					if(isSuggest==true || isSuggest=='true') {
						var sug = window.showModalDialog("getFirstCommentPage.do?EMP_SID="+'<%=request.getParameter("EMP_SID")%>',"suggestPage","dialogHeight=260px;dialogWidth=500px;");
						if(sug =='cancelStart'){
						  doCancel(form);
 						  return ;
						}
						if(sug==null || sug=='') {
							//alert('请输入好流程意见！');
							return;
						}
						document.getElementById("commentContent").value = sug;
					}
					//submitWorkFlow(form);
					doWorkFlowAgent(submitWorkFlow, form);
				}
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('post',url, callback, postData);
    }

  	//重置表单元素
	//防止试图执行不同操作时，可能改变了原来的值。如子流程后，会改变表单实例号，如果再直接执行主流程提交，将会出错
	var retsetFormValue = function(form) {
		form.instanceId.value = "${context.instanceId}";
		form.mainInstanceId.value = "${context.mainInstanceId}";
		form.nodeId.value = "${context.nodeId}";
		form.mainNodeId.value = "${context.mainNodeId}";
		form.subWfType.value='';
		form.subWfSign.value='';
		form.commentSign.value='';
		form.commentContent.value='';
		commentContentCopy._setValue('');
		form.callBackModel.value='';
		form.entrustModel.value='';
	}
  
	</script>
		
	<form id="submitForm4WF" action="<emp:url action='initiateWF.do'/>" method="POST">
		<emp:text id="WfiJoin.table_name" label="业务主表模型" maxlength="64" required="true" hidden="true"/>
		<emp:text id="WfiJoin.pk_col" label="主键字段" maxlength="32" required="true" hidden="true"/>
		<emp:text id="WfiJoin.pk_value" label="主键值" maxlength="32" required="true" hidden="true"/>
		<emp:text id="WfiJoin.cus_type" label="客户类型" maxlength="3" hidden="true" />
		<emp:text id="WfiJoin.cus_id" label="客户码" maxlength="30" required="false" hidden="true"/>
		<emp:text id="WfiJoin.cus_name" label="客户名称" maxlength="80" required="false" hidden="true"/>
		<emp:text id="WfiJoin.appl_type" label="申请类型" maxlength="3" required="false" hidden="true"/>
		<emp:text id="WfiJoin.wfsign" label="流程标识" maxlength="32" required="false" hidden="true"/>
		<emp:text id="WfiJoin.prd_pk" label="产品PK" maxlength="32" required="false" hidden="true"/>
		<emp:text id="WfiJoin.prd_name" label="产品名" maxlength="60" required="false" hidden="true"/>
		<emp:text id="WfiJoin.cert_type" label="证件类型" maxlength="2" required="false" hidden="true"/>
		<emp:text id="WfiJoin.cert_code" label="证件号" maxlength="20" required="false" hidden="true"/>
		<emp:text id="WfiJoin.term" label="期限" maxlength="16" required="false" hidden="true"/>
		<emp:text id="WfiJoin.ruling_ir" label="基准利率（年）" maxlength="16" required="false" hidden="true"/>
		<emp:text id="WfiJoin.reality_ir_y" label="月利率（年）" maxlength="16" required="false" hidden="true"/>
		<emp:text id="WfiJoin.amt" label="金额" maxlength="16" required="false" hidden="true"/>
		<emp:text id="WfiJoin.input_id" label="业务登记人" maxlength="20" required="false" hidden="true"/>
		<emp:text id="WfiJoin.input_br_id" label="业务登记机构" maxlength="20" required="false" hidden="true"/>
		<emp:text id="WfiJoin.apply_date" label="业务申请日期" maxlength="10" required="false" hidden="true"/>
		<emp:text id="WfiJoin.prevent_list" label="风险拦截list多个用英文逗号分隔" maxlength="200" hidden="true" />
    	<emp:text id="WfiJoin.status_name" label="业务表审批状态字段名称" maxlength="20" hidden="true" />
    	<emp:text id="WfiJoin.wfi_end_org" label="流程办结机构" maxlength="20" hidden="true" />
    	<emp:text id="WfiJoin.wfi_status" label="流程审批状态" maxlength="10" hidden="true" />
    	
		<input type="hidden" id="instanceId" name="instanceId" value="${context.instanceId}" />
		<input type="hidden" id="mainInstanceId" name="mainInstanceId" value="${context.mainInstanceId}" />
		<input type="hidden" id="currentUserId" name="currentUserId" value="${context.currentUserId}"/>
		<input type="hidden" id="nodeId" name="nodeId" value="${context.nodeId}"/>
		<input type="hidden" id="nodeName" name="nodeName" value="${context.nodeName}"/>
		<input type="hidden" id="mainNodeId" name="mainNodeId" value="${context.mainNodeId}"/>
		<input type="hidden" id="nextNodeId" name="nextNodeId"/>
		<input type="hidden" id="nextNodeUser" name="nextNodeUser"/>
		<input type="hidden" id="nextAnnouceUser" name="nextAnnouceUser"/>
		<input type="hidden" id="isProcessed" name="isProcessed" value="${context.isProcessed}"/>
		<input type="hidden" id="isDraft" name="isDraft" value="${context.isDraft}"/>
		<input type="hidden" id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}"/>
		<input type="hidden" id="scene" name="scene" value="${context.scene}"/>
		<input type="hidden" id="wfSign" name="wfSign" value="${context.wfSign}"/>
		<input type="hidden" id="subWfSign" name="subWfSign" value=""/>  <!-- 子流程标识 -->
		<input type="hidden" id="subWfType" name="subWfType" value=""/>  <!-- 子流程类型0.不允许;1.用户选择同步子流;2.用户选择异步子流;3.系统指定同步子流;4.系统指定异步子流 -->
		<input type="hidden" id="applType" name="applType" value="${context.applType}"/>
		<input type="hidden" id="modelId" name="modelId" value="${context.modelId}"/>
		<input type="hidden" id="statusName" name="statusName" value="${context.statusName}"/>
		<input type="hidden" id="pkVal" name="pkVal" value="${context.pkVal}"/>
		<input type="hidden" id="pkCol" name="pkCol" value="${context.pkCol}"/>
		<input type="hidden" id="commentSign" name="commentSign" value=""/><!-- 审批结论（意见标识） -->
		<input type="hidden" id="commentContent" name="commentContent" value=""/>
		<input type="hidden" id="commentContentOther" name="commentContentOther" value=""/> <!-- added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能   -->
		<input type="hidden" id="callBackModel" name="callBackModel"/> <!-- 退（打）回后提交方式 :1逐级提交,0提交给退回发起人-->
		<input type="hidden" id="entrustModel" name="entrustModel"/> <!-- 委托代办模式：0代办人办理，1原办理人代人都可以办理，2原办理人办理 -->
		<input type="hidden" id="ext" name="ext"/>
		<input type="hidden" id="isSpecial" name="isSpecial"> <!-- 泉州流程审批中调整参数，标识特殊处理的情况-授信 -->
		<input type="hidden" id="preventList" name="preventList" value="${context.preventList}"> <!-- 泉州流程审批中调整参数，风险拦截 -->
		<input type="hidden" id="callBackDiscs" name="callBackDiscs" value="" /> <!-- add by wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求 -->
		<input type="hidden" id="approveOpModel" name="approveOpModel"/> <!--1：打回可修改 0:打回不可修改 add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 -->
	</form>
 </emp:page>
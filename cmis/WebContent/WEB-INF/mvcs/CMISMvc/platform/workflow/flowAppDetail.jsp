<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.util.WorkFlowUtil"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIFormActionVO"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String serno = (String)context.getDataValue("pkVal");
String appUrl = (String)context.getDataValue("wfAppUrl");
/**modified by lisj 2014-11-26 授信流程关联关系信息改造  begin**/
//我的工作台待办事项标志
appUrl = appUrl+"&wf_flag=1";
String bizUrl = (String)context.getDataValue("wfBizUrl");
String preventList = (String)context.getDataValue("preventList");
String wfiStatus = (String)context.getDataValue("wfiStatus");
//modify by jiangcuihua 2019-03-16 context中可能不存在cus_id
String cusId = "";
if(context.containsKey("cus_id")){
	cusId = (String)context.getDataValue("cus_id");
}
/**modified by lisj 2014-11-26 授信流程关联关系信息改造  end**/
WFIInstanceVO instanceVO = (WFIInstanceVO)context.getDataValue("wfiInstanceVO");
WFIFormActionVO actionVO = instanceVO.getFormActionVO();
String instanceId = instanceVO.getInstanceId();
String nodeId = instanceVO.getNodeId();
String wfid = instanceVO.getWfId();
String subWfType = null;
String subWfSign = null;
if(actionVO.getCallsubflow()!=null) {
	subWfSign = (String)WorkFlowUtil.getWFNodeProperty(nodeId, "subwfid");
	subWfType = (String)WorkFlowUtil.getWFNodeProperty(nodeId, "nodeusesubwf");
}
String curUserName = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME);
/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  */
String commentContent = "";
/* added by yangzy 2015/04/07 审批意见查看改造 start  */
String commentContentFlag = "";
if(context.containsKey("commentContent")){
	commentContent = (String)context.getDataValue("commentContent");
	if(commentContent!=null&&!"".equals(commentContent)){
		commentContentFlag = "1";
	}
	/* added by yangzy 2015/04/07 审批意见查看改造 end  */
}
/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  */
%>
<emp:page>
<html>
<head>
<title>流程实例详情</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<style type="text/css">

.emp_field_textarea_textarea_copy {
	width: 700px;
	height: 150px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

</style>

<script type="text/javascript">
var updateFlag = "";
function doWorkFLow(op) {
	var form = document.getElementById('submitForm4WF');
	var isSpecial = '${context.isSpecial}';
	form.isSpecial.value = isSpecial;
	//校验流程意见是否超长
	if(!commentContentCopy._checkAll()){
		return;
	}
	var comment = commentContentCopy._getValue();
	form.commentContent.value = comment;
	/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  */
	var commentOther = commentContentCopyOther._getValue();
	form.commentContentOther.value = commentOther;
	/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  */
	var commentTmp = comment.replace(/(^\s+)|(\s+$)/g, "");
	if(commentTmp=='' && op!='gather' && op!='signIn' && op!='signOff' && op!='taskSignOff') {
		alert('请先输入好流程意见！');
		return;
	}
	if(op == 'save') {
	    /* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  start */
	    
		updateFlag = 'succ';
		doAddHiddenChildToForm(form, "op", op); 
	//	addBizVar(form); 
	//	form.commentSign.value = '10';
		if(updateFlag == 'succ'){ 
			doWorkFlowAgent(saveWorkFlow, form); 
		}
		/* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  end */
	} else if(op == 'submit') {
		//检查是否需要执行风险拦截
		/** 风险拦截不在此处进行，放到保存完进行提交时，由于审批中变更所以先进行保存操作后再进行风险拦截 edit by tangzf 2014-03-12 */
	/*	var _preventIdLst = '<%=preventList%>';
		if(_preventIdLst!=null && _preventIdLst!='null' && _preventIdLst!='') {
			var applType = '${context.applType}';
			var pkValue = '${context.pkVal}';
			var modelId = '${context.modelId}';
			var wfSign = '${context.wfSign}';
			var _urlPrv = "<emp:url action='procRiskInspect4WF.do'/>&applType="+applType+"&pkVal="+pkValue+"&modelId="+modelId+"&wfSign=" +wfSign +"&pvId="+_preventIdLst+"&rd="+Math.random()+"&nodeId=${context.nodeId }";
	        var _retObj = window.showModalDialog(_urlPrv,"preventPage","dialogHeight=500px;dialogWidth=850px;");
			if(!_retObj || _retObj == '2' || _retObj == '5'){
				if( _retObj == '5'){
					alert("执行风险拦截有错误，请检查！");
				}
				return;
			}
		}*/
		doAddHiddenChildToForm(form, "op", op);
		if(isSpecial==null||isSpecial==""){
			addBizVar(form);
			form.commentSign.value = '10';
			doWorkFlowAgent(submitWorkFlow, form);
		}else{
			form.commentSign.value = '10';
			addBizVar(form);
		}
	} else if(op == 'assist') {
		form.commentSign.value = '21';
		doWorkFlowAgent(assistWorkFlow, form);
	} else if(op == 'change') {
		form.commentSign.value = '11';
		doWorkFlowAgent(changeWorkFlow, form);
	} else if(op == 'returnBack') {
		form.commentSign.value = '40';
		doWorkFlowAgent(returnBackWorkFlow, form);
	} else if(op == 'callBack') {
		form.commentSign.value = '30';
		doWorkFlowAgent(callBackWorkFlow, form);
	} else if(op == 'jump') {
		form.commentSign.value = '50';
		doWorkFlowAgent(jumpWorkFlow, form);
	} else if(op == 'cancel') {
		//撤办；流程发起人或流程管理员执行流程撤办操作。在我发起的任务-未办结、管理后台提供
	} else if(op == 'hang') {
		form.commentSign.value = '80';
		doWorkFlowAgent(hangWorkFlow, form);
	} else if(op == 'gather') {
		doStartGather(form);
	} else if(op == 'callSubFlow') {
		form.commentSign.value = '10';
		form.subWfSign.value = '<%=subWfSign%>';
		form.subWfType.value = '<%=subWfType%>';
		doSubWorkFlow(form);
	} else if(op == 'signIn') {
		doSignInWorkFlow(form);
	} else if(op == 'signOff') {
		doSignOffWorkFlow(form);
	} else if(op == 'taskSignOff') {
		doTaskSignOff(form);
	} else if(op == 'reject') {
		form.commentSign.value = '20';
		doWorkFlowAgent(customDisagree, form);
	}
}

function doWfFaster() {
	var val = commentContentCopy._getValue();
	val = val.replace('【同意】','').replace('【不同意】','');
	commentContentCopy._setValue(wfFaster._getValue()+val);
}

function addBizVar(form) {
	var isSpecial = '${context.isSpecial}';
	//检查是否有审批变更
	var bizUrl = '<%=bizUrl%>';
	if(bizUrl!=null && bizUrl!='' && bizUrl!='null') {
		if(isSpecial==null||isSpecial==''){
			onVarSubmit();//调用变量修改页面方法
		
			WfiBizVarRecord._toForm(form);
			WfiVarFlag._toForm(form);
			WfiVarType._toForm(form);
			WfiVarName._toForm(form);
			WfiVarDisp._toForm(form);
		}else{
			onVarSubmit(form);//调用变量修改页面方法
		}
	}else{
		if(isSpecial!=null&&isSpecial!=''){
			doWorkFlowAgent(submitWorkFlow, form);
		}
	}
}

function hiddenShow(obj, divId) {
	var disp = document.getElementById(divId).style.display;
	if(disp == 'none') {
		obj.src = "<%=request.getContextPath()%>/images/workflow/up.png";
		obj.alt = "收起";
		document.getElementById(divId).style.display = 'block';
	} else {
		obj.src = "<%=request.getContextPath()%>/images/workflow/down.png";
		obj.alt = "展开";
		document.getElementById(divId).style.display = 'none';
	}
}
/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  */
function hiddenShowComment(obj, divId) {
	var disp = document.getElementById(divId).style.display;
	if(disp == 'none') {
		obj.src = "<%=request.getContextPath()%>/images/workflow/up.png";
		obj.alt = "收起";
		document.getElementById(divId).style.display = 'block';
	} else {
		obj.src = "<%=request.getContextPath()%>/images/workflow/down.png";
		obj.alt = "展开";
		document.getElementById(divId).style.display = 'none';
	}
}
/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  */
	function onLoad(){
	    /* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  */
		var isSpecial = '${context.isSpecial}';
		//检查是否有审批变更
		var bizUrl = '<%=bizUrl%>';
		if(bizUrl!=null && bizUrl!='' && bizUrl!='null') {
			onVarLoad();//调用变量修改页面方法
		}
		/* modified by yangzy 2015/04/07 审批意见查看改造 start  */
		var applType = '${context.applType}';
		var commentContentFlag = '<%=commentContentFlag%>';
		if(commentContentFlag=="1"){
			var obj = document.getElementById('alt_slide');
			obj.src = "<%=request.getContextPath()%>/images/workflow/up.png";
			obj.alt = "收起";
			document.getElementById('div_adviceOther').style.display = 'block';
		}
		/* modified by yangzy 2015/04/07 审批意见查看改造 end  */
		/* added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  */
		//业务类型为：单一法人授信，单一法人授信复议，个人授信复议，合作方额度申请，联保小组授信,个人额度申请，融资性担保公司授信
		if(applType =="003" || applType =="0061" || applType =="0062" || applType=="3231"
			|| applType=="3241" || applType=="3281" || applType=="372"){
				doCheckCusRelInfo();
		}
	};
	/**modified by lisj 2014-11-26 授信流程关联关系信息改造  begin**/
	//校验客户关联关系信息
	function doCheckCusRelInfo(){
		var cusId  = '<%=cusId%>';
		if(cusId != null && cusId != ""){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "existence"){
						if(confirm("该客户存在我行存在关联信息，请点击【确定】进行查看！")){
							var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+cusId;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height=600,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
						}
					}else if(flag == "existence4CusIndvi"){
						if(confirm("该客户存在我行存在关联信息，请点击【确定】进行查看！")){
							var url = '<emp:url action="GetIndivCusRelTreeOp.do"/>?cus_id='+ cusId+"&cusType=indiv";
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height=600,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
						}
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
			var url = "<emp:url action='ckeckCusRelInfoOp.do'/>?cus_id="+cusId;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}	
	};
	/**modified by lisj 2014-11-26 授信流程关联关系信息改造  end**/
</script>

</head>
<body  onload="onLoad();">
<emp:tabGroup mainTab="wfApproveTab" id="instanceTab">
<emp:tab label="申请信息" id="appTab" needFlush="false" url="<%=appUrl %>"/>
<emp:tab label="流程办理" id="wfApproveTab" needFlush="true">

	<div align="right">
	<span>当前审批步骤：【<%=instanceVO.getNodeName() %>】  当前办理人：【<%=curUserName %>】</span>
	</div>
	
	<% if(bizUrl!=null && !bizUrl.trim().equals("")) {%>
	<jsp:include page="<%=bizUrl %>" flush="true"/><br>
	<%} %>
	
	<emp:gridLayout id="wfGridLayout" title="我的流程意见" maxColumn="1">
		<emp:select id="wfFaster" label="快捷意见：" dictname="WF_APP_SHORTCUT"  onclick="doWfFaster()" hidden="true"></emp:select>
		<emp:textarea id="commentContentCopy" label="流程意见：" maxlength="4000" cssElementClass="emp_field_textarea_textarea_copy" defvalue='<%=(instanceVO.getCommentList()!=null&&instanceVO.getCommentList().size()>0&&instanceVO.getCommentList().get(0).getCommentContent()!=null)?instanceVO.getCommentList().get(0).getCommentContent():"" %>'></emp:textarea>
	</emp:gridLayout>
    <!-- added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start  -->
	<fieldset><legend>流程意见(附)[流程意见扩展]</legend>
	<img id="alt_slide" alt="展开" src="<%=request.getContextPath()%>/images/workflow/down.png" align="left" onclick="hiddenShowComment(this,'div_adviceOther')" onmouseover="this.style.cursor='hand'">
	<div id="div_adviceOther" style="margin: 13px 200px 23px 173px;display: none;">
		<emp:textarea id="commentContentCopyOther" label="流程意见(附)" maxlength="4000"  cssElementClass="emp_field_textarea_textarea_copy" defvalue='<%=commentContent%>'></emp:textarea>
	</div>
	</fieldset>	
	<!-- added by yangzy 2014/11/27 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end  -->
	<div align="center">
		<% if(actionVO.isSave()) { %>
			<button id="save" onclick="doWorkFLow('save')">保  存</button>
		<%} %>
		<% if(actionVO.isSubmit()&&!"991".equals(wfiStatus)&&!"992".equals(wfiStatus)&&!"993".equals(wfiStatus)) { %>
			<button id="submit" onclick="doWorkFLow('submit')">提  交</button>
		<%} %>
		<% if(actionVO.isAssist()) { %>
			<button id="assist" onclick="doWorkFLow('assist')">协助办理</button>
		<%} %>
		<% if(actionVO.isChange()) { %>
			<button id="change" onclick="doWorkFLow('change')">转  办</button>
		<%} %>
		<% if(actionVO.isReturnback()&&(1==2)) {//退回按钮暂时不放开 2014-1-9 %>
			<button id="returnBack" onclick="doWorkFLow('returnBack')">退  回</button>
		<%} %>
		<% if(actionVO.isCallback()) {//原退回任一级按钮，现改为“退回” %>
			<button id="callBack" onclick="doWorkFLow('callBack')">退回</button>
		<%} %>
		<% if(actionVO.isJump()) { %>
			<button id="jump" onclick="doWorkFLow('jump')">跳  转</button>
		<%} %>
		<% if(false && actionVO.isCancel()) { //暂不实现 %>
			<button id="cancel" onclick="doWorkFLow('cancel')">撤  办</button>
		<%} %>
		<% if(actionVO.isHang()) { %>
			<button id="hang" onclick="doWorkFLow('hang')">挂  起</button>
		<%} %>
		<% if(actionVO.isGather()) { %>
			<button id="gather" onclick="doWorkFLow('gather')">发起会办</button>
		<%} %>
		<% if(actionVO.getCallsubflow()!=null) { %>
			<button id="callSubFlow" onclick="doWorkFLow('callSubFlow')"><%=actionVO.getCallsubflow() %></button>
		<%} %>
		<% if(actionVO.isSignin()) { %>
			<button id="signIn" onclick="doWorkFLow('signIn')">签  收</button>
		<%} %>
		<% if(actionVO.isSignoff()) { %>
			<button id="signOff" onclick="doWorkFLow('signOff')">撤销签收</button>
		<%} %>
		<% if(actionVO.isTasksignoff()) { %>
			<button id="taskSignOff" onclick="doWorkFLow('taskSignOff')">放回项目池</button>
		<%} %>
		<% if(actionVO.isReject()) { %>
			<button id="reject" onclick="doWorkFLow('reject')">否 决</button>
		<%} %>
	</div>
	
	<!--bengin 泉州银行审批历史与流程办理整合修改，cyg2013年12月27日 -->
	<fieldset><legend>流程审批历史</legend>
	<img alt="收起" src="<%=request.getContextPath()%>/images/workflow/up.png" align="right" onclick="hiddenShow(this,'div_advice_lcgz')" onmouseover="this.style.cursor='hand'">
	<div id="div_advice_lcgz">
	<iframe id="advice_wf_lcgz" frameborder="0" width="100%" 
				src='<%=request.getContextPath()%>/getWorkFlowSimHistory.do?&instanceId=<%=instanceId%>&nodeId=<%=nodeId%>&serno=<%=serno%>&EMP_SID=<%=request.getParameter("EMP_SID")%>'></iframe>
	</div>
	</fieldset>
	<!--end 泉州银行审批历史与流程办理整合修改，cyg2013年12月27日 -->
	
</emp:tab>

<!-- 泉州银行审批历史与流程办理整合修改，cyg2013年12月27日-->
<%//<emp:tab label="审批历史" id="wfHisTab" url="getWfApproveHis.do" reqParams="instanceId=${context.instanceId }&nodeId=${context.nodeId }" needFlush="true"/>%>

</emp:tabGroup>

</body>
</html>
</emp:page>
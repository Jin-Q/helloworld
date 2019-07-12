<%@page import="com.yucheng.cmis.platform.workflow.util.WorkFlowUtil"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIFormActionVO"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String appUrl = (String)context.getDataValue("wfAppUrl");
String bizUrl = (String)context.getDataValue("wfBizUrl");
WFIInstanceVO instanceVO = (WFIInstanceVO)context.getDataValue("wfiInstanceVO");
WFIFormActionVO actionVO = instanceVO.getFormActionVO();
String curUserId = instanceVO.getCurrentUserId();
String preUserId = instanceVO.getPreUser();
String preNodeId = instanceVO.getPreNodeId();
String instanceId = instanceVO.getInstanceId();
String nodeId = instanceVO.getNodeId();
String mng = request.getParameter("mng"); //是否流程实例管理功能，1是且未办结，2是且办结，其他否
String optype = request.getParameter("opType"); //操作类型 1.仅查看 2.正常操作
/**
判断是否有收回权限的必要条件：1.前一办理人是当前登录人；2.前一节点是当前节点在流程定义中的前一节点；
第二点暂时不做判断
*/
boolean canAgain = false;
if(curUserId.equals(preUserId) || (curUserId+";").equals(preUserId)) {
	canAgain = true;
}

%>
<emp:page>
<html>
<head>
<title>流程实例详情</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<script type="text/javascript">

function doWorkFLow(op) {
	if(!confirm('确定要执行此操作？')) {
		return;
	}
	var form = document.getElementById('submitForm4WF');
	if(op == 'wake') { //唤醒在挂起事项里提供
		form.commentSign.value = '90';
		var returnAction = null;
		if('<%=mng%>'=='1' || '<%=mng%>'=='2') {
			returnAction = '<emp:url action="getWfInstanceList.do"/>?flag=1';
		} else {
			returnAction = '<emp:url action="getHangWorkList.do"/>';
		}
		form.ext.value=returnAction;//存放返回的action
		doWakeWorkFlow(form);
	} else if(op == 'again') { //收回
		form.commentSign.value = '60';
		doAgainWorkFlow(form);
	} else if(op == 'callBack') {
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 start */
		form.commentSign.value = '30';
		var comment = "授信流程进度管理员[somebody]打回!";
		form.commentContent.value = comment;
		doWorkFlowAgent(callBackWorkFlowUnlimit, form);
		doWorkFlowAgent(saveWorkFlow, form);
	/* added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回 end */
	}  else if(op == 'urge') {  //催办
		form.commentSign.value = '31';
		doUrgeWorkFlow(form);
	} else if(op == 'del') {
		doDelWorkFlow(form);
	} else if(op == 'reset') {
		doResetWorkFlow(form);
	} else if(op == 'againFirst') {
		form.commentSign.value = '61';
		doAgainFisrtWorkFlow(form);
	} else if(op == 'hang') {
		form.commentSign.value = '80';
		doWorkFlowAgent(hangWorkFlow, form);
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

</script>

</head>
<body>
<emp:tabGroup mainTab="wfHisTab" id="instanceTab">

<emp:tab label="申请信息" id="wfApproveTab" needFlush="false" url="<%=appUrl %>"/>

<emp:tab label="审批历史" id="wfHisTab" url="getWorkFlowSimHistory.do" reqParams="instanceId=${context.instanceId }&nodeId=${context.nodeId }&serno=${context.pkVal}" needFlush="true"></emp:tab>

</emp:tabGroup>

<div align="center">
	<%if(!"1".equals(optype)) { %>
		<% if(actionVO.isHang()) { %>
			<button id="hang" onclick="doWorkFLow('hang')">挂  起</button>
		<%} %>
		<% if(actionVO.isWake()) { %>
			<button id="wake" onclick="doWorkFLow('wake')">唤  醒</button>
		<%} %>
		<%if("1".equals(mng) || "2".equals(mng)) { %>
			<button id="del" onclick="doWorkFLow('del')">删除实例</button>
			<%if("1".equals(mng)) { %>
				<button id="reset" onclick="doWorkFLow('reset')">重置办理人</button>
			<%	} %>
		<%} else if("3".equals(mng)) { %>	
			<button id="callBack" onclick="doWorkFLow('callBack')">管理员打回</button> <!-- added by yangzy 2014/11/25 XD140718026_新信贷系统授信进度查询改造，流程管理员无条件打回  -->
		<%} else { %>
			<% if(canAgain && actionVO.isAgain()) { %>
				<button id="again" onclick="doWorkFLow('again')">收 回</button>
			<%} %>
			<% if(actionVO.isAgainFirst()) { %>
				<button id="againFirst" onclick="doWorkFLow('againFirst')">追 回</button>
			<%} %>
			<% if(actionVO.isUrge()) { %>
				<button id="urge" onclick="doWorkFLow('urge')">催 办</button>
			<%} %>
		<%} %>
	<%} %>
</div>

</body>
</html>
</emp:page>
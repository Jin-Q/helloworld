<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>已办事项</title>
<jsp:include page="/include.jsp" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiWorklistTodo._toForm(form);
		WfiWorklistTodoList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfiWorklistTodoList._obj.getSelectedData()[0];
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&applType="+applType;
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

	//流程跟踪
	function doMyTrack() {
		if(WfiWorklistTodoList._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=WfiWorklistTodoList._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};

	function doReset() {
		page.dataGroups.WfiWorklistTodoGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfiWorklistTodoGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="WfiWorklistTodo.pk_value" label="业务流水号" />
		<emp:select id="WfiWorklistTodo.appl_type" label="业务类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="WfiWorklistTodo.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:button id="myTrack" label="跟踪" op="track"/>
	
	<emp:table icollName="WfiWorklistTodoList" pageMode="true" url="pageDoneWorkQuery.do" >
		<emp:link id="appl_type" label="业务类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="pk_value" label="业务流水号" operation="sel"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称/项目名称" />
		<emp:text id="prd_name" label="产品名称" />
		<emp:text id="amt" label="申请金额" dataType="Currency" />
		<emp:text id="prenodename" label="上一审批岗" />
		<emp:text id="nodename" label="当前审批岗" />
		<emp:text id="currentnodeuser_displayname" label="当前办理人" />
		<emp:text id="nodestatus" label="节点状态" dictname="WF_NODE_STATUS" hidden="true"/> 
		<emp:text id="nodestarttime" label="业务提交时间"/>
	    <emp:text id="wfname" label="流程名称" hidden="true"/>
	    <emp:text id="wfi_status" label="审批状态" dictname="WF_APP_STATUS" hidden="true"/>
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
	    <emp:text id="prenodeid" label="前一节点ID" hidden="true"/>
	    <emp:text id="nodeid" label="节点ID" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
		<emp:text id="currentnodeuser" label="当前办理人" hidden="true"/>
	</emp:table>
	<form id="form" action="">
		<input id="instanceid" name="instanceid" value="" type="hidden" />
		<input id="currentuserid" name="currentuserid" value="${context.currentUserId}" type="hidden" />
		<input id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}" type="hidden" />
	</form>
</body>
</html>
</emp:page>
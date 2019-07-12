<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>待办事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {

	};

	function doQuery(){
		debugger;
		var form = document.getElementById('queryForm');
		WfiWorklistTodo._toForm(form);
		WfiWorklistTodoList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfiWorklistTodoList._obj.getSelectedData()[0];
		var nodeid=selObj.nodeid._getValue();
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&nodeId="+nodeid+"&applType="+applType;
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

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
	<emp:gridLayout id="WfiWorklistTodoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiWorklistTodo.pk_value" label="申请流水号" />
		<emp:text id="WfiWorklistTodo.cus_name" label="客户名称" />
		<emp:text id="WfiWorklistTodo.appl_type" label="流程申请类型" hidden="" defvalue="${context.WfiWorklistTodo.appl_type }"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:table icollName="WfiWorklistTodoList" pageMode="true" url="pageToDoWorkQuery.do" reqParams="WfiWorklistTodo.appl_type=${context.WfiWorklistTodo.appl_type }">
		<emp:link id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="pk_value" label="申请流水号" operation="sel"/>
		<emp:link id="cus_name" label="客户名称" operation="sel"/>
		<emp:link id="prenodename" label="前一节点" operation="sel"/>
		<emp:link id="nodename" label="当前节点" operation="sel"/>
		<emp:link id="currentnodeuser_displayname" label="当前办理人" operation="sel"/>
		<emp:link id="nodestatus" label="节点状态" dictname="WF_NODE_STATUS" operation="sel"/>
		<emp:link id="nodestarttime" label="节点开始时间" operation="sel"/>
	    <emp:link id="wfname" label="流程名称" operation="sel"/>
	    <emp:link id="wfi_status" label="审批状态" operation="sel" dictname="WF_APP_STATUS"/>
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
	    <emp:text id="prenodeid" label="前一节点ID" hidden="true"/>
	    <emp:text id="nodeid" label="节点ID" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
		<emp:text id="currentnodeuser" label="当前办理人" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
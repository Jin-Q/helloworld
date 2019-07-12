<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>项目池任务列表</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiWorklistTodo._toForm(form);
		WfiWorklistTodoList._obj.ajaxQuery(null,form);
	};

	function doTaskSignIn() {
		if(confirm("您确定认领该任务吗？任务认领后将进入个人待办事项！")){
			var handleSuccess = function(o){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 1) {
						alert('认领成功！');
						window.location.reload();
					} else {
						alert('认领失败！'+flag);
					}
				}catch(e) {
					alert('认领异常！'+o.responseText);
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
			var postData = null;
			var selObj = WfiWorklistTodoList._obj.getSelectedData()[0];
			var instanceid=selObj.instanceid._getValue();
			var url = '<emp:url action="taskSignIn.do"/>?instanceId='+instanceid;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback,postData);
		}
	}

</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfiWorklistTodoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiWorklistTodo.pk_value" label="申请流水号" />
		<emp:select id="WfiWorklistTodo.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="WfiWorklistTodo.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:button label="认领" id="taskSignIn"></emp:button>
	<emp:table icollName="WfiWorklistTodoList" pageMode="true" url="pageToDoTaskPoolQuery.do" reqParams="tpid=${context.tpid }">
		<emp:text id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="pk_value" label="申请流水号" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prenodename" label="前一节点" />
		<emp:text id="nodename" label="当前节点" />
		<emp:text id="nodestatus" label="节点状态" dictname="WF_NODE_STATUS" />
		<emp:text id="nodestarttime" label="节点开始时间" />
		<emp:text id="currentnodeuser" label="当前办理人" />
	    <emp:text id="wfname" label="流程名称" />
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
	    <emp:text id="prenodeid" label="前一节点ID" hidden="true"/>
	    <emp:text id="nodeid" label="节点ID" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
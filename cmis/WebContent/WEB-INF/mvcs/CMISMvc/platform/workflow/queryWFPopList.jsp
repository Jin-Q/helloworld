<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WorkFlow._toForm(form);
		WorkFlowList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.WorkFlowGroup.reset();
	};
	
	/*--user code begin--*/
			
	function doSelect() {
		var data = WorkFlowList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WorkFlowGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WorkFlow.wfsign" label="流程标识" />
			<emp:text id="WorkFlow.wfname" label="流程名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选取返回" op=""/>
	</div>

	<emp:table icollName="WorkFlowList" pageMode="true" url="pageWFListQuery.do">
		<emp:text id="wfid" label="流程ID" />
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfname" label="流程名称" />
		<emp:text id="wfver" label="版本" />
	</emp:table>
	
</body>
</html>
</emp:page>
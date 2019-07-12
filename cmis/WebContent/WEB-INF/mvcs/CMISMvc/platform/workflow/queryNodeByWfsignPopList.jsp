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
		WorkFlowNode._toForm(form);
		WorkFlowNodeList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.WorkFlowNodeGroup.reset();
	};
	
	/*--user code begin--*/
			
	function doSelect() {
		var data = WorkFlowNodeList._obj.getSelectedData();
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

	<emp:gridLayout id="WorkFlowNodeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WorkFlowNode.nodeid" label="节点ID" />
			<emp:text id="WorkFlowNode.nodename" label="节点名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选取返回" op=""/>
	</div>

	<emp:table icollName="WorkFlowNodeList" pageMode="false" url="pageNodeByWfsignQuery.do">
		<emp:text id="nodeid" label="节点ID" />
		<emp:text id="nodename" label="节点名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
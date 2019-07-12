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
		WfClient._toForm(form);
		WfClientList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.WfClientGroup.reset();
	};
	
	function doSelect() {
		var data = WfClientList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<br>	
	<div align="left">
		<emp:button id="select" label="选取返回" />
	</div>

	<emp:table icollName="WfClientList" pageMode="false" url="">
		<emp:text id="clientsign" label="客户端标识" />
		<emp:text id="clientname" label="客户端名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
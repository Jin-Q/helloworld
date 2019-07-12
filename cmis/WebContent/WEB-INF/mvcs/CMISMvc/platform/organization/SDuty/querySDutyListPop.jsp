<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>岗位POP</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SDuty._toForm(form);
		SDutyList._obj.ajaxQuery(null,form);
	};
	
	
	/*--user code begin--*/
	function doCancel(){
		window.close();
	};	
	function doSelectreturn(){
		var data = SDutyList._obj.getSelectedData();
		methodName = "${context.returnMethod}";
		if (data==null||data.length==0) {
			alert('请先选择一条记录！');
			return;
		}
		top.opener[methodName](data[0]);
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="SDutyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SDuty.dutyno" label="岗位码" />
			<emp:text id="SDuty.dutyname" label="岗位名称" />
			<emp:text id="SDuty.organno" label="机构码" />
			<emp:text id="SDuty.depno" label="部门码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:table icollName="SDutyList" pageMode="true" url="pageSDutyQuery.do">
		<emp:text id="dutyno" label="岗位码" />
		<emp:text id="dutyname" label="岗位名称" />
		<emp:text id="organno" label="机构码" />
		<emp:text id="depno" label="部门码" />
	</emp:table>
	<div align="left">
			<br>
			<emp:button id="selectreturn" label="选取并返回" />
			<emp:button id="cancel" label="重置" />
	</div>
</body>
</html>
</emp:page>
    
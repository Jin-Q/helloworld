<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>角色POP</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SRole._toForm(form);
		SRoleList._obj.ajaxQuery(null,form);
	};
	
	/*--user code begin--*/
	function doSelectreturn(){
		var data = SRoleList._obj.getSelectedData();
		methodName = "${context.returnMethod}";
		if (data==null||data.length==0) {
			alert('请先选择一条记录！');
			return;
		}
		top.opener[methodName](data[0]);
		window.close();
	};
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:table icollName="SRoleList" pageMode="true" url="pageSRoleQuery.do">
		<emp:text id="roleno" label="角色码" />
		<emp:text id="rolename" label="角色名称" />
	</emp:table>
	<div align="left">
			<br>
			<emp:button id="selectreturn" label="选取并返回" />
			<emp:button id="cancel" label="重置" />
	</div>
</body>
</html>
</emp:page>
    
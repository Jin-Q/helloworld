<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>表单选取pop页面</title>
<style type="text/css">
 .emp_field_width {
	border: 1px solid #b7b7b7;
	text-align: left;
	width:210px
}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');	
		SResource._toForm(form);
		SResourceList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.SResourceGroup.reset();
	};

	function doSelect(){
		var data = SResourceList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		window.opener["${context.returnMethod}"](data[0]);
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SResourceGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SResource.resourceid" label="TAB标签页资源ID" />
			<emp:text id="SResource.cnname" label="TAB标签页资源名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<div align="left">
		<emp:button id="select" label="选取返回"/>
	</div>
	</div>

	<emp:table icollName="SResourceList" pageMode="true" url="pageTabResourceList.do">
		<emp:text id="resourceid" label="TAB标签页资源ID" />
		<emp:text id="cnname" label="TAB标签页资源名称" />
	</emp:table>
	
</body>
</html>
</emp:page>


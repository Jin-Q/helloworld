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
		FncProject._toForm(form);
		FncProjectList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncProjectPage() {
		var paramStr = FncProjectList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncProjectUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncProject() {
		var paramStr = FncProjectList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncProjectViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncProjectPage() {
		var url = '<emp:url action="getFncProjectAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncProject() {
		var paramStr = FncProjectList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncProjectRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncProjectGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncProjectGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncProject.fnc_prj_name" label="项目名称" />
			<emp:text id="FncProject.fnc_const_loc" label="施工地点" />
			<emp:text id="FncProject.fnc_const_depnt" label="施工单位" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncProjectPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncProjectPage" label="修改" op="update"/>
		<emp:button id="deleteFncProject" label="删除" op="remove"/>
		<emp:button id="viewFncProject" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncProjectList" pageMode="true" url="pageFncProjectQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="fnc_prj_name" label="项目名称" />
		<emp:text id="fnc_const_loc" label="施工地点" />
		<emp:text id="fnc_const_depnt" label="施工单位" />
		<emp:text id="fnc_invt_amt" label="投入金额" />
		<emp:text id="fnc_invt_amted" label="已完成投资额" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="pk_id" label="主键" hidden="true" />

	</emp:table>
	
</body>
</html>
</emp:page>
    
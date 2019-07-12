<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<style type="text/css">
		.emp_field_longtext_input { 
		/****** 长度固定 ******/
			width: 250px;
			border-width: 1px;
			border-color: #b7b7b7;
			border-style: solid;
			text-align: left;
		}
	</style>
	<script type="text/javascript">
	function doQuery() {
		var form = document.getElementById('queryForm');
		WfiOrgCbRel._toForm(form);
		WfiOrgCbRelList._obj.ajaxQuery(null, form);
	};

	function doReset() {
		page.dataGroups.WfiOrgCbRelGroup.reset();
	};

	function doSelect(){
		var data = WfiOrgCbRelList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.popReturnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};

</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm">
	<emp:gridLayout id="WfiOrgCbRelGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiOrgCbRel.comm_branch_id" label="社区支行编码" />
		<emp:text id="WfiOrgCbRel.comm_branch_name" label="社区支行名称" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="WfiOrgCbRelList" pageMode="true" url="pageCommBranchPopQuery.do">
	    <emp:text id="comm_branch_id" label="社区支行编码" />
		<emp:text id="comm_branch_name" label="社区支行名称" />
		<emp:text id="org_id" label="机构码" />
		<emp:text id="org_id_displayname" label="机构名称" />
	</emp:table>

	<div ><br>
	<button onclick="doSelect()">选取返回</button>
	<br>
	</div>
	</form>
	</body>
	</html>
</emp:page>

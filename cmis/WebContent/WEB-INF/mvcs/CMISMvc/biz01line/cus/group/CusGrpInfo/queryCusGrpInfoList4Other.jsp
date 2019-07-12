<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doGetAddCusGrpInfoPage(){
		var url = '<emp:url action="getCusGrpInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteCusGrpInfo(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no','parent_cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusGrpInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdateCusGrpInfoPage(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusGrpInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusGrpInfo(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusGrpInfoDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		var grpNo = CusGrpInfo.grp_no._obj.element.value;
		var grpName = CusGrpInfo.grp_name._obj.element.value;
		if(grpNo== '' && grpName == ''){
			alert("请至少输入一个查询条件!");
			return;
		}
		CusGrpInfo._toForm(form);
		CusGrpInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.CusGrpInfoGroup.reset();
	};

	/*--user code begin--*/

	/*--user code end--*/

</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGrpInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" />
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddCusGrpInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusGrpInfoPage" label="修改" op="update"/>
		<emp:button id="deleteCusGrpInfo" label="删除" op="remove"/>
		<emp:button id="viewCusGrpInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="CusGrpInfoList" pageMode="true" url="pageCusGrpInfo4OtherQuery.do">
		<emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主申请(集团)客户码" />
		<emp:text id="parent_cus_name" label="主申请(集团)名称" />
		<emp:text id="cus_manager_displayname" label="主办客户经理" />
		<emp:text id="main_br_id_displayname" label="主办行" />
		<emp:text id="cus_manager" label="主办客户经理" hidden="true"/>
		<emp:text id="main_br_id" label="主办行" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
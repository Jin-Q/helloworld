<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncConfItems._toForm(form);
		FncConfItemsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncConfItemsPage() {
		var paramStr = FncConfItemsList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfItemsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncConfItems() {
		var paramStr = FncConfItemsList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfItemsViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncConfItemsPage() {
		var url = '<emp:url action="getFncConfItemsAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncConfItems() {
		var paramStr = FncConfItemsList._obj.getParamStr(['item_id','item_name']);
		if (paramStr != null) {
			if(confirm("警告！：在删除该项目前，应确保系统中每个模块都没有引用该指标，如有引用，要先删除！\n\n"
					+ "否则，会影响现有报表数据的平衡和准确性，是否确定删除？\n\n"
					+ "点击“确定”继续完成删除操作，点击“重置”，重置删除操作"
					)){
				var url = '<emp:url action="deleteFncConfItemsRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncConfItemsGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncConfItemsGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncConfItems.item_id" label="项目编号" />
			<emp:text id="FncConfItems.item_name" label="项目名称" />
			<emp:select id="FncConfItems.fnc_conf_typ" label="所属报表种类" dictname="STD_ZB_FNC_TYP"/>
			<emp:select id="FncConfItems.fnc_no_flg" label="新旧报表标志" dictname="STD_ZB_FNC_ON_TYP"/>  
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncConfItemsPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncConfItemsPage" label="修改" op="update"/>
		<emp:button id="deleteFncConfItems" label="删除" op="remove"/>
		<emp:button id="viewFncConfItems" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncConfItemsList" pageMode="true" url="pageFncConfItemsQuery.do">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:select id="fnc_conf_typ" label="所属报表种类" dictname="STD_ZB_FNC_TYP"/>
		<emp:select id="fnc_no_flg" label="新旧报表标志" dictname="STD_ZB_FNC_ON_TYP"/>
		<emp:text id="remark" label="备注" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
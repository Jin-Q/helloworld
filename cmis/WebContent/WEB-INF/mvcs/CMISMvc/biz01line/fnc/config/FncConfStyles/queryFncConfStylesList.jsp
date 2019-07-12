<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" />


<script>

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncConfStyles._toForm(form);
		FncConfStylesList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncConfStylesPage() {
		var paramStr = FncConfStylesList._obj.getParamStr(['style_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfStylesUpdatePage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncConfStyles() {
		var paramStr = FncConfStylesList._obj.getParamStr(['style_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfStylesViewPage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doCloneFncConfStyles() {
		var paramStr = FncConfStylesList._obj.getParamStr(['style_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfStylesClonePage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncConfStylesPage() {
		var url = '<emp:url action="getFncConfStylesAddPage.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncConfStyles() {
		var paramStr = FncConfStylesList._obj.getParamStr(['style_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncConfStylesRecord.do"/>?'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncConfStylesGroup.reset();
	};
</script>
</head>
<body class="page_content"  >
	<form  method="POST" action="#" id="queryForm">
	</form>

		<emp:gridLayout id="FncConfStylesGroup" maxColumn="2" title="输入查询条件">
			<emp:text id="FncConfStyles.style_id" label="报表样式编号" />
			<emp:text id="FncConfStyles.fnc_conf_dis_name" label="显示名称" />
		</emp:gridLayout>
		<jsp:include page="/queryInclude.jsp" />
	
	<div align="left">
		<emp:button id="getAddFncConfStylesPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncConfStylesPage" label="修改" op="update"/>
		<emp:button id="deleteFncConfStyles" label="删除" op="remove"/>
		<emp:button id="viewFncConfStyles" label="查看" op="view"/>
		<emp:button id="cloneFncConfStyles" label="复制" op="view"/>
	</div>

	<emp:table icollName="FncConfStylesList" pageMode="true" url="pageFncConfStylesQuery.do">
		<emp:text id="style_id" label="报表样式编号" />
		<emp:text id="fnc_name" label="报表名称" />
		<emp:text id="fnc_conf_dis_name" label="显示名称" />
		<emp:text id="fnc_conf_typ" label="所属报表种类" dictname="STD_ZB_FNC_TYP" />
		<emp:text id="fnc_conf_data_col" label="数据列数" dictname="STD_ZB_FNC_COL"/>
		<emp:text id="fnc_conf_cotes" label="栏位" dictname="STD_ZB_FNC_COTES"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    
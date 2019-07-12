<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%
  String styleId = (String)request.getParameter("style_id");
%>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" />


<script>
	
	var page = new EMP.util.Page();

	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncConfStyles._toForm(form);
		FncConfStylesList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncConfDefFormatPage() {
		//var paramStr = FncConfDefFmtList._obj.getParamStr(['style_id','item_id']);
		var paramStr = FncConfStylesList._obj.getParamStr(['style_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfDefFormatUpdatePage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncConfDefFormat() {
		var paramStr = FncConfDefFmtList._obj.getParamStr(['style_id','item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncConfDefFormatViewPage.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetDealFncConfDefFormatPage() {
	    var paramStr = FncConfStylesList._obj.getParamStr(['style_id','fnc_conf_typ']);
	    if (paramStr != null) {
			var url = '<emp:url action="dealFncConfDefFormat.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doDeleteFncConfDefFormat() {
		var paramStr = FncConfDefFmtList._obj.getParamStr(['style_id','item_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncConfDefFormatRecord.do"/>?'+paramStr;
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
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="FncConfStylesGroup" maxColumn="2" title="输入查询条件">
		<emp:text id="FncConfStyles.style_id" label="报表样式编号" />
		<emp:text id="FncConfStyles.fnc_conf_dis_name" label="显示名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" />
	<!--<emp:button id="getUpdateFncConfDefFormatPage" label="修改"/>-->
	<emp:table icollName="FncConfStylesList" pageMode="true" url="pageFncConfStylesQuery.do">
		<emp:link id="style_id" label="报表样式编号" operation="getDealFncConfDefFormatPage"/>
		<emp:text id="fnc_name" label="报表名称" />
		<emp:text id="fnc_conf_dis_name" label="显示名称" />
		<emp:text id="fnc_conf_typ" label="所属报表种类" dictname="STD_ZB_FNC_TYP" />
		<emp:text id="fnc_conf_data_col" label="数据列数" />
		<emp:text id="fnc_conf_cotes" label="栏位" />
	</emp:table>
	
</body>
</html>
</emp:page>
    
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
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
		s_resourceaction._toForm(form);
		s_resourceactionList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdates_resourceactionPage() {
		var paramStr = s_resourceactionList._obj.getParamStr(['resourceid','actid']);
		if (paramStr!=null) {
			var url = '<emp:url action="getEditActPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = s_resourceactionList._obj.getParamStr(['resourceid']);
		if (paramStr != null) {
			var url = '<emp:url action="getViewPage__s_resourceaction.do"/>?'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAdds_resourceactionPage() {
		var resid=document.getElementById("resourceid").value;
		var url = "<emp:url action='getAddActionPage.do'/>&s_resourceaction.resourceid="+resid;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeletes_resourceaction() {
		var paramStr = s_resourceactionList._obj.getParamStr(['resourceid','actid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="delAction.do"/>?'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.s_resourceactionGroup.reset();
	};
</script>
</head>
<body class="page_content"  onload="doOnLoad()">
<emp:text id="resourceid" label="resourceid" hidden="true"/>
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:button id="getAdds_resourceactionPage" label="新增"/>
		<emp:button id="getUpdates_resourceactionPage" label="修改"/>
		<emp:button id="deletes_resourceaction" label="删除"/>
	</div>

	<emp:table icollName="s_resourceactionList" pageMode="false" url="pageQuery__s_resourceaction.do">

		<emp:text id="resourceid" label="资源ID" hidden="true"/>
		<emp:text id="actid" label="操作ID" />
		<emp:text id="descr" label="描述" />
	
	</emp:table>
	
</body>
</html>
</emp:page>
    